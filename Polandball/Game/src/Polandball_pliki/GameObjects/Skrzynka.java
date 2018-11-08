package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.SkrzynkaString;

/**
 * Klasa reprezentujaca skrzynke - zniszczalny element terenu
 */
public class Skrzynka extends Terrain {
    /**
     * Konstruktor klasy Skrzynka
     * @param x polozenie na osi x
     * @param y polozenie na osi y
     */
    public Skrzynka(int x,int y){
        super();
        x_=x;
        y_=y;
        name_class_object=SkrzynkaString;
        buffImage_=createBufferedImage();
    }

    /**
     * Metoda udostepniająca obiektowi grafike
     * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
     */
    public BufferedImage createBufferedImage(){
        try {
            File file = new File(SkrzynkaString);
            BufferedImage bufferedImage= ImageIO.read(file);
            return bufferedImage;
        }
        catch(IOException e ){
            e.printStackTrace();
            System.out.println("Blad wczytywania obiektu typu skrzynka");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
