package Polandball_pliki.Counter;

import Polandball_pliki.GameObjects.Polandball;
import static Polandball_pliki.Panel.PanelBoard.PlayerExistence;

/**
 * Klasa odpowiedzialna za odliczanie czasu do respawnu Player-a
 */
public class CounterPlayer extends Counter {

    /**
     * Instancja klasy Polandball
     */
    private Polandball player_;

    /**
     * Konstruktor klasy CounterPlayer, odliczajacy czas do odrodzenia gracza
     * @param player - obiekt typu Polandball
     */

    public CounterPlayer(Polandball player){
        super();//wywolanie konstruktora rodzica ( Counter )
        born_time_=System.currentTimeMillis();//ustawienie czasu od ktorego liczymy
        isStillNeed_=true;//ustawienie flagi przydatnosci licznika na true
        player_=player;//przypisanie licznika do konkretnego obiektu eksplozji
    }
    /**
     * Metoda wywolujaca metode checkTimeToRespawn
     */
    public void checkTime(){
        checkTimeToRespawn();
    }//sprawdzenia czasu1

    /**
     * Metoda sprawdzajaca, czy nastapil juz czas respawnu gracza po wczesniejszej smierci
     */
    public void checkTimeToRespawn(){
        long current = System.currentTimeMillis();//obecny czas komputera
        if(current-born_time_>3000){//wykonuje sie jesli roznica wynosi wiecej niz 500
            PlayerExistence=true;//zmieniam wartosc flagi w zmiennej statycznej PanelBoard, na true. Jest to sygnał zeby program zrespawnował gracza w miejscu startowm
            isStillNeed_=false;//ustawiam flage ze instancja tego licznika nie jest juz potrzebna
        }
    }
}
