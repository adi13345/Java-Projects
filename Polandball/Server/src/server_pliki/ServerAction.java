package server_pliki;

import java.io.BufferedReader;
import java.io.FileReader;
import static server_pliki.ParseConfigFile.*;
import static server_pliki.ParseLevelFile.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * Klasa obslugujaca zlecenia od klienta otrzymane przez serwer
 */
public class ServerAction {

    /**
     * Napis, zalezny od odpowiedzi serwera
     */

    public static String serverflag_;

    /**
     * Bufor potrzebny do wyciagniecia informacji o zadanym przez klienta poziomie
     */

    public static String bufor[] = new String[2];

    /**
     * Flaga sluzaca do weryfikacji, czy serwer dodal gracza do listy najlepszych wynikow, czy tez nie
     */

    public static boolean scoredecision;

    /**
     * Metoda oblsugujaca zadania od klienta
     * @param command zadanie klienta
     * @return odpowiedz serwera - wiadomosc jaka zostanie wyslana do klienta
     */
    public static String ServerResponse(String command) {
        String servercommand = command;//przypisanie otrzymanego zadanie do zmiennej
        String servermessage;//wiadomosc zwracana przez metode

        switch (servercommand) {//obsluga zadan na podstawie otrzymanej wiadomosci od klienta
            case "GET_HIGHSCORES":
                servermessage = GetHighscores();//wywolanie metody GetHighscores, pobranie listy najlepszych wynikow
                serverflag_ = "HIGHSCORES: ";
                break;
            case "GET_CONFIGFILE":
                servermessage = GetConfigfile();//wywolanie metody ladujacej sparsowane zmienne do napisu-budowanie wiadomosci
                serverflag_ = "CONFIGFILE: ";
                break;
            default:
                try {
                    scoredecision = false;//ustawienie na falsz, zeby bylo dobrze
                    //robienie na chama troche, ale dziala, switch musi miec const przypadek a levele rozne bedziemy mieli
                    WhichCommand(command);//wywolanie metody, ktora obrabia otrzymana wiadomosc tekstowa
                    if(bufor[0].equals("PUT_SCORE")){//sprawdzenie czy to nie przypadkiem zadanie dodania gracza do highscoree
                        int bufor2 = Integer.valueOf(bufor[2]);//konwersja tekstowego (stringowego) wyniku na inta
                        PutScore(bufor[1],bufor2);//wywolanie metody dodajacej wynik do highscores
                        if(scoredecision == true) {//sprawdzenie, czy wynik zostal dodany do listy
                            servermessage = "TRUE";
                            serverflag_ = "SCORE_RESPONSE: ";
                        }else{
                            servermessage ="FALSE";
                            serverflag_ = "SCORE_RESPONSE: ";
                        }
                    }else {
                        StringBuilder stringbuilder = new StringBuilder();
                        stringbuilder.append("GET_LEVEL: " + bufor[1]);//tworzenie Stringa, zeby porownac z wiadomoscia
                        String makecommandlevel = stringbuilder.toString();//konwersja na String
                        System.out.println(makecommandlevel);
                        if (servercommand.equals(makecommandlevel) == true) {//sprawdzenie odebrane zadanie dotyczy levela
                            int whichlevel = Integer.valueOf(bufor[1]);
                            servermessage = GetLevelConfig(whichlevel);//wywolanie metody tworzacej dane poziomu do wyslania
                            if(IsLevelAvailable == true) {
                                serverflag_ = "LEVEL: " + bufor[1] + ": ";
                            }else {
                                servermessage = "LEVEL_NOT_FOUND";
                                StringBuilder serverflagclearing = new StringBuilder();
                                serverflag_ = serverflagclearing.toString();//czyszczenie tej flagi
                            }
                        } else {
                            throw new Exception("Nieznana komenda");//rzucenie wyjatku, jak nie wiadomo jak komenda
                        }
                    }
                }
                catch (Exception e){
                    System.out.println(e);
                    servermessage = "INVALID_COMMAND";//w przypadku nieznanego zadania zostanie wyswietlona informacja o nieznaje komendzie
                }
                break;
        }
        return servermessage;//zwrocenie wiadomosci serwera przez metode ServerResponse
    }

    /**
     * Metoda wczytujaca liste najlepszych wynikow
     * @return zwraca wiadomosc (tekst) zawierajaca liste najlepszych wynikow
     */

    private static String GetHighscores() {
        //plik tekstowy zawierajacy liste najlepszych wynikow jest pakowany do stringa, aby mozna bylo go wyslac jako
        StringBuilder stringbuilder = new StringBuilder();//wiadomosc tekstowa;
        String currentline;
        try (
                //wczytywanie kolejnych linijek pliku i dodawanie ich do zmiennej stringbuilder
                //co do pliku tekstowego
                //znak " / " oddziela kazdy wynik rogrywki i nick od reszty
                //znak " _ " oddziela dany wynik od gracza, ktory go uzyskal
                BufferedReader buildreader = new BufferedReader(new FileReader("src\\server_pliki\\highscores.txt"))) {
            while ((currentline = buildreader.readLine()) != null) {
                stringbuilder.append(currentline);
            }
        } catch (Exception e) {
            System.err.println("Nastapil niespodziewany blad");
            System.err.println(e);
        }
        stringbuilder.append("\n");//dodanie znaku konca wiadomosci
        return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
    }

    /**
     * Metoda budujaca napis ze zmiennych sparsowanych w klasie ParseConfigfile
     * @return wiadomosc (tekst) zawierajaca podstawowe parametry aplikacji
     */
    private static String GetConfigfile() {

        ParseConfigFile.ParseConfig();//wywolanie metody z klasy parsujacej plik
        StringBuilder stringbuilder = new StringBuilder();//zmienna, na ktorej bedziemy budowac wiadomsosc tekstowa
        for (int i = 0; i < configbufor.length; i++) {//dodanie do bufora napisow wszystkich sparsowanych zmiennych
            stringbuilder.append(configbufor[i] + " ");//kazda liczbe(napis) oddziela spacja
        }
        stringbuilder.delete(stringbuilder.length() - 1, stringbuilder.length());//usuwamy ostatni " ", zeby latwiej sie obrabialo
        stringbuilder.append("\n");//dodanie znaku konca wiadomosci
        return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
    }

    /**
     * Metoda budujaca napis ze zmiennych sparsowanych w klasie ParseLevelFile
     * @param number_of_level numer levela, ktory chcemy odczytac
     * @return wiadomosc (tekst) zawierajaca podstawowe parametry danego poziomu
     */

    private static String GetLevelConfig(int number_of_level) {
        try {//jak metoda WhichLevel sie dobrze wykonala to nastepuje tworzenie wiadomosci
            if(number_of_level >0) {
                ParseLevelFile.ParseLevelFile(number_of_level);//wywolanie metody z klasy parsujacej plik);
                StringBuilder stringbuilder = new StringBuilder();//zmienna, na ktorej bedziemy budowac wiadomsosc tekstowa
                for (int i = 0; i < levelbufor.length; i++) {//dodanie do bufora napisow wszystkich sparsowanych zmiennych
                    stringbuilder.append(levelbufor[i] + " ");//kazda liczbe(napis) oddziela spacja
                }
                stringbuilder.delete(stringbuilder.length() - 1, stringbuilder.length());//usuwamy ostatni " ", zeby latwiej sie obrabialo
                stringbuilder.append("&");//dodanie na poczatku znak "&", bo ta wiadomosc bedzie sklejana z inna
                for (int i = 0; i < buforrow.length; i++) {
                    stringbuilder.append(buforrow[i] + "%");//kazda liczbe(napis) oddziela "%"
                }
                stringbuilder.delete(stringbuilder.length() - 1, stringbuilder.length()); //usuwamy ostatni "%", zeby latwiej sie obrabialo
                stringbuilder.append("\n"); //dodanie znaku konca wiadomosci
                return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
            }
        }catch (Exception e){
            System.out.println(e + "Blad metody GetLevelConfig");
        }
        return "LEVEL_NOT_FOUND";
    }

    /**
     * Metoda tworza bufor przechowujacy komendy szczegolne,np. zadanie danych poziomu
     * @param command zadanie klienta
     */
    private static void WhichCommand(String command){
        try {
            bufor = command.split(" ");//podzielenie komendy, ktora jest obslugiwana defaultowo
        }catch(Exception e){
            System.out.println(e + "Blad metody WhichLevel");
        }
    }

