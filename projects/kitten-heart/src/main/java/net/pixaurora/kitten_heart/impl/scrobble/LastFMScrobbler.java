package net.pixaurora.kitten_heart.impl.scrobble;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.pixaurora.catculator.api.error.ClientResponseException;
import net.pixaurora.catculator.api.http.RequestBuilder;
import net.pixaurora.catculator.api.http.Response;
import net.pixaurora.kitten_heart.impl.KitTunes;
import net.pixaurora.kitten_heart.impl.error.UnhandledKitTunesException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.pixaurora.kitten_heart.impl.error.KitTunesException;
import net.pixaurora.kitten_heart.impl.error.ScrobblerParsingException;
import net.pixaurora.kitten_heart.impl.network.Encryption;
import net.pixaurora.kitten_heart.impl.network.XMLHelper;

public class LastFMScrobbler implements Scrobbler {
    public static final String API_KEY = "6f9e533b5f6631a5aa3070f5e757de8c";
    public static final String SHARED_SECRET = "97fbf9a3d76ba36dfb5a2f6c3215bf49";

    public static final String ROOT_API_URL = "https://ws.audioscrobbler.com/2.0/";
    public static final String SETUP_URL = "https://last.fm/api/auth?api_key=" + API_KEY;

    public static final ScrobblerType<LastFMScrobbler> TYPE = new ScrobblerType<>("last.fm.new", LastFMScrobbler.class,
            SETUP_URL, LastFMScrobbler::setup);

    private final LastFMSession session;

    public LastFMScrobbler(LastFMSession session) {
        this.session = session;
    }

    public static LastFMScrobbler setup(String token) throws KitTunesException {
        return new LastFMScrobbler(createSession(token));
    }

    @Override
    public String username() {
        return this.session.name;
    }

    @Override
    public void startScrobbling(ScrobbleInfo track) throws KitTunesException {
        Map<String, String> query = new HashMap<>();

        query.put("method", "track.updateNowPlaying");

        query.put("artist", track.artistTitle());
        query.put("track", track.trackTitle());
        query.put("api_key", this.apiKey());
        query.put("sk", this.session.key);

        if (track.albumTitle().isPresent()) {
            query.put("album", track.albumTitle().get());
        }

        this.submitScrobble(this.addSignature(query));
    }

    @Override
    public void completeScrobbling(ScrobbleInfo track) throws KitTunesException {
        Map<String, String> query = new HashMap<>();

        query.put("method", "track.scrobble");

        query.put("artist", track.artistTitle());
        query.put("track", track.trackTitle());
        query.put("timestamp", String.valueOf(track.startTime().getEpochSecond()));
        query.put("api_key", this.apiKey());
        query.put("sk", this.session.key);

        Optional<String> albumTitle = track.albumTitle();
        if (albumTitle.isPresent()) {
            query.put("album", albumTitle.get());
        }

        this.submitScrobble(this.addSignature(query));
    }

    private void submitScrobble(Map<String, String> query) throws KitTunesException {
        RequestBuilder builder = KitTunes.CLIENT.post(ROOT_API_URL);

        for (Map.Entry<String, String> entry : query.entrySet()) {
            builder.query(entry.getKey(), entry.getValue());
        }

        Response response = null;

        try {
            response = builder.send();
        } catch (ClientResponseException e) {
            KitTunes.LOGGER.error("Failed to submit scrobble.", e);
        }

        if (response != null) {
            String message = new String(response.body(), StandardCharsets.UTF_8);
            KitTunes.LOGGER.info("Received {} with body {}.", response.status(), message);
        }
    }

    private Map<String, String> addSignature(Map<String, String> parameters) {
        return addSignature(parameters, this.sharedSecret());
    }

    private static Map<String, String> addSignature(Map<String, String> parameters, String sharedSecret) {
        List<Map.Entry<String, String>> sortedParameters = parameters.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey())).collect(Collectors.toList());

        String regularSignature = "";
        for (Map.Entry<String, String> parameter : sortedParameters) {
            regularSignature += parameter.getKey() + parameter.getValue();
        }

        regularSignature += sharedSecret;

        parameters = new HashMap<>(parameters);
        parameters.put("api_sig", Encryption.signMd5(regularSignature));

        return parameters;
    }

    private static LastFMSession createSession(String token) throws ScrobblerParsingException {
        Map<String, String> query = new HashMap<>();

        query.put("method", "auth.getSession");
        query.put("api_key", API_KEY);
        query.put("token", token);

        RequestBuilder builder = KitTunes.CLIENT.get(ROOT_API_URL);

        for (Map.Entry<String, String> entry : addSignature(query, SHARED_SECRET).entrySet()) {
            builder.query(entry.getKey(), entry.getValue());
        }

        Response response = null;

        try {
            response = builder.send();
        } catch (ClientResponseException e) {
            throw new UnhandledKitTunesException(e);
        }

        if (response == null || !response.ok()) {
            throw new UnhandledKitTunesException("Response not ok");
        } else {
            InputStream stream = new ByteArrayInputStream(response.body());

            Document body = XMLHelper.getDocument(stream);
            Node root = XMLHelper.requireChild("lfm", body);

            return LastFMSession.fromXML("session", root);
        }
    }

    @Override
    public ScrobblerType<?> type() {
        return TYPE;
    }

    protected String apiKey() {
        return API_KEY;
    }

    protected String sharedSecret() {
        return SHARED_SECRET;
    }

    public static class LastFMSession {
        private final String name;
        private final String key;
        @SuppressWarnings("unused")
        private final int subscriber;

        public LastFMSession(String name, String key, int subscriber) {
            this.name = name;
            this.key = key;
            this.subscriber = subscriber;
        }

        public static LastFMSession fromXML(String name, Node parent) throws ScrobblerParsingException {
            Node session = XMLHelper.requireChild(name, parent);

            String username = XMLHelper.requireString("name", session);
            String key = XMLHelper.requireString("key", session);
            int subscriber = XMLHelper.requireInt("subscriber", session);

            return new LastFMSession(username, key, subscriber);
        }
    }
}
