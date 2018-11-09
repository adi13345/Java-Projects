import java.io.FileWriter;
import java.io.IOException;
/**
 * Klasa glowna programu
 */
public class Main {
    /**
     * Obiekt klasy ConsoleFrame
     * Tworzenie okna konsoli serwera
     */
    public static ConsoleFrame consoleFrame;
    /**
     * Obiekt do nadpisywania logow serwera
     */
    public static FileWriter fileWriter;
    /**
     * Funkcja glowna programu
     * @param args - parametr funkcji glownej programu
     */
    public static void main(String [] args){
        saveLogs(ConsoleFrame.getTimeandDate() + "------------------------------------>START APLIKACJI<-------------------------------------");//komunikat do logow
        new ServerParameters().ParseServerParameters();//parsowanie danych konfiguracyjnych serwera
        new Users().ParseUsers();//parsowanie informacji o uzytkownikach
        consoleFrame = new ConsoleFrame();//utworzenie obiektu klasy Consoleframe - okno konsolowe serwera
        consoleFrame.setVisible(true);//wlaczenie widocznosci ramki
        new Server().StartServer();//start serwera
    }
    /**
     * Metoda sluzaca do zapisywania logow w pliku
     * @param log - tekst zapisywany do logow
     */
    public static void saveLogs(String log){
        try {
            fileWriter = new FileWriter("server_logs.txt", true);//otworzenie pliku z logami
            fileWriter.write(log+"\r\n");//dodanie informacji+znak nowej lini
            fileWriter.close();//zamkniecie i zapisanie
        }catch (IOException e){
            System.out.println(ConsoleFrame.getTimeandDate()+ "Prawdopodbnie nie udało się otworzyć pliku server_logs.txt: " + e);
            if(ConsoleFrame.textarea_ != null) {//sprawdzenie czy nie zamknieto okna konsoli
                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate()+ "Prawdopodbnie nie udało się otworzyć pliku server_logs.txt: " + e+"\n");//komunikat o uruchomieniu serwera
            }
        }
    }
}
