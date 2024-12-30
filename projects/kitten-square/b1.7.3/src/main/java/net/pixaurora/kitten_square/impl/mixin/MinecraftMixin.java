package net.pixaurora.kitten_square.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Window;
import net.pixaurora.kitten_cube.impl.math.Size;
import net.pixaurora.kitten_square.impl.ui.toast.ToastManager;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    private Size scaledWindow(int width, int height) {
        Window window = new Window(((Minecraft) (Object) this).options, width, height);

        return Size.of(window.getWidth(), window.getHeight());
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void onStart(CallbackInfo cInfo) {
        Minecraft client = (Minecraft) (Object) this;

        ToastManager.INSTANCE = new ToastManager(this.scaledWindow(client.width, client.height));
    }

    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    public void onWindowUpdate(int width, int height, CallbackInfo cInfo) {
        ToastManager.INSTANCE.onWindowUpdate(this.scaledWindow(width, height));
    }
}
