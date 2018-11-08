package Polandball_pliki.GameObjects;

/**
 * Klasa rodzic dla wszystkich przedmiotow do zebrania/interakcji
 */
public class Item extends StationaryObject {
    /**
     * Konstruktor klasy Item
     * @param x polozenie na osi x
     * @param y polozenie na osi y
     */
    public Item(int x,int y){
        super();
        x_=x;
        y_=y;
        buffImage_=null;
        name_class_object=null;
    }
    /**
     * Konstruktor bezparametrowy klasy Item
     */
    public Item(){
        super();
        x_=0;
        y_=0;
        buffImage_=null;
        name_class_object=null;
    }
}
