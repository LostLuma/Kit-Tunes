package net.pixaurora.kit_tunes.impl.music;

import java.util.Optional;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record AlbumTrack(Track trackInfo, Optional<Album> album) {
	public AlbumTrack(ResourceLocation path) {
		this(new Track(path, Optional.empty()), Optional.empty());
	}

	public ResourceLocation path() {
		return this.trackInfo.path();
	}

	public Optional<String> artistKey() {
		return this.trackInfo.artistKey();
	}

	public MutableComponent title() {
		String path = this.path().toString();
		return Component.translatableWithFallback(
			"kit_tunes.track." + path.replaceAll(":|/", "."),
			MusicPathConverter.titleCase(path.substring(path.lastIndexOf("/") + 1))
		);
	}

	public MutableComponent artist() {
		return Component.translatable("kit_tunes.artist." + this.artistKey().orElse("unknown"));
	}

	public Optional<MutableComponent> albumTitle() {
		return this.album.map(album -> Component.translatable("kit_tunes.album." + album.key()));
	}
}
