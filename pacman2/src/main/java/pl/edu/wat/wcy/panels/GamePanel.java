package pl.edu.wat.wcy.panels;

import pl.edu.wat.wcy.App;
import pl.edu.wat.wcy.Level;
import pl.edu.wat.wcy.Player;

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

public class GamePanel extends JPanel implements Runnable, KeyListener {
    public static boolean isRunning = false;

    private Thread thread;

    public static Player player;
    public static int levelCounter = 0;
    public static Level currentLevel;
    public static List<Level> levels;
    public static int score = 0;

    public GamePanel() {
        Dimension dimension = new Dimension(App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        addKeyListener(this);
        player = new Player(10*40, 8*40);
        levels = readLevels();
        currentLevel = levels.get(levelCounter);
        player.x = currentLevel.playerStartX;
        player.y = currentLevel.playerStartY;
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
                System.out.println(fps);
                fps = 0;
                timer += 1000;
            }
        }
        stop();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.left = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) player.up = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.down = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.left = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) player.up = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.down = false;
    }
}
