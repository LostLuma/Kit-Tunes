package net.pixaurora.kitten_heart.impl.music.metadata;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.pixaurora.kit_tunes.api.music.Album;
import net.pixaurora.kit_tunes.api.music.Artist;
import net.pixaurora.kit_tunes.api.music.Track;
import net.pixaurora.kit_tunes.api.resource.ResourcePath;
import net.pixaurora.kitten_heart.impl.service.MusicMetadataService;

public class MusicMetadataImpl implements MusicMetadataService, MutableMusicMetadata {
    private final Map<ResourcePath, Album> albums = new HashMap<>();
    private final Map<ResourcePath, Artist> artists = new HashMap<>();
    private final Map<ResourcePath, Track> tracks = new HashMap<>();

    private final HashMap<String, Track> trackMatches = new HashMap<>();
    private final HashMap<ResourcePath, List<Album>> trackToAlbums = new HashMap<>();

    private final HashMap<ResourcePath, Duration> trackDurations = new HashMap<>();

    @Override
    public void add(Album album) {
        this.albums.put(album.path(), album);

        for (Track track : album.tracks()) {
            this.albumsWithTrack(track).add(album);
        }
    }

    @Override
    public void add(Artist artist) {
        this.artists.put(artist.path(), artist);
    }

    @Override
    public void add(Track track) {
        this.tracks.put(track.path(), track);

        for (String trackMatch : track.matches()) {
            this.trackMatches.put(trackMatch, track);
        }
    }

    @Override
    public void giveDuration(Track track, Duration duration) {
        this.trackDurations.put(track.path(), duration);
    }

    @Override
    public void load(List<Path> albumFiles, List<Path> artistFiles, List<Path> trackFiles) {
        MusicMetadataLoader.load(this, albumFiles, artistFiles, trackFiles);
    }

    @Override
    public Optional<Album> getAlbum(ResourcePath path) {
        return Optional.ofNullable(this.albums.get(path));
    }

    @Override
    public Optional<Artist> getArtist(ResourcePath path) {
        return Optional.ofNullable(this.artists.get(path));
    }

    @Override
    public Optional<Track> getTrack(ResourcePath path) {
        return Optional.ofNullable(this.tracks.get(path));
    }

    @Override
    public Optional<Track> matchTrack(ResourcePath soundPath) {
        String[] splitPath = soundPath.representation().split("/");
        String filename = splitPath[splitPath.length - 1];

        int lastFullStop = filename.lastIndexOf(".");
        String extensionlessFilename = lastFullStop != -1 ? filename.substring(0, lastFullStop) : filename;

        return Optional.ofNullable(trackMatches.get(extensionlessFilename));
    }

    @Override
    public List<Album> albumsWithTrack(Track track) {
        return trackToAlbums.computeIfAbsent(track.path(), path -> new ArrayList<>());
    }

    @Override
    public Duration trackDuration(Track track) {
        Optional<Duration> duration = Optional.ofNullable(this.trackDurations.get(track.path()));

        if (duration.isPresent()) {
            return duration.get();
        } else {
            throw new RuntimeException("Track duration has not been initialized for Track `" + track.path() + "`!");
        }
    }
}
