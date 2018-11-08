package Polandball_pliki.Panel;

import Polandball_pliki.Frame.MainFrame;
import Polandball_pliki.Frame.GameOver;
import Polandball_pliki.Others.GetConstans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

import static Polandball_pliki.Frame.MainFrame.startPanel;
import static Polandball_pliki.Main.setconnection;
import static Polandball_pliki.Others.GetConstans.*;
/**
 * Klasa reprezentujaca panel okna wyboru trybu rozgrywki
 */
public class SetConnectionPanel extends JPanel implements ActionListener {

    /**
     * Tlo dla okna klasy SetConnection
     */

    private Image image;

    /**
     * Prywatny obiekt klasy GetConstans, wykorzystanie sparsowanych plików
     */

    static public GetConstans getConstans_;

    /**
     * Button zawierajacy pytanie o to, czy uzytkownik chce grac w trybie online czy offline
     */

    public JButton Question;

    /**
     * Przycisk zatwierdzajacy polaczenie z serwerm
     */

    public JButton Yes;

    /**
     * Przycisk anulujacy polaczenie z serwerem
     */

    public JButton No;

    /**
     * Bufor, do ktorego beda zapisane dane konfiguracyjne podstawowe
     * Jego pojemnosc jest dokladnie okreslona, zalezna od ilosci zmiennych konfiguracyjnych
     */

    public static String[] configbufor = new String[13];

    /**
     * Bufor, do ktorego beda zapisywane rozdzielone dane konfiguracyjne poziomu - oddzielnie rozklad pol planszy i reszta
     * Jego pojemnosc jest dokladnie okreslona, zalezna od ilosci zmiennych konfiguracyjnych
     */

    public static String[] levelbufor = new String[2];

    /**
     * Zmienna informujaca, czy gramy online czy offline
     */

    public static boolean ServerMode;
    /**
     * Flaga informujaca, moze byc zaladowany nastepny poziom
     */

    public static boolean LoadLevel;

    /**
     * Konstruktor klasy SetConnectionPanel, wyglad zawartosci panelu
     */

    public SetConnectionPanel(Image img){
        image=img;
        //okno wyboru, czy chcemy korzystac z uslug serwera czy nie + odpowiednie parametry dla okna
        Dimension size = new Dimension(900, 400);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        //label z pytaniem, zeby bylo wiadomo, o co chodzi
        Question = new JButton("Czy chcesz rozpocząć rozgrywkę w trybie online?");
        Question.setFont(new Font("Italic", Font.PLAIN, 40));
        Question.setFocusPainted(false);
        Question.setMargin(new Insets(0, 0, 0, 0));
        Question.setContentAreaFilled(false);
        Question.setBorderPainted(false);
        Question.setOpaque(false);
        add(Question);

        //przycisk do zatwierdzenia
        Yes = new JButton("Yes");
        Yes.setFont(new Font("Serif", Font.PLAIN, 20));
        add(Yes);
        Yes.addActionListener(this );

        //przycisk do anulowania
        No = new JButton("No");
        No.setFont(new Font("Serif", Font.PLAIN, 20));
        add(No);
        No.addActionListener(this );

    }

