package Socket;

import java.util.Calendar;
import java.util.Date;

public class CheckTime implements Runnable {
    /**
     * Czas odliczania
     */
    private static int sleeptime = 60*1000;
    /**
     * Aktualny czas
     * Godzina:minuty - ]- 15:34
     */
    private static String actual_time;
    /**
     * Metoda zwracajaca aktualny czas
     * @return zwraca aktualny czas
     */
    public static String getTime(){
        return actual_time;
    }
    /**
     * Flaga informujaca, czy watek ma dzialac
     */
    public static boolean checktime_flag = true;
    /**
     * Ciało wątku
     * odliczanie czasu co 1 sekunde i  podstawianie za zmienna actual_time aktualny czas
     */
    @Override
    public void run() {
        while (checktime_flag){
            try {
                Date calender = Calendar.getInstance().getTime();//pobranie aktualnej daty i czasu
                String date = calender.toString();//konwersja daty i czasu na String
                //System.out.println(date); ----> Fri Nov 24 15:46:42 CET 2017
                String[] bufor = date.split(" ");//dobranei sie do 15:46:42
                String[] bufor_two = bufor[3].split(":");//dobranei sie do 15 45 42, chcemy wyrzucic 42
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(bufor_two[0]+":"+bufor_two[1]);//15:45
                String time = stringBuilder.toString().trim();//otrzymanie napisu
                actual_time = time;//podstawienie aktulanej daty do zmiennej
                Thread.sleep(sleeptime);//odliczanie co 60 sekund
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
