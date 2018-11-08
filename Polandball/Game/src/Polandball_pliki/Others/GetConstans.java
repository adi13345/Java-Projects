package Polandball_pliki.Others;

import Polandball_pliki.Frame.GameOver;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

import static Polandball_pliki.Panel.PanelBoard.PauseActive;
/**
 * <p>Klasa odpowiedzialna za parsowanie pliku serwera z podstawowymi parametrami aplikacji</p>
 * <p>Przykladowa zawartosc pliku config.xml, pokazujaca kolejnosc parsowania i przypisywania do bufora</p>
 * <p>Nalezy stosowac notacje xml, jak w przykladowych plikach konfiguracyjnych</p>
 *
 <p>Boardheight 700 Boardheight - Wysokosc ramki okna gry</p>
 <p>Boardwidth 1400 Boardwidth - Szerokosc ramki okna gry</p>
 <p>MainFrameheight 200 MainFrameheight - Wysoksc ramki okna glownego</p>
 <p>MainFramewidth 200 MainFramewidth - Szerokosc ramki okna glownego</p>
 <p>HighscoresFrameSize 500 HighscoresFrameSize - Rozmiar ramki okna listy najlepszych wynikow - okno kwadratowe</p>
 <p>TimeToExplosion 1 TimeToExplosion  - Czas, po jakim wybucha bomba zwykla</p>

 <p>PointsForCreate 20 PointsForCreate - Ilosc punktow za zniszczenie skrzynek</p>
 <p>PointsForMonster 100 PointsForMonster  - Ilosc punktow za zabicie potwora</p>
 <p>PointsForItem 10 PointsForItem  - Ilosc punktow zapodniesie itemku</p>
 <p>PointsForChestOfGold 1000 PointsForChestOfGold  - Ilosc punktow za podniesie skrzynki zlota</p>
 <p>PointsForKey 50 PointsForKey  - Ilosc punktow za podniesienie klucza</p>
 <p>PointsForLevel 500 PointsForLevel  - Ilosc punktow za przejscie poziomu</p>
 <p>PointsForSecond 3 PointsForSecond  - Ilosc punktow za kazda sekunde, jesli ukonczymy poziom przed czasem</p>

 * <p>Przykladowa zawartosc pliku z parametrami poziomu, pokazuja kolejnosc parsowania i przypisania do bufora</p>
 * <p>Ilosc kolumn/wierszy musi sie zgadzac z iloscia row-ow oraz liczba pol, jaka zawiera kazdy row</p>
 * <p>Nalezy stosowac notacje xml, jak w przykladowych plikach konfiguracyjnych</p>

 <p>Amountofcolumns 20 Amountofcolumns  - Ilosc kolumn planszy gry</p>
 <p> Amountoflines 20 Amountoflines - Ilosc wierszy planszy gry</p>
 <p>Monsterspeed 3 Monsterspeed   - Predkosc potwora na danym poziomie</p>
 <p>Amountoflifes 2 Amountoflifes   - Ilosc zyc na danym poziomie</p>
 <p>Amountofordinarybombs 50 Amountofordinarybombs   - Ilosc bomb zwyklych na danym poziomie</p>
 <p>Amountofremotebombs 50 Amountofremotebombs   - Ilosc bomb zdalnych na danym poziomie</p>
 <p>Amountofhusarswings 1 Amountofhusarswings  - Ilosc skrzydel husarskich na danym poziomie</p>
 <p>Amountoflasers 1 Amountoflasers  - Ilosc laserow na danym poziomie</p>
 <p>Amountofkeys 1 Amountofkeys - Ilosc kluczy na danym poziomie, zalecane ustawienie tej wartosc na 0</p>
 <p>LevelTime 760 LevelTime   - Czas danego poziomie</p>


 <p>Ponizszy kod zawiera uklad poszczegolnych pol na danym poziomie, oznaczenia:</p>
 <p>N_ - puste pole</p>
 <p>B_ - beton, nieznisczalna przeszkoda</p>
 <p>S_ - skrzynka, przeszkoda, ktora mozna zniszczyc</p>
 <p>NG - pozycja startowa gracza</p>
 <p>NW_ - pozycja startowa wroga</p>
 <p>SD - skrzynka, pod ktora kryja sie drzwi</p>
 <p>SI - skrzynka, pod ktora kyje sie item, rodzaje itemow sa zawarte w dokumentacji klienta gry</p>
 <p>SK - skrzynka, pod znajduje sie klucz do drzwi</p>

 <p>row N_ NG N_ N_ N_ NW NW N_ N_ N_ N_ NW N_ N_ N_ S_ S_ S_ N_ NW row</p>
 <p>row N_ N_ N_ B_ S_ S_ S_ S_ S_ B_ B_ B_ B_ SI S_ N_ N_ NW N_ N_ row</p>
 <p>row N_ B_ B_ B_ S_ S_ SI S_ SI B_ S_ S_ S_ s_ S_ SI S_ B_ S_ S_ row</p>
 <p>row N_ N_ N_ N_ N_ N_ N_ N_ N_ N_ S_ S_ S_ S_ S_ N_ S_ S_ S_ S_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ N_ N_ N_ N_ S_ SI S_ S_ S_ S_ S_ B_ S_ S_ row</p>
 <p>row NW N_ N_ S_ S_ S_ N_ N_ N_ N_ N_ NW N_ N_ N_ N_ S_ S_ S_ S_ row</p>
 <p>row N_ N_ N_ B_ S_ S_ N_ N_ N_ B_ B_ B_ B_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ B_ SI S_ N_ N_ N_ N_ S_ S_ S_ s_ SI S_ S_ B_ S_ S_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ N_ N_ S_ S_ S_ S_ S_ S_ N_ N_ NW N_ N_ N_ N_ N_ NW N_ N_ row</p>
 <p>row NW N_ N_ B_ S_ S_ S_ S_ S_ B_ B_ B_ B_ B_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ B_ S_ S_ S_ S_ S_ B_ S_ S_ S_ s_ S_ N_ N_ N_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ B_ S_ S_ S_ S_ S_ N_ N_ N_ B_ B_ row</p>
 <p>row NW N_ N_ S_ S_ S_ N_ N_ N_ N_ N_ NW N_ N_ N_ N_ S_ S_ S_ S_ row</p>
 <p>row N_ N_ N_ B_ S_ S_ N_ N_ N_ B_ B_ B_ B_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ B_ S_ S_ N_ N_ N_ N_ S_ S_ S_ s_ S_ S_ S_ B_ S_ S_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ SI S_ SI S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ SI S_ S_ S_ S_ S_ S_ S_ B_ B_ B_ row</p>
 <p>row N_ S_ S_ S_ S_ S_ S_ S_ S_ B_ S_ S_ S_ S_ B_ S_ S_ S_ S_ SD row</p>
 */

