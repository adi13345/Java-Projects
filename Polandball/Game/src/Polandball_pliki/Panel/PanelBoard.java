package Polandball_pliki.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import Polandball_pliki.Collision.*;
import Polandball_pliki.Counter.*;
import Polandball_pliki.Frame.NextLevelInfo;
import Polandball_pliki.GameObjects.*;

import Polandball_pliki.Frame.GameOver;
import Polandball_pliki.Others.GameTime;
import Polandball_pliki.Others.GetConstans;


import static Polandball_pliki.Others.GetConstans.*;
import static Polandball_pliki.Frame.MainFrame.startPanel;
import static Polandball_pliki.Panel.SetConnectionPanel.ServerMode;
import static Polandball_pliki.Panel.SetNameFramePanel.levelframe;

/**
 * Klasa reprezentujaca plansza gry
 */

public class PanelBoard extends JPanel implements ActionListener,KeyListener{
    /**
     * Zmienna sprawdzajaca, czy gracz ma wlaczone skrzydla hussarskie
     */
    public static boolean hussars_Power = false;
    /**
     * Zmienna okreslajaca, czy gracz jest w poblizu drzwi
     */
    public static boolean player_stay_on_door = false;
    /**
     * Tablica obiektow przeciwnikow
     */
    public static ArrayList<Enemy> enemy=new ArrayList<>();

    /**
     * Tablica obiektow typu gracz
     */
    public static  ArrayList<Polandball> player=new ArrayList<>();
    /**
     * Tablica obiektow zdalnych bomb
     */
    public static Remote_Bomb remote_bomb=null;

    /**
     * Tablica obiektow zwykle bomby
     */
    public static ArrayList<Normal_Bomb> normal_bomb=new ArrayList<>();

    /**
     *  Tablica obiektow eksplozji
     */
    public static ArrayList<Explosion> explosions =new ArrayList<>();

    /**
     * Tablica licznikow, jakie sa w tej chwili wlaczone
     */
    public static ArrayList<Counter> counters=new ArrayList<>();

    /**
     *  Tablica obiektow bedacych elementami terenu
     */
    public static ArrayList<Terrain> terrains=new ArrayList<>();

    /**
     *  Tablica itemow (elementow do zebrania/uruchomienia interakcji przez gracza)
     */
    public static ArrayList<Item> items=new ArrayList<>();

    /**
     * Tablica znakow (pol), na ktorej jest zapisana plansza
     */
    public static ArrayList<ArrayList<String>> field = new ArrayList<>();
    /**
     * Tablica watkow wrogow
     */
    public static ArrayList<Thread> TableOfEnemyThreads = new ArrayList<>();
    /**
     * Tablica, do ktorej beda rozdzielane ciagi znakow z pliku konfiguracyjnego
     */
    public String bufor_string[];

    /**
     * Zmienna okreslajaca, czy player zyje
     */
    public static boolean PlayerExistence=false;

    /**
     * Zmienna okreslajaca, czy jest w danej chwili wywietlany laser
     */
    public static boolean LaserExistence=false;

    /**
     * Zmienna okreslajaca, czy gra znajduje sie w stanie pauzy
     */
    public static boolean PauseActive = false;

    /**
     * Zmienna okreslajaca, czy mozemy postawic kolejna zdalna bombe
     */

    public static boolean PermissionForRemoteBomb=true;

    /**
     * Zmienna okreslajaca kierunek rozchodzenia się lasera  0 - polnoc, 1 - poludnie, 2 - wschod, 3 -zachod
     */
    public int directionOfLaser;

    /**
     * Watek zegara gry
     */
    public static Thread timethread;

    /**
     * Szerokosc obiektu graficznego, zalezna od szerokosci panela i ilosci kolumn
     */

    public static int SizeWidthIcon = ((int)(0.8*Boardwidth))/Amountofcolumns;

    /**
     * Wysokosc obiektu graficznego, zalezna od wysokosci panela i ilosci wierszy
     */

    public static int SizeHeightIcon = ((int)(0.8*Boardheight))/Amountoflines;

    /**
     * Zmienna przechowujaca szerokosc pojedynczego pola planszy w typie double
     */

    public static double SizeWidthIconDouble = (0.8*Boardwidth)/(double)((Amountofcolumns));

    /**
     * Zmienna przechowujaca wysokosc pojedynczego pola planszy w typie double
     */

    public static double SizeHeightIconDouble = (0.8*Boardheight)/((double)(Amountoflines));
    /**
     * Zmienna przechowujaca kopie szerokosci pojedynczego pola planszy w typie double
     */

    public static double SizeWidthIconCopyDouble;

    /**
     * Zmienna przechowujaca kopie wysokosci pojedynczego pola planszy w typie double
     */

    public static double SizeHeightIconCopyDouble;

    /**
     * Zmienna okreslajaca predkosc gracza, zalezna od rozmiarow planszy i pol
     */

    public static int SpeedPlayer = setSpeedPlayer();

    /**
     * Zmienna okreslaja, czy nastapila zmiana rozmiarow planszy gry
     */
    public static boolean IsSizeOfPanelBoardChanged;

    /**
     * Konstruktor panelu głównego, zawierający funkcję PanelBoard
     */

    public PanelBoard(){PanelBoard();}


    /**
     * Konstruktor zawierający parametry planszy, rozlozenie obiektow na mapie gry,
     * Konstruktor zawiera rowniez metody tworzace watki wrogow i zegara gry
     */

