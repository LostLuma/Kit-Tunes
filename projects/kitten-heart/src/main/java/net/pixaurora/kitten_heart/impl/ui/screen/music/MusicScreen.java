package net.pixaurora.kitten_heart.impl.ui.screen.music;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.pixaurora.kit_tunes.api.music.Album;
import net.pixaurora.kit_tunes.api.music.Track;
import net.pixaurora.kit_tunes.api.resource.ResourcePath;
import net.pixaurora.kitten_cube.impl.math.Point;
import net.pixaurora.kitten_cube.impl.math.Size;
import net.pixaurora.kitten_cube.impl.text.Color;
import net.pixaurora.kitten_cube.impl.text.Component;
import net.pixaurora.kitten_cube.impl.ui.screen.Screen;
import net.pixaurora.kitten_cube.impl.ui.screen.WidgetContainer;
import net.pixaurora.kitten_cube.impl.ui.screen.align.Alignment;
import net.pixaurora.kitten_cube.impl.ui.screen.align.WidgetAnchor;
import net.pixaurora.kitten_cube.impl.ui.texture.GuiTexture;
import net.pixaurora.kitten_cube.impl.ui.texture.Texture;
import net.pixaurora.kitten_cube.impl.ui.widget.StaticTexture;
import net.pixaurora.kitten_cube.impl.ui.widget.text.PushableTextLines;
import net.pixaurora.kitten_heart.impl.EventHandling;
import net.pixaurora.kitten_heart.impl.KitTunes;
import net.pixaurora.kitten_heart.impl.music.control.MusicControls;
import net.pixaurora.kitten_heart.impl.music.control.PlaybackState;
import net.pixaurora.kitten_heart.impl.music.progress.PlayingSong;
import net.pixaurora.kitten_heart.impl.ui.screen.KitTunesScreenTemplate;
import net.pixaurora.kitten_heart.impl.ui.widget.PauseButton;
import net.pixaurora.kitten_heart.impl.ui.widget.Timer;
import net.pixaurora.kitten_heart.impl.ui.widget.progress.ProgressBar;
import net.pixaurora.kitten_heart.impl.ui.widget.progress.ProgressBarTileSet;
import net.pixaurora.kitten_heart.impl.ui.widget.progress.ProgressBarTileSets;

import static net.pixaurora.kitten_heart.impl.music.metadata.MusicMetadata.asComponent;

public class MusicScreen extends KitTunesScreenTemplate {
    private static final ProgressBarTileSet FILLED_TILE_SET = tileSet(
            KitTunes.resource("textures/gui/sprites/widget/music/progress_bar/filled.png"));
    private static final ProgressBarTileSet EMPTY_TILE_SET = tileSet(
            KitTunes.resource("textures/gui/sprites/widget/music/progress_bar/empty.png"));

    private static final ResourcePath DEFAULT_ALBUM_ART = KitTunes.resource("textures/icon.png");

    private static final ProgressBarTileSets PLAYING_SONG_TILE_SET = new ProgressBarTileSets(EMPTY_TILE_SET,
            FILLED_TILE_SET);

    Optional<DisplayMode> mode;

    public MusicScreen(Screen previous) {
        super(previous);

        this.mode = Optional.empty();
    }

    @Override
    protected Alignment alignmentMethod() {
        return Alignment.CENTER;
    }

    @Override
    protected void firstInit() {
        this.setupMode();
    }

    @Override
    public void tick() {
        if (!this.mode.isPresent()) {
            return;
        }

        DisplayMode mode = this.mode.get();

        if (!mode.isActive()) {
            mode.cleanup();

            this.setupMode();
        }
    }

    private void setupMode() {
        Optional<PlayingSong> progress = EventHandling.playingSongs().stream().findFirst();

        DisplayMode mode = progress.isPresent() ? this.createMusicDisplay(progress.get()) : this.createWaitingDisplay();
        this.mode = Optional.of(mode);
    }

    private static ProgressBarTileSet tileSet(ResourcePath texturePath) {
        return ProgressBarTileSet.create(
                GuiTexture.of(texturePath, Size.of(12, 4)),
                Point.ZERO, Size.of(4, 4), Point.of(4, 0), Size.of(4, 4), Point.of(8, 0), Size.of(4, 4));
    }

    private static interface DisplayMode {
        boolean isActive();

        void cleanup();
    }

    public DisplayMode createMusicDisplay(PlayingSong song) {
        WidgetContainer<ProgressBar> progressBar = this
                .addWidget(new ProgressBar(Point.of(0, -24), song, PLAYING_SONG_TILE_SET))
                .customizedAlignment(Alignment.CENTER_BOTTOM);

        WidgetContainer<Timer> timer = this.addWidget(new Timer(Point.of(0, -13), song))
                .customizedAlignment(Alignment.CENTER_BOTTOM);

        Optional<Album> album = song.track().flatMap(Track::album);

        ResourcePath albumArtTexture = album
                .flatMap(Album::albumArtPath)
                .orElse(DEFAULT_ALBUM_ART);
        Size iconSize = Size.of(128, 128);
        WidgetContainer<StaticTexture> albumArt = this
                .addWidget(
                        new StaticTexture(Texture.of(albumArtTexture, iconSize), iconSize.centerOn(Point.of(-70, 0))));

        WidgetContainer<PushableTextLines> songInfo = this.addWidget(PushableTextLines.regular(Point.of(70, -8)));

        if (song.track().isPresent()) {
            Track track = song.track().get();

            songInfo.get().push(asComponent(track), Color.WHITE);
            songInfo.get().push(asComponent(track.artist()), Color.WHITE);
            if (album.isPresent()) {
                songInfo.get().push(asComponent(track.album().get()), Color.WHITE);
            }
        } else {
            songInfo.get().push(Component.literal("No track found :("), Color.RED);
        }

        WidgetContainer<PauseButton> pauseButton = this
                .addWidget(
                        new PauseButton(
                                () -> song.controls().playbackState(),
                                (button) -> {
                                    MusicControls controls = song.controls();

                                    PlaybackState state = controls.playbackState();

                                    if (state == PlaybackState.PAUSED) {
                                        controls.unpause();
                                    } else if (state == PlaybackState.PLAYING) {
                                        controls.pause();
                                    }
                                },
                                Point.of(0, 0)))
                .customizedAlignment(progressBar.relativeAlignment(WidgetAnchor.BOTTOM_LEFT));

        return new MusicDisplayMode(song, Arrays.asList(progressBar, timer, albumArt, songInfo, pauseButton));
    }

    public DisplayMode createWaitingDisplay() {
        return new WaitingDisplayMode();
    }

    private class MusicDisplayMode implements DisplayMode {
        private final PlayingSong song;
        private final List<WidgetContainer<?>> widgets;

        MusicDisplayMode(PlayingSong song, List<WidgetContainer<?>> widgets) {
            this.song = song;
            this.widgets = widgets;
        }

        @Override
        public boolean isActive() {
            return EventHandling.isTracking(song.progress());
        }

        @Override
        public void cleanup() {
            for (WidgetContainer<?> widget : this.widgets) {
                MusicScreen.this.removeWidget(widget);
            }
        }
    }

    private class WaitingDisplayMode implements DisplayMode {
        @Override
        public boolean isActive() {
            return !EventHandling.isTrackingAnything();
        }

        @Override
        public void cleanup() {

        }
    }
}
