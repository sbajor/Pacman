package pl.edu.wat.wcy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.wat.wcy.panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ghost extends Rectangle {
    private Image image;

    private int speed = 1;

    private int startX, startY;

    public Ghost(int x, int y, Image image) {
        setBounds(x, y, 40, 40);
        this.image = image;
        startX = x;
        startY = y;
    }

    public void tick() {
        int playerX = GamePanel.player.x;
        int playerY = GamePanel.player.y;
        if (playerX < x && canMove(x-speed, y)) x -= speed;
        else if (playerX > x && canMove(x+speed, y)) x += speed;
        else if (playerY < y && canMove(x, y-speed)) y -= speed;
        else if (playerY > y && canMove(x, y+speed)) y += speed;

        if (this.intersects(GamePanel.player)) {
            GamePanel.levelCounter = 0;
            GamePanel.currentLevel = GamePanel.levels.get(0);
            GamePanel.isRunning = false;
            for (Level level : GamePanel.levels) {
                level.reset();
            }
            System.out.println("Score: " + GamePanel.score);
            String nick = JOptionPane.showInputDialog(App.gamePanel, "Enter your nickname");
            HighScore highScore = new HighScore(nick, GamePanel.score);
            GamePanel.score = 0;
            GamePanel.player.reset();
            App.frame.setContentPane(App.menuPanel);
            App.frame.pack();

            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.save(highScore);
            session.getTransaction().commit();
            sessionFactory.close();


        }
    }

    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    private boolean canMove(int nextX, int nextY) {
        Rectangle rectangle = new Rectangle(nextX, nextY, 40, 40);
        Level level = GamePanel.currentLevel;
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 20; x++) {
                if (level.tiles[y][x].color == Color.BLACK && rectangle.intersects(level.tiles[y][x])) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        x = startX;
        y = startY;
    }
}
