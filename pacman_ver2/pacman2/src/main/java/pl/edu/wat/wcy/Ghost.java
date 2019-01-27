package pl.edu.wat.wcy;


import java.awt.*;

public class Ghost extends Rectangle {
    private Image image;

    private static final int SPEED = 1;

    private int startX, startY;

    public Ghost(int x, int y, Image image) {
        setBounds(x, y, 40, 40);
        this.image = image;
        this.startX = x;
        this.startY = y;
    }

    public void tick() {
        Player player = App.gamePanel.getPlayer();
        int playerX = player.x;
        int playerY = player.y;
        if (playerX < x && canMove(x- SPEED, y)) x -= SPEED;
        else if (playerX > x && canMove(x+ SPEED, y)) x += SPEED;
        else if (playerY < y && canMove(x, y- SPEED)) y -= SPEED;
        else if (playerY > y && canMove(x, y+ SPEED)) y += SPEED;

        if (this.intersects(player)) {
            App.gamePanel.gameOver();
        }
    }

    public void render(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    private boolean canMove(int nextX, int nextY) {
        Rectangle rectangle = new Rectangle(nextX, nextY, 40, 40);
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

    public void reset() {
        x = startX;
        y = startY;
    }
}
