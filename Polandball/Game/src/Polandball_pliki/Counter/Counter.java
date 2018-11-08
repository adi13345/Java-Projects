package Polandball_pliki.Counter;

/**
 * Klasa Protoplasta - liczaca czas zycia obiektow
 */
public class Counter {
    /**
     * Czas systemowy, od ktorego licznik zaczyna odliczac czas
     */
    long born_time_;
    /**
     * Zmienna okreslajaca, czy licznik nadal jest potrzebny
     */
    boolean isStillNeed_;
    /**
     * Konstruktor klasy licznika
     */
    public Counter(){
        born_time_=System.currentTimeMillis();
        isStillNeed_=true;
    }
    /**
     * Metoda dziedziczona przez klas pochodne, sprawdzajaca, czy czas nie uplynal
     */
    public void checkTime(){
    }
    /**
     * Metoda zwracajaca informacje, czy licznik jest nadal potrzebny
     * @return isStillNeed_
     */
    public boolean getisStillNeed(){return isStillNeed_;}
}
