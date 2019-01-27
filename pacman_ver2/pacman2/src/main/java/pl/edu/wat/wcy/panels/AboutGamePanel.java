package pl.edu.wat.wcy.panels;

import java.awt.*;

public class AboutGamePanel extends AboutPanel {
    private static final String IMAGE_PATH = "images/backgrounds/highScores.jpg";

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        Font font = g.getFont().deriveFont(20.0f);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("My version of Pacman for event-driven programing project" ,20, 20);
        g.dispose();
    }
    public AboutGamePanel() {
        super(IMAGE_PATH);
    }
}