    private void PanelBoard() {
        panelboardheight =(int)(0.8*Boardheight);
        panelboardwidth =(int)(0.8*Boardwidth);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        //ogólne parametry planszy
        this.setSize(panelboardwidth, panelboardheight);
        this.setBackground(Color.BLACK);

            //dwuwymiarowa tablica, w której zawarte są kody poszczególnych pól planszy, wczytywane z pliku konfiguracyjnego
            for (int i=0;i<Amountoflines;i++) {
                field.add(new ArrayList<>());
                bufor_string=row[i].split(" ");
                for(int j=0;j<Amountofcolumns;j++) {
                    field.get(i).add(bufor_string[j]);
                }
            }


            //wypelnienie dynamicznej tablicy elementami, w zaleznosci od wczytanej konfiguracji
            for(int i=0;i<Amountoflines;i++){
                for(int j=0;j<Amountofcolumns;j++) {
                    if (field.get(i).get(j).equals("N_")) {
                    } else if (field.get(i).get(j).equals("B_")) {
                        terrains.add(new Beton(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie betonu w pole j,i
                    } else if (field.get(i).get(j).equals("S_")) {
                        terrains.add(new Skrzynka(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie skrzynki w pole j,i
                    } else if (field.get(i).get(j).equals("NG")) {
                        player.add(new Polandball(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie playera w polu j,i
                        PlayerExistence=true;
                    } else if (field.get(i).get(j).equals("NW")) {
                        enemy.add(lottery_of_enemies(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie wroga w  pole j,i
                    } else if (field.get(i).get(j).equals("SD")) {
                        items.add(new Door(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie itemu-Drzwi w polu j,i
                        terrains.add(new Skrzynka(SizeWidthIcon*(j),SizeHeightIcon*(i)));//zakrywam item skrzynka
                    } else if (field.get(i).get(j).equals("_D")) {
                        items.add(new Door(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie itemu-Drzwi w polu j,i
                      //  terrains.add(new Skrzynka(SizeWidthIcon*(j),SizeHeightIcon*(i)));//zakrywam item skrzynka
                    } else if (field.get(i).get(j).equals("_K")) {
                        items.add(new Key(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie itemu-Klucz w polu j,i
                      //  terrains.add(new Skrzynka(SizeWidthIcon*(j),SizeHeightIcon*(i)));//zakrywam item skrzynka
                    } else if (field.get(i).get(j).equals("SK")) {
                        items.add(new Key(SizeWidthIcon*(j),SizeHeightIcon*(i)));//dodanie itemu-Klucz w polu j,i
                        terrains.add(new Skrzynka(SizeWidthIcon*(j),SizeHeightIcon*(i)));//zakrywam item skrzynka
                    } else if (field.get(i).get(j).equals("_I")) {
                        items.add(lottery_of_items(SizeWidthIcon*(j),SizeHeightIcon*(i)));
                      //  terrains.add(new Skrzynka(SizeWidthIcon*(j),SizeHeightIcon*(i)));//zakrywam item skrzynka
                    } else if (field.get(i).get(j).equals("SI")) {
                        items.add(lottery_of_items(SizeWidthIcon*(j),SizeHeightIcon*(i)));
                        terrains.add(new Skrzynka(SizeWidthIcon*(j),SizeHeightIcon*(i)));//zakrywam item skrzynka
                    }
                    //do zrobienia jeszcze if z itemami typu skrzydla husarskie czy laser sprawiedliwosci
                }
            }
        createThreadsForEnemy();//tworzenie watkow wrogow
        GameTime gametime = new GameTime();//ustawienie zegara gry,utworzenie obiektu zegara
        timethread =new Thread(gametime.getGameTime());//stworzenie watku dla zegara
        timethread.start();//uruchomienie watku zegara
    }

    /**
     * Metoda sprawdzajaca czy komórka [x][y] jest wolna
     * @return zwraca true jesli w komorce nie znjaduje sie zadna skrzynka lub beton
     */
    public static boolean CellIsFree(int yyy,int xxx){
        if(StatioonaryObjectTab[yyy][xxx]=="N_" || StatioonaryObjectTab[yyy][xxx]=="_K"
             || StatioonaryObjectTab[yyy][xxx]=="_I" || StatioonaryObjectTab[yyy][xxx]=="_D"
                || StatioonaryObjectTab[yyy][xxx]=="NG" || StatioonaryObjectTab[yyy][xxx]=="NW"
                )
            {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Metoda sprawdzajaca czy w komórce [x][y] znajduje się beton
     * @return zwraca true jesli w komorce nie znjaduje sie  beton
     */
    public static boolean BetonIsNotHere(int yyy,int xxx){
        if(StatioonaryObjectTab[yyy][xxx]=="B_" )
        {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Metoda modyfikująca tablice obiektow nieruchomych po jakichs wydarzeniach
     * @param yyy numer wiersza komorki tablicy stacjonarnych obiektow
     * @param xxx numer kolumny komorki tablicy stacjonarnych obiektow
     */

    public void DestroySkrzynkaOnCell(int yyy,int xxx){
            if(StatioonaryObjectTab[yyy][xxx] == "S_"){
              //  System.out.println("1StatioonaryObjectTab"+yyy+" "+xxx+" "+"S_");
                StatioonaryObjectTab[yyy][xxx]="N_";
              //  System.out.println("2StatioonaryObjectTab"+yyy+" "+xxx+" "+"N_");
            } else if(StatioonaryObjectTab[yyy][xxx]=="SD"){
              //  System.out.println("1StatioonaryObjectTab"+yyy+" "+xxx+" "+"SD");
                StatioonaryObjectTab[yyy][xxx]="_D";
              //  System.out.println("2StatioonaryObjectTab"+yyy+" "+xxx+" "+"_D");
            } else if(StatioonaryObjectTab[yyy][xxx]=="SK"){
             //   System.out.println("1StatioonaryObjectTab"+yyy+" "+xxx+" "+"SK");
                StatioonaryObjectTab[yyy][xxx]="_K";
              //  System.out.println("2StatioonaryObjectTab"+yyy+" "+xxx+" "+"_K");
            } else if(StatioonaryObjectTab[yyy][xxx]=="SI"){
              //  System.out.println("1StatioonaryObjectTab"+yyy+" "+xxx+" "+"SI");
                StatioonaryObjectTab[yyy][xxx]="_I";
               // System.out.println("2StatioonaryObjectTab"+yyy+" "+xxx+" "+"_I");
            }
    }

    /**
     * Metoda usuwujaca z tablicy stacjonarnych obiektow przedmioty, ktore zostaly podniesione przez gracza
     * @param row numer wiersza komorki tablicy stacjonarnych obiektow
     * @param column numer kolumny komorki tablicy stacjonarnych obiektow
     */

    public void RemoveItemFromStaTable(int row,int column){
        if(StatioonaryObjectTab[row][column]=="_I"){
            StatioonaryObjectTab[row][column]="N_";
        } else if(StatioonaryObjectTab[row][column]=="_K"){
            StatioonaryObjectTab[row][column]="N_";
        }
    }

    /**
     * Metoda losujaca item, jaki ma sie pojawic pod skrzynka w danym miejscu
     */
    public Item lottery_of_items(int position_itemX,int position_itemY){
        Item item_;
        Random randomGenerator =new Random();
        int fate= randomGenerator.nextInt(5);
        if(fate==0){
            item_=new WingsOfHussar(position_itemX,position_itemY);
        }
        else if(fate==1){
            item_=new ChestOfGold(position_itemX,position_itemY);
        }
        else if(fate==2){
            item_=new BoxOfBomb(position_itemX,position_itemY);
        }
        else if(fate==3){
            item_=new GunLaser(position_itemX,position_itemY);
        }
        else if(fate==4){
            item_=new Heart(position_itemX,position_itemY);
        }
        else {
            item_=null;
        }
        return item_;

    }


    /**
     * Metoda losujaca typ potwora
     */
    public Enemy lottery_of_enemies(int position_enemyX,int position_enemyY){
        Enemy enemy_;//instancja klasy enemy do ktorej w zaleznosci od losu przypisze konkretny enemyballa
        Random randomGenerator=new Random(); //inicjuje zmienna losujaca
        int fate= randomGenerator.nextInt(3);//losuje liczbe od 0 do 2
        if(fate==0){//kiedy wylosuje zero losuje turkey balla
            enemy_=new Turkeyball(position_enemyX,position_enemyY);
        }else if(fate==1){//kiedy wylosuje jeden losuje naziballa
            enemy_=new Naziball(position_enemyX,position_enemyY);
        }else if(fate==2){//kiedy wylosuje dwa losuje sovietballa
            enemy_=new Sovietball(position_enemyX,position_enemyY);
        }else {
            enemy_=null;//zwraca null w innych przypadkach, jakis blad w losowaniu
        }
        return enemy_;//zwracam wylosowanego wroga

    }

    /**
     * Metoda okreslajaca zasieg lasera
     * @param direction - kierunek propagacji 0 - polnoc, 1 - poludnie, 2 - wschod, 3 -zachod
     */
    public int rangeOfLaser(int direction){
        int positionX=player.get(0).getX();
        int positionY=player.get(0).getY();
        boolean ICheckNextCell=true;
        int i=0;
        if(direction==0){
            ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_North();
            while(ICheckNextCell){
                i++;
                positionY=positionY-SizeHeightIcon;
                ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_North();
            }
            return i;
        }
        else if(direction==1){
            positionY=positionY+SizeHeightIcon;
            ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_South();
            while(ICheckNextCell){
                i++;
                positionY=positionY+SizeHeightIcon;
                ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_South();
            }
            return i;
        }
        else if(direction==2){
            positionX=positionX+SizeWidthIcon;
            ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_East();
            while(ICheckNextCell){
                i++;
                positionX=positionX+SizeWidthIcon;
                ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_East();
            }
            return i;
        }
        else if(direction==3){
            ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_West();
            while(ICheckNextCell){
                i++;
                positionX=positionX-SizeWidthIcon;
                ICheckNextCell=new CollisionGameObjectWithLaser(positionY,positionX).Collision_West();
            }
            return i;
        }
        return i;
    }



    /**
     * Metoda wywolywana przy wywołaniu jakiejś akcji (np. przesuniecia obiektu z punktu a do b)
     * @param e parametr przchowujacy informacje na temat zmian w programie
     */
    public void actionPerformed(ActionEvent e) {
        if(PauseActive==false) {
            movePlayer();//meteda odpowiedzialna za poruszenie gracza
            moveEnemy();//metoda odpowiedzialna za poruszanie wrogow
            checkPlayerHasCollectedItem();//metoda sprawdzajaca czy gracz zebrał którys item
            checkNormalBombStatus();//sprawdzanie na bieżąco czy bomba nie ma juz wybuchnąć
            checkExplosionStatus();//sprawdzanie czy przypadkiem eksplozja nie powinna sie juz skonczyc
            checkDestroyOfLaser(); // sprawdzenia objektow czy nie sa na drodze lasera
            checkRespawnPlayer(); // sprawdzanie czy w razie gdyby player zginal nie nastapil czas jego respawnu
            checkCounter();//funkcja sprawdzajaca stan licznik w grze
            repaint();//odmalowywanie gracza po kazdym wykrytym zdarzeniu
        }
    }


    /**
     * Metoda sprawdzajaca, czy gracz nie zebral ktoregos przedmiotu
     */
    void checkPlayerHasCollectedItem() {
        if (items.size() > 0 && player.size() > 0) {

            boolean Player_not_take_a_item = true;//zmienna okreslajaca czy player schwycil item
            for (Iterator<Item> iteratoItems = items.iterator(); iteratoItems.hasNext(); ) {//iteratoItems wskazuje na kolejne elementy z kolekcji items
                Item itemInstance = iteratoItems.next();// przypisuje obiekt item na obiekt na który wskazuje iteraItems
                    Player_not_take_a_item = new CollisionPlayerWithItem(player.get(0), itemInstance).getIsNotCollision();
                    //wykonuje sie sie kiedy player wykonal kolizje z itemem i w omawianym miejscu nie ma zadnej skrzynki
                    if (!Player_not_take_a_item && CellIsFree(itemInstance.getRowY(),itemInstance.getColumnX())) {
                       // System.out.println("Zlapalem " + itemInstance.getNameClassObject());
                        if(!itemInstance.getNameClassObject().equals(DoorString)){
                            RemoveItemFromStaTable(itemInstance.getRowY(),itemInstance.getColumnX());//zmieniam wartosc w tablicy stworzonej w GetConstan apropo pol w ktore mogą wejsc living object
                            iteratoItems.remove();//usuwam element jesli kolizja wykryla zetkniecie sie itema i gracza
                        }
                        //sprawdzamy, ktory itemek podnieslismy
                        if (itemInstance.getNameClassObject().equals(WingsOfHussarString)) {
                            Amountofhusarswings = ChangeInfoStatus(Amountofhusarswings, 1);//dodanie skrzydel do ekwipunku
                            PanelInfoTwo.Iloscskrzydelhusarskich.setText(Integer.toString(Amountofhusarswings));//wyswietlenie w labelu
                            Amountofpoints = ChangeInfoStatus(Amountofpoints, PointsForItem);//punty za itemek
                            PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                        } else if (itemInstance.getNameClassObject().equals(ChestOfGoldString)) {
                            Amountofpoints = ChangeInfoStatus(Amountofpoints, PointsForChestOfGold);//punty za skrzynke zlota
                            PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                        } else if (itemInstance.getNameClassObject().equals(GunLaserString)) {
                            Amountoflasers = ChangeInfoStatus(Amountoflasers, 1);//dodanie lasera do ekwipunku
                            PanelInfoTwo.Ilosclaserow.setText(Integer.toString(Amountoflasers));//wyswietlenie w labelu
                            Amountofpoints = ChangeInfoStatus(Amountofpoints, PointsForItem);//punty za itemek
                            PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                        } else if (itemInstance.getNameClassObject().equals(HeartString)) {
                            Amountoflifes = ChangeInfoStatus(Amountoflifes, 1);//dodanie zycia
                            PanelInfoOne.LifeLabel2.setText(Integer.toString(Amountoflifes));//wyswietlenie w labelu
                            Amountofpoints = ChangeInfoStatus(Amountofpoints, PointsForItem);//punty za itemek
                            PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                        } else if (itemInstance.getNameClassObject().equals(BoxOfBombsString)) {
                            Amountofremotebombs = ChangeInfoStatus(Amountofremotebombs, 1);//dodanie zdalnej bomby
                            PanelInfoTwo.Iloscbombzdalnych.setText(Integer.toString(Amountofremotebombs));//wyswietlenie w labelu
                            Amountofordinarybombs = ChangeInfoStatus(Amountofordinarybombs, 2);//dodanie zwyklej bomby
                            PanelInfoTwo.Iloscbombzwyklych.setText(Integer.toString(Amountofordinarybombs));//wyswietlenie w labelu
                            Amountofpoints = ChangeInfoStatus(Amountofpoints, 3 * PointsForItem);//punty za itemek
                            PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                        } else if (itemInstance.getNameClassObject().equals(KeyString)) {
                            Amountofkeys = ChangeInfoStatus(Amountofkeys, 1);
                            Amountofpoints = ChangeInfoStatus(Amountofpoints,  PointsForKey);//punty za itemek
                            PanelInfoTwo.Ilosckluczy.setText(Integer.toString(Amountofkeys));//wyswietlenie w labelu
                        } else if (itemInstance.getNameClassObject().equals(DoorString) &&  Amountofkeys>0){
                                player_stay_on_door=true;
                        }
                    }
                    else {
                        player_stay_on_door=false;
                    }
                }
            }
        }

    /**
     * Metoda zmieniajaca polozenie gracza
     */

    void movePlayer() {
        if(player.size()>0) {
            //zmienna przechowujaca informacje czy player nie zachacza o instancje jakiegos terenu
            boolean I_can_go = new CollisionLivingObjectWithTerrain(player.get(0), "player").getIsNotCollision();
            boolean Enemy_didnt_caught_player= checkPotentialCollisionWithEnemy();
            if (I_can_go && Enemy_didnt_caught_player) {//jesli jest I_can_go oraz Enemy_didnt_caught_player jest true to player moze sie poruszyc
                player.get(0).changeX(player.get(0).getX() + player.get(0).get_velX());
                player.get(0).changeY(player.get(0).getY() + player.get(0).get_velY());
            }
        }
    }

    /**
     * Metoda sprawdzajaca, czy nie nastapila kolizja gracza z wrogiem
     * @return isNotCollision
     */
    boolean checkPotentialCollisionWithEnemy() {
        boolean isNotCollision = true;
        int i = 0;
        if (LaserExistence == false) {
            for (Iterator<Enemy> iteratoEnemy = enemy.iterator(); iteratoEnemy.hasNext(); ) {
                Enemy enemy1 = iteratoEnemy.next();
                isNotCollision = new CollisionPlayerWithEnemy(player.get(0), enemy1).getIsNotCollision();
                if (isNotCollision == false) {

                    counters.add(new CounterPlayer(player.get(0)));
                    if (hussars_Power == false) {
                        Amountoflifes = ChangeInfoStatus(Amountoflifes, -1);//zmniejszenie zyc o 1
                        PanelInfoOne.LifeLabel2.setText(Integer.toString(Amountoflifes));//aktualizacja wyswietlenia ilosci zyc
                        checkPlayerLifes();//metoda sprawdzajaca czy graczowi nei skonczyly sie zycia
                        PlayerExistence = false;
                        player.remove(0);
                    } //usuwa playera z mapy
                    else if (hussars_Power == true)/// w trybie hussarskim to nasz Dzielny Polandball niszczy wrogów !
                    {
                        //TableOfEnemyThreads.get(i).stop();
                        //  TableOfEnemyThreads.remove(i);
                        iteratoEnemy.remove();//usuwa enemy wskazywany przez iterator
                        Amountofpoints = ChangeInfoStatus(Amountofpoints, PointsForMonster);//punty za zabicie potwora
                        PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                    }
                    return isNotCollision;
                }
                // i++;
            }
        }
        return isNotCollision;
    }

    /**
     * Metoda tworzaca watki dla wrogow
     */

    void createThreadsForEnemy(){
        try {
            for (int i = 0; i < enemy.size(); i++) {
                Thread EnemyThread = new Thread(enemy.get(i).getEnemy());//.start();//(new Enemy(enemy.get(i).getEnemy())).start();
                EnemyThread.start();
                TableOfEnemyThreads.add(EnemyThread);
            }

        } catch(Exception e){
            System.out.println("Blad w watku");
            System.out.println(e);
        }
    }
    /**
     * Metoda poruszajaca wrogow
     */

    void moveEnemy(){
        for(int i=0;i<enemy.size();i++){
        //dla kazdego worga kierunek ruchu jest wyliczany w niezaleznym watku
        int kierunek = enemy.get(i).getEnemydirection();
            //zmiana predkosci (poruszania sie) wroga, w zaleznosci od kierunku
            switch(kierunek) {
                case 0:
                    enemy.get(i).change_velX(1);
                    enemy.get(i).change_velY(0);
                break;
                case 1:
                    enemy.get(i).change_velX(-1);
                    enemy.get(i).change_velY(0);
                break;
                case 2:
                    enemy.get(i).change_velY(-1);
                    enemy.get(i).change_velX(0);
                break;
                case 3:
                    enemy.get(i).change_velY(1);
                    enemy.get(i).change_velX(0);
                break;
            }
            enemy.get(i).changeX(enemy.get(i).getX() + enemy.get(i).get_velX());
            enemy.get(i).changeY(enemy.get(i).getY() + enemy.get(i).get_velY());
        }
    }

    /**
     * Metoda sprawdzajaca, czy bomba juz wybuchla
     */

    private void checkNormalBombStatus(){
        //sprawdzam wszystkie bomby po kolei
        for(int i=0;i<normal_bomb.size();i++) {
            if (normal_bomb.get(i).getExplosionflag() == true){//wywoluje sie gdy flaga bomby informujaca ze ma teraz eksplozje jest true
                createExplosion(normal_bomb.get(i)); // tworze eksplozje na ekranie
                normal_bomb.remove(i);//usuniecie danej bomby z tablicy bomb, jesli wybuchla(czas sie skonczyl)
            }
        }
    }

    /**
     * Metoda tworzaca eksplozja w okreslonym obszarze
     * @param bomb  bomba ktora wybucha i zostawia eksplozje
     */
    private void createExplosion(Bomb bomb){
        explosions.add(new Explosion(bomb.getX()-SizeWidthIcon,bomb.getY()-SizeHeightIcon));//dodaje do tablicy obiekt typu eksplozja
        // UWAGAbedzie wymiarow 3 na 3 wiec przesuwam go o jedna kolumne na zachod i o wiersz na polnoc
        checkTerrainToDestroyByExplosion();//wywoluje funkcje sprawdzajaca czy w wyniku ekplozji nie powinno sie zniszczyc jakiejs skrzynki
        counters.add(new CounterExplosion(explosions.get(explosions.size()-1) ) );//dodaje licznik liczacy czas trwania eksplozji
    }

    /**
     * Metoda sprawdzajaca, czy eksplozja nie zniszczyla jakichs skrzynek, usuwa zniszczoen skrzynki z tablicy stacjonarnych obiektow
     */
    public void checkTerrainToDestroyByExplosion(){

        boolean notDestroy=true;//zmienna przechowujaca informacje czy aby napewno dochodzi do jakiegoś zniszczenia obiektu

        for(Iterator<Terrain> iteratoTerrain =terrains.iterator();iteratoTerrain.hasNext();) {// dokonuje iteracji po calym zbiorze terrain za pomoca iteratorow, 1) tworze iterator 2) ustawiam go na pierwszy element tej kolejkcji
            Terrain ter = iteratoTerrain.next();//tworze instancje obiektu w petli na podstawie obiektu na ktory wskazuje iterator
            if (ter.getNameClassObject().equals(SkrzynkaString)) {//jesli element skrzynka zwroci parametr name_class_object taki sam jak SkrzynkaString, to wykona sie if
                notDestroy = new CollisionExplosionWithSkrzynki( ter, explosions.get(explosions.size() - 1) ).getIsNotCollision();//sprawdzam czy ta konkretne skrzynke moze zniszczyc eksplozja
                if (!notDestroy) {//jesli eksplozja pokazuje ze mamy zniszczyc obiekt wykonuje sie if
                    DestroySkrzynkaOnCell(ter.getRowY(),ter.getColumnX());//zmieniam wartosc w tablicy stworzonej w GetConstan apropo pol w ktore mogą wejsc living object
                    iteratoTerrain.remove();//usun obiekt wskazywany przez iterator ( a wlasciwie to pozbadz sie wszelkich wskaznikow na niego wskazujacych )
                    Amountofpoints=ChangeInfoStatus(Amountofpoints,PointsForCreate);//punkty za skrzynki
                    PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//aktualizacja wyswietlenia punktow
                }
            }
        }

    }

    /**
     * Metoda sprawdzajaca status eksplozji (czy eksplozja juz sie skonczyla)
     */
    private void checkExplosionStatus(){
        for (int i=0;i<explosions.size();i++){//iteruje po co calej kolekcji eksplozje
            if(explosions.get(i).get_end_of_explosion()==true){//jesli eksplozja ma flage oznaczajaca koniec jej istnienie to odpala sie if z usunieciem eksplozji z ekranu
                explosions.remove(i);
            }
        }
        checkPlayerToDestroy();//sprawdza czy player nie zginie w wyniku wybuchu eksplozji
        checkEnemyToDestroy();//sprawdza czy ktorys z potwoorow nie zginie w wyniku eksplozji
    }

    /**
     * Metoda niszczaca obiekty znajdujace sie w zasiegu lasera
     */
    public void checkObjectToDestroyByLaser(){

        boolean notDestroy=true;//zmienna przechowujaca informacje czy aby napewno dochodzi do jakiegoś zniszczenia obiektu
        int xLaser=0;
        int yLaser=0;
        int heightLaser=0;
        int widthLaser=0;

        if (directionOfLaser == 0) {
            xLaser=player.get(0).getX()+SizeWidthIcon/4;
            yLaser=player.get(0).getY() - (SizeHeightIcon * rangeOfLaser(0));
            widthLaser=SizeWidthIcon / 2;
            heightLaser=SizeHeightIcon * rangeOfLaser(0);
        } else if (directionOfLaser == 1) {
            xLaser=player.get(0).getX()+SizeWidthIcon/4;
            yLaser=player.get(0).getY() + SizeHeightIcon;
            widthLaser=SizeWidthIcon / 2;
            heightLaser=SizeHeightIcon * rangeOfLaser(1);
        } else if (directionOfLaser == 2) {
            xLaser=player.get(0).getX() + SizeWidthIcon;
            yLaser=player.get(0).getY()+SizeHeightIcon/4;
            widthLaser=rangeOfLaser(2) * SizeWidthIcon;
            heightLaser=SizeHeightIcon / 2;
        } else if (directionOfLaser == 3) {
            xLaser=player.get(0).getX() - SizeWidthIcon * rangeOfLaser(3);
            yLaser=player.get(0).getY()+SizeHeightIcon/4;
            widthLaser=rangeOfLaser(3) * SizeWidthIcon;
            heightLaser=SizeHeightIcon / 2;
        }
        for(Iterator<Terrain> iteratoTerrain =terrains.iterator();iteratoTerrain.hasNext();) {// dokonuje iteracji po calym zbiorze terrain za pomoca iteratorow, 1) tworze iterator 2) ustawiam go na pierwszy element tej kolejkcji
            Terrain ter = iteratoTerrain.next();//tworze instancje obiektu w petli na podstawie obiektu na ktory wskazuje iterator
            if (ter.getNameClassObject().equals(SkrzynkaString)) {//jesli element skrzynka zwroci parametr name_class_object taki sam jak SkrzynkaString, to wykona sie if
                notDestroy=new CollisionGameObjectWithLaser(xLaser,yLaser,heightLaser,widthLaser,ter).getIsNotCollision();
                // notDestroy = new CollisionExplosionWithSkrzynki( ter, explosions.get(explosions.size() - 1) ).getIsNotCollision();//sprawdzam czy ta konkretne skrzynke moze zniszczyc eksplozja
                if (!notDestroy) {//jesli eksplozja pokazuje ze mamy zniszczyc obiekt wykonuje sie if
                    DestroySkrzynkaOnCell(ter.getRowY(),ter.getColumnX());//zmieniam wartosc w tablicy stworzonej w GetConstan apropo pol w ktore mogą wejsc living object
                    iteratoTerrain.remove();//usun obiekt wskazywany przez iterator ( a wlasciwie to pozbadz sie wszelkich wskaznikow na niego wskazujacych )
                    Amountofpoints=ChangeInfoStatus(Amountofpoints,PointsForCreate);//punkty za skrzynki
                    PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//aktualizacja wyswietlenia punktow
                }
            }
        }
        boolean notDestroyEnemy=true;
            //int i=0;
            for (Iterator<Enemy> iteratoEnemy = enemy.iterator(); iteratoEnemy.hasNext(); ) {//iteruje po kolekcji enemy 1) tworze iterator dla kolekcji enemy 2) przypisuje go do do pierwszego elementu kolekcji enemy
                Enemy enemInstance = iteratoEnemy.next();//tworze instancje obiektu typu enemy na podstawie obiektu na ktory wskazuje w danej chwili iterator
                notDestroyEnemy=new CollisionGameObjectWithLaser(xLaser,yLaser,heightLaser,widthLaser,enemInstance).getIsNotCollision();
                if (!notDestroyEnemy) {//jesli w wyniku obliczen wyszlo ze potwor ucierpi w wyniku eksplozji
                 //   TableOfEnemyThreads.get(i).stop();
                   // TableOfEnemyThreads.remove(i);
                    iteratoEnemy.remove();//usuwa enemy wskazywany przez iterator
                    Amountofpoints=ChangeInfoStatus(Amountofpoints,PointsForMonster);//punty za zabicie potwora
                    PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                }
                //i++;
            }
    }
    /**
     * Metoda wywolujaca funkcje usuwujacaca obiekty  zniszczone przez laser, w przypadku, gdy laser jest aktywny
     */
    public void checkDestroyOfLaser(){
        if(LaserExistence==true) {
            checkObjectToDestroyByLaser();
        }
    }

    /**
     * Metoda sprawdzajaca, czy gracz nie zostanie zniszczonyw w wyniku eksplozji
     */

    private void checkPlayerToDestroy(){
        if(player.size()>0) {

            boolean notDestroy = true;// flaga jednoznacznie stwierdzajaca czy obiekt typu player powinnien zostac usuniety
            for (Iterator<Explosion> iteratoExplosion = explosions.iterator(); iteratoExplosion.hasNext(); ) {//iteruje po kolekcji explosion 1) tworze iterator dla kolekcji ekplozjii 2) przypisuje go do do pierwszego elementu kolekcji
                Explosion explInstance = iteratoExplosion.next();//tworze instancje obiektu typu eksplozja na podstawie obiektu na ktory wskazuje w danej chwili iterator
                notDestroy = new CollisionLivingObjectWithExplosion(player.get(0), explInstance).getIsNotCollision();//sprawdzam czy player napewno nie ucierpi w wyniku eksplozji
                if (!notDestroy) {//jesli w wyniku oblicazen wyszlo ze gracz ucierpi w wyniku eksplozji
                    Amountoflifes=ChangeInfoStatus(Amountoflifes,-1);//zmniejszenie zyc o 1
                    PanelInfoOne.LifeLabel2.setText(Integer.toString(Amountoflifes));//aktualizacja wyswietlenia ilosci zyc
                    PlayerExistence=false;
                    counters.add(new CounterPlayer(player.get(0)));
                    player.remove(0); //usuwa playera z mapy
                    checkPlayerLifes();//metoda sprawdzajaca czy graczowi nei skonczyly sie zycia
                }
            }
        }
    }

    /**
     * Metoda sprawdzajaca, czy obiekt typu Enemy nie zostanie zniszczony w wyniku eksplozji
     */

    private void checkEnemyToDestroy(){
        boolean notDestroy;// flaga jednoznacznie stwierdzajaca czy obiekt typu enemy powinnien zostac usuniety

        for(Iterator<Explosion> iteratoExplosion = explosions.iterator();iteratoExplosion.hasNext();) {//przelatuje po kolekcji explosion
            Explosion explInstance=iteratoExplosion.next();// przypisuje do zminnej insntanceExplosion obiekt na ktory wskazuje iterator
            int i=0;
            for (Iterator<Enemy> iteratoEnemy = enemy.iterator(); iteratoEnemy.hasNext(); ) {//iteruje po kolekcji enemy 1) tworze iterator dla kolekcji enemy 2) przypisuje go do do pierwszego elementu kolekcji enemy
                Enemy enemInstance = iteratoEnemy.next();//tworze instancje obiektu typu enemy na podstawie obiektu na ktory wskazuje w danej chwili iterator
                    notDestroy = new CollisionLivingObjectWithExplosion(enemInstance, explInstance).getIsNotCollision();//sprawdzam czy enemy napewno nie ucierpi w wyniku eksplozji
                    if (!notDestroy) {//jesli w wyniku obliczen wyszlo ze potwor ucierpi w wyniku eksplozji
                       // TableOfEnemyThreads.get(i).stop();
                     //   TableOfEnemyThreads.remove(i);
                        iteratoEnemy.remove();//usuwa enemy wskazywany przez iterator
                        Amountofpoints=ChangeInfoStatus(Amountofpoints,PointsForMonster);//punty za zabicie potwora
                        PanelInfoOne.PointLabel2.setText(Integer.toString(Amountofpoints));//wyswietlenie w labelu
                    }
                i++;
            }
        }
    }

    /**
     * Metoda wywolujaca funkcje respawnu playera, jesli gracz zginal i minal jego czas respawnu
     */
    public void checkRespawnPlayer(){

        if(PlayerExistence==true && player.size()==0) {
            setRespawnPolandball();//wywolanie metody ustawiajacej gracza w polu startowym
        }
    }

    /**
     * Metoda ustawiajaca gracza w miejscu startowym po respawnie
     */
    public static void setRespawnPolandball() {
        for (int i = 0; i < Amountoflines; i++) {
            for (int j = 0; j < Amountofcolumns; j++) {
                if (field.get(i).get(j).equals("NG")) {
                    player.add(new Polandball(SizeWidthIcon * (j), SizeHeightIcon * (i)));//dodanie player w polu j,i
                }
            }
        }
    }

    /**
     * Metoda sprawdzajaca, czy ktorys "doliczyl sie" do konca
     */
    void checkCounter(){
        for(int i=0;i<counters.size();i++){//iteruje po kolekcji liczniki
            counters.get(i).checkTime();//sprawdzam czy aby i-ty element kolekcji liczniki nie powinien juz zakonczyc swojego dzialania
            if(counters.get(i).getisStillNeed()==false){//sprawdzam czy licznik w ogole jeszcze jest potrzebny
                counters.remove(i);//usuwam niepotrzebny licznik
            }
        }
    }

    /**
     * Metoda obslugujaca nacisniecie klawiszy przez gracza
     * @param e wydarzenie przechowujace informacje na temat wcisnietego klawisza
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if(PauseActive==false) { // sprawdzam czy gra nie jest w stanie pauzy
            if (player.size() > 0) {
                int factor_mode=1;
                if(hussars_Power==true){
                    factor_mode=2;
                }
                //if(remote_bomb==null && normal_bomb.size()==0) {
                    if (c == KeyEvent.VK_LEFT && LaserExistence == false) {
                        player.get(0).change_velX(setSpeedPlayer() * -1 * (factor_mode));
                        player.get(0).change_velY(0);
                    } else if (c == KeyEvent.VK_RIGHT && LaserExistence == false) {
                        player.get(0).change_velX(setSpeedPlayer() * 1 * (factor_mode));
                        player.get(0).change_velY(0);
                    } else if (c == KeyEvent.VK_DOWN && LaserExistence == false) {
                        player.get(0).change_velY(setSpeedPlayer() * 1 * (factor_mode));
                        player.get(0).change_velX(0);
                    } else if (c == KeyEvent.VK_UP && LaserExistence == false) {
                        player.get(0).change_velY(setSpeedPlayer() * (-1) * factor_mode);
                        player.get(0).change_velX(0);
                    }
                //}
                //stawianie bomby
                else if (c == KeyEvent.VK_SPACE) {
                    if (Amountofordinarybombs > 0) {//sprawdzenie czy mamy jeszcze bomby
                        normal_bomb.add(new Normal_Bomb(player.get(0).getX(), player.get(0).getY()));//dodanie dodani bomby do tablicy
                        counters.add(new CounterNormalBomb(normal_bomb.get(normal_bomb.size() - 1)));//ustawienie licznika dla danego obiekty typu bomba
                        Amountofordinarybombs = ChangeInfoStatus(Amountofordinarybombs, -1);//zmniejszenie ilosci bomb
                        PanelInfoTwo.Iloscbombzwyklych.setText(Integer.toString(Amountofordinarybombs));//aktualizacja ilosci bomb
                        // znajdujacego sie w tablicy
                    } else {
                        System.out.println("Nie masz wiecej bomb");
                    }
                } else if (c == KeyEvent.VK_Z  && PermissionForRemoteBomb != false) { // stawianie zdalnej bomby
                    System.out.println("remote " + (Amountofremotebombs > 0));
                    if (Amountofremotebombs > 0) {
                        remote_bomb = new Remote_Bomb(player.get(0).getX(), player.get(0).getY());//dodanie bomby do tablicy
                        Amountofremotebombs = ChangeInfoStatus(Amountofremotebombs, -1);//zmniejszenie ilosci bomb
                        PanelInfoTwo.Iloscbombzdalnych.setText(Integer.toString(Amountofremotebombs));//aktualizacja ilosci bomb
                        //znajdujace sie w tablicy
                        PermissionForRemoteBomb = false;//nie mozemy postawic nastepnej zdalnej bomby
                    }
                } else if (remote_bomb != null && c == KeyEvent.VK_X) { //detonacja zdalnej bomby
                    createExplosion(remote_bomb); // tworze eksplozje na ekranie
                    remote_bomb = null; //zwracam referencje na obiekt null, z powrotem moge stawiac bombe
                    PermissionForRemoteBomb = true;//po zdetonowaniu zdalnej bomby mozemy postawic nastepna
                } else if (c == KeyEvent.VK_E) {
                    if (player_stay_on_door == true && Amountofkeys > 0) {
                        PauseActive = true;//zatrzymanie gry
                        NextLevelInfo nextlevelinfo = new NextLevelInfo();
                        nextlevelinfo.setVisible(true);
                    } else if (player_stay_on_door == true && Amountofkeys == 0) {
                        System.out.println("Brak kluczy do otworzenia drzwi");
                    } else if (player_stay_on_door == false && Amountofkeys == 1) {
                        System.out.println("Brak drzwi które mozna otworzyc");
                    }
                } else if(c == KeyEvent.VK_W) { // transformacja w husarza
                    if (Amountofhusarswings > 0 && hussars_Power == false) {
                        hussars_Power = true;
                        Amountofhusarswings = ChangeInfoStatus(Amountofhusarswings, -1);
                        PanelInfoTwo.Iloscskrzydelhusarskich.setText(Integer.toString(Amountofhusarswings));//aktualizacja ilosci skrzydel
                        System.out.println("Hussarball, Active!");
                        counters.add(new CounterHussarWings());
                    }
                }

                if(normal_bomb.size()==0 && remote_bomb==null) {
                    if (c == KeyEvent.VK_R) { //laser, polnoc
                        if (Amountoflasers > 0) {
                            Amountoflasers = ChangeInfoStatus(Amountoflasers, -1);
                            PanelInfoTwo.Ilosclaserow.setText(Integer.toString(Amountoflasers));
                            directionOfLaser = 0;
                            LaserExistence = true;
                            counters.add(new CounterLaser());
                        }
                    } else if (c == KeyEvent.VK_F) { //laser,poludnie
                        if (Amountoflasers > 0) {
                            Amountoflasers = ChangeInfoStatus(Amountoflasers, -1);
                            PanelInfoTwo.Ilosclaserow.setText(Integer.toString(Amountoflasers));
                            directionOfLaser = 1;
                            LaserExistence = true;
                            counters.add(new CounterLaser());
                        }
                    } else if (c == KeyEvent.VK_G) { //laser,wschod
                        if (Amountoflasers > 0) {
                            Amountoflasers = ChangeInfoStatus(Amountoflasers, -1);
                            PanelInfoTwo.Ilosclaserow.setText(Integer.toString(Amountoflasers));
                            directionOfLaser = 2;
                            LaserExistence = true;
                            counters.add(new CounterLaser());
                        }
                    } else if (c == KeyEvent.VK_D) { //laser,zachod
                        if (Amountoflasers > 0) {
                            Amountoflasers = ChangeInfoStatus(Amountoflasers, -1);
                            PanelInfoTwo.Ilosclaserow.setText(Integer.toString(Amountoflasers));
                            directionOfLaser = 3;
                            LaserExistence = true;
                            counters.add(new CounterLaser());
                        }
                    }
                }

            }
            if (c == KeyEvent.VK_P) {
                PauseActive = true;
                levelframe.setResizable(true);

            }
        }
        else if( c==KeyEvent.VK_P ) {
            PauseActive=false;
            levelframe.setResizable(false);
        }

    }

    /**
     * Metoda wymuszona przez intefejs, nieuzywana
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e){
    }

    /**
     * Metoda opisujaca co sie dzieje gdy nie jest wciskany zaden klawisz
     * @param e zmienna przechowujaca informacje o dzialaniach w danej chwili na klawiszach
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(player.size()>0) {
            player.get(0).change_velY(0);
            player.get(0).change_velX(0);
        }
    }


    /**
     * Metda rysujaca wszystkie obiekty graficzne
     * @param g grafika
     */

    public void paint(Graphics g){
        setSpeedPlayer();//ustwienie odpowiedniej predkosci gracza
        if(PauseActive == true) {
            changeSizeIcons();//zmiana rozmiarow pol i kopiowanei staryh
            Rectangle rectangle = new Rectangle(levelframe.getWidth(),levelframe.getHeight());
            rectangle.setLocation(levelframe.getX(),levelframe.getY());
            levelframe.setMaximizedBounds(rectangle);
            changeTerrainPosition(SizeWidthIconDouble, SizeHeightIconDouble);//zmiana pozycji obietkow stacjonarnych
            changeItemPosition(SizeWidthIconDouble, SizeHeightIconDouble);//zmiana pozycji itemow
            changePlayerPosition();//zmiana pozycji playera
            changeEnemyPosition();// zmiana pozycji wrogow
        }
        g.setColor(Color.black);
        g.fillRect(0,0,getWidth(),getHeight());// rysuje czarny kwadrat bedacy tlem dla naszych grafik
        drawItem(g); //rysuje itemy na planszy
        drawBomb(g); //rysuje bomby na planszy
        drawPlayerObject(g);//rysuje playera
        drawEnemyObject(g);//rysuje wrogow
        drawTerrain(g);//rysuje elementy terenu - skrzynki, beton itd
        drawExplosion(g);//rysuje eksplozje
        drawLaser(g);//rysuje laser
    }
    /**
     * Metoda rysujaca przedmioty
     * @param g grafika
     */
    public void drawItem(Graphics g){
        for(int i=0;i<items.size();i++) {
            g.drawImage(items.get(i).getBuffImage(), items.get(i).getX(), items.get(i).getY(), SizeWidthIcon, SizeHeightIcon, null);
        }
    }
    /**
     * Metoda rysujaca bomby
     * @param g grafika
     */
    public void drawBomb(Graphics g){
        //rysowanie wszystkich bomb na ekran
        for(int i=0;i<normal_bomb.size();i++){
                    g.drawImage(normal_bomb.get(i).getGIF(), normal_bomb.get(i).getX(), normal_bomb.get(i).getY(), SizeWidthIcon, SizeHeightIcon, this);
        }
        if(remote_bomb!=null){
                    g.drawImage(remote_bomb.getBuffImage(),remote_bomb.getX(),remote_bomb.getY(),SizeWidthIcon,SizeHeightIcon,null);
        }
    }

    /**
     * Metoda rysujaca eksplozje
     * @param g grafika
     */
    public void drawExplosion(Graphics g) {
        //rysowanie wszystkich eksplozji na ekran
        for (int i = 0; i < explosions.size(); i++) {
            {
                if(PauseActive==false) {
                    g.drawImage(explosions.get(i).getGIF(), explosions.get(i).getX(), explosions.get(i).getY(), 3 * SizeWidthIcon, 3 * SizeHeightIcon, this);
                }
                else{
                    g.drawImage(explosions.get(i).getGIF(), explosions.get(i).getX(), explosions.get(i).getY(), 3 * SizeWidthIcon, 3 * SizeHeightIcon, null);
                }
            }
        }
    }

    /**
     * Metoda rysujaca gracza
     * @param g grafika
     */
    public void drawPlayerObject(Graphics g) {
        if(player.size()>0 && PlayerExistence==true) {
            if(hussars_Power==false) {
                g.drawImage(player.get(0).getBuffImage(), player.get(0).getX(), player.get(0).getY(), SizeWidthIcon, SizeHeightIcon, null);
            }else if(hussars_Power==true) {
                if(player.get(0).get_velX()>0) {
                    g.drawImage(player.get(0).getRightHussar(), player.get(0).getX(), player.get(0).getY(), SizeWidthIcon, SizeHeightIcon, null);
                }else if(player.get(0).get_velX()<0){
                    g.drawImage(player.get(0).getLeftHussar(), player.get(0).getX(), player.get(0).getY(), SizeWidthIcon, SizeHeightIcon, null);
                }else {
                    g.drawImage(player.get(0).getRightHussar(), player.get(0).getX(), player.get(0).getY(), SizeWidthIcon, SizeHeightIcon, null);
                }
            }
        }
    }
    /**
     * Metoda rysujaca wrogow
     * @param g grafika
     */

    public void drawEnemyObject(Graphics g){
        for(int i=0;i<enemy.size();i++){
                g.drawImage(enemy.get(i).getBuffImage(), enemy.get(i).getX(), enemy.get(i).getY(), SizeWidthIcon, SizeHeightIcon,  null);
        }
    }

    /**
     * Metoda rysujaca elementy terenu
     * @param g grafika
     */

    public void drawTerrain(Graphics g){
        for(int i=0;i<terrains.size();i++) {
            g.drawImage(terrains.get(i).getBuffImage(),terrains.get(i).getX(),terrains.get(i).getY(),SizeWidthIcon,SizeHeightIcon,null);
        }
    }
    /**
     * Metoda rysujaca laser
     * @param g grafika
     */

    public void drawLaser(Graphics g){
        if(LaserExistence==true && PlayerExistence==true) {
            g.setColor(Color.YELLOW);
            if (directionOfLaser == 0) {
                g.fillRect(player.get(0).getX()+SizeWidthIcon/4, player.get(0).getY() - (SizeHeightIcon * rangeOfLaser(0)), SizeWidthIcon / 2, SizeHeightIcon * rangeOfLaser(0));
            } else if (directionOfLaser == 1) {
                g.fillRect(player.get(0).getX()+SizeWidthIcon/4, player.get(0).getY() + SizeHeightIcon, SizeWidthIcon / 2, SizeHeightIcon * rangeOfLaser(1));
            } else if (directionOfLaser == 2) {
                g.fillRect(player.get(0).getX() + SizeWidthIcon, player.get(0).getY()+SizeHeightIcon/4, rangeOfLaser(2) * SizeWidthIcon, SizeHeightIcon / 2);
            } else if (directionOfLaser == 3) {
                g.fillRect(player.get(0).getX() - SizeWidthIcon * rangeOfLaser(3), player.get(0).getY()+SizeHeightIcon/4, rangeOfLaser(3) * SizeWidthIcon, SizeHeightIcon / 2);
            }
        }
    }


    /**
     * Metoda zmieniaja pozycje obiektow stacjonarnych podczas skalowania
     * @param nowaszerokoscicony - szerokosc pola/obiektu po zmianie szerokosci ramki
     * @param nowawysokoscicony - wysokosc pola/obiektu po zmianie wysokosci ramki
     */

    public void changeTerrainPosition(double nowaszerokoscicony, double nowawysokoscicony){
        for(int i=0; i<terrains.size(); i++){
            if(nowaszerokoscicony > SizeWidthIconCopyDouble || nowawysokoscicony > SizeHeightIconCopyDouble) {
                terrains.get(i).changeX((int) (Math.ceil(terrains.get(i).getX()/SizeWidthIconDouble) *nowaszerokoscicony));
                terrains.get(i).changeY((int) (Math.ceil(terrains.get(i).getY()/SizeHeightIconDouble) * nowawysokoscicony ));
            }else if( nowaszerokoscicony < SizeWidthIconCopyDouble || nowawysokoscicony  < SizeHeightIconCopyDouble){
                terrains.get(i).changeX((int) (terrains.get(i).getColumnX() * nowaszerokoscicony));
                terrains.get(i).changeY((int) (terrains.get(i).getRowY()*nowawysokoscicony ));
            }
        }
    }
    /**
     * Metoda zmieniajaca pozycje itemow podczas skalowania
     * @param nowaszerokoscicony - szerokosc obiektu po zmianie szerokosci ramki
     * @param nowawysokoscicony - wysokosc obiektu po zmianie wyskosci ramki
     */
    public void changeItemPosition(double nowaszerokoscicony, double nowawysokoscicony){
        for(int i=0; i<items.size(); i++){
            if(nowaszerokoscicony > SizeWidthIconCopyDouble || nowawysokoscicony > SizeHeightIconCopyDouble) {
                items.get(i).changeX((int) (Math.ceil(items.get(i).getX()/SizeWidthIconDouble) *nowaszerokoscicony));
                items.get(i).changeY((int) (Math.ceil(items.get(i).getY()/SizeHeightIconDouble) * nowawysokoscicony ));
            }else if( nowaszerokoscicony < SizeWidthIconCopyDouble || nowawysokoscicony  < SizeHeightIconCopyDouble){
                items.get(i).changeX((int) (items.get(i).getColumnX() * nowaszerokoscicony));
                items.get(i).changeY((int) (items.get(i).getRowY()*nowawysokoscicony ));
            }
        }
    }

    /**
     * Metoda zmieniajaca pozycje gracza podczas skalowania
     */
    public void changePlayerPosition(){

        if(IsSizeOfPanelBoardChanged == true && PlayerExistence==true) {
           int kolumnaplayera =(int) Math.floor(((double)(player.get(0).getX()+(SizeWidthIcon/2))/((double)(SizeWidthIcon))));
           int wierszplayera= (int) Math.floor(((double)(player.get(0).getY()+(SizeHeightIcon/2))/((double)(SizeHeightIcon))));
           player.get(0).changeX(kolumnaplayera*SizeWidthIcon);
           player.get(0).changeY(wierszplayera*SizeHeightIcon);


        }
    }

    /**
     * Metoda zmieniajca pozycje wrogow podczas skalowania
     */
    public void changeEnemyPosition(){
        if(IsSizeOfPanelBoardChanged == true){
            int kolumnaenemy;
            int wierszenemy;
            for (Iterator<Enemy> iteratoEnemy = enemy.iterator(); iteratoEnemy.hasNext(); ) {//iteruje po kolekcji enemy 1) tworze iterator dla kolekcji enemy 2) przypisuje go do do pierwszego elementu kolekcji enemy
                Enemy enemInstance = iteratoEnemy.next();//tworze instancje obiektu typu enemy na podstawie obiektu na ktory wskazuje w danej chwili iterator
                kolumnaenemy =(int) Math.floor(((double)(enemInstance.getX()+(SizeWidthIcon/2))/((double)(SizeWidthIcon))));
                wierszenemy = (int) Math.floor(((double)(enemInstance.getY()+(SizeHeightIcon/2))/((double)(SizeHeightIcon))));
                enemInstance.changeX(kolumnaenemy*SizeWidthIcon);
                enemInstance.changeY(wierszenemy*SizeHeightIcon);
            }
        }
    }
    /**
     * Metoda zmieniajaca okreslony parametr
     * @param whattochange informuje jaka rzecz chcemy zmienic
     * @param howmuchtochange o ile chemy ja zmienic
     */
    public static int ChangeInfoStatus(int whattochange, int howmuchtochange){
        whattochange=whattochange+howmuchtochange;
        return whattochange;
    }

    /**
     * Metoda sprawdzajaca, czy gra nie powinna sie juz skonczyc, gdy gracz straci zycia
     */
    private void checkPlayerLifes(){
        if(Amountoflifes==0){
            GameOver gameover = new GameOver();
            gameover.setVisible(true);
            PauseActive=true;
        }
    }

    /**
     * Metoda przywracajaca ustawienia domyslne sprzed rozpoczecia rozgrywki
     */

    public static void MakeDefaultOption(int level){
        try{
            timethread.stop();//zatrzymanie watku licznika
            if(startPanel.MakeSocket()!=null && ServerMode==true){
                SetConnectionPanel.GetLevelConfig(startPanel.MakeSocket(), level);//pobranie danych pierwszego poziomu na poczatku gry
                GetConstans.MakeBoardObstacleTable();//zaladowanie jeszcze raz tablicy kolizji
            }else{
                GetConstans.read_on_level(level);//wczytanie konkretnego poziomu
                GetConstans.MakeBoardObstacleTable();//zaladowanie jeszcze raz tablicy kolizji
            }
            SizeHeightIcon = (int)((0.8*(double)Boardheight))/Amountoflines;//ustawienie pożadanych rozmiarów pól
            SizeWidthIcon = ((int)(0.8*(double)Boardwidth))/Amountofcolumns;
            KillEnemyThreads();//zabijane watkow wrogow
            ClearArraylists();//wywolanie nizej zdefiniowanej metody odpowiedzialnej za czyscienie buforw dynamicznych
        }catch(Exception e){
            System.out.println(e+"Blad metody panelboarda - PanelBoard, MakeDefaultOption()");
        }
    }

    /**
     * Metoda czyszczaca wszystkie dynamiczne tablice, zerujaca flagi
     */

    public static void ClearArraylists(){
        //czyszczenie wszystkich buforow
        enemy.clear();
        player.clear();
        remote_bomb=null;
        normal_bomb.clear();
        explosions.clear();
        counters.clear();
        terrains.clear();
        items.clear();
        field.clear();
        TableOfEnemyThreads.clear();
        PauseActive=false;
        PermissionForRemoteBomb=true;
        hussars_Power=false;
        LaserExistence=false;
    }

    /**
     * Metoda odpowiedzialna za niszczenie watkow wrogow po zakonczeniu rozgrywki lub zmiany poziomu
     */
    public static void KillEnemyThreads(){
        for (int i=0; i<TableOfEnemyThreads.size();i++) {//stopowanie wszystkich watkow wroga
            TableOfEnemyThreads.get(i).stop();
        }
    }


    /**
     * Metoda zmieniajaca parametry pol i kopiujaca stare parametry pola
     */

    public void changeSizeIcons(){
        SizeWidthIconCopyDouble = SizeWidthIconDouble;//kopiowanie szerokosci pola
        SizeHeightIconCopyDouble = SizeHeightIconDouble;//kopiowanie wyskosci pola
        SizeWidthIconDouble = ((double)(getWidth())/(double)(Amountofcolumns));//skalowanie szerokosci elementow, nowa wartosc
        SizeHeightIconDouble =((double)(getHeight())/(double)(Amountoflines));//skalowanie wysokosci elementow, nowa wartosc
        SizeWidthIcon = (int)((SizeWidthIconDouble));
        SizeHeightIcon = (int)((SizeHeightIconDouble));
        if(SizeWidthIconCopyDouble != SizeWidthIconDouble || SizeHeightIconCopyDouble != SizeHeightIconDouble){
            IsSizeOfPanelBoardChanged = true;
        }else{
            IsSizeOfPanelBoardChanged = false;
        }
    }


    /**
     * Metoda obliczaja predkosc gracza
     * @return SpeedPlayer - predkosc gracza
     */

    public static int setSpeedPlayer(){
        //patrzymy, ktora wielkosc - szerokosc czy wysoksoc icony jest mniejsza - wybieramy mniejsza
        //kazde 10 pikseli rozmiaru icony to 1 pksel predkosc, zaokraglane w dol
        int mniejszyrozmiar;
        mniejszyrozmiar = Math.min(SizeWidthIcon,SizeHeightIcon);
        SpeedPlayer = (int)(Math.floor((mniejszyrozmiar/10)));
        return SpeedPlayer;
    }
}
