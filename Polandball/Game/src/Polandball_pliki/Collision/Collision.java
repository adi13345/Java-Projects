package Polandball_pliki.Collision;

/**
 * Klasa "Protoplasta" wszystkich kolizji
 */
public class Collision {

    /**
     * Zmienna okreslajaca, czy nie nastapila kolizja
     */
    boolean isNotCollision;

    /**
     *  Konstruktor Klasy Collision
     */
    public Collision(){
        isNotCollision=true;
    }

    /**
     * Metoda zwracajaca informacje o "niezajsciu" kolizji
     * @return isNotCollision
     */
    public boolean getIsNotCollision(){return isNotCollision;}
}
