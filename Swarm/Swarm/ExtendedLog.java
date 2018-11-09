package Swarm;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Adrian Szymowiat on 16.08.2018
 * Klasa do zapisywania logow z pliku
 */
public class ExtendedLog {
    /**
     * Metoda zwracajaca aktulana date i czas
     * @return - aktualna data i czas
     */
    public static String getTimeAndDate(){
        String timeAndDate =  DateFormat.getDateTimeInstance().format(new Date());
        String formatedTimeAndDate;
        formatedTimeAndDate = "[" + timeAndDate.replaceAll(",","") + "]:";
        return formatedTimeAndDate;
    }
    public static String getLog(String log){
        return getTimeAndDate() + " " + log;
    }
}
