package Polandball_pliki.Collision;

import Polandball_pliki.GameObjects.Explosion;
import Polandball_pliki.GameObjects.Terrain;

import static Polandball_pliki.Panel.PanelBoard.SizeHeightIcon;
import static Polandball_pliki.Panel.PanelBoard.SizeWidthIcon;
/**
 * Klasa okreslajaca, czy nastapila kolizja eksplozji z obiektem typu Skrzynka
 */
public class CollisionExplosionWithSkrzynki extends Collision{
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
     * Maksymalny zachodni punkt skrzynki
     */
    int Skrzynka_point_west;
    /**
     * Maksymalny wschodni punkt skrzynki
     */
    int Skrzynka_point_east;
    /**
     * Maksymalny polnocny punkt skrzynki
     */
    int Skrzynka_point_north;
    /**
     * Maksymalny poludniowy punkt skrzynki
     */
    int Skrzynka_point_south;
    /**
     * Konstruktor sprawdzajacy, czy skrzynka powinna ulec zniszczeniu pod wplywem eksplozji
     * @param skrzynka - obiekt typ Skrzynka
     * @param explosion - obiekt typu Explosion
     */
    public CollisionExplosionWithSkrzynki(Terrain skrzynka, Explosion explosion){
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
        Explosion_point_south=explosion.getY()+3*SizeHeightIcon-(int) Math.floor( (double)SizeHeightIcon/2.25 );///przydzielam south punkt eksplozji UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji

     //Punkty skrajne skrzynki
        Skrzynka_point_west=skrzynka.getX();//przydzielam zachodni punkt skrzynki
        //przydzielam zachodni punkt skrzynki
            // 1)pobieram polozenie lewego gornego rogu skrzynki
            // 2)dodaje szerokosc jednej komorki
            // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Skrzynka_point_east=skrzynka.getX()+SizeWidthIcon;//przydzielam wschodni punkt skrzynki
        Skrzynka_point_north=skrzynka.getY();//przydzielam polnocny punkt skrzynki
        //przydzielam polnocny punkt skrzynki
        // 1)pobieram polozenie lewego gornego rogu skrzynki
        // 2)dodaje wysokosc jednej komorki
        // UWAGA ostatni człon może pójść do wymiany, służy do ograniczenia oefektow dzialania eksplozji na brzegach
        Skrzynka_point_south=skrzynka.getY()+SizeHeightIcon;//przydzielam poludniowy punkt skrzynki

        //kolizja z zachodnia czescia eksplozji skrzynki|exp

        //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
        if( Skrzynka_point_east > Explosion_point_west && Skrzynka_point_east<Explosion_point_east ) {
                //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
                if( ( Skrzynka_point_south > Explosion_point_north )  &&
                        Skrzynka_point_north < Explosion_point_south )
                {

                    isNotCollision = false; // zwraca ze zaszla kolizja z czego wynika że obiekt typu skrzynka powinien zostac zlikwidowany
                }

        }


        //kolizja z wschodnia czescia eksplozji    exp|skrzynki

        //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
        else if( Skrzynka_point_west < Explosion_point_east && Skrzynka_point_west>Explosion_point_west ) {
            //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
            if( ( Skrzynka_point_south > Explosion_point_north )  &&
                    Skrzynka_point_north < Explosion_point_south )
            {
                isNotCollision = false;// zwraca ze zaszla kolizja  z czego wynika że obiekt typu skrzynka powinien zostac zlikwidowany
            }

        }

        //kolizja z polnocna czescia eksplozji        skrzynki
        //                                               -
        //                                              exp

        //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
        else if( Skrzynka_point_south > Explosion_point_north && Skrzynka_point_south<Explosion_point_south ) {
            //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
            if(Skrzynka_point_east > Explosion_point_west && Skrzynka_point_west < Explosion_point_east){
                isNotCollision = false;
            }

        }
        //kolizja z poludniowa czescia eksplozji       skrzynki
        //                                               -
        //                                              liv

        //sprawdzam czy obiekt miesci sie w polnoc i poludniowym punkcie eksplozji
        else if( (Skrzynka_point_north <Explosion_point_south) && (Skrzynka_point_south>Explosion_point_north) ){
            //sprawdzam czy obiekt miesci sie w wschodnim i zachodnim punkcie eksplozji
            if(Skrzynka_point_east > Explosion_point_west && Skrzynka_point_west < Explosion_point_east){
                isNotCollision = false;// zwraca ze zaszla kolizja z czego wynika że obiekt typu skrzynka powinien zostac zlikwidowany
            }

        }
        else {
                isNotCollision =true;// zwraca ze nie zaszla kolizja
        }
    }
}
