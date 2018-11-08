package Polandball_pliki.Collision;


import Polandball_pliki.GameObjects.Enemy;
import Polandball_pliki.GameObjects.Polandball;
import static Polandball_pliki.Panel.PanelBoard.SizeHeightIcon;
import static Polandball_pliki.Panel.PanelBoard.SizeWidthIcon;

/**
 * Klasa sprawdzajaca, czy nastapla kolizja gracza z wrogiem
 */
public class CollisionPlayerWithEnemy extends Collision {
     /**
     * Maksymalny zachodni punkt gracza
     */
    int Player_point_west;
    /**
     * Maksymalny wschodni punkt gracza
     */
    int Player_point_east;
    /**
     * Maksymalny polnocny punkt gracza
     */
    int Player_point_north;
    /**
     * Maksymalny poludniowy punkt gracza
     */
    int Player_point_south;
    /**
     * Maksymalny zachodni punkt wroga
     */
    int Enemy_point_west;
    /**
     * Maksymalny wschodni punkt wroga
     */
    int Enemy_point_east;
    /**
     * Maksymalny polnocny punkt wroga
     */
    int Enemy_point_north;
    /**
     * Maksymalny poludniowy punkt wroga
     */
    int Enemy_point_south;

    /**
     * Konstruktor sprawdzajacy, czy zachodzi kolizja gracza z wrogiem
     * @param player - obiekt typu Polandball
     * @param enemy - obiekt typu Enemy
     */
    public  CollisionPlayerWithEnemy(Polandball player, Enemy enemy){
        super();

        //Punkty skrajne player

        //przydzielam zachodni punkt player
        // 1)pobieram polozenie lewego gornego rogu player
        // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego roza player
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Player_point_west=player.getX()+(int)Math.floor((double)SizeWidthIcon  * player.getDistance_from_azimuth_walls() );//przydzielam zachodni punkt livingObject
        //przydzielam wschodni punkt player
        // 1)pobieram polozenie lewego gornego rogu player
        // 2)Dodaje szerokosc jednej komorki
        // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego rodzaju player
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Player_point_east=player.getX()+SizeWidthIcon-(int)Math.floor((double)SizeWidthIcon  * player.getDistance_from_azimuth_walls() );
        //przydzielam polnocny punkt player
        // 1)pobieram polozenie lewego gornego rogu eksplozji
        // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego rodzaju player
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Player_point_north=player.getY()-(int)Math.floor((double)SizeHeightIcon  * player.getDistance_from_elevation_walls() );//przydzielam polnocny punkt skrzynki
        //przydzielam wschodni punkt player
        // 1)pobieram polozenie lewego gornego rogu player
        // 2)Dodaje wysokosc jednej komorki
        // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego rodzaju player
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia efektow dzialania eksplozji na brzegach
        Player_point_south=player.getY()+SizeHeightIcon-(int)Math.floor((double)SizeHeightIcon  * player.getDistance_from_elevation_walls() );//przydzielam poludniowy punkt livingObject


        //Punkty skrajne Enemy

        //przydzielam zachodni punkt enemy
        // 1)pobieram polozenie lewego gornego rogu enemy
        // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego roza enemy
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Enemy_point_west=enemy.getX()+(int)Math.floor((double)SizeWidthIcon  * enemy.getDistance_from_azimuth_walls() );//przydzielam zachodni punkt livingObject
        //przydzielam wschodni punkt enemy
        // 1)pobieram polozenie lewego gornego rogu enemy
        // 2)Dodaje szerokosc jednej komorki
        // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego rodzaju enemy
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Enemy_point_east=enemy.getX()+SizeWidthIcon-(int)Math.floor((double)SizeWidthIcon  * enemy.getDistance_from_azimuth_walls() );
        //przydzielam polnocny punkt enemy
        // 1)pobieram polozenie lewego gornego rogu eksplozji
        // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego rodzaju enemy
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Enemy_point_north=enemy.getY()+(int)Math.floor((double)SizeHeightIcon  * enemy.getDistance_from_elevation_walls() );//przydzielam polnocny punkt skrzynki
        //przydzielam wschodni punkt enemy
        // 1)pobieram polozenie lewego gornego rogu enemy
        // 2)Dodaje wysokosc jednej komorki
        // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego rodzaju enemy
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia efektow dzialania eksplozji na brzegach
        Enemy_point_south=enemy.getY()+SizeHeightIcon-(int)Math.floor((double)SizeHeightIcon  * enemy.getDistance_from_elevation_walls() );//przydzielam poludniowy punkt livingObject


        //sprawdzam czy obiekt player na chodzi na enemy na osi x
        if( Player_point_south > Enemy_point_north && Player_point_south<Enemy_point_south ) {
            //sprawdzam czy obiekt player nie na chodzi na enemy na osi y
            if(
                    (Player_point_east > Enemy_point_west && Player_point_east < Enemy_point_east )
                            ||
                            ( Player_point_west < Enemy_point_east && Player_point_west > Enemy_point_west )
                    )
            {
                isNotCollision = false;// zwraca ze zaszla kolizja
            }

        }


        //kolizja z poludniowa czescia eksplozji          item
        //                                                 -
        //                                                player
        //sprawdzam czy obiekt player na chodzi na enemy na osi x
        if( Player_point_north < Enemy_point_south && Player_point_south>Enemy_point_north ){
            //sprawdzam czy obiekt player na chodzi na enemy na osi y
            if(
                    (Player_point_east > Enemy_point_west && Player_point_east < Enemy_point_east)
                            ||
                            (Player_point_west < Enemy_point_east && Player_point_west > Enemy_point_west)

                    )

            {
                isNotCollision = false;// zwraca ze zaszla kolizja
            }
        }

        else {
            isNotCollision =true;// zwraca ze nie zaszla kolizja
        }
    }

}
