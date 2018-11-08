package Polandball_pliki.Counter;

import static Polandball_pliki.Panel.PanelBoard.PlayerExistence;
import static Polandball_pliki.Panel.PanelBoard.hussars_Power;
/**
 * Klasa odliczajaca czas dzialania skrzydel husarkisch
 */
public class CounterHussarWings extends Counter {
    /**
     * Konstruktor klasy CounterHussarWings
     */
    public CounterHussarWings() {
        super();
        born_time_=System.currentTimeMillis();
        isStillNeed_=true;
    }
    /**
     * Metoda wywolujaca metode checkTimeToEndOfHussarsWings
     */
    public void checkTime(){
        checkTimeToEndOfHussarsWings();
    }//sprawdzenia czasu
    /**
     * Metoda sprawdzajaca, czy nie minal juz czas trwania skrzydel husarskich
     */
    public void checkTimeToEndOfHussarsWings(){
        long current = System.currentTimeMillis();//obecny czas komputera
        if(current-born_time_>5000){//wykonuje sie jesli roznica wynosi wiecej niz 500
            hussars_Power=false;// ustawiam flage explozji ze nadszedl jej kres
            isStillNeed_=false;//ustawiam flage ze instancja tego licznika nie jest juz potrzebna
        }
        if(PlayerExistence==false){
            hussars_Power=false;
            isStillNeed_=false;
        }
    }
}
