package pl.edu.wat.wcy;

import javax.imageio.ImageIO;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Player extends Rectangle {
    private static final int WIDTH = 40, HEIGHT = 40;
    private static final int SPEED = 4;

    private boolean right, left, up, down;

    private Image currentImage;
    private Image pacmanLeft, pacmanRight, pacmanUp, pacmanDown;

    private int startX, startY;

    public Player(int x, int y) {
        setBounds(x, y, WIDTH, HEIGHT);
        readImage();
        startX = x;
        startY = y;
    }

    public void tick() {
        if (right && canMove(x + SPEED, y)) {
            x += SPEED;
            currentImage =  pacmanLeft;
        }
        if (left && canMove(x - SPEED, y)){
            x -= SPEED;
            currentImage = pacmanRight;
        }
        if (up && canMove(x, y - SPEED)){
            y -= SPEED;
            currentImage = pacmanUp;

        }
        if (down && canMove(x, y + SPEED)){
            y += SPEED;
            currentImage = pacmanDown ;
        }

        List<Fruit> fruits = App.gamePanel.getCurrentLevel().getFruits();
        for (Fruit fruit : fruits) {
            if (!fruit.isEaten() && this.intersects(fruit)) {
                fruit.markAsEaten();
                App.gamePanel.incrementScore();
                break;
            }
        }
        if (ateAll(fruits)) App.gamePanel.loadNextLevel();
    }

    private boolean ateAll(List<Fruit> fruits) {
        for (Fruit fruit : fruits) {
            if (!fruit.isEaten()) return false;
        }
        return true;
    }

    private boolean canMove(int nextX, int nextY) {
        Rectangle rectangle = new Rectangle(nextX, nextY, WIDTH, HEIGHT);
        Tile[][] tiles = App.gamePanel.getCurrentLevel().getTiles();
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 20; x++) {
                if (tiles[y][x].getColor() == Color.BLACK && rectangle.intersects(tiles[y][x])) {
                    return false;
                }
            }
        }
        return true;
    }

    public void render(Graphics g) {
        g.drawImage(currentImage, x, y, null);
    }

    private void readImage() {
        try {
            Path path_1 = Paths.get("images/pacman/pacmanLeft.png").normalize().toAbsolutePath();
            pacmanLeft = ImageIO.read(Files.newInputStream(path_1));
            Path path_2 = Paths.get("images/pacman/pacmanRight.png").normalize().toAbsolutePath();
            pacmanRight = ImageIO.read(Files.newInputStream(path_2));
            Path path_3 = Paths.get("images/pacman/pacmanUp.png").normalize().toAbsolutePath();
            pacmanUp = ImageIO.read(Files.newInputStream(path_3));
            Path path_4 = Paths.get("images/pacman/pacmanDown.png").normalize().toAbsolutePath();
            pacmanDown = ImageIO.read(Files.newInputStream(path_4));
            currentImage = pacmanLeft;
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

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
