package pl.edu.wat.wcy.panels;

import pl.edu.wat.wcy.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MenuPanel extends JPanel implements ActionListener {
    private Image image;
    private GridBagConstraints c;
    private JButton newGame, highScores, aboutGame, aboutMe;

    public MenuPanel() {
        super(new GridBagLayout());
        setSize();
        readImage();
        initGrid();
        initButtons();
    }

    private void setSize() {
        Dimension dimension = new Dimension(App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
    }

    private void readImage() {
        try {
            Path path = Paths.get("images/backgrounds/menu.jpg").normalize().toAbsolutePath();
            image = ImageIO.read(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGrid() {
        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 20, 0);
        c.gridwidth = 1;
        c.gridheight = 1;
    }

    private void initButtons() {
        newGame = createButton("New game", 0, 0);
        highScores = createButton("High scores", 0, 1);
        aboutGame = createButton("About game", 0, 2);
        aboutMe = createButton("About me", 0, 3);
    }

    private JButton createButton(String name, int gridX, int gridY) {
        int width = 150, height = 30;
        JButton button = new JButton(name);
        button.setPreferredSize(new Dimension(width, height));
        button.addActionListener(this);
        button.setVisible(true);
        c.gridx = gridX;
        c.gridy = gridY;
        add(button, c);
        return button;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(newGame)) {
            App.frame.setContentPane(App.gamePanel);
            App.frame.pack();
            App.gamePanel.start();
        } else if (e.getSource().equals(highScores)) {
            App.highScoresPanel.getHighScores();
            App.frame.setContentPane(App.highScoresPanel);
            App.frame.pack();
        } else if (e.getSource().equals(aboutGame)) {
            App.frame.setContentPane(App.aboutGamePanel);
            App.frame.pack();
        } else if (e.getSource().equals(aboutMe)) {
            App.frame.setContentPane(App.aboutMePanel);
            App.frame.pack();
        }
    }
}
