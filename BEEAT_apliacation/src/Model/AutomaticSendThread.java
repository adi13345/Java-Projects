package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static Model.Main.*;
import static Model.ParseConfigFile.checkTimePeriodicity;

/**
 * Created by Adrian Szymowiat on 19.05.2018.
 * Klasa odpowiedzialna za automatyczne wysłanie wiadomosci
 */
public class AutomaticSendThread extends Thread {
    /**
     * Flaga informujaca, czy dany watek jest uruchomiony
     */
    public boolean automaticSendThreadRunFlag;
    /**
     * Opcjonalna flaga dla watkow, ktore maja chodzic caly czas
     */
    public boolean infinityRunFlag;
    /**
     * Flaga sluzaca do zakonczenia watku w momencie gyd watek jest w sekcji sprawdzanai czasu wyslanai automatycznej wiadomosci
     */
    public boolean checkTimeRunFlag;
    /**
     * Bufor z numerami odbiorcow
     */
    public ArrayList<String> buforOfNumbers;
    /**
     * Bufor zawierajacy wiadomosc do odbiorcow
     */
    public String message;
    /**
     * Data wyslania automatycznej wiadomosci
     */
    public LocalDate nextDate;
    /**
     * Czas wyslania automatycznej wiadomosci
     */
    public LocalTime nextTime;
    /**
     * Zmienna przechowujaca okresowosc w minutach
     */
    public int periodicityInMinutesFormat;
    /**
     * Cialo watku
     * Metoda sterujaca wysylaniem automatycznych wiadomosci
     */
    @Override
    public void run(){
        automaticSendThreadRunFlag = true;
        System.out.println("Start wątku AutomaticSend - > Data wysłania pierwszej wiadomości: "+nextDate +", "+nextTime);
        if(infinityRunFlag){//jesli flaga ustawiona na true
            while(infinityRunFlag){//wątek chodziło czasy czas
                try {
                    while (!correctnessGuard.checkTimeToSend(nextDate, nextTime) && checkTimeRunFlag) {//sprawdzania czy nie nastpil juz czas wyslanai wiaodmosci
                        Thread.sleep(checkTimePeriodicity);//usypianie watku na x ms, zalecana 1 sekunda
                    }
                    if (checkTimeRunFlag) {
                        System.out.println("Start automatycznej okresowej wiadomości - >"+nextDate +", "+nextTime);//info o rozpoczeciu dzialania
                        String date_bufor = nextDate.toString()+" "+nextTime.truncatedTo(ChronoUnit.MINUTES).toString();//pobranie napisu daty z dokladnoscia do minut
                        LocalDateTime actualDate = LocalDateTime.now();//pobranei aktualna czasu, ktory jest rowny ustawionej dacie
                        actualDate = actualDate.plusMinutes(periodicityInMinutesFormat);//dodanei czasu (minut) do wczesniejszej daty - ustawienie nowej
                        nextDate = actualDate.toLocalDate();//podstawienie nowej daty
                        nextTime = actualDate.toLocalTime();//podstawienie nowego czasu
                        while(!ticket){
                            Thread.sleep(correctnessGuard.randomDelay());//losowy czas oczekwiania na kolejne sprawdzenie, czy mozna już wysyłać
                        }
                        SimpleSendThread simpleSendThread = new SimpleSendThread();//stowrzenei watku jednorazowej wiadomosci
                        simpleSendThread.date = date_bufor;//podstawienie daty do zmiennej watku jednorazowej wiadomosci
                        simpleSendThread.isThisThreadAuto = true;//informacja, ze watek jets tworzony w ramach watku automatycznego powiadamaiania
                        String[] bufor = new String[this.buforOfNumbers.size()];//stowrzenie pomocniczego bufora w cleu konwersji
                        bufor = this.buforOfNumbers.toArray(bufor);//przekonwertowania araylista na bufor typu String[]
                        simpleSendThread.buforOfNumbers = bufor;//zaladowanei nuemrow do bufora zmiennej watku jednorazowej widomosci
                        simpleSendThread.message = message + "\u001A";//zaladowanie wiadomosci do bufora + znak zakonćzenie wiadomosci
                        simpleSendThread.start();//urchomienie wątku pojedynczje wiadomosc
                        System.out.println("Koniec automatycznej okresowej wiadomości - >"+nextDate +", "+nextTime);//info o zakonczeniu (pojedynczego) dzialania (niecalkowicie)
                    }else{
                        infinityRunFlag = false;//wyłaczenie watku (dzialania w petli nieskonczonej)
                        automaticSendThreadRunFlag = false;//informacja, ze watek zakończyl swoje dzialanie, profilaktycznie
                        //System.out.println("Koniec automatycznej okresowej wiadomości - >"+nextDate +", "+nextTime);
                    }
                }catch(Exception er){
                    infinityRunFlag = false;//wyłaczenie watku (dzialania w petli nieskonczonej)
                    automaticSendThreadRunFlag = false;//informacja, ze watek zakończyl swoje dzialanie, profilaktycznie
                    System.out.println("Błąd wątku AutomaticSendThread (okresowy): "+er);
                    System.out.println("Koniec automatycznej okresowej wiadomości - >"+nextDate +", "+nextTime +"spowodowany błędem!");
                }
            }
        }else{//jesli brak okresowości
            try {
                while (!correctnessGuard.checkTimeToSend(nextDate, nextTime) && checkTimeRunFlag) {//sprawdzania czy nie nastpil juz czas wyslanai wiaodmosci
                    Thread.sleep(checkTimePeriodicity);//usypianie watku na x ms, zalecana 1 sekunda
                }
                if(checkTimeRunFlag) {
                    System.out.println("Start automatycznej jednorazowej wiadomości - >"+nextDate +", "+nextTime);//info o rozpoczeciu dzialania
                    while(!ticket){
                        Thread.sleep(correctnessGuard.randomDelay());//losowy czas oczekwiania na kolejne sprawdzenie, czy mozna już wysyłać
                    }
                    String date_bufor = nextDate.toString()+" "+nextTime.truncatedTo(ChronoUnit.MINUTES).toString();//pobranie napisu daty z dokladnoscia do minut
                    SimpleSendThread simpleSendThread = new SimpleSendThread();//stowrzenei watku jednorazowej wiadomosci
                    simpleSendThread.date = date_bufor;//podstawienie daty do zmiennej watku jednorazowej wiadomosci
                    simpleSendThread.isThisThreadAuto = true;//informacja, ze watek jets tworzony w ramach watku automatycznego powiadamaiania
                    String[] bufor = new String[this.buforOfNumbers.size()];//stowrzenie pomocniczego bufora w cleu konwersji
                    bufor = this.buforOfNumbers.toArray(bufor);//przekonwertowania araylista na bufor typu String[]
                    simpleSendThread.buforOfNumbers = bufor;//zaladowanei nuemrow do bufora zmiennej watku jednorazowej widomosci
                    simpleSendThread.message = message + "\u001A";//zaladowanie wiadomosci do bufora + znak zakonćzenie wiadomosci
                    simpleSendThread.start();//urchomienie wątku pojedynczje wiadomosc
                    System.out.println("Koniec automatycznej jednorazowej wiadomości - >"+nextDate +", "+nextTime);//infor o zakonczeniu dzialania (calkowicie)
                }else{
                    automaticSendThreadRunFlag = false;//informacja, ze watek zakończyl swoje dzialanie, profilaktycznie
                    //System.out.println("Koniec automatycznej jednorazowej wiadomości - >"+nextDate +", "+nextTime);
                }
            }catch(Exception er){
                automaticSendThreadRunFlag = false;
                System.out.println("Błąd wątku AutomaticSendThread (jednorazowy): "+er);
                System.out.println("Koniec automatycznej jednorazowej wiadomości - >"+nextDate +", "+nextTime+"spowodowany błędem!");
            }

        }
        System.out.println("Koniec wątku AutomaticSend - > Data wysłania pierwszej wiadomości: "+nextDate +", "+nextTime);
        automaticSendThreadRunFlag = false;
    }

}
