package Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Connection.ConnectSocket;
import Connection.GetInputStream;
import Action.ActionFrame;
import Action.ConsoleFrame;

import static Action.ConsoleFrame.getTimeandDate;
import static Action.ConsoleFrame.newlinechar_;
import static Action.ConsoleFrame.textarea_;
import static MainFrame.MainFrame.actionframe_;
import static MainFrame.MainFrame.consoleframe_;
import static MainFrame.MainFrame.socketmode_;
import static MainFrame.MainFrame.isConfigurationWindowsOn;
import static MainFrame.MainFrame.connectsocket_;


/**
 * Klasa do wczytywania adresu IP i nr portu, przy wyborze polaczenia sieciowego
 * Created by Adrian Szymowiat on 28.07.2017.
 */
public class SetIPAddAndPort extends JFrame implements ActionListener, KeyListener, WindowListener{
    /**
     * Adres IP wpisywany przez użytkownika
     */
    public static String ipaddr_;
    /**
     * Numer portu wpisywany przez użytkownika
     */
    public static int portnr_;
    /**
     * Szerokosc okna ramki
     */
    private static int setipandportframewidth_ = 600;
    /**
     * Wysokosc okna ramki
     */
    private static int setipandportframeheight_ = 200;
    /**
     * Panel okna
     */
    private static JPanel setipaddandportpanel_;
    /**
     * Label zawierajacy prosbe o podanie
     */
    private static JLabel setipandportquestionlabel_;
    /**
     * Label zawierajacy informacje, gdzie nalezy wpisac adres ip
     */
    private static JLabel setipandportlabelinfoone_;
    /**
     * Label zawierajacy informacje, gdzie wpisac numer portu
     */
    private static JLabel setipandportlabelinfotwo_;
    /**
     * Pole tekstowe do wpisania adres ip
     */
    private static JTextField setipandporttextfieldone_;
    /**
     * Pole tekstowe do wpisania numeru portu
     */
    private static JTextField setipandporttextfieldtwo_;
    /**
     * Przycisk rozpoczynajacy polaczenie
     */
    private static JButton setipandportokbutton_;
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
     * Watek, ktory ciagle odbiera dane ze strumienia gniazda sieciowego
     */
    public static Thread getinputstreamthread_;
    /**
     * Napis wyswietlany w oknie podczas nieprawidlowo podanego adres ip i numeru portu
     */
    private static String errormessage_;
    /**
     * Kontruktor klasy SetIPAddAndPort
     * Tworzenie okna wyboru adresu IP i nr portu
     */
    public SetIPAddAndPort() {
        // ---------->dodanie panelu i ustawienie parametrow ramki<----------
        setipaddandportpanel_ = new JPanel();//stworzenie panelu
        setipaddandportpanel_.setLayout(null);//wylaczenie siatki na panelu
        setipaddandportpanel_.setPreferredSize(new Dimension(setipandportframewidth_,setipandportframeheight_));//ustawienei rozmiarow panelu
        this.getContentPane().add(setipaddandportpanel_);//dodanie panelu do ramki
        this.pack();//dopasowanie rozmiarow
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        //---------->dodanie komponentów<----------
        Font questionfont = new Font("Helvetica", Font.BOLD, setipandportframeheight_ / 10 );//ustawienie parametrow dla czcionki tekstu labela
        setipandportquestionlabel_ = new JLabel("Proszę podać adres IP i numer portu", SwingConstants.CENTER);//stworzenie labela,ustawienie napisu i wysrodkowanie tekstu
        setipandportquestionlabel_.setFont(questionfont);//ustawienie czcionki dla tego labela
        setipandportquestionlabel_.setBounds(0, setipandportframeheight_ / 15, setipandportframewidth_ , (setipandportframeheight_ * 2) / 10);//ustawienie rozmiarow i polozenia dla tego labela
        setipaddandportpanel_.add(setipandportquestionlabel_);//dodanie labela do panelu
        //
        Font infofont = new Font("Helvetica", Font.BOLD, 10);//ustawienie parametrow dla czcionki tekstu labeli informacyjnych
        Font textfieldfont = new Font("Helvetica", Font.BOLD, setipandportframeheight_ / 10);//ustawienie parametrow dla pola tekstowego
        //
        setipandportlabelinfoone_ = new JLabel();//stworzenie labela
        setipandportlabelinfoone_.setFont(infofont);//ustawienie czcionki dla tego labela
        setipandportlabelinfoone_.setText("Adres IP:");//ustawienie tekstu dla tego labela
        setipandportlabelinfoone_.setHorizontalTextPosition(JLabel.LEFT);//ustawienie tekstu na maksa z lewej
        setipandportlabelinfoone_.setBounds(setipandportframewidth_/ 10, (setipandportframeheight_ * 3) / 10, (setipandportframewidth_ * 2) / 10, setipandportframeheight_ / 10);//ustawienie rozmiarow i polozenia dla tego labela
        setipaddandportpanel_.add(setipandportlabelinfoone_);//dodanie labela do panelu
        //
        setipandportlabelinfotwo_ = new JLabel();//stworzenie labela
        setipandportlabelinfotwo_.setFont(infofont);//ustawienie czcionki dla tego labela
        setipandportlabelinfotwo_.setText("Numer Portu:");//ustawienie tekstu dla tego labela
        setipandportlabelinfotwo_.setHorizontalTextPosition(JLabel.LEFT);//ustawienie tekstu na maksa z lewej
        setipandportlabelinfotwo_.setBounds((int)(setipandportframewidth_ * 5.5) / 10, (setipandportframeheight_ * 3) / 10, (setipandportframewidth_ * 2) / 10, setipandportframeheight_ / 10);//ustawienie rozmiarow i polozenia dla tego labela
        setipaddandportpanel_.add(setipandportlabelinfotwo_);//dodanie labela do panelu
        //
        setipandporttextfieldone_ = new JTextField();//utworzenie pola tekstowego
        setipandporttextfieldone_.setBounds(setipandportframewidth_ / 10, (setipandportframeheight_ * 4) / 10, ((int)(setipandportframewidth_ * 3.5) )/ 10, (setipandportframeheight_ * 2) / 10);//ustawienie rozmiarow i polozenia dla tego textfielda
        setipandporttextfieldone_.setFont(textfieldfont);//ustawienie czccionki dla pola tekstowego
        setipaddandportpanel_.add(setipandporttextfieldone_);//dodanie pola tekstowego do panelu
        //
        setipandporttextfieldtwo_ = new JTextField();//utworzenie pola tekstowego
        setipandporttextfieldtwo_.setBounds((int)(setipandportframewidth_ * 5.5) / 10, (setipandportframeheight_ * 4) / 10, ((int)(setipandportframewidth_ * 3.5)) / 10, (setipandportframeheight_ * 2) / 10);//ustawienie rozmiarow i polozenia dla tego textfielda
        setipandporttextfieldtwo_.setFont(textfieldfont);//ustawienie czccionki dla pola tekstowego
        setipaddandportpanel_.add(setipandporttextfieldtwo_);//dodanie pola tekstowego do panelu
        //
        setipandportokbutton_ = new JButton("OK");//utworzenie przycisku
        setipandportokbutton_.setBounds((setipandportframewidth_*4)/10,(int)(setipandportframeheight_*6.5)/10,(setipandportframewidth_*2)/10,(setipandportframeheight_*2)/10);//ustawienie rozmiarow i polozenia dla tego przycisku
        setipandportokbutton_.addActionListener(this);//dodanie litenera do przycisku
        setipaddandportpanel_.add(setipandportokbutton_);//dodanie przycisku do panelu
        setipandportokbutton_.addKeyListener(this);//dodanie nasluchiwania na wcisniecie przycisku
        setipandportokbutton_.setFocusable(true);//mozemy przechodzic tabulatorem
        setipandportokbutton_.setFocusTraversalKeysEnabled(false);//...
        //
        help_ = new JMenuItem("Pomoc");//stworzenie obiektu typu JMenuItem
        help_.addActionListener(this);//dodanie nasluchiwania na zdarzenie
        info_ = new JMenu("Informacje");//stworzenie obiektu typu JMenu
        info_.add(help_);//dodanie opcji "pomoc" do zakladki "informacje"
        menubar_ = new JMenuBar();//stworzenie obiektu typu JMenuBar
        menubar_.add(info_);//dodanie zakladki "informacje" do paska narzedzi
        //
        this.setJMenuBar(menubar_);//dodanie paska narzedzi do okna
        this.getRootPane().setDefaultButton(setipandportokbutton_);//ustawienie przycisku defaultowego - dzieki temu mozemy w dowolnym momencie wcisnac enter
        this.addWindowListener(this);//dodanie nasluchiwania na zdarzenia okna
    }
    /**
     * Metoda oblsugujaca zdarzenia
     * @param event - zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event){
        Object source = event.getSource();//pobranie zdarzenia
        if(source == setipandportokbutton_){//wcisniecie przycisku
            okButtonEvent();//wywolanie nizej zdefiniowanej metody
        }else if (source == help_){//wyswietlenie informacji o przyciskach - pomoc
            String info1 = "- W polach tekstowych nalezy podać odpowiednio adres ip punktu docelowego i numer portu.\n";//String przechowujacy instrukcje nr 1
            String info2 = "- Przycisk OK: Utworzenie połączenia. W przypadku niepojawienia się od razu okien konfiguracyjnych należy odczekać kilka sekund.\n";//String przechowujacy instrukcje nr 2
            JOptionPane.showMessageDialog(null,info1 + info2 );//pomoc dotycząca przycisków i opis
        }
    }
    /**
     * Metoda wykonujaca czynnosci po wcisnieciu przycisku - obudowanie kodu w celu unikniecia powtarzalnosci
     */
    private void okButtonEvent(){
        try {
            if(checkIPAddandPortNumber() == true) {//sprawdzenie czy adres ip i numer portu zostaly wpisane poprawnie
                if(isConfigurationWindowsOn == false) {//sprawdzenie czy jakies okna do komunikacji z urzadzeniem nie zostaly otwarte
                    connectsocket_ = new ConnectSocket(ipaddr_, portnr_);//utworzenie obiektu klasy Connect, nawiazanie polaczenie
                    if(connectsocket_.showConnectionStatus()==false) {//sprawdzenie czy nawiazano polaczenie
                        consoleframe_ = new ConsoleFrame();//utworzenie obiektu klasy Consoleframe
                        consoleframe_.setVisible(true);//wlaczenie widocznosci ramki
                        getinputstreamthread_ = new Thread(new GetInputStream());//stworzenie watku wyswietlajacego dane ze strumienia wejsciowego
                        getinputstreamthread_.start();//uruchomienie watku, ciagle odbieranie danych z gniazda
                        actionframe_ = new ActionFrame();//stworzenie okna do komunikacji z urzadzeniem
                        actionframe_.setVisible(true);//wlaczenie widocznosci okna action frame
                        this.dispose();//zamkniecie tego okna
                        isConfigurationWindowsOn = true;//flaga na true - otwarcie okien konfiguracyjnych - nie mozna wiecej
                        textarea_.append(getTimeandDate() + ": " + "Utworzono gniazdo: " + "Adres IP: " + ipaddr_ + "; " + "Numer portu: " + Integer.toString(portnr_) + newlinechar_);//komunikat o utworzeniu gniazda na konsole
                    }else{
                        JOptionPane.showMessageDialog(null, "Nie można nawiązać połączenia");//wiadomosc informujaca, ze nie mozna nawiazac polaczenia
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Można nawiązac połaczenie tylko z jednym urządzeniem");//wiadomosc informujaca, ze sa otwarte jakies inne okna konfiguracji
                }
            }else{
                JOptionPane.showMessageDialog(null, errormessage_);//wiadomosc informujaca o nieprawidlowym podaniu addr ip i nr portu
            }
        }catch(Exception e){
            System.out.println("Blad przycisku setipandportokbutton_: "+e);//wyswietlenie bledu na konsole
        }
    }
    /**
     * Metoda sprawdzajaca poprawnosc wpisanego adresu ip i numeru portu
     * @return - true/flase, w zaleznosci, czy adresip/port poprawnie zadeklarowane
     */
    private boolean checkIPAddandPortNumber(){
        //-----------------------------------------------------------------------------------
        //------------Najpierw sprawdzany adres ip, potem numer portu------------------------
        //-----------------------------------------------------------------------------------
        //Adres IP MUSI zawierac 3 kropki, inaczej na pewno wpisany jest zle
        if(findAndCountCharinString(setipandporttextfieldone_.getText(),'.') != 3){
            errormessage_ = "Nieprawidlowy Adres IP!";//ustawienie napisu powiadomienia
            return false;//zwrocenie false
        }//Adres IP nie moze byc dluzszy niz 15 znakow
        else if(setipandporttextfieldone_.getText().trim().length() > 15){
            errormessage_ = "Nieprawidlowy Adres IP!";//ustawienie napisu powiadomienia
            return false;//zwrocenie false
        }//Sprawdzenie, czy kazdy oktet jest liczba z przedzialu 0 - 255
        else if (checkOctetsinIpAddress(setipandporttextfieldone_.getText()) == true){
            errormessage_ = "Nieprawidlowy Adres IP!";//ustawienie napisu powiadomienia
            return false;//zwrocenie false
        }//Sprawdzenie, czy numer portu jest liczba z przedzialu 0 - 65535
        else if (chectPortFormat(setipandporttextfieldtwo_.getText()) == true){
            errormessage_ = "Nieprawidlowy Numer Portu!";//ustawienie napisu powiadomienia
            return false;//zwrocenie false
        }//sprawdzenie, czy port miesci sie w zakresie
        else if(checkRangeofPort(setipandporttextfieldtwo_.getText()) == true){
            errormessage_ = "Nieprawidlowy Numer Portu!";//ustawienie napisu powiadomienia
            return false;//zwrocenie false
        }
        else {
            ipaddr_ = setipandporttextfieldone_.getText();//pobranie adresu ip z pola tekstowego
            portnr_ = Integer.valueOf(setipandporttextfieldtwo_.getText());//pobranie nr portu i konwersja na inta
            return true;//zwrocenie true
        }
    }

    /**
     * Metetoda zliczajaca wystapienia danego znaku w napisie
     * @param inscription - napis, w ktorym szukamy danego znaku
     * @param character - szukany znak
     */
    private int findAndCountCharinString(String inscription, char character){
        int amountofchar = 0;//ilosc wystapienia danego znaku
        for(int i = 0; i < inscription.length();i++){//sprawdzanie kazdego znaku napisau
            if(inscription.charAt(i)==character){//sprawdzenie, czy dany znak to szukany znal
                amountofchar++;//zwiekszenie ilosci wystapien znaku
            }
        }
        return amountofchar;//zwrocenie ilosi wystapien znaku
    }

    /**
     * Metoda sprawdzajaca, czy poszczegolne oktety adresu ip zostaly poprawnie zdefiniowane
     * @param ipaddress - adres ip, ktory ma zostac sprawdzony
     * @return - true/false, w zaleznosci, czy adres ip poprawny
     */
    private boolean checkOctetsinIpAddress(String ipaddress){
        String ipaddresssbufor[];//bufor do rozdzielenia oktetów adresu ip
        ipaddress.trim();//usuniecie zbednych spacji z napisu
        ipaddress = ipaddress.replace('.','%');//zamiana kropek na procenty w napisie adresu ip, bo z kropka split nie pykal
        ipaddresssbufor = ipaddress.split("%");//rozdzielenie adresu na 4 oktety
        boolean badipaddress = false;//flaga informujaca czy adres ip zostal poprawnie zdefiniowany
        try {
            for (int i = 0; i < ipaddresssbufor.length; i++) {//sprawdzenie kazdego oktetu
                if (Integer.valueOf(ipaddresssbufor[i]) < 0 || Integer.valueOf(ipaddresssbufor[i]) > 255) {//sprawdzenie czy podany oktet miesci
                    badipaddress = true;//flaga na true, zly adres ip
                    break;//wyjscie z petli, nie ma sensu dalej sprawdzac
                }else{
                    badipaddress = false;//nie ma bledow w adresi, sprawdzamy dalej
                }
            }
        }catch (NumberFormatException e){//lapanie wyjatku o zlym formacie, np. jak chcemy litere na int skonwertowac
            badipaddress = true;//flaga na true, zly adres ip
        }
        return badipaddress;//zwrocenie adresu
    }

    /**
     * Metoda swpradzajaca, czy podany numer portu jest poprawny
     * @param port - numer portu (napis)
     * @return - true - port bledny;false - port poprawny, w zaleznosi, czy port zwiera cyfry 0 -9
     */
    private boolean chectPortFormat(String port){
        try{
            Integer.parseInt(port);//proba konwersji stringa na int
            return false;//jak sie udalo to zwracane falsz
        }catch (NumberFormatException e){//sprawdzenie formatu
            return true;//w przypadku zlego formatu przy konwersji zwracane true
        }
    }

    /**
     * Metoda sprawdzająca, czy podany numer portu miescie sie w zakresie dostepnych portow
     * @param port - numer portu (napis)
     * @return - true - port bledny;false - port poprawny, w zaleznosi, czy port miesci sie w zakrecie
     */
    private boolean checkRangeofPort(String port){
        if(Integer.parseInt(port) < 0 || Integer.parseInt(port) > 65535){//sprawdzenie zakresu portu
            return true;//port dobry
        }else{
            return false;//port zly
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
     * Metoda obslugujaca zdarzenia zakmniecia okna wyboru adresu ip i portu
     * @param e
     */
    public void windowClosing(WindowEvent e) {
        this.dispose();//zamkniecie okna
        try{
            if(isConfigurationWindowsOn == false) {
                socketmode_ = false;//ustawienie flagi na false, można otworzyc inne polaczenia
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
}