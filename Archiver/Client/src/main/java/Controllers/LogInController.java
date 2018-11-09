package Controllers;

import Socket.AutomaticArchiver;
import Socket.CheckTime;
import Socket.GetInputStream;
import Socket.SendFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import static Controllers.WriteLoginAndPasswordController.connectSocket;
import static java.lang.Integer.valueOf;

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
     * Tablica przechowujaca dane na temat plikow planowanych do wysłania na serwer, jak i godzine o której ma zachodzic synchronizacja
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
    private static ArrayList<File> fileClientList = new ArrayList<>();

    /**
     * Przechowuje liste watkow
     */
    public static ArrayList<Thread> threadArrayList = new ArrayList<>();

    /**
     * Properties przechowujacy dane [klucz,wartosc] gdzie klucz=nazwa_pliku a wartosc=czas_wyslania_pliku na serwer
     * */
    private static Properties map_files_to_auto_archive = new Properties();

    @FXML
    /**
     * Inicjalizowanie kontrollerów i elementów pliku fxml
     */
    void initialize(){
        nameFileCol.setEditable(false);
        nameFileCol.setMinWidth(100);
        nameFileCol.setCellValueFactory(
                new PropertyValueFactory<FileRow, String>("nameFile"));
        nameFileCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameFileCol.setCellFactory(TextFieldTableCell.forTableColumn());

        //blok odpowiadajacy za edycje czasu Time w wierszu TableView
        timeCol.setMinWidth(75);
        timeCol.setCellValueFactory(
                new PropertyValueFactory<FileRow, String>("Time"));
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<FileRow, String>>() { //eventHandler
                    @Override
                    public void handle(TableColumn.CellEditEvent<FileRow, String> t) {
                        if(checkArchiveTime(t.getNewValue())){
                            ((FileRow) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setTime(t.getNewValue());



                            int selectionRow= t.getTableView().getSelectionModel().getSelectedIndex();//łapie numer wiersza

                            //łapie nameFile zaznaczanego
                            String name=t.getTableView().getColumns().get(0).getCellObservableValue(selectionRow).getValue().toString();
                            //drukuje properties ktory istniał nowy properties
                            System.out.println("Stary Properties "+name+" " +map_files_to_auto_archive.getProperty(name));
                            //usuwa properties
                            map_files_to_auto_archive.remove(name);
                            //tworzy nowy o nowej godzinie
                            map_files_to_auto_archive.setProperty(name,t.getTableView().getColumns().get(1).getCellObservableValue(selectionRow).getValue().toString());
                            System.out.println("Nowy Properties "+name+" " +map_files_to_auto_archive.getProperty(name));
                        }else{
                            Alert alert = new Alert(Alert.AlertType.NONE, "Wpisanie niepoprawnych danych czasowych", ButtonType.OK);
                            alert.showAndWait();
                            if (alert.getResult() == ButtonType.OK) {
                                ((FileRow) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                ).setTime("empty");

                                int selectionRow= t.getTableView().getSelectionModel().getSelectedIndex();//łapie numer wiersza
                                //łapie nameFile zaznaczonego
                                String name=t.getTableView().getColumns().get(0).getCellObservableValue(selectionRow).getValue().toString();
                                //drukuje properties ktory istniał nowy properties
                                System.out.println("Stary Properties "+name+" " +map_files_to_auto_archive.getProperty(name));
                                map_files_to_auto_archive.remove(name);
                                //tworzy nowy properties o czasie synchronizacji empty
                                map_files_to_auto_archive.setProperty(name,"empty");
                                System.out.println("Nowy Properties "+name+" " +"empty");
                            }
                        }
                    }
                }//koniec eventHandlera
        );

        tableViewClient.setItems(data);//wstawiam do tableView tablice danych w której będą przechowywawne rekordy
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn());

        AutomaticArchiver.flag = true;
        Thread thread = new Thread(new AutomaticArchiver(map_files_to_auto_archive,fileClientList));//uruchomienie watku odpowiedzialnego archiwizacje
        thread.start();


    }

    /**
     * Metoda sprawdzająca poprawnośc wpisanego czasu
     * @param time wpisany czasy
     * @return zwraca prawda/falsz czy wpisany czas archiwzacji jest poprawny
     */
    public boolean checkArchiveTime(String time){
        boolean flag;//flaga informujaca, czy czas jest poprawny czy nie
        String[] bufor = time.split(":");//bufor pomocniczy
        if(time.equals("empty")){
            flag=true;
        } else if(WriteLoginAndPasswordController.findAndCountCharinString(time,':') != 1){//sprawdzenei czy jest jeden :
            flag = false;
        }else if(valueOf(bufor[0]) < 0 ||valueOf(bufor[0]) > 23){ //sprawdzenie godziny od 0 do 23
            flag = false;
        }else if(valueOf(bufor[1]) < 0 ||valueOf(bufor[1]) > 60){//sprawdzenie minut 0 - 60
            flag = false;
        }else {//jesli wszystko oki to czas git git
            flag = true;
        }
        return flag;
    }

    @FXML
    /**
     * Otwiera FileChooser w którym możemy wybierać plik z systemu
     */
    void SelectMethodClient() {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        //blok odpowiedzialny za unikniecie sytuacji dodania pliku do listy o tej samej nazwie
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
                tableViewClient.getItems().addAll(new FileRow(selectedFile.getName(), "empty"));
                fileClientList.add(new File(selectedFile.getPath()));
                map_files_to_auto_archive.setProperty(selectedFile.getName(),"empty");
                //map_files_to_auto_archive.store(output, null);
                String name = fileClientList.get(fileClientList.size()-1).getName();
                System.out.println("Nowy Properties "+name+" " +map_files_to_auto_archive.getProperty(name));
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
     *  Pozwala usuwac wybrany plik z ListViewClient
     */
    void DeleteMethodClient() {
        int selectedIdx;
        try {
            selectedIdx = tableViewClient.getSelectionModel().getSelectedIndex();
            System.out.println("selectedIdx"+selectedIdx);
            if(selectedIdx>=0 ) {
                String name = tableViewClient.getColumns().get(0).getCellObservableValue(selectedIdx).getValue().toString();
                System.out.println("Stary Properties " + name + " " + map_files_to_auto_archive.getProperty(name));
                map_files_to_auto_archive.remove(name);
                System.out.println("Nowy Properties " + name + " " + map_files_to_auto_archive.getProperty(name));

                tableViewClient.getItems().remove(selectedIdx);
                fileClientList.remove(selectedIdx);
            }
        }catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException: ");
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
            AutomaticArchiver.flag = false;//wylaczenei sprawdzania czy nalezy archiwizowac pliki
            CheckTime.checktime_flag = false;//wylaczenie licznika czasu
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

        fileClientList.clear();
        map_files_to_auto_archive.clear();

        Alert alert = new Alert(Alert.AlertType.NONE, "Poprawnie wylogowano użytkownika", ButtonType.OK);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            //do stuff
        }
        System.out.println("Wylogowanie");
    }

    @FXML
    /**
     * Metoda pozwala wybrać plik z tych jakie są na serwerze
     */
    void methodGetFileServer() {
        //GIVE_FILE:<filename>
        int selectedIdx = listViewServer.getSelectionModel().getSelectedIndex();
        System.out.println("selectedIdx:"+listViewServer.getSelectionModel().getSelectedIndex());
        String TESTOWY_FILE = null;
        String SCIEZKA_GDZIE_MA_ZAPISAC_PLIK = null;

        if(selectedIdx>=0) {
            TESTOWY_FILE = listViewServer.getItems().get(selectedIdx).toString();//------------------------>testowy plik - ma byc pobierany z listy plikow otrzymanych od serwera<--------------------
            SCIEZKA_GDZIE_MA_ZAPISAC_PLIK = pathTextField.getText();//"C:\\Users\\Adrian\\Downloads";//------------------------>MA BYC USTAWIANA PRZEZ UZYTKOWNIKA I LAPAC WYJATEK GDY NIE USTAWIONA<---------------------
            File testfile = new File(SCIEZKA_GDZIE_MA_ZAPISAC_PLIK);
            if (testfile.isDirectory() && !pathTextField.getText().isEmpty()) {
                boolean flagAlert = true;
                try {
                    changeTitleStage("Status:Getting file...");
                    OutputStream outputStream = connectSocket.getSocket().getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println("GIVE_FILE:" + TESTOWY_FILE);//wyslanie żadania do serwera
                    printWriter.flush();
                    InputStream inputStream = connectSocket.getSocket().getInputStream();//wczytanei strumienia wejsciowego
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań serwera
                    //WIADOMOSC OD SERWERA MA POSTAC: OK<240>:Sending file, size:%999
                    String answer = bufferedreader.readLine();//odczytanie wiadomosci czy mozna odtworzyc plik
                    String code = GetInputStream.getAnswear(answer)[0];//odczytanei kodu wiadomosci
                    if (code.equals("240")) {
                        String[] bufor = answer.split("%");//podzielenei odpwoiedziserwera b ysie dobrac do rozmiaru pliku
                        String filesize = bufor[1];//podstawienie rozmiaru pliku do zmienne
                        InputStream inputstr = connectSocket.getSocket().getInputStream();//strumien danych przywiazany do gniazda
                        FileOutputStream fileOutputStream = new FileOutputStream(SCIEZKA_GDZIE_MA_ZAPISAC_PLIK + "\\" + TESTOWY_FILE);//sciezka, gdzie ma byc zapisany plik
                        byte[] buffer = new byte[4096];//pomocniczy bufor bajtow
                        long filelenght = Long.valueOf(filesize);//dlugosc pliku w bajtach
                        int read = 0;//zmienna do odczytania bajtow
                        long remaining = filelenght;//zmienna pomocnicza
                        while ((read = inputstr.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                            remaining -= read;
                            fileOutputStream.write(buffer, 0, read);//wczytanie
                            fileOutputStream.flush();//wypchanie wszystkich bitow
                        }
                        fileOutputStream.close();//zamkniecie strumienia pliku
                        flagAlert = true;
                        System.out.println("ODEBRANO PLIK - INNY KOMUNIKAT DAC");
                        changeTitleStage("Status:OK");
                    } else {
                        System.out.println("BLAD PRZY ODBIERANIU PLIKU - BOSLUZYC INNE PRZYPADKI");
                        flagAlert = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    flagAlert = false;
                } catch (NullPointerException e) {
                    flagAlert = false;
                    System.out.println("catch NullPointerException");
                }
                //okienka Alerta
                if (flagAlert) {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Zakończono pobieranie pliku", ButtonType.OK);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.OK) {
                        //do stuff
                    }
                    System.out.println("Zakonczono pobieranie pliku");
                } else {
                    Alert alert = new Alert(Alert.AlertType.NONE, "Wystapil blad przy pobieraniu pliku", ButtonType.OK);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.OK) {
                        //do stuff
                    }
                    System.out.println("Wystapil blad przy pobieraniu pliku");
                }
            }//testfile.isDirectory() && !pathTextField.getText().isEmpty() && TESTOWY_FILE!=null
            else if (TESTOWY_FILE == null) {
                Alert alert = new Alert(Alert.AlertType.NONE, "Wystapil blad przy pobieraniu pliku", ButtonType.OK);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    //do stuff
                }
                System.out.println("Wystapil blad przy pobieraniu pliku");
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Wpisano nieprawidłową ścieżkę", ButtonType.OK);
                alert.showAndWait();
            }
        } //selectedidx>=0
        else{
            Alert alert = new Alert(Alert.AlertType.NONE, "Nie wybrano zadnego pliku", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                //do stuff
            }
            System.out.println("Nie wybrano zadnego pliku");
        }
    }

    @FXML
    /**
     * Metoda zmieniająca nazwe stage (ramki)
     */
    private void changeTitleStage(String text) {
        Stage primStage = (Stage) pathTextField.getScene().getWindow();//
        primStage.setTitle(text);
    }

    @FXML
    /**
     * Wysyla pliki z wybranych przez klienta (te na tableViewClient) na serwer
     */
    void methodSendFileClient() {

        System.out.println("Sending...");
        changeTitleStage("Status:Sending");
        boolean flagAlert=false;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < fileClientList.size(); i++) {
            if (fileClientList.size() > 0) {
                File file = fileClientList.get(i);
                long Length = file.length();
                String fileName = file.getName();
                String fileLength = Long.toString(Length);
                String lastModification = Long.toString(file.lastModified());
                System.out.println(fileClientList.get(i).getPath());
                threadArrayList.add(new Thread(new SendFile(connectSocket.getSocket(), fileClientList.get(i).getPath(), fileLength, fileName, lastModification,false)));
            }
            for (int x = 0; x < threadArrayList.size(); x++) {
                threadArrayList.get(x).start();
                try {
                    threadArrayList.get(x).join();/// w jakis sposob synchronizuje
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("OK");
                changeTitleStage("Status:OK");
                flagAlert=true;
                //showFileListClient();
                threadArrayList.clear();
            }
        }
        //okienko alerta
        if (flagAlert){
            Alert alert = new Alert(Alert.AlertType.NONE, "Zakończono wysyłanie pliku", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                //do stuff
            }
            System.out.println("Zakonczono wysyłanie pliku");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.NONE, "Wystapil blad przy wysyłaniu pliku", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                //do stuff
            }
            System.out.println("Wystapil blad przy wysyłaniu pliku");
        }

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
        boolean flagAlert=false;
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
                flagAlert=true;
            }else {
                System.out.println("BLAD PRZY ODBIERANIU INFORMACJI JAKIE MAMY PLIKI - DAC INNE OPCJE/KOMUNIKATY ");
                flagAlert=false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            flagAlert=false;
        }
        //okienka Alerta
        if (flagAlert) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Poprawnie pobrano liste dostępnych plików", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                //do stuff
            }
            System.out.println("Uaktualizowano liste plików Serwera");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.NONE, "Twój folder jest pusty", ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                //do stuff
            }
            System.out.println("Twój folder jest pusty");
        }
    }

    /**
     * metoda wybrany usuwajaca plik z serwera
     */
    @FXML
    void deleteFileServer() {
        int selectedIdx;
        //DELETE_FILES:plikjakis.txt%drugiplik.pdf  ---------------------->MUSI BYC POBIERANE Z LISTY PLIKOW JAKIE DA SERWER<----------------------
        String file_to_delete=null; //= "server_logs.txt%testowy 3.txt%testowy.txt";//--------------------->TRZEBA BEDZIE TU STOWRZYC TEN CIAG Z POBRANYCH PLIKOW Z LISTY <-------------------
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
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Carch ArrayIndeOutBoundException");
        }
    }

    /**
     * Uaktualnia mainController, dzięki czemu może dojść do zmiany zawartości wyswietlanej w ramce
     * @param mainController przesyla jako parametr kontroler do glownego okna programu
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    /**
     * Funkcja wyswietla nazwy plikow z array lista clienta
     */
    public void showFileListClient(){
        System.out.println("Lista wybranych plikow na kliencie");
        for(int i=0;i<fileClientList.size();i++){
            System.out.println(i+"."+fileClientList.get(i));
        }
    }

    /**
     * Funkcja wyswietla nazwy plikow z array lista serwera i
     */
    public void showFileListServer(){
        System.out.println("Lista plikow na serwerze");
        for(int i=0;i<listViewServer.getItems().size();i++){
            System.out.println(i+"."+listViewServer.getItems().get(i));
        }
    }

    /**
     * Lista zachowujaca liste danych do tableViewClient
     */
    private final ObservableList<FileRow> data =
            FXCollections.observableArrayList(
                    //new FileRow("Jacob", "12:00"),//przyklady wypelnienia tableViewClient
                    //new FileRow("Damian", "12:00"),
                    //new FileRow("Quartus", "12:00")
                    );

}
