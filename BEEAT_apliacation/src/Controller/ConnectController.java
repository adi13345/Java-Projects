package Controller;

import Model.AutomaticSendThread;
import Model.SerialConnection;
import Model.SimpleSendThread;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Model.Main.*;
import static Model.ParseConfigFile.commandDelay;
import static Model.ControlThread.controlThreadRunFlag;

/**
 * Kontroler okna polaczeniowego
 */
public class ConnectController implements Initializable{
    /**
     * Przycisk do sprawdzenia dostepnych portow com
     */
    @FXML
    private Button checkComPortsButton;
    /**
     * Przycisk inicjalizujacy polaczenie
     */
    @FXML
    private Button connectButton;
    /**
     * Pole tekstowe do wpisania numeru portu COM
     */
    @FXML
    private TextField comPortTextField;
    /**
     * Metoda obslugujaca zdarzenie wcisniecia przycisku checkComPortsButton
     * @param event - zdarzenie
     */
    @FXML
    public void checkComPorts(ActionEvent event) {
        SerialConnection.availablePorts();
    }

    /**
     * Metoda obslugujaca zdarzenie wcisniecia przycisku connectButton
     */
    @FXML
    void connectSerialPort() {
        try {
            String comPort;//numer portu com
            comPort = comPortTextField.getText();//pobranie wpisanej nazwy port z poal tekstowego
            if(correctnessGuard.isComportCorrect(comPort)) {//sprawdzenie poprawnosci
                serialConnection.openSerialConnection(comPort);//proba nawiazania polaczenia
                if(isConnectionReady) {//sprawdzenie czy polaczenie zostalo pomyslnie utworzone
                    serialConnection.sendMessage("AT");//wysłanie komendy sprawdzającej poprawnośc nawiązanego połączenia z modułem
                    Thread.sleep(commandDelay);//uspanie aplikacji na x milisekund
                    showAnswer(serialConnection.getAnswerBufor());//wyswietlenie otrzymanych wiadomosci
                    if(correctnessGuard.checkAnswer(serialConnection.getAnswerBufor(), "\r\nOK\r\n")) {//sprawdzenie, czy poprawnie nawiazano polaczenie
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Resources/aplication.fxml"));//zaladowanie parametrow okna z pliku fxml
                        //Parent root = fxmlLoader.load();//zaladowanie danych do roota
                        Parent root = FXMLLoader.load(getClass().getResource("/Resources/aplication.fxml"));
                        root.setId("myroot");//ustwaienie id w celu identyfikacji w roota w pliku css
                        Stage stage = new Stage();//nowy stejdż
                        stage.setTitle("BEE  - Simple aplication for sending phone messages");//przypisanie nazwy okna
                        Scene scene = new Scene(root);//stworzenie sceny
                        stage.setScene(scene);//ustawienie zaladowanego okna
                        stage.setResizable(false);//wylaczenie mozliwosci zmian rozmiarow
                        stage.getIcons().add(new Image("file:"+getClass().getResource("/Resources/connection.png").getPath()));//ustawienie icony okna
                        stage.show();//wyswietlenie okna
                        stage.setOnCloseRequest((WindowEvent event_2) ->{//ustawienie zdarzenia na zamkniecie okna
                            closeMainStage();//wywoloanie nizej zdefiniowanej metody
                        });
                        primaryStage.close();//zamkniecie poprzedniego stejdżu - okna nawiązywania połaczenia
                        serialConnection.clearAnswerBufor();//wyczyszczenie bufora wiadomosci
                        mainStage = stage;//przypisanie stowrzonego stage do stalej
                    }else{
                        JOptionPane.showMessageDialog(new JFrame(), "Komenda AT zgłosiła błąd - niepoprawna odpowiedź modułu!", "Controller Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }catch(IOException er){
            System.out.println(er);
            JOptionPane.showMessageDialog(new JFrame(), "Błąd wejścia/wyjścia w kontrolerze okna połączeniowego!", "Controller Error",
                    JOptionPane.ERROR_MESSAGE);
        }catch(Exception e){
            JOptionPane.showMessageDialog(new JFrame(), "Niespodziewany błąd w kontrolerze okna połączeniowego!", "Controller Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println(e);
        }
    }
    /**
     * Metoda zamykajaca okno glowne programu
     */
    private void closeMainStage(){
        serialConnection.closeSerialConnection();//zamkniecie polaczenai serialowego i wyczyszczenie zasobow
        controlThreadRunFlag = false;//zastopowanie wątku kontrolnego
        closeAllAutomaticSendThreads(automaticSendThreadArraylist);//zamkniecie wszystkich watkow automatycznej wiadomości
        System.gc();//poinformowanie maszyny wirtualnej o usunieciu referencji na nieuzywane obiekty
        Platform.exit();//zamkniecie apliakcji - DO ZMIANY !!!!!!!!!!!!!!!!
    }
    /**
     * Metoda obslugujaca wcisnienie przycisku
     * @param event - wcisniety przycisk
     */
    @FXML
    void enterPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){//sprawdzenie czy wcisinieto enter
            connectSerialPort();//wywolanie wyzej zdefiniowanej metody - nawiaza polaczenie + otworzenie okna glownego aplikacji
        }
    }
    /**
     * Metoda wykonujaca odpowiednei czynnosci startowe
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Metoda wylaczajaca wszystkie watki automatycznej wiadomosci
     * @param arrayList
     */
    private void closeAllAutomaticSendThreads(ArrayList<AutomaticSendThread> arrayList){
        for(int i = 0; i < arrayList.size();i++){
            arrayList.get(i).checkTimeRunFlag = false; //wyłaczenei działanai wątku
        }
    }
}
