package net.pixaurora.kit_tunes.impl.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.pixaurora.kit_tunes.impl.KitTunes;
import net.pixaurora.kit_tunes.impl.music.AlbumTrack;

public class MeowPlayingToast implements Toast {
	public static final ResourceLocation DEFAULT_ALBUM_SPRITE = new ResourceLocation(KitTunes.MOD_ID, "textures/album_art/default.png");
	public static final ResourceLocation TOAST_BACKGROUND = new ResourceLocation("toast/tutorial");

	public static final Component TITLE = Component.translatable("kit_tunes.toast.title");

	private final ResourceLocation albumSprite;
	private final Component songName;

	private boolean hasRendered;
	private long firstRenderedTime;

	public MeowPlayingToast(AlbumTrack track) {
		this.albumSprite = DEFAULT_ALBUM_SPRITE;
		this.songName = track.artist().append(Component.literal(" - ")).append(track.title());
		this.hasRendered = false;
	}

	private void drawAlbumArt(GuiGraphics graphics, int x, int y) {
		RenderSystem.enableBlend();
		graphics.blit(this.albumSprite, x, y, 0, 0.0F, 0.0F, 16, 16, 16, 16);
	}

	@Override
	public Toast.Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
		if (!this.hasRendered) {
			this.hasRendered = true;
			this.firstRenderedTime = startTime;
		}

		graphics.blitSprite(TOAST_BACKGROUND, 0, 0, this.width(), this.height());

		this.drawAlbumArt(graphics, 6, 6);

		Minecraft client = manager.getMinecraft();

		graphics.drawString(client.font, TITLE, 30, 7, ChatFormatting.DARK_PURPLE.getColor(), false);
		graphics.drawString(client.font, this.songName, 30, 18, ChatFormatting.BLACK.getColor(), false);

		return startTime - this.firstRenderedTime < 5000 ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
	}
}
