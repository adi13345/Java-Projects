package Settings;

import FTP.ServerFTP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Okno kreowania uzytkownika serwera ftp
 * Created by Adrian Szymowiat on 16.08.2017.
 */
public class SetUserandPasswordFTP extends JFrame implements ActionListener {
    /**
     * Szerokosc ramki
     */
    private int setuserandpasswordftpwidth_ = 450;
    /**
     * Wysokosc ramki
     */
    private int setuserandpasswordftpheight_ = 200;
    /**
     * Panel okna
     */
    private static JPanel setuserandpasswordftppanel_;
    /**
     * Label - User
     */
    private static JLabel ftplabelone_;
    /**
     * Label - Password
     */
    private static JLabel ftplabeltwo_;
    /**
     * Label - Direcotry Path
     */
    private static JLabel ftplabelthree_;
    /**
     * Pole tekstowe na wpisanie nazwy uzytkownika serwera ftp
     */
    public static JTextField ftpusertextfield_;
    /**
     * Pole tekstowe na wpisanie hasla uzytkownika
     */
    public static JTextField ftppasswordtextfield_;
    /**
     * Pole tekstowe na sciezke do katalogu serwera
     */
    public static JTextField ftppathtodirecotrytextfield_;
    /**
     * Przycisk zatwierdzajacy wpisane dane, uruchomienie serwera ftp
     */
    private static JButton okbutton_;
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
     * Obiekt klasy ServerFTP
     * Uruchomienie serwera
     */
    public static ServerFTP serverftp_;
    /**
     * Konstruktor klasy SetUserandPasswordFTP
     */
    public SetUserandPasswordFTP(){
        //---------->dodanie panelu<----------
        setuserandpasswordftppanel_ = new JPanel();//stworzenie obiektu panelu
        setuserandpasswordftppanel_.setLayout(null);//wylaczenie siatki
        setuserandpasswordftppanel_.setPreferredSize(new Dimension(setuserandpasswordftpwidth_,setuserandpasswordftpheight_));//ustawienie rozmiarow panelu
        this.getContentPane().add(setuserandpasswordftppanel_);//dodanie panelu do ramki
        //---------->okno i jego parametry<----------
        this.pack();//dopasowanie rozmiarow
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        //
        Font infofont = new Font("Helvetica", Font.BOLD, 10);//ustawienie parametrow dla czcionki tekstu labeli informacyjnych
        Font textfieldfont = new Font("Helvetica", Font.BOLD, setuserandpasswordftpheight_/10);//ustawienie parametrow dla czcionki tekstu labeli informacyjnych
        //
        ftplabelone_ = new JLabel("User: ");//utworzenie labela informacyjnego
        ftplabelone_.setFont(infofont);//ustawienie czcionki
        ftplabelone_.setBounds(setuserandpasswordftpwidth_/10,((setuserandpasswordftpheight_/10)-10),setuserandpasswordftpwidth_/8,10);//rozmiary i polozenie labela
        setuserandpasswordftppanel_.add(ftplabelone_);//dodanie labela do panelu
        //
        ftplabeltwo_ = new JLabel("Password: ");//utworzenie labela informacyjnego
        ftplabeltwo_.setFont(infofont);//ustawienie czcionki
        ftplabeltwo_.setBounds((int)(setuserandpasswordftpwidth_*5.5)/10,((setuserandpasswordftpheight_/10)-10),setuserandpasswordftpwidth_/8,10);//rozmiary i polozenie labela
        setuserandpasswordftppanel_.add(ftplabeltwo_);//dodanie labela do panelu
        //
        ftplabelthree_ = new JLabel("Directory Path: ");//utworzenie labela informacyjnego
        ftplabelthree_.setFont(infofont);//ustawienei czcionki
        ftplabelthree_.setBounds(setuserandpasswordftpwidth_/10,(((setuserandpasswordftpheight_*4)/10)-10),setuserandpasswordftpwidth_/6,10);//rozmiary i polozenie labela
        setuserandpasswordftppanel_.add(ftplabelthree_);//dodanie labela do panelu
        //
        ftpusertextfield_ = new JTextField();//utworzenie pola tekstowego
        ftpusertextfield_.setText("123");//ustawienie domyslnego usera
        ftpusertextfield_.setFont(textfieldfont);//ustawienie czcionki
        ftpusertextfield_.setBounds(setuserandpasswordftpwidth_/10,setuserandpasswordftpheight_/10,(int)(setuserandpasswordftpwidth_*3.5)/10,(setuserandpasswordftpheight_*2)/10);//ustawienie polozenia i rozmiarow pola tekstowego
        setuserandpasswordftppanel_.add(ftpusertextfield_);//dodanie pola tekstowego do panelu
        //
        ftppasswordtextfield_ = new JTextField();//utworzenie pola tekstowego
        ftppasswordtextfield_.setText("tak");//ustawienie domyslnego hasla
        ftppasswordtextfield_.setFont(textfieldfont);//ustawienie czcionki
        ftppasswordtextfield_.setBounds((int)(setuserandpasswordftpwidth_*5.5)/10,setuserandpasswordftpheight_/10,(int)(setuserandpasswordftpwidth_*3.5)/10,(setuserandpasswordftpheight_*2)/10);//ustawienie polozenia i rozmiarow pola tekstowego
        setuserandpasswordftppanel_.add(ftppasswordtextfield_);//dodanie pola tekstowego do panelu
        //
        ftppathtodirecotrytextfield_ = new JTextField();//utworzenie pola tekstowego
        ftppathtodirecotrytextfield_.setText("FTPFolder");//ustawienie domyslnego katalogu serwera
        ftppathtodirecotrytextfield_.setFont(textfieldfont);//ustawienie czcionki
        ftppathtodirecotrytextfield_.setBounds(setuserandpasswordftpwidth_/10,(setuserandpasswordftpheight_*4)/10,(int)(setuserandpasswordftpwidth_*6.5)/10,(setuserandpasswordftpheight_*2)/10);//ustawienie polozenia i rozmiarow pola tekstowego
        setuserandpasswordftppanel_.add(ftppathtodirecotrytextfield_);//dodanie pola tekstowego do panlu
        //
        okbutton_ = new JButton("Accept");//utworzenie przycisku uruchomiajacego serwer ftp
        okbutton_.setFont(textfieldfont);//ustawienie czcioki
        okbutton_.setBounds((setuserandpasswordftpwidth_*4)/10,(setuserandpasswordftpheight_*7)/10,(setuserandpasswordftpwidth_*3)/10,(setuserandpasswordftpheight_*2)/10);//ustawienie rozmiarow i polozenia przycisku
        okbutton_.addActionListener(this);//dodanie nasluchiwania na zdarzenia
        setuserandpasswordftppanel_.add(okbutton_);//dodanie przycisku do panelu
        //
        help_ = new JMenuItem("Pomoc");//stworzenie obiektu typu JMenuItem
        help_.addActionListener(this);//dodanie nasluchiwania na zdarzenie
        info_ = new JMenu("Informacje");//stworzenie obiektu typu JMenu
        info_.add(help_);//dodanie opcji "pomoc" do zakladki "informacje"
        menubar_ = new JMenuBar();//stworzenie obiektu typu JMenuBar
        menubar_.add(info_);//dodanie zakladki "informacje" do paska narzedzi
        //
        this.setJMenuBar(menubar_);//dodanie paska narzedzi do okna
    }

    /**
     * Metoda obslugujaca zdarzenia okna
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();//pobranie zdarzeniaa
        if(source == okbutton_){//wcisniecie przycisku
            if(ServerFTP.getServerStatus() == false) {//sprawdzenie czy serwer ftp zostal uruchomiony
                serverftp_ = new ServerFTP(ftpusertextfield_.getText(), ftppasswordtextfield_.getText(), ftppathtodirecotrytextfield_.getText());//utworzenie serwera ftp
                System.out.println(serverftp_.getServerStatus());//wyswietlenie informacji na konsoli czy serwer ftp zostal wlaczony
                StopFTP stopFTP = new StopFTP();//utworzenie okna do zatrzymania serwera ftp
                stopFTP.setVisible(true);//wlaczenie widocznosci okna
                this.dispose();//wylaczenie
            }else{
                JOptionPane.showMessageDialog(null,"Serwer FTP został już uruchomiony!\nNie można uruchomić kolejnego(zajety port).");//komunikat o niemoznosci uruchomienia drugiego serwera ftp(port  zajety)
            }
        }else if (source == help_){//wyswietlenie informacji o przyciskach - pomoc
            String info1 = "- Przycisk ACCEPT: Utworzenie serwera ftp.\n";//String przechowujacy instrukcje nr 1
            String info2 = "- Domyślnie został utworzony user: 123, hasło:tak, katalog serwera: FTPFolder.\n";//String przechowujacy instrukcje nr 2
            String info3 = "- W celu usunięcia usera i jego konfiguracji (nizalecane) należy zrobić to ręcznie w \\FTPProperties\\users.properties usuwając wszystkie linie z nazwą usera.\n";//String przechowujacy instrukcje nr 3
            String info4 = "- Logi serwera znajdują się w \\res\\log\\ftpd.text.\n";//String przechowujacy instrukcje nr 4
            JOptionPane.showMessageDialog(null,info1 + info2 + info3 + info4 );//pomoc dotycząca przycisków i opis
        }
    }
}
