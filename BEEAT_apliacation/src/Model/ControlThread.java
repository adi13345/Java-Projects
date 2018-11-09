package Model;

import static Model.ParseConfigFile.controlThreadDelay;
import static Model.BatteryStatusThread.batteryStatusRunFlag;
import static Model.PowerStatusThread.powerStatusRunFlag;
import static Model.RegistrationStatusthread.registrationStatusRunFlag;

/**
 * Created by Adrian Szymowiat on 03.05.2018.
 * Klasa odpowiedzialna za sterowanie/uruchamianie pozostalych watkow aplikacji
 */
public class ControlThread implements Runnable {
    /**
     * Flaga sterujaca dzialaniem watku (wlaczanie/wylaczanie watku)
     */
    public static boolean controlThreadRunFlag;
    /**
     * Cialo watku
     * Metoda uruchamiajaca pozostale watki
     */
    @Override
    public void run() {
        while(controlThreadRunFlag){
            try{
                if(!batteryStatusRunFlag){
                    Thread batteryThread = new Thread(new BatteryStatusThread());//utworzenie watku sprawdzania statusu baterii
                    batteryThread.start();//uruchumienie watku
                }
                if(!powerStatusRunFlag){
                    Thread powerThread = new Thread(new PowerStatusThread());//utworzenie watku sprawdzania poziomu mocy
                    powerThread.start();//uruchomienie watku
                }
                if(!registrationStatusRunFlag){
                    Thread registrationThread = new Thread(new RegistrationStatusthread());//utworzenie watku sprawdzania statusu rejestracji
                    registrationThread.start();//uruchomienie watku
                }
                Thread.sleep(controlThreadDelay);//odczekanie określonego czasu, po czym następuje ponowna próba wyciągnięcia informacj z modulu
            }catch(InterruptedException e){
                System.out.println("Błąd wątku Control: "+e);
            }

        }
    }
}
