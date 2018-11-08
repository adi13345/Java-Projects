import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

/**
 * Created by Adrian Szymowiat on 19.01.2018.
 */
public class MainFrame extends JFrame implements WindowListener, ActionListener {
    private String title = "Connected to \"Szkoła Językowa\"";
    private int frame_width = 800;
    private int frame_height = 500;
    private JPanel panel;
    private static JMenuBar menubar;
    private static JMenu info;
    private static JMenuItem help;
    private JLabel PESEL_label;
    private JLabel delete_language_label;
    private JLabel level_label;
    private JLabel name_language_label;
    private JTextField PESEL_textfield;
    private JTextField delete_language_textfield;
    private JTextField level_textfield;
    private JTextField name_language_textfield;
    private JButton check_lectors_button;
    private JButton delete_language_button;
    private JButton add_language_button;
    private JButton check_language_button;
    private JTable result_table;
    private JScrollPane scrollPane;
    private JButton clear_table_button;
    private JButton close_aplication_button;
    private DefaultTableModel model = new DefaultTableModel();

    private final static String check_lectors = "SELECT \"Lektorzy\".PESEL,\"Lektorzy\".\"imie\", \"Lektorzy\".\"nazwisko\", \"Jezyki\".\"nazwa\"\n" +
            "FROM \"Lektorzy\" JOIN \"Jezyki_lektor\" ON \"Lektorzy\".PESEL = \"Jezyki_lektor\".PESEL JOIN \"Jezyki\"\n" +
            "ON \"Jezyki\".\"id_jezyk\" = \"Jezyki_lektor\".\"id_jezyk\"";
    private final static String check_capability_room = "SELECT MAX(\"pojemnosc\") AS \"pojemnosc\" FROM \"Sale\"";
    private final static String check_languages_levels = "SELECT \"Jezyki\".\"nazwa\" AS jezyk,\"Poziomy\".\"nazwa\" AS poziom\n" +
            "FROM \"Jezyki\" JOIN \"Jezyki_poziomy\" ON \"Jezyki\".\"id_jezyk\" = \"Jezyki_poziomy\".\"id_jezyk\" JOIN \"Poziomy\"\n" +
            "ON \"Poziomy\".\"id_poziom\" = \"Jezyki_poziomy\".\"id_poziom\"";
    private final static String select_levels = "SELECT * FROM \"Poziomy\"";
    private final static String select_languages = "SELECT * FROM \"Jezyki\"";
    private final static String select_levels_and_languages_bridge_table = "SELECT * FROM \"Jezyki_poziomy\"";
    private final static String count_levels = "SELECT COUNT(*) FROM \"Poziomy\"";
    private final static String count_languages = "SELECT COUNT(*) FROM \"Jezyki\"";
    /**
     * Kontruktor klas okna glownego aplikacji
     */
    public MainFrame(){
        this.setTitle(title);
        panel= new JPanel();//stworzenie obiektu panelu
        panel.setLayout(null);//wylaczenie siatki
        panel.setPreferredSize(new Dimension(frame_width, frame_height));//ustawienie rozmiarow panelu
        this.getContentPane().add(panel);//dodanie panelu do ramki
        this.pack();//dopasowanie rozmiarow
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        this.addWindowListener(this);

        help = new JMenuItem("Pomoc");//stworzenie obiektu typu JMenuItem
        help.addActionListener(this);//dodanie nasluchiwania na zdarzenie
        info = new JMenu("Informacje");//stworzenie obiektu typu JMenu
        info.add(help);//dodanie opcji "pomoc" do zakladki "informacje"
        menubar = new JMenuBar();//stworzenie obiektu typu JMenuBar
        menubar.add(info);//dodanie zakladki "informacje" do paska narzedzi
        this.setJMenuBar(menubar);//dodanie paska narzedzi do okna

        check_lectors_button = new JButton("Check Lectors");
        check_lectors_button.setBounds((frame_width)/20,(int)(frame_height*0.75)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        check_lectors_button.addActionListener(this);
        panel.add(check_lectors_button);

        delete_language_button = new JButton("Delete");
        delete_language_button.setBounds((frame_width*6)/20,(int)(frame_height*0.75)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        delete_language_button.addActionListener(this);
        panel.add(delete_language_button);

        add_language_button = new JButton("Add");
        add_language_button.setBounds((frame_width*11)/20,(int)(frame_height*0.75)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        add_language_button.addActionListener(this);
        panel.add(add_language_button);

        check_language_button = new JButton("Show");
        check_language_button.setBounds((int)(frame_width*16.25)/20,(int)(frame_height*0.75)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        check_language_button.addActionListener(this);
        panel.add(check_language_button);

        PESEL_label = new JLabel("Lector's PESEL:");
        PESEL_label.setBounds((frame_width)/20,(int)((frame_height*0.3)/2)-15,(frame_width*3)/20,(int)(frame_height*0.15)/5);
        panel.add(PESEL_label);


        delete_language_label = new JLabel("Language to delete:");
        delete_language_label.setBounds((frame_width*6)/20,(int)((frame_height*0.3)/2)-15,(frame_width*3)/20,(int)(frame_height*0.15)/5);
        panel.add(delete_language_label);

        level_label = new JLabel("Level:");
        level_label.setBounds((frame_width*11)/20,(int)((frame_height*0.3)/2)-15,(frame_width*3)/20,(int)(frame_height*0.15)/5);
        panel.add(level_label);

        name_language_label = new JLabel("Language to add:");
        name_language_label.setBounds((int)(frame_width*16.25)/20,(int)((frame_height*0.3)/2)-15,(frame_width*3)/20,(int)(frame_height*0.15)/5);
        panel.add(name_language_label);

        PESEL_textfield = new JTextField();
        PESEL_textfield.setBounds((frame_width)/20,(int)(frame_height*0.3)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        panel.add(PESEL_textfield);

        delete_language_textfield = new JTextField();
        delete_language_textfield.setBounds((frame_width*6)/20,(int)(frame_height*0.3)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        panel.add(delete_language_textfield);

        level_textfield = new JTextField();
        level_textfield.setBounds((frame_width*11)/20,(int)(frame_height*0.3)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        panel.add(level_textfield);

        name_language_textfield = new JTextField();
        name_language_textfield.setBounds((int)(frame_width*16.25)/20,(int)(frame_height*0.3)/2,(frame_width*3)/20,(int)(frame_height*0.25)/5);
        panel.add(name_language_textfield);

        result_table = new JTable();
        result_table.setModel(model);
        result_table.setRowHeight(30);
        result_table.setBounds(frame_width/20,(int)(frame_height*0.9)/2,(int)(frame_width*18.25)/20,(frame_height*2)/5);
        result_table.setGridColor(Color.white);
        panel.add(result_table);

        clear_table_button = new JButton("Clear");
        clear_table_button.setBounds((frame_width*2)/20,(int)(frame_height*4.5)/5,(frame_width*2)/20,(int)(frame_height*0.25)/5);
        clear_table_button.addActionListener(this);
        panel.add(clear_table_button);

        close_aplication_button = new JButton("Close");
        close_aplication_button.setBounds((int)(frame_width*16.25)/20,(int)(frame_height*4.5)/5,(frame_width*2)/20,(int)(frame_height*0.25)/5);
        close_aplication_button.addActionListener(this);
        panel.add(close_aplication_button);

    }
    private void close(){
        this.dispose();
        Main.mainConnection.close();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == close_aplication_button){
            close();
        }else if (source == help){//wyswietlenie informacji o przyciskach - pomoc
            String help_strings = "Pole tekstowe Lector's PESEL: należy wpisać PESEL lektora, którego dane chcemy wyświetlić, w przypadku nie wpisania nic zostaną wyświetlone dane wszysktich lektorów.\n" +
                    "Pole tekstowe Language to delete: należy wpisać jezyk i poziom, który chcemy usunąć, format: jezyk poziom.\n" +
                    "Pole tekstowe Level: należy wpisać poziom języka, który chcemy dodać.\n" +
                    "Pole tekstowe Language: należy wpisąc nazwę języka, który chcemy dodać.\n" +
                    "Przycisk Check Lectors: sprawdzenie dostępnych lektorów, jakich języków mogą uczyć\n" +
                    "Przycisk Create: pozwala na usunięcie języka.\n" +
                    "Przycisk Add: pozwala na zatwierdzenie dodanie języka.\n" +
                    "Przycisk Show: pozwala wyświetlić dostępne jezyki i ich poziomy.\n" +
                    "Przycisk Clear: wyczyszczenie widoku tabel.\n" +
                    "Przycisk Close: zamknięcie aplikacji.\n";
            JOptionPane.showMessageDialog(null,help_strings);//pomoc dotycząca komponentow i opis
        }else if(source == check_lectors_button){
            chechLectors(check_lectors);
        }else if(source == clear_table_button){
            model.setRowCount(0);
        }else if(source == check_language_button){
            try{
                checkLanguages(check_languages_levels);
                System.out.println(checkExistingOfLangAndLev(true,"Angielski ","B2"));
            }catch (SQLException sql_err){

            }
        }else if(source == add_language_button){
            try {
                addLanguages();
            }catch(SQLIntegrityConstraintViolationException sql_err){
                System.out.println("Podany jezyk już istnieje w bazie lub nic nie zostało wpisane");
                JOptionPane.showMessageDialog(null,"Podany jezyk już istnieje w bazie danych lub nic nie zostało wpisane");
            }catch (SQLException e1) {
                e1.printStackTrace();
            }
        }else if (source == delete_language_button){
            String[] bufor;//jezyk poziom
            bufor = delete_language_textfield.getText().split(" ");
            try {
                deleteLanguage(bufor[0],bufor[1]);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null,"Błąd podczas próby usunięcia języka");
                e1.printStackTrace();
            }catch (ArrayIndexOutOfBoundsException array_error){
                JOptionPane.showMessageDialog(null,"Należy wpisać jezyk i poziom oddzielone spacją!");
            }
        }
    }
    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}

    private void reconnect(){
        try{
            System.out.println("Rozpoczeto laczenie z baza danych...");
            Class.forName(GetConfig.JDBC_DRIVER);
            MainConnection.connection = DriverManager.getConnection(GetConfig.DB_URL,MainConnection.login,MainConnection.password);
            System.out.println("Polaczono z baza danych!");
    }catch(ClassNotFoundException er) {
        System.out.println("Blad podczas nawiazywania polaczenia: " + er);
    }catch(SQLException sql_error){
            System.out.println("Napotkano niespodziewny blad SQL: " +sql_error);
    }catch(Exception e){
        System.out.println("Napotkano niespodziewny blad podczas proby nawiazania polaczenia z baza danych: " +e);
    }
    }
    private void chechLectors(String command){
        try {
            if(MainConnection.connection.isClosed()){
                reconnect();
            }
            model.setRowCount(0);
            String lector_pesel = PESEL_textfield.getText();
            String sql_command = null;
            if(lector_pesel.equals("")){
                sql_command = command;
                System.out.println("TAK:"+sql_command);
            }else {
                sql_command = command +" WHERE \"Lektorzy\".PESEL ='"+lector_pesel+"'";
                System.out.println("NIE"+sql_command);
            }
            System.out.println("Wyslanie kwerendy: " + sql_command +";");
            MainConnection.statement = MainConnection.connection.createStatement();
            MainConnection.statement.setQueryTimeout(10);
            MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, sql_command);
            Object[] columns = {"PESEL:", "Imie:","Nazwisko:","Jezyk:"};
            model.setColumnIdentifiers(columns);
            ArrayList<String> list = new ArrayList<>();
            model.addRow(columns);
            while (MainConnection.resultSet.next()) {
                String pesel = MainConnection.resultSet.getString("PESEL");
                String imie = MainConnection.resultSet.getString("imie");
                String nazwisko = MainConnection.resultSet.getString("nazwisko");
                String nazwa = MainConnection.resultSet.getString("nazwa");
                list.add(pesel);
                list.add(imie);
                list.add(nazwisko);
                list.add(nazwa);
                model.addRow(list.toArray());
                list.clear();
                /*
                System.out.print("id_jezyk: " + pesel);
                System.out.print(", imie: " + imie);
                System.out.print(", nazwisko: " + nazwisko);
                System.out.print(", nazwa: " + nazwa);
                System.out.print("\n");
                */
            }
            result_table.setModel(model);
            System.out.println("Pobrano dane dotyczące lektorów");
        }catch (SQLException e){
            System.out.println("Blad metody testujacej kwerende SQL - SQLException: " +e);
        }catch (Exception er){
            System.out.println("Blad metody testujacej kwerende SQL - nieznany blad: " +er);
        }
    }

    /**
     * Metoda dodajaca jezyk do bazy danych
     */
    private void addLanguages() throws SQLException {
        boolean level_exists_in_database = false;
        boolean language_exists_in_database = false;
        String command;
        String Language_ID;
        String Level_ID;
        String language_name = name_language_textfield.getText();
        String language_level = level_textfield.getText();
       // if(checkExistingOfLangAndLev(true,language_name,language_name) && checkExistingOfLangAndLev(false,language_name,language_name)) {
            if (MainConnection.connection.isClosed()) {
                reconnect();
            }
            MainConnection.statement = MainConnection.connection.createStatement();
            MainConnection.statement.setQueryTimeout(10);
            MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, select_levels);
            while (MainConnection.resultSet.next()) {
                if (MainConnection.resultSet.getString(2).equals(language_level)) {
                    level_exists_in_database = true;
                    break;
                }
            }
            //System.out.println(level_exists_in_database);
            MainConnection.statement = MainConnection.connection.createStatement();
            MainConnection.statement.setQueryTimeout(10);
            MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, select_languages);
            while (MainConnection.resultSet.next()) {
                if (MainConnection.resultSet.getString(2).equals(language_name)) {
                    language_exists_in_database = true;
                    break;
                }
            }
            //System.out.println(language_exists_in_database);
            if (language_exists_in_database == true && level_exists_in_database == true) {
                Language_ID = getID(select_languages, count_languages, true, true);
                Level_ID = getID(select_levels, count_levels, false, true);
                command = "INSERT INTO \"Jezyki_poziomy\" VALUES('" + Level_ID + "',NULL,'" + Language_ID + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                MainConnection.connection.commit();
            } else if (language_exists_in_database == true && level_exists_in_database == false) {
                Language_ID = getID(select_languages, count_languages, true, true);
                Level_ID = getID(select_levels, count_levels, false, false);
                command = "INSERT INTO \"Poziomy\" VALUES('" + Level_ID + "','" + language_level + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                command = "INSERT INTO \"Jezyki_poziomy\" VALUES('" + Level_ID + "',NULL,'" + Language_ID + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                MainConnection.connection.commit();
            } else if (language_exists_in_database == false && level_exists_in_database == true) {
                Language_ID = getID(select_languages, count_languages, true, false);
                Level_ID = getID(select_levels, count_levels, false, true);
                command = "INSERT INTO \"Jezyki\" VALUES('" + Language_ID + "','" + language_name + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                command = "INSERT INTO \"Jezyki_poziomy\" VALUES('" + Level_ID + "',NULL,'" + Language_ID + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                MainConnection.connection.commit();
            } else {
                Language_ID = getID(select_languages, count_languages, true, false);
                Level_ID = getID(select_levels, count_levels, false, false);
                command = "INSERT INTO \"Jezyki\" VALUES('" + Language_ID + "','" + language_name + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                command = "INSERT INTO \"Poziomy\" VALUES('" + Level_ID + "','" + language_level + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                command = "INSERT INTO \"Jezyki_poziomy\" VALUES('" + Level_ID + "',NULL,'" + Language_ID + "')";
                System.out.println(command);
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.statement.executeUpdate(command);
                MainConnection.connection.commit();
            }
       // }//else {
          //  JOptionPane.showMessageDialog(null,"Nieprawidlowa nazwa jezyka lub poziom");
      //  }
    }
    //jezyk -> true, poziom->false

    /**
     * Metoda zwracajaca id poziomu lub jezyka
     * @param command
     * @param command2
     * @param jezyk_or_poziom
     * @param exist
     * @return
     * @throws SQLException
     */
    private String getID(String command, String command2, boolean jezyk_or_poziom, boolean exist) throws SQLException {
        MainConnection.statement = MainConnection.connection.createStatement();
        MainConnection.statement.setQueryTimeout(10);
        MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, command2);
        int size_of_table = 0;
        while(MainConnection.resultSet.next()){
            size_of_table = MainConnection.resultSet.getInt(1);
        }
        String ID =null;
        String last_id = null;
        String[] bufor;
        MainConnection.statement = MainConnection.connection.createStatement();
        MainConnection.statement.setQueryTimeout(10);
        MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, command);
        int i=1;
        while(MainConnection.resultSet.next()) {
            if (i == size_of_table) {
                last_id = MainConnection.resultSet.getString(1);
            }
            i++;
        }
        if(jezyk_or_poziom){
            bufor = last_id.split("k");
        }else{
            bufor = last_id.split("m");
        }
        int id_number;
        String name;
        if(exist){
            if(jezyk_or_poziom){
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, command);
                while(MainConnection.resultSet.next()) {
                    String language = MainConnection.resultSet.getString(2);
                    if(language.equals(name_language_textfield.getText())){
                        ID = MainConnection.resultSet.getString(1);
                    }
                }
            }else{
                MainConnection.statement = MainConnection.connection.createStatement();
                MainConnection.statement.setQueryTimeout(10);
                MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, command);
                while(MainConnection.resultSet.next()) {
                    String level = MainConnection.resultSet.getString(2);
                    if(level.equals(level_textfield.getText())){
                        ID = MainConnection.resultSet.getString(1);
                    }
                }
            }
        }else{
            id_number = 1+Integer.valueOf(bufor[1]);
            String help_id = Integer.toString(id_number);
            if(jezyk_or_poziom){
                ID = "jezyk"+help_id;
            }else{
                ID = "poziom"+help_id;
            }
        }
        System.out.println(ID);
        return ID;
    }
    /**
     *
     * @param language
     * @param level
     */
    private void deleteLanguage(String language, String level) throws SQLException {
        if(checkExistingOfLangAndLev(true,language,level) && checkExistingOfLangAndLev(false,language,level)) {
            String Lang_ID = null;
            String Lev_ID = null;
            MainConnection.statement = MainConnection.connection.createStatement();
            MainConnection.statement.setQueryTimeout(10);
            MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, select_languages);
            while (MainConnection.resultSet.next()) {
                String language_bufor = MainConnection.resultSet.getString(2);
                if (language_bufor.equals(language)) {
                    Lang_ID = MainConnection.resultSet.getString(1);
                }
            }
            MainConnection.statement = MainConnection.connection.createStatement();
            MainConnection.statement.setQueryTimeout(10);
            MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, select_levels);
            while (MainConnection.resultSet.next()) {
                String level_bufor = MainConnection.resultSet.getString(2);
                if (level_bufor.equals(level)) {
                    Lev_ID = MainConnection.resultSet.getString(1);
                }
            }
            String command = "DELETE FROM \"Jezyki_poziomy\" WHERE \"id_poziom\" = '" + Lev_ID + "' AND \"id_jezyk\" = '" + Lang_ID + "'";
            System.out.println(command);
            MainConnection.statement = MainConnection.connection.createStatement();
            MainConnection.statement.setQueryTimeout(10);
            MainConnection.statement.executeUpdate(command);
            MainConnection.connection.commit();
            System.out.println("Poprawnie usunięto język i poziom");
        }else{
            JOptionPane.showMessageDialog(null,"Niepoprawny jezyk lub poziom, poprawny format to: Jezyk Poziom, np. Angielski B2");
        }
    }
    private void checkLanguages(String command) throws SQLException{
        if(MainConnection.connection.isClosed()){
            reconnect();
        }
        model.setRowCount(0);
        System.out.println("Wyslanie kwerendy: " + command +";");
        MainConnection.statement = MainConnection.connection.createStatement();
        MainConnection.statement.setQueryTimeout(10);
        MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, command);
        Object[] columns = {"Jezyk:", "Poziom:"};
        model.setColumnIdentifiers(columns);
        ArrayList<String> list = new ArrayList<>();
        model.addRow(columns);
        while (MainConnection.resultSet.next()) {
            String jezyk = MainConnection.resultSet.getString(1);
            String poziom = MainConnection.resultSet.getString(2);
            list.add(jezyk);
            list.add(poziom);
            model.addRow(list.toArray());
            list.clear();
            System.out.print("Jezyk: " + jezyk);
            System.out.print(", Poziom: " + poziom);
            System.out.print("\n");
        }
        result_table.setModel(model);
        System.out.println("Pobrano dane dotyczące jezyków");
    }
    // true - jezki, false - poziomy
    private boolean checkExistingOfLangAndLev(boolean flag, String language, String level){
        boolean isOk = false;
        String path = "";
        String text_to_check;
        if(flag){
            path = "languages.txt";
            text_to_check = language.trim();
        }else{
            path = "levels.txt";
            text_to_check = level.trim();
        }
        File file = new File(path);
        try {
            BufferedReader text = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = text.readLine()) != null) {
                if(line.indexOf(text_to_check) != -1){
                    isOk = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isOk;
    }



    /*
    private boolean checkCapabilityRoom(String command, String capability_from_textfield) throws SQLException {
        System.out.println("Wyslanie kwerendy: " + command +";");
        MainConnection.statement = MainConnection.connection.createStatement();
        MainConnection.resultSet = MainConnection.getResult(MainConnection.statement, command);
        String capability = null;
        while (MainConnection.resultSet.next()) {
            capability = MainConnection.resultSet.getString("pojemnosc");
            System.out.println(capability);
        }
        if(Integer.valueOf(amount_of_slots_textfield.getText()) > Integer.valueOf(capability)){
            return false;
        }else{
            return true;}
    }
*/
    /*try {
                if(checkCapabilityRoom(check_capability_room,amount_of_slots_textfield.getText())){
                    System.out.println("Poprawna ilosc miejsc");
                }else {
                    System.out.println("Niepoprawna ilosc miejsc");
                    JOptionPane.showMessageDialog(null,"Podana ilość miejsc przekracza możliwości największej sali!");
                }
            }catch(SQLException sql_er){

            }*/
}
