package net.pixaurora.kitten_heart.impl;

import java.nio.file.Path;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.ModMetadata;
import org.quiltmc.loader.api.QuiltLoader;

public abstract class Constants {
    public static final String MOD_ID = "kit_tunes";
    public static final String MOD_VERSION;
    public static final String HOMEPAGE;

    private static final Path CACHE_PATH = QuiltLoader.getCacheDir().resolve(Constants.MOD_ID);
    public static final Path SCROBBLER_CACHE_PATH = CACHE_PATH.resolve("scrobblers.json");
    public static final Path LISTEN_HISTORY_PATH = CACHE_PATH.resolve("history.json");

    static {
        ModContainer mod = QuiltLoader.getModContainer(MOD_ID).get(); // Should never be null, since we're running from
                                                                      // in this mod.
        ModMetadata metadata = mod.metadata();

        MOD_VERSION = metadata.version().raw();
        HOMEPAGE = metadata.getContactInfo("homepage");
    }
}
