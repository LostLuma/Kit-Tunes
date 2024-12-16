package net.pixaurora.kitten_cube.impl;

import java.io.IOException;

import net.pixaurora.kit_tunes.api.resource.ResourcePath;
import net.pixaurora.kitten_cube.impl.math.Size;
import net.pixaurora.kitten_cube.impl.text.Component;
import net.pixaurora.kitten_cube.impl.ui.screen.Screen;
import net.pixaurora.kitten_cube.impl.ui.sound.Sound;
import net.pixaurora.kitten_heart.impl.KitTunes;
import net.pixaurora.kitten_heart.impl.resource.temp.FileAccess;
import net.pixaurora.kitten_heart.impl.service.UICompat;

public final class MinecraftClient {
    private static UICompat impl() {
        return KitTunes.UI_LAYER;
    }

    public static int textHeight() {
        return impl().textHeight();
    }

    public static int textWidth(Component text) {
        return impl().textWidth(text);
    }

    public static Size textSize(Component text) {
        return impl().textSize(text);
    }

    public static Size textSize(Component... text) {
        return impl().textSize(text);
    }

    public static void playSound(Sound sound) {
        impl().playSound(sound);
    }

    public static void setScreen(Screen screen) {
        impl().setScreen(screen);
    }

    public static void openURL(String url) {
        impl().openURL(url);
    }

    public static FileAccess accessResource(ResourcePath resource) throws IOException {
        return impl().accessResource(resource);
    }
}