public final class GetConstans {

	/**
	 * Sciezka do pliku konfiguracyjnego
	 */
	public static final String Config = "Config\\config.xml"; //poprawiłem ścieszke

	/**
	 * Wysokosc ramki
	 **/

	public static int Boardheight;

	/**
	 * Szerokosc ramki
	 **/

	public static int Boardwidth;

	/**
	 * Wysokość okna głównego - menu
	 */

	public static int MainFrameheight;

	/**
	 * Szerokość okna głównego - menu
	 */

	public static int MainFramewidth;

	/**
	 * Rozmiar okna Highscores, ramka jest kwadratowa
	 */

	public static int HighscoresFrameSize;

	/**
	 * Zmienna zawierajaca ilosc sekund, po ktorym wybuchnie bomba
	 */

	public static int TimeToExplosion;

	/**
	 * Ilosc punktow gracza na poczatku, zawsze 0
	 */

	public static int Amountofpoints;

	/**
	 * Punkty za zniszczenie skrzynek
	 */

	public static int PointsForCreate;

	/**
	 * Punkty za zabicie potwora
	 */

	public static int PointsForMonster;

	/**
	 * Punkty za podniesienie przedmiotu
	 */

	public static int PointsForItem;

	/**
	 * Punkty za zdobycie skrzynki zlota
	 */

	public static int PointsForChestOfGold;

	/**
	 * Punkty za zdobycie klucza
	 */

	public static int PointsForKey;

	/**
	 * Punkty za przejscie poziomu
	 */

	public static int PointsForLevel;

	/**
	 * Punkty bonusowe za kazda sekunde pozostala do uplyniecia ustalonego progu czasowego
	 */

	public static int PointsForSecond;

	/**
	 * Ilosc kolumn
	 **/

	public static int Amountofcolumns;

	/**
	 *Ilosc wierszy
	 **/

	public static int Amountoflines;

	/**
	 * Szybkosc potworow
	 **/

	public static int Monsterspeed;

	/**
	 * Ilosc zyc na poczatku rozgrywki
	 **/

