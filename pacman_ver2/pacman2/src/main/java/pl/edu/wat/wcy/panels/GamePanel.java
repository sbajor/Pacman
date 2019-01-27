package pl.edu.wat.wcy.panels;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.wat.wcy.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable, KeyListener, GameOverListener {
    private static boolean isRunning = false;

    private Thread thread;

    private Player player;
    private int levelCounter = 0;
    private Level currentLevel;
    private List<Level> levels;
    private int score = 0;

    public GamePanel() {
        Dimension dimension = new Dimension(App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        setPreferredSize(dimension);
        setMinimumSize(dimension);//nie zmieni się wielkośc okna
        setMaximumSize(dimension);
        addKeyListener(this);
        player = new Player(10*40, 8*40);//położenie początkowe
        levels = readLevels();
        currentLevel = levels.get(levelCounter);
        player.x = currentLevel.getPlayerStartX();
        player.y = currentLevel.getPlayerStartY();
    }

    public List<Level> readLevels() {
        try {
            Path path = Paths.get("images/levels").normalize().toAbsolutePath();
            DirectoryStream<Path> stream = Files.newDirectoryStream(path);
            List<Level> levels = new ArrayList<>();
            for (Path entry : stream) {
                Image image = ImageIO.read(Files.newInputStream(entry));
                Level level = new Level(image);
                levels.add(level);
            }
            return levels;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void start() {
        if (isRunning) return;
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!isRunning) return;
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tick() {
        player.tick();
        currentLevel.tick();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        currentLevel.render(g);
        player.render(g);

        Font font = g.getFont().deriveFont(20.0f);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score,20, 20);
        g.dispose();
    }

    public void run() {
        requestFocus();
        int fps = 0;
        double timer = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double targetTick = 60.0;
        double delta = 0.0;
        double ns = 1000000000 / targetTick;

        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                tick();
                repaint();
                fps++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                fps = 0;
                timer += 1000;
            }
        }
        stop();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.setRight(true);
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.setLeft(true);
        if (e.getKeyCode() == KeyEvent.VK_UP) player.setUp(true);
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.setDown(true);
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.setRight(false);
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.setLeft(false);
        if (e.getKeyCode() == KeyEvent.VK_UP) player.setUp(false);
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.setDown(false);
    }

    @Override
    public void gameOver() {
        levelCounter = 0;
        currentLevel = levels.get(0);
        isRunning = false;
        for (Level level : levels) {
            level.reset();
        }
        String nick = JOptionPane.showInputDialog(App.gamePanel, "Your score: " + score + " points, enter your nickname:");
        HighScore highScore = new HighScore(nick, score);
        score = 0;
        player.reset();
        App.frame.setContentPane(App.menuPanel);
        App.frame.pack();

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(highScore);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    public void loadNextLevel() {
        score += 25;
        levelCounter++;
        currentLevel = levels.get(levelCounter);
        player.x = currentLevel.getPlayerStartX();
        player.y = currentLevel.getPlayerStartY();
    }

    public Player getPlayer() {
        return player;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void incrementScore() {
        score++;
    }
}
