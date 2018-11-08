package Polandball_pliki.Counter;

import Polandball_pliki.GameObjects.Explosion;

/**
 * Klasa odliczajaca czas trwania eskplozji
 */
public class CounterExplosion extends Counter  {

    /**
     * Obiekt typu Explosion, potrzebny do przypisania licznika do konkretnej eksplozji
     */
    private Explosion explosion_;
    /**
     * Konstruktor ustawiajacy czas i poczatkowe wartosci licznika dla danej eksplozji
     * @param explosion - obiekt typu Explosion
     */
    public CounterExplosion(Explosion explosion){
        super();//wywolanie konstruktora rodzica ( Counter )
        born_time_=System.currentTimeMillis();//ustawienie czasu od ktorego liczymy
        isStillNeed_=true;//ustawienie flagi przydatnosci licznika na true
        explosion_=explosion;//przypisanie licznika do konkretnego obiektu eksplozji
    }

    /**
     * Metoda wywolujaca metode checkTimeToEndOfExplosion
     */
    public void checkTime(){
        checkTimeToEndOfExplosion();
    }//sprawdzenia czasu
    /**
     * Metoda sprawdzajaca, czy nie minal juz czas trwania wybuchu
     */
    public void checkTimeToEndOfExplosion(){
        long current = System.currentTimeMillis();//obecny czas komputera
        if(current-born_time_>500){//wykonuje sie jesli roznica wynosi wiecej niz 500
            explosion_.set_end_of_explosion(true);// ustawiam flage explozji ze nadszedl jej kres
            isStillNeed_=false;//ustawiam flage ze instancja tego licznika nie jest juz potrzebna
        }
    }
}
