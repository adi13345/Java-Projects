package ParsingPaths;

import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Klasa odpowiedzialna za parsowanie sciezek do plikow/folderow aplikacji
 * Created by Adrian Szymowiat on 10.08.2017.
 */
public class ParsePaths {
    /**
     * Sciezka do arkusza kalkulacyjnego
     */
    public static String excelfilename_;
    /**
     * Sciezka do pliku serwera ftp
     */
    public static String log4jpropertispath_;
    /**
     * Sciezka do pliku serwera ftp
     */
    public static String userspropertiespath_;
    /**
     * Metoda wczytyujaca(parsujaca) sciezki aplikacji
     */
    public void parseXmlfile(){
        try {
            //------>parsowanie danych z pliku xml<------
            File file = new File("paths.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            //wczytanie danych
            excelfilename_ = doc.getElementsByTagName("ExcelFilePath").item(0).getTextContent();
            log4jpropertispath_ = doc.getElementsByTagName("FTPlo4jproperties").item(0).getTextContent();
            userspropertiespath_ = doc.getElementsByTagName("FTPuserspoperties").item(0).getTextContent();
        }catch(FileNotFoundException er){
            JOptionPane.showMessageDialog(null,"Błąd podczas wczytywania ścieżek aplikacji!\nAplikacja nie będzie działała poprawnie!\nNależy sprawdzić, czy plik paths.xml znajduje się w folderze z aplikacją" + er );//komunikat o bledzie
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Błąd podczas wczytywania ścieżek aplikacji!\nAplikacja nie będzie działała poprawnie!\nNależy sprawdzić, czy plik paths.xml znajduje się w folderze z aplikacją" + e );//komunikat o bledzie
        }
    }
}
