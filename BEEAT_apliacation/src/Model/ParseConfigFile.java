package Model;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Locale;


/**
 * Created by Adrian Szymowiat on 01.05.2018.
 * Klasa odpowiedzialna za parsowanie danych konfiguracyjnych
 */
public class ParseConfigFile {
    /**
     * Zmienna okreslajaca odstep czasowy pomiedzy kolejnymi komendami AT
     */
    public static int commandDelay;
    /**
     * Zmienna okreslajaca odstep czasowy pomiedzy kolejnymi automatycznymi czynnosciami zbierania informacji z urzadzania
     */
    public static int controlThreadDelay;
    /**
     * Domyslna sciezka do folderu z numerami
     */
    public static String pathToNumbers;
    /**
     * Domyslna sicezka do folderu z wiadomosciami
     */
    public static String pathToMessages;
    /**
     * Domyslna sciezka do folderu z konfiguracjami
     */
    public static String pathToConfigurations;
    /**
     * Zmienna zawieraja domyslny jezyk aplikacji
     */
    public static Locale locale;
    /**
     * Czas pomiedzy kolejnymi sprawdzeniami wyslania automatycznej wiadomosci
     */
    public static int checkTimePeriodicity;
    /**
     * Metoda odpowiedzialna za parsowanie danych konfiguracyjnych
     * @throws ParserConfigurationException - blad podczas proby parsowania pliku
     * @throws SAXException - blad zwiazany z uzytem typem parsowania pliku
     * @throws IOException - blad wejscia/wyjscia
     */
    public void getConfigurationData() throws ParserConfigurationException, SAXException, IOException {
        //-->parsowanie danych z pliku configfile.xml<--
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(getClass().getResource("/Resources/configfile.xml").getPath());
        doc.getDocumentElement().normalize();

        commandDelay = Integer.valueOf(doc.getElementsByTagName("command_delay").item(0).getTextContent());//wczytanie delay-u
        if (commandDelay < 50) {
            commandDelay = 50;
        }
        controlThreadDelay = Integer.valueOf(doc.getElementsByTagName("controlthread_delay").item(0).getTextContent());//wczytanie delay-u watku kontrolnego
        if (controlThreadDelay < 200) {//minimalny czas odświeżania wątku kontrolnego to 200 ms - załozenie programisty
            controlThreadDelay = 200;
        }
        pathToNumbers = doc.getElementsByTagName("default_path_to_numbers").item(0).getTextContent();//sciezka do numerow
        pathToMessages = doc.getElementsByTagName("default_path_to_messages").item(0).getTextContent();//sciezka do wiadomosci
        pathToConfigurations = doc.getElementsByTagName("default_path_to_configurations").item(0).getTextContent();//sciezka do konfiguracji
        locale = new Locale(doc.getElementsByTagName("default_language").item(0).getTextContent(),
                doc.getElementsByTagName("default_language_region").item(0).getTextContent());//wczytanie zmiennej okreslajacej jezyk +region
        checkTimePeriodicity = Integer.valueOf(doc.getElementsByTagName("check_time_periodicity").item(0).getTextContent());//zaladowanie czasu sprawdzania chetime

    }
}
