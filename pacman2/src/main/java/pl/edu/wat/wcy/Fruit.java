package pl.edu.wat.wcy;

import java.awt.*;

public class Fruit extends Rectangle {
    public boolean eaten = false;

    public Fruit(int x, int y) {
        setBounds(x+18, y+18, 4, 4);
    }

    public void render(Graphics g) {
        if (!eaten) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, 4, 4);
        }
    }

    public void reset() {
        eaten = false;
    }
}
