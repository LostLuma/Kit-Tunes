package net.pixaurora.kit_tunes.impl.compat;

import java.util.Map;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.minecraft.client.gui.screens.Screen;
import net.pixaurora.kit_tunes.impl.Constants;
import net.pixaurora.kit_tunes.impl.gui.ScreenImpl;
import net.pixaurora.kit_tunes.impl.gui.MinecraftScreen;
import net.pixaurora.kit_tunes.impl.ui.screen.KitTunesHomeScreen;

public class ModMenuIntegration implements ModMenuApi {
    public ScreenImpl modHomeScreen(Screen parent) {
        return new ScreenImpl(new KitTunesHomeScreen(new MinecraftScreen(parent)));
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return Map.of(Constants.MOD_ID, this::modHomeScreen);
    }
}
