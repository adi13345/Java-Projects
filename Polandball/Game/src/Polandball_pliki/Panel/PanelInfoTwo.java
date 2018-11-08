package Polandball_pliki.Panel;

import javax.swing.*;
import java.awt.*;

import Polandball_pliki.Others.ButtonLabel;
import static Polandball_pliki.Others.GetConstans.*;
/**
 * Klasa reprezentujaca panel boczny okna gry
 */
public class PanelInfoTwo extends JPanel{

    /**
     * Label odpowiedzialny za zwykle bomby
     */

    public static ButtonLabel Amountofbomb;

    /**
     * Label odpowiedzialny za wyswietlenie ilosci zwyklych bomb
     */

    public static ButtonLabel Iloscbombzwyklych;

    /**
     * Label odpowiedzialny za wyswietlenie ilosci bomb zdalnych
     */

    public static ButtonLabel Iloscbombzdalnych;

    /**
     * Label odpowiedzialny za skrzydla husarwskie
     */

    public static ButtonLabel Husarswings;

    /**
     * Label odpowiedzialny za wyswietlenie ilosci skrzydel husarskich
     */

    public static ButtonLabel Iloscskrzydelhusarskich;

    /**
     * Label odpowiedzialny za laser
     */

    public static ButtonLabel Laser;

    /**
     * Label odpowiedzialny za wyswietlenie ilosci laserow
     */

    public static ButtonLabel Ilosclaserow;

    /**
     * Label odpowiedzialny za klucze
     */

    public static ButtonLabel Key;

    /**
     * Label odpowiedzialny za wyswietlanie ilosci kluczy
     */

    public static ButtonLabel Ilosckluczy;

    /**
     * kontruktor panelu drugiego, zawierajacy funkcję PanelInfoTwo
     */

    public PanelInfoTwo(){ PanelInfoTwo();}

    /**
     * Metoda zawierająca parametry panelu, komponenty z informacjami dodatkowymi o statystykach gracza
     */

    private void PanelInfoTwo()
    {
        //dostosawnie rozmiarow do parametrow calej ramki
        panelboardwidth =(int)(0.8*Boardwidth);
        panelinfooneheight = (int)(0.2*Boardheight);
        panelinfotwoheight =(int)(0.8*Boardheight);
        panelinfotwowidth = (int)(0.2*Boardwidth);

        Dimension size = new Dimension(panelinfotwowidth,  panelinfotwoheight);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        this.setLayout(null);

        /****************************************************/
        //labele panela górnego, zawierające dodatkowe informaje o rozgrywce
        /****************************************************/

        Amountofbomb = new ButtonLabel("Ilość bomb:");
        add(Amountofbomb);


        //ilość bomb zwykłych zawarta w pliku konfiguracyjnym
        String iloscbombzwyklych =Integer.toString(Amountofordinarybombs);

        Iloscbombzwyklych = new ButtonLabel(iloscbombzwyklych);
        add(Iloscbombzwyklych);

        //ilość bomb zdalnych zawarta w pliku konfiguracyjnym
        String iloscbombzdalnych =Integer.toString(Amountofremotebombs);

        Iloscbombzdalnych = new ButtonLabel(iloscbombzdalnych);
        add(Iloscbombzdalnych);

        Husarswings = new ButtonLabel("Skrzydła husarskie");
        add(Husarswings);

        //ilość skrzydeł husarskich zawarta w pliku konfiguracyjnym
        String iloscskrzydelhusarskich =Integer.toString(Amountofhusarswings);

        Iloscskrzydelhusarskich = new ButtonLabel(iloscskrzydelhusarskich);
        add(Iloscskrzydelhusarskich);

        Laser = new ButtonLabel("Laser");
        add(Laser);

        //ilość laserów zawarta w pliku konfiguracyjnym
        String ilosclaserow =Integer.toString(Amountoflasers);

        Ilosclaserow = new ButtonLabel(ilosclaserow);
        add(Ilosclaserow);

        Key = new ButtonLabel("Key");
        add(Key);

        //ilość zawarta w pliku konfiguracyjnym ( ->parametr do cheatowania ;)<- )
        String ilosckluczy =Integer.toString(Amountofkeys);

        Ilosckluczy = new ButtonLabel(ilosckluczy);
        add(Ilosckluczy);

    }
    /**
     * Metoda skalujaca elementy skladowe panelu dolnego
     * @param g
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());

        Amountofbomb.setBounds(0,0,getWidth(),(int)((double)(getHeight())/9));
        Iloscbombzwyklych.setBounds(0,(int)((double)(getHeight()/9)),getWidth(),(int)((double)(getHeight())/9));
        Iloscbombzdalnych.setBounds(0,(int)((double)((2*getHeight())/9)),getWidth(),(int)((double)(getHeight())/9));
        Husarswings.setBounds(0,(int)((double)((3*getHeight())/9)),getWidth(),(int)((double)(getHeight())/9));
        Iloscskrzydelhusarskich.setBounds(0,(int)((double)((4*getHeight())/9)),getWidth(),(int)((double)(getHeight())/9));
        Laser.setBounds(0,(int)((double)((5*getHeight())/9)),getWidth(),(int)((double)(getHeight())/9));
        Ilosclaserow.setBounds(0,(int)((double)((6*getHeight())/9)),getWidth(),(int)((double)(getHeight())/9));
        Key.setBounds(0,(int)((double)((7*getHeight())/9)),getWidth(),(int)((double)(getHeight())/9));
        Ilosckluczy.setBounds(0,(int)((double)((8*getHeight())/9)),getWidth(),(int)((double)(getHeight())/9));

    }
}
