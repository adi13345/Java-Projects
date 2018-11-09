package Swarm;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by Adrian Szymowiat on 29.08.2018
 * Klasa sluzaca do polaczenia SSH
 */
public class ExtendedJSch extends JSch {
    /**
     * Adres hosta
     */
    private String host;
    /**
     * Nazwa uzytkownika
     */
    private String user;
    /**
     * Haslo uzytkownika
     */
    private String password;
    /**
     * Numer portu polaczenia SSH
     */
    private int port;
    /**
     * Obiekt sesji SSH
     */
    private Session session;
    /**
     * Czas oczekiwania na nawiazanie polaczenia
     */
    private int timeout;
    /**
     * Obiekt kanalu SSH
     */
    private Channel channel;
    /**
     * Bufora na dane ze strumienia
     */
    private ByteArrayOutputStream byteArrayOutputStream;
    /**
     * Obiekt umozliwiajacy wyslanie danych
     */
    private PrintStream printStreamOutput;
    /**
     * Obiekt umozliwiajacy odbieranie danych
     */
    private InputStream inputStream;
    /**
     * Flaga sterujaca odczytem
     */
    private boolean readOutputFlag;
    /**
     * Konstruktor klasy
     * Ustawienie paremtrow polaczenia
     * @param host - adres hosta
     * @param user - nazwa uzytkownika
     * @param password - haslo uzytkownika
     * @param port - numer portu polaczenia SSH
     * @param timeout - czas oczekiwania na nawiazanie polaczenia
     * @throws JSchException
     */
    public ExtendedJSch(String host, String user, String password, int port, int timeout) throws JSchException {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
        this.timeout = timeout;
        session = this.getSession(this.user, this.host, this.port);
        session.setPassword(password);
        readOutputFlag = true;
    }
    /**
     * Metoda zwracajaca adres hosta
     * @return - adres hosta
     */
    public String getHost(){ return host; }
    /**
     * Metoda zwracajaca nazwe uzytkownika
     * @return - nazwa uzytkownika
     */
    public String getUser(){ return user; }
    /**
     * Metoda zwracajaca haslo uzytkownika
     * @return - haslo uzytkownika
     */
    public String getPassword(){ return password; }
    /**
     * Metoda zwracajaca port polaczenia SSH
     * @return - port polaczenia SSH
     */
    public int getPort(){ return port; }
    /**
     * Metoda zwracajaca obiekt sesji SSH
     * @return - obiekt sesji SSH
     */
    public Session getSession(){ return session; }
    /**
     * Metoda zwracajaca czas oczekiwania na polaczenie
     * @return - czas oczekiwania na polaczenie
     */
    public int getTimeout(){ return  timeout; }
    /**
     * Metoda zwracajaca obiekt kanalu SSH
     * @return - obiekt kanalu SSH
     */
    public Channel getChannel() { return channel; }
    /**
     * Metoda zwracajaca bufor danych strumienia
     * @return - bufor danych strumienia
     */
    public ByteArrayOutputStream getByteArrayOutputStream(){ return byteArrayOutputStream; }
    /**
     * Metoda ustawiajaca brak sprawdzania klucza hosta
     */
    public void setNoKeyChecking(){ this.getSession().setConfig("StrictHostKeyChecking", "no"); }
    /**
     * Metoda otwierajaca kanal typu "shell"
     * @param timeout - czas oczekiwania na otworzenie kanalu
     * @param kindOfShell - rodzaj shell'a, okresla czy po polaczeniu ma istniec mozliwosc wysylania komend w konsoli(I) (z klawiatury),
     *                      czy za pomoca dedykowanej metody (II): "shell" - opcja pierwsza, "dowolny_ciag_znakow" - opcja druga
     * @throws JSchException
     */
    public void setShellChannel(int timeout, String kindOfShell) throws JSchException{
        if(channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
        channel = this.getSession().openChannel("shell");
        if(kindOfShell.equals("shell") || kindOfShell.equals("Shell")) {
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
        }else{
            try {
                printStreamOutput = new PrintStream(channel.getOutputStream());
                inputStream = channel.getInputStream();
            }catch (IOException | NullPointerException e){
                System.out.println("Nastąpił niespodziewany błąd podczas operacji na strumieniu wyjściowym (setShellCommand) " + e);
            }
        }
        if(timeout > 0) {
            channel.connect(timeout);
        }else{
            channel.connect();
        }
    }
    /**
     * Metoda otwierajaca kanal typu "exec"
     * @param timeout - czas oczekiwania na otworzenie kanalu
     * @param command - komenda do wykonania
     * @param outputType - okreslenie typu strumienia wyjsciowego: out - System.out, buf - zapisanie do bufora klasy
     * @throws JSchException
     */
    public void setExecChannel(int timeout, String command, String outputType) throws JSchException{
        channel = this.getSession().openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null);
        if(outputType.equals("out")) {
            channel.setOutputStream(System.out);
        }else if(outputType.equals("buf")){
            channel.setOutputStream(byteArrayOutputStream);
        }else{
            channel.setOutputStream(System.out);
        }
        channel.connect(timeout);
    }
    /**
     * Metoda umozliwiajaca wysylanie wielu komend dla polaczenia typu "shell" (dostepna jedynie przy wybraniu II opcpji podczas tworzenia kanalu)
     * @param commandBufor - bufor z komendami
     * @param sleepTime - czas pomiedzy kolejnymi, wykonywanymi komendami
     * @param saveTobufor - true/false: true - dane zapisaywane do bufora (ktory mozna potem wykorzystac do zapisywania w pliku)
     *                    false - dane wyswietlane na konsoli
     */
    public void sendCommand(List<String> commandBufor, long sleepTime, boolean saveTobufor){
        try {
            if(printStreamOutput != null) {
                for (String command : commandBufor) {
                    Thread.sleep(sleepTime);
                    printStreamOutput.println(command);
                }
                printStreamOutput.flush();
                readOutput(saveTobufor);
            }else{
                System.out.println("Nie można wysłać komendy, jesli zostało wbrane wpisywanie ręczne");
            }
        }catch (InterruptedException e){
            System.out.println("Nastapił niespodziewany błąd podczas wysyłania komendy związany prawdopodbnie z Thread.sleep: " + e);
        }
    }

    /**
     * Metoda odczytujaca dane wejsciowe
     * @param saveTobufor - true/false: true - dane zapisaywane do bufora (ktory mozna potem wykorzystac do zapisywania w pliku)
     *                    false - dane wyswietlane na konsoli
     */
    private void readOutput(boolean saveTobufor){
        byte[] buffer = new byte[1024];
        try{
            String line;
            while (readOutputFlag){
                while (inputStream.available() > 0) {
                    int i = inputStream.read(buffer, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    line = new String(buffer, 0, i);
                    if(saveTobufor){
                        byteArrayOutputStream.write(line.getBytes());
                    }else {
                        System.out.println(line);
                    }
                }
                if (channel.isClosed()){
                    break;
                }
                Thread.sleep(1000);
            }
        }catch(Exception e){
            System.out.println("Nastąpił niespodziewany błąd podczas odczytu danych (readOutput): "+ e);
        }

    }
    /**
     * Metoda nawiazujaca polaczenie SSH
     * @throws JSchException
     */
    public void connect() throws JSchException{
        System.out.println(ExtendedLog.getLog("Próba nawiązania połączenia..."));
        session.connect(timeout);
        if(session.isConnected()) {
            System.out.println(ExtendedLog.getLog("Połaczenie z hostem " + host + " zostało nawiązane."));
        }else{
            System.out.println(ExtendedLog.getLog("Połaczenie z hostem " + host + " nie zostało nawiązane."));
        }
    }
    /**
     * Metoda zamykajaca polaczenie
     */
    public void close(){
        readOutputFlag = false;
        if(this.getChannel() != null && this.getChannel().isConnected()){
            this.getChannel().disconnect();
            System.out.println(ExtendedLog.getLog("Kanał połączeniowy hosta " + host + " zostal zamknięty."));
        }
        if(this.getSession() != null && this.getSession().isConnected()){
            this.getSession().disconnect();
            System.out.println(ExtendedLog.getLog("Sesja połączeniowa hosta " + host + " została zamknięta."));
        }
    }
}
