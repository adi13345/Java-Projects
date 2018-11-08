package Polandball_pliki.GameObjects;

import java.awt.*;
/**
 * Klasa rodzic dla wszystkich rodzajow bomb
 */
public class Bomb extends StationaryObject {
    /**
     * Flaga informujaca, czy bomba ma wybuchnac
     */
    boolean explosionflag_=false;
    /**
     * Metoda zwracajaca obecny stan eksplozji
     * @return explosionflag_
     */
    public boolean getExplosionflag(){
        return explosionflag_;
    }
    /**
     * Metoda ustawiajaca stan eksplozji
     * @param explosionflag
     */
    public void setExplosionflag(boolean explosionflag){
        explosionflag_=explosionflag;
    }
    /**
     * Zmienna, przechowujaca gif eksplozji
     */
    public Image image_;

    /**
     * Konstruktor bezparametrowy klasy Bomb
     */
    public Bomb() {
        super();
        x_ = 0;
        y_ = 0;
        name_class_object=null;
        buffImage_ = null;
        image_ = null;
        explosionflag_=false;
    }
    /**
     * Meotda zwracajaca gif eksplozji
     * @return image_
     */
    public Image getGIF() {
        return image_;
    }
}