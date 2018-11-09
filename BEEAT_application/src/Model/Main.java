package Model;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Locale;
import static Model.ParseConfigFile.locale;

/**
 * Klasa glowna programu
 * @author Adrian Szymowiat
 */
public class Main extends Application {
    /**
     * Layout (stage) okna wyboru polaczenia
     */
    public static Stage primaryStage;
    /**
     * Layout (stage) okna glownego aplikacji
     */
    public static Stage mainStage;
    /**
     * Instancja klasy polaczenia serialowego, umozliwiajacy korzystanie z polaczenia
     */
    public static SerialConnection serialConnection;
    /**
     * Instancja klasy do sprawdzania warunkow i wymagan
     */
    public static CorrectnessGuard correctnessGuard;
    /**
     * Instacja wątku kontrolnego aplikacji
     */
    public static Thread controlThread;
    /**
     * Instancja klasy klasy parsujacej dane konfiguracyjne
     */
    private static ParseConfigFile parseConfigFile;
    /**
     * Instacja klasy StingProperty sluzaca do zbindowania zmieniajcego sie napisu stanu baterii z labelem wyswietlajacym ten napis
     * Kazda zmiana statusu baterri (odbywajecego sie w oddzielnym watku) powoduje zmiane obiektu batterySatatus za pozrednictem metody w kontrolorze
     * Zaznaczenie/odznaczenie odpowiedniego checkBox decyduje, czy batteryStatus jest polaczony/rozlaczony z labelem wyswietlajacym napis, co
     * w efekcie decyduje o wyswietlanym napisie w oknie aplikacji
     */
    public static StringProperty batteryStatus;
    /**
     * Instancja klasy StringProperty, analogiczna funckja do batteryStatus, tyle ze sluzy do wyswietlania mocy
     */
    public static StringProperty powerStatus;
    /**
     * Instancja klasy StringProperty, analogiczna funckja do batteryStatus i powerStatus, tyle ze sluzy do wyswietlania statusu rejestracji
     */
    public static StringProperty registrationStatus;
    /**
     * Bufor zawierajacy wszystkie uruchomione watki wysylania jednorazej wiadomosci
     */
    public static ArrayList<SimpleSendThread> simpleSendThreadArrayList;
    /**
     * Bufor zawierajacy wszystkie uruchomione watki wysylania autmotaycznej wiadomosci
     */
    public static ArrayList<AutomaticSendThread> automaticSendThreadArraylist;
    /**
     * Flaga informujaca, czy polaczenie serialowe zostalo nawiazane
     */
    public static boolean isConnectionReady;
    /**
     * Flaga informujaca, czy mozliwe jest wyslanie komendy do modulu
     * Kazdy watek musi czekac az inny watek zwolni ta flage (ustawi na true)
     */
    public static boolean ticket;
    /**
     * Metoda odpiewiadzialna za wyswietlenie okna po uruchomieniu programu
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage){
        try {
            ticket = true;//ustawienie flagi na true, mozliwe wyslanie komend do modulu
            parseConfigFile = new ParseConfigFile();//stworzenie obiekty klasy parsujacej
            parseConfigFile.getConfigurationData();//wczytanie danych konfiguracyjnych
            serialConnection = new SerialConnection();//utworzenie obiektu polaczenia serialowego
            correctnessGuard = new CorrectnessGuard();//utworzenie obiektu staznika
            batteryStatus = new SimpleStringProperty("--");//stworzenie obiektu zawierajacego napis ze stanem baterii
            powerStatus = new SimpleStringProperty("--");//stworzenie obiektu zawierajacego napis z poziomem odbieranej mocy
            registrationStatus = new SimpleStringProperty("--");//stworzenie obiektu zawierajacego napis ze statusem rejestracji
            simpleSendThreadArrayList = new ArrayList<>();//stworzenie bufora zawierajacego wszystkie waki jednorazowej wiadomosci
            automaticSendThreadArraylist = new ArrayList<>();//storzenei bufora zawierajacego wszystkie wątki automatycznej wiadomości
            Locale.setDefault(locale);//ustawienie domyślnego języka
            Parent root = FXMLLoader.load(getClass().getResource("../Resources/connect.fxml"));//załadowanie parametrów okna z pliku fxml
            stage.setTitle("Connection");//przypisanie nazwy okna
            stage.setScene(new Scene(root));//ustawienie załadowanego okna
            stage.getIcons().add(new Image("file:"+getClass().getResource("/Resources/connection.png").getPath()));//ustawienie icony okna
            stage.show();//wyświetlenie okna
            stage.setResizable(false);//wylaczenie mozliwosci zmian rozmiarow
            stage.setOnCloseRequest((WindowEvent event_1) -> {//obsluga zdarzenia zamkniecia okna polaczeniowego
                serialConnection.closeSerialConnection();//zwolnienie zasobow poalczenia serialowego, jesli przez przypadek zostaly jakies zajete
                System.gc();//poinformowanie maszyny wirtualnej o usunieciu referencji na nieuzywane obiekty
                Platform.exit();//zamkniecie aplikacji
            });
            primaryStage = stage;//przypisanie stworzonego stage do zmiennej
        }catch (Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Nastąpił błąd podczas uruchamiania aplikacji:\n"+e+"\n");//okno informujace o bledzie podczas uruchomienia aplikacji
            System.gc();//poinformowanie maszyny wirtualnej o usunieciu referencji na nieuzywane obiekty
            System.exit(1);//wyjście z programu z kodem błędu
        }
    }
    /**
     * Metoda glowna programu
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Metoda wyswietlajaca otrzymane wiadomosci
     * @param arrayList - bufor wiadomosci
     */
    public static void showAnswer(ArrayList<String> arrayList){
        for (int i =0; i < arrayList.size(); i++){
            System.out.println("Odpowiedź modułu: "+arrayList.get(i));//wyswietlenie po kolei eleemntów bufora
        }
    }
}
