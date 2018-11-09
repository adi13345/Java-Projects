package Action;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Locale;

import jxl.write.*;
import jxl.write.Label;
import jxl.format.Colour;
import jxl.Workbook;
import jxl.WorkbookSettings;

import static Connection.ConnectSocket.socket_;
import static Connection.GetInputStream.answerbufor_;
import static Connection.GetInputStream.bufferedReader;
import static Connection.GetInputStream.getsncommandrunning_;
import static Connection.ConnectSerial.needtosavedevicename_;
import static Action.ConsoleFrame.getTimeandDate;
import static Action.ConsoleFrame.newlinechar_;
import static Action.ConsoleFrame.textarea_;
import static MainFrame.MainFrame.connectserial_;
import static MainFrame.MainFrame.consoleframe_;
import static MainFrame.MainFrame.socketmode_;
import static MainFrame.MainFrame.serialmode_;
import static MainFrame.MainFrame.isConfigurationWindowsOn;
import static MainFrame.MainFrame.connectsocket_;
import static Settings.SetIPAddAndPort.getinputstreamthread_;
import static ParsingPaths.ParsePaths.excelfilename_;

/**
 * Klasa umozliwiajaca komunikacje z urzadzeniem
 * Created by Adrian Szymowiat on 01.08.2017.
 */
