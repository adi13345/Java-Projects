package Polandball_pliki.Collision;

import Polandball_pliki.GameObjects.LivingObject;

import static Polandball_pliki.Others.GetConstans.Amountofcolumns;
import static Polandball_pliki.Others.GetConstans.Amountoflines;
import static Polandball_pliki.Panel.PanelBoard.CellIsFree;
import static Polandball_pliki.Panel.PanelBoard.SizeHeightIcon;
import static Polandball_pliki.Panel.PanelBoard.SizeWidthIcon;

/**
 * Klasa sprawdzajaca, czy LivingObject moze wejsc na dane pole
 */
public class CollisionLivingObjectWithTerrain extends Collision {

    /**
     * Wiersz, w ktorym znajduje sie dany livingobject
     */

    double double_row_livingobject;

    /**
     * Kolumna, w ktorej znajduje sie dany livingobject
     */

    double double_column_livingobject;

    /**
     * Zmienna, bedaca zaokragleniem w dol numeru wiersza
     */

    int row_livingobject_north;

    /**
     * Zmienna bedaca zaokragleniem w dol numeru kolumny
     */
    int column_livingobject_west;

    /**
     * Konstruktor sprawdzajacy, czy zachodzi kolizja LivingObject z elementem terenu
     * @param livingobject - obiekt typu LivingObject
     * @param kind_of_living_object - rodzaj LivingObject, dla ktorego ma byz rozpatrywana kolizja - wrog czy gracz
     */
    //metoda przyjmuje jak argument gracza lub wroga - jesli sprawdamy kolizje dla gracza to kind_of_living_obejct
    //nalezy ustawic na "player" jak wrog to "enemy"
    //wykorzystanie metod sprawdzajacyh konkretny kierunek ruchu, kazda z tych metod, gdy nie wykryje kolizji wg.
    //podanych warunkow ustawia kolozje na true ------>mozna zmienic<------
    public CollisionLivingObjectWithTerrain(LivingObject livingobject, String kind_of_living_object) {
        super(); //wywoluje konstruktor klasy rodzica

        //wiersz w double, w ktorym znajduje sie dany livingobject
        double_row_livingobject = (double) livingobject.getY() / (double) SizeHeightIcon;
        //kolumna w double, w ktorej znajduje sie dany livingobject
        double_column_livingobject = (double) livingobject.getX() / (double) SizeWidthIcon;
        //zaokraglenie numeru wiersza w dol
        row_livingobject_north = (int) Math.floor(double_row_livingobject);
        //zaokraglenie kolumny w dol
        column_livingobject_west = (int) Math.floor(double_column_livingobject);


        //musimy sprawdzic, czy chcemy sprawdzac kolizje dla playera, bo mozemy sterowac graczem, nadawac mu predkosc
        //a wrogom nie
        if (kind_of_living_object == "player") {
            //sprawdzamy czy obiekt porusza sie w prawo > (wschod)
            if (livingobject.get_velX() > 0) {
                Collision_East();
            }
            //sprawdzamy czy obiekt porusza sie w lewo < (zachod)
            else if (livingobject.get_velX() < 0) {
                Collision_West();
            }
            //sprawdzamy czy obiekt porusza sie w w gore ^ (polnoc)
            else if (livingobject.get_velY() < 0) {
                Collision_North();

            }
            //sprawdzamy czy obiekt porusza sie w dol V (poludnie)
            else if (livingobject.get_velY() > 0) {
                Collision_South();
            }//jesli predkosc gracza 0 wyskauje komunikat o braku ruchu(predkosci)
            else if(livingobject.get_velY()==0){///---->problem dla enemy<------
                //System.out.println("Predkosc gracza 0");
            }
        }
    }

