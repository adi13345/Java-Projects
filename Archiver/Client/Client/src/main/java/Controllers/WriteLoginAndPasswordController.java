package Controllers;

import Socket.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.*;

import java.util.List;

import static Socket.GetInputStream.messagefromserver;
import static java.lang.Integer.valueOf;

/**
 * Kontroller do pliku fxml LoginAndPassword
 */
public class WriteLoginAndPasswordController {

    /**
     * Zmienna trzymające dane na temat kontrolera "ramy" programu
     */
    private MainController mainController;

    /**
     * Obiekt TextField który ma przechowywać IPAddres
     */
    @FXML
    private TextField IPAdressTextField;

    /**
     * Obiekt TextField który ma przechowywać numer portu
     */
    @FXML
    private TextField PortTextField;

    /**
     * Obiekt TextField który ma przechowywać login usera
     */
    @FXML
    private TextField LoginTextField;

    /**Obiekt TextField który ma przechowywać hasło usera*/
    @FXML
    private PasswordField PasswordTextField;

    /**
     * Przycisk który po naciśnieniu ma nas  połączyć z serwerem o podanych w TextFieldach danych
     */
    @FXML
    private Button ConnectButton;

    /**
     * Po naciśnieciu przycisku aplikacja kończy swoje działanie
     */
    @FXML
    private Button CloseButton;

    /**
     * Przechowuje tekst o błędzie
     */
    private String errormessage;

    /**
     * Flaga informująca o poprawnym połączeniu
     */
    private boolean correctConnect;

    /**
     * Klasa przechowująca dane natemat połączenia i wtyczki
     */
    public static ConnectSocket connectSocket;


    private Thread getInputStream;

    /**
     * Ustawia MainControler w tej klasie
     * @param mainController
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Laduje wlaściwy pane aplikacji, po potwierdzeniu danych przez serwer
     */
    @FXML
    void loadLogIn() {
        methodConnect();
    }

    /**
     * Metoda inicjalizuje obiekty, których istnienie definiuje plik fxml do którego jest podpięty ten kontroler
     */
    @FXML
    public void initialize() {

    }


    @FXML
    void closeApplication(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Metoda sprawdzajaca poprawnosc wpisanego adresu ip i numeru portu
     * @return - true/flase, w zaleznosci, czy adresip/port poprawnie zadeklarowane
     */
    private boolean checkIPAddandPortNumber(){
        //-----------------------------------------------------------------------------------
        //------------Najpierw sprawdzany adres ip, potem numer portu------------------------
        //-----------------------------------------------------------------------------------
        if(IPAdressTextField.getText().equals("localhost")){
            if (chectPortFormat(PortTextField.getText()) == true) {
                errormessage = "Nieprawidlowy Numer Portu!";//ustawienie napisu powiadomienia
                return false;//zwrocenie false
            }//sprawdzenie, czy port miesci sie w zakresie
            else if (checkRangeofPort(PortTextField.getText()) == true) {
                errormessage = "Nieprawidlowy Numer Portu!";//ustawienie napisu powiadomienia
                return false;//zwrocenie false
            }
            else{
                return true;//zwrocenie true
            }
        }else {
            //Adres IP MUSI zawierac 3 kropki, inaczej na pewno wpisany jest zle
            if (findAndCountCharinString(IPAdressTextField.getText(), '.') != 3) {
                errormessage = "Nieprawidlowy Adres IP!";//ustawienie napisu powiadomienia
                return false;//zwrocenie false
            }//Adres IP nie moze byc dluzszy niz 15 znakow
            else if (IPAdressTextField.getText().trim().length() > 15) {
                errormessage = "Nieprawidlowy Adres IP!";//ustawienie napisu powiadomienia
                return false;//zwrocenie false
            }//Sprawdzenie, czy kazdy oktet jest liczba z przedzialu 0 - 255
            else if (checkOctetsinIpAddress(IPAdressTextField.getText()) == true) {
                errormessage = "Nieprawidlowy Adres IP!";//ustawienie napisu powiadomienia
                return false;//zwrocenie false
            }//Sprawdzenie, czy numer portu jest liczba z przedzialu 0 - 65535
            else if (chectPortFormat(PortTextField.getText()) == true) {
                errormessage = "Nieprawidlowy Numer Portu!";//ustawienie napisu powiadomienia
                return false;//zwrocenie false
            }//sprawdzenie, czy port miesci sie w zakresie
            else if (checkRangeofPort(PortTextField.getText()) == true) {
                errormessage = "Nieprawidlowy Numer Portu!";//ustawienie napisu powiadomienia
                return false;//zwrocenie false
            } else {
                return true;//zwrocenie true
            }
        }
    }

    /**
     * Metoda zliczajaca wystapienia danego znaku w napisie
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
                if (valueOf(ipaddresssbufor[i]) < 0 || valueOf(ipaddresssbufor[i]) > 255) {//sprawdzenie czy podany oktet miesci
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
     * Metoda tworzy połączenie z serwerem o danych podanych przez użytkownika
     */
    @FXML
    void methodConnect() {

        correctConnect=checkIPAddandPortNumber();
        if(correctConnect){
            //Socket
            try {
                connectSocket = new ConnectSocket(IPAdressTextField.getText(),valueOf(PortTextField.getText()));

                //getInputStream=new Thread(new GetInputStream());
                //getInputStream.start();

                OutputStream outputStream= connectSocket.getSocket().getOutputStream();
                PrintWriter pw=new PrintWriter(outputStream,true);
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(LoginTextField.getText().trim()+"%"+PasswordTextField.getText().trim());
                pw.println(stringBuilder);
                pw.flush();


                InputStream inputStream = connectSocket.getSocket().getInputStream();//pobranie odpowiedzi serwera, strumien wejsciowy danych
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//zaladowanie danych wejsciowych do obiektu klasy bufferedreader
                messagefromserver = bufferedReader.readLine();

                if(GetInputStream.getAnswear(messagefromserver)[0].equals("200")){
                        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXML/LogInWindow.fxml"));
                        Pane pane = null;
                        try {
                            pane = loader.load();
                        } catch (IOException e) {
                            System.out.println("Errror in LoadAndPassword.fxml");
                            e.printStackTrace();
                        }
                        LogInController logInController = loader.getController();
                        logInController.setMainController(mainController);
                        mainController.setScreen(pane);
                        //blok wywołujący
                        Alert alert = new Alert(Alert.AlertType.NONE, "Poprawnie zalogowano użytkownika", ButtonType.OK);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.OK) {
                            //do stuff
                        }
                }else if(GetInputStream.getAnswear(messagefromserver)[0].equals("300")){

                }else if(GetInputStream.getAnswear(messagefromserver)[0].equals("400")){

                }else {
                }
            }
            catch (IOException e ){
                System.out.println("Blad w connectServer");
                Alert alert = new Alert(Alert.AlertType.NONE, "Nie udało się poprawnie zalogować na serwerze. Sprawdź dane serwera", ButtonType.OK);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    //do stuff
                }
            }
        }
    }

}