public class ActionFrame extends JFrame implements ActionListener, KeyListener, WindowListener{
    /**
     * Szerkosc ramki
     */
    public static int actionframewidth_ = 500;
    /**
     * Wyskosc ramki
     */
    public static int actionframeheight_ = 400;
    /**
     * Panel ramki
     */
    private static JPanel actionframepanel_;
    /**
     * Pole tekstowe do wpisania komendy
     */
    public static JTextField getcommandtextfield_;
    /**
     * Przcysik wysłania komendy
     */
    public static JButton sendcommandbutton_;
    /**
     * Przycisk sluzacy do pobrania numerow seryjnych z urzadzenia
     */
    private static JButton getserialnumbers_;
    /**
     * Przycisku sluzacy do zapisania informacji pobranych z urzadzenia
     */
    private static JButton saveserialnumbers_;
    /**
     * Przycisk do uruchamiania bootroomu
     */
    private static JButton ctrlplusb_;
    /**
     * Przycisk do wyczyszczenia konsoli
     */
    private static JButton clearconsole_;
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
     * Wiadomosc (komenda) pobierana z pola tekstowego
     */
    private static String messagefromtextfield_;
    /**
     * Nazwa urzadzenia, z ktorym sie laczymy
     */
    public static String devicename_;
    /**
     * Flaga informujaca o rozpoczeciu pobierania adresow SN
     */
    public static boolean getsnnumbersflag_ = false;
    /**
     * Dynamiczna tablica na BoardType
     */
    private static ArrayList<String> boardtypearraylist_ = new ArrayList<>();
    /**
     * Dynamiczna tablica na BarCode
     */
    private static ArrayList<String> barcodearraylist_ = new ArrayList<>();
    /**
     * Dynamiczna tablica na Item
     */
    private static ArrayList<String> itemarraylist_ = new ArrayList<>();
    /**
     * Dynamiczna tablica na Description
     */
    private static ArrayList<String> descriptionarraylist_ = new ArrayList<>();
    /**
     * Stala nazwa szukana w buforze zawierajacym dane z komendy "display elabel" - BoardType
     */
    private static final String boardtype_ = "BoardType";
    /**
     * Stala nazwa szukana w buforze zawierajacym dane z komendy "display elabel" - BarCode
     */
    private static final String barcode_ = "BarCode";
    /**
     * Stala nazwa szukana w buforze zawierajacym dane z komendy "display elabel" - Item
     */
    private static final String description_ = "Description";
    /**
     * Stala nazwa szukana w buforze zawierajacym dane z komendy "display elabel" - Description
     */
    private static final String item_ = "Item";
    /**
     * Konstruktor klasy ActionFrame
     * Tworzenie okna do komunikacj iz urzadzeniem
     */
    public ActionFrame(){
        // ---------->dodanie panelu i ustawienie parametrow ramki<----------
        actionframepanel_ = new JPanel();//stworzenie panelu
        actionframepanel_.setLayout(null);//wylaczenie siatki na panelu
        actionframepanel_.setPreferredSize(new Dimension(actionframewidth_,actionframeheight_));//ustawienie rozmiarow panel
        this.getContentPane().add(actionframepanel_);//dodanie panelu do ramki
        this.pack();//dopasowanie rozmiarow
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        //ustawienie okna po lewej stronie ekranu
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();//...
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();//...
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();//prostokat o wymiarach ekranu komputera
        this.setLocation(40, ((int) (rect.getMaxY() - this.getHeight())/2));//ustawienie polozenia ramki
        //parametry czcionek dla komponentow
        Font textfieldfont = new Font("Helvetica", Font.BOLD, (actionframeheight_)/25);//czcionka dla pol tekstowych
        //---------->przyciski okna,pola tekstowe i ich parametry<----------
        getcommandtextfield_ = new JTextField("Write a command");//utworzenie pola tekstowego
        getcommandtextfield_.setBounds(actionframewidth_/10,(actionframeheight_*2)/25,actionframewidth_*5/10,(actionframeheight_*2)/25);//polozenie pola tekstowego
        getcommandtextfield_.setFont(textfieldfont);//utawienie czcionki dla tego pola tesktowego
        actionframepanel_.add(getcommandtextfield_);//dodanie pola tekstowego do panelu
        getcommandtextfield_.addKeyListener(this);//dodanie nasluchiwania na wcisniecie przycisku
        getcommandtextfield_.setFocusable(true);//...
        getcommandtextfield_.setFocusTraversalKeysEnabled(false);//...
        //
        sendcommandbutton_ = new JButton("Send");//utworzenie przycisku do wyslania komendy
        sendcommandbutton_.setBounds((actionframewidth_*7)/ 10, (actionframeheight_*2 )/ 25,( actionframewidth_*2 )/ 10, (actionframeheight_*2)/25);//rozmiary i polozenie przycisku do wyslania komendy
        sendcommandbutton_.setFont(textfieldfont);//ustawienie parametrow czcionki dla komponentu
        sendcommandbutton_.addActionListener(this);//wlaczenie nasluchiwania na zdarzenie
        actionframepanel_.add(sendcommandbutton_);//dodanie przycisku do panelu
        //
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Functions");//obramowanie wydzielajaca pewien obszar na ramce
        actionframepanel_.setBorder(titledBorder);//dodanie obramowanie do panelu
        //
        getserialnumbers_ = new JButton("Get SN");//utworzenie przycisku do pobieranie numwerow SN
        getserialnumbers_.setBounds((int)(actionframewidth_*0.7)/10,(actionframeheight_*6)/25,(int)(actionframewidth_*1.625)/10,(actionframeheight_*2 )/ 25);//rozmiary i polozenie przycisku do pobrania numerow seryjnych
        getserialnumbers_.addActionListener(this);//wlaczenie nasluchiwania na zdarzenie
        actionframepanel_.add(getserialnumbers_);//dodanie przycisku do panelu
        //
        saveserialnumbers_ = new JButton("Save SN");//utworzenie przycisku do zapisania numwerow SN
        saveserialnumbers_.setBounds((int)(actionframewidth_*3.025)/10,(actionframeheight_*6)/25,(int)(actionframewidth_*1.625)/10,(actionframeheight_*2 )/ 25);//rozmiary i polozenie przycisku do zapisania numerow seryjnych
        saveserialnumbers_.addActionListener(this);//wlaczenie nasluchiwania na zdarzenie
        actionframepanel_.add(saveserialnumbers_);//dodanie przycisku do panelu
        //
        ctrlplusb_ = new JButton("Ctrl+B");//utworzenie przycisku do uruchamianai bootroomu
        ctrlplusb_.setBounds((int)(actionframewidth_*5.35)/10,(actionframeheight_*6)/25,(int)(actionframewidth_*1.625)/10,(actionframeheight_*2 )/ 25);//romiary i polozenie przycisku
        ctrlplusb_.addActionListener(this);//dodanie nasluchiwwania na zdarzenie
        actionframepanel_.add(ctrlplusb_);//dodanie przycisku do panelu
        //
        clearconsole_ = new JButton("Clear");//utworzenie przycisku do wyczyszczenia konsoli
        clearconsole_.setBounds((int)(actionframewidth_*7.675)/10,(actionframeheight_*6)/25,(int)(actionframewidth_*1.625)/10,(actionframeheight_*2 )/ 25);//rozmiary i polozenie przycisku
        clearconsole_.addActionListener(this);//dodanie nasluchiwwania na zdarzenie
        actionframepanel_.add(clearconsole_);//dodanie przycisku do panelu
        //
        help_ = new JMenuItem("Pomoc");//stworzenie obiektu typu JMenuItem
        help_.addActionListener(this);//dodanie nasluchiwania na zdarzenie
        info_ = new JMenu("Informacje");//stworzenie obiektu typu JMenu
        info_.add(help_);//dodanie opcji "pomoc" do zakladki "informacje"
        menubar_ = new JMenuBar();//stworzenie obiektu typu JMenuBar
        menubar_.add(info_);//dodanie zakladki "informacje" do paska narzedzi
        //
        this.setJMenuBar(menubar_);//dodanie paska narzedzi do okna
        this.addWindowListener(this);//odanie nasluchiwania na zamkniecie okna
    }
    /**
     * Metoda obslugujaca zdarzenia okna
     * @param event - obslugiwane zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent event){
        Object source = event.getSource();//pobranie zdarzenia
        if (source == sendcommandbutton_) {//wyslanie zdarzenia
            if(socketmode_ == true) {
                messagefromtextfield_ = getcommandtextfield_.getText();//pobranie wiadomosci z pola tekstowego
                connectsocket_.makeOutputStream(messagefromtextfield_);//wyslanie wiadomosci
                getcommandtextfield_.setText("");//wyczyszczenie pola tekstowego
                //bez sensu----> textarea_.append(getTimeandDate() + ": " + messagefromtextfield_ + newlinechar_);//wypisanie komendy na konsole
            }else if (serialmode_ == true){
                messagefromtextfield_ = getcommandtextfield_.getText();//pobranie wiadomosci z pola tekstowego;
                connectserial_.makeSerialOutputStream(messagefromtextfield_.trim());//wyslanie wiadomosci
                getcommandtextfield_.setText("");//wyczyszczenie pola tekstowego
            }
        }else if (source == getserialnumbers_){
            getSerialNumbers();//wywolanie nizej zdefiniowanej metody pobierajacej nuemry seryjen z urzadzenia
        }else if (source == saveserialnumbers_){
            if(getsncommandrunning_ == false) {
                findSN(answerbufor_);//wywolanie metody znajdujacej pozadane informacje
                saveSN(barcodearraylist_, barcodearraylist_, itemarraylist_, descriptionarraylist_);//wywolanie metody zapisujacej zawartosci buforow pomocniczyc do pliku
                //------------>wyczyszczenie buforow<-----------
                answerbufor_.clear();
                boardtypearraylist_.clear();
                barcodearraylist_.clear();
                itemarraylist_.clear();
                descriptionarraylist_.clear();
                //
            }else if(getsncommandrunning_ == true){
                    JOptionPane.showMessageDialog(null, "Nie można zapisać informacji, ponieważ aplikacja jest w trakcie pobierania danych.\nProszę poczekąć na zakończenie pobierania danych.");//wiadomosc o pobieraniu danych z urzadzenia
            }
        }else if (source == ctrlplusb_){//wcisniecie przycisku wejscia do bootroomu
            if(getsncommandrunning_ == false) {
                if (socketmode_ == true) {//sprawdzenie trybu
                    connectsocket_.makeOutputStream("\u0002");//wyslanie ctrl+B
                } else if (serialmode_ == true) {//sprawdzenei trybu
                    connectserial_.makeSerialOutputStream("\u0002");//wyslanie ctrl+B
                }
            }
        }else if (source == help_){//wyswietlenie informacji o przyciskach - pomoc
            String info1 = "- W polu tekstowym należy wpisywać komendy.\n";//String przechowujacy instrukcje nr 1
            String info2 = "- Przycisk SEND: Wysłanie komendy wpisanej w polu tekstowym (można po prostu ENTEREM).\n";//String przechowujacy instrukcje nr 2
            String info3 = "- Przycisk GET SN: Pobranie  i załadowanie do bufora informacji z urządzenia dotyczących: BoardType, BarCode, Item, Description.\n";//String przechowujacy instrukcje nr 3
            String info4 = "- Przycisk SAVE SN: Zapisanie informacji pobranych przyciskiem GET SN do pliku zanjdującego się w folderze z aplikacją - poprzednei dane zapisane w tym pliku zostają utracone.\n";//String przechowujacy instrukcje nr 4
            String info5 = "- Przycisk Ctrl+B: Przycisk symulujący kombinację klawiszy ctrl+B, przydatne przy wejściu do bootroomu.\n";//String przechowujacy instrukcje nr 5
            String info6 = "- Przycisk CLEAR: Przcisk usuwający zawartość okna konsoli.\n";//string przechowujacy instrukcje nr 6
            String info7 = "- Okno konsolowe służy do wyświtelania danych otrzymanych od urządzenia (nie działa dokładnie - brak synchornizacji podczas wpisywania hasła do urządzenia + inne perełki).\n";//String przechowujacy instrukcje nr 7
            JOptionPane.showMessageDialog(null,info1 + info2 + info3 + info4 + info5 + info6 + info7);//pomoc dotycząca przycisków i opis
        }else if( source == clearconsole_){//wyczyszczenie konsoli
            textarea_.setText("");//ustawienie tekstu na "null", zeby wyczyscic konsole
        }
    }
    /**
     * Metoda oblsugujaca wcisniecie przycisku
     * @param event - wcisniety przycisk
     */
    @Override
    public void keyPressed(KeyEvent event){
        int source = event.getKeyCode();//pobranie kodu wcisniete przycisku
        if(source == KeyEvent.VK_ENTER){//wcisniecie entera
            if(socketmode_ == true) {
                messagefromtextfield_ = getcommandtextfield_.getText();//pobranie wiadomosci z pola tekstowego
                connectsocket_.makeOutputStream(messagefromtextfield_);//pobieranie wpisanego tekstu w polu tekstowym i jednoczesne wysylanie go
                getcommandtextfield_.setText("");//wyczyszczenie pola tekstowego
                //bez sensu----> textarea_.append(getTimeandDate() + ": " + messagefromtextfield_ + newlinechar_);//wypisanie komendy na konsole
            }else if (serialmode_){
                messagefromtextfield_ = getcommandtextfield_.getText();//pobranie wiadomosci z pola tekstowego
                connectserial_.makeSerialOutputStream(messagefromtextfield_.trim());//wyslanie wiadomosci
                getcommandtextfield_.setText("");//wyczyszczenie pola tekstowego
            }
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
     * Metoda sprawdzajaca nazwe urzadzenia, wykonujaca komende "display elabel" oraz zezwalajaca na obrabianie otrzymywanych informacji
     */
    public static void getSerialNumbers(){
        try {
            if(getsncommandrunning_ == false) {
                if (socketmode_ == true) {
                    connectsocket_.makeOutputStream("\n");//wyslanie znaku nowej linni w celu zidentyfikowania nazwy urzadzenia
                    devicename_ = bufferedReader.readLine();//odczytanie nazwy urzadzenia
                    getsnnumbersflag_ = true;//flaga informujaca, ze nalezy obrobic dane ze strumienia
                    connectsocket_.makeOutputStream("display elabel");//wyslanie wiadomosci "display elabel" - wyswietlenie informacji fabrycznych o elementach urzadzenia
                } else if (serialmode_ == true) {
                    connectserial_.makeSerialOutputStream("\n");
                    needtosavedevicename_ = true;// ustawienie flagi na true, odczytanie nazwy urzadzenia
                    Thread.sleep(500);
                    getsnnumbersflag_ = true;
                    connectserial_.makeSerialOutputStream("display elabel");//wyslanie wiadomosci "display elabel" - wyswietlenie informacji fabrycznych o elementach urzadzenia
                    System.out.println("Nazwa urządzenia: " + devicename_);//wyswietlenie nazwy urzadzenia na konsole
                }
            }else {
                JOptionPane.showMessageDialog(null, "Aplikacja jest w trakcie pobierania danych!\nProszę czekać!");//wiadomosc o pobieraniu danych, nie mozna drugi raz pobrac
            }
        }catch(IOException e) {//wychwycenie wyjatku
            System.out.println("Error GETSERIALNUMBERS method " + e);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error GETSERIALNUMBERS method " + e + newlinechar_);//komunikat bledu na konsole programu
        } catch(Exception e) {//wychwycenie wyjatku
            System.out.println("Error GETSERIALNUMBERS method " + e);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error GETSERIALNUMBERS method " + e + newlinechar_);//komunikat bledu na konsole programu
        }
    }
    /**
     * Metoda obslugujaca zdarzenia zakmniecia okna konsoli, przwracajaca ustawienia domyslne
     * @param e
     */
    public void windowClosing(WindowEvent e) {
        this.dispose();//zamkniecie okna
        try{
            if (socket_ != null){
                getinputstreamthread_.stop();//zatrzymanie watku
                socket_.close();//zamkniecie gniazda jesli zostalo utworzone
            }
            if(consoleframe_ !=null){
                consoleframe_.dispose();//zamkniecie okna konsoli
            }
            if(socketmode_ == true){
                socketmode_ = false;//ustawienie flagi na false, gdy zamkniete okno
            }
            if(serialmode_ == true){
                serialmode_ = false;//ustawienie flagi na false, gdy zamkniete okno
            }
            isConfigurationWindowsOn = false;//flada na false - mozna otworzyc inne okna konfiguracyjne
            if(connectserial_ != null){
                connectserial_.closeSerialPort();//zamkniecie polaczenia serialowego
            }
        }catch(Exception error){
            System.out.println("Error WINDOWCLOSING method " + error);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error WINDOWCLOSING method " + error + newlinechar_ );//komunikat bledu na konsole programu
        }
        isConfigurationWindowsOn = false;//flaga na false - mozna otworzyc inne okna konfiguracyjne
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
     * Metoda wyciagajca dane z bufora
     * @param bufor - bufor do przeanalizowania
     */
    private static void findSN(ArrayList<String> bufor){
        //przeszukajemy caly bufor
        for(int i = 0; i<bufor.size();i++) {
                if (bufor.get(i).indexOf(boardtype_)!= -1) {//sprawdzenie czy dany napis zawiera sie w danej linijce danych
                    boardtypearraylist_.add(bufor.get(i));//dodanie calego napisu pod danym indeksem w buforze do pomocniczego bufora klasy
                } else if (bufor.get(i).indexOf(barcode_) != -1) {//sprawdzenie czy dany napis zawiera sie w danej linijce danych
                    barcodearraylist_.add(bufor.get(i));//dodanie calego napisu pod danym indeksem w buforze do pomocniczego bufora klasy
                } else if (bufor.get(i).indexOf(item_) != -1) {//sprawdzenie czy dany napis zawiera sie w danej linijce danych
                    itemarraylist_.add(bufor.get(i));//dodanie calego napisu pod danym indeksem w buforze do pomocniczego bufora klasy
                }else if (bufor.get(i).indexOf(description_) != -1) {//sprawdzenie czy dany napis zawiera sie w danej linijce danych
                    descriptionarraylist_.add(bufor.get(i));//dodanie calego napisu pod danym indeksem w buforze do pomocniczego bufora klasy
                }
            }
    }
    /**
     * Metoda zapisujca otrzymane dane do arkusza kalkulacyjnego
     * @param boardtype - dynamiczna tablica boardtype
     * @param barcode - dynamiczna tablica barcode
     * @param item - dynamiczna tablica item
     * @param description - dynamiczna tablica description
     */
    private void saveSN(ArrayList<String> boardtype, ArrayList<String> barcode, ArrayList<String> item, ArrayList<String> description){
        try {
            if(answerbufor_.size() == 0){
                JOptionPane.showMessageDialog(null, "Bufor jest pusty\nNalezy najpierw pobrać dane! - przycisk Get SN");//wiadomosc o braku danych
                textarea_.append(getTimeandDate() + ": " + "Bufor jest pusty\nnNalezy najpierw pobrać dane! - przycisk Get SN" + newlinechar_);//komunikat na konsole programu
            }else {
                WritableWorkbook spreadsheet = Workbook.createWorkbook(new File(excelfilename_));//stworzenie arkusza kalkulacyjnego
                WritableSheet sheet = spreadsheet.createSheet("Arkusz", 0);//utworzenie arkusza w pliku
                //---------->ustawienia arkusza<----------
                WorkbookSettings settings = new WorkbookSettings();//konstruktorek
                settings.setLocale(new Locale("pl", "PL"));//...ustawienie jezyka
                WritableFont writablefont = new WritableFont(WritableFont.TIMES, 10);//ustawienia czcionki
                WritableCellFormat writeablecellformat = new WritableCellFormat(writablefont);//dodanie ustawien
                writeablecellformat.setBackground(Colour.WHITE);//ustawienie koloru tla
                //---------->wprowadzanie danych do arkusza<----------
                Label columnname1 = new Label(1, 0, boardtype_, writeablecellformat);//dodanie nazwy kolumny 1
                Label columnname2 = new Label(2, 0, barcode_, writeablecellformat);//dodanie nazwy kulmny 2
                Label columnname3 = new Label(3, 0, item_, writeablecellformat);//dodanie nazwy kolumny 3
                Label columnname4 = new Label(4, 0, description_, writeablecellformat);//dodanie nazwy kolumny 4
                sheet.addCell(columnname1);//dodanie obiektu do arkusza
                sheet.addCell(columnname2);//dodanie obiektu do arkusza
                sheet.addCell(columnname3);//dodanie obiektu do arkusza
                sheet.addCell(columnname4);//dodanie obiektu do arkusza
                //
                String[] helpbuforboardtype;//bufor pomocniczy na boardtype
                String[] helpbuforbarcode;//bufor pomocniczy na barcode
                String[] helpbuforitem;//bufor pomocniczy na item
                String[] helpbufordescription;//bufor pomocniczy na description
                int boardtypecounter = 0;//licznik kolumny boardtype
                int barcodecounter = 0;//licznik kolumny barcode
                int itemcounter = 0;//licznik kolumny item
                int descriptioncounter = 0;//licznik kolumny description
                for (int i = 0; i < boardtype.size(); i++) {//Label(kolumna,wiersz,"tekst",format)
                    helpbuforboardtype = boardtype.get(i).trim().split("=");//rozdzielenie napisu
                    helpbuforbarcode = barcode.get(i).trim().split("=");//rozdzielenie napisu
                    helpbuforitem = item.get(i).trim().split("=");//rozdzielenie napisu
                    helpbufordescription = description.get(i).trim().split("=");//rozdzielenie napisu
                   /* System.out.println("Rozmiar board: "+helpbuforboardtype.length);
                    System.out.println("Rozmiar bar: "+helpbuforbarcode.length);
                    System.out.println("Rozmiar item: "+helpbuforitem.length);
                    System.out.println("Rozmiar description: "+helpbufordescription.length);*/
                    if(helpbuforboardtype.length == 1 && helpbuforbarcode.length  == 1 && helpbuforitem.length == 1 && helpbufordescription.length  == 1 ) {//sprawdzenie czy jakas informacja jets pod danym slotem
                        //nothing
                    }else{
                        if(helpbuforboardtype.length > 1) {
                            try {
                                boardtypecounter++;//zwiekszenie licznika
                                Label label1 = new Label(1, boardtypecounter, helpbuforboardtype[1], writeablecellformat);//utworzenie obiektu
                                sheet.addCell(label1);//dodanie obiektu do arkusza
                            } catch (ArrayIndexOutOfBoundsException e) {//lapanie wyjatku gdy nie ma tego rekordu
                                boardtypecounter--;//zmniejszenie licznika, gdy zlapany wyjatek
                                continue;
                            }
                        }else{
                            boardtypecounter++;//zwiekszenie licznika
                        }
                        if(helpbuforbarcode.length  > 1) {
                            try {
                                barcodecounter++;//zwiekszenie licznika
                                Label label2 = new Label(2, barcodecounter, helpbuforbarcode[1], writeablecellformat);//utworzenie obiektu
                                sheet.addCell(label2);//dodanie obiektu do arkusz
                            } catch (ArrayIndexOutOfBoundsException e) {//lapanie wyjatku gdy nie ma tego rekordu
                                barcodecounter--;//zmniejszenie licznika, gdy zlapany wyjatek
                                continue;
                            }
                        }else{
                            barcodecounter++;//zwiekszenie licznika
                        }
                        if(helpbuforitem.length > 1) {
                            try {
                                itemcounter++;//zwiekszenie licznika
                                Label label3 = new Label(3, itemcounter, helpbuforitem[1], writeablecellformat);//utworzenie obiektu
                                sheet.addCell(label3);//dodanie obiektu do arkusza
                            } catch (ArrayIndexOutOfBoundsException e) {//lapanie wyjatku gdy nie ma tego rekordu
                                itemcounter--;//zmniejszenie licznika, gdy zlapany wyjatek
                                continue;
                            }
                        }else{
                            itemcounter++;//zwiekszenie licznika
                        }
                        if(helpbufordescription.length  > 1) {
                            try {
                                descriptioncounter++;//zwiekszenie licznika
                                Label label4 = new Label(4, descriptioncounter, helpbufordescription[1], writeablecellformat);//utworzenie obiektu
                                sheet.addCell(label4);//dodanie obiektu do arkusza
                            } catch (ArrayIndexOutOfBoundsException e) {
                                descriptioncounter--;//zmniejszenie licznika, gdy zlapany wyjatek
                                continue;
                            }
                        }else{
                            descriptioncounter++;//zwiekszenie licznika
                        }
                    }
                }
                //
                spreadsheet.write();//zapisanie pliku
                spreadsheet.close();//zamkniecie pliku
                JOptionPane.showMessageDialog(null, "Zapisano informacje w pliku file.xls!\nPlik znajduje się w folderze z aplikacją.\nPonowne zapisanie danych do pliku powoduje utratę obecnych.");//wiadomosc informujaca o nieprawidlowym podaniu addr ip i nr portu
                textarea_.append(getTimeandDate() + ": " + "Pomyslnie zapisane dane do pliku" + newlinechar_);//komunikat na konsole programu
                //
            }
        }catch (IOException e){
            System.out.println("Error SAVESN method " + e);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error SAVESN method " + e + newlinechar_ );//komunikat bledu na konsole programu
        }catch(WriteException we){
            System.out.println("Error SAVESN method " + we);//komunikat o bledzie na konsole intellij
            textarea_.append(getTimeandDate() + ": " + "Error SAVESN method " + we + newlinechar_ );//komunikat bledu na konsole programu
        }
    }
}
