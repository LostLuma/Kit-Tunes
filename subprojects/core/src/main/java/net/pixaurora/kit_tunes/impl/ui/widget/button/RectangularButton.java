package net.pixaurora.kit_tunes.impl.ui.widget.button;

import net.pixaurora.kit_tunes.impl.resource.ResourcePathImpl;
import net.pixaurora.kit_tunes.impl.ui.GuiDisplay;
import net.pixaurora.kit_tunes.impl.ui.MinecraftClient;
import net.pixaurora.kit_tunes.impl.ui.math.Point;
import net.pixaurora.kit_tunes.impl.ui.math.Size;
import net.pixaurora.kit_tunes.impl.ui.sound.Sound;
import net.pixaurora.kit_tunes.impl.ui.text.Color;
import net.pixaurora.kit_tunes.impl.ui.text.Component;
import net.pixaurora.kit_tunes.impl.ui.texture.GuiTexture;
import net.pixaurora.kit_tunes.impl.ui.widget.surface.RectangularSurface;
import net.pixaurora.kit_tunes.impl.ui.widget.surface.WidgetSurface;

public class RectangularButton implements Button {
    public static final Size DEFAULT_SIZE = Size.of(200, 20);

    private static final GuiTexture DEFAULT_DISABLED_TEXTURE = GuiTexture
            .of(new ResourcePathImpl("minecraft", "textures/gui/sprites/widget/button_disabled.png"), DEFAULT_SIZE);
    private static final GuiTexture DEFAULT_UNHIGLIGHTED_TEXTURE = GuiTexture
            .of(new ResourcePathImpl("minecraft", "textures/gui/sprites/widget/button.png"), DEFAULT_SIZE);
    private static final GuiTexture DEFAULT_HIGHLIGHTED_TEXTURE = GuiTexture
            .of(new ResourcePathImpl("minecraft", "textures/gui/sprites/widget/button_highlighted.png"), DEFAULT_SIZE);

    private final ButtonBackground background;
    private final RectangularSurface surface;

    private final Component text;
    private final Point textPos;

    private final ClickEvent action;

    private boolean isDisabled;

    public RectangularButton(ButtonBackground background, Point pos, Component text, ClickEvent action) {
        this.background = background;
        this.surface = RectangularSurface.of(pos, background.size());
        this.text = text;
        this.action = action;

        Size textSize = MinecraftClient.textSize(text);

        this.textPos = pos.offset(background.size().centerWithinSelf(textSize));
        this.isDisabled = false;
    }

    public static RectangularButton vanillaButton(Point pos, Component text, ClickEvent action) {
        return new RectangularButton(new ButtonBackground(DEFAULT_UNHIGLIGHTED_TEXTURE, DEFAULT_HIGHLIGHTED_TEXTURE,
                DEFAULT_DISABLED_TEXTURE), pos, text, action);
    }

    @Override
    public void draw(GuiDisplay gui, Point mousePos) {
        GuiTexture background = this.background.texture(this.isDisabled, this.surface.isWithinBounds(mousePos));
        gui.drawGuiTexture(background, this.surface.startPos());

        gui.drawText(this.text, Color.WHITE, this.textPos);
    }

    @Override
    public WidgetSurface surface() {
        return this.surface;
    }

    @Override
    public boolean isDisabled() {
        return this.isDisabled;
    }

    @Override
    public void setDisabledStatus(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @Override
    public void onClick(Point mousePos) {
        MinecraftClient.playSound(Sound.BUTTON_CLICK);
        this.action.onClick(this);
    }
}
