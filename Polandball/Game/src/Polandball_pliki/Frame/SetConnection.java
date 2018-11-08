package Polandball_pliki.Frame;

import Polandball_pliki.Panel.SetConnectionPanel;
import javax.swing.*;
import static Polandball_pliki.Others.GetConstans.*;

/**
 * Klasa tworzaca okno wyboru, czy chcemy grac w trybie onlnie czy offline
 */
public class SetConnection extends JFrame {

    /**
     * Obiekt typu SetConnectionPanel, zawierajacy wyglad okna wyboru trybu rozgrywki
     */
    public static SetConnectionPanel setConnectionPanel;
    /**
     * Konstruktor klasy SetConnection, zawierajacy metode createFrame()
     */
    public SetConnection(){ createFrame();}
    /**
     * Metoda tworzaca okno wyboru trybu rozgrywki
     */
    private void createFrame(){
        //okno wyboru, czy chcemy korzystac z uslug serwera czy nie + odpowiednie parametry dla okna
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setConnectionPanel=new SetConnectionPanel(new ImageIcon(BackgroundForSetConnectionString).getImage());
        this.getContentPane().add(setConnectionPanel);
        this.pack();
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setVisible(true);
    }

}

