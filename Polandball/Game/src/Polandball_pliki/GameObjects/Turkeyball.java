package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.TurkeyBallString;

/**
 * Klasa reprezentujaca wroga typu Turkeyball
 */
public class Turkeyball extends Enemy {
    /**
     * Konstruktor klasy Turkeyball
     * @param x obecne polozenie obiektu na osi x
     * @param y obecne polozenie obiektu na osi y
     */
    public Turkeyball(int x,int y){
        super();
        x_=x;
        y_=y;
        velX_=0;
        velY_=0;
        name_class_object=TurkeyBallString;
        buffImage_=createBufferedImage();
        distance_from_elevation_walls=6/80;
        distance_from_azimuth_walls=6/80;
    }
    /**
     * Metoda udostepniająca obiektowi grafike
     * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
     */
    public BufferedImage createBufferedImage(){
        try {
            File file = new File(TurkeyBallString);
            BufferedImage bufferedImage= ImageIO.read(file);
            return bufferedImage;
        }
        catch(IOException e ){
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu turkey ball");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
