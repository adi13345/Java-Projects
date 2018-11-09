package FTP;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ParsingPaths.ParsePaths.log4jpropertispath_;
import static ParsingPaths.ParsePaths.userspropertiespath_;

/**
 * Klasa odpowiedzialna za tworzenie i konfiguracje servera FTP
 * Created by Adrian Szymowiat on 09.08.2017.
 */
public class ServerFTP {
    /**
     * Numer portu, na ktorym nasluchuje ftp
     */
    private static final int ftpport_ = 21;
    /**
     * Flaga informujaca, czy serwer zostal uruchomiony
     */
    private static boolean isserveron_ = false;
    /**
     * Nazwa uzytkownika
     */
    private String username_;
    /**
     * Haslo uzytkownika
     */
    private String password_;
    /**
     * Lista autoryzacji uzytkownika
     */
    private List<Authority> authorities;
    /**
     * Instacja klasy FtpServer
     */
    private static FtpServer ftpServer;
    /**
     * Konstruktor klasy ServerFTP
     * @param username - nazwa uzytkownika/login
     * @param password - haslo do konta
     */
    public ServerFTP(String username, String password, String homedirectory){
        try {
            PropertyConfigurator.configure(log4jpropertispath_);//ustawienie sciezki
            isserveron_ = true;//ustawienie flagi na true, serwer włączony
            username_ = username;//przypisanie nazwy uzytkownika
            password_ = password;//przypisanie  hasla uzytkownika
            ListenerFactory listenerFactory = new ListenerFactory();//stworzenie listnerea serwera
            listenerFactory.setPort(ftpport_);//ustawienie nasluchiwania na konkretny port
            FtpServerFactory serverFactory = new FtpServerFactory();//...obiekt ustawien serwea ftp
            serverFactory.addListener("default", listenerFactory.createListener());//doddanie(stworzenie) listenera do ustawien serwera
            PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
            File file = new File(userspropertiespath_);//plik ustawien uzytkownika
            userManagerFactory.setFile(file);//ustawienie pliku do userManagerFactory
            userManagerFactory.setPasswordEncryptor(passwordEncryptorConfiguration());//...
            //dodanie użytkownika - wersja podstawowa
            BaseUser baseruser = new BaseUser();//utworzenie podstawowego uzytkownika
            baseruser.setName(username_);//przypisanie nazwy uzytkownika
            baseruser.setPassword(password_);//ustawienie hasla uzytkownika
            baseruser.setHomeDirectory(homedirectory);//ustawienie sciezki serwera ftp
            authorities = new ArrayList<>();//imnicjalizacja listy autorycacji
            authorities.add(new WritePermission());//ustawienie dostepu
            baseruser.setAuthorities(authorities);//ustawienie autoryacji dla uzytkownika
            UserManager userManager = userManagerFactory.createUserManager();//utworzenie usermanagera
            serverFactory.setUserManager(userManager);//...
            Map<String, Ftplet> map = new HashMap<>();//...mapa
            map.put("miaFtplet",createFtplet());//dodanie ftpleta do mapy
            serverFactory.setFtplets(map);//...
            userManager.save(baseruser);//zapisanie uzytkownika do users.properties
            createFtpServer(serverFactory);//uruchomienie serwera
            if(ftpServer.isStopped() == false){
                JOptionPane.showMessageDialog(null,"Serwer został pomyślnie uruchomiony!");//informacja o pomyslnych uruchomioeniu serwera
            }else{
                JOptionPane.showMessageDialog(null,"Nie udało się uruchomić serwera!");//informacja, ze nie udalo sie uruchomic serwera
            }
        }catch(FtpException er){
            System.out.println("Error ServerFtp Construktor: " + er);
            JOptionPane.showMessageDialog(null,"Wystąpił błąd podczas uruchamiania serwera FTP:\n" + er );//komunikat o bledzie
        }catch(Exception e){
            System.out.println("Error ServerFtp Construktor: " + e);
            JOptionPane.showMessageDialog(null,"Wystąpił błąd podczas uruchamiania serwera FTP:\n" + e );//komunikat o bledzie
        }
    }
    /**
     * Metoda zwracajaca password encryptor - potrzebne do logowania
     * @return PasswordEncryptor
     */
    private PasswordEncryptor passwordEncryptorConfiguration(){
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor() {//stworzenie obiektu operowania nad haslem uzytkownika
            @Override
            public String encrypt(String password) {
                return password;
            }//metoda zwrcajaca haslo uzytkownika
            @Override
            public boolean matches(String passwordToCheck, String storedPassword) {
                return passwordToCheck.equals(storedPassword);//metoda porownujaca hasla: podane z zapisanym
            }
        };
        return  passwordEncryptor;//zwrocenie enkryptora
    }
    /**
     * Metoda okreslajca ustawienia funkcji Ftplet
     * @return Ftplet
     */
    private Ftplet createFtplet(){
        Ftplet ftplet = new Ftplet() {//utworzenie obiektu ftplet
            @Override
            public void init(FtpletContext ftpletContext) throws FtpException {
                //System.out.println("init");
                //System.out.println("Thread #" + Thread.currentThread().getId());
            }
            @Override
            public void destroy() {
                //System.out.println("destroy");
               // System.out.println("Thread #" + Thread.currentThread().getId());
            }
            @Override
            public FtpletResult beforeCommand(FtpSession ftpSession, FtpRequest ftpRequest) throws FtpException, IOException {
                //System.out.println("beforeCommand " + ftpSession.getUserArgument() + " : " + ftpSession.toString() + " | " + ftpRequest.getArgument() + " : " + ftpRequest.getCommand() + " : " + ftpRequest.getRequestLine());
                //System.out.println("Thread #" + Thread.currentThread().getId());
                return FtpletResult.DEFAULT;//...or return accordingly
                //return null;
            }
            @Override
            public FtpletResult afterCommand(FtpSession ftpSession, FtpRequest ftpRequest, FtpReply ftpReply) throws FtpException, IOException {
                //System.out.println("afterCommand " + ftpSession.getUserArgument() + " : " + ftpSession.toString() + " | " + ftpRequest.getArgument() + " : " + ftpRequest.getCommand() + " : " + ftpRequest.getRequestLine() + " | " + ftpReply.getMessage() + " : " + ftpReply.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());
                return FtpletResult.DEFAULT;//...or return accordingly
                //return null;
            }
            @Override
            public FtpletResult onConnect(FtpSession ftpSession) throws FtpException, IOException {
                //System.out.println("onConnect " + ftpSession.getUserArgument() + " : " + ftpSession.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());
                return FtpletResult.DEFAULT;//...or return accordingly
                //return null;
            }
            @Override
            public FtpletResult onDisconnect(FtpSession ftpSession) throws FtpException, IOException {
                //System.out.println("onDisconnect " + ftpSession.getUserArgument() + " : " + ftpSession.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());
                return FtpletResult.DEFAULT;//...or return accordingly
                //return null;
            }
        };
        return ftplet;//zwrocenie obiektu
    }
    /**
     * Metoda przypisujaca ustawienia serwera i uruchamiajaca go
     * @param ftpServerFactory - ServerFactory
     * @throws FtpException - wyjatek rzucany przez metoda, lapany w konstruktorze
     */
    private void createFtpServer(FtpServerFactory ftpServerFactory) throws FtpException{
        ftpServer = ftpServerFactory.createServer();//utworzenie serwera ftp na ustawieniach zawartych w konstruktorze
        ftpServer.start();//urchomienie serwera
        System.out.println("Uruchomiono serwer FTP");//komunikat na konsole
    }
    /**
     * Metoda zwracajaca stan serwera - uruchomiony lub nie
     * @return - true/false, zaleznosci czy serwer zostal uruchomiony
     */
    public static boolean getServerStatus(){
        return isserveron_;
    }
    /**
     * Metoda zamykajaca serwer FTP
     */
    public void closeFtpServer(){
        if(ftpServer.isStopped() == false) {//sprawdzenie czy
            try {
                ftpServer.stop();//zatrzymanei serwera ftp
                System.out.println("Zamknieto serwer FTP");//komunikat na konsole
                isserveron_ = false;//ustawienei flagi na false, mozna uruchomic ponownie serwer
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,"Nie można zamknąć serwera ftp: " + e);//informacja o bledzie
            }
        }
    }
}

//----------------------->MOZE SIE PRZYDA<-----------------------
  /*
   Metoda tworzaca uzytkownika
     @return - uzytkownik wraz z parametrami
   private User createUser(){
        User user = new User() {
            //metody zwracajace parametry uctkownika
            @Override
            public String getName() {//metoda zwracajaca nazwe uzytkownika
                return username_;
            }
            @Override
            public String getPassword() {//metoda zrwcajaca haslo uzytkownika
                return password_;
            }
            @Override
            public List<? extends Authority> getAuthorities() {
                return authorities;
            }
            @Override
            public List<? extends Authority> getAuthorities(Class<? extends Authority> aClass) {
                return null;
            }
            @Override
            public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
                return null;
            }
            @Override
            public int getMaxIdleTime() {
                return 0;
            }
            @Override
            public boolean getEnabled() {
                return false;
            }
            @Override
            public String getHomeDirectory() {
                return null;
            }
        };
        return user;
    }*/
