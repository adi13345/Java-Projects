package Polandball_pliki.Panel;

import javax.swing.*;
import java.awt.*;

import static Polandball_pliki.Others.GetConstans.Boardheight;
import static Polandball_pliki.Others.GetConstans.Boardwidth;

/**
 * Klasa reprezentujaca panel okna gry
 */
public class MainPanel extends JPanel {

    /**
     * Panel glowny gry
     */
    public static PanelBoard panelboard;

    /**
     * Panel gorny gry
     */

    public static PanelInfoOne panelinfoone;

    /**
     * Panel boczny gry
     */

    public static  PanelInfoTwo panelinfotwo;

    /**
     * Kontruktor klasy MainPanel, tworzenie okna gry
     */
    public MainPanel() {

        Dimension size = new Dimension(Boardwidth, Boardheight);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);

        //panel, który będzie naszą plansza do dodawania elementów poziomu
        panelboard = new PanelBoard();
        add(panelboard);
        panelboard.setVisible(true);

        //panel gorny, w ktorym beda informacje dotyczace gry
        panelinfoone = new PanelInfoOne();
        add(panelinfoone);
        panelinfoone.setVisible(true);

        //panel boczny, w ktorym beda pozostale informacje
        panelinfotwo = new PanelInfoTwo();
        add(panelinfotwo);
        panelinfotwo.setVisible(true);

    }

    /**
     * Metoda skalujaca elementy skladowe okna gry
     * @param g
     */

    public void paintComponent(Graphics g){
            panelinfoone.setBounds(0,0,getWidth(),(int)((double)getHeight()*0.2));
            panelinfotwo.setBounds((int)((double)getWidth()*0.8),(int)((double)getHeight()*0.2),(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.8));
            panelboard.setBounds(0,(int)((double)getHeight()*0.2),(int)((double)getWidth()*0.8),(int)((double)getHeight()*0.8));
    }
}