    /**
     * Metoda obsługująca naciśniecia przycisków
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source==Yes) {
            try {
                startPanel.ConnectToServer();//polaczenie z serwerem
                if(startPanel.MakeSocket()!=null) {//jeesli udalo sie nawiazac polaczenie to pobieramy dane
                    GetBasicConfig(startPanel.MakeSocket());//pobranie danych konfiguracyjnych
                    GetLevelConfig(startPanel.MakeSocket(), 1);//pobranie danych pierwszego poziomu na poczatku gry
                    MakeBoardObstacleTable();//utworzenie tablicy statycznej do wykrywania kolzji
                    EventQueue.invokeLater(() -> { //utworzenie okna menu
                        MainFrame mainframe = new MainFrame();
                        mainframe.setVisible(true);
                    });
                    ServerMode=true;
                    setconnection.dispose();
                }else{//jak nie sie nie udalo to komunikacik i dobranoc
                    JOptionPane.showMessageDialog(null, "Nie udało się uruchomić gry w trybie online.");
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
        if(source==No) {
            GetConstans getConstans_=new GetConstans();//wczytanie grafik i podstawowych parametrow aplikacji
            GetConstans.read_on_level(1);//wczytanie parametrow poziomu
            MakeBoardObstacleTable();//utworzenie tablicy statycznej do wykrywania kolzji
            EventQueue.invokeLater(() -> {//utworzenie okna menu
                MainFrame mainframe = new MainFrame();
                mainframe.setVisible(true);
            });
            ServerMode=false;
            setconnection.dispose();
        }
    }

    /**
     * Metoda pobierajaca dane konfiguracyje podstawowe aplikacji
     * @param socket
     */
    public void GetBasicConfig (Socket socket) {
        try {
            if (socket != null) {//sprawdzenie czy gniazdo serwera nie jest nullem(czy zostalo "cos" przypisane)
                socket.getInputStream().skip(socket.getInputStream().available());//pominiecie odpowiedzi serwera
                OutputStream outputstream = socket.getOutputStream();//strumien wyjsciowy
                PrintWriter printwriter = new PrintWriter(outputstream, true);//przywiazanie do strumienia wyjsciowego
                printwriter.println("GET_CONFIGFILE\n");//utworzenie wiadomosci GET_CONFIGfILE    //wiadomosci ktora bedziemy wysylac
                System.out.println("WYSLANO WIADOMOSC: GET_CONFIGFILE");
                InputStream inputstream = socket.getInputStream();//odebranie wiadomosci od serwera
                BufferedReader buildreader = new BufferedReader(new InputStreamReader(inputstream));
                String answer = buildreader.readLine();//przypisanie do stringa odczytanej wiadomosci
                if (answer != "INVALID_COMMAND") {//sprawdzenie czy serwer zrozumial zadanie
                    System.out.println("OTRZYMANO WIADOMOSC: " +answer);//wyswietlenie otrzymanej wiadomosci
                    configbufor = answer.split(" ");//podzial ciagu tekstu i zaladowanie do bufora
                    //wyswietlenie bufora, zeby sprawdzic czy dobre    wywalic
                    for(int i=0;i<7;i++){
                        System.out.println(configbufor[i]);}
                    SetBasicConfic();//wywolanie nizje zdefiniowanej metody
                }
                else if(answer == "INVALID_COMMAND"){
                    System.out.println("OTRZYMANO WIADOMOSC: " +answer);
                }
            }
            else{
                System.out.println("Blad socketa");//w przypadku, gdy serversocket null
            }
        }catch (IOException e) {
            System.out.println("Dane nie mogły zostać pobrane z serwera zostać pobrane z serwera lub wystapil inny blad");
            System.out.println(e);
        }
    }

    /**
     * Metoda przypisujaca pobrane podstawowe dane konfiguracyjne do zmiennych aplikacji
     */
    private void SetBasicConfic()
    {
        //przypisanie wartosci z bufora odpowiednim zmiennym
        Boardheight = Integer.valueOf(configbufor[0]);
        Boardwidth = Integer.valueOf(configbufor[1]);
        MainFrameheight = Integer.valueOf(configbufor[2]);
        MainFramewidth = Integer.valueOf(configbufor[3]);
        HighscoresFrameSize = Integer.valueOf(configbufor[4]);
        TimeToExplosion = Integer.valueOf(configbufor[5]);
        //ilosc poczatkowych punktow, zawsze 0;
        Amountofpoints=0;
        PointsForCreate = Integer.valueOf(configbufor[6]);
        PointsForMonster = Integer.valueOf(configbufor[7]);
        PointsForItem = Integer.valueOf(configbufor[8]);
        PointsForChestOfGold = Integer.valueOf(configbufor[9]);
        PointsForKey = Integer.valueOf(configbufor[10]);
        PointsForLevel = Integer.valueOf(configbufor[11]);
        PointsForSecond = Integer.valueOf(configbufor[12]);
    }

    /**
     * Metoda pobierajace dane, dotyczace konkretnego poziomu
     */

