package Polandball_pliki.Counter;

import static Polandball_pliki.Panel.PanelBoard.LaserExistence;

/**
 * Klasa odliczajaca czas wyswietlania lasera
 */

public class CounterLaser extends Counter {
    /**
     * Konstruktor klasy CounterLaser
     */
    public CounterLaser() {
        super();
        born_time_=System.currentTimeMillis();
        isStillNeed_=true;
    }
    /**
     * Meotda wywolujaca metode checkTimeToEndOfShowingLaser
     */
    public void checkTime(){
        checkTimeToEndOfShowingLaser();
    }//sprawdzenia czasu
    /**
     * Metoda sprawdzajaca, czy nie minal juz czas trwania wyswietlania lasera
     */
    public void checkTimeToEndOfShowingLaser(){
        long current = System.currentTimeMillis();//obecny czas komputera
        if(current-born_time_>1000){//wykonuje sie jesli roznica wynosi wiecej niz 500
            LaserExistence=false;// ustawiam flage lasera ze nadszedl jej kres
            isStillNeed_=false;//ustawiam flage ze instancja tego licznika nie jest juz potrzebna
        }
        if(LaserExistence==false){
            isStillNeed_=false;
        }
    }
}
