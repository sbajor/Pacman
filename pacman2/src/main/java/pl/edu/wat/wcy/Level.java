package pl.edu.wat.wcy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Level {
    public int width, height;

    public Tile[][] tiles;
    public List<Fruit> fruits;
    public List<Ghost> ghosts;

    public int playerStartX, playerStartY;

    public Level(Image image) {
        BufferedImage map = (BufferedImage) image;
        this.width = map.getWidth();
        this.height = map.getHeight();
        tiles = new Tile[height][width];
        fruits = new ArrayList<>();
        ghosts = new ArrayList<>();
        List<Image> ghostImages = readGhostImages();
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int clr = map.getRGB(x, y);
                if (clr == 0xFF000000) {
                    // ściana
                    tiles[y][x] = new Tile(x*40, y*40, Color.BLACK);
                } else if (clr == 0xFFFFFF00) {
                    // gracz
                    //GamePanel.player.x = x*40;
                    //GamePanel.player.y = y*40;
                    playerStartX = x*40;
                    playerStartY = y*40;
                    Color pathColor = new Color(0xFF232576);
                    tiles[y][x] = new Tile(x*40, y*40, pathColor);
                } else if (clr == 0xFFFF0000) {
                    // duch
                    ghosts.add(new Ghost(x*40, y*40, ghostImages.get(index%ghostImages.size())));
                    index++;
                    Color pathColor = new Color(0xFF232576);
                    tiles[y][x] = new Tile(x*40, y*40, pathColor);
                } else {
                    Color pathColor = new Color(0xFF232576);
                    tiles[y][x] = new Tile(x*40, y*40, pathColor);
                    fruits.add(new Fruit(x*40, y*40));
                }
            }
        }
    }

    public void tick() {
        for (Ghost ghost : ghosts) {
            ghost.tick();
        }
    }

    public void render(Graphics g) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x].render(g);
            }
        }
        for (Fruit fruit : fruits) {
            fruit.render(g);
        }
        for (Ghost ghost : ghosts) {
            ghost.render(g);
        }
    }

    private List<Image> readGhostImages() {
        try {
            Path path = Paths.get("images/ghosts").normalize().toAbsolutePath();
            DirectoryStream<Path> stream = Files.newDirectoryStream(path);
            List<Image> images = new ArrayList<>();
            for (Path entry : stream) {
                Image image = ImageIO.read(Files.newInputStream(entry));
                images.add(image);
            }
            return images;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reset() {
        for (Fruit fruit : fruits) {
            fruit.reset();
        }
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }
}
