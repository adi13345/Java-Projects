package Polandball_pliki.GameObjects;

/**
 * Klasa rodzic dla elementow terenu - skrzynek, betonow
 */
public class Terrain extends StationaryObject {

    /**
     * Konstruktor klasy Terrain
     * @param x obecne polozenie obiektu na osi x
     * @param y obecne polozenie obiektu na osi y
     */
    public Terrain(int x,int y){
        super();
        x_=x;
        y_=y;
        buffImage_=null;
        name_class_object=null;
    }

    /**
     * Konstruktor bezparametrowy klasy Terrain
     */
    public Terrain(){
        super();
        x_=0;
        y_=0;
        buffImage_=null;
        name_class_object=null;
    }
}
