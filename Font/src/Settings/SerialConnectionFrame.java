package Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Connection.ConnectSerial;
import Action.ConsoleFrame;
import Action.ActionFrame;
import static Connection.ConnectSerial.isportcomok_;
import static MainFrame.MainFrame.*;

/**
 * Klasa do sprawdzania i podawania portu COM
 * Created by Adrian Szymowiat on 03.08.2017.
 */
public class SerialConnectionFrame extends JFrame implements ActionListener, WindowListener, KeyListener {
    /**
     * Szerokosc ramki
     */
    public static int serialconnectionframewidth_ = 550;
    /**
     * Wysokosc ramki
     */
    public static int serialconnectionframeheight_ = 140;
    /**
     * Panel ramki
     */
    private static JPanel serialconnectionframepanel_;
    /**
     * Pole tekstowe na wpisanie portu com
     */
    private static JTextField comporttexfield_;
    /**
     * Przycisk do sprawdzenia dostepnych portow COM
     */
    private static JButton checkcomportbutton_;
    /**
     * Nawiazanie polaczenia serialowego
     */
    private static JButton acceptserialconnectionbutton_;
    /**
     * Pasek narzedzi okna
     */
    private static JMenuBar menubar_;
    /**
     * Zakladka "Informacje" paska narzedzi
     */
    private static JMenu info_;
    /**
     * Opcja "Pomoc" zakladki "Informacje"
     */
    private static JMenuItem help_;
    /**
     * Konstruktor klasy SerialConnectionFrame
     * Okno wyboru portu COM
     */
    public SerialConnectionFrame(){
        // ---------->dodanie panelu i ustawienie parametrow ramki<----------
        serialconnectionframepanel_ = new JPanel();//stworzenie panelu
        serialconnectionframepanel_.setLayout(null);//wylaczenie siatki na panelu
        serialconnectionframepanel_.setPreferredSize(new Dimension(serialconnectionframewidth_,serialconnectionframeheight_));//ustawienie rozmiarow panelu
        this.getContentPane().add(serialconnectionframepanel_);//dodanie panelu do ramki
        this.pack();//dopasowanie rozmiarow
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        //---------->dodanie komponentów<----------
        Font font = new Font("Helvetica", Font.BOLD, serialconnectionframeheight_/10);//ustawienie parametrow czcionki
        comporttexfield_ = new JTextField("Podaj port COM");//stworzenie pola tekstowego
        comporttexfield_.setFont(font);//ustawienie czcionki
        comporttexfield_.setBounds(serialconnectionframewidth_/10,serialconnectionframeheight_/10,(serialconnectionframewidth_*4)/10,(int)(serialconnectionframeheight_*2.5)/10);//polozenie rozmiary pola tekstowego
        serialconnectionframepanel_.add(comporttexfield_);//dodanei przycisku do panelu
        //
        checkcomportbutton_ = new JButton("SHOW COM");//utworzenie przycsiku
        checkcomportbutton_.setBounds((serialconnectionframewidth_*7)/10,serialconnectionframeheight_/10,(serialconnectionframewidth_*2)/10,(int)(serialconnectionframeheight_*2.5)/10);//polozenie i rozmiary przycisku
        checkcomportbutton_.addActionListener(this);//dodanie nasluchiwania na zdzarzenie
        serialconnectionframepanel_.add(checkcomportbutton_);//dodanie przycisku do panelu
        //
        acceptserialconnectionbutton_ = new JButton("OK");//utworzenie przycisku
        acceptserialconnectionbutton_.setBounds((serialconnectionframewidth_*4)/10,(int)(serialconnectionframeheight_*5.5)/10,(int)(serialconnectionframewidth_*2)/10,(int)(serialconnectionframeheight_*2.5)/10);//polozenie i rozmiary przycisku
        acceptserialconnectionbutton_.addActionListener(this);//dodanei nasluchiwania na zdarzenie
        serialconnectionframepanel_.add(acceptserialconnectionbutton_);//dodanie przycisku do panelu
        //
        help_ = new JMenuItem("Pomoc");//stworzenie obiektu typu JMenuItem
        help_.addActionListener(this);//dodanie nasluchiwania na zdarzenie
        info_ = new JMenu("Informacje");//stworzenie obiektu typu JMenu
        info_.add(help_);//dodanie opcji "pomoc" do zakladki "informacje"
        menubar_ = new JMenuBar();//stworzenie obiektu typu JMenuBar
        menubar_.add(info_);//dodanie zakladki "informacje" do paska narzedzi
        //
        connectserial_ = new ConnectSerial();//utworzenie obiektu klasy ConnectSerial
        this.setJMenuBar(menubar_);//dodanie paska narzedzi do okna
        this.getRootPane().setDefaultButton(acceptserialconnectionbutton_);//ustawienie przycisku defaultowego - dzieki temu mozemy w dowolnym momenciE wcisnac enter
        this.addWindowListener(this);//dodanie nasluchiwania na zdarzenia okna
        //
    }
    /**
     * Metoda oblsugujaca zdarzenia
     * @param event - zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event){
        Object source = event.getSource();//pobranie zdarzenia
        if(source == checkcomportbutton_){//wcisniecie przycisku
            connectserial_.showCommPort();//sprawdzenie i wyswietlenie dosepnych portow COM
        }else if(source == acceptserialconnectionbutton_){
            okButtonEvent();//wywolanie nizej zdefiniowanej metody
        }else if (source == help_){//wyswietlenie informacji o przyciskach - pomoc
            String info1 = "- W polu teksotwym należy podać numer portu com np. COM3.\n";//String przechowujacy instrukcje nr 1
            String info2 = "- Przycisk SHOW COM: Wyświetlenie wszystkich dostępnych portów com.\n";//String przechowujacy instrukcje nr 2
            String info3 = "- Przycisk OK: Utworzenie połączenia serialowego.\n";//String przechowujacy instrukcje nr 3
            JOptionPane.showMessageDialog(null,info1 + info2 + info3);//pomoc dotycząca przycisków i opis
        }
    }
    /**
     * Metoda oblsugujaca wcisniecie przycisku
     * @param event
     */
    @Override
    public void keyPressed(KeyEvent event){
        int source = event.getKeyCode();//pobranie kodu wcisniete przycisku
        if(source == KeyEvent.VK_ENTER){//wcisniecie entera
            okButtonEvent();//wywolanie wyzej zdefiniowanej metody
        }
    }
    /**
     * Metoda wymuszona przez intefejs, nieuzywana
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e){}
    /**
     * Metoda wymuszona przez intefejs, nieuzywana
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {}
    /**
     * Metoda obslugujaca zdarzenia zakmniecia okna polaczenia serialowego
     */
    public void windowClosing(WindowEvent e) {
        this.dispose();//zamkniecie okna
        try{
            if(isConfigurationWindowsOn == false){
                serialmode_= false;//ustawienie flagi na false, można otworzyc inne polaczenia
            }
        }catch(Exception error){
            System.out.println("Error WINDOWCLOSING method " + error);//komunikat o bledzie na konsole intellij
        }
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowActivated(WindowEvent e) {
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowClosed(WindowEvent e) {
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowDeactivated(WindowEvent e) {
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowDeiconified(WindowEvent e) {
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowIconified(WindowEvent e) {
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowOpened(WindowEvent e) {
    }
    /**
     * Metoda wykonujaca czynnosci po wcisnieciu przycisku - obudowanie kodu w celu unikniecia powtarzalnosci
     */
    private void okButtonEvent(){
        try {
            if(isConfigurationWindowsOn == false){
                connectserial_.openSerialPort(comporttexfield_.getText().trim());//otwarcie polaczenia serialowego
                if(isportcomok_ == true) {
                    consoleframe_ = new ConsoleFrame();//stworzenie okna konsoli
                    consoleframe_.setVisible(true);//wlaczenie widocznosci okna konsoli
                    actionframe_ = new ActionFrame();//utworzenie okna do komunikacj z urzadzeniem
                    actionframe_.setVisible(true);//wlaczenie widocznosci tego okna
                    this.dispose();//zamkniecie tego okna
                    isConfigurationWindowsOn = true;//flaga na true - otwarcie okien konfiguracyjnych - nie mozna wiecej
                }
            }else{
                JOptionPane.showMessageDialog(null, "Można nawiązac połaczenie tylko z jednym urządzeniem");//wiadomosc informujaca, ze sa otwarte jakies inne okna konfiguracji
            }
        }catch(Exception e){
            System.out.println("Wystapil blad podczas polaczenia serialowego: " + e);//informacje o bledzie
        }
    }
}
