package Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static Settings.SetUserandPasswordFTP.serverftp_;

/**
 * Klasa tworzaca okno do zatrymania serwera FTP
 * Created by Adrian Szymowiat on 16.08.2017.
 */
public class StopFTP extends JFrame implements ActionListener{
    /**
     * Przyciks zatrzymujacy serwer ftp
     */
    private static JButton button_;
    /**
     * Kontruktor klasy StopFTP
     * Zatrzymanie serwera ftp
     */
    public StopFTP(){
        //---------->dodanie panelu<----------
        JPanel panel = new JPanel();//stworzenie obiektu panelu
        panel.setLayout(null);//wylaczenie siatki
        panel.setPreferredSize(new Dimension(250,50));//ustawienie rozmiarow panelu
        this.getContentPane().add(panel);//dodanie panelu do ramki
        //---------->okno i jego parametry<----------
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//nie można zamknąć okna
        this.pack();//dopasowanie rozmiarow
        this.setLocation(0,0);//ustawienie pozycji okna
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        //---------->przycisk<----------
        button_ = new JButton("Stop FTP Server");//utworzenie przycisku
        button_.setBounds(50,10,150,30);//ustawienie rozmiarów i położenia
        button_.addActionListener(this);
        panel.add(button_);//dodanie przycisku do panelu
    }

    /**
     * Metoda obslugujaca zdarzenia
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();//pobranie zdarzenia
        if(source == button_){
            if(serverftp_ != null) {
                serverftp_.closeFtpServer();//zamkniecie server ftp
                this.dispose();//wylaczenie okna
                JOptionPane.showMessageDialog(null,"Zamknięto serwer ftp.");//info o zamknieciu serwera
            }
        }
    }
}
