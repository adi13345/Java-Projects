package Polandball_pliki.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;

import static Polandball_pliki.Panel.SetConnectionPanel.LoadLevel;
import static Polandball_pliki.Panel.SetNameFramePanel.levelframe;
import static Polandball_pliki.Others.GetConstans.*;
import static Polandball_pliki.Panel.PanelBoard.ChangeInfoStatus;
import Polandball_pliki.Panel.PanelBoard;
/**
 * Klasa odpowiedzialna za wyswietlenie okna ukonczenia poziomu
 */
public class NextLevelInfo extends JFrame implements ActionListener, WindowListener {

    /**
     * Label zawierajacy informacje okna
     */

    private JLabel NextLevelInfoLabel;

    /**
     * Przycisk, po wcisnieciu ktorego zostaje zaladowany nowy poziom
     */
    private JButton Okey;

    /**
     * Kopia liczby zyc z poprzedniego poziomu
     */

    public int Amountoflifescopy;

    /**
     * Kopia liczby punktow z poprzedniego poziomu
     */

    public int Amountofpointscopy;

    /**
     * Kopia liczby bomb zwyklych z poprzedniego poziomu
     */

    public int Amountofordinarybombscopy;

    /**
     * Kopia licbzy bomb zdalnych z poprzedniego poziomu
     */

    public int Amountofremotebombscopy;

    /**
     * Kopia liczby skrzydel husarskich z poprzedniego poziomu
     */

    public int Amountofhusarswingscopy;

    /**
     * Kopia liczby laserow z poprzedniego poziomu
     */

    public int Amountoflaserscopy;

    /**
     * Konstruktor okna, zawierający funkcję initNextLevelInfo()
     */

    public NextLevelInfo() {
        initNextLevelInfo();
    }

    /**
     * Metoda tworzaca okno konca gry
     */
    private void initNextLevelInfo() {

        this.setBackground(Color.WHITE);
        this.setSize(400, 200);
        this.setLayout(new GridLayout(3,1));
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.addWindowListener(this);

        //label, zawierajacy informacje o ukonczeniu poziomu
        NextLevelInfoLabel = new JLabel("Gratulacje ukonczyłeś "+Integer.toString(WhiChLevel)+" poziom!",JLabel.CENTER);
        NextLevelInfoLabel.setBounds(100, 50, 400, 50);
        NextLevelInfoLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        add(NextLevelInfoLabel);

        //Przycisk zatwierdzajacy wyslanie do serwera wyniku gracza, po tym nastepuje powrot do menu
        Okey = new JButton("OK");
        Okey.setBounds(250, 100, 100, 30);
        add(Okey);
        Okey.addActionListener(this);

    }
    /**
     * Metoda obslugujaca wcisniecie przycisku, przejscie do nastepnego poziomu
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source==Okey){
            try{
                WhiChLevel++;//zwiekszenie poziomu
                LoadLevel=true;//ustawianie na true zeby bylo dobrze
                CopyParameters();//skopiowanie parametrow, z ktorymi gracz zakonczyl poziom i kore maja byc uwzglednione w nastepnym
                this.dispose();//setVisible(false);
                levelframe.dispose();//zamkniecie okna gry
                LevelFrame.tm.stop();//zatrzymanie timera
                PanelBoard.MakeDefaultOption(WhiChLevel);//przywrocenie domyslnych parametrow dla danego poziomu
                if(LastLevel==false && LoadLevel == true) {//sprawdzenie czy to nie ostatni poziom
                    IncludeParametersFromThePreviousLevel();//aktualizacja wczytanych parametrow o parametry z poprzedniego poziomu
                    Thread.sleep(200);//uspanie watku, zeby wszystko sie na nowo przleiczylo
                    levelframe = new LevelFrame();
                    levelframe.setVisible(true);
                }
            } catch(Exception e){
                System.out.println(e+"Blad przycisku OK - klasa NextLevelInfo");
            }
        }
    }
    /**
     * Metoda wymuszona przez interfejs, nieuzywana
     * @param e
     */
    public void windowClosing(WindowEvent e) {
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
     * Metoda kopiujaca odpowiednie parametry, ktore maja byc zachowane w nastepnym poziomie
     */
    private void CopyParameters()
    {
        Amountoflifescopy = Amountoflifes;
        Amountofpointscopy = PointsForLevel + Amountofpoints + (PointsForSecond*(LevelTime+1));
        Amountofordinarybombscopy = Amountofordinarybombs;
        Amountofremotebombscopy = Amountofremotebombs;
        Amountofhusarswingscopy = Amountofhusarswings;
        Amountoflaserscopy = Amountoflasers;
    }
    /**
     * Metoda uwzgledniajaca osiagniecia (parametry) z poprzedniego poziomiu w biezacym poziomie
     * zapewnia, ze zdobyte przedmioty/punkty beda uwzglednione w nastepnym poziomie
     */
    private void IncludeParametersFromThePreviousLevel(){
        Amountoflifes = ChangeInfoStatus(Amountoflifes,Amountoflifescopy);
        Amountofpoints = ChangeInfoStatus(Amountofpoints,Amountofpointscopy);
        Amountofordinarybombs = ChangeInfoStatus(Amountofordinarybombs,Amountofordinarybombscopy);
        Amountofremotebombs = ChangeInfoStatus(Amountofremotebombs,Amountofremotebombscopy);
        Amountofhusarswings = ChangeInfoStatus(Amountofhusarswings,Amountofhusarswingscopy);
        Amountoflasers = ChangeInfoStatus(Amountoflasers,Amountoflaserscopy);
    }
}