package server_pliki;


import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * <p>Klasa odpowiedzialna za parsowanie pliku serwera z parametrami konkretnego poziomu</p>
 * <p>Przykladowa zartosc pliku z parametrami poziomu, pokazuja kolejnosc parsowania i przypisania do bufora</p>
 * <p>Ilosc kolumn/wierszy musi sie zgadzac z iloscia row-ow oraz liczba pol, jaka zawiera kazdy row</p>
 * <p>Nalezy stosowac notacje xml, jak w przykladowych plikach konfiguracyjnych</p>

 <p>Amountofcolumns 20 Amountofcolumns  - Ilosc kolumn planszy gry</p>
 <p> Amountoflines 20 Amountoflines - Ilosc wierszy planszy gry</p>
 <p>Monsterspeed 3 Monsterspeed   - Predkosc potwora na danym poziomie</p>
 <p>Amountoflifes 2 Amountoflifes   - Ilosc zyc na danym poziomie</p>
 <p>Amountofordinarybombs 50 Amountofordinarybombs   - Ilosc bomb zwyklych na danym poziomie</p>
 <p>Amountofremotebombs 50 Amountofremotebombs   - Ilosc bomb zdalnych na danym poziomie</p>
 <p>Amountofhusarswings 1 Amountofhusarswings  - Ilosc skrzydel husarskich na danym poziomie</p>
 <p>Amountoflasers 1 Amountoflasers  - Ilosc laserow na danym poziomie</p>
 <p>Amountofkeys 1 Amountofkeys - Ilosc kluczy na danym poziomie, zalecane ustawienie tej wartosc na 0</p>
 <p>LevelTime 760 LevelTime   - Czas danego poziomie</p>


 <p>Ponizszy kod zawiera uklad poszczegolnych pol na danym poziomie, oznaczenia:</p>
 <p>N_ - puste pole</p>
 <p>B_ - beton, nieznisczalna przeszkoda</p>
 <p>S_ - skrzynka, przeszkoda, ktora mozna zniszczyc</p>
 <p>NG - pozycja startowa gracza</p>
 <p>NW_ - pozycja startowa wroga</p>
 <p>SD - skrzynka, pod ktora kryja sie drzwi</p>
 <p>SI - skrzynka, pod ktora kyje sie item, rodzaje itemow sa zawarte w dokumentacji klienta gry</p>
 <p>SK - skrzynka, pod znajduje sie klucz do drzwi</p>

 <p>row N_ NG N_ N_ N_ NW NW N_ N_ N_ N_ NW N_ N_ N_ S_ S_ S_ N_ NW row</p>
 <p>row N_ N_ N_ B_ S_ S_ S_ S_ S_ B_ B_ B_ B_ SI S_ N_ N_ NW N_ N_ row</p>
 <p>row N_ B_ B_ B_ S_ S_ SI S_ SI B_ S_ S_ S_ s_ S_ SI S_ B_ S_ S_ row</p>
 <p>row N_ N_ N_ N_ N_ N_ N_ N_ N_ N_ S_ S_ S_ S_ S_ N_ S_ S_ S_ S_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ N_ N_ N_ N_ S_ SI S_ S_ S_ S_ S_ B_ S_ S_ row</p>
 <p>row NW N_ N_ S_ S_ S_ N_ N_ N_ N_ N_ NW N_ N_ N_ N_ S_ S_ S_ S_ row</p>
 <p>row N_ N_ N_ B_ S_ S_ N_ N_ N_ B_ B_ B_ B_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ B_ SI S_ N_ N_ N_ N_ S_ S_ S_ s_ SI S_ S_ B_ S_ S_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ N_ N_ S_ S_ S_ S_ S_ S_ N_ N_ NW N_ N_ N_ N_ N_ NW N_ N_ row</p>
 <p>row NW N_ N_ B_ S_ S_ S_ S_ S_ B_ B_ B_ B_ B_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ B_ S_ S_ S_ S_ S_ B_ S_ S_ S_ s_ S_ N_ N_ N_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ B_ S_ S_ S_ S_ S_ N_ N_ N_ B_ B_ row</p>
 <p>row NW N_ N_ S_ S_ S_ N_ N_ N_ N_ N_ NW N_ N_ N_ N_ S_ S_ S_ S_ row</p>
 <p>row N_ N_ N_ B_ S_ S_ N_ N_ N_ B_ B_ B_ B_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ B_ S_ S_ N_ N_ N_ N_ S_ S_ S_ s_ S_ S_ S_ B_ S_ S_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ SI S_ SI S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ SI S_ S_ S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ B_ S_ S_ S_ S_ B_ S_ S_ S_ S_ SD row</p>
 */
