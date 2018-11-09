package MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Connection.ConnectSocket;
import Connection.ConnectSerial;
import Action.ActionFrame;
import Action.ConsoleFrame;
import Action.CreateScript;
import Settings.SerialConnectionFrame;
import Settings.SetIPAddAndPort;
import Settings.SetUserandPasswordFTP;

/**
 * Okno glowne programu
 * Created by Adrian Szymowiat on 14.07.2017.
 */
public class MainFrame extends JFrame implements ActionListener {
    /**
     * Szerokosc okna glownego
     */
    public static int mainframewidth_ = 530;
    /**
     * Wysokosc okna glownego
     */
    public static int mainframeheight_ = 150;
    /**
     * Przycisk do nawiazania polaczenia przez gniazdo
     */
    private static JButton connectbutton_;
    /**
     * Przycisk do nawiazania polaczenia przez kabel serialowy
     */
    private static JButton serialconnectbutton_;
    /**
     * Panel glowny okna
     */
    private static JPanel mainframepanel_;
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
     * Przycisk do tworzenia serwera ftp
     */
    private static JButton createftpserver_;
    /**
     * Przycisk do generowania configow
     */
    private static JButton createscript_;
    /**
     * Obiekt klasy Connect
     * Nawiazanie polaczenia i wysylanie wiadomosci
     */
    public static ConnectSocket connectsocket_;
    /**
     * Obiekt klasy ConnectSerial
     * Nawiazywanie polaczenia, sprawdzanie portu COM i wysylanie wiadomosci
     */
    public static ConnectSerial connectserial_;
    /**
     * Obiekt klasy SetIPAddAndPort
     * Okno wyboru adresu ip i nr portu
     */
    public static SetIPAddAndPort setipaddandport_;
    /**
     * Obiekt klasy SerialConnectionFrame
     * Okno wyboru portu COM
     */
    public static SerialConnectionFrame serialconnectionframe_;
    /**
     * Obiekt klasy ConsoleFrame
     * Tworzenie okna konsoli
     * Wykorzystywany w innych klasach
     */
    public static ConsoleFrame consoleframe_;
    /**
     * Obiekt klasy ActionFrame
     * Okno do komunikacji z urzadzeniem
     * Wykorzystywany w innych klasach
     */
    public static ActionFrame actionframe_;
    /**
     * Flaga informujaca, ze nastapilo polaczenie przez gniazdo
     */
    public static boolean socketmode_ = false;
    /**
     * Flaga informujaca, ze nastapilo polaczenie przez kabel serialowy
     */
    public static boolean serialmode_ = false;
    /**
     * Zmienna informujaca okno konsoli i okno akcji(konfiguracji) sa otwarte - mozna polaczcy sie tylko z jednym urzadzeniem jednoczesnie
     */
    public static boolean isConfigurationWindowsOn = false;
    /**
     * Metoda tworzaca okno glowne programu
     */
    public MainFrame() {
        //---------->dodanie panelu<----------
        mainframepanel_ = new JPanel();//stworzenie obiektu panelu
        mainframepanel_.setLayout(null);//wylaczenie siatki
        mainframepanel_.setPreferredSize(new Dimension(mainframewidth_,mainframeheight_));//ustawienie rozmiarow panelu
        this.getContentPane().add(mainframepanel_);//dodanie panelu do ramki
        //---------->okno i jego parametry<----------
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//zamkniecie okna przy wcisnieciu "x"
        this.pack();//dopasowanie rozmiarow
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        //---------->rozmiar czcionki dla komponentów<----------
        Font font = new Font("Helvetica", Font.BOLD, 12);//parametry tekstu
        //---------->przyciski okna, pole tekstowe i ich parametry<----------
        connectbutton_ = new JButton("Connect - Socket");//stowrzenie przycisku nawiazywania polaczenia
        connectbutton_.setBounds(mainframewidth_ / 10, (mainframeheight_ *2)/ 10, (mainframewidth_*3) / 10,(int)( mainframeheight_*2.5 )/ 10);//ustawienie parametrow przycisku
        connectbutton_.setFont(font);//ustawienie parametrow czcionki dla komponentu
        connectbutton_.addActionListener(this);//wlaczenie nasluchiwania na zdarzenie dla przycisku
        mainframepanel_.add(connectbutton_);//dodanie przycisku do okna(panelu)
        //
        serialconnectbutton_ = new JButton("Connect - Serial");//stworzenie obiektu przycisku polaczenia przez kabel serialowy
        serialconnectbutton_.setBounds( (mainframewidth_*6)/ 10, (mainframeheight_*2)/ 10, (mainframewidth_*3) / 10,(int)(mainframeheight_*2.5 )/ 10);//ustawienie rozmiarow przycisku polaczenia przez kabel serialowy
        serialconnectbutton_.setFont(font);//ustawienie parametrow czcionki dla komponentu
        serialconnectbutton_.addActionListener(this);//dodanie nasluchiwaania na zdarzenie
        mainframepanel_.add(serialconnectbutton_);//dodanie przycisku do panelu okna
        //
        createscript_ = new JButton("Create Config Scripts");//stworzenie przycisku do tworzenia configow
        createscript_.setBounds(mainframewidth_ / 10,(int)(mainframeheight_*5.5)/10,(mainframewidth_*3) / 10,(int)( mainframeheight_*2.5 )/ 10);//rozmiary i polozenei przycisku
        createscript_.setFont(font);//ustawienie parametrow czcionki dla komponentu
        createscript_.addActionListener(this);//dodanie nasluchiwaania na zdarzenie
        mainframepanel_.add(createscript_);//dodanie przycisku do panelu
        //
        createftpserver_ = new JButton("Create FTP Server");//utworzenie przycisku tworzenia serwera FTP
        createftpserver_.setBounds((mainframewidth_*6)/ 10,(int)(mainframeheight_*5.5)/10,(mainframewidth_*3) / 10,(int)( mainframeheight_*2.5 )/ 10);//rozmiary i polozenie przycisku
        createftpserver_.setFont(font);//ustawienei parametrow czcionki dla komponentu
        createftpserver_.addActionListener(this);//dodanie nasluchiwaania na zdarzenie
        mainframepanel_.add(createftpserver_);//dodanie przycisku do panelu
        //
        help_ = new JMenuItem("Pomoc");//stworzenie obiektu typu JMenuItem
        help_.addActionListener(this);//dodanie nasluchiwania na zdarzenie
        info_ = new JMenu("Informacje");//stworzenie obiektu typu JMenu
        info_.add(help_);//dodanie opcji "pomoc" do zakladki "informacje"
        menubar_ = new JMenuBar();//stworzenie obiektu typu JMenuBar
        menubar_.add(info_);//dodanie zakladki "informacje" do paska narzedzi
        //---------->tworzenie obiektów<----------
        connectserial_ = new ConnectSerial();//utworzenie obiektu klasy Consoleframe
        //
        this.setJMenuBar(menubar_);//dodanie paska narzedzi do okna
        this.setVisible(true);//wlaczenie widowcznosci okna
    }
    /**
     * Metoda obslugujaca zdarzenia
     * @param event - zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event){
        Object source = event.getSource();//pobranie zdarzenia
        if(source == connectbutton_){//polaczenie z danym adresem
            if(serialmode_ == false) {//sprawdzenie, czy nie jest otwarte polaczenie serialowe
                setipaddandport_ = new SetIPAddAndPort();//stworzenie obiektu klasy SetIPAddAndPort
                setipaddandport_.setVisible(true);//wlaczenie widocznosci okna
                socketmode_ = true;//ustawienie flagi na true
            }
            else{
                JOptionPane.showMessageDialog(null, "Nie można połączyć sie przez gniazdo, dopóki otwarte jest połączenie serialowe!");//wiadomosc informujaca ze jest otwarte polaczenie serialowe
            }
        }
        else if (source == serialconnectbutton_){//polaczenie przez kabel serialowy
            if(socketmode_ == false) {//sprawdzenie, czy nie zostalo otwarte gniazdo
                serialconnectionframe_ = new SerialConnectionFrame();//stworzenie obiektu klasy SerialConnecionFrame
                serialconnectionframe_.setVisible(true);//wlaczenie widocznosci okna
                serialmode_ = true;//ustawienie flagi na true
            }else{
                JOptionPane.showMessageDialog(null, "Nie można połączyć sie przez kabel serialowy, dopóki jest otwarte gniazdo!");//wiadomosc informujaca ze jest otwarte gniazdo
            }
        }else if (source == help_){//wyswietlenie informacji o przyciskach - pomoc
            String info1 = "- Przycisk Connect - Socket: Połaczenie z urządzeniem poprzez gniazdo(TCP), wymagany adres sieciowy oraz numer portu.\n";//String przechowujacy instrukcje nr 1
            String info2 = "- Przycisk Connect - Serial: Połączenie z urządzeniem za pomocą kabla serialowego.\n";//String przechowujacy instrukcje nr 2
            String info3 = "- Przycisk Create Config Scripts: Generowanie skryptów do urządzeń sieciowych - pliki konfiguracyjne: ringconfiguration.xml.\n";//String przechowujacy instrukcje nr 3
            String info4 = "- Przycisk Create FTP Server: Stworzenie serwera ftp.";//String przechowujacy instrukcje nr 4
            JOptionPane.showMessageDialog(null,info1 + info2 + info3 + info4);//pomoc dotycząca przycisków i opis
        }else if(source == createftpserver_){//utworzenei serwera ftp
            SetUserandPasswordFTP setUserandPasswordFTP = new SetUserandPasswordFTP();//stworzenie ramki do wczytania parametrow serwera ftp
            setUserandPasswordFTP.setVisible(true);//wlaczenie widocznosci okna
        }else if(source == createscript_){//otworzenie okna konfiguracji skryptow
            new CreateScript();
        }
    }
}
