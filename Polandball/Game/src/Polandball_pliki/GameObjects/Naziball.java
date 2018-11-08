package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.NaziBallString;

/**
 * Klasa reprezentujaca wroga typu NaziBall
 */
public class Naziball extends Enemy {

    /**
     * Konstruktor klasy Naziball
     * @param x obecne polozenie obiektu na osi x
     * @param y obecne polozenie obiektu na osi y
     */
    public Naziball(int x,int y){
        super();
        x_=x;
        y_=y;
        velX_=0;
        velY_=0;
        buffImage_=createBufferedImage();
        name_class_object=NaziBallString;
        distance_from_elevation_walls=1/16;
        distance_from_azimuth_walls=1/8;
    }

    /**
     * Metoda udostepniająca obiektowi grafike
     * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
     */
    public BufferedImage createBufferedImage(){
        try {
            File file = new File(NaziBallString);
            BufferedImage bufferedImage= ImageIO.read(file);
            return bufferedImage;
        }
        catch(IOException e ){
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu typu naziball");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
