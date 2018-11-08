package Polandball_pliki.GameObjects;

/**
 * Klasa przodek dla wszystkich poruszającyhc/żyjących obiektow
 */
public class LivingObject extends GameObject {
    /**
     * Predkosc obiektu na osi x
     */
    int velX_;
    /**
     * Predkosc obiektu na osi y
     */
    int velY_;
    /**
     * Zmienna okreslajaca przerwe miedzy punktami skrajnymi na zachodzie i wschodzie balla a krawedziami bocznymi grafiki
     */
    double distance_from_elevation_walls;
    /**
     * Zmienna okreslajaca przerwe miedzy punktami skrajnymi na północy i południu balla a krawedziami sufitu i podłogi grafiki
     */
    double distance_from_azimuth_walls;

    /**
     * Konstruktor bezparametrowy klasy Living Object
     */
    public LivingObject(){
        super();
        x_=0;
        y_=0;
        velX_=0;
        velY_=0;
        buffImage_=null;
        distance_from_azimuth_walls=0;
        distance_from_elevation_walls=0;
    }
    /**
     * Metoda zmienajaca wartosc predkosci na osi X
     * @param velX nowa wartosc predkosci na osi X
     */
    public void change_velX(int velX){
        velX_=velX;
    }

    /**
     * Metoda zmienajaca wartosc predksci na osi Y
     * @param velY nowa wartosc prędkosci na osi Y
     */
    public void change_velY(int velY){
        velY_=velY;
    }

    /**
     * Metoda zwracajaca dotychczasowa predkosc na osi X
     * @return velX_ predkosc obiektu na osi x
     */
    public int get_velX(){return velX_;}
    /**
     * Metoda zwracajaca dotychczasowa predkosc na osi Y
     * @return velY_ predkosc obiektu na osi y
     */
    public int get_velY(){return velY_;}

    /**
     * Metoda zwracaja poziomy wspolczynnik odleglosci
     * @return distance_from_elevation_walls
     */
    public double getDistance_from_elevation_walls(){return distance_from_elevation_walls;}
    /**
     * Metoda zwracaja pionowy wspolczynnik odleglosci
     * @return distance_from_azimuth_walls
     */
    public double getDistance_from_azimuth_walls(){return distance_from_azimuth_walls;}
}
