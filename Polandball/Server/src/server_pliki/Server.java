package server_pliki;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa serwera, przyjmujaca zadania klientow
 */
public class Server {

    /**
     * Numer portu serwera
     */

    private int serverport;

    /**
     * Metoda wczytujaca port z pliku i startujaca server
     */

    public void StartServer(){
        try {
            BufferedReader nrportu = new BufferedReader(new FileReader("src\\server_pliki\\nrportu.txt"));//wczytanie nr portu z pliku
            serverport = Integer.parseInt(nrportu.readLine());//przypisanie nr portu z pliku do zmiennej serverport
            ServerSocket serverSocket = new ServerSocket(serverport);//utworzenie gniazda serwera, nasluchiwanie na zadanym porcie
            System.out.println("Serwer zostal uruchomiony ");//wypisanie informacji o uruchomieniu serwera
            while (true) {
                Socket socket = serverSocket.accept();//akceptowanie klientów
                new Thread(new ServerThread(socket)).start();//oddzielny watek dla kazdego gniazda
            }
        }
        catch (IOException e){
            System.out.println("Blad uruchomienia serwera");
            System.err.println(e);
        }
    }
}


