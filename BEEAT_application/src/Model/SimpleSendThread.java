package Model;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.util.ArrayList;

import static Model.Main.*;
import static Model.Main.ticket;
import static Model.ParseConfigFile.commandDelay;

/**
 * Created by Adrian Szymowiat on 16.05.2018.
 * Klasa odpowiedzialna za wysyłanie jednorazowej wiadomoci
 */
public class SimpleSendThread extends Thread {
    /**
     * Bufor zawierajacy wszystkie numery, do ktorych trzeba wyslac dana wiadomosc
     */
    public String[] buforOfNumbers;
    /**
     * Wysylana wiadomosc
     */
    public String message;
    /**
     * Bufor zawierajacy informacje, czy kolejna wiadomosc zostala wyslana poprawnie
     */
    public ArrayList<String> listOfCorrectSent;
    /**
     * Flada informujaca, czy dany watek skonczyl swoje dzialanie
     */
    public boolean simpleSendRunflag;
    /**
     * Flaga informujaca, ze watek zostal stworzony w ramach watku automatycznego wysylanai wiadomosci
     */
    public boolean isThisThreadAuto;
    /**
     * Zmienna przechowujaca date wyslania, uzywana tylko w przypadku automatycznej wiadomosci
     */
    public String date;
    /**
     * Cialo watku
     * Obsluga wysylania jednorazej wiadomosci
     */
    @Override
    public void run(){
        try {
            System.out.println("Start wątku SimpleSendThread");
            simpleSendRunflag = true;//informacja, ze wątek rozpoczyna działanie
            createInofArrayList();//stworzenei bufora na informacje o poprawnosci wyslania wiadomosci
            while (!ticket) {
                Thread.sleep(correctnessGuard.randomDelay());//losowy czas oczekwiania na kolejne sprawdzenie, czy mozna już wysyłać
            }
            ticket = false;// zarezerwowanie prawa do wyslania komendy
            String sendStatus = null;//flaga okreslajaca, czy wiadomosc zostala wyslana poprawnie, czy nie
            serialConnection.sendMessage("AT+CMGF=1");//ustawienie trybu tekstowego wiadomosci
            Thread.sleep(commandDelay);//uspanie watku na x milisekund
            showAnswer(serialConnection.getAnswerBufor());//wyswietlenie otrzymanych wiadomosci
            serialConnection.getAnswerBufor().clear();//wyczyszczenie bufora wiadomosci
            for(int i = 0; i < buforOfNumbers.length;i++) {
                serialConnection.sendMessage("AT+CMGS=\"" + buforOfNumbers[i] + "\"");//wybieranie kolejncyh nuemrow
                Thread.sleep(commandDelay);//uspanie watku na x milisekund
                showAnswer(serialConnection.getAnswerBufor());//wyswietlenie otrzymanych wiadomosci
                if (!correctnessGuard.checkAnswer(serialConnection.getAnswerBufor(), "ERROR")) {//sprawdzenie moduł tu nie odpowiedział błędem
                    serialConnection.getAnswerBufor().clear();//wyczyszczenie bufora wiadomosci
                    serialConnection.sendMessage(message);//wyslanie wiadomosci
                    while (!correctnessGuard.checkAnswer(serialConnection.getAnswerBufor(), "OK") && //sprawdzenia czy modul juz odpowiedzial po wyslaniu sms
                            !correctnessGuard.checkAnswer(serialConnection.getAnswerBufor(), "ERROR")) {//sprawdzanie czy jest OK albo ERROR
                        Thread.sleep(correctnessGuard.randomDelay());//uspanie watku na x milisekund
                    }
                    if (correctnessGuard.checkAnswer(serialConnection.getAnswerBufor(), "OK")) {
                        sendStatus = "Wiadomość wysłana.";//informacja, ze wiadomosc zostala wyslana poprawnie
                    } else if (correctnessGuard.checkAnswer(serialConnection.getAnswerBufor(), "ERROR")) {
                        sendStatus = "Nie udało się wysłać wiadomości.";//informacja, ze wystapil blad podczas wysylania wiadomosci
                    }
                    showAnswer(serialConnection.getAnswerBufor());//wyswietlenie otrzymanych wiadomosci
                    serialConnection.getAnswerBufor().clear();//wyczyszczenie bufora wiadomosci
                    listOfCorrectSent.add( sendStatus);//wstawienie do bufora informacji o poprawnosci/niepoprawnosci wyslania wiadomosci dla danego numeru
                }else{
                    listOfCorrectSent.add( "Nie udało się wysłać wiadomości.");//wstawienie do bufora informacji o niepoprawności wyslania wiadomosci dla danego numeru
                }
            }
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            simpleSendRunflag = false;//informacja, że wątek skonczyl dzialanie
            String statement = correctnessGuard.createStatement(listOfCorrectSent,buforOfNumbers,message);//stworzenei odpowiedniego komunikatu
            Platform.runLater(() -> {//wywołanie w watku javafx apliakcji
                String info;//headertex komunikatu
                if(isThisThreadAuto){
                    info = "Wysłanie automatycznej wiadomości dnia: " + date + " zostało zakończone!";
                }else{
                    info = "Wysyłanie jednorazej wiadomośći zostało zakończone! Data wysłania: "+date;
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);//stworzenie alarmu
                alert.initModality(Modality.NONE);//okno nieblokujace
                alert.setTitle("Message send!");//tytuł okna
                alert.setHeaderText(info);
                alert.setContentText(statement);//wyświetlenie raportu
                alert.show();//wyświetlenie alarmu, nie trzeba czekąć
            });
            listOfCorrectSent.clear();//wyczyssczenei bufora
            System.out.println("Koniec wątku SimpleSendThread");
        }catch(InterruptedException e){
            listOfCorrectSent.clear();//wyczyssczenei bufora
            serialConnection.getAnswerBufor().clear();//wyczyszczenie bufora wiadomosci
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            simpleSendRunflag = false;//informacja, że wątek skonczyl dzialanie
            System.out.println("Błąd wątku SimpleSendThread: "+e);
        }catch(Exception er){
            listOfCorrectSent.clear();//wyczyssczenei bufora
            serialConnection.getAnswerBufor().clear();//wyczyszczenie bufora wiadomosci
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            simpleSendRunflag = false;//informacja, że wątek skonczyl dzialanie
            System.out.println("Błąd wątku SimpleSendThread: "+er);
        }
    }
    /**
     * Metoda tworząca bufor zwierajacy informacje o poprawnosci kolejno wyslanych wiadomosci
     */
    private void createInofArrayList(){
        listOfCorrectSent = new ArrayList<>();
    }

    //;502614584;502614584
    //505140549;502614584;505140549;502614584
    //at test - komunikaty+polskei znaki -> 4 razy śźćżąęłń
}
