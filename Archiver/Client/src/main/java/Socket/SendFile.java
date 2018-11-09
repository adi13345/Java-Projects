package Socket;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Klasa waek wysylający dane na serwer
 */
public class SendFile implements Runnable {
    /**
     * Gniazdo klienta
     */
    private Socket socket;
    /**
     * Sciezka do pliku, ktory uzytkownik chce wyslac
     */
    private String path_to_file;
    /**
     * Rozmiar wysylanego pliku
     */
    private String fileLength;
    /**
     * Nazwa wysylanego pliku
     */
    private String fileName;
    /**
     * Data osatniej modyfikacji (format long)
     */
    private String lastModification;


    private boolean automaticArhive;
    /**
     * Konstruktor watku wysylania pliku
     * @param socket - gniazdo polaczeniowe przez, ktore wysylamy plik
     * @param path_to_file - sciezka do wysylanego pliku
     * @param fileLength - dlugosc wysylanego pliku
     * @param fileName - nazwa wysylanego pliku
     * @param lastModification - data modyfikacji pliku
     * @param automaticArhive - flaga automatycznej archwizacji
     */
    public SendFile(Socket socket,String path_to_file, String fileLength,String fileName, String lastModification,boolean automaticArhive){
        this.socket = socket;
        this.path_to_file=path_to_file;
        this.fileLength = fileLength;
        this.fileName = fileName;
        this.lastModification = lastModification;
        this.automaticArhive=automaticArhive;
    }
    /**
     * Watek odpowiedzialny za archiwizacje plikow
     */
    @Override
    public void run() {
           try {
               OutputStream outputStream = socket.getOutputStream();//strumien wyjsciowy gniazda
               PrintWriter printWriter = new PrintWriter(outputStream, true);//obiekt do utworzenia wiadomosci
               printWriter.println("TAKE_FILE:" + fileLength + "%" + fileName + "%" + lastModification);//utworzenie wiadomosci do serwera, informujacej go o rozmiarze pliku i jego nazwie
               printWriter.flush();//wypchanie wszystkich bitow ze strumienia
               InputStream inputStream = socket.getInputStream();//strumien wejsciowy na gniezdzie, dane od serwera
               BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań serwera
               String answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
               System.out.println(answer);
               String code = GetInputStream.getAnswear(answer)[0];//odczytanei kodu wiadomosci
               if(code.equals("210")) {
                   sendFile();//wywloanie nizej zdefiniowanej metody
                   answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
               }else if(code.equals("308")) {//gdy serwer sie pyta, cyz na pewno przeslacp lik, bo juz istnieje o podanej nazwie
                   if (!automaticArhive) {
                       String flag = "null";
                       int selectedOption = JOptionPane.showConfirmDialog(null, "Plik o podanej nazwie już istnieje, czy chcesz go zastąpić?", "Warning!", JOptionPane.YES_NO_OPTION);
                       if (selectedOption == JOptionPane.YES_OPTION) {
                           flag = "true";
                           printWriter.println("TAKE_FILE:" + flag);
                           printWriter.flush();
                           try {
                               Thread.sleep(100);//!!!!!!!!!!!!!!! bez czekania nie dziala
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           sendFile();//wywolanie nizej zdefiniowanej metody
                           System.out.println("Potwierdzono zamiane pliku");
                       } else if (selectedOption == JOptionPane.NO_OPTION) {
                           flag = "false";
                           printWriter.println("TAKE_FILE:" + flag);
                           printWriter.flush();
                           System.out.println("Anulowano zamiane pliku");
                       }
                       answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
                   }else{
                       String flag = "true";
                       printWriter.println("TAKE_FILE:" + flag);
                       printWriter.flush();
                       try {
                           Thread.sleep(100);//!!!!!!!!!!!!!!! bez czekania nie dziala
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       sendFile();
                       answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
                   }
               }
               else if (code.equals("370")) {
                   //DOROBIC 2-3 PRZYPADKI - 2-3 WYSKAKUJCE OKIENKA <- BO SA JESZCZE DWIE/TRZY ODPOWIEDZI SERWERA
                   System.out.println("Podany plik juz istnieje " + answer);
               }
           } catch (FileNotFoundException e) {
               System.out.println("Nie mozna znalezc pliku " + e);
           } catch (IOException er) {
               System.out.println("Blad: " + er);
           }
    }

    /**
     * Metoda odpowiedzialna za wysylanie pliku
     */
    private void sendFile(){
        try {//zmienic dataoutputstream n zwykly/bufferadoutputstream
            File myFile = new File(path_to_file);//plik do wyslania
            OutputStream outputStream = socket.getOutputStream();//przywizania strumienia wyjsciowego danych do gnizada
            FileInputStream fileInputStream = new FileInputStream(myFile);//zaladowanie pliku do wyslania - utworzenie strumienia pliku
            byte[] buffer = new byte[4096];//paczka danych
            int read = 0;//licznik odczytanych bajtow
            long remaining = myFile.length();//dlugosci pliku - w sumie niepotrzebne ddrugi raz ale co tam
            while ((read = fileInputStream.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {//ramaining rzutowane na int - ograniczenie rozmiaru wysylanego pliku
                remaining -= read;//zmniejszamy licznik o ilosc odczytach bajtow az dojdziemy do 0 czyli odczytamy caly plik
                outputStream.write(buffer, 0, read);//wyslanie paczki danych - na koncu nie bedzie to juz cale 4096
                outputStream.flush();//wypchanie wszystkich danych
                //System.out.println("Wyslano bitow:  " +read);
            }
            fileInputStream.close();//zamkniecie strumienia pliku
        } catch (FileNotFoundException e) {
            System.out.println("Nie mozna znalezc pliku " + e);
        } catch (IOException er) {
            System.out.println("Blad: " + er);
        }
    }
}