    public static void GetLevelConfig(Socket socket, int number_of_level){
        try {
            if (socket != null) {//sprawdzenie czy gniazdo serwera nie jest nullem(czy zostalo "cos" przypisane)
                socket.getInputStream().skip(socket.getInputStream().available());//pominiecie odpowiedzi serwera
                OutputStream outputstream = socket.getOutputStream();//strumien wyjsciowy
                PrintWriter printwriter = new PrintWriter(outputstream, true);//przywiazanie do strumienia wyjsciowego
                //wiadomosci ktora bedziemy wysylac
                String level = Integer.toString(number_of_level);//konwersja na text numer poziomu ktory chcemy pobac
                printwriter.println("GET_LEVEL: "+level+"\n");//utworzenie wiadomosci
                System.out.println("WYSLANO WIADOMOSC: GET_LEVEL: " +level);
                InputStream inputstream = socket.getInputStream();//odebranie wiadomosci od serwera
                BufferedReader buildreader = new BufferedReader(new InputStreamReader(inputstream));
                String answer = buildreader.readLine();//przypisanie do stringa odczytanej wiadomosci
                answer.trim();//wyczyszczenie z bialych znakow

                if(answer.equals("INVALID_COMMAND")){
                    System.out.println("OTRZYMANO WIADOMOSC: " +answer);
                    LoadLevel=false;//nie mozna zaladowac poziomu
                }else if (answer.equals("LEVEL_NOT_FOUND")) {
                    System.out.println("OTRZYMANO WIADOMOSC: " + answer);
                    LoadLevel =false;//nie mozna zaladowac poziomu
                    GameOver gameover = new GameOver();
                    gameover.setVisible(true);
                }else if (!answer.equals("INVALID_COMMAND" ) && !answer.equals("LEVEL_NOT_FOUND")) {//sprawdzenie czy serwer zrozumial zadanie
                    LoadLevel = true;//mozna zaladowac poziom
                    System.out.println("OTRZYMANO WIADOMOSC: " +answer);//wyswietlenie otrzymanej wiadomosci
                    levelbufor = answer.split("&");//podzial ciagu tekstu i zaladowanie do bufora
                    SetLevelConfig();//wywolanie metody obrabiajacej dane
                    WhiChLevel=number_of_level;//ustawienie numeru poziomu na panelu gornym gry
                }
            }
            else{
                System.out.println("Blad socketa");//w przypadku, gdy serversocket null
            }
        }catch (IOException e) {
            System.out.println("Dane nie mogły zostać pobrane z serwera zostać pobrane z serwera lub wystapil inny blad");
            System.out.println(e);
        }
    }

    /**
     * Metoda obrabiajaca otrzymane dane, przypisujaca dane do zmiennych aplikacji
     */

    private static void SetLevelConfig(){
        try{
            String[] basicparametersbufor ;//bufor pomocniczy na podstawowe parametry levela
            basicparametersbufor = levelbufor[0].split(" ");//rozdzielenie parametrow i przypisanei od bufora

            Amountofcolumns = Integer.valueOf(basicparametersbufor[0]);
            Amountoflines = Integer.valueOf(basicparametersbufor[1]);
            Monsterspeed = Integer.valueOf(basicparametersbufor[2]);
            Amountoflifes = Integer.valueOf(basicparametersbufor[3]);
            Amountofordinarybombs = Integer.valueOf(basicparametersbufor[4]);
            Amountofremotebombs = Integer.valueOf(basicparametersbufor[5]);
            Amountofhusarswings = Integer.valueOf(basicparametersbufor[6]);
            Amountoflasers = Integer.valueOf(basicparametersbufor[7]);
            Amountofkeys = Integer.valueOf(basicparametersbufor[8]);
            LevelTime  = Integer.valueOf(basicparametersbufor[9]);

            String[] rowsbufor;//bufor pomocniczy do poszczegolnych wierszy planszy
            rowsbufor = levelbufor[1].split("%");//podzielenie mapy na wiersze
            row = new String[Amountoflines];//utworzenie tablicy wierszy wykorzystawej w klasie Panelboard
            for(int i=0;i<Amountoflines;i++) {
                row[i] = rowsbufor[i];//przypisanie kazdego wiersza z bufora pomocniczego do bufora gry
            }
        }catch(Exception e){
            System.out.println(e+"Blad metody SetLevelConfig ");
        }
    }
    /**
     * Metoda skalujaca elementy skladowe okna wyboru tryby rozgrywki
     * @param g
     */
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        Question.setBounds(0,getHeight()/5,getWidth(),getHeight()/6);
        Yes.setBounds(getWidth()/12,(getHeight())/2,getWidth()/3,getHeight()/6);
        No.setBounds((7*getWidth())/12,(getHeight())/2,getWidth()/3,getHeight()/6);
    }

}
