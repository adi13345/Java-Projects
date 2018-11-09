package Action;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.util.Date;

import static Connection.ConnectSocket.socket_;
import static Settings.SetIPAddAndPort.getinputstreamthread_;
import static MainFrame.MainFrame.connectserial_;
import static MainFrame.MainFrame.serialmode_;
import static MainFrame.MainFrame.socketmode_;
import static MainFrame.MainFrame.actionframe_;
import static MainFrame.MainFrame.isConfigurationWindowsOn;

/**
 * Okno konsolowe
 * Created by Adrian Szymowiat on 14.07.2017.
 */
public class ConsoleFrame extends JFrame implements ActionListener, WindowListener {
    /**
     * Znak nowej linii
     */
    public static final String newlinechar_ = "\n";
    /**
     * Szerokosc okna konsoli
     */
    private static int consoleframewidth_ = 800;
    /**
     * Wysokosc okna konsoli
     */
    private static int consoleframeheight_ = 600;
    /**
     * Panel glowny ramki
     */
    public static JPanel consolepanel_;
    /**
     * Pole tekstowe, w ktorym beda wyswietlane informacja
     */
    public static JTextArea textarea_;
    /**
     * Scroll, przewijanie pola tekstowego
     */
    private static JScrollPane scroll_;
    /**
     * Obiekt klasy DefaultCaret
     * Umozliwa ustawienie automatycznego scrolla
     */
    public static DefaultCaret caret_;
    /**
     * Konstruktor klasy ConsoleFrame
     * Tworzenie okna konsoli
     */
    public ConsoleFrame(){
        this.setSize(consoleframewidth_,consoleframeheight_);//ustawienie rozmiarow ramki
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        this.setLayout(null);//wylaczenie siatki
        this.setTitle("Console - test");//ustawienie tytulu okna
        //ustawienie okna po prawej stronie ekranu
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();//...
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();//...
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();//prostokat o wymiarach ekranu komputera
        this.setLocation(((int) rect.getMaxX() - this.getWidth()), ((int) (rect.getMaxY() - this.getHeight())/2));//ustawienie polozenia ramki
        //
        consolepanel_ = new JPanel();//stworzenie panelu konsoli
        consolepanel_.setBorder(BorderFactory.createLineBorder(Color.blue));//stworzenie obramowania dla panelu + kolor
        consolepanel_.setBackground(Color.black);//ustawienie koloru dla panelu (okna)
        consolepanel_.setBorder ( new TitledBorder( new EtchedBorder(), "Start: " + getTimeandDate()) );//tytuł ramki pokazujacy date/godzien wlaczenia konsoli
        this.setContentPane(consolepanel_);//przyklejenie panelu do ramki
        //
        textarea_ = new JTextArea();//utworzenie pola tekstowego
        textarea_.setEditable(false);//zablokowane mozliwosci edytowania pola tekstowego
        caret_ = (DefaultCaret)textarea_.getCaret();//
        caret_.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);//ustawienie automatycznego scrolla w dol (jenorazowe)
        //scroll_ = new JScrollPane(textarea_,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//utworzenie scrolla,dodanie pola tekstowego do scrollapane,ustawienie obu scrolli
        scroll_ = new JScrollPane(textarea_);//utworzenie scrolla, dodanie pola tekstowego do scrolla
        scroll_.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);//dodanie bocznego scrolla
        scroll_.setPreferredSize(new Dimension(consoleframewidth_-8,consoleframeheight_-53));//ustawienie preferowanych rozmiarow scrola
        //
        consolepanel_.add(scroll_);//dodanie scrolla do panelu
        Color color = consolepanel_.getBackground();//pobranie koloru panelu
        //
        textarea_.setBackground(color);//ustawienie koloru tla pola tekstowego
        textarea_.setForeground(Color.white);//ustawienie koloru czcionki w polu tekstowym
        textarea_.setFont(new Font("Dialog",Font.TYPE1_FONT,14));//ustawienie czcionki i jej rozmiaru
        textarea_.setLineWrap(true);//zawijanie tekstu na koncu linii
        this.addWindowListener(this);//dodanie listenera okna do ramki
    }

    /**
     * Metoda zwracajaca aktulana date i czas
     * @return - aktualna data i czas
     */
    public static String getTimeandDate(){
        return DateFormat.getDateTimeInstance().format(new Date());//pobranie aktualnej daty i czasu
    }
    /**
     * Metoda obslugujaca zdarzenia
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event){}
    /**
     * Metoda obslugujaca zdarzenia zakmniecia okna konsoli, przwracajaca ustawienia domyslne
     * @param e
     */
    public void windowClosing(WindowEvent e) {
        try{
            if (socket_ != null){
                getinputstreamthread_.stop();//zatrzymanie watku
                socket_.close();//zamkniecie gniazda jesli zostalo utworzone
            }
            if(actionframe_ !=null){
                actionframe_.dispose();//zamkniecie okna actionframe jesli zostalo utworzone
            }
            if(socketmode_ == true){
                socketmode_ = false;//ustawienie flagi na false, gdy zamkniete okno
            }
            if(serialmode_ == true){
                serialmode_ = false;//ustawienie flagi na false, gdy zamkniete okno
            }
            isConfigurationWindowsOn = false;//flaga na false - mozna otworzyc inne okna konfiguracyjne
            if(connectserial_ != null){
                connectserial_.closeSerialPort();//zamkniecie portu serialowego jesli istnieje
            }
        }catch(Exception error){
            System.out.println("Error WINDOWCLOSING method " + error);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error WINDOWCLOSING method " + error + newlinechar_ );//komunikat bledu na konsole programu
        }
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowActivated(WindowEvent e) {}
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowClosed(WindowEvent e) {}
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowDeactivated(WindowEvent e) {}
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowDeiconified(WindowEvent e) {}
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowIconified(WindowEvent e) {}
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowOpened(WindowEvent e) {}
}
