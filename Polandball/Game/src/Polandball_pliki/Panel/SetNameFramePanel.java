package Polandball_pliki.Panel;



import Polandball_pliki.Frame.LevelFrame;
import Polandball_pliki.Frame.SetNameFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static Polandball_pliki.Panel.StartPanel.setnameframe;

/**
 * Klasa reprezentujaca panel wyboru nazwy gracza
 */
public class SetNameFramePanel extends JPanel implements ActionListener, KeyListener {

    /**
     * Label zawierjący prośbę o podanie swojej nazwy
     */

    private JLabel Info;

    /**
     * Pole tekstowe, gdzie należy podać swoją nazwę
     */

    private JTextField TextField;

    /**
     * Przycisk zatwierdzający wprowadzoną nazwę
     */

    private JButton Okey;

    /**
     * Zmienna, do której będzie wczytywana nazwa gracza z textfielda
     */

    public static String nickname;

    /**
     * Obiekt klasy LevelFrame, reprezentujacy okno gry
     */

    public static LevelFrame levelframe;

    /**
     * Konstruktor okna, zawierający metode initSetNameFrame
     */

    public SetNameFramePanel() {
        initSetNameFrame();
    }

    /**
     * Metoda zawierająca parametry i komponenty okna wyboru nazwy podczas gry
     */

    private void initSetNameFrame(){

        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        Dimension size = new Dimension(400, 300);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);

        //pole, gdzie wspisujemy nazwę gracza, parametry tego okna nie są zawarte w pliku konfiguracyjnym
        TextField = new JTextField();
       // TextField.setBounds(100,100,150,30);
        add(TextField);

        //label, zawierajacy prośbę o podanie nazwy
        Info = new JLabel("Proszę podać nazwę ",JLabel.CENTER);
       // Info.setBounds(90,65,300,30);
        Info.setFont(new Font("Serif", Font.PLAIN, 25));
        add(Info);

        //przycisk zatwierdzający wpisany tekst
        Okey = new JButton("OK");
       // Okey.setBounds(260,100,70,30);
        add(Okey);
        Okey.addActionListener(this);
        //zeby dzialal enter
        TextField.addKeyListener(this);
        TextField.setFocusable(true);
        TextField.setFocusTraversalKeysEnabled(false);
    }

    /**
     * Metoda obsługująca zdarzenia okna - wczytywanie nazwy gracza
     * @param event
     */

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        //wczytanie nazwy gracza, zamknięcie okna, przejście do poziomu 1
        if(source==Okey){
            nickname = TextField.getText();//wczytanie nazwy gracza z pola tekstowego
            if(nickname.length() > 10 || nickname.indexOf(" ") != -1 || nickname.isEmpty()==true){
                //NAZWA GRACZA NIE MOZE BYC DLUZSZA NIZ 10 ZNAKOW i nie moze zawierac spacji i nie moze byc pusta
                JOptionPane.showMessageDialog(null, "Nieprawidłowa nazwa gracza");
                // ^ informacja o nieprawidlowej nazwie gracza
            }else{//jak wszystko okey to gramy
                levelframe = new LevelFrame();
                levelframe.setVisible(true);
                //this.setVisible(false);
                setnameframe.setVisible(false);
            }
        }
    }

    /**
     * Metoda obslugujaca przycisk ENTER
     * @param event
     */
    @Override
    public void keyPressed(KeyEvent event) {
        int source = event.getKeyCode();
        if(source == KeyEvent.VK_ENTER){
            nickname = TextField.getText();//wczytanie nazwy gracza z pola tekstowego
            if(nickname.length() > 10 || nickname.indexOf(" ") != -1 || nickname.isEmpty()==true){
                //NAZWA GRACZA NIE MOZE BYC DLUZSZA NIZ 10 ZNAKOW i nie moze zawierac spacji i nie moze byc pusta
                JOptionPane.showMessageDialog(null, "Nieprawidłowa nazwa gracza");
                // ^ informacja o nieprawidlowej nazwie gracza
            }else{//jak wszystko okey to gramy
                levelframe = new LevelFrame();
                levelframe.setVisible(true);
                this.setVisible(false);
            }
        }

    }

    /**
     * Metoda zwracająca tekst wpisany w textfielda
     */

    public static String GetName(){
        return nickname;
    }


    /**
     * Metoda wymuszona przez intefejs, nieuzywana
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e){

    }

    /**
     * Metoda wymuszona przez intefejs, nieuzywana
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
    /**
     * Metoda skalujaca elementy skladowe okna wyboru nazwy gracza
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight()/2);
        g.setColor(Color.RED);
        g.fillRect(0,getHeight()/2,getWidth(),getHeight()/2);
        Info.setBounds(0,getHeight()/3,getWidth(),getHeight()/6);
        TextField.setBounds(getWidth()/12,(6*getHeight())/10,getWidth()/2,getHeight()/9);
        Okey.setBounds((9*getWidth())/12,(6*getHeight())/10,getWidth()/6,getHeight()/9);
    }
}
