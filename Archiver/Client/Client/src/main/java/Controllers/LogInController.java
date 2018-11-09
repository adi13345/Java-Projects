package Controllers;

import Socket.AutomaticArchiver;
import Socket.GetInputStream;
import Socket.SendFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.DefaultStringConverter;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Properties;

import static Controllers.WriteLoginAndPasswordController.connectSocket;

public class LogInController {

    @FXML
    /**
     * Przycisk do wylogowywania uzytkownika
     */
    private Button LogOutButton;

    @FXML
    /**
     * Przycisk do wyboru pliku prze klienta
     */
    private Button SelectFileClientButton;

    @FXML
    /**
     * Tablica przechowujaca dane na temat plikow planowanych byc skierowanym do realizacji, jak i godzinie o której ma zachodzic
     */
    private TableView<FileRow> tableViewClient;

    @FXML
    /**
     * Usuwam plik z listViewClienta
     */
    private Button DeleteFileClientButton;

    @FXML
    /**
     *  Przechowuje nazwy plików jakie sa na serwerze
     */
    private ListView listViewServer;

    @FXML
    /**
     * Uaktualnia liste plikow jakie sa w tej chwili dostepne po stronie serwera
     */
    private Button ShowFileServerButton;

    @FXML
    /**
     * Wysyla pliki z listy listViewClient na serwer
     */
    private Button SendFileClientButton;

    @FXML
    /**
     * Pobierz plik z serwera
     */
    private Button GetFileServerButton;

    @FXML
    /**
     * Usun plik z serwera
     */
    private Button DeleteFleServerButton;

    @FXML
    /**
     * Label informujacy o statusie przesyłania plikow przez program
     */
    private Label labelSendFile;

    @FXML
    /**
     * TextFiel przechowujacy scieszke do pobrania
     */
    private TextField pathTextField;

    @FXML
    /**
     * Pierwsza Kolumna TableView, przechowuje dane na temat nazwy pliku
     */
    private TableColumn<FileRow, String> nameFileCol;

    @FXML
    /**
     * Druga kolumna tableView, przechowuje dane na temat czasu archwizacji pliku
     */
    private TableColumn<FileRow, String> timeCol;

    /**
     * Obiekt który służy do wyboru pliku
     */
    private FileChooser fileChooser = new FileChooser();

    /**
     * Kontroler ramki głównej, metoda tego obiektu setScreen ustawia nowe okno na ramce
     */
    private MainController mainController;

    /**
     * Służy do przechowywania nazw plików jakie wybrał Klient
     */
    private ArrayList<File> fileClientList = new ArrayList<>();

    public static ArrayList<Thread> threadArrayList = new ArrayList<>();

    //tak de facto to nwm po co nam ten properties
    /**
     * Properties do....
     */
    private Properties map_files_to_auto_archive = new Properties();


    private ComboBox<String> comboBox;



    @FXML
    /**
     * Inicjalizowanie kontrollerów i elementów pliku fxml
     */
    void initialize(){

        nameFileCol.setEditable(false);
        nameFileCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameFileCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //tableViewClient.getColumns().add(nameCol);

        timeCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        //ComboBoxTableCell tak=new ComboBoxTableCell(time)
        //categoryCol.setCellFactory(comboBox);
        timeCol.setCellFactory(ComboBoxTableCell.forTableColumn(time));

        //tableViewClient.getColumns().add(categoryCol);

        Thread thread = new Thread(new AutomaticArchiver(map_files_to_auto_archive,fileClientList));//uruchomienie watku odpowiedzialnego archiwizacje
        thread.start();

        // doe propertiesa? map_files_to_auto_archive.store(output, null);
    }

