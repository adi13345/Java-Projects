package Socket;

import java.io.IOException;
import java.net.Socket;

/**
 * Przechowuje dane na temat połączenia - numeru IP,numeru portu i wtyczki
 */
public class ConnectSocket {
    /**
     * String zachowujący dane IP z którym mamy się połączyć
     */
    private String ipAdress;
    /**
     * Przechowuje numer portu z którym mamy się połączyć
     */
    private int portNumber;
    /**
     * Socket przechowujący dane na temat socketa
     */
    private Socket socket;

    /**Kontruktor
     *
     * @param ipAdress_-Ip adress z którym ma nastapić połączenie
     * @param portNumber_- numer portu z kórym ma połączyć
     * @throws IOException - rzuca wyjątkiem wrazie Błedu typu IO
     */
    public ConnectSocket(String ipAdress_,int portNumber_) throws IOException{
        ipAdress=ipAdress_;
        portNumber=portNumber_;
        socket =new Socket(ipAdress,portNumber_);
    }

    /**
     * Zwraca numer ip
     * @return
     */
    public String getIpAdress() {
        return ipAdress;
    }

    /**
     * UStawia nowy adress ip
     * @param ipAdrees
     */
    public void setIpAdress(String ipAdrees) {
        this.ipAdress = ipAdrees;
    }

    /**
     * Ustawia nowy numer portu
     * @param portNumber
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Zwraca numer portu
     * @return
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Zwracaa atrybut Socket
     * @return
     */
    public Socket getSocket() {
        return socket;
    }
}
