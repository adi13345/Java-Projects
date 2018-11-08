package Polandball_pliki.GameObjects;

/**
 * Klasa rodzic dla wszystkich obiektów nieruchomych/nieozywionych
 */
public class StationaryObject extends GameObject {
    /**
     * Konstruktor bezparametrowy klasy StationaryObject
     */
    public StationaryObject(){
        super();
        x_=0;
        y_=0;
        buffImage_=null;
        name_class_object=null;
    }

}
