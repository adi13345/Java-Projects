import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Watek serwera, tworzony dla kazdego klienta
 */
public class ClientThread implements Runnable {
    /**
     * Flaga informujaca ze trzeba odebrac plik
     */
    public boolean getFileFlag = false;
    /**
     * Id klienta
     */
    private String client_id;
    /**
     * Gniazdo polaczenia z klientem
     */
    private Socket socket;

    public boolean  thread_flag = true;

    /**
     * Konstruktor watku serwera, przypisanie gniazda do zmiennej
     * @param socket - gniazdo polaczeniowe
     * @param id - id klienta, ktory nawiazal polaczenie
     */
    public ClientThread(Socket socket, String id) {
        this.socket = socket;
        this.client_id = id;
    }

    /**
     * Metoda oblsugujaca przymujaca, zdarzenia pomiedzy klientem i serwerem
     */
    @Override
    public void run() {
        thread_flag = true;//flaga watku, odbieranie żądan klienta
        while (thread_flag) {//ZMIENIC NA FLAGE GLOBALNE
            try {
                if(socket!=null) {
                    //dane wejsciowe od klienta sa w postaci bitowej, trzeba je przekonwertowac na tekst
                    InputStream inputStream = socket.getInputStream();//strumien wejsciowy na gniezdzie, dane od klienta
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań klienta
                    OutputStream outputstream = socket.getOutputStream();//dane wyjsciowe od serwera
                    PrintWriter printwriter = new PrintWriter(outputstream, true);//przypisanie do strumienia wyjsciowego tego,
                    String clientrequest = bufferedreader.readLine();//przypisanie zadania klienta do zmiennej typu string
                    if (clientrequest != null) {//sprawdzanie czy klient wyslal wiadomosc
                        System.out.println(ConsoleFrame.getTimeandDate() + "ŻĄDANIE OD KLIENTA " + client_id + ": " + clientrequest);//komunikat na konsole Intellij
                        ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "ŻĄDANIE OD KLIENTA " + client_id + ": " + clientrequest + "\n");//komunikat na konsole serwera
                        Main.saveLogs(ConsoleFrame.getTimeandDate() + "ŻĄDANIE " + client_id + ": " + clientrequest);//komunikat do logow
                        String servermessage = ServerAction.ServerResponse(clientrequest,client_id);//obsluga zadania klienta przez klase SERverResponse
                        if (servermessage.equals("State:<250>Client logout") && servermessage !=null) {//w przypadku zadania natychmiastowego zerwania polaczenia
                            System.out.println(ConsoleFrame.getTimeandDate() + "Użytkownik o ID: "+client_id+" został wylogowany");//komunikat na konsole Intellij
                            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Użytkownik o ID: "+client_id+" został wylogowany" + "\n");//komunikat o bledzie
                            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Użytkownik o ID: "+client_id+" został wylogowany" );//komunikat do logow
                            Server.removeUserFromLoggedUserTable(client_id);//usuniecie usera z listy zalogowanych uzytkownikow
                            printwriter.println(servermessage);//wyslanei wiadomosci do klienta
                            printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                            thread_flag = false;//wylaczenie odbieranai żądań klienta
                            socket.close();//zamkniecie gniazda
                        }
                        else {
                            String[] first_bufor = clientrequest.split(":");//podzielenie żądania serwera
                            String[] second_bufor = first_bufor[1].split("%");//podzielenie żądania serwera
                            if(servermessage.equals("OK:<210>Getting file")){
                               // System.out.println(servermessage);
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                                servermessage = getFile(second_bufor[0],second_bufor[1],second_bufor[2]);//rozpoczecie pobieranai pliku
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                            }else if (servermessage.equals("Error:<308>The filename already exists")){
                                System.out.println(servermessage);
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                                clientrequest = bufferedreader.readLine();//przypisanie zadania klienta do zmiennej typu string
                                System.out.println(clientrequest);
                                String[] help_bufor = clientrequest.split(":");//podzielenie żądania serwera
                                if(help_bufor[1].equals("true")){
                                    servermessage = getFile(second_bufor[0],second_bufor[1],second_bufor[2]);//rozpoczecie pobieranai pliku
                                    printwriter.println(servermessage);//utworzenie wiadomosci
                                    printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                                }else if (help_bufor[1].equals("false") || help_bufor[1].equals("null")){
                                    System.out.println("adrian lubi macki");
                                    servermessage = "State:<255>Canceled file transfer";
                                    printwriter.println(servermessage);//utworzenie wiadomosci
                                    printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                                    Thread.sleep(100);
                                }
                            }else if(servermessage.equals("Error:<370>File already exists")){
                                System.out.println(ConsoleFrame.getTimeandDate() + "Podany plik już istnieje.");//komunikat na konsole Intellij
                                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Podany plik już istnieje." + "\n");//komunikat o bledzie
                                Main.saveLogs(ConsoleFrame.getTimeandDate() + "Podany plik już istnieje." );//komunikat do logow
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                            }else if(servermessage.equals("OK:<416>Failed to load path through unknown reason")){
                                System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas sprawdzania plikow od klienta o ID: "+client_id);//komunikat na konsole Intellij
                                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas sprawdzania plikow od klienta o ID: "+client_id+"\n");//komunikat o bledzie
                                Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas osprawdzania plikow od klienta o ID: "+client_id);//komunikat do logow
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                            }else if(servermessage.equals("OK:<230>Files deleted")){
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                            }else if(servermessage.equals("Error:<418>Cannot delete files through unknown reason")){
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                            }else if(servermessage.equals("OK:<240>Sending file")){
                                String message = getFileParameters(second_bufor[0]);
                                printwriter.println(servermessage+", size:%"+message);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                                //Thread.sleep(5);//uspanei watku na 5 milisekund
                                sendFile(second_bufor[0]);
                            }else if(first_bufor[0].equals("SHOW_FILES")){
                                printwriter.println(servermessage);//utworzenie wiadomosci
                                printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                            }
                        }
                    }

                }
            } catch (SocketException err){
                System.out.println(ConsoleFrame.getTimeandDate() + "Zresetowano połączenie użytkownika o ID: "+client_id+err);//komunikat na konsole Intellij
                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Zresetowano połączenie użytkownika o ID: "+client_id+"\n");//komunikat o bledzie
                Main.saveLogs(ConsoleFrame.getTimeandDate() + "Zresetowano połączenie użytkownika o ID: "+client_id);//komunikat do logow
                Server.removeUserFromLoggedUserTable(client_id);//usuniecie usera z listy zalogowanych uzytkownikow
                thread_flag = false;//wylaczenie odbieranai żądań klienta
            }
            catch(NullPointerException er){
                System.out.println(ConsoleFrame.getTimeandDate() + "Użytkownik o ID: "+client_id+" został wylogowany");//komunikat na konsole Intellij
                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Użytkownik o ID: "+client_id+" został wylogowany" + "\n");//komunikat o bledzie
                Main.saveLogs(ConsoleFrame.getTimeandDate() + "Użytkownik o ID: "+client_id+" został wylogowany" );//komunikat do logow
                Server.removeUserFromLoggedUserTable(client_id);//usuniecie usera z listy zalogowanych uzytkownikow
                thread_flag = false;//wylaczenie odbieranai żądań klienta
            }
            catch (Exception e) {
                System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd: " + e);//komunikat na konsole Intellij
                if(ConsoleFrame.textarea_!=null) {
                    ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd: " + e + "\n");//komunikat o bledzie
                }
                Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd: " + e);//komunikat do logow
                Server.removeUserFromLoggedUserTable(client_id);//usuniecie usera z listy zalogowanych uzytkownikow
                thread_flag = false;//wylaczenie odbieranai żądań klienta
            }
        }
    }

    /**
     * Metoda obslugujaca pobieranie pliku od klienta
     * @param filesize - rozmiar pliku
     * @param filename - nazwa pliku
     * @param modificationdate - data modyfikacji pliku
     * @return - odpowiedz serwera, czy sie udalo czy nie udalo sie otrzymac plik
     */
    private String getFile(String filesize, String filename, String modificationdate){
        //thread_flag=false;
        System.out.println(ConsoleFrame.getTimeandDate() + "Rozpoczęto pobieranie pliku "+filename);//komunikat na konsole Intellij
        ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Rozpoczęto pobieranie pliku "+filename+"\n");//komunikat o bledzie
        Main.saveLogs(ConsoleFrame.getTimeandDate() + "Rozpoczęto pobieranie pliku "+filename);//komunikat do logow
        try {
            String path = getPath();//sciezka do folderu usera
            InputStream inputStream = socket.getInputStream();//strumien danych przywiazany do gniazda
            FileOutputStream fileOutputStream = new FileOutputStream(path + "\\" + filename);//sciezka, gdzie ma byc zapisany plik zalezna od ID klienta
            byte[] buffer = new byte[4096];//pomocniczy bufor bajtow
            long filelenght = Long.valueOf(filesize);//dlugosc pliku w bajtach
            int read = 0;//zmienna do odczytania bajtow
            long remaining = filelenght;//zmienna pomocnicza
           // System.out.println("jest bitow:" + dataInputStream.available());
            while ((read = inputStream.read(buffer, 0, (int)Math.min(buffer.length,remaining))) > 0) {
                remaining -= read;
                fileOutputStream.write(buffer, 0, read);//wczytanie
                fileOutputStream.flush();//wypchanie wszystkich bitow
            }
            fileOutputStream.close();//zamkniecie strumienia pliku
            File file = new File(path + "\\" + filename);//zaladowanie nowoutworzonego pliku
            file.setLastModified(Long.valueOf(modificationdate));//ustawienei daty modyfikacji pliku, zgodna z plikiem klienta
            System.out.println(ConsoleFrame.getTimeandDate() + "Ukończono pobieranie pliku " + filename);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Ukończono pobieranie pliku " + filename + "\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Ukończono pobieranie pliku " + filename);//komunikat do logow
            return "OK:<220>File received";//zwrocienie wiadomosci do klienta w przypadku, gdy poprawnie odebrano dane
        }catch (IOException e){
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił błąd podczas odbierania pliku klienta o ID: "+client_id);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił błąd podczas odbierania pliku klienta o ID: "+client_id+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił błąd podczas odbierania pliku klienta o ID: "+client_id);//komunikat do logow
            return "Error:<410>Failed to load file";//w przypadku gdy nie udalo sie pobrac pliku
        }catch (Exception er){
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas odbierania pliku od klienta o ID: "+client_id+" typ błędu: "+er);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas odbierania pliku od klienta o ID: "+client_id+" typ błędu: "+er+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas odbierania pliku od klienta o ID: "+client_id+" typ błędu: "+er);//komunikat do logow
            return "Error:<415>Failed to load file through unknown reason";//w przypadku gdy nie udalo sie pobrac pliku
        }
    }

    /**
     * Metoda wysyłająca plik do klienta
     * @param file_name - nazwa pliku
     * @return - wiadomosc serwera
     */
    private String sendFile(String file_name){//PRZETESTOWAC
        try {
            System.out.println(ConsoleFrame.getTimeandDate() + "Rozpoczęto wysyłanie pliku "+file_name);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Rozpoczęto wysyłanie pliku "+file_name+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Rozpoczęto wysyłanie pliku "+file_name);//komunikat do logow
            String path = getPath();//sciezka do pliku danego userA
            File myFile = new File(path+"\\"+file_name);//plik do wyslania
            OutputStream outputStream = socket.getOutputStream();//przywizania strumienia wyjsciowego danych do gnizada
            FileInputStream fileInputStream = new FileInputStream(myFile);//zaladowanie pliku do wyslania - utworzenie strumienia pliku
            byte[] buffer = new byte[4096];//paczka danych
            int read = 0;//licznik odczytanych bajtow
            long remaining = myFile.length();//dlugosci pliku - w sumie niepotrzebne ddrugi raz ale co tam
            while ((read = fileInputStream.read(buffer, 0, (int)Math.min(buffer.length,remaining))) > 0) {//ramaining rzutowane na int - ograniczenie rozmiaru wysylanego pliku
                remaining -= read;//zmniejszamy licznik o ilosc odczytach bajtow az dojdziemy do 0 czyli odczytamy caly plik
                outputStream.write(buffer, 0, read);//wyslanie paczki danych - na koncu nie bedzie to juz cale 4096
                outputStream.flush();//wypchanie wszystkich danych
            }
            fileInputStream.close();//zamkniecie strumienia pliku
            System.out.println(ConsoleFrame.getTimeandDate() + "Ukończono wysyłanie pliku "+file_name);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Ukończono wysyłanie pliku "+file_name+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Ukończono wysyłanie pliku "+file_name);//komunikat do logow
            return "OK:<232>The file was send";
        } catch (FileNotFoundException e) {
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania pliku klienta o ID: "+client_id+" typ błędu: "+e);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania pliku klienta o ID: "+client_id+" typ błędu: "+e+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania pliku klienta o ID: "+client_id+" typ błędu: "+e);//komunikat do logow
            return "Error:<420>Cannot find specified file";
        } catch (Exception er) {
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania pliku klienta o ID: "+client_id+" typ błędu: "+er);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania pliku klienta o ID: "+client_id+" typ błędu: "+er+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania pliku klienta o ID: "+client_id+" typ błędu: "+er);//komunikat do logow
            return "Error:<421>Cannot send specified file through unkown reason";
        }
    }
    /**
     * Metoda zwracajaca sciezke danego uzytkownika
     * @return - sciezka danego uzytkownika
     */
    public String getPath(){
        String path = null;
        for (int i = 0; i < Users.userstable.size(); i++) {//przeszukjemy cala tablice userow
            if (Users.userstable.get(i)[2].equals(client_id)) {//sprawdzamy,ktorej sciezke odpowiada id klienta wyzylajacego żądanie
                path = Users.userstable.get(i)[3];//przypisanie danej sciezki
            }
        }
        return path;
    }
    /**
     * Metoda tworzaca wiadomosc do klienta, inforujaca go o rozmiarze pliku
     * @param filename - nazwa szukanego pliku
     * @return - fragment wiadomosci do klienta, dlugosc pliku do wyslania
     */
    private String getFileParameters(String filename){
       try {
           String file_length;//dlugosc pliku
           File file = new File(getPath() + "\\" + filename);//zaladowanie okreslonego pliku
           System.out.println(getPath() + "\\" + filename);//
           long length = file.length();//pobranie dlugosci pliku
           file_length = Long.toString(length);//konwersja longa na stringa
           return file_length;//zwrocenie dlugosci pliku w postaci napisu
       }catch (Exception e){
           System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania rozmiaru pliku do wyslania klienta o ID: "+client_id+" typ błędu: "+e);//komunikat na konsole Intellij
           ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania rozmiaru pliku do wyslania klienta o ID: "+client_id+" typ błędu: "+e+"\n");//komunikat o bledzie
           Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas wczytywania rozmiaru pliku do wyslania klienta o ID: "+client_id+" typ błędu: "+e);//komunikat do logow
           return "Error:<419>Cannot load file size";
       }
    }
}