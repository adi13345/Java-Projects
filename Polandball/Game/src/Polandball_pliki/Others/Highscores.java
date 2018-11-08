package Polandball_pliki.Others;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import static Polandball_pliki.Others.GetConstans.HighscoresFrameSize;


/**
 * Klasa odpowiadaja za pobranie z serwera najlepszych wynikow, sparsowanie pobranego pliku i wyswieltenie go
 */
public class Highscores {

    /**
     * Zmienna serversocket, do ktorej przypisane jest gniazdo serwera
     */

    public static Socket serversocket;

    /**
     * Tablica wynikow wraz z nazwami graczy
     */

    public static String[] results = new String[10];

    /**
     * Flaga informujaca o pustej liscie najlepszych wynikow
     */
    public static boolean emptyhighscorelist=false;
    /**
     * Konstruktor klasy Highscores
     */

    public Highscores(){}

    /**
     * Metoda pobierajaca z serwera liste najlepszych wynikow
     * @param socket - gniazdo klienta
     */

    public static String[] GetHighscores (Socket socket) {
        serversocket = socket;//przypisanie gniazda serwera
        try {
        if (socket != null) {//sprawdzenie czy gniazdo serwera nie jest nullem(czy zostalo "cos" przypisane)
                socket.getInputStream().skip(socket.getInputStream().available());//pominiecie odpowiedzi serwera
                OutputStream outputstream = socket.getOutputStream();//strumien wyjsciowy
                PrintWriter printwriter = new PrintWriter(outputstream, true);//przywiazanie do strumienia wyjsciowego
                printwriter.println("GET_HIGHSCORES\n");//utworzenie wiadomosci GET_HIGHSCORES    //wiadomosci ktora bedziemy wysylac
                System.out.println("WYSLANO WIADOMOSC: GET_HIGHSCORES");
                InputStream inputstream = socket.getInputStream();//odebranie wiadomosci od serwera
                BufferedReader buildreader = new BufferedReader(new InputStreamReader(inputstream));
                String answer = buildreader.readLine();//przypisanie do stringa odczytanej wiadomosci
                answer.trim();
            if(answer.equals("")){//sprawdzenie czy otrzymana wiadomosc nie jest pusta
                    emptyhighscorelist =true;
                }
                else if (!answer.equals("INVALID_COMMAND")) {//sprawdzenie czy serwer zrozumial zadanie
                    System.out.println("OTRZYMANO WIADOMOSC: " +answer);//wyswietlenie otrzymanej wiadomosci
                    //znak " / " oddziela kazdy wynik rogrywki i nick od reszty
                    //znak " _ " oddziela dany wynik od gracza, ktory go uzyskal
                    //podzielenie ciagu tekstu i przypisanie go do zmiennej results
                    results=answer.split("/");
                    emptyhighscorelist =false;
                }
                else if(answer.equals("INVALID_COMMAND")){
                    System.out.println("OTRZYMANO WIADOMOSC: " +answer);

                }
            }
        else{
            System.out.println("Blad socketa");//w przypadku, gdy serversocket null
        }

        }catch (IOException e) {
            System.out.println("Dane nie mogły zostać pobrane z serwera lub wystapil inny blad");
            System.out.println(e);
        }
        return results;
    }

    /**
     * Metoda wyswietlajaca listę najlepszych wyników
     */
    public static void ShowHighscores(){
           System.out.println(emptyhighscorelist);
        if(emptyhighscorelist==false) {
               JFrame highscoresview = new JFrame();//utworzenie ramki
               highscoresview.setSize(HighscoresFrameSize, HighscoresFrameSize);//ustawienie rozmiarow,ramka jest kwadratowa
               highscoresview.setBackground(Color.WHITE);//ustawienie tla ramki
               highscoresview.setLayout(new GridLayout(results.length, 3));
               String name_and_score[];//tablica, ktora bedzie nadpisywana po kazdej iteracji, przechowuje nazwe i wynik

               for (int i = 0; i < results.length; i++) {//iterujemy, poki cos jest w tablicy sparsowanej
                   //------------>OGARNAC ROZMIARY<---------------
                   //pobieranie z bufora nazwy gracza i jego wyniki i podzielenie go
                   name_and_score = results[i].split("_");//dzielimy napis, miec oddzielnie nazwe i wynik
                   String number_of_row = String.valueOf(i + 1);//numer wiersza, kolejnosc wynikow

                   //label odpowiedzialny za miejsce w tabeli wynikow
                   JLabel label1 = new JLabel(number_of_row, JLabel.CENTER);
                   label1.setFont(new Font("Serif", Font.PLAIN, (HighscoresFrameSize / 25)));
                   highscoresview.add(label1);

                   //label odpowiedzialny za nazwe danego gracza
                   JLabel label2 = new JLabel(name_and_score[0], JLabel.CENTER);
                   label2.setFont(new Font("Serif", Font.PLAIN, (HighscoresFrameSize / 25)));
                   highscoresview.add(label2);

                   //label odpowiedzialny za wynik danego gracza
                   JLabel label3 = new JLabel(name_and_score[1], JLabel.CENTER);
                   label3.setFont(new Font("Serif", Font.PLAIN, (HighscoresFrameSize / 25)));
                   highscoresview.add(label3);
               }
               //pokazanie ramki z wynikami
               highscoresview.setVisible(true);
           }
    }
}
