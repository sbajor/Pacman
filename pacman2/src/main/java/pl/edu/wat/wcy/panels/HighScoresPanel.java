package pl.edu.wat.wcy.panels;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.wat.wcy.App;
import pl.edu.wat.wcy.HighScore;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HighScoresPanel extends JPanel implements ActionListener {
    private static final String IMAGE_PATH = "images/backgrounds/highScores.jpg";

    private Image image;
    private JButton mainMenu;

    public HighScoresPanel() {
        super(new GridBagLayout());
        setSize();
        readImage();
        initComponents();
    }

    private void setSize() {
        Dimension dimension = new Dimension(App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
    }

    private void readImage() {
        try {
            Path path = Paths.get(IMAGE_PATH).normalize().toAbsolutePath();
            image = ImageIO.read(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(20, 0, 20, 0);
        grid.gridwidth = 1;
        grid.gridheight = 1;
        grid.gridx = 0;
        grid.gridy = 0;
        int buttonsWidth = 300;
        int buttonsHeight = 30;
        mainMenu = new JButton("Back to main menu");
        mainMenu.setPreferredSize(new Dimension(buttonsWidth, buttonsHeight));
        mainMenu.addActionListener(this);
        mainMenu.setVisible(true);
        add(mainMenu, grid);
        grid.gridx = 0;
        grid.gridy = 1;
        List<HighScore> highScores = getHighScores();
        String data[][] = new String[highScores.size()][3];
        for (int i = 0; i < highScores.size(); i++) {
            data[i][0] = Long.toString(highScores.get(i).getId());
            data[i][1] = highScores.get(i).getPlayerNick();
            data[i][2] = Integer.toString(highScores.get(i).getScore());
        }
        String columnNames[] = new String[]{"id", "player nick", "score"};
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(300, 450));
        add(pane, grid);
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

    public List<HighScore> getHighScores() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<HighScore> highScores = session.createCriteria(HighScore.class).list();
        //session.getTransaction().commit();
        sessionFactory.close();
        System.out.println(highScores);
        return highScores;
    }
}
