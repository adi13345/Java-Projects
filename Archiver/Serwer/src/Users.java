import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za wczytywanie parametrow konfiguracyjnych serwera
 */
public class Users {
    /**
     * Tablica zawieraja informacje o wszystkich uzytkownikach
     */
    public static ArrayList<String[]> userstable;
    /**
     * Metoda wczytyjaca parametry z pliku xml
     */
    public static void ParseUsers() {
        try {
            //parsowanie danych uzytkownikow z pliku konfiguracyjnego
            File file = new File("users.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            userstable = new ArrayList<>();//stworzenie tablicy uzytkownikow
            NodeList nodeList = doc.getElementsByTagName("User");//lista userow
            for(int i = 0; i <nodeList.getLength();i++){//lecimy tyle razy, ile jest userow
                Node node = nodeList.item(i);//bierzemy kazdego usera z listy userow po kolei
               if (node.getNodeType() == Node.ELEMENT_NODE) {//cos tam
                    Element element = (Element) node;//rzutowanie na Element zeby mozna bylo pobrac informacje
                    String[] bufor = new String[4];//statyczna tablica stringow - kazda przechowujaca informacje o konkretnym uzytkowniku
                    bufor[0] = element.getElementsByTagName("Login").item(0).getTextContent();//pobranie loginu danego usera
                    bufor[1] = element.getElementsByTagName("Password").item(0).getTextContent();//pobranie hasla danego usera
                    bufor[2] = element.getElementsByTagName("ID").item(0).getTextContent();//pobranie ID danego usera
                    bufor[3] = element.getElementsByTagName("Path").item(0).getTextContent();//pobranie sciezki do folderu danego usera
                    userstable.add(bufor);//dodanie bufora (user+haslo+informacje) do tablicy uzytkownikow
               }
            }
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Dane dostępnych użytkowników: ");//komunikat do logow
            for (int j = 0; j < userstable.size();j++){//dodanie informacji o uzytkownikach do logow
                StringBuilder stringBuilder = new StringBuilder();//stworzenie obiektu klasy StringBuilder
                for(int k = 0; k < userstable.get(j).length; k ++) {
                    stringBuilder.append(userstable.get(j)[k]);//dodanie informacji do bufora
                    stringBuilder.append(" ");
                }
                Main.saveLogs("                         "+stringBuilder);//komunikat do logow
            }

        } catch (FileNotFoundException er) {
            System.out.println(ConsoleFrame.getTimeandDate()+ "Nie można otworzyć pliku z użytkownikami serwera: "+er);//komunikat na konsole Intellij
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Nie można otworzyć pliku z uzytkownikami serwera; "+er);//komunikat do logow
        } catch (Exception e) {
            System.out.println(ConsoleFrame.getTimeandDate()+ "Nastąpił niespodziewany błąd podczas wczytywania użytkowników serwera: "+e);//komunikat na konsole Intellij
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Nastąpił niespodziewany błąd podczas wczytywania użytkowników serwera: "+e);//komunikat do logow
        }
    }
}