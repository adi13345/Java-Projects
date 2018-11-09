package Socket;

import java.io.*;

import static Controllers.WriteLoginAndPasswordController.connectSocket;

public class GetInputStream implements Runnable {
    /**
     * Zmienna typu BufferedReader, bufor do odczytu/obrobki danych
     * Wykorzystywany w innych klasach
     */
    public static BufferedReader bufferedReader;

    public static String[] tableAnswear=new String[2];

    public static String messagefromserver;//zmienna przechowujaca linijke wiadomosci
    /**
     *
     */
    private boolean threadFlag=false;

    /**
     * Cialo watku
     * Przyjmowanie danych i wyswietlanie ich na konsoli
     * Wywolywanie metod obrabiajacych dane
     */
    @Override
    public void run() {
        threadFlag=false;
        while (threadFlag) {//nieskonczona petla, caly czas odbierane sa dane ze strumienia wejsciowego
            try {
                if (connectSocket != null) {//sprawdzenie, czy zostalo utworzone jakies gniazdo sieciowe
                    InputStream inputStream = connectSocket.getSocket().getInputStream();//pobranie odpowiedzi serwera, strumien wejsciowy danych
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//zaladowanie danych wejsciowych do obiektu klasy bufferedreader
                    while ((messagefromserver = bufferedReader.readLine()) != null) {//odczytanie kolejnych linijek wiadomosci
                        System.out.println(messagefromserver);
                    }
                }
            } catch (IOException e) {//wychwycenie wyjatku

            } catch (Exception err) {

            }
        }
    }

    /**
     * Metoda wyciagajca kod informacji z odpowiedzi serwera
     * @param messagefromserver
     * @return
     */
    public static String[] getAnswear(String messagefromserver){
        System.out.println(messagefromserver);
        String[] bufor=messagefromserver.split(":");
        String secondBufor =bufor[1].substring(1,4);
        String[] thirdBufor=messagefromserver.split(">");
        secondBufor.trim();
        thirdBufor[1].trim();
       // System.out.println(secondBufor);
      //  System.out.println(thirdBufor[1]);
        tableAnswear[0]=secondBufor;
        tableAnswear[1]=thirdBufor[1];
        return tableAnswear;
    }

}