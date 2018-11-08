package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.ChestOfGoldString;
import static Polandball_pliki.Others.GetConstans.WingsOfHussarString;

/**
 * Klasa reprezentujaca skrzynke ze zlotem
 */

public class ChestOfGold extends Item {

    /**
     * Konstruktor klasy ChestOfGold
     * @param x polozenie na osi x
     * @param y polozenie na osi y
     */
    public ChestOfGold(int x, int y){
        super();
        x_=x;
        y_=y;
        name_class_object=ChestOfGoldString;
        buffImage_=createBufferedImage();
    }

    /**
     * Metoda udostepniająca obiektowi grafike
     * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
     */
    public BufferedImage createBufferedImage(){
        try {
            File file = new File(ChestOfGoldString);
            BufferedImage bufferedImage= ImageIO.read(file);
            return bufferedImage;
        }
        catch(IOException e ){
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu typu skrzynia ze złotem");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
