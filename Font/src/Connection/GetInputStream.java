package Connection;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import static Connection.ConnectSocket.socket_;
import static Action.ConsoleFrame.*;
import static Action.ActionFrame.getsnnumbersflag_;
import static Action.ActionFrame.devicename_;
import static MainFrame.MainFrame.connectserial_;
import static MainFrame.MainFrame.connectsocket_;
import static MainFrame.MainFrame.socketmode_;
import static MainFrame.MainFrame.serialmode_;

/**
 * Klasa odpowiedzialna za odbior i wyswietlanie strumienia wejsciowego dla gniazda sieciowego oraz zawierajaca metody
 * obrobki danych zarowno dla polaczenia przez gniazdo sieciowe jak i serialowe
 * Created by Adrian Szymowiat on 19.07.2017.
 */
public class GetInputStream implements Runnable{
    /**
     * Zmienna typu BufferedReader, bufor do odczytu/obrobki danych
     * Wykorzystywany w innych klasach
     */
    public static BufferedReader bufferedReader;
    /**
     * Bufor na przychodzace od serwera wiadomosci
     */
    public static ArrayList<String> answerbufor_ = new ArrayList<>();
    /**
     * Licznik potrzebny do ignorowania kilku pierwszych wierszy danych
     */
    public static int counter_ = 0;
    /**
     * Ilosc krokow licznika, po ktorej nastepuje zaprzestanie blokowania sprawdzania jakiegos warunku metody
     */
    public static int counterstep_ = 5;
    /**
     * Flaga informujaca, ze aplikacja jest w trakcie pobierania numerow seryjnych
     */
    public static boolean getsncommandrunning_ = false;
    /**
     * Cialo watku
     * Przyjmowanie danych i wyswietlanie ich na konsoli
     * Wywolywanie metod obrabiajacych dane
     */
    @Override
    public void run(){
        try{
            if(socket_!=null) {//sprawdzenie, czy zostalo utworzone jakies gniazdo sieciowe
                String messagefromserver;//zmienna przechowujaca linijke wiadomosci
                InputStream inputStream = socket_.getInputStream();//pobranie odpowiedzi serwera, strumien wejsciowy danych
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//zaladowanie danych wejsciowych do obiektu klasy bufferedreader
                while (true) {//nieskonczona petla, caly czas odbierane sa dane ze strumienia wejsciowego
                    while ((messagefromserver = bufferedReader.readLine()) != null) {//odczytanie kolejnych linijek wiadomosci
                        textarea_.setCaretPosition(textarea_.getDocument().getLength());//za kazdym razem jak przychodza dane ustawiamy nowa pozycje caret, zeby autoscroll dzialal
                        textarea_.append(getTimeandDate() + ": " + messagefromserver + newlinechar_);//wyswietlenie wiadomosci na konsole programu
                        if(getsnnumbersflag_ == true){//sprawdzenie czy wcisnieto przycisk GetSN
                            getsncommandrunning_ = true;//ustawienie flagi na true, nie mozna zapisywac numerow seryjnych
                            getSN(messagefromserver);//wywolanie nizej zdefiniowanej metody
                        }
                    }
                }
            }
        }catch(IOException e){//wychwycenie wyjatku
            System.out.println("Error GETINPUTSTREAM method " + e);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error GETINPUTSTREAM method " + e + newlinechar_ );//komunikat bledu na konsole programu
        }catch(Exception err){
            System.out.println("Error GETINPUTSTREAM method " + err);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error GETINPUTSTREAM method " + err + newlinechar_ );//komunikat bledu na konsole programu
        }
    }

    /**
     * Metoda zapisujaca do bufora otrzymane dane z komendy "display elabel"
     * @param message - wiersz danych otrzymancyh na wejscie
     */
    public static void getSN(String message) {
        try {
            counter_++;//iteracja licznika, zwiekszamy w celu poczatkowego ignorowania sprawdzania ponizszego warunku
            answerbufor_.add(message);//dodanie kolejnego wiersza wiadomosci do bufora
            if (message.trim().equals(devicename_.trim()) == true && counter_ > counterstep_) {//sprawdzamy czy po kolejnej spacji jako wiadomsoc od urzadzenia
                // nie otrzymujemy juz pustych wiadomosci (nazwy rzadzenia) oraz czy licznik do liczyl do okreslonego momentu
                getsnnumbersflag_ = false;//ustawienie flagi na false, nie wracamy juz do tej metody
                counter_ = 0;//zerowanie licznika
                getsncommandrunning_ = false;//mozna pobrac numery seryjne
               /* for(int i = 0;i<answerbufor_.size();i++){
                    System.out.println(answerbufor_.get(i));
                }*/
                JOptionPane.showMessageDialog(null, "Pomyslnie odczytano dane.");//wiadomosc o pomyslnym odczytanei danych
                textarea_.append(getTimeandDate() + ": " + "Pomyslnie odczytano dane." + newlinechar_ );//komunikat na konsole programu
            }
            if(socketmode_ == true) {//przypadek, gdy polaczenie sieciowe
                connectsocket_.makeOutputStream(" ");//wysylamy spacje, zeby otrzymac kolejne informacje
            }
            if(serialmode_ == true){//przypadek, gdy polaczenie serialowe
                connectserial_.makeSerialOutputStream(" ");//wysylamy spacje, zeby otrzymac kolejne informacje
            }
        }catch(Exception e) {//wychwycenie wyjatku
            System.out.println("Error GETSERIALNUMBERS method " + e);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error GETSERIALNUMBERS method " + e + newlinechar_);//komunikat bledu na konsole programu
        }
    }
}
