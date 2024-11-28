package net.pixaurora.kitten_cube.impl.ui.widget;

import net.pixaurora.kitten_cube.impl.math.Point;
import net.pixaurora.kitten_cube.impl.ui.controls.MouseButton;
import net.pixaurora.kitten_cube.impl.ui.display.GuiDisplay;
import net.pixaurora.kitten_cube.impl.ui.texture.GuiTexture;
import net.pixaurora.kitten_cube.impl.ui.widget.surface.RectangularSurface;
import net.pixaurora.kitten_cube.impl.ui.widget.surface.WidgetSurface;

public class StaticGuiTexture implements BasicWidget {
    private final GuiTexture texture;
    private final RectangularSurface surface;

    private StaticGuiTexture(GuiTexture icon, RectangularSurface surface) {
        this.texture = icon;
        this.surface = surface;
    }

    public StaticGuiTexture(GuiTexture icon) {
        this(icon, RectangularSurface.of(icon.size()));
    }

    @Override
    public void draw(GuiDisplay gui, Point mousePos) {
        gui.drawGui(this.texture, Point.ZERO);
    }

    public WidgetSurface surface() {
        return this.surface;
    }

    @Override
    public void onClick(Point mousePos, MouseButton button) {
    }
}
