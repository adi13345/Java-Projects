package Polandball_pliki.GameObjects;

import java.util.Random;

import Polandball_pliki.Collision.CollisionLivingObjectWithTerrain;
import static Polandball_pliki.Others.GetConstans.*;

/**
 * Klasa reprezentujaca wroga
 */
public class Enemy extends LivingObject implements Runnable {

    /**
     * Konstruktor bezparametrowy klasy Enemy
     */
    public Enemy(){
        super();
        x_=0;
        y_=0;
        velX_=Monsterspeed;
        velY_=Monsterspeed;
        buffImage_=null;
        name_class_object=null;
    }

    /**
     * Zmienna określająca, który kierunek bedzie rozpatrywany podczas sprawdzania kolizji
     */
    int enemydirection=0;
    /**
     * Metoda zwracajaca kierunek ruchu wroga
     * @return enemydirection - kierunek wroga
     */
    public int getEnemydirection(){
        return enemydirection;
    }
    /**
     * Metoda zwracaja danego wroga
     */
    public Enemy getEnemy(){
        return this;
    }

    /**
     * Metoda run() watku wroga, losoujaca kierunek ruchu, wywolujaca metody sprawdzajace kolizje i zmieniajaca pozycje
     * wrogow po sprawdzeniu warunkow kolizji
     */

    @Override
    public void  run(){
           try {
               while (true) {//petla nieskonczona, caly czas sprawdzany jest możliwy kierunek ruchu
                   synchronized (this){//poczatek bloku synchronizujacego
                   Thread.sleep(5);//usypianie watku na x milisekund, wszystko wyliczamy co ten czas
                   //tworzenie obiektow klasy Collision, do sprawdzania kolizji
                   boolean can_I_go_East = new CollisionLivingObjectWithTerrain(this.getEnemy(), "enemy").Collision_East();
                   boolean can_I_go_West = new CollisionLivingObjectWithTerrain(this.getEnemy(), "enemy").Collision_West();
                   boolean can_I_go_North = new CollisionLivingObjectWithTerrain(this.getEnemy(), "enemy").Collision_North();
                   boolean can_I_go_South = new CollisionLivingObjectWithTerrain(this.getEnemy(), "enemy").Collision_South();
                   //w zaleznosci od obranego kierunku (kierunku w jakim w tej chwili porusza sie dany wrog)
                   //jest sprawdzana odpowiednia kolizja, w odpowiedniej kolejnosci
                   //0-wschod,1-zachod,2-polnoc,3-poludnie
                   //------------->bez losowania kierunku<-----------------
                   /*switch (enemydirection) {
                       case 0:
                           if (can_I_go_East == true) {
                               enemydirection = 0;
                           } else if (can_I_go_West == true) {
                                   enemydirection = 1;
                           } else if (can_I_go_North == true ) {
                                   enemydirection = 2;
                           } else if (can_I_go_South == true) {
                                   enemydirection = 3;
                           }
                           break;
                       case 1:
                           if (can_I_go_West == true) {
                               enemydirection = 1;
                           } else if (can_I_go_North == true) {
                                   enemydirection = 2;
                           } else if (can_I_go_South == true) {
                                   enemydirection = 3;
                           } else if (can_I_go_East == true) {
                                   enemydirection = 0;
                           }
                           break;
                       case 2:
                           if (can_I_go_North == true) {
                               enemydirection = 2;
                           } else if (can_I_go_South == true) {
                                   enemydirection = 3;
                           } else if (can_I_go_East == true) {
                                   enemydirection = 0;
                           } else if (can_I_go_West == true) {
                                   enemydirection = 1;
                           }
                           break;
                       case 3:
                           if (can_I_go_South == true) {
                               enemydirection = 3;
                           } else if (can_I_go_East == true) {
                                   enemydirection = 0;
                           } else if (can_I_go_West == true) {
                                   enemydirection = 1;
                           } else if (can_I_go_North == true) {
                                   enemydirection = 2;
                           }
                           break;
                   }*/

                   //-------------->z losowaniem kierunku<-------------
                   //-------------->upewnic sie czy dziala<------------
                   switch (enemydirection) {
                       case 0:
                           if (can_I_go_East == true) {
                               enemydirection = 0;
                           } else {
                               Random rand = new Random();//jak nie moze isc juz, to losujemy nastepny kirunek, tak dla kazdego
                               int los = rand.nextInt(3);
                               if (can_I_go_West == true && los == 0 ) {
                                   enemydirection = 1;
                               } else if (can_I_go_North == true && los == 1) {
                                   enemydirection = 2;
                               } else if (can_I_go_South == true && los == 2) {
                                   enemydirection = 3;
                               }
                           }
                           break;
                       case 1:
                           if (can_I_go_West == true) {
                               enemydirection = 1;
                           } else{
                               Random rand = new Random();
                               int los = rand.nextInt(3);
                               if (can_I_go_North == true && los == 0) {
                                   enemydirection = 2;
                               } else if (can_I_go_South == true && los == 1) {
                                   enemydirection = 3;
                               } else if (can_I_go_East == true && los == 2) {
                                   enemydirection = 0;
                               }
                           }
                           break;
                       case 2:
                           if (can_I_go_North == true) {
                               enemydirection = 2;
                           } else {
                               Random rand = new Random();
                               int los = rand.nextInt(3);
                               if (can_I_go_South == true && los == 0) {
                                   enemydirection = 3;
                               } else if (can_I_go_East == true && los == 1) {
                                   enemydirection = 0;
                               } else if (can_I_go_West == true && los == 2) {
                                   enemydirection = 1;
                               }
                           }
                           break;
                       case 3:
                           if (can_I_go_South == true) {
                               enemydirection = 3;
                           } else {
                               Random rand = new Random();
                               int los = rand.nextInt(3);
                               if (can_I_go_East == true && los == 0) {
                                   enemydirection = 0;
                               } else if (can_I_go_West == true && los == 1) {
                                   enemydirection = 1;
                               } else if (can_I_go_North == true && los == 3) {
                                   enemydirection = 2;
                               }
                           }
                           break;
                   }
                   }//koniec bloku synchronizujacego
               }
           }catch(Exception e){
               System.out.println("Blad watku wroga");
           }
    }
}