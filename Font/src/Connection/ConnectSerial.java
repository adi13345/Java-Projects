package Connection;

import javax.swing.*;
import jssc.*;

import static Action.ActionFrame.getsnnumbersflag_;
import static Action.ConsoleFrame.getTimeandDate;
import static Action.ConsoleFrame.newlinechar_;
import static Action.ConsoleFrame.textarea_;
import static Action.ActionFrame.devicename_;
import static MainFrame.MainFrame.actionframe_;
import static MainFrame.MainFrame.consoleframe_;

/**
 * Klasa sluzaca do laczenia sie przez kabel serialowy z urzadzeniem
 * Created by Adrian Szymowiat on 20.07.2017.
 */
public class ConnectSerial implements SerialPortEventListener{
    /**
     * Obiekt klasy SerialPort
     */
    public static SerialPort serialport_;
    /**
     * Nazwa portu np. COM3.
     */
    private static String comportname_;
    /**
     * Tablica przechowujaca dostepne porty COM
     */
    public static String[] comportstable_;
    /**
     * Flaga informujaca, czy otwieranie polaczenia serialowego przebieglo pomyslnie
     */
    public static boolean isportcomok_;
    /**
     * Bufor na na znaki otrzymywanej wiadomosci
     */
    public static StringBuilder bufor_ = new StringBuilder();
    /**
     * Flaga informujaca, ze trzeba zapisac otrzymywane dane - nazwe urzadzenia
     */
    public static boolean needtosavedevicename_ = false;
    /**
     * Metoda zwracajaca(wyswietlajaca) informacje o dostepnych portach COM
     */
    public static void showCommPort() {
        comportstable_ = SerialPortList.getPortNames();//pobranie dostepnych portow COM
        StringBuilder stringBuilder = new StringBuilder();//stowrzenie obiektu klasy StringBuilder to zbudowania napisu
        for (int i = 0; i < comportstable_.length; i++) {//tworzenie napisu
            stringBuilder.append(comportstable_[i] + "\n");//dodanie kolejnych nazw portów COM do napisu
        }
        if (comportstable_.length != 0) {//sprawdzenie czy sa jakies port COM
            JOptionPane.showMessageDialog(null, "Dostepne porty COM:\n" + stringBuilder);//okno informujace o dostepnych portach COM
        } else {
            JOptionPane.showMessageDialog(null, "Brak dostępnych portów COM");//okno informujace o dostepnych portach COM
        }
    }
    /**
     * Metoda otwierajaca i konfigurujaca port
     */
    public void openSerialPort(String comport) {
        comportname_ = comport;//podstawienie nazwy portu COM
        try {
            serialport_ = new SerialPort(comportname_);//stworzenie obiektu polaczenia serialowego o danej nazwie COM
            serialport_.openPort();//otwarcie polaczenia serialowego
            serialport_.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);//ustawienie parametrow
            serialport_.addEventListener(this, SerialPort.MASK_RXCHAR);//dodanie listenera
            isportcomok_ = true;//ustawieniefalgi na true, pomyslne otwarcie polaczenia serialowego
        } catch (SerialPortException err) {
            JOptionPane.showMessageDialog(null, "Nieprawidlowy nazwa portu lub port zajęty!");//komunikat o bledzie
            System.out.println("Error OPENSERIALPORT method: " + err);//komunikat na konsole
            isportcomok_ = false;//flaga na flase, informacja o bledzie podczas otwierania polaczenia serialowego
        }catch (NullPointerException er) {
            JOptionPane.showMessageDialog(null, "Nie można nawiązać połączenia");//komunikat o bledzie
            System.out.println("Error OPENSERIALPORT method: " + er);//komunikat na konsole
            isportcomok_ = false;//flaga na flase, informacja o bledzie podczas otwierania polaczenia serialowego
        } catch (Exception e) {
            System.out.println("Error OPENSERIALPORT method: " + e);//komunikat na konsole
            isportcomok_ = false;//flaga na flase, informacja o bledzie podczas otwierania polaczenia serialowego
        }
    }

    /**
     * Metoda nasłuchujaca na dane na wskazanym porcie i wyświetlajaca je w konsoli programu
     */
    public void serialEvent(SerialPortEvent Event) {
        if (Event.isRXCHAR() && Event.getEventValue() > 0) {//...sprawdzenie czy otrzymano dane
            try {
                String receivedData = serialport_.readString(Event.getEventValue());//odczytanie pojedynczego znaku
                if ((int) (receivedData.charAt(0)) == 10) {//sprawdzenie czy kolejny otrzyamny znak to znak konca linii - \r
                    textarea_.setCaretPosition(textarea_.getDocument().getLength());//za kazdym razem jak przychodza dane ustawiamy nowa pozycje caret, zeby autoscroll dzialal
                    textarea_.append(getTimeandDate() + ": " + bufor_ + newlinechar_);//wyswietlenie zawartosci bufora na konsoli
                    if(needtosavedevicename_ == true){//sprawdzenie czy zostaly nalezy odzczytac nazwe urzadzenia - przycisk GetSN
                        devicename_ = bufor_.toString();//przypisanie nazwy urzadzenia
                        needtosavedevicename_ = false;//nie trzeba juz odczytywac nazwy urzadzenia
                    }
                    if(getsnnumbersflag_ == true){//sprawdzenie czy nalezy pobrac numery seryjne
                        GetInputStream.getSN(bufor_.toString());//dodanie informacji do bufora
                    }
                    bufor_.setLength(0);//wyczyszczenie buforu
                } else {
                    bufor_.append(receivedData);//dodanie kolejnego znaku napisu do bufora wiadomosci
                }
            } catch (SerialPortException e) {
                closeSerialPort();//zamkniecie polaczenia
                if (actionframe_ != null) {
                    actionframe_.dispose();//zamkniecie okna
                }
                if (consoleframe_ != null) {
                    consoleframe_.dispose();//zamkniecie okna
                }
                JOptionPane.showMessageDialog(null, "Bład połączenia serialowego:\n" + e + "\n" + "Połączenie zostanie przerwane");//informacja o bledzie
                // textarea_.append(getTimeandDate() + ": " + "Error SERIALEVENT method " + e + newlinechar_);
            } catch (Exception e) {
                closeSerialPort();//zamkniecie polaczenia
                if (actionframe_ != null) {
                    actionframe_.dispose();//zamkniecie okna
                }
                if (consoleframe_ != null) {
                    consoleframe_.dispose();//zamkniecie okna
                }
                System.out.println("Error SERIALEVENT method: " + e);//komunikat na konsole
                JOptionPane.showMessageDialog(null, "Niespodziewany błąd:\n" + e + "\n" + "Połączenie zostanie przerwane");//informacja o bledzie
                //textarea_.append(getTimeandDate() + ": " + "Error SERIALEVENT method " + e + newlinechar_ );//komunikat na konsole aplikacji
            }
        }
    }

    /**
     * Metoda tworzaca wiadomosc do wyslania przez polaczenie serialowe
     * @param command - komenda do wyslania
     */
    public static void makeSerialOutputStream(String command){
        try {
            if(serialport_ != null) {
                serialport_.writeString(command+newlinechar_);//wyslanie wiadomosci do urzadzenia
            }
        }catch(SerialPortException er){
            System.out.println("Error MAKESERIALOUTPUTSTREAM method " + er);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error MAKESERIALOUTPUTSTREAM method " + er + newlinechar_ );//komunikat bledu na konsole programu
        } catch(Exception e){
            System.out.println("Error MAKESERIALOUTPUTSTREAM method " + e);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error MAKESERIALOUTPUTSTREAM method " + e + newlinechar_ );//komunikat bledu na konsole programu
        }
    }
    /**
     * Metoda zamykajaca port serialowy
     */
    public static void closeSerialPort() {
        try {
            if (serialport_ != null && serialport_.isOpened()) {//sprawdzenie, czy jakies polaczenie zostalo utworzone i zostalo utworzone polaczenie
                serialport_.removeEventListener();//usuniecie nasluchiwania
                serialport_.closePort();//zamkniecie portu
            }
        }catch(Exception e){
            System.out.println("Error CLOSESERIALPORT method: " + e);//komunikat o bledzie na konsole
        }
    }
}
