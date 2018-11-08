package Polandball_pliki.GameObjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Polandball_pliki.Others.GetConstans.ExplosionString;

/**
 * Klasa reprezentujaca eksplozje bomby
 */
public class Explosion extends GameObject {
        /**
         * Flaga informujaca, czy nastapil juz koniec eksplozji
         */
        boolean end_of_explosion_;
        /**
         * Zmienna, przechowujaca gif eksplozji
         */
        public Image image_;
        /**
         * Konstruktor klasy Explosion
         * @param x obecne polozenie obiektu na osi x
         * @param y obecne polozenie obiektu na osi y
         */
        public Explosion(int x,int y){
            super();
            name_class_object=ExplosionString;
            x_=x;
            y_=y;
            buffImage_=createBufferedImage();
            image_=createImageGIF();
            end_of_explosion_=false;
        }
        /**
         * Konstruktor bezparametrowy klasy Explosion
         */
        public Explosion(){
            super();
            name_class_object=ExplosionString;
            x_=0;
            y_=0;
            buffImage_=null;
            image_=null;
            end_of_explosion_=false;
        }

        /**
        * Metoda udostepniająca obiektowi grafike
        * @return bufferedImage/null - zwraca stworzone zdjecie lub w wypadku zlapania wyjatku null
        */
        public BufferedImage createBufferedImage(){
            try {
                File file = new File(ExplosionString);
                BufferedImage bufferedImage= ImageIO.read(file);

                return bufferedImage;
            }
            catch(IOException e ){
                e.printStackTrace();

                System.out.println("Blad wczytywania obiektu1");
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Blad wczytywania obiektu typu eksplozja");
            }
            return null;
        }
        /**
         * Metoda udostepniająca obiektowi grafike typu gif
         * @return bufferedImage/null - zwraca stworzony gif lub w wypadku zlapania wyjatku null
         */
        public Image createImageGIF(){
            try{
                image_= Toolkit.getDefaultToolkit().createImage(ExplosionString); //ladowanie do pliku
                return image_;
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Blad wczytywania obiektu typu eksplozja");
            }
            return null;
        }

        /**
         * ZWraca Image bedacy gifem
         *
         * @return gif z eksplozja
         */
        public Image getGIF() {
            return image_;
        }

        /**
         * metoda zwracajaca flage, czy eksplozja sie powinna juz zakonczyc
         * @return zwraca flage
         */
        public boolean get_end_of_explosion() {
            return end_of_explosion_;
        }

        /**
         * metoda ustawiajaca flage, czy eksplozja trwa, czy sie skonczyla
         */
        public void set_end_of_explosion(boolean end_of_explosion) {
            this.end_of_explosion_ = end_of_explosion;
        }
}

