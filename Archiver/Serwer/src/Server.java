import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
/**
 * Klasa serwera, odpowiedzialna za przyjmowanie klientow
 */
public class Server {
    /**
     * Nazwa uzytkownika, ktory jest juz zalogowany
     */
    private static String logged_user_login;
    /**
     * Flaga informujaca, ze dany uzytkownik jest zalogowany
     */
    private static boolean user_is_logged;
    /**
     * Zmienna przechowujaca ID aktualnie logujacego sie uzytkownika
     */
    private static String user_id;
    /**
     * Dynamiczna tablica zalogowanych uzytkownikow
     */
    private static ArrayList<String[]> logged_users;
    /**
     * Wiadomosc wysylana do klienta podczas weryfikacji
     */
    private static String verification_answer;
    /**
     * Gniazdo komunikacyjne serwera
     */
    public static ServerSocket serverSocket;
    /**
     * Metoda tworzaca gniazdo serwera, uchamiajaca go i oblsugujaca logowania klientow
     */
    public void StartServer(){
        try {
            serverSocket = new ServerSocket(ServerParameters.server_port);//utworzenie gniazda serwera, nasluchiwanie na zadanym porcie
            logged_users = new ArrayList<>();//utworzenie listy zalogowanych uzytkownikow
            System.out.println(ConsoleFrame.getTimeandDate()+ "Uruchomiono serwer, numer portu: " + String.valueOf(ServerParameters.server_port));//komunikat o uruchomieniu serwera
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Uruchomiono serwer, numer portu: " + String.valueOf(ServerParameters.server_port));//komunikat o uruchomieniu serwera do pliku z logami
            if(ConsoleFrame.textarea_ != null) {//sprawdzenie czy nie zamknieto okna konsoli
                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Uruchomiono serwer, numer portu: " + String.valueOf(ServerParameters.server_port+"\n"));//komunikat o uruchomieniu serwera
            }
        }
        catch (Exception e){
            System.out.println(ConsoleFrame.getTimeandDate()+ "Wystąpił błąd podczas uruchamiania serwera: " + e);//komunikat o o bledzie
            if(ConsoleFrame.textarea_ != null) {//sprawdzenie czy nie zamknieto okna konsoli
                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Wystąpił błąd podczas uruchamiania serwera: " + e+"\n");//komunikat o bledzie
            }
        }
        while (serverSocket != null ) {//zmienilem
            acceptClient();//wywolanei nizej zdefiniowanej metody
        }
    }
    /**
     * Metoda odpowiedzialna za przyjmownie klientow
     */
    private void acceptClient() {
        try {
            if(serverSocket.isClosed() != true){//sprawdzenie czy gniazdo jest otwarte
                Socket socket = serverSocket.accept();//akceptowanie klientów
                OutputStream outputstream = socket.getOutputStream();//dane wyjsciowe od serwera
                PrintWriter printWriter = new PrintWriter(outputstream, true);//przypisanie do strumienia wyjsciowego
                if(verifityClient(socket) == true){//sprawdzenie danych logwania klienta
                    System.out.println(ConsoleFrame.getTimeandDate()+ "Logowanie przebieglo pomyslnie");//komunikat na konsole Intellij
                    ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate()+ "Logowanie przebieglo pomyslnie"+"\n");//komunikat na konsole serwera
                    Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Logowanie przebieglo pomyslnie");//komunikat do logow
                    new Thread(new ClientThread(socket,user_id)).start();//oddzielny watek dla kazdego gniazda
                    verification_answer = "OK:<200>Logged Successfully, ID:"+user_id;//wiadomosc do klienta
                    printWriter.println(verification_answer);//utworzenie wiadomosci
                    printWriter.flush();
                }else{
                    if(user_is_logged == false) {
                        System.out.println(ConsoleFrame.getTimeandDate() + "Nieprawidłowy login lub hasło");//komunikat na konsole Intellij
                        ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nieprawidłowy login lub hasło" + "\n");//komunikat na konsole serwera
                        Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nieprawidłowy login lub hasło");//komunikat do logow
                        verification_answer = "Error:<400>Invalid login or password";//wiadomosc do klienta
                        printWriter.println(verification_answer);//utworzenie wiadomosci
                        printWriter.flush();
                        socket.close();//zamkniecie gniazda
                    }else {
                        System.out.println(ConsoleFrame.getTimeandDate() + "Uzytkownik '" +logged_user_login+"' jest juz zalogowany");//komunikat na konsole Intellij
                        ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Uzytkownik '" +logged_user_login+"' jest juz zalogowany" + "\n");//komunikat na konsole serwera
                        Main.saveLogs(ConsoleFrame.getTimeandDate() + "Uzytkownik '" +logged_user_login+"' jest juz zalogowany");//komunikat do logow
                        verification_answer = "Error:<300>User '"+logged_user_login+"' is already logged in";//wiadomosc do klienta
                        logged_user_login = null;//ustawienie nazwy zalogowanego usera na null
                        printWriter.println(verification_answer);//utworzenie wiadomosci
                        printWriter.flush();
                        socket.close();//zamkniecie gniazda
                    }
                }

            }
        }
        catch(SocketException er){//--------------------------->WALI BLEDEM ZAWSZE<----------------------------
            System.out.println(ConsoleFrame.getTimeandDate()+ "Zamknieto gniazdo serwera");//komunikat
            if(ConsoleFrame.textarea_ != null) {//sprawdzenie czy nie zamknieto okna konsoli
                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Zamknieto gniazdo serwera"+"\n");//komunikat
            }
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Zamknieto gniazdo serwera");//komunikat do logow
        }
        catch (Exception e) {
            System.out.println(ConsoleFrame.getTimeandDate()+ "Wystąpił błąd podczas weryfikacja klienta: " + e);//komunikat o bledzie
            if(ConsoleFrame.textarea_ != null) {//sprawdzenie czy nie zamknieto okna konsoli
                ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Wystąpił błąd podczas weryfikacja klienta: " + e+"\n");//komunikat o bledzie
            }
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Wystąpił błąd podczas weryfikacja klienta: " + e);//komunikat do logow
        }
    }

    /**
     * Metoda weryfikujaca logowania klientow
     * @param socket - gniazdo polaczeniowe klient/serwer
     * @return - flaga informujaca o poprawnosci logowania klienta
     */
    private boolean verifityClient(Socket socket){
        try {
            logged_user_login = null;//ustawienie nazwy zalogowanego usera na null
            user_id = null;//ustawienie id usera na null
            user_is_logged = false;//globalna flaga informujaca o zalogowaniu uzytkownika
            boolean flag = false;//flaga informujaca o poprawnosci weryfikajci klienta
            boolean logged_user_flag = false;//lokalna flaga informujaca ze user jest zalogowany
            InputStream inputStream = socket.getInputStream();//strumien wejsciowy na gniezdzie, dane od klienta
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań klienta
            String clientrequest = bufferedreader.readLine();//przypisanie zadania klienta do zmiennej typu string
            System.out.println(ConsoleFrame.getTimeandDate()+ "Próba logowania klienta: "+clientrequest);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate()+ "Próba logowania klienta: "+clientrequest+"\n");//komunikat na konsole serwera
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Próba logowania klienta: "+clientrequest);//komunikat do logow
            String[] bufor_one = clientrequest.split(":");
            String bufor[] = bufor_one[1].split("%");//bufor pomocniczy zawierajacy dane logowania klienta
            for(int i = 0; i < Users.userstable.size(); i++){
                if(Users.userstable.get(i)[0].equals(bufor[0]) && Users.userstable.get(i)[1].equals(bufor[1])){
                    for(int j =0;j < logged_users.size();j++) {
                        if(logged_users.get(j)[0].equals(bufor[0])) {
                            logged_user_flag = true;//ustawienie flagi na true, user jest juz zalogowany
                            user_is_logged = true;//flaga informujaca o zalogwaniu uzytkwonika, potrzebna do wyslania odpowiedniej wiadomoci do klienta
                            logged_user_login = bufor[0];//przypisanie loginu zalogowanego usera
                            break;//wyjscie z petli
                        }
                    }
                    if(logged_user_flag == false) {
                        flag = true;//poprawnie zweryfikowany klient
                        String[] logged_users_bufor = new String[2];//bufor pomocniczy
                        logged_users_bufor[0] = Users.userstable.get(i)[0];//dodanie nazwy zalogowanego uzytkownika
                        logged_users_bufor[1] = Users.userstable.get(i)[2];//dodanie ID zalogowanego uzytkownika
                        user_id = logged_users_bufor[1];
                        logged_users.add(logged_users_bufor);//dodanie zalogowanego usera do tablicy zalogwanych userow
                        break;//wyjscie z petli
                    }
                }
            }
            return flag;//zwrocenie flagi weryfikacjia
        }catch (IOException e){
            System.out.println(ConsoleFrame.getTimeandDate()+ "Nastąpił niespodziewany błąd podcas weryfikacji klienta: "+e);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate()+ "Nastąpił niespodziewany błąd podcas weryfikacji klienta: "+e+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Nastąpił niespodziewany błąd podcas weryfikacji klienta: "+e);//komunikat do logow
            return false;//flaga na false
        }
    }
    /**
     * Metoda usuwajaca uzytkownika z tablicy zalogowanych userow po wylogowywaniu
     * @param id - id uzytkownika, ktory ma zostac usuniety z listy zalogowanych uzytkownikow
     */
    public static void removeUserFromLoggedUserTable(String id){
        for(int i = 0; i < logged_users.size();i++){//sprawdzenei calej tablicy userow
            if(logged_users.get(i)[1].equals(id)){//jak id sie zgadza to usuwamy
                logged_users.remove(i);//usuniecie konkretne usera
            }
        }
    }
}