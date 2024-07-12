package net.pixaurora.kit_tunes.impl.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.pixaurora.kit_tunes.api.resource.ResourcePath;
import net.pixaurora.kit_tunes.impl.gui.widget.TextBoxImpl;
import net.pixaurora.kit_tunes.impl.ui.MinecraftClient;
import net.pixaurora.kit_tunes.impl.ui.display.GuiDisplay;
import net.pixaurora.kit_tunes.impl.ui.math.Point;
import net.pixaurora.kit_tunes.impl.ui.math.Size;
import net.pixaurora.kit_tunes.impl.ui.text.Color;
import net.pixaurora.kit_tunes.impl.ui.text.Component;
import net.pixaurora.kit_tunes.impl.ui.widget.text.TextBox;

public class GuiDisplayImpl implements GuiDisplay {
    private final GuiGraphics graphics;

    private final ConversionCacheImpl conversions;

    public GuiDisplayImpl(GuiGraphics graphics, ConversionCacheImpl conversions) {
        this.graphics = graphics;
        this.conversions = conversions;
    }

    @Override
    public void drawTexture(ResourcePath path, Size size, Point pos) {
        int width = size.width();
        int height = size.height();

        this.graphics.blit(conversions.convert(path), pos.x(), pos.y(), 0, 0.0F, 0.0F, width, height, width, height);
    }

    @Override
    public void drawGuiTextureSubsection(ResourcePath path, Size size, Point pos, Size subsection, Point offset) {
        this.graphics.blitSprite(conversions.convert(path), size.width(), size.height(), offset.x(), offset.y(),
                pos.x(), pos.y(), subsection.width(), subsection.height());
    }

    @SuppressWarnings("resource")
    @Override
    public void drawText(Component text, Color color, Point pos, boolean shadowed) {
        this.graphics.drawString(Minecraft.getInstance().font, conversions.convert(text), pos.x(), pos.y(), color.hex(),
                shadowed);
    }

    @SuppressWarnings("resource")
    @Override
    public void drawTextBox(TextBox textBox) {
        if (textBox instanceof TextBoxImpl) {
            TextBoxImpl impl = (TextBoxImpl) textBox;

            int y = impl.startPos.y();

            for (FormattedCharSequence line : impl.lines) {
                this.graphics.drawString(Minecraft.getInstance().font, line, impl.startPos.x(), y, impl.color.hex(),
                        false);

                y += MinecraftClient.textHeight();
            }
        } else {
            throw new UnsupportedOperationException("Unsupported instance of textbox");
        }
    }
}
