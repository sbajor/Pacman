package pl.edu.wat.wcy.panels;

import java.awt.*;

public class AboutMePanel extends AboutPanel {
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
        g.drawString("My name is Sebastian. I am a student of the 4th year of computer science" ,20, 20);
        g.drawString("If you want more info about me, please concact: 660-450-719" ,20, 45);
        g.dispose();
    }
    public AboutMePanel() {
        super(IMAGE_PATH);
    }
}
