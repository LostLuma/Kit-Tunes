package net.pixaurora.kit_tunes.impl.ui.widget.text;

import java.util.ArrayList;
import java.util.List;

import net.pixaurora.kit_tunes.impl.ui.GuiDisplay;
import net.pixaurora.kit_tunes.impl.ui.MinecraftClient;
import net.pixaurora.kit_tunes.impl.ui.math.Point;
import net.pixaurora.kit_tunes.impl.ui.text.Color;
import net.pixaurora.kit_tunes.impl.ui.text.Component;
import net.pixaurora.kit_tunes.impl.ui.widget.Widget;

public class PushableTextLines implements Widget {
    private final Point startPos;
    private final List<PositionedText> lines;

    public PushableTextLines(Point startPos) {
        this.lines = new ArrayList<>();
        this.startPos = startPos;
    }

    private int height() {
        return this.lines.size() * MinecraftClient.textHeight();
    }

    public Point endPos() {
        return startPos.offset(0, this.height());
    }

    public void push(Component text, Color color) {
        Point newLinePos = MinecraftClient.textSize(text).centerOnVertical(startPos).offset(0, this.height());
        this.lines.add(new PositionedText(text, newLinePos, color));
    }

    @Override
    public void draw(GuiDisplay gui, Point mousePos) {
        for (PositionedText line : this.lines) {
            gui.drawText(line.text(), line.color(), line.pos());
        }
    }

    @Override
    public void onClick(Point mousePos) {
    }

    @Override
    public boolean isWithinBounds(Point mousePos) {
        return false;
    }
}
