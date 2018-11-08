package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.Normal_BombString;

/**
 * Klasa reprezentujaca zwykla bombe
 */

public class Normal_Bomb extends Bomb {

    /**
     * Konstruktor klasy Normal_bomb
     * @param x polozenie na osi x
     * @param y polozenie na osi y
     */
    public Normal_Bomb(int x,int y){
        super();
        x_=x;
        y_=y;
        buffImage_=createBufferedImage();
        image_=createImageGIF();
        name_class_object=Normal_BombString;
        explosionflag_=false;
    }

    /**
     * Metoda udostepniająca obiektowi grafike
     * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
     */
    public BufferedImage createBufferedImage(){
        try {
            //Image icon =new ImageIcon(new URL())
            File file = new File(Normal_BombString);
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
    /**
     * Metoda udostepniająca obiektowi grafike typu gif
     * @return bufferedImage/null - zwraca stworzony gif lub w wypadku zlapania wyjatku null
     */
    public Image createImageGIF(){
        try{
            image_= Toolkit.getDefaultToolkit().createImage(Normal_BombString); //ladowanie do pliku
            return image_;
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu typu normal_bomb");
        }
        return null;
    }
}
