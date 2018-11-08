package Polandball_pliki.Panel;


import Polandball_pliki.Others.ButtonLabel;

import javax.swing.*;
import java.awt.*;
import static Polandball_pliki.Others.GetConstans.*;
/**
 * Klasa reprezentujaca panel gorny okna gry
 */

public class PanelInfoOne extends JPanel{

    /**
     * Label odpowiedzialny za zycia
     */

    public static ButtonLabel LifeLabel1;
    /**
     * Label odpowiedzialny za punkty
     */
    public static ButtonLabel PointLabel1;
    /**
     * Label odpowiedzialny za czas
     */
    public static ButtonLabel TimeLabel1;
    /**
     * label odpowiedzialny za poziom
     */
    public static ButtonLabel LevelLabel1;
    /**
     * Label odpowiedzialny za nazwe gracza
     */
    public static ButtonLabel NameLabel1;

    /**
     * Label odpowiedzialny za wyswietlanie ilosci zyc
     */
    public static ButtonLabel LifeLabel2;

    /**
     * Label odpowiedzialny za wyswietlanie punktow
     */

    public static ButtonLabel PointLabel2;

    /**
     * Label odpowiedzialny za pokazywanie, ile pozostalo czasu
     */

    public static ButtonLabel TimeLabel2;

    /**
     * Label odpowiedzialny za wyswietlanie aktualnego poziomu
     */

    public static ButtonLabel LevelLabel2;
    /**
     * Label odpowiedzialny za wyswietlenie nazwy gracza
     */
    public static ButtonLabel NameLabel2;
    /**
     * Zmienna przechowujaca nazwe gracza
     */

    public static String PlayerName;

    /**
     * konstruktor panelu pierwszego, zawierający funkcję PanelInfoOne
     */

    public PanelInfoOne(){ PanelInfoOne();}

    /**
     *  funkcja zawierająca parametry panelu, komponenty z informacjami o statystykach gracza
     */

    private void PanelInfoOne(){

        panelinfooneheight = (int)(0.2*Boardheight);

        Dimension size = new Dimension(Boardwidth,  panelinfooneheight);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        this.setLayout(null);

        /****************************************************/
         //labele panela górnego, zawierające informaje o rozgrywce
        /****************************************************/

        LifeLabel1 = new ButtonLabel("Ilość żyć:");
        add(LifeLabel1);

        PointLabel1 = new ButtonLabel("Ilość punktów:");
        add(PointLabel1);

        TimeLabel1 = new ButtonLabel("Czas:");
        add(TimeLabel1);

        LevelLabel1 = new ButtonLabel("Poziom:");
        add(LevelLabel1);

        NameLabel1 = new ButtonLabel("Nick:");
        add(NameLabel1);

        //ilosc zyc zawarta w pliku konfiguracyjnym
        String ilosczyc =Integer.toString(Amountoflifes);

        LifeLabel2 = new ButtonLabel(ilosczyc);
        add(LifeLabel2);

        //ilosc punktow, zawsze 0, konwersja
        String iloscpunktow = Integer.toString(Amountofpoints);

        PointLabel2 = new ButtonLabel(iloscpunktow);
        add(PointLabel2);

        //nizej zdefinoiwana metoda SetInitialTime()- wyswietlenie czasu poczatkowego
        TimeLabel2 = new ButtonLabel(SetInitialTime());
        add(TimeLabel2);

        LevelLabel2 = new ButtonLabel(Integer.toString(WhiChLevel));
        add(LevelLabel2);

        //wyswietlenie nazwy gracza wczytanej w okienku wyboru nicku w odpowiednim labelu
        PlayerName = SetNameFramePanel.GetName();

        NameLabel2 = new ButtonLabel(PlayerName);
        add(NameLabel2);

    }

    /**
     * Metoda skalujaca elementy skladowe panelu gornego
     * @param g
     */
    public void paintComponent(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(0,0,getWidth(),getHeight());
        LifeLabel1.setBounds(0,0,(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        PointLabel1.setBounds((int)((double)getWidth()*0.2),0,(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        TimeLabel1.setBounds((int)((double)getWidth()*0.4),0,(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        LevelLabel1.setBounds((int)((double)getWidth()*0.6),0,(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        NameLabel1.setBounds((int)((double)getWidth()*0.8),0,(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));

        LifeLabel2.setBounds(0,(int)((double)getHeight()*0.5),(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        PointLabel2.setBounds((int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5),(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        TimeLabel2.setBounds((int)((double)getWidth()*0.4),(int)((double)getHeight()*0.5),(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        LevelLabel2.setBounds((int)((double)getWidth()*0.6),(int)((double)getHeight()*0.5),(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
        NameLabel2.setBounds((int)((double)getWidth()*0.8),(int)((double)getHeight()*0.5),(int)((double)getWidth()*0.2),(int)((double)getHeight()*0.5));
    }

    /**
     * Metoda zwracajaca odpowiedni napis potrzebny do wyswietlania poczatkowego stanu zegara gry
     */

    public static String SetInitialTime(){
        //czas, od ktorego odliczamy
        String iloscczasu;
        //System.out.println((((double)(LevelTime))/60)-(int)(Math.floor(LevelTime/60)));
        //sprawdzenie czy czas jest podzielny przez 60 bez reszty + kilka innych sprawdzen zeby bylo dobrze wyswietlane na samym poczatku
        if ((LevelTime >= 60 && ((((double)(LevelTime))/60)-(int)(Math.floor(LevelTime/60))) == 0) || LevelTime < 10) {
            iloscczasu = Integer.toString((int) Math.floor(LevelTime / 60)) + ":0"
                    + Integer.toString((int) (LevelTime - (Math.floor(LevelTime / 60)) * 60));
            return iloscczasu;
        }else{
            iloscczasu = Integer.toString((int) Math.floor(LevelTime / 60)) + ":"
                    + Integer.toString((int) (LevelTime - (Math.floor(LevelTime / 60)) * 60));
            return iloscczasu;
        }
    }
}
