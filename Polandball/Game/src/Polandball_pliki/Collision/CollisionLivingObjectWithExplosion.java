package Polandball_pliki.Collision;

import Polandball_pliki.GameObjects.Explosion;
import Polandball_pliki.GameObjects.LivingObject;

import static Polandball_pliki.Panel.PanelBoard.SizeHeightIcon;
import static Polandball_pliki.Panel.PanelBoard.SizeWidthIcon;

/**
 * Klasa okreslajaca, czy nastapila kolizja explozji z obiektem typu LivingObject
 */
public class CollisionLivingObjectWithExplosion extends Collision{
    /**
     * Maksymalny zachodni punkt eksplozji
     */
    int Explosion_point_west;
    /**
     * Maksymalny wschodni punkt eksplozji
     */
    int Explosion_point_east;
    /**
     * Maksymalny polnocny punkt eksplozji
     */
    int Explosion_point_north;
    /**
     * Maksymalny poludniowy punkt eksplozji
     */
    int Explosion_point_south;
    /**
     * Maksymalny zachodni punkt LivingObject
     */
    int LivingObject_point_west;
    /**
     * Maksymalny wschodni punkt LivingObject
     */
    int LivingObject_point_east;
    /**
     *Maksymalny polnocne punkt LivingObject
     */
    int LivingObject_point_north;
    /**
     * Maksymalny poludniowe punkt LivingObject
     */
    int LivingObject_point_south;

    /**
     * Konstruktor sprawdzajacy, czy nie nastapila kolizja eksplozji z LivingObject
     * @param livingObject - obiekt typu LivingObject
     * @param explosion - obiekt typu Explosion
     */
    public  CollisionLivingObjectWithExplosion(LivingObject livingObject, Explosion explosion){
        super();

     //Punkty skrajne eksplozji

        Explosion_point_west=explosion.getX();//przydzielam zachodni punkt eksplozji

        //przydzielam wschodni punkt eksplozji
            // 1)pobieram polozenie lewego gornego rogu eksplozji
            // 2)Dodaje 3 szerokosci komorki
            // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Explosion_point_east=explosion.getX()+3*SizeWidthIcon-(int) Math.floor( (double)SizeWidthIcon/2.25 );
        Explosion_point_north=explosion.getY();//przydzielam polnocny punkt eksplozji
        //przydzielam wschodni punkt eksplozji
            // 1)pobieram polozenie lewego gornego rogu eksplozji
            // 2)Dodaje 3 wysokosci komorki
            // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Explosion_point_south=explosion.getY()+3*SizeHeightIcon-(int) Math.floor( (double)SizeHeightIcon/2.25 );

     //Punkty skrajne LivingObject

        //przydzielam zachodni punkt living object
            // 1)pobieram polozenie lewego gornego rogu living object
            // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
            // , specyficznie dobrany dla kazdego roza living object
            // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        LivingObject_point_west=livingObject.getX()-(int)Math.floor((double)SizeWidthIcon  * livingObject.getDistance_from_azimuth_walls() );//przydzielam zachodni punkt livingObject
        //przydzielam wschodni punkt livingobject
            // 1)pobieram polozenie lewego gornego rogu livingobject
            // 2)Dodaje szerokosc jednej komorki
            // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
            // , specyficznie dobrany dla kazdego rodzaju living object
            // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        LivingObject_point_east=livingObject.getX()+SizeWidthIcon-(int)Math.floor((double)SizeWidthIcon  * livingObject.getDistance_from_azimuth_walls() );
        //przydzielam polnocny punkt livingobject
            // 1)pobieram polozenie lewego gornego rogu eksplozji
            // 2)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
            // , specyficznie dobrany dla kazdego rodzaju living object
            // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        LivingObject_point_north=livingObject.getY()-(int)Math.floor((double)SizeHeightIcon  * livingObject.getDistance_from_elevation_walls() );//przydzielam polnocny punkt skrzynki
        //przydzielam wschodni punkt livingobject
            // 1)pobieram polozenie lewego gornego rogu livingobject
            // 2)Dodaje wysokosc jednej komorki
            // 3)odejmuje ilosc pikseli odzwierciedlajaca fakt ze kula jest mniejsza niz kwadrat który jest jego obramowka
            // , specyficznie dobrany dla kazdego rodzaju living object
            // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia efektow dzialania eksplozji na brzegach
        LivingObject_point_south=livingObject.getY()+SizeHeightIcon-(int)Math.floor((double)SizeHeightIcon  * livingObject.getDistance_from_elevation_walls() );//przydzielam poludniowy punkt livingObject

        //kolizja z zachodnia czescia eksplozji liv|exp

        //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
        if( LivingObject_point_east > Explosion_point_west && LivingObject_point_east<Explosion_point_east ) {
            //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
            if( ( LivingObject_point_south > Explosion_point_north )  &&
                    LivingObject_point_south < Explosion_point_south )
            {
                isNotCollision = false; // zwraca ze zaszla kolizja z czego wynika że obiekt typu skrzynka powinien zostac zlikwidowany
            }

        }

        //kolizja z wschodnia czescia eksplozji    exp|liv

        //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
        else if( Explosion_point_east > LivingObject_point_west && LivingObject_point_west>Explosion_point_west ) {
            //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
            if( ( LivingObject_point_south > Explosion_point_north )  &&
                    LivingObject_point_south < Explosion_point_south )
            {
                isNotCollision = false;// zwraca ze zaszla kolizja  z czego wynika że obiekt typu skrzynka powinien zostac zlikwidowany
            }

        }



        //kolizja z polnocna czescia eksplozji          liv
        //                                               -
        //                                              exp

        //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
        else if( LivingObject_point_south > Explosion_point_north && LivingObject_point_south<Explosion_point_south ) {
            //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
            if(LivingObject_point_east > Explosion_point_west && LivingObject_point_east < Explosion_point_east){
                isNotCollision = false;// zwraca ze zaszla kolizja z czego wynika że obiekt typu livingobject powinien zostac zlikwidowany
            }

        }

        //kolizja z poludniowa czescia eksplozji          exp
        //                                                 -
        //                                                liv
        //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
       if( LivingObject_point_north < Explosion_point_south && LivingObject_point_south>Explosion_point_north ){
           //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
            if(LivingObject_point_east > Explosion_point_west && LivingObject_point_east < Explosion_point_east){
                isNotCollision = false;// zwraca ze zaszla kolizja z czego wynika że obiekt typu livingobject powinien zostac zlikwidowany
            }
        }
        else {
            isNotCollision =true;// zwraca ze nie zaszla kolizja
        }
    }
}