    @FXML
    /**
     * Metoda obsluujaca zmiany w kolumnie Time
     */
    ///niedziala! nie lapie zmian w comboboxie chlip
    void EditTimeMethod() {
        System.out.println("Gut morning");
        int selectedIdx;
        try {
            selectedIdx = tableViewClient.getSelectionModel().getSelectedIndex();
//            map_files_to_auto_archive.remove(map_files_to_auto_archive.getProperty(fileClientList.get(selectedIdx).getPath()));
            //map_files_to_auto_archive.setProperty(tableViewClient.getColumns().get(0).getCellFactory(selectedIdx),"Empty");
            //System.out.println(tableViewClient.getSelectionModel().getSelectedItem());
            //tableViewClient.getColumns().get(0).getCellObservableValue(selectedIdx).setValue();
            System.out.println(tableViewClient.getColumns().get(0).getCellObservableValue(selectedIdx).getValue());
            System.out.println(tableViewClient.getColumns().get(1).getCellObservableValue(selectedIdx).getValue());
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }
    }


    @FXML
    /**
     *  Pozwala usuwac wybrany plik  z ListViewClient
     */
    void DeleteMethodClient() {
        int selectedIdx;
        try {
            selectedIdx = tableViewClient.getSelectionModel().getSelectedIndex();
            //System.out.println("Witam "+selectedIdx);
            map_files_to_auto_archive.remove(map_files_to_auto_archive.getProperty(fileClientList.get(selectedIdx).getPath()));
            tableViewClient.getItems().remove(selectedIdx);
            fileClientList.remove(selectedIdx);

        }catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }
        // ^ this should return players
        //listView.remove(selectedIdx);
        showFileListClient();
        System.out.println("Usunieto plik");
    }

    @FXML
    /**
     * Wylogowanie się użytkownika
     */
    void LogOut() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXML/LoginAndPassword.fxml"));
        Pane pane = null;

        OutputStream outputStream= null;
        try {
            outputStream = connectSocket.getSocket().getOutputStream();
            PrintWriter pw=new PrintWriter(outputStream,true);
            pw.println("LOGOUT");
            pw.flush();
            //odpowiedz serwara oblsluzyc
            //zamknac gniazdo i okno
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            pane = loader.load();
        }catch (IOException e){
            System.out.println("Errror in LoginAndPassword.fxml");
            e.printStackTrace();
        }
        WriteLoginAndPasswordController writeLoginAndPasswordController= loader.getController();
        writeLoginAndPasswordController.setMainController(mainController);
        mainController.setScreen(pane);


        Alert alert = new Alert(Alert.AlertType.NONE, "Poprawnie wylogowano użytkownika", ButtonType.OK);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            //do stuff
        }
        System.out.println("Wylogowanie");
    }

    @FXML
    /**
     * Otwiera FileChooser w którym możemy wybierać plik z systemu
     */
    void SelectMethodClient() {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        //OutputStream output=null;
        //output=new FileOutputStream("config.properties");

        boolean hasFileInListClient=false;
        if(selectedFile!=null) {
            for (int i = 0; i < fileClientList.size(); i++) {
                if (fileClientList.get(i).getName().equals(selectedFile.getName())) {
                    hasFileInListClient = true;
                }
            }
        }
        else{
            hasFileInListClient=false;
        }

        if(!hasFileInListClient && selectedFile!=null) {
            if (selectedFile != null) {
                tableViewClient.getItems().addAll(new FileRow(selectedFile.getName(), "12:00"));
                fileClientList.add(new File(selectedFile.getPath()));
                map_files_to_auto_archive.setProperty(selectedFile.getName(), "12:00");
                //map_files_to_auto_archive.store(output, null);
            } else {
                System.out.println("file is not valid");
            }
            showFileListClient();
            System.out.println("Wybrano plik");
        } else if(selectedFile==null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Nie wybrano żadnego pliku", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                //do stuff
            }
        }
    }

    @FXML
    /**
     * Metoda pozwala wybrać plik z tych jakie są na serwerze
     */
    void methodGetFileServer() {
        //GIVE_FILE:<filename>
        int selectedIdx = listViewServer.getSelectionModel().getSelectedIndex();
        String TESTOWY_FILE = listViewServer.getItems().get(selectedIdx).toString();//------------------------>testowy plik - ma byc pobierany z listy plikow otrzymanych od serwera<---------------------
        String SCIEZKA_GDZIE_MA_ZAPISAC_PLIK = pathTextField.getText();//"C:\\Users\\Adrian\\Downloads";//------------------------>MA BYC USTAWIANA PRZEZ UZYTKOWNIKA I LAPAC WYJATEK GDY NIE USTAWIONA<---------------------
        try {
            OutputStream outputStream = connectSocket.getSocket().getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.println("GIVE_FILE:"+TESTOWY_FILE);//wyslanie żadania do serwera
            printWriter.flush();
            InputStream inputStream = connectSocket.getSocket().getInputStream();//wczytanei strumienia wejsciowego
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań serwera
            //WIADOMOSC OD SERWERA MA POSTAC: OK<240>:Sending file, size:%999
            String answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
            String code = GetInputStream.getAnswear(answer)[0];//odczytanei kodu wiadomosci
            if(code.equals("240")){
                String[] bufor = answer.split("%");//podzielenei odpwoiedziserwera b ysie dobrac do rozmiaru pliku
                String filesize = bufor[1];//podstawienie rozmiaru pliku do zmienne
                InputStream inputstr = connectSocket.getSocket().getInputStream();//strumien danych przywiazany do gniazda
                FileOutputStream fileOutputStream = new FileOutputStream(SCIEZKA_GDZIE_MA_ZAPISAC_PLIK + "\\" + TESTOWY_FILE);//sciezka, gdzie ma byc zapisany plik
                byte[] buffer = new byte[4096];//pomocniczy bufor bajtow
                long filelenght = Long.valueOf(filesize);//dlugosc pliku w bajtach
                int read = 0;//zmienna do odczytania bajtow
                long remaining = filelenght;//zmienna pomocnicza
                while ((read = inputstr.read(buffer, 0, (int)Math.min(buffer.length,remaining))) > 0) {
                    remaining -= read;
                    fileOutputStream.write(buffer, 0, read);//wczytanie
                    fileOutputStream.flush();//wypchanie wszystkich bitow
                }
                fileOutputStream.close();//zamkniecie strumienia pliku
                System.out.println("ODEBRANO PLIK - INNY KOMUNIKAT DAC");
            }else {
                System.out.println("BLAD PRZY ODBIERANIU PLIKU - BOSLUZYC INNE PRZYPADKI");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.NONE, "Zakończono pobieranie pliku", ButtonType.OK);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            //do stuff
        }
        System.out.println("Zakonczono pobieranie pliku");
    }

    @FXML
    /**
     * Wysyla pliki z listView na serwer
     */
    void methodSendFileClient() {
        for(int i=0;i<fileClientList.size();i++) {
            labelSendFile.setText("Sending...");
            //boolean goodReceiveFile=false;
            if (fileClientList.size()>0) {
                File file = fileClientList.get(i);
                long Length = file.length();
                String fileName = file.getName();
                String fileLength = Long.toString(Length);
                String lastModification = Long.toString(file.lastModified());
                System.out.println(fileClientList.get(i).getPath());
                threadArrayList.add(new Thread(new SendFile(connectSocket.getSocket(), fileClientList.get(i).getPath(), fileLength, fileName, lastModification)));
                //sendFileThread.start();
               // goodReceiveFile=true;

            } else {

            }

        }
        for(int x=0;x<threadArrayList.size();x++){
            threadArrayList.get(x).start();
            try {
                threadArrayList.get(x).join();/// w jakis sposob synchronizuje
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        labelSendFile.setText("OK");
        showFileListClient();
        threadArrayList.clear();

    }

    /**
     * Do showFile button
     */
    @FXML
    void showFileServer() {
        try {
            listViewServer.getItems().clear();
        }catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }
        //SHOW_FILES:0
        try {
            OutputStream outputStream = connectSocket.getSocket().getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.println("SHOW_FILES:0");//wyslanie żadania do serwera
            printWriter.flush();
            InputStream inputStream = connectSocket.getSocket().getInputStream();//wczytanei strumienia wejsciowego
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań serwera
            String answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
            String code = GetInputStream.getAnswear(answer)[0];//odczytanei kodu wiadomosci
            if(code.equals("205")){
                //System.out.println(answer);
                String[] show_files_bufor = GetInputStream.getAnswear(answer)[1].split("%");//rozdzielnie wiadomosci, by wydobyc informacje o wszystkich plikach
                for(int i = 0; i <show_files_bufor.length; i++){
                    System.out.println(show_files_bufor[i]);
                    listViewServer.getItems().add(show_files_bufor[i]);
                }
            }else {
                System.out.println("BLAD PRZY ODBIERANIU INFORMACJI JAKIE MAMY PLIKI - DAC INNE OPCJE/KOMUNIKATY ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deleteFile button
     */
    @FXML
    void deleteFileServer() {
        int selectedIdx;
        //DELETE_FILES:plikjakis.txt%drugiplik.pdf  ---------------------->MUSI BYC POBIERANE Z LISTY PLIKOW JAKIE DA SERWER<----------------------
        String file_to_delete = "server_logs.txt%testowy 3.txt%testowy.txt";//--------------------->TRZEBA BEDZIE TU STOWRZYC TEN CIAG Z POBRANYCH PLIKOW Z LISTY <-------------------
        try {
            selectedIdx = listViewServer.getSelectionModel().getSelectedIndex();
            System.out.println("Witam "+selectedIdx);
            //fileClientList.remove(selectedIdx);
            file_to_delete=listViewServer.getItems().get(selectedIdx).toString();


            OutputStream outputStream = connectSocket.getSocket().getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream,true);
            printWriter.println("DELETE_FILES:"+file_to_delete);//wyslanie żadania do serwera
            printWriter.flush();
            InputStream inputStream = connectSocket.getSocket().getInputStream();//wczytanei strumienia wejsciowego
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań serwera
            String answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
            String code = GetInputStream.getAnswear(answer)[0];//odczytanei kodu wiadomosci


            listViewServer.getItems().remove(selectedIdx);

            if(code.equals("230")){
                System.out.println("POPRAWNIE USUNIETO PLIK/PLIKI - DAC COS INNEGO");
            }else {
                System.out.println("BLAD PRZY USUWANIU PLIKOW - DAC INNE OPCJE/KOMUNIKATY ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uaktualnia mainController, dzięki czemu może dojść do zmiany zawartości wyswietlanej w ramce
     * @param mainController
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    /**
     * Funkcja wyswietla nazwy plikow z array lista clienta
     */
    public void showFileListClient(){
        System.out.println("Kusofc1");
        for(int i=0;i<fileClientList.size();i++){
            System.out.println(i+"."+fileClientList.get(i).getName());
        }
    }

    /**
     * Funkcja wyswietla nazwy plikow z array lista serwera
     */
    public void showFileListServer(){
        System.out.println("Kusofc2");
        for(int i=0;i<fileClientList.size();i++){
            System.out.println(i+"."+listViewServer.getItems().get(i));
        }
    }


    ObservableList time = FXCollections.observableArrayList(
            "Empty","0:00", "0:30", "1:00", "1:30",
            "2:00", "2:30", "3:00", "3:30", "4:00",
            "4:30", "5:00", "6:30", "7:00", "7:30",
            "8:00", "8:30", "9:00", "9:30", "10:00",
            "10:30", "11:00", "11:30", "12:00",
            "12:30", "13:00", "13:30", "14:00",
            "14:30", "15:00", "15:30", "16:00",
            "16:30", "17:00", "17:30", "18:00",
            "18:30", "19:00", "19:30", "20:00",
            "20:30", "21:00", "21:30", "22:00",
            "22:30", "23:00", "23:30");

    /**
     *
     */
    public class FileRow {
        private final StringProperty name = new SimpleStringProperty();
        private final StringProperty category = new SimpleStringProperty();

        public FileRow(String name, String category) {
            setName(name);
            setCategory(category);
        }

        public final StringProperty nameProperty() {
            return this.name;
        }


        public final String getName() {
            return this.nameProperty().get();
        }


        public final void setName(final String name) {
            this.nameProperty().set(name);
        }


        public final StringProperty categoryProperty() {
            return this.category;
        }


        public final String getCategory() {
            return this.categoryProperty().get();
        }


        public final void setCategory(final String category) {
            this.categoryProperty().set(category);
        }
    }

}
