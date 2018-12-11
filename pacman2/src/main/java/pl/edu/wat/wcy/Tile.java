package pl.edu.wat.wcy;

import java.awt.*;

public class Tile extends Rectangle {
    private static final int WIDTH = 40, HEIGHT = 40;

    public Color color;

    public Tile(int x, int y, Color color) {
        setBounds(x, y, WIDTH, HEIGHT);
        this.color = color;
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}
