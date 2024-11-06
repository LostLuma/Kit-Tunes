package net.pixaurora.kit_tunes.api.event;

import java.util.Optional;

import net.pixaurora.kit_tunes.api.music.ListeningProgress;
import net.pixaurora.kit_tunes.api.music.Track;
import net.pixaurora.kit_tunes.api.resource.ResourcePath;

public interface TrackMiddleEvent {
    public Optional<Track> track();

    public ResourcePath searchPath();

    public ListeningProgress progress();
}
