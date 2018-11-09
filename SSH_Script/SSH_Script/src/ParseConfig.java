import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Adrian Szymowiat on 29.08.2018
 * Klasa parsujaca dane konfiguracyjne
 */
public class ParseConfig {
    /**
     * Sciezka do folderu, w ktorym maja byc zapisywane logi
     */
    public static String pathToLogFolder;
    /**
     * Timemout sesji
     */
    public static int sessionTimeout;
    /**
     * Timeout kanalu
     */
    public static int channelTimeout;
    /**
     * Numer portu SSH
     */
    public static int sshPort;
    /**
     * Adres hosta
     */
    public static String hostIpAddress;
    /**
     * Nazwa uzytkownika
     */
    public static String userName;
    /**
     * Haslo uzytkownika
     */
    public static String userPassword;
    /**
     * Bufor z komendami
     */
    public static ArrayList<String> commandsBufor;
    /**
     * Czas trwania sesji
     */
    public static int sessionTime;
    /**
     * Metoda wczytujaca dane konfiguracyjne
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void getConfig() throws ParserConfigurationException, SAXException, IOException {
        //-->parsowanie danych z pliku configfile.xml<--
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse("config.xml");
        doc.getDocumentElement().normalize();

        pathToLogFolder = doc.getElementsByTagName("path_to_log_folder").item(0).getTextContent();
        if(pathToLogFolder.equals("user.dir")){
            pathToLogFolder = System.getProperty("user.dir");
        }
        sessionTimeout = Integer.valueOf(doc.getElementsByTagName("session_timeout").item(0).getTextContent());
        channelTimeout = Integer.valueOf(doc.getElementsByTagName("channel_timeout").item(0).getTextContent());
        sshPort = Integer.valueOf(doc.getElementsByTagName("ssh_port").item(0).getTextContent());
        hostIpAddress = doc.getElementsByTagName("host_ip_address").item(0).getTextContent();
        userName = doc.getElementsByTagName("user_name").item(0).getTextContent();
        userPassword = doc.getElementsByTagName("user_password").item(0).getTextContent();
        String[] bufor = doc.getElementsByTagName("commands").item(0).getTextContent().split("-");
        commandsBufor = new ArrayList<>();
        for(int i = 0; i < bufor.length; i++){
            String command = bufor[i].replace('+',' ');
            commandsBufor.add(command);
        }
        sessionTime = Integer.valueOf(doc.getElementsByTagName("session_time").item(0).getTextContent());//czas w minutach
    }
}