    /**
     * Metoda dodajaca (lub nie) gracza do listy najlepszych wynikow
     * @param playername - nazwa gracza, ktora otrzymal serwer
     * @param score - wynik gracza
     */
    private static void PutScore(String playername, int score){
        try {
            boolean IsHighscoreListFull=false;//flaga informujaca czy mamy pelna liste wynikow, wazne przy sklejaniu napisu
            String highscoresbufor[];//bufor do zaladowania calego highscores
            String helpbufor[];//pomocniczy bufor na rozdzielenie nazwy gracza od wyniku
            File file = new File("src\\server_pliki\\highscores.txt");//otworzenie pliku
            Scanner in = new Scanner(file);//wczytanie zawartosci
            if(in.hasNext() == false){//jesli plik pusty do dodajemy na pewno
                StringBuffer stringbuilder = new StringBuffer();
                stringbuilder.append(playername+"_"+score+"/");
                PrintWriter printwriter = new PrintWriter("src\\server_pliki\\highscores.txt");
                printwriter.println(stringbuilder.toString());
                printwriter.close();
                scoredecision = true;
            }else {
                highscoresbufor = in.nextLine().split("/");//rozdzielenie cale napisu z highscores
                String highscoretable[][] = new String[2][10];//dwuwymiarowa tablica, kazda kolumna to nazwa gracza i wynik
                for (int i = 0; i < highscoresbufor.length; i++) {
                    helpbufor = highscoresbufor[i].split("_");//rozdzielenie nazwy gracza od wyniku
                    highscoretable[0][i] = helpbufor[0];//przypisanie nazwy gracza do odpowiedniego pola
                    highscoretable[1][i] = helpbufor[1];//przypisanie wyniku gracza do odpowiedniego pola
                }
                if (highscoresbufor.length < 10) {//sprawdzamy czy jest miejsce w highscores
                    IsHighscoreListFull = false;
                    for (int i = 0; i < highscoresbufor.length; i++) {//lecimy po wszystkich wynikach
                        if (score > Integer.valueOf(highscoretable[1][i])) {//sprawdzamy czy przeslany do nas wynik jest wiekszy od aktualnei znajdujacego sie
                            String highscoretablecopy[][] = new String[2][highscoresbufor.length - i];//kopia wynikow, które sa mniejsze od tego, ktory chcemy dodac
                            for (int j = i; j < highscoresbufor.length; j++) {//kopiujemy pozostala czesc tablicy (ktora trzeba przesunac)
                                highscoretablecopy[0][j - i] = highscoretable[0][j];
                                highscoretablecopy[1][j - i] = highscoretable[1][j];
                            }
                            highscoretable[0][i] = playername;//wstawiamy nazwe gracza przeslana do serwera
                            highscoretable[1][i] = Integer.toString(score);//wstawiamy wynik przeslany do serwera
                            for (int k = 0; k < (highscoresbufor.length - i); k++) {//doklejamy pozostale wyniki
                                highscoretable[0][i + k + 1] = highscoretablecopy[0][k];
                                highscoretable[1][i + k + 1] = highscoretablecopy[1][k];
                            }
                            scoredecision = true;//informacja, ze wynik zostal dodany do listy
                            break;
                        }
                    }
                } else if (highscoresbufor.length == 10) { //jesli highscores jest pelny to trzeba bedzie jeden wynik wywalic
                    IsHighscoreListFull = true;
                    for (int i = 0; i < highscoresbufor.length; i++) {//lecimy po wszystkich wynikach
                        if (score > Integer.valueOf(highscoretable[1][i])) {//sprawdzamy czy przeslany do nas wynik jest wiekszy od aktualnei znajdujacego sie
                            String highscoretablecopy[][] = new String[2][highscoresbufor.length - i];//kopia wynikow, które sa mniejsze od tego, ktory chcemy dodac
                            for (int j = i; j < highscoresbufor.length; j++) {//kopiujemy pozostala czesc tablicy (ktora trzeba przesunac)
                                highscoretablecopy[0][j - i] = highscoretable[0][j];
                                highscoretablecopy[1][j - i] = highscoretable[1][j];
                            }
                            highscoretable[0][i] = playername;//wstawiamy nazwe gracza przeslana do serwera
                            highscoretable[1][i] = Integer.toString(score);//wstawiamy wynik przeslany do serwera
                            for (int k = 0; k < (highscoresbufor.length - i - 1); k++) {//doklejamy pozostale wyniki TU RÓŻNICA W STOSUNKU GDY NIE MAMY PELNEGO HIGHSCOES
                                highscoretable[0][i + k + 1] = highscoretablecopy[0][k];
                                highscoretable[1][i + k + 1] = highscoretablecopy[1][k];
                            }
                            scoredecision = true;//informacja, ze wynik zostal dodany do listy
                            break;
                        }
                    }
                }
                // budowanie na nowo listy wynikow
                StringBuffer stringbuilder = new StringBuffer();
                if (IsHighscoreListFull == false) {
                    for (int i = 0; i < (highscoresbufor.length + 1); i++) {
                        stringbuilder.append(highscoretable[0][i] + "_" + highscoretable[1][i] + "/");
                    }
                } else if (IsHighscoreListFull == true) {
                    for (int i = 0; i < highscoresbufor.length; i++) {
                        stringbuilder.append(highscoretable[0][i] + "_" + highscoretable[1][i] + "/");
                    }
                }
                stringbuilder.delete(stringbuilder.length() - 1, stringbuilder.length());//usuniecie ostatniego "/"
                System.out.print(stringbuilder.toString() + "\n");
                //zapisanie nowej listy wynikow pliku
                PrintWriter printwriter = new PrintWriter("src\\server_pliki\\highscores.txt");
                printwriter.println(stringbuilder.toString());
                printwriter.close();
            }

        }catch(FileNotFoundException e){
           System.out.println(e + " Problem z otwrciem pliku ");
        } catch(Exception e){
            System.out.println(e + "Blad metody Putscore, klasa ServerAction");
        }
    }
}

