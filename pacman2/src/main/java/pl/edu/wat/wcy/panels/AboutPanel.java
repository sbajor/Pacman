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

public abstract class AboutPanel extends JPanel implements ActionListener {
    private Image image;
    private JButton mainMenu;

    public AboutPanel(String imagePath) {
        super(new GridBagLayout());
        setSize();
        readImage(imagePath);
        addButton();
    }

    private void setSize() {
        Dimension dimension = new Dimension(App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
    }

    private void readImage(String imagePath) {
        try {
            Path path = Paths.get(imagePath).normalize().toAbsolutePath();
            image = ImageIO.read(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addButton() {
        GridBagConstraints grid = initGrid();
        initButton();
        add(mainMenu, grid);
    }

    private GridBagConstraints initGrid() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 20, 0);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        return c;
    }

    private void initButton() {
        int buttonsWidth = 150;
        int buttonsHeight = 30;
        mainMenu = new JButton("Back to main menu");
        mainMenu.setPreferredSize(new Dimension(buttonsWidth, buttonsHeight));
        mainMenu.addActionListener(this);
        mainMenu.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(mainMenu)) {
            App.frame.setContentPane(App.menuPanel);
            App.frame.pack();
        }
    }
}
