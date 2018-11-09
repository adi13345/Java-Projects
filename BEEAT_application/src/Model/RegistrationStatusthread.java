package Model;

import static Controller.MainController.setStatus;
import static Model.Main.*;
import static Model.ParseConfigFile.commandDelay;

/**
 * Created by Adrian Szymowiat on 05.05.2018.
 * Klasa odpowiedzialna za sprawdzanie informacji o statusie rejestracji
 */
public class RegistrationStatusthread implements Runnable {
    /**
     * Flaga informujaca, czy watek zakonczyl swoje dzialanie
     */
    public static boolean registrationStatusRunFlag;
    /**
     * Cialo watku
     * Zbieranie informacji na temat statusu rejestracji
     */
    @Override
    public void run() {
        try {
            System.out.println("Start wątku RegistrationStatus");
            registrationStatusRunFlag = true;// informacja, że wątek rozpoczął swoje działanie
            while(!ticket){
                Thread.sleep(correctnessGuard.randomDelay());//losowy czas oczekwiania na kolejne sprawdzenie, czy mozna już wysyłać
            }
            ticket = false;// zarezerwowanie prawa do wyslania komendy
            serialConnection.sendMessage("AT+CREG?");//wysłanie komendy sprawdzającej status baterii
            Thread.sleep(commandDelay);//uspanie watku na x milisekund
            setStatus(correctnessGuard.getRegistrationStatus(serialConnection.getAnswerBufor()),3);//wyświetlenie napisu w aplikacji
            showAnswer(serialConnection.getAnswerBufor());//wyswietlenie otrzymanych wiadomosci
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            registrationStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku RegistrationStatus");
        }catch (InterruptedException e){
            System.out.println("Błąd wątku RegistrationStatus: "+e);
        }catch(IndexOutOfBoundsException e){
            System.out.println("Błąd w RegistrationStatus: "+e);
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            registrationStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku RegistrationStatus");
        }catch (NumberFormatException e){
            System.out.println("Błąd w RegistrationStatus: "+e);
            serialConnection.clearAnswerBufor();//wyczyszczenie bufora
            ticket = true;//oddanie możliwości wysylania komend dla innych wątków
            registrationStatusRunFlag = false;// informacja, że wątek zakończył swoje działanie
            System.out.println("Koniec wątku RegistrationStatus");
        }
    }
}
