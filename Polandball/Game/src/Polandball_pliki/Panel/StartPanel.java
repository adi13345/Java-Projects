package Polandball_pliki.Panel;


import Polandball_pliki.Frame.SetNameFrame;
import Polandball_pliki.Others.Highscores;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Socket;

import static Polandball_pliki.Others.GetConstans.MainFrameheight;
import static Polandball_pliki.Others.GetConstans.MainFramewidth;

/**
 * Klasa reprezentujaca panel, ktory bedzie zawieral elementy okna glownego
 */
public class StartPanel extends JPanel implements ActionListener {

    /**
     * Zdjęcie, które ustawimy na tło
     */

    private Image img_;
    /**
     * Przycisk rozpoczynający nową grę
     */

    public JButton NewGame;

    /**
     * Przycisk wyświetlający listę najlepszych wyników
     */

    public JButton Highscores;

    /**
     * Przycisk wyjścia z programu
     */

    public JButton Ending;

    /**
     * Obiekt klasy SetNameFrame, tworzenie okna wyboru nazwy gracza
     */
    public static SetNameFrame setnameframe;
    /**
     * Gniazdo serwera
     */

    public static Socket serversocket;

    /**
     * Adres IP serwera
     */

    private static String IPAddress;

    /**
     * Port serwera
     */

    private static int Port;

    /**
     * Konstruktor klasy StartPanel, tworzenie
     * @param img
     */
    public StartPanel(Image img){
        img_ = img;

        Dimension size = new Dimension(MainFramewidth, MainFrameheight);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);

        //przycisk nowej gry
        NewGame = new JButton("Nowa Gra");
        NewGame.setFont(new Font("Serif", Font.PLAIN, 20));
        add(NewGame);
        NewGame.addActionListener(this);


        //przycisk, ktory
        Highscores = new JButton("Najlepsze Wyniki");
        Highscores.setFont(new Font("Serif", Font.PLAIN, 20));
        add(Highscores);
        Highscores.addActionListener(this);

        //przycisk wyjścia z gry
        Ending = new JButton("Wyjście");
        Ending.setFont(new Font("Serif", Font.PLAIN, 20));
        add(Ending);
        Ending.addActionListener(this);

    }

    /**
     * Metoda skalujaca elementy skladowe okna glownego gry
     * @param g
     */
    public void paintComponent(Graphics g) {
        g.drawImage(img_, 0, 0, getWidth(), getHeight(), null);
        NewGame.setBounds(getWidth()/3,getHeight()/7,getWidth()/3,50);
        Highscores.setBounds(getWidth()/3,(3*getHeight())/7,getWidth()/3,50);
        Ending.setBounds(getWidth()/3,(5*getHeight())/7,getWidth()/3,50);

    }

    /**
     * Metoda obslugujaca wcisniecie przyciskow okna glownego gry
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();//bierzemy buttony, które nasłuchują na zdarzenie
        if(source==NewGame) {//przejście do okna wyboru nicku postaci
            setnameframe = new SetNameFrame();
            setnameframe.setVisible(true);
        }
        else if(source==Highscores){
            if(serversocket !=null) {
                Highscores highscores = new Highscores();
                highscores.GetHighscores(MakeSocket());//pobranie danych z serwera, przypisanie do bufora
                highscores.ShowHighscores();//wyswietlenie listy najlepszych wynikow
            }else{
                JOptionPane.showMessageDialog(null, "Nie mozna wyswietlic listy najlepszych wyników.");
            }
        }
        else if(source==Ending) //zamknięcie okna głownego, wyjście z gry
            System.exit(0);

    }
    /**
     * Metoda wczytujaca z pliku nr portu i adres ip serwera, tworzoca gniazdo
     * @return gniazdo serwera
     */
    public static Socket MakeSocket() {
        try {//wczytanie pliku ipconfig
            BufferedReader buildreader = new BufferedReader(new FileReader("ipconfig.txt"));
            IPAddress = buildreader.readLine();//przypisanie adresu ip serwera z pliku
            Port = Integer.parseInt(buildreader.readLine());//przypisanie nr portu serwera z pliku
            serversocket = new Socket(IPAddress, Port);//utworzenie gniazda

        } catch (Exception e) {
            System.out.println("Nie mozna polaczyc sie z serwerem");
            System.out.println("Blad: " + e);

        }
        return serversocket;
    }

    /**
     * Metoda odpowiadajaca za polaczenie z serwerem
     */
    public static void ConnectToServer() {
        try {
            MakeSocket();//wywolanie metody Getsocket(), utworzenie gniazda
        } catch (Exception e) {
            System.out.println("Nie mozna nawiazac polaczenia");
            System.out.println("Blad: " + e);
        }

    }

}
