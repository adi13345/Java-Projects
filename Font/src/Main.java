import MainFrame.MainFrame;
import ParsingPaths.ParsePaths;
/**
 * Klasa glowna programu
 * Created by Adrian Szymowiat on 14.07.2017.
 */
public class Main{
    /**
     * Glowna funkcja programu
     * @param args
     */
    public static void main (String[] args ){
        ParsePaths parsePaths = new ParsePaths();//wczytanie sciezek - konstruktor klasy ParsePaths
        parsePaths.parseXmlfile();//wywolanie metody wczytujacej sciezki
        MainFrame mainframe = new MainFrame();//stworzenie obiektu okna glownego porgramu
        mainframe.setVisible(true);//wlaczenie widocznosci okna
    }
}