    //sprawdzanie kolizji w prawo >
    public boolean Collision_East(){
        try {
            //sprawdzanie czy wyszlismy poza prawa scianke
            if (double_column_livingobject >= (double) (Amountofcolumns - 1 +0.15)) {
               // System.out.println("H");
                isNotCollision = false;
            }
            //sprawdzamy pozycja y-wa obiektu, czy znajduje sie wystarczajaco blisko gornej krawedzi wiersza
            else if (double_row_livingobject < ((double) row_livingobject_north + 0.15)) {
                if (CellIsFree(row_livingobject_north,column_livingobject_west+1)) {
                    //System.out.println("a");
                    isNotCollision = true;
                } else {
                    //System.out.println("m");
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycja y-wa obiektu, czy znajduje sie wystarczajaco blisko dolnej krawedzi wiersza
            else if (double_row_livingobject > ((double) row_livingobject_north + 0.85)) {
                //System.out.println(3);
                if (CellIsFree(row_livingobject_north+1,column_livingobject_west+1)) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycje y-owa obiektu, czy znajduje sie optymalnie w srodku wiersza
            else if (double_row_livingobject > ((double) row_livingobject_north + 0.15) &&
                    double_row_livingobject < ((double) row_livingobject_north + 0.85)) {
                //System.out.println(4+" "+row_player_north+" "+" "+column_player_west);
                if ((CellIsFree(row_livingobject_north,column_livingobject_west+1))&&
                        (CellIsFree(row_livingobject_north+1,column_livingobject_west+1))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            } else {//System.out.println(5);
                isNotCollision = false;
            }
            return isNotCollision;
        } catch (ArrayIndexOutOfBoundsException e) {
            isNotCollision = true;
            //System.out.println("Blad kolizji -wschod");
            //System.out.println(e);
        }

        return isNotCollision;
    }
    //sprawdzanie kolizji w lewo <
    public boolean Collision_West(){
        try {
            if (double_column_livingobject < 0) {//sprawdzanie czy wyszlismy poza lewa scianke
                //System.out.println(1);
                isNotCollision = false;
            }
            //sprawdzamy pozycja y-wa obiektu, czy znajduje sie wystarczajaco blisko gornej krawedzi wiersza
            else if (double_row_livingobject < ((double) row_livingobject_north + 0.15)) {
                //System.out.println(2);
                if ((CellIsFree(row_livingobject_north,column_livingobject_west))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycja y-wa obiektu, czy znajduje sie wystarczajaco blisko dolnej krawedzi wiersza
            else if (double_row_livingobject > ((double) row_livingobject_north + 0.85)) {
                //System.out.println(3);
                if ((CellIsFree(row_livingobject_north+1,column_livingobject_west))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycje y-owa obiektu, czy znajduje sie optymalnie w srodku wiersza
            else if (double_row_livingobject > ((double) row_livingobject_north + 0.15) &&
                    double_row_livingobject < ((double) row_livingobject_north + 0.85)) {
                //System.out.println(4+" "+row_player_north+" "+" "+column_player_west);
                if ((CellIsFree(row_livingobject_north,column_livingobject_west))&&
                        (CellIsFree(row_livingobject_north+1,column_livingobject_west))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            } else {//System.out.println(5);
                isNotCollision = false;
            }
            return isNotCollision;
        } catch (ArrayIndexOutOfBoundsException e) {
            isNotCollision = true;
           //System.out.println("Blad kolizji -zachod");
            //System.out.println(e);
        }

        return isNotCollision;
    }
    //sprawdzanie kolizji w gore ^
    public boolean Collision_North(){
        try {
            if (double_row_livingobject <= 0) {//sprawdzanie czy wyszlismy poza gorna scianke
                isNotCollision = false;
            }
            //sprawdzamy pozycja x-wa obiektu, czy znajduje sie wystarczajaco blisko lewej krawedzi kolumny
            else if (double_column_livingobject < ((double) column_livingobject_west + 0.15)) {
                //System.out.println(2);
                if ((CellIsFree(row_livingobject_north,column_livingobject_west))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycja x-wa obiektu, czy znajduje sie wystarczajaco blisko prawej krawedzi kolumny
            else if (double_column_livingobject > ((double) column_livingobject_west + 0.85)) {
                //System.out.println(3);
                if ((CellIsFree(row_livingobject_north,column_livingobject_west+1))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycje x-owa obiektu, czy znajduje sie optymalnie w srodku kolumny
            else if (double_column_livingobject > ((double) column_livingobject_west + 0.15) &&
                    double_column_livingobject < ((double)column_livingobject_west + 0.85)) {
                //System.out.println(4+" "+row_player_north+" "+" "+column_player_west);
                if ((CellIsFree(row_livingobject_north,column_livingobject_west)) &&
                        (CellIsFree(row_livingobject_north,column_livingobject_west+1))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            } else {//System.out.println(5);
                isNotCollision = false;
            }
            return isNotCollision;
        } catch (ArrayIndexOutOfBoundsException e) {
            isNotCollision = true;
          //  System.out.println("Blad kolizji -polnoc");
          //  System.out.println(e);
        }

        return isNotCollision;
    }
    //sprawdzanie kolizji w dol V
    public boolean Collision_South(){
        try {
            if (double_row_livingobject >= (double) (Amountoflines - 1 +0.15)) {//sprawdzanie czy wyszlismy poza dolna scianke
                //System.out.println(1);
                isNotCollision = false;
            }
            //sprawdzamy pozycja x-wa obiektu, czy znajduje sie wystarczajaco blisko lewej krawedzi kolumny
            else if (double_column_livingobject < ((double) column_livingobject_west + 0.15)) {
                //System.out.println(2);
                if ((CellIsFree(row_livingobject_north+1,column_livingobject_west))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycja x-wa obiektu, czy znajduje sie wystarczajaco blisko prawej krawedzi kolumny
            else if (double_column_livingobject > ((double) column_livingobject_west + 0.85)) {
                //System.out.println(3);
                if ((CellIsFree(row_livingobject_north+1,column_livingobject_west+1))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            }
            //sprawdzamy pozycje x-owa obiektu, czy znajduje sie optymalnie w srodku kolumny
            else if (double_column_livingobject > ((double) column_livingobject_west + 0.15) &&
                    double_column_livingobject < ((double) column_livingobject_west + 0.85)) {
                //System.out.println(4+" "+row_player_north+" "+" "+column_player_west);
                if ((CellIsFree(row_livingobject_north+1,column_livingobject_west)) &&
                        (CellIsFree(row_livingobject_north+1,column_livingobject_west+1))) {
                    isNotCollision = true;
                } else {
                    isNotCollision = false;
                }
            } else {//System.out.println(5);
                isNotCollision = false;
            }
            return isNotCollision;
        } catch (ArrayIndexOutOfBoundsException e) {
            isNotCollision = true;
           // System.out.println("Blad kolizji -poludnie");
          //  System.out.println(e);
        }
        return isNotCollision;
    }
}
