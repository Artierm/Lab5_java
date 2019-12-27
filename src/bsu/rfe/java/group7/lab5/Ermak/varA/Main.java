package bsu.rfe.java.group7.lab5.Ermak.varA;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;



public class Main extends JFrame {

    private final int HEIGHT = 700;
    private final int WIDTH = 800;
    private boolean fileLoaded = false;
    private GraphicsDisplay display = new GraphicsDisplay();
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JMenuItem resetGraphicsMenuItem;
    private JFileChooser fileChooser = null;
    public Main()  {
        super("Построение графика функции из файла");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл") {

            public void actionPerformed(ActionEvent arg0) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION);
                openGraphics(fileChooser.getSelectedFile());

            }
        };
        fileMenu.add(openGraphicsAction);


        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        Action showAxisAction = new AbstractAction("Показать оси координат") {
            public void actionPerformed(ActionEvent e) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);


        Action showMarkersAction = new AbstractAction("Показать маркеры точек") {

            public void actionPerformed(ActionEvent e) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        graphicsMenu.addSeparator();

        Action resetGraphicsAction = new AbstractAction("Отменить все изменения") {
            public void actionPerformed(ActionEvent event) {
                Main.this.display.reset();
            }
        };
        resetGraphicsMenuItem = new JMenuItem(resetGraphicsAction);
        graphicsMenu.add(resetGraphicsMenuItem);
        resetGraphicsMenuItem.setEnabled(false);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        getContentPane().add(display, BorderLayout.CENTER);
    }

    protected void openGraphics(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            ArrayList graphicsData = new ArrayList(50);
            while (in.available() > 0) {
                Double x = Double.valueOf(in.readDouble());
                Double y = Double.valueOf(in.readDouble());
                graphicsData.add(new Double[] { x, y });
            }
            if (graphicsData.size() > 0) {
                fileLoaded = true;
                resetGraphicsMenuItem.setEnabled(true);
                display.showGraphics(graphicsData);
            }
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }
    }

    private class GraphicsMenuListener implements MenuListener {

        public void menuCanceled(MenuEvent arg0) {
        }

        public void menuDeselected(MenuEvent arg0) {
        }

        public void menuSelected(MenuEvent arg0) {

            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }
    }
    public static void main(String[] args) {
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
