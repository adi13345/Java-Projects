import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
/**
 * Klasa odpowiedzialna zap olaczenei z baza danych
 */
public class MainConnection extends JFrame implements WindowListener, ActionListener, KeyListener {
    private int frame_width = 400;
    private int frame_height = 250;
    private JPanel panel;
    private JLabel login_label;
    private JLabel password_label;
    private JPasswordField login_texfield;
    private JPasswordField password_texfield;
    private JButton close_button;
    private JButton connect_button;
    public static Connection connection;
    public static ResultSet resultSet;
    public static Statement statement;
    private MainFrame mainFrame;
    public static String login;
    public static String password;
    /**
     * Konstruktor klasy, okno polaczeniowe
     */
    public MainConnection(){
        this.setTitle("Connection to DataBase");
        panel= new JPanel();//stworzenie obiektu panelu
        panel.setLayout(null);//wylaczenie siatki
        panel.setPreferredSize(new Dimension(frame_width, frame_height));//ustawienie rozmiarow panelu
        this.getContentPane().add(panel);//dodanie panelu do ramki
        this.pack();//dopasowanie rozmiarow
        this.setLocationRelativeTo(null);//ustawienie ramki na srodku
        this.setResizable(false);//wylaczenie mozliwosci zmieniania rozmiarow okna
        this.addWindowListener(this);
        this.addKeyListener(this);

        Font infofont = new Font("Helvetica", Font.BOLD, 10);//ustawienie parametrow dla czcionki tekstu labeli informacyjnych
        Font textfieldfont = new Font("Helvetica", Font.BOLD, frame_height/10);

        login_label = new JLabel("Login:");//utworzenie labela informacyjnego
        login_label.setFont(infofont);//ustawienie czcionki
        login_label.setBounds((int)(1.5*frame_width)/10,(frame_height/9),(5*frame_width)/10,frame_height/9);//rozmiary i polozenie labela
        panel.add(login_label);//dodanie labela do panelu

        login_texfield = new JPasswordField(GetConfig.USER);
        login_texfield.setFont(infofont);
        login_texfield.setBounds((int)(1.5*frame_width)/10,((frame_height*2)/9)-8,(7*frame_width)/10,frame_height/9);
        login_texfield.setEchoChar('*');
        panel.add(login_texfield);

        password_label = new JLabel("Password:");//utworzenie labela informacyjnego
        password_label.setFont(infofont);//ustawienie czcionki
        password_label.setBounds((int)(1.5*frame_width)/10,(frame_height*3)/9,(5*frame_width)/10,frame_height/9);//rozmiary i polozenie labela
        panel.add(password_label);//dodanie labela do panelu

        password_texfield= new JPasswordField(GetConfig.PASS);
        password_texfield.setFont(infofont);
        password_texfield.setBounds((int)(1.5*frame_width)/10,((frame_height*4)/9)-8,(7*frame_width)/10,frame_height/9);
        password_texfield.setEchoChar('*');
        panel.add(password_texfield);

        connect_button = new JButton("Connect");
        connect_button.setBounds((int)(1.5*frame_width)/10,((frame_height*6)/9),(int)(2.5*frame_width)/10,frame_height/9);
        connect_button.addActionListener(this);
        panel.add(connect_button);

        close_button = new JButton("Close");
        close_button.setBounds((int)(6*frame_width)/10,((frame_height*6)/9),(int)(2.5*frame_width)/10,frame_height/9);
        close_button.addActionListener(this);
        panel.add(close_button);

        connect_button.addKeyListener(this);
        login_texfield.addKeyListener(this);
        password_texfield.addKeyListener(this);

        this.getRootPane().setDefaultButton(connect_button);
    }
    /**
     * Metoda odpowiedzialna za nawiazywaniep olaczenia z baza danych
     */
    private void connectDataBase(){
        try {
            System.out.println("Rozpoczeto laczenie z baza danych...");
            login = login_texfield.getText();
            password = password_texfield.getText();
            Class.forName(GetConfig.JDBC_DRIVER);
            connection = DriverManager.getConnection(GetConfig.DB_URL, login_texfield.getText(), password_texfield.getText());
            System.out.println("Polaczono z baza danych!");
        }catch(ClassNotFoundException er) {
            System.out.println("Blad podczas nawiazywania polaczenia: " + er);
        }catch(SQLException sql_error){
            if(sql_error.toString().indexOf("invalid username/password") != -1){
                System.out.println("Nieprawidlowy login lub haslo ");
                JOptionPane.showMessageDialog(null, "Nieprawidłowy login lub hasło");
            }else {
                System.out.println("Napotkano niespodziewny blad SQL: " +sql_error);
            }
        }catch(Exception e){
            System.out.println("Napotkano niespodziewny blad podczas proby nawiazania polaczenia z baza danych: " +e);
        }
    }
    /**
     * Metoda tworzaca tworzaca zapytania sql
     * @param command - komenda sql
     * @return - komenda sql
     */
    private String createSQLCommand(String command){
        return command;
    }

    /**
     * Metoda wysylaja zapytania i generujaca rezultat
     * @param statement - statement
     * @param sql_command - komenda sql
     * @return - obiekt typu ResultSet
     * @throws SQLException
     */
    public static ResultSet getResult(Statement statement, String sql_command) throws SQLException{
        ResultSet resultSet;
        resultSet = statement.executeQuery(sql_command);
        return resultSet;
    }
    /**
     * Metoda testujaca zapytanie
     * @throws SQLException
     */
    private void test(){
        try {
            System.out.println("Wyslanie kwerendy: " + "SELECT * FROM \"Jezyki\";");
            statement = connection.createStatement();
            String command = createSQLCommand("SELECT * FROM \"Jezyki\"");
            resultSet = getResult(statement, command);
            while (resultSet.next()) {
                String id = resultSet.getString("id_jezyk");
                String nazwa = resultSet.getString("nazwa");
                System.out.print("id_jezyk: " + id);
                System.out.print(", nazwa: " + nazwa);
                System.out.print("\n");
            }
        }catch (SQLException e){
            System.out.println("Blad metody testujacej kwerende SQL - SQLException: " +e);
        }catch (Exception er){
            System.out.println("Blad metody testujacej kwerende SQL - nieznany blad: " +er);
        }
    }

    /**
     * Metoda obslugujaca zdarzenia wcisniecia przycisku
     * @param e - zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == connect_button){
            connectDataBase();
            //test();
            if(connection != null) {
                this.dispose();
                mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null,"Nie można połączyć się z baza danych");
            }
        }else if(source == close_button){
            close();
        }
    }
    /**
     * Metoda odpowiedzialan za zamkniecie okna i wylaczenie aplikacji
     */
    public void close(){
        try {
            if(resultSet != null) {
                if (!resultSet.isClosed()) {
                    resultSet.close();
                    System.out.println("Zamknieto obiekt ResultSet");
                }
            }
            if(statement != null) {
                if (!statement.isClosed()) {
                    statement.close();
                    System.out.println("Zamknieto obiekt Statement");
                }
            }
            if(connection != null) {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Zamknieto obiekt Connection");
                }
            }
            this.dispose();
            System.exit(0);
        }catch(Exception er){
            System.out.println("Napotkano niespodziewany blad podczas zamykania okna logowania: "+er);
            System.exit(1);
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
            connectDataBase();
            this.dispose();
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        }
    }
    /**
     * Metoda odpowiedzialna za wcisniecie "krzyzyka" zamykajacego aplikacje
     */
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
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public Connection getConnection(){
        return connection;
    }
    public Statement getStatement(){
        return  statement;
    }
    public ResultSet getResultSet(){
        return resultSet;
    }
}