public class ParseLevelFile {

    /**
     * Ilosc kolumn
     **/

    public static String Amountofcolumns;

    /**
     *Ilosc wierszy
     **/

    public static String Amountoflines;

    /**
     * Szybkosc potworow
     **/

    public static String Monsterspeed;

    /**
     * Ilosc zyc na poczatku rozgrywki
     **/

    public static String Amountoflifes;


    /**
     * Ilosc zwykłych bomb
     */

    public static String Amountofordinarybombs;

    /**
     * ilosc bomb zdalnych
     */

    public static String Amountofremotebombs;

    /**
     * Ilosc skrzydel husarskich
     */

    public static String Amountofhusarswings;

    /**
     * Ilosc lasrów
     */

    public static String Amountoflasers;

    /**
     * Ilosc bomb zdalnych
     */

    public static String Amountofkeys;

    /**
     * Czas konkretnego poziomu
     */
    public static String LevelTime;
    /**
     * String przechowujcy informacje o wszystkich polach danego poziomu.
     * W kolejnych elementach tablicy zawarte sa kolejne linie planszy
     */

    public static String buforrow[];

    /**
     * Bufor zawierajacy dane konfiguracyjne
     */

    public static String levelbufor[];

    /**
     * Flaga informująca, czy serwer posiada dane zadanego poziomu
     */

    public static boolean IsLevelAvailable;

    /**
     * Metoda tworzaca sciezke do poziomu
     * @param level numer poziomu, ktory chcemy wczytac
     * @return path - sciezka do konkretnego levela
     */

    private static String create_path_to_level(int level){
        String first_part="src\\server_pliki\\Level";
        String second_part=Integer.toString(level);
        String third_part=".xml";

        String path=first_part+second_part+third_part;
        return path;
    }

    /**
     * Metoda wczytyjaca level
     * @param level numer poziomu, ktory chcemy wczytac
     */
    public static void ParseLevelFile(int level){
        try {
            IsLevelAvailable = true;//zakladamy ze dany poziom jest dostepny
            //sciezka do konkretne levela
            String path_to_level=create_path_to_level(level);
            File file = new File(path_to_level);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            levelbufor = new String[10];

            Amountofcolumns = doc.getElementsByTagName("Amountofcolumns").item(0).getTextContent();
            levelbufor[0] = Amountofcolumns;
            Amountoflines = doc.getElementsByTagName("Amountoflines").item(0).getTextContent();
            levelbufor[1] = Amountoflines;
            Monsterspeed = doc.getElementsByTagName("Monsterspeed").item(0).getTextContent();
            levelbufor[2] = Monsterspeed;
            Amountoflifes = doc.getElementsByTagName("Amountoflifes").item(0).getTextContent();
            levelbufor[3] = Amountoflifes;
            Amountofordinarybombs = doc.getElementsByTagName("Amountofordinarybombs").item(0).getTextContent();
            levelbufor[4] = Amountofordinarybombs;
            Amountofremotebombs = doc.getElementsByTagName("Amountofremotebombs").item(0).getTextContent();
            levelbufor[5] = Amountofremotebombs;
            Amountofhusarswings = doc.getElementsByTagName("Amountofhusarswings").item(0).getTextContent();
            levelbufor[6] = Amountofhusarswings;
            Amountoflasers = doc.getElementsByTagName("Amountoflasers").item(0).getTextContent();
            levelbufor[7] = Amountoflasers;
            Amountofkeys = doc.getElementsByTagName("Amountofkeys").item(0).getTextContent();
            levelbufor[8] = Amountofkeys;
            LevelTime = doc.getElementsByTagName("LevelTime").item(0).getTextContent();
            levelbufor[9]= LevelTime;
            //konwersja stringa - liczby wierszy
            int rowiteration = Integer.valueOf(Amountoflines);
            //pakowanie znakow do bufora
            buforrow = new String[rowiteration];
            for (int i = 0; i < rowiteration; i++) {
                buforrow[i] = doc.getElementsByTagName("row").item(i).getTextContent();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            IsLevelAvailable = false;//jesli nie ma poziomu to chlip
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
