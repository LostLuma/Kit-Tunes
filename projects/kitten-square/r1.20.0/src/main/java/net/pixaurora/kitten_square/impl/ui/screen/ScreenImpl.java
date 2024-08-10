package net.pixaurora.kitten_square.impl.ui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.pixaurora.kitten_cube.impl.math.Point;
import net.pixaurora.kitten_cube.impl.math.Size;
import net.pixaurora.kitten_cube.impl.ui.controls.MouseButton;
import net.pixaurora.kitten_cube.impl.ui.display.GuiDisplay;
import net.pixaurora.kitten_cube.impl.ui.screen.Screen;
import net.pixaurora.kitten_square.impl.ui.ConversionCacheImpl;
import net.pixaurora.kitten_square.impl.ui.display.GuiDisplayImpl;

public class ScreenImpl extends net.minecraft.client.gui.screens.Screen {
    private final Screen screen;

    private final ConversionCacheImpl conversions;

    public ScreenImpl(Screen screen) {
        super(Component.empty());

        this.screen = screen;

        this.conversions = new ConversionCacheImpl();
    }

    // "Minecraft Screen" functions

    @Override
    public void init() {
        this.screen.init(Size.of(this.width, this.height));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        GuiDisplay display = new GuiDisplayImpl(graphics, this.conversions);
        Point mousePos = Point.of(mouseX, mouseY);

        this.screen.draw(display, mousePos);
    }

    @Override
    public void onClose() {
        this.screen.onExit();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        Point mousePos = Point.of((int) x, (int) y);

        this.screen.handleClick(mousePos, MouseButton.fromGlfwCode(button));

        return false;
    }

    @Override
    public void tick() {
        this.screen.tick();
    }
}
