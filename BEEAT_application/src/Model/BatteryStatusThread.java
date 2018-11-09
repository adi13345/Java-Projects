package Model;

import static Controller.MainController.setStatus;
import static Model.Main.*;
import static Model.Main.serialConnection;
import static Model.ParseConfigFile.commandDelay;

/**
 * Created by Adrian Szymowiat on 03.05.2018.
 * Klasa odpowiedzialna za sprawdzanie informacji o baterii
 */
public class BatteryStatusThread implements Runnable {
    /**
     * Flaga informujaca, czy watek zakonczyl swoje dzialanie
     */
    public static boolean batteryStatusRunFlag;
    /**
     * Cialo watku
     * Zbieranie informacji na temat baterii urzadzenia
     */
    @Override
    public void run() {
        try {
            System.out.println("Start wątku BatteryStatus");
            batteryStatusRunFlag = true;// informacja, że wątek rozpoczął swoje działanie
            while(!ticket){
                Thread.sleep(correctnessGuard.randomDelay());//losowy czas oczekwiania na kolejne sprawdzenie, czy mozna już wysyłać
            }
            ticket = false;// zarezerwowanie prawa do wyslania komendy
            serialConnection.sendMessage("AT+CBC");//wysłanie komendy sprawdzającej status baterii
            Thread.sleep(commandDelay);//uspanie watku na x milisekund
            setStatus(correctnessGuard.getBatteryStatus(serialConnection.getAnswerBufor()),1);//wyświetlenie napisu w aplikacji
            showAnswer(serialConnection.getAnswerBufor());//wyswietlenie otrzymanych wiadomosci
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            batteryStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku BatteryStatus");
        }catch (InterruptedException e){
            System.out.println("Błąd wątku BatteryStatus: "+e);
        }catch(IndexOutOfBoundsException e){
            System.out.println("Błąd w BatteryStatus: "+e);
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            batteryStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku BatteryStatus");
        }catch (NumberFormatException e){
            System.out.println("Błąd w BatteryStatus: "+e);
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            batteryStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku BatteryStatus");
        }
    }
    //System.out.println("Status:->"+correctnessGuard.getBatteryStatus(serialConnection.getAnswerBufor())+"<-");
    //System.out.println("Romziar bufora -> "+serialConnection.getAnswerBufor().size());
}
