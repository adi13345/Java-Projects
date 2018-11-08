package server_pliki;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;



/**
 * <p>Klasa odpowiedzialna za parsowanie pliku serwera z podstawowymi parametrami aplikacji</p>
 * <p>Przykladowa zawartosc pliku config.xml, pokazujaca kolejnosc parsowania i przypisywania do bufora</p>
 * <p>Nalezy stosowac notacje xml, jak w przykladowych plikach konfiguracyjnych</p>
 *
 <p>Boardheight 700 Boardheight - Wysokosc ramki okna gry</p>
 <p>Boardwidth 1400 Boardwidth - Szerokosc ramki okna gry</p>
 <p>MainFrameheight 200 MainFrameheight - Wysoksc ramki okna glownego</p>
 <p>MainFramewidth 200 MainFramewidth - Szerokosc ramki okna glownego</p>
 <p>HighscoresFrameSize 500 HighscoresFrameSize - Rozmiar ramki okna listy najlepszych wynikow - okno kwadratowe</p>
 <p>TimeToExplosion 1 TimeToExplosion  - Czas, po jakim wybucha bomba zwykla</p>

 <p>PointsForCreate 20 PointsForCreate - Ilosc punktow za zniszczenie skrzynek</p>
 <p>PointsForMonster 100 PointsForMonster  - Ilosc punktow za zabicie potwora</p>
 <p>PointsForItem 10 PointsForItem  - Ilosc punktow zapodniesie itemku</p>
 <p>PointsForChestOfGold 1000 PointsForChestOfGold  - Ilosc punktow za podniesie skrzynki zlota</p>
 <p>PointsForKey 50 PointsForKey  - Ilosc punktow za podniesienie klucza</p>
 <p>PointsForLevel 500 PointsForLevel  - Ilosc punktow za przejscie poziomu</p>
 <p>PointsForSecond 3 PointsForSecond  - Ilosc punktow za kazda sekunde, jesli ukonczymy poziom przed czasem</p>
 */


public class ParseConfigFile {

    /**
     * Sciezka do pliku konfiguracyjnego
     **/
    public static final String Config = "src\\server_pliki\\config.xml";

    /**
     * Wysokosc ramki
     **/

    public static String Boardheight;

    /**
     * Szerokosc ramki
     **/

    public static String Boardwidth;

    /**
     * Wysokość okna głównego - menu
     */

    public static String MainFrameheight;

    /**
     * Szerokość okna głównego - menu
     */

    public static String MainFramewidth;

    /**
     * Rozmiar okna Highscores, ramka jest kwadratowa
     */

    public static String HighscoresFrameSize;

    /**
     * Zmienna zawierajaca ilosc sekund, po ktorym wybuchnie bomba
     */

    public static String TimeToExplosion;

    /**
     * Punkty za zniszczenie skrzynek
     */

    public static String PointsForCreate;

    /**
     * Punkty za zabicie potwora
     */

    public static String PointsForMonster;

    /**
     * Punkty za podniesienie przedmiotu
     */

    public static String PointsForItem;

    /**
     * Punkty za zdobycie skrzynki zlota
     */

    public static String PointsForChestOfGold;

    /**
     * Punkty za zdobycie klucza
     */

    public static String PointsForKey;

    /**
     * Punkty za przejscie poziomu
     */

    public static String PointsForLevel;

    /**
     * Punkty bonusowe za kazda sekunde pozostala do uplyniecia ustalonego progu czasowego
     */

    public static String PointsForSecond;

    /**
     * Bufor, gdzie beda przetrzymywane dane konfiguracyjne
     */

    public static String[] configbufor;
    /**
     * Metoda parsujaca plik config.xml
     */

    public static void ParseConfig(){

        try {
            File file = new File(Config);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            configbufor = new String[13];
            Boardheight = doc.getElementsByTagName("Boardheight").item(0).getTextContent();
            configbufor[0] = Boardheight;
            Boardwidth = doc.getElementsByTagName("Boardwidth").item(0).getTextContent();
            configbufor[1] = Boardwidth;
            MainFrameheight = doc.getElementsByTagName("MainFrameheight").item(0).getTextContent();
            configbufor[2] = MainFrameheight;
            MainFramewidth = doc.getElementsByTagName("MainFramewidth").item(0).getTextContent();
            configbufor[3] = MainFramewidth;
            HighscoresFrameSize = doc.getElementsByTagName("HighscoresFrameSize").item(0).getTextContent();
            configbufor[4] = HighscoresFrameSize;
            TimeToExplosion = doc.getElementsByTagName("TimeToExplosion").item(0).getTextContent();
            configbufor[5] = TimeToExplosion;
            PointsForCreate = doc.getElementsByTagName("PointsForCreate").item(0).getTextContent();
            configbufor[6] = PointsForCreate;
            PointsForMonster = doc.getElementsByTagName("PointsForMonster").item(0).getTextContent();
            configbufor[7] = PointsForMonster;
            PointsForItem = doc.getElementsByTagName("PointsForItem").item(0).getTextContent();
            configbufor[8] = PointsForItem;
            PointsForChestOfGold = doc.getElementsByTagName("PointsForChestOfGold").item(0).getTextContent();
            configbufor[9] = PointsForChestOfGold;
            PointsForKey = doc.getElementsByTagName("PointsForKey").item(0).getTextContent();
            configbufor[10] = PointsForKey;
            PointsForLevel = doc.getElementsByTagName("PointsForLevel").item(0).getTextContent();
            configbufor[11] = PointsForLevel;
            PointsForSecond = doc.getElementsByTagName("PointsForSecond").item(0).getTextContent();
            configbufor[12] = PointsForSecond;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
