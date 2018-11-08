package Polandball_pliki.Collision;

import Polandball_pliki.GameObjects.Item;
import Polandball_pliki.GameObjects.Polandball;

import static Polandball_pliki.Panel.PanelBoard.SizeHeightIcon;
import static Polandball_pliki.Panel.PanelBoard.SizeWidthIcon;

/**
 * Klasa sprawdzajaca, czy nastapla kolizja gracza z przedmiotem
 */
public class CollisionPlayerWithItem extends Collision {
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
     * Maksymalny zachodni punkt Item
     */
    int Item_point_west;
    /**
     * Maksymalny wschodni punkt Item
     */
    int Item_point_east;
    /**
     * Maksymalny polnocny punkt Item
     */
    int Item_point_north;
    /**
     * Maksymalny poludniowy punkt Item
     */
    int Item_point_south;
    /**
     * Konstruktor sprawdzajacy, czy zachodzi kolizja obiektu typu Item z graczem
     * @param player - obiekt typu Polandball
     * @param item - obiekt typu Item
     */
    public CollisionPlayerWithItem(Polandball player, Item item){
        super();

        //Punkty skrajne player

        //przydzielam zachodni punkt player
        // 1)pobieram polozenie lewego gornego rogu player
        // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego roza player
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Player_point_west=player.getX()+(int)Math.floor((double)SizeWidthIcon  * 5*player.getDistance_from_azimuth_walls() );//przydzielam zachodni punkt livingObject
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
        Player_point_north=player.getY()+(int)Math.floor((double)SizeHeightIcon  * player.getDistance_from_elevation_walls() );//przydzielam polnocny punkt skrzynki
        //przydzielam wschodni punkt player
        // 1)pobieram polozenie lewego gornego rogu player
        // 2)Dodaje wysokosc jednej komorki
        // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
        // , specyficznie dobrany dla kazdego rodzaju player
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia efektow dzialania eksplozji na brzegach
        Player_point_south=player.getY()+SizeHeightIcon-(int)Math.floor((double)SizeHeightIcon  * player.getDistance_from_elevation_walls() );//przydzielam poludniowy punkt livingObject


        //Punkty skrajne Item

        //przydzielam zachodni punkt item
        // 1)pobieram polozenie lewego gornego rogu item
        // 2)odejmuje odpowiednia ilosc pikseli by oddzwierciedlic item
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Item_point_west=item.getX();//-(int)Math.floor((double)SizeWidthIcon);  //* enemy.getDistance_from_azimuth_walls() );//przydzielam zachodni punkt livingObject
        //przydzielam wschodni punkt enemy
        // 1)pobieram polozenie lewego gornego rogu item
        // 2)Dodaje szerokosc jednej komorki
        // 3)odejmuje ilosc pikseli odzwierciedlajaca pusta przestrzen w itemie
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Item_point_east=item.getX()+SizeWidthIcon;//-(int)Math.floor((double)SizeWidthIcon); // * Item.getDistance_from_azimuth_walls() );
        //przydzielam polnocny punkt enemy
        // 1)pobieram polozenie lewego gornego rogu itemu
        // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt pustej przestrzeni w itemie
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Item_point_north=item.getY();//-(int)Math.floor((double)SizeHeightIcon); // * Item.getDistance_from_elevation_walls() );//przydzielam polnocny punkt skrzynki
        //przydzielam wschodni punkt enemy
        // 1)pobieram polozenie lewego gornego rogu enemy
        // 2)Dodaje wysokosc jednej komorki
        // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt pustej przestrzeni w itemie
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia efektow dzialania eksplozji na brzegach
        Item_point_south=item.getY()+SizeHeightIcon;//-(int)Math.floor((double)SizeHeightIcon)* Item.getDistance_from_elevation_walls() );//przydzielam poludniowy punkt livingObject



        //kolizja  player|item

        //sprawdzam czy player nachodzi na enemy na osi x
      /* if( Player_point_east > Item_point_west && Player_point_east<Item_point_east ) {
            //sprawdzam czy player nachodzi na enemy na osi y
            if(
                    ( Player_point_south > Item_point_north   &&
                    Player_point_south < Item_point_south )
                    ||
                    ( Player_point_north < Item_point_south   &&
                    Player_point_north > Item_point_north )

                    )
                        {
                            isNotCollision = false; // zwraca ze zaszla kolizja z czego wynika że obiekt typu item powinien zostac zlikwidowany
                        }

        }


        //kolizja z wschodnia czescia eksplozji    item|player

        //sprawdzam czy obiekt player na chodzi na enemy na osi x
        else if( Item_point_east > Player_point_west && Player_point_west>Item_point_west ) {
            //sprawdzam czy player nachodzi na enemy na osi y
            if (
                     ( Player_point_south > Item_point_north   &&
                         Player_point_south < Item_point_south )
                      ||
                     ( Player_point_north < Item_point_south  &&
                      Player_point_north > Item_point_north)
                )
                {
                    System.out.println("2");
                    isNotCollision = false;// zwraca ze zaszla kolizja  z czego wynika że obiekt typu item powinien zostac zlikwidowany
                }

        }*/


        //kolizja z polnocna czescia player            player
        //                                               -
        //                                              item

        //sprawdzam czy obiekt player na chodzi na enemy na osi x
        if( Player_point_south > Item_point_north && Player_point_south<Item_point_south ) {
            //sprawdzam czy obiekt player na chodzi na enemy na osi y
            if(
                    (Player_point_east > Item_point_west && Player_point_east < Item_point_east )
                     ||
                     ( Player_point_west < Item_point_east && Player_point_west > Item_point_west )
               )
            {
                isNotCollision = false;// zwraca ze zaszla kolizja z czego wynika że obiekt typu item powinien zostac zlikwidowany
            }

        }


        //kolizja z poludniowa czescia eksplozji          item
        //                                                 -
        //                                                player
        //sprawdzam czy obiekt player na chodzi na enemy na osi x
        if( Player_point_north < Item_point_south && Player_point_south>Item_point_north ){
            //sprawdzam czy obiekt player na chodzi na enemy na osi y
            if(
                    (Player_point_east > Item_point_west && Player_point_east < Item_point_east)
                    ||
                    (Player_point_west < Item_point_east && Player_point_west > Item_point_west)

                    )

                        {
                            isNotCollision = false;// zwraca ze zaszla kolizja z czego wynika że obiekt typu item powinien zostac zlikwidowany
                        }
        }

        else {
            isNotCollision =true;// zwraca ze nie zaszla kolizja
        }
    }

}
