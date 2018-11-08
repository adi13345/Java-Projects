package Polandball_pliki.Counter;

import Polandball_pliki.GameObjects.Normal_Bomb;

/**
 * Klasa odliczajaca czas do wybuchu bomby zwyklej
 */
public class CounterNormalBomb extends Counter {
    /**
     * Instacja klasy Normal_Bomb
     */
    private Normal_Bomb normal_bomb_;
    /**
     * Konstruktor ustawiajacy czas i poczatkowe wartosci licznika
     * @param normal_bomb  - obiekt typu Normal_Bomb
     */
    public CounterNormalBomb(Normal_Bomb normal_bomb){
        super();
        born_time_=System.currentTimeMillis();// ustawiam czas kiedy powstal counter
        isStillNeed_=true; // flaga mowiaca czy counter nadal cos sensownego liczy
        normal_bomb_=normal_bomb; //przypisanie do obiektu bomba
    }

    /**
     * Metoda wywolujaca funkcje checkTimeToExplosionBomb sprawdzajaca czas eksplozji bomby
     */
    public void checkTime(){
        checkTimeToExplosionBomb();
    }//sprawdzam czas

    /**
     * Metoda odliczajaca czas do wybuchu bomby
     */
    public void checkTimeToExplosionBomb(){
        long current = System.currentTimeMillis();//pobieram obecny czas
        if(current-born_time_>3000){//sprawdzam czy minely 3 sekundy od podlozenia bomby, trzeba bedzie ustawic w configu
            normal_bomb_.setExplosionflag(true);//ustawienie flagi ze bomba wybucha
            isStillNeed_=false;// zaznaczam obiektowi ze nie jest juz mi do niczego potrzebny
        }
    }
}
