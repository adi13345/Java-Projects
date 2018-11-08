package Polandball_pliki.GameObjects;

import java.awt.image.BufferedImage;

import static Polandball_pliki.Panel.PanelBoard.SizeHeightIcon;
import static Polandball_pliki.Panel.PanelBoard.SizeWidthIcon;

/**
 * Klasa rodzic dla wszystkich obiektow na planszy
 */
public class GameObject  {
    /**
     * Zmienna okreslajaca nazwe typu obiektu
     */
    public String name_class_object;
    /**
     * Pozycja x-owa obiektu
     */
    int x_;
    /**
     * Pozycja y-oka obiektu
     */
    int y_;
    /**
     * Zmienna przechowujaca grafike obiektu
     */
    public BufferedImage buffImage_;

    /**
     * Konstruktor bezparametrowy klasy GameObject
     */
    public GameObject(){
        x_=0;
        y_=0;
        buffImage_=null;
    }

    /**
     * Metoda zwracajaca polozenie na osi x obiektu
     * @return x_
     */
    public int getX(){
        return x_;
    }
    /**
     * Metoda zwracajaca polozenie na osi y obiektu
     * @return y_
     */
    public int getY(){
        return y_;
    }
    /**
     * Metoda zwracająca grafike przechowywaną przez obiekt
     * @return buffImage_
     */
    public BufferedImage getBuffImage(){
        return buffImage_;
    }
    /**
     * Metoda zmieniajca pozycje obiektu na osi x
     * @param x - nowa pozycja obiektu na osi x
     */
    public void changeX(int x){
        x_=x;
    }

    /**
     * Metoda zmieniajaca pozycje na osi y
     * @param y nowa pozycja obiektu na osi y
     */
    public void changeY(int y){
        y_=y;
    }

    /**
     * Metoda zwrcajaca scieszke do grafiki jakiej uzyto w klasie
     */
    public String getNameClassObject(){return name_class_object;}

    /**
     * Metoda zwracajaca kolumne, w jakiej znajduje sie obiekt
     */
    public int getColumnX(){
        return this.getX()/SizeWidthIcon;
    }

    /**
     * Metoda zwracajaca kwiersz, w jakim znajduje sie obiekt
     */
    public int getRowY(){
        return this.getY()/SizeHeightIcon;
    }
}
