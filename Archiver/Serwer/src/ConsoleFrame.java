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
        this.setTitle("Console");//ustawienie tytulu okna
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
        scroll_.setPreferredSize(new Dimension(consoleframewidth_-15,consoleframeheight_-60));//ustawienie preferowanych rozmiarow scrola
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
        String date = "["+DateFormat.getDateTimeInstance().format(new Date())+ "]" + ": ";//pobranie aktualnej daty i czasu, dodanie detali
        date = date.replace(",", "");//dalsza obrobka
        return date;
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
            if (Server.serverSocket != null){
                Server.serverSocket.close();//zamkniecie gniazda jesli zostalo utworzone
                this.dispose();//zamkniecie okna
                textarea_ = null;//
                Main.saveLogs(ConsoleFrame.getTimeandDate()+ "Serwer został pomyślnie wyłączony");//komunikat o zamknieciu serwera do pliku z logami
                Main.saveLogs(ConsoleFrame.getTimeandDate() + "------------------------------------>ZAMKNIECIE APLIKACJI<-------------------------------------");//komunikat do logow
                System.exit(0);//wylaczenie aplikacji
            }
        }catch(Exception error){
            System.out.println(getTimeandDate()+ "Wystąpił błąd podczas zamykania okna: " + error);//komunikat o bledzie na konsole intellij
            System.exit(1);//wylaczenie aplikacji
            if(ConsoleFrame.textarea_ != null) {//sprawdzenie czy nie zamknieto okna konsoli
                textarea_.append(getTimeandDate() + "Wystąpił błąd podczas zamykania okna: " + error + newlinechar_);//komunikat bledu na konsole programu
            }
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