package Model;

import jssc.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Adrian Szymowiat on 30.03.2018.
 * Klasa odpowiedzialna za polaczenie kablem serialowym
 */
public class SerialConnection implements SerialPortEventListener{
    /**
     * Obiekt klasy SerialPort
     */
    public static SerialPort serialPort;
    /**
     * Nazwa portu np. COM3.
     */
    private static String comportNumber;
    /**
     * Tablica przechowujaca dostepne porty COM
     */
    public static String[] comportsTable;
    /**
     * Flaga informujaca, czy otwieranie polaczenia serialowego przebieglo pomyslnie
     */
    public static boolean isComPortOk;
    /**
     * Bufor przechowujacy wiadomosci
     */
    private static ArrayList<String> answerBufor;
    /**
     * Metoda sprawdzajaca dostepne port COM
     */
    public static void availablePorts(){
        comportsTable = SerialPortList.getPortNames();//pobranie dostepnych portow COM
        StringBuilder stringBuilder = new StringBuilder();//stowrzenie obiektu klasy StringBuilder to zbudowania napisu
        for (int i = 0; i < comportsTable.length; i++) {//tworzenie napisu
            stringBuilder.append(comportsTable[i] + "\n");//dodanie kolejnych nazw portów COM do napisu
        }
        for (int i = 0; i < comportsTable.length; i++) {
            System.out.println(comportsTable[i] + "\n");//wypisanie wszystkich dostepnych portow com na konsole
        }
        if (comportsTable.length != 0) {//sprawdzenie czy sa jakies port COM
            JOptionPane.showMessageDialog(null, "Dostepne porty COM:\n" + stringBuilder);//okno informujace o dostepnych portach COM
        } else {
            JOptionPane.showMessageDialog(null, "Brak dostępnych portów COM");//okno informujace o dostepnych portach COM
        }
        comportsTable = null;//wyczyszczenie tablicy portów COM
    }
    /**
     * Metoda otwierajaca polaczenei serialowe
     * @param comportName
     */
    public void openSerialConnection(String comportName){
        comportNumber = comportName;//podstawienie nazwy portu COM
        try {
            answerBufor = new ArrayList<>();//stworzenie bufora na przechowywanei wiadomosci
            serialPort = new SerialPort(comportNumber);//stworzenie obiektu polaczenia serialowego o danej nazwie COM
            serialPort.openPort();//otwarcie polaczenia serialowego
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);//ustawienie parametrow
            serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);//dodanie listenera
            isComPortOk = true;//ustawienie falgi na true, pomyslne otwarcie polaczenia serialowego
        } catch (SerialPortException err) {
            JOptionPane.showMessageDialog(null, "Nieprawidlowa nazwa portu lub port zajęty!");//komunikat o bledzie
            System.out.println("Error in openSerialConnection method: " + err);//komunikat na konsole
            isComPortOk = false;//flaga na false, informacja o bledzie podczas otwierania polaczenia serialowego
        }catch (NullPointerException er) {
            JOptionPane.showMessageDialog(null, "Nie można nawiązać połączenia");//komunikat o bledzie
            System.out.println("Error in openSerialConnection method: " + er);//komunikat na konsole
            isComPortOk = false;//flaga na false, informacja o bledzie podczas otwierania polaczenia serialowego
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Napotkano niespodziewany blad podczas nawiązywania połączenai serialowego");//komunikat o bledzie
            System.out.println("Error in openSerialConnection method: " + e);//komunikat na konsole
            isComPortOk = false;//flaga na false, informacja o bledzie podczas otwierania polaczenia serialowego
        }finally {
            Main.isConnectionReady = isComPortOk;//DODATKOWE -> ustawienie informacji o powodzeniu/niepowodzeniu nawiazania polaczenia
        }
    }
    /**
     * Metoda odczytajaca otrzymywane wiadomosci (tekstowe) dla danego polaczenia
     * @param serialPortEvent
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {//...sprawdzenie czy otrzymano dane
            try {
                String receivedData = serialPort.readString(serialPortEvent.getEventValue());//odczytanie pojedynczego znaku
                answerBufor.add(receivedData);//dodanie otrzymanej wiadomosci do bufora
                //System.out.println("Odpowiedź modułu: "+receivedData);
            } catch (SerialPortException e) {
                closeSerialConnection();//zamkniecie polaczenia
                JOptionPane.showMessageDialog(null, "Bład połączenia serialowego:\n" + e + "\n" + "Połączenie zostanie przerwane");//informacja o bledzie
            } catch (Exception e) {
                closeSerialConnection();//zamkniecie polaczenia
                System.out.println("Error in serialEvent method: " + e);//komunikat na konsole
                JOptionPane.showMessageDialog(null, "Niespodziewany błąd:\n" + e + "\n" + "Połączenie zostanie przerwane");//informacja o bledzie
            }
        }
    }
    /**
     * Metoda zwracająca bufor wiadomosci wczytanych z portu
     * @return - bufor klasy
     */
    public ArrayList<String> getAnswerBufor(){
        return answerBufor;//zwracany bufor
    }
    /**
     * Metoda czyszczaca bufor wiadomosci wczytanych z portu
     */
    public void clearAnswerBufor(){
        answerBufor.clear();//wyczyszczenie bufora
    }
    /**
     * Metoda do wysylania wiadomosci tekstowych
     * @param command komenda do wyslania
     */
    public void sendMessage(String command){
        try {
            if(serialPort != null) {
                String answer = command+"\r";
                serialPort.writeString(answer);//wyslanie wiadomosci do urzadzenia
                System.out.println("Wysłano komendę: "+command);
            }
        }catch(SerialPortException er){
            System.out.println("Error in sendMessage method " + er);//komunikat o bledzie na konsole intellij
        } catch(Exception e){
            System.out.println("Error in sendMessage method " + e);//komunikat o bledzie na konsole intellij
        }
    }

    /**
     * Metoda zamykajaca istniejace polaczenie serialowe
     */
    public void closeSerialConnection(){
        try {
            if (serialPort != null && serialPort.isOpened()) {//sprawdzenie, czy jakies polaczenie zostalo utworzone i zostalo utworzone polaczenie
                serialPort.removeEventListener();//usuniecie nasluchiwania
                serialPort.closePort();//zamkniecie portu
                clearAnswerBufor();//wyczyszczenie bufora wiadomosci
            }
        }catch(Exception e){
            System.out.println("Error in closeSerialConnection method: " + e);//komunikat o bledzie na konsole
        }
    }
}
