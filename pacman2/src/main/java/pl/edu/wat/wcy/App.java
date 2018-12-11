package pl.edu.wat.wcy;

import pl.edu.wat.wcy.panels.*;

import javax.swing.*;

public class App {
    public static final int APPLICATION_WIDTH = 800;
    public static final int APPLICATION_HEIGHT = 600;

    private static final String TITLE = "Pacman";

    public static MenuPanel menuPanel;
    public static GamePanel gamePanel;
    public static HighScoresPanel highScoresPanel;
    public static AboutGamePanel aboutGamePanel;
    public static AboutMePanel aboutMePanel;
    public static JFrame frame;

    public static void main(String[] args) {
        menuPanel = new MenuPanel();
        gamePanel = new GamePanel();
        highScoresPanel = new HighScoresPanel();
        aboutGamePanel = new AboutGamePanel();
        aboutMePanel = new AboutMePanel();
        frame = new JFrame();
        frame.setTitle(TITLE);
        frame.add(menuPanel);
        frame.add(gamePanel);
        frame.add(aboutGamePanel);
        frame.add(aboutMePanel);
        frame.setResizable(false);
        frame.setContentPane(menuPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        //gamePanel.start();
    }
}
