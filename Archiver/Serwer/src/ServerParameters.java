import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Klasa odpowiedzialna za wczytywanie parametrow konfiguracyjnych serwera
 */
public class ServerParameters {
    /**
     * Numer portu serwera
     */
    public static int server_port;
    /**
     * Metoda wczytyjaca parametry z pliku xml
     */
    public static void ParseServerParameters() {
        try {
            //parsowanie parametrow z pliku konfiguracyjnego
            File file = new File("server_parameters.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            server_port = Integer.parseInt(doc.getElementsByTagName("Server_Port").item(0).getTextContent());//wczytanie numeru portu serwera

        } catch (FileNotFoundException e) {
            System.out.println(ConsoleFrame.getTimeandDate()+ "Nie można otworzyć pliku z parametrami serwera");//komunikat na konsole Intellij
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Nie można otworzyć pliku z parametrami serwera");//komunikat do logow
        } catch (Exception e) {
            System.out.println(ConsoleFrame.getTimeandDate()+ "Nastąpił niespodziewany błąd podczas wczytywania parametrów serwera: "+e);//komunikat na konsole Intellij
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Nastąpił niespodziewany błąd podczas wczytywania parametrów serwera: "+e);//komunikat do logow
        }
    }
}
