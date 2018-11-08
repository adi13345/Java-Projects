package Polandball_pliki.Frame;

import Polandball_pliki.Panel.SetNameFramePanel;

import javax.swing.*;

/**
 * Klasa tworzaca okno wyboru nazwy gracza
 */
public class SetNameFrame extends JFrame {

    /**
     * Obiekt panelu SetNameFramePanel, zawierajacy wyglad okna wyboru nazwy gracza
     */
    public static SetNameFramePanel setNameFramePanel;
    /**
     * Konstruktor klasy SetConnection, zawierajacy metode createFrame()
     */
    public SetNameFrame(){ createFrame();}
    /**
     * Metoda tworzaca okno wyboru nazwy gracza
     */
    private void createFrame(){
        //okno wyboru, czy chcemy korzystac z uslug serwera czy nie + odpowiednie parametry dla okna
        setNameFramePanel=new SetNameFramePanel();
        this.setTitle("Wybór nazwy gracza");
        this.getContentPane().add(setNameFramePanel);
        this.pack();
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setVisible(true);
    }
}


