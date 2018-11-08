import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Klasa odpowiedzialna za parsowanie danych konfiguracyjnych
 * Created by Adrian Szymowiat on 09.01.2018.
 */
public class GetConfig {
    /**
     * Sterownik JDBC
     */
    static public String JDBC_DRIVER;
    /**
     * Adres bazy danych
     */
    static public String DB_URL;
    /**
     * Nazwa uzytkownika
     */
    static public String USER;
    /**
     * Haslo uzytkownika
     */
    static  public String PASS;
    /**
     * Metoda odpowiedzialna za wczytywanie domyslnych ustawien polaczenia
     */
    public void getDefaultParameters(){
        try {
            File file = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            JDBC_DRIVER = doc.getElementsByTagName("Driver").item(0).getTextContent();
            DB_URL = doc.getElementsByTagName("DB_URL").item(0).getTextContent();
            USER = doc.getElementsByTagName("Username").item(0).getTextContent();
            PASS = doc.getElementsByTagName("Password").item(0).getTextContent();

        }catch(FileNotFoundException er){
            JOptionPane.showMessageDialog(null,"Błąd podczas wczytywania ścieżek aplikacji!\nAplikacja nie będzie działała poprawnie!\n" + er );//komunikat o bledzie
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Błąd podczas wczytywania ścieżek aplikacji!\nAplikacja nie będzie działała poprawnie!\n" + e );//komunikat o bledzie
        }
    }
}