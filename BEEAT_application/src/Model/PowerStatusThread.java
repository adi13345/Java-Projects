package Model;

import static Controller.MainController.setStatus;
import static Model.Main.*;
import static Model.Main.serialConnection;
import static Model.ParseConfigFile.commandDelay;

/**
 * Created by Adrian Szymowiat on 04.05.2018.
 * Klasa odpowiedzialna za sprawdzanie informacji o odbieranej mocy
 */
public class PowerStatusThread implements Runnable {
    /**
     * Flaga informujaca, cyz watek zakonczyl swoje dzialanie
     */
    public static boolean powerStatusRunFlag;
    /**
     * Cialo watku
     * Zbieranie informacji o odbieranej mocy
     */
    @Override
    public void run() {
        try {
            System.out.println("Start wątku PowerStatus");
            powerStatusRunFlag = true;// informacja, że wątek rozpoczął swoje działanie
            while (!ticket) {
                Thread.sleep(correctnessGuard.randomDelay());//losowy czas oczekwiania na kolejne sprawdzenie, czy mozna już wysyłać
            }
            ticket = false;// zarezerwowanie prawa do wyslania komendy
            serialConnection.sendMessage("AT+CSQ");//wysłanie komendy sprawdzającej odbierana moc
            Thread.sleep(commandDelay);//uspanie watku na x milisekund
            setStatus(correctnessGuard.getPowerStatus(serialConnection.getAnswerBufor()),2);//wyświetlenie napisu w aplikacji
            showAnswer(serialConnection.getAnswerBufor());//wyswietlenie otrzymanych wiadomosci
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            powerStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku PowerStatus");
        }catch(InterruptedException e){
            System.out.println("Błąd wątku PowerStatus: "+e);
        }catch(IndexOutOfBoundsException e){
            System.out.println("Błąd w PowerStatus: "+e);
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            powerStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku PowerStatus");
        }catch (NumberFormatException e){
            System.out.println("Błąd w PowerStatus: "+e);
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            powerStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku PowerStatus");
        }
    }
}
