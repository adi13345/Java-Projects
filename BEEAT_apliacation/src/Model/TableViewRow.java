package Model;

/**
 * Klasa reprezentujaca wiersz tabeli
 */
public class TableViewRow {
    /**
     * Nazwa danego wiersza
     */
    public String name;
    /**
     * Pole zawierajace czas wykonania watku automatycznej wiadomosci
     */
    public String eventTime;
    /**
     * Pole zawierajace okresowosc watku automatycznej wiiadomosci
     */
    public String periodicity;
    /**
     * Referencja na watek automatycznego powiadamia trzymany w TableView
     */
    public AutomaticSendThread automaticSendThread;
    /**
     * Konstruktor klasy, tworzenie obiektu wiersza tabeli zawierajacego odpowiednei atrybutu watku
     * @param name - nazwa wierza odzwierciedlajacy nazwe pliku konfiguracyjnegp
     * @param etime - najblizszy czas wykonania watku automatycznej wiadomosci
     * @param period - okresowosc watku automatycznej wiadomosci
     */
    public TableViewRow(String name, String etime, String period){
        this.name = name;//podstawienie
        this.eventTime = etime;//podstawienie
        this.periodicity = period;//podstawienies
    }
    //UWAGA bez tych (CHYBA) metod nie dziala wstawienie wierszy do tablicy, chgw dlaczego
    /**
     * Metoda zwracajaca nazwe wiersza
     * @return  - nazwa wiersza
     */
    public String getName() { return name; }
    /**
     * Metoda zwracajaca czas wykonania eventu
     * @return - czas wykonania eventu
     */
    public String getEventTime() { return eventTime; }
    /**
     * Metoda zwracajaca wartosc okresowosci (minutes)
     * @return - wartosc okresowosci (minutes)
     */
    public String getPeriodicity() { return periodicity; }
    /**
     * Metoda zwracajaca referencje na obiekt watku automatycznej wiadomosci
     * @return - referencja na obiekt watku automtycznej wiadomosci
     */
    public AutomaticSendThread getAutomaticSendThread() { return automaticSendThread; }
}
