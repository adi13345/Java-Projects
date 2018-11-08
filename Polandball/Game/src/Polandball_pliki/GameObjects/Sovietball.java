package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.Monsterspeed;
import static Polandball_pliki.Others.GetConstans.NaziBallString;
import static Polandball_pliki.Others.GetConstans.SovietBallString;

/**
 * Klasa reprezentujaca wroga typu Sovietball
 */
public class Sovietball extends Enemy {
    /**
     * Konstruktor klasy Sovietball
     * @param x obecne polozenie obiektu na osi x
     * @param y obecne polozenie obiektu na osi y
     */
    public Sovietball(int x,int y){
        super();
        x_=x;
        y_=y;
        velX_=0;//Monsterspeed;
        velY_=0;//Monsterspeed;
        name_class_object=SovietBallString;
        buffImage_=createBufferedImage();
        distance_from_elevation_walls=1/32;
        distance_from_azimuth_walls=1/32;
    }
    /**
     * Metoda udostepniająca obiektowi grafike
     * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
     */
    public BufferedImage createBufferedImage(){
        try {
            File file = new File(SovietBallString);
            BufferedImage bufferedImage= ImageIO.read(file);
            return bufferedImage;
        }
        catch(IOException e ){
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu sovietball");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