	public static int Amountoflifes;


	/**
 	* Ilosc zwykłych bomb
 	*/

	public static int Amountofordinarybombs;

	/**
	* Ilosc bomb zdalnych
	 */

	public static int Amountofremotebombs;

	/**
	 * Ilosc skrzydel husarskich
	 */

	public static int Amountofhusarswings;

	/**
	 * Ilosc lasrów
	 */

	public static int Amountoflasers;

	/**
	 * Ilosc bomb zdalnych
	 */

	public static int Amountofkeys;

	/**
	 * Czas konkretnego poziomu
	 */

	public static int LevelTime;

	/**
	 * Pomocnicza zmienna, okreslajaca wysokosc panelboard
	 */

	public static int panelboardheight;

	/**
	 * Pomocnicza zmienna, okeslajaca szerokosc panelboard
	 */

	public static int panelboardwidth;


	/**
	 * Pomocnicza zmienna, okreslajaca wysokosc gornego panelu
	 */

	public static int panelinfooneheight;

	/**
	 * Pomocnicza zmienna, okreslajaca wysokosc bocznego panelu
	 */

	public static int panelinfotwoheight;

	/**
	 * Pomocnicza zmienna, okreslajaca szerokosc gornego panelu
	 */

	public static int panelinfotwowidth;

	/**
	 * String przechowujcy informacje o wszystkich polach danego poziomu.
	 * W kolejnych elementach tablicy zawarte sa kolejne linie planszy
	 */

	public static String row[];
	/**
	 * Sciezka do grafiki Polandball
	 */
	public static String PolandBallString;
	/**
	 * Sciezka do grafiki TurkeyBall
	 */
	public static String TurkeyBallString;
	/**
	 * Sciezka do grafiki NaziBall
	 */
	public static String NaziBallString;
	/**
	 * Sciezka do grafiki SovietBall
	 */
	public static String SovietBallString;
	/**
	 * Sciezka do grafiki HussarBall_left
	 */
	public static String HussarBall_leftString;
	/**
	 * Sciezka do grafiki HussarBall_right
	 */
	public static String HussarBall_rightString;
	/**
	 * Sciezka do grafiki Skrzynka
	 */
	public static String SkrzynkaString;
	/**
	 * Sciezka do grafiki Beton
	 */
	public static String BetonString;
	/**
	 * Sciezka do grafiki Key
	 */
	public static String KeyString;
	/**
	 * Sciezka do grafiki WingsOfHussar
	 */
	public static String WingsOfHussarString;
	/**
	 * Sciezka do grafiki BoxOfBomb
	 */
	public static String BoxOfBombsString;
	/**
	 * Sciezka do grafiki BoxOfBomb
	 */
	public static String GunLaserString;

	/**
	 * Sciezka do grafiki Heart
	 */
	public static String HeartString;

	/**
	 * Sciezka do grafiki skrzynki ze złotem
	 */
	public static String ChestOfGoldString;

	/**
	 * Sciezka do gifa Normal_Bomb
	 */
	public static String Normal_BombString;
	/**
	 * Scieszka do grafiki Remote_Bomb
	 */
	public static String Remote_BombString;
	/**
	 * Sciezka do grafiki Door
	 */
	public static String DoorString;
	/**
	 * Scieszka do gif-a z grafiką
	 */
	public static String ExplosionString;
	/**
	 * Sciezka do grafiki tła menu wejsciowego
	 */
	public static String BackgroundString;

	/**
	 * Sciezka do grafiki tla okna wynoru trybu rozgrywki
	 */

	public static String BackgroundForSetConnectionString;

	/**
	 * Dwuwymaiora tablica, reprezentujaca wystepowanie przeszkody
	 */

	public static String StatioonaryObjectTab[][];

	/**
	 * Zmienna informujaca, ktory poziom zostal zaladowany
	 */

	public static int WhiChLevel;

	/**
	 * Zmienna informujaca o tym, ze przeszlismy ostatni poziom
	 */

	public static boolean LastLevel=false;
	/**
	 * Konstruktor klasy GetConstans, wczytywanie danych startowych z plikow
	 **/

	public GetConstans(){
		try {
			read_on_config();
			read_path_to_graphics();
		}catch(NullPointerException e){
			System.out.println("Blad w parsowaniu danych");
		}
	}

	/**
	 * Metoda wczytyujaca parametry z pliku configuracyjnego
	 */

	public static void read_on_config(){
		try {
			File file = new File(Config);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			//podstawowe parametry aplikacji
			Boardheight = Integer.parseInt(doc.getElementsByTagName("Boardheight").item(0).getTextContent());
			Boardwidth = Integer.parseInt(doc.getElementsByTagName("Boardwidth").item(0).getTextContent());
			MainFrameheight=Integer.parseInt(doc.getElementsByTagName("MainFrameheight").item(0).getTextContent());
			MainFramewidth=Integer.parseInt(doc.getElementsByTagName("MainFramewidth").item(0).getTextContent());
			HighscoresFrameSize=Integer.parseInt(doc.getElementsByTagName("HighscoresFrameSize").item(0).getTextContent());
			TimeToExplosion = Integer.parseInt(doc.getElementsByTagName("TimeToExplosion").item(0).getTextContent());
			//ilosc poczatkowych punktow, zawsze 0
			Amountofpoints=0;
			//punktacja gry
			PointsForCreate = Integer.parseInt(doc.getElementsByTagName("PointsForCreate").item(0).getTextContent());
			PointsForMonster = Integer.parseInt(doc.getElementsByTagName("PointsForMonster").item(0).getTextContent());
			PointsForItem = Integer.parseInt(doc.getElementsByTagName("PointsForItem").item(0).getTextContent());
			PointsForChestOfGold = Integer.parseInt(doc.getElementsByTagName("PointsForChestOfGold").item(0).getTextContent());
			PointsForKey = Integer.parseInt(doc.getElementsByTagName("PointsForKey").item(0).getTextContent());
			PointsForLevel = Integer.parseInt(doc.getElementsByTagName("PointsForLevel").item(0).getTextContent());
			PointsForSecond = Integer.parseInt(doc.getElementsByTagName("PointsForSecond").item(0).getTextContent());

		}
		catch(FileNotFoundException e){
			e.printStackTrace();

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Metoda odpowiedzialna za wczytywanie poziomu
	 * @param level parametr decydujacy ktory level zostanie wczytany
	 */

	public static void read_on_level(int level){
		try {
			LastLevel=false;//zakladamy zawsze ze sa jeszcze jakies poziomy
			WhiChLevel = level;// zmeinna globalne, mowiaca ktory aktualny level jest zaladowany
			String path_to_level=create_path_to_level(level);
			File file = new File(path_to_level);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();


			Amountofcolumns = Integer.parseInt(doc.getElementsByTagName("Amountofcolumns").item(0).getTextContent());
			Amountoflines = Integer.parseInt(doc.getElementsByTagName("Amountoflines").item(0).getTextContent());
			Monsterspeed = Integer.parseInt(doc.getElementsByTagName("Monsterspeed").item(0).getTextContent());
			Amountoflifes = Integer.parseInt(doc.getElementsByTagName("Amountoflifes").item(0).getTextContent());
			Amountofordinarybombs = Integer.parseInt(doc.getElementsByTagName("Amountofordinarybombs").item(0).getTextContent());
			Amountofremotebombs = Integer.parseInt(doc.getElementsByTagName("Amountofremotebombs").item(0).getTextContent());
			Amountofhusarswings = Integer.parseInt(doc.getElementsByTagName("Amountofhusarswings").item(0).getTextContent());
			Amountoflasers = Integer.parseInt(doc.getElementsByTagName("Amountoflasers").item(0).getTextContent());
			Amountofkeys = Integer.parseInt(doc.getElementsByTagName("Amountofkeys").item(0).getTextContent());
			LevelTime = Integer.parseInt(doc.getElementsByTagName("LevelTime").item(0).getTextContent());

			row = new String[Amountoflines];
			for (int i = 0; i < Amountoflines; i++) {
				row[i] = doc.getElementsByTagName("row").item(i).getTextContent();
				//System.out.println(i+"  "+Amountoflines+row[i]);
			}
			//wywolanie metody tworzacej statyczna tablicy do wykrywania kolzji
			//MakeBoardObstacleTable();
		}
		catch(FileNotFoundException e){
			//e.printStackTrace();
			System.out.println("Nie znaleziono następnego poziomu\n");
			PauseActive=true;//zatrzymanie gry
			GameOver gameover = new GameOver();//wywolanie okna końca gry
			gameover.setVisible(true);
			LastLevel=true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Metoda wczytujaca sciezki do grafik gry
	 */

	public static void read_path_to_graphics() {
		try {
			File file = new File("Config\\PathToGraphics.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			//LivingObject
			PolandBallString = doc.getElementsByTagName("PolandBall").item(0).getTextContent();
			TurkeyBallString = doc.getElementsByTagName("TurkeyBall").item(0).getTextContent();
			SovietBallString = doc.getElementsByTagName("SovietBall").item(0).getTextContent();
			NaziBallString = doc.getElementsByTagName("NaziBall").item(0).getTextContent();
			HussarBall_leftString = doc.getElementsByTagName("HussarBall_Left").item(0).getTextContent();
			HussarBall_rightString = doc.getElementsByTagName("HussarBall_Right").item(0).getTextContent();
			//Tereny
			SkrzynkaString = doc.getElementsByTagName("Skrzynka").item(0).getTextContent();
			BetonString = doc.getElementsByTagName("Beton").item(0).getTextContent();
			//Itemy
			WingsOfHussarString = doc.getElementsByTagName("WingsOfHussars").item(0).getTextContent();
			ChestOfGoldString = doc.getElementsByTagName("ChestOfGold").item(0).getTextContent();
			BoxOfBombsString = doc.getElementsByTagName("BoxOfBombs").item(0).getTextContent();
			GunLaserString = doc.getElementsByTagName("GunLaser").item(0).getTextContent();
			HeartString = doc.getElementsByTagName("Heart").item(0).getTextContent();
			DoorString = doc.getElementsByTagName("Door").item(0).getTextContent();
			KeyString = doc.getElementsByTagName("Key").item(0).getTextContent();
			//Bomby
			Normal_BombString = doc.getElementsByTagName("Normal_bomb").item(0).getTextContent();
			Remote_BombString= doc.getElementsByTagName("Remote_bomb").item(0).getTextContent();
			//Inne
			ExplosionString = doc.getElementsByTagName("Explosion").item(0).getTextContent();
			BackgroundString = doc.getElementsByTagName("Background").item(0).getTextContent();
			BackgroundForSetConnectionString = doc.getElementsByTagName("BackgroundForSetconnection").item(0).getTextContent();

		} catch (FileNotFoundException e) {
			System.out.println("Blad wczytania grafik");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Blad wczytania grafik");
			e.printStackTrace();
		}
	}

	/**
	 * Metoda tworzaca sciezke do poziomu
	 * @param level - numer poziomu uzyty w tworzonej sciezki
	 * @return path - sciezka do konkretnego levela
	 */

	static String create_path_to_level(int level){
		String first_part="Level_Folder\\Level";
		String second_part=Integer.toString(level);
		String third_part=".xml";

		String path=first_part+second_part+third_part;
		return path;
	}

	/**
	 * Metoda tworzaca statyczna tablice do tworzenia kolizji
	 */
	public static void MakeBoardObstacleTable(){
		try {
			//tablica potrzebna do kolizji
			StatioonaryObjectTab = new String[Amountoflines][Amountofcolumns];
			for (int k = 0; k < Amountoflines; k++) {
				String bufor[] = row[k].split(" ");
				for (int j = 0; j < Amountofcolumns; j++) {

					//tworzy 1 tam gdzie jest skrzynka/beton/skrzynka z drzwiami//skrzynka z kluczem// skrzynka z  innymi itemem
					if (bufor[j].equals("S_") ) {
						StatioonaryObjectTab[k][j] = "S_";
					} else if ( bufor[j].equals("B_")) {
						StatioonaryObjectTab[k][j] = "B_";
					} else if (bufor[j].equals("SD") ) {
						StatioonaryObjectTab[k][j] = "SD";
					} else if (bufor[j].equals("SK")) {
						StatioonaryObjectTab[k][j] = "SK";
					} else if (bufor[j].equals("SI")) {
						StatioonaryObjectTab[k][j] = "SI";
					} else {
						StatioonaryObjectTab[k][j] = "N_";
					}
				}
			}
			//DisplayStationaryObject();
		}
		catch(NullPointerException e) {
			System.out.println("W metodzie MakeBoardObstaclesTable wystapil blad typu NullPointerException");
		}
	}

	/**
	 * Metoda wyswietlajaca stan tablicy Stacjonarnych obiektów
	 */
	public static void DisplayStationaryObject(){
		for(int i=0;i<Amountoflines;i++){
			for(int j=0;j<Amountofcolumns;j++){
				System.out.print(StatioonaryObjectTab[i][j]+" ");
			}
			System.out.println();
		}

	}
}
