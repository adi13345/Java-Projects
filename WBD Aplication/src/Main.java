
/**
 * Klasa glowna programu
 * Created by Adrian Szymowiat on 08.01.2018.
 */
public class Main{
    public static MainConnection mainConnection;
    /**
     * Glowna funkcja programu
     * @param args
     */
    public static void main (String[] args ){
        GetConfig getConfig = new GetConfig();
        getConfig.getDefaultParameters();//wczytanie parametrów z pliku konfiguracyjnego
        mainConnection = new MainConnection();
        mainConnection.setVisible(true);
    }
}