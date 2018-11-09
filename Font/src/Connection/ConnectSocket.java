package Connection;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static Action.ConsoleFrame.newlinechar_;
import static Action.ConsoleFrame.textarea_;
import static Action.ConsoleFrame.getTimeandDate;

/**
 * Klasa sluzaca do nawiazywania polaczenia
 * Created by Adrian Szymowiat on 14.07.2017.
 */
public class ConnectSocket {
    /**
     * Adres IP hosta docelowego
     */
    public static String ipaddress_;
    /**
     * Numer portu
     */
    public static int portnumber_;
    /**
     * Gniazdo polaczeniowe
     */
    public static Socket socket_;
    /**
     * Metoda nawiazujaca polaczenie
     */
    public ConnectSocket(String ipaddress, int portnumber){
        ipaddress_=ipaddress;//przypisanie adresu ip
        portnumber_=portnumber;//przypisanie numeru portu
        try {
            socket_ = new Socket(ipaddress_, portnumber_);//utworzenie gniazda
        }catch (Exception e){
            System.out.println("Error CONNECT Constructor " + e);//komunikat o bledzie na konsole intellij
            JOptionPane.showMessageDialog(null, "Nie można nawiązać połączenia.");//wiadomosc informujaca, ze nie mozna nawiazac polaczenia
        }
    }
    /**
     * Metoda tworzaca wiadomosc do wyslania przez gniazdo
     * @param command - komenda do wyslania
     */
    public static void makeOutputStream(String command){
        try {
            if(socket_ != null) {
                OutputStream outputStream = socket_.getOutputStream();//strumien wyjsciowy danych
               // outputStream.write((command+newlinechar_).getBytes());
                PrintWriter printWriter = new PrintWriter(outputStream, true);//"przywiazanie?" wiadomosci do strumienia wyjsciowego (obudowanie?)
                printWriter.print(command+newlinechar_);//utworzenie/wyslanie wiadomosci
                printWriter.flush();///wypchanie wszystkich danych
            }
        }catch(Exception e){
            System.out.println("Error MAKEOUTPUTSTREAM method " + e);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error MAKEOUTPUTSTREAM method " + e + newlinechar_ );//komunikat bledu na konsole programu
        }
    }

    /**
     * Metoda zwracajaca informacje, czy zostalo utworzone polaczenie
     * @return - true - polaczenie zamkniete, false - polaczenei otwarte
     */
    public static boolean showConnectionStatus(){
        return socket_.isClosed();
    }
}
