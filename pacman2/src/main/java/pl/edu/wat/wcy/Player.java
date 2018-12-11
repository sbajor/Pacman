package pl.edu.wat.wcy;

import pl.edu.wat.wcy.panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Player extends Rectangle {
    private static final int WIDTH = 40, HEIGHT = 40;

    public boolean right, left, up, down;
    private int speed = 4;

    private Image image;

    private int startX, startY;

    public Player(int x, int y) {
        setBounds(x, y, WIDTH, HEIGHT);
        readImage();
        startX = x;
        startY = y;
    }

    public void tick() {
        if (right && canMove(x+speed, y)) x += speed;
        if (left && canMove(x-speed, y)) x -= speed;
        if (up && canMove(x, y-speed)) y -= speed;
        if (down && canMove(x, y+speed)) y += speed;

        Level level = GamePanel.currentLevel;
        for (int i = 0; i < level.fruits.size(); i++) {
            if (!level.fruits.get(i).eaten && this.intersects(level.fruits.get(i))) {
                level.fruits.get(i).eaten = true;
                GamePanel.score++;
                break;
            }
        }
        if (ateAll()) {
            GamePanel.score += 25;
            GamePanel.levelCounter++;
            GamePanel.currentLevel = GamePanel.levels.get(GamePanel.levelCounter);
            GamePanel.player.x = GamePanel.currentLevel.playerStartX;
            GamePanel.player.y = GamePanel.currentLevel.playerStartY;
        }
    }

    private boolean ateAll() {
        for (Fruit fruit : GamePanel.currentLevel.fruits) {
            if (!fruit.eaten) return false;
        }
        return true;
    }

    private boolean canMove(int nextX, int nextY) {
        Rectangle rectangle = new Rectangle(nextX, nextY, WIDTH, HEIGHT);
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

    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    private void readImage() {
        try {
            Path path = Paths.get("images/pacman.png").normalize().toAbsolutePath();
            image = ImageIO.read(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        x = startX;
        y = startY;
        up = false;
        down = false;
        left = false;
        right = false;
    }
}
