package net.pixaurora.kitten_square.impl.ui.widget;

import java.util.List;

import net.pixaurora.kitten_cube.impl.MinecraftClient;
import net.pixaurora.kitten_cube.impl.math.Point;
import net.pixaurora.kitten_cube.impl.math.Size;
import net.pixaurora.kitten_cube.impl.text.Color;
import net.pixaurora.kitten_cube.impl.ui.widget.text.TextBox;

public class TextBoxImpl implements TextBox {
    public final List<String> lines;
    public final Color color;
    public final Point startPos;

    public TextBoxImpl(List<String> lines, Color color, Point startPos) {
        this.lines = lines;
        this.color = color;
        this.startPos = startPos;
    }

    @SuppressWarnings("resource")
    @Override
    public Size size() {
        int width = 0;

        // for (String line : lines) {
        // width = Math.max(width, Minecraft.getInstance().font.width(line));
        // }

        return Size.of(width, MinecraftClient.textHeight() * lines.size());
    }
}
