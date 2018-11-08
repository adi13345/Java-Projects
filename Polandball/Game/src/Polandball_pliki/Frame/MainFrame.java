package Polandball_pliki.Frame;

import javax.swing.*;

import Polandball_pliki.Panel.StartPanel;
import static Polandball_pliki.Others.GetConstans.*;

/**
 * Okno glowne, wyświetlane podczas uruchomienia gry
 */
public class MainFrame extends JFrame  {

    /**
     * Obiekt typu startPanel, zawierajacy wyglad okna glownego
     */

    public static StartPanel startPanel;

    /**
     * Konstruktor okna glownego, zawierający metode initMainFrame()
     */

    public MainFrame() {
        initMainFrame();
    }

    /**
     * MetodA zawierająca parametry i komponenty okna glownego
     */

    private void initMainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startPanel =new StartPanel(new ImageIcon(BackgroundString).getImage());
        this.getContentPane().add(startPanel);
        this.pack();
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setVisible(true);
    }
}