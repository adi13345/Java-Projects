package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.Remote_BombString;

/**
 * Klasa reprezentujaca bomby zdalne
 */
public class Remote_Bomb extends Bomb {

    /**
     * Konstruktor klasy Remote_Bomb
     * @param x polozenie na osi x
     * @param y polozenie na osi y
     */
    public Remote_Bomb(int x, int y){
        super();
        x_=x;
        y_=y;
        buffImage_=createBufferedImage();
        image_=null;
        name_class_object=Remote_BombString;
        explosionflag_=false;
    }

    /**
     * Metoda udostepniająca obiektowi grafike
     * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
     */
    public BufferedImage createBufferedImage(){
        try {
            File file = new File(Remote_BombString);
            BufferedImage bufferedImage= ImageIO.read(file);

            return bufferedImage;
        }
        catch(IOException e ){
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu typu normal_bomb");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu typu normal_bomb");
        }
        return null;
    }
}
