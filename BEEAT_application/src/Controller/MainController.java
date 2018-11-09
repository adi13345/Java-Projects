package Controller;

import Model.AutomaticSendThread;
import Model.ControlThread;
import Model.SimpleSendThread;
import Model.TableViewRow;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.ControlThread.controlThreadRunFlag;
import static Model.Main.*;
import static Model.ParseConfigFile.pathToNumbers;
import static Model.ParseConfigFile.pathToMessages;
import static Model.ParseConfigFile.pathToConfigurations;

/**
 * Kontroler okna glownego apliakacji
 * Created by Adrian Szymowiat on 15.04.2018.
 */
public class MainController implements Initializable {
    /**
     * Label wyswietlajacy informacje o statanie baterii
     */
    @FXML
    private Label batteryStatusLabel;
    /**
     * Label wyswietlajacy informacje o poziomie odbieranej mocy
     */
    @FXML
    private Label powerStatusLabel;
    /**
     * Label wyswietlajacy informacje o statusie rejestracji
     */
    @FXML
    private Label registrationStatusLabel;
    /**
     * CheckBox decydujacy o wyswietlaniu informacji o module
     */
    @FXML
    private CheckBox refreshInfoCheckBox;
    /**
     * Przycisk usuwajacy wybrany watek z listy watkow dzialajacych (wysylajacych okreslone wiadomosci okreslonych porach)
     */
    @FXML
    private Button deleteEventButton;
    /**
     * Tabela zawierajaca uruchomione watki automatycznego powiadomienia
     */
    @FXML
    private TableView<TableViewRow> eventTableView;
    /**
     * Kolumna tabeli, nazwa powiadomienia bedaca nazwa pliku konfiguracyjnego, z ktorego zostaly wczytane dane
     */
    @FXML
    private TableColumn<TableViewRow, String> nameColumn;
    /**
     * Kolumna tabeli, aktualny czas wykonania watku
     */
    @FXML
    private TableColumn<TableViewRow, String> eventTimeColumn;
    /**
     * Kolumna tabeli, informacja, czy watek ma sie powtarzac, jelsi tak to co jaki okres czasu
     */
    @FXML
    private TableColumn<TableViewRow, String> periodicityColumn;
    /**
     * Pole tekstowe do wpisywania numerow (format: numer_1;numer2;numer3) jednorazowego wyslania wiadomosci
     */
    @FXML
    private TextField numbersSimpleTextfield;
    /**
     * Pole tekstowe do wpisania tresci jednorazej wiadomosci
     */
    @FXML
    private TextArea messageSimpleTextArea;
    /**
     * Przycisk wyslania jednorazej wiadomosci
     */
    @FXML
    private Button simpleSendButton;
    /**
     * Pole tekstowe wyboru konfiguracji (jakie numery i jaka wiadomosc)
     */
    @FXML
    private TextField selectConfigurationTextField;
    /**
     * Przycik do zalaczania pliku z konfiguracja
     */
    @FXML
    private Button selectConfigurationButton;
    /**
     * Pole do wyboru daty wyslania automtycznej wiadomosci
     */
    @FXML
    private JFXDatePicker datePicker;
    /**
     * Pole do wyboru czasu wyslania automatycznej wiadomości
     */
    @FXML
    private JFXTimePicker timePicker;
    /**
     * Combobox do wyboru minut okresowosci
     */
    @FXML
    private ComboBox<String> comboBoxMinutes;
    /**
     * Combobox do wyboru godzin okresowosci
     */
    @FXML
    private ComboBox<String> comboBoxHours;
    /**
     * Combobox do wyboru dni okresowosci
     */
    @FXML
    private ComboBox<String> comboBoxDays;
    /**
     * Przycisk uruchumienia zaladowanej konfiguracji
     */
    @FXML
    private Button runConfigurationButton;
    /**
     * Przycisk usuwajacy dotychczasowe efekty podczas tworzenia konfiguracji (wczyszczenie pol tekstowych oraz wybranego pliku konfiguracyjnego)
     */
    @FXML
    private Button clearSelectConfigButton;
    /**
     * Pole tekstowe zawierajace numery do zapisania w pliku (oddzielone średnikiem)
     */
    @FXML
    private TextField saveNumbersTextField;
    /**
     * Pole tekstowe zawierajace wiadomosc do zapisania w pliku
     */
    @FXML
    private TextArea saveMessageTextField;
    /**
     * Przycisk do zapisania numerow w pliku
     */
    @FXML
    private Button saveNumbersButton;
    /**
     * Przycisk do zapiasania wiadomosci w pliku
     */
    @FXML
    private Button saveMessageButton;
    /**
     * Pole tekstowe zawierajace sciezke do pliku z numerami (podglad + mozliwosc wpisania sciezki)
     */
    @FXML
    private TextField fileWithNumbersTextField;
    /**
     * Pole tekstowe zawierajace sciezke do pliku z wiadomoscia (podglad + mozliwosc wpisania sciezki)
     */
    @FXML
    private TextField fileWithMessageTextField;
    /**
     * Przycisk wyboru pliku z numerami
     */
    @FXML
    private Button selectNumbersFileButton;
    /**
     * Przycisk wyboru pliku z wiadomoscia
     */
    @FXML
    private Button selectMessageFileButton;
    /**
     * Przycisk zapisujacy wybrana konfiguracje numerow i wiadomosci
     */
    @FXML
    private Button saveConfigButton;
    /**
     * Przycisk usuwajacy wybrane pliki podczas tworzenia konfiguracji
     */
    @FXML
    private Button clearConfigTextFieldButton;
    /**
     * Przycisk usuwajacy zapisana wczesniej konfiguracje (lub też inne, wybrane pliki)
     */
    @FXML
    private Button deleteSavedConfigButton;
    /**
     * Pole tekstowe zawierajace opis wybranego tematu
     */
    @FXML
    private TextArea helpInforamtionTextArea;
    /**
     * Przycisk otwierajacy okno pomocy - wybor tematu
     */
    @FXML
    private Button helpButton;
    /**
     * Label wyswietlajacy aktualnei wybrany temat
     */
    @FXML
    private Label topicInformationLabel;
    /**
     * Zmienna przechowujaca wczesniej wpisana wiadomosc dla simpleSend - zapobieganie przypadkowemu wyslaniu tego samego dwa razy
     */
    private static String earlierMessage;
    /**
     * Analogicznie do earlierMessage
     */
    private static String earlierNumbers;
    /**
     * Zmienna przechowujaca sciezke do pliku z numerami w celu utworzenia konfiguracji
     */
    private static String configurationPathToNumbers;
    /**
     * Zmienna przechowujaca sciezke do pliku z wiadomoscia w celu otworzenia konfiguracji
     */
    private static String configurationPathToMessage;
    /**
     * Zmienna przechowujaca sciezke do pliku z konfiguracyjnego
     */
    private static String pathToConfigurationFile;
    /**
     *
     */
    private static ObservableList<TableViewRow> observableListForTableView;
    /**
     * Metoda ustawiajaca odpowiednie informacje dotyczace modulu
     * @param text - tekst do wyswietlenia
     * @param number - numer decydujacy o rodzaju informacji: 1 - status baterii; 2 - poziom odbieranej mocy ;3-
     */
    public static void setStatus(String text, int number) {
        if (number ==1){
            //ustawienie tekstu mozliwe jedynie poprzez dobranie sie do watku FX zwiazanego zaladowanymy plikami fxml
            Platform.runLater(() -> batteryStatus.setValue(text));//ustawienie tekstu w StringProperty implikujace zmiane napisu wyswietlanego batterySatusLabel
        }else if (number ==2){
            Platform.runLater(() -> powerStatus.setValue(text));
        }else if (number == 3){
            Platform.runLater(() -> registrationStatus.setValue(text));
        }
    }
    /**
     * Metoda obslugujaca zaznaczenie/odznaczenie checkboxa
     */
    @FXML
    void refreshInformation() {
        if(refreshInfoCheckBox.isSelected()){
            batteryStatusLabel.textProperty().bind(batteryStatus);//polaczenie Labela wyswietlajacego tekst ze zmieniajacym sie StringProperty
            powerStatusLabel.textProperty().bind(powerStatus);//analogicznie dla poziomu mocy
            registrationStatusLabel.textProperty().bind(registrationStatus);//analogicznie do powyzszych
            controlThreadRunFlag = true;//ustawienie flagi umożliwiającej działanie watku kontrolnego
            controlThread = new Thread(new ControlThread());//utwrozenie wątku kontrolnego
            controlThread.start();//wystartowanie tego wątku
        }else{
            batteryStatusLabel.textProperty().unbind();//rozlaczenie Labela i StringProperty
            batteryStatusLabel.setText("-");//ustawienie randomowego tekstu,
            powerStatusLabel.textProperty().unbind();//analogicznie
            powerStatusLabel.setText("-");//analogicznie
            registrationStatusLabel.textProperty().unbind();//analogicznie
            registrationStatusLabel.setText("-");//analogicznie
            controlThreadRunFlag = false;//ubicie watku kontrolnego
        }
    }
    /**
     * Metoda usuwajaca wybrany watek automatycznego wysylanai wiadomosci
     */
    @FXML
    void deleteEvent() {
        int selectedIdx;//id wybranego (poprzez klikniecie) wiersza
        try {
            selectedIdx = eventTableView.getSelectionModel().getSelectedIndex();//pobranie z tabeli wiersza pod wybranym indeksem
            if(selectedIdx >= 0 ) {//profilaktyczny warunek
                //observablelist zawiera TableviewRowy a kazdy row posiada referencje na konkretny watek
                observableListForTableView.get(selectedIdx).automaticSendThread.checkTimeRunFlag = false;//ustawienie flagi sprawdzanai czasu watku na false, wyjscie z petli sprawdzajacej czas wyslania
                observableListForTableView.get(selectedIdx).automaticSendThread.infinityRunFlag = false;//ustawienie flagi nieskonczosnosci watku na false, jesli wybrany zdarzenei reprezentowalo watek okresowy to skonczy on swoje dzialanie
                automaticSendThreadArraylist.remove(observableListForTableView.get(selectedIdx).automaticSendThread);//usuniecie danego watku z tablicy watkow
                observableListForTableView.remove(selectedIdx);//usuniecie wiersza z observablelisty i przy okazji z tabeli
            }
        }catch(IndexOutOfBoundsException e) {
            System.out.println("Nastapił niespodziewny błąd podczas usuwania zdarzenia z tabeli: " + e);
        }
    }
    /**
     * Metoda wysylajaca jednorazowa wiadomosc
     */
    @FXML
    void simpleSend() throws InterruptedException {
        SimpleSendThread simpleSendThread = new SimpleSendThread();//stworzenie wątku jednorazowej wiadomosci
        String currentMessage = messageSimpleTextArea.getText();//odczytanei wpisanej wiadomosci
        String currentNumbers = numbersSimpleTextfield.getText();//odczytanie sekwencji wpisanych numerow
        boolean sendFlag = true;//flaga informujaca czy mozna urchomic watek = wyslan wiadomosc
        if(earlierMessage != null && earlierNumbers != null) {//sprawdzenie czy to nie pierwsze wyslanie wiadomosci
            if (currentMessage.trim().equals(earlierMessage.trim()) && currentNumbers.trim().equals(earlierNumbers.trim())) {//sprawdzenie czy wiadomosci - wpisana obecnie i poprzednia - nie sa takie same
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Uwaga!");
                alert.setHeaderText("Próbujesz wysłać tą samą wiadomość co poprzednio do tych samych numerów");
                alert.setContentText("Czy na pewno chcesz kontynuować?");
                Optional<ButtonType> result = alert.showAndWait();//wcisniecie przycisku przez uzytkownika
                if (result.get() == ButtonType.OK) {
                    sendFlag = true;//wysylamy
                } else {
                    sendFlag = false;//nie wysylamy
                }
            }
        }
        if(sendFlag) {
            earlierMessage = currentMessage;//zapisanie wiadomosci do bufora
            earlierNumbers = currentNumbers;//zapisanie sekwencji numerów do bufora
            simpleSendThread.buforOfNumbers = correctnessGuard.getNumbers(currentNumbers);//zaladowanie numerow do bufora
            simpleSendThread.message = currentMessage + "\u001A";//zaladowanie wiadomosci do bufora + znak zakonćzenie wiadomosci
            simpleSendThread.isThisThreadAuto = false;//informacja, ze jest to jednorawowe, reczne wyslanei wiadomosci
            simpleSendThread.start();//uruchomienie wątku
            simpleSendThreadArrayList.add(simpleSendThread);//dodanie wątku do bufora
        }
    }
    /**
     * Metoda wyboru konfiguracji
     */
    @FXML
    void selectConfiguration() {
        FileChooser fileChooser = new FileChooser();//utworzenie obiektu wyboru pliku
        fileChooser.setInitialDirectory(new File(System.getProperty(pathToConfigurations)));//ustawienie domyslne sciezk do folderow
        File selectedFile = fileChooser.showOpenDialog(mainStage);//pokazanie okna i przypisanie wybranego pliku
        if(selectedFile != null){
            pathToConfigurationFile = selectedFile.getPath();//zapisanei sciezki do pliku konfiguracyjnego
            selectConfigurationTextField.setText(selectedFile.getName());//umieszczenie w polu tekstowym nazwy pliku
        }
    }
    /**
     * Metoda uruchomienia wybranej konfiguracji
     */
    @FXML
    void runConfiguration() {
        boolean areDataOk = true;//flaga informujaca, ze wczytane dane sa poprawne i  można uruchomić wątek
        ArrayList<String> numbersArraylist = new ArrayList<>();//bufor zawierajacy numery odbiorcow
        String message = null;//wiadomosc do wyslania
        String name = correctnessGuard.removeExtension(pathToConfigurationFile);//wyłuskanie ze ściezki nazwy pliku i usnięcie rozszerzenia
        //-->sprawdzenie poprawnosci pliku z konfiguracją i załadowanie danych do buforów<--//
        try{
            if(pathToConfigurationFile != null) {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToConfigurationFile));//zaladowanie pliku konfiguracyjnego ze sciezkami do bufora
                ArrayList<String> pathsArrayList = new ArrayList<>();//bufor przechowujacy sciezki do plikow - numerow i wiadomosci
                String line;//linijka tekstu z pliku
                while ((line = bufferedReader.readLine()) != null) {
                    pathsArrayList.add(line.trim());//dodanie sciezki (bez bialych znakow)
                }
                bufferedReader.close();//zamkniecie pliku
                FileReader fileReaderNumbers = new FileReader(pathsArrayList.get(0));//utworzenie obiektu file readera,a by sprawdzic zawartosc pliku
                FileReader fileReaderMessage = new FileReader(pathsArrayList.get(1));//utworzenie obiektu file readera,a by sprawdzic zawartosc pliku
                if(fileReaderNumbers.read() == -1 || fileReaderMessage.read() == -1){
                    Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
                    alert.setTitle("Error!");//tytuł okna
                    alert.setHeaderText("Nie można poprawnie uruchomić akcji!");
                    alert.setContentText("Plik z numerami lub z wiadomością jest pusty!");
                    alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                }else{
                    numbersArraylist = correctnessGuard.readNumbersFromFile(pathsArrayList.get(0));//wczytanie numerow do bufora
                    message = correctnessGuard.readMessageFromFile(pathsArrayList.get(1));//wczytanie wiadomosci do bufora
                }
                fileReaderMessage.close();//zamkniecie pliku
                fileReaderNumbers.close();//zamkniecie pliku
            }else{
                areDataOk = false;//informacja o niepoprawnosci wpisanych danych
                Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
                alert.setTitle("Error!");//tytuł okna
                alert.setHeaderText("Nie można poprawnie uruchomić akcji!");
                alert.setContentText("Prawdopodobnie nie został wybrany żaden plik konfiguracyjny.");
                alert.show();//wyświetlenie alarmu, nie trzeba czekąć
            }
        }catch (IOException e){
            areDataOk = false;//informacja o niepoprawnosci wpisanych danych
            message = null;//wczyszczenie bufora wiadomosci
            numbersArraylist.clear();//wyczyszczenie bufora
            Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
            alert.setTitle("Error!");//tytuł okna
            alert.setHeaderText("Nie można poprawnie uruchomić akcji!");
            alert.setContentText("Prawdopodbnie nie udało się poprawnie odczytać pliku konfiguracyjnego.");
            alert.show();//wyświetlenie alarmu, nie trzeba czekąć
        }
        //-->sprawdzenie poprawnosci wybranej daty i czasu - czy to nie jest juz przeszlosc+dodatkowe zalozenie programisty - 2 min do przodu<--//
        LocalDate localDate = datePicker.getValue();
        LocalTime localTime = timePicker.getValue();
        if(areDataOk) {
            if(datePicker.getValue() != null && timePicker.getValue() != null) {
                if (!correctnessGuard.checkDateAndTime(localDate, localTime)) {
                    areDataOk = false;//informacja o niepoprawnosci wpisanych danych
                    Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
                    alert.setTitle("Error!");//tytuł okna
                    alert.setHeaderText("Wybrana data i czas są przedawniowe!");
                    alert.setContentText("Należy wybrać odpowiednią date i czas (minimum 2 minuty później niż aktualny), uwzględniając fakt, że nie można wysyłać powiadomień w przeszłości");
                    alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                } else {
                    //wstawienie daty i czasu pod atrybuty watku automatycznego wysylana sms
                }
            }else{
                areDataOk = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
                alert.setTitle("Error!");//tytuł okna
                alert.setHeaderText("Wybrana data i czas są niezdefiniowane!");
                alert.setContentText("Należy wybrać odpowiednią date i czas.");
                alert.show();//wyświetlenie alarmu, nie trzeba czekąć
            }
        }
        if(areDataOk) {
            String minutes = comboBoxMinutes.getValue();//pobrane minut z comboboxa
            String hours = comboBoxHours.getValue();//pobranie godzin z comboboxa
            String days = comboBoxDays.getValue();//pobranie dni z combo boxa
            AutomaticSendThread automaticSendThread = new AutomaticSendThread();//stworzenie watku automatycznej wiadomosci
            if(correctnessGuard.checkEnabledPeriodicity(minutes,hours,days)) {//sprawdzenie, czy zostalo cos zaznaczone w comboboxach(rozne od 0)
                automaticSendThread.infinityRunFlag = true;//ustawienie flagi - informacj, ze wiadomsoc bedzie wysylana okresowo
                automaticSendThread.periodicityInMinutesFormat = correctnessGuard.computePeriodicityToMinutes(minutes,hours,days);//zaladowanie okresowosci ( w minutach) do bufora obiektu
            }else{
                automaticSendThread.infinityRunFlag = false;//flaga na false - watek wysle powidomienie tylko raz
            }
            //Konieczne ustawienie ponizszej flagi na true, jesli nie to nie mam mozliwosci wyslania o danej porze - wiadomosc nie zostanie wyslana
            automaticSendThread.checkTimeRunFlag = true;//informacja, ze watek ma sprawdzac, czy nadszedl juz czas na wyslanie wiadomosci
            automaticSendThread.nextDate = localDate;//podstawienie daty do pola watku
            automaticSendThread.nextTime = localTime;//postawienie czasu do pola watku
            automaticSendThread.buforOfNumbers = new ArrayList<>(numbersArraylist);//zaladowanie wczytanych numerow do bufora obiektu
            automaticSendThread.message = message;//zaladowanie wczytanej wiadomosci do bufora obiektu
            automaticSendThread.start();//uruchomienie watku
            automaticSendThreadArraylist.add(automaticSendThread);//dodanie watku do bufora
            String date_bufor = automaticSendThread.nextDate.toString()+" "+automaticSendThread.nextTime.truncatedTo(ChronoUnit.MINUTES).toString();//pobranie napisu daty z dokladnoscia do minut
            String period;//zmienna przechowujaca okresowsc
            if(automaticSendThread.infinityRunFlag){//jesli ustawiona okresowosc
                period = Integer.toString(automaticSendThread.periodicityInMinutesFormat);
            }else{//w przypadku braku okresowosci
                period = "brak";
            }
            TableViewRow tableViewRow = new TableViewRow(name,date_bufor,period);//stworzenie nowego wiersza tabeli
            tableViewRow.automaticSendThread = automaticSendThread;////podstawienei referencji pod watek
            observableListForTableView.add(tableViewRow);//wstawienie wiersza do observableList - > do tablicy
            pathToConfigurationFile = null;//wyczyszczenie sciezki do pliku
            selectConfigurationTextField.setText("");//wyczyszczenie pola tekstowego
            datePicker.setValue(null);//wyczyszczenie pola z data
            timePicker.setValue(null);//wyczyszczenie pola z czasem
            numbersArraylist.clear();//wyczyszczenie bufora z numerami
            message = null;//wczyszczenie bufora wiadomosci
        }else{
            numbersArraylist.clear();//wyczyszczenie bufora z numerami
            message = null;//wczyszczenie bufora wiadomosci
        }
    }
    /**
     * Metoda czyszczaca pole pracy podczas tworzenia konfiguracji
     */
    @FXML
    void clearSelectConfig() {
        if(pathToConfigurationFile != null){
            pathToConfigurationFile = null;;//wyczyszczenie siezki do pliku konfugarycjengo
        }
        //wyczyszczenie pol tekstowych
        selectConfigurationTextField.setText("");//wyczyszczenie pola z konfiguracja
        comboBoxMinutes.setValue("Minutes");//ustawienie domyslnej wartosci comboboxa
        comboBoxHours.setValue("Hours");//ustawienie domyslnej wartosci comboboxa
        comboBoxDays.setValue("Days");//ustawienie domyslnej wartosci comboboxa
        datePicker.setValue(null);//wyczyszczenie pola z data
        timePicker.setValue(null);//wyczyszczenie pola z czasem
    }
    /**
     * Metoda zapisujaca numery w pliku
     */
    @FXML
    void saveNumbers() {
        String[] buforOfNumbers = correctnessGuard.getNumbers(saveNumbersTextField.getText());//zaladowanie numerow do bufora
        if(!correctnessGuard.checkNumbers(buforOfNumbers)){
            Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
            alert.setTitle("Error!");//tytuł okna
            alert.setHeaderText("Nieprawidłowy format wpisanych numerów");
            alert.setContentText("Numery powinny byc oddzielone średnikiem, muszą zawierać tylko cyfry i nie mogą być krótsze niż 9 cyfr ani dłuższe niż 15 cyfr.");
            alert.show();//wyświetlenie alarmu, nie trzeba czekąć
        }else{
            FileChooser fileChooser = new FileChooser();//utworzenie obiektu wyboru pliku
            fileChooser.setInitialDirectory(new File(System.getProperty(pathToNumbers)));//ustawienie domyslne sciezk do folderow
            File selectedFile = fileChooser.showOpenDialog(mainStage);//pokazanie okna i przypisanie wybranego pliku
            if(selectedFile != null){
                try {
                    FileWriter fileWriter = new FileWriter(selectedFile, true);//stworzenie obiektu do zapisania pliku
                    for(int i = 0; i < buforOfNumbers.length; i++){
                        fileWriter.write(buforOfNumbers[i]+"\r\n");//zapisanie linijki numeru
                        fileWriter.flush();//wymuszenie wypchania wszystkich danych
                    }
                    fileWriter.close();//zamkniecie pliku
                    saveNumbersTextField.setText("");//wyczyszczenie fielda
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);//stworzenie alarmu
                    alert.initModality(Modality.NONE);//ustawienie możliwośći zignorowania okna - minimalizujemy i mozemy dzialac dlaej w aplikacji
                    alert.setTitle("Files Saved!");//tytuł okna
                    alert.setHeaderText("Numery zostały poprawnie zapisane!");
                    alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                }catch (IOException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
                    alert.setTitle("Error!");//tytuł okna
                    alert.setHeaderText("Nastąpil błąd podczas zapisywania pliku!");
                    alert.setContentText(e.toString());//Wyświetlene błędu
                    alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                }
            }
        }
    }
    /**
     * Metoda zapisujaca wiadomosc w pliku
     */
    @FXML
    void saveMessage() {
        String message = saveMessageTextField.getText().replaceAll("\n", System.getProperty("line.separator"));//pobranie wiadomosci do zapisania
        if(!correctnessGuard.checkMessage(message)){
            Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
            alert.setTitle("Error!");//tytuł okna
            alert.setHeaderText("Nieprawidłowy format wiadomości");
            alert.setContentText("Wiadomość nie powinna przekraczać 160 znaków");
            alert.show();//wyświetlenie alarmu, nie trzeba czekąć
        }else{
            FileChooser fileChooser = new FileChooser();//utworzenie obiektu wyboru pliku
            fileChooser.setInitialDirectory(new File(System.getProperty(pathToMessages)));//ustawienie domyslne sciezk do folderow
            File selectedFile = fileChooser.showOpenDialog(mainStage);//pokazanie okna i przypisanie wybranego pliku
            if(selectedFile != null){
                try {
                    FileReader fileReader = new FileReader(selectedFile);//utworzenie obiektu file readera,a by sprawdzic zawartosc pliku
                    boolean writeFlag = true;//flaga informujaca, czy kontynuowac zapis pliku
                    if(fileReader.read() != -1){//sprawdzenei czy plik ma jakąs zawartosc( slabe ale nie wiem jakie lepsze)
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Uwaga!");
                        alert.setHeaderText("Plik może zawierać inną wiadomość. Zapis nowej wiadomości spowoduje utratę starej.");
                        alert.setContentText("Czy na pewno chcesz kontynuować?");
                        Optional<ButtonType> result = alert.showAndWait();//wcisniecie przycisku przez uzytkownika
                        if (result.get() == ButtonType.OK){
                            writeFlag = true;//zapisujemy
                        }else{
                            writeFlag = false;//nie zapisujemy
                        }
                    }
                    fileReader.close();//zamkniecie file readera
                    if(writeFlag) {//jesli zdecydowano sie na kontynuacje zapisu to zpaisujemy
                        FileWriter fileWriter = new FileWriter(selectedFile);//stworzenie obiektu do zapisania pliku
                        fileWriter.write(message);//zapisanie wiadomosci
                        fileWriter.flush();//wypchanie wszystkich danych
                        fileWriter.close();//zamkniecie pliku
                        saveMessageTextField.setText("");//wyczyszczenie fielda
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);//stworzenie alarmu
                        alert.initModality(Modality.NONE);//ustawienie możliwośći zignorowania okna - minimalizujemy i mozemy dzialac dlaej w aplikacji
                        alert.setTitle("Message Saved!");//tytuł okna
                        alert.setHeaderText("Wiadomość została poprawnie zapisana!");
                        alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                    }
                }catch (IOException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
                    alert.setTitle("Error!");//tytuł okna
                    alert.setHeaderText("Nastąpil błąd podczas zapisywania pliku!");
                    alert.setContentText(e.toString());//Wyświetlene błędu
                    alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                }
            }
        }
    }
    /**
     * Metoda wybierajaca plik z numerami do stworzenia konfiguracji
     */
    @FXML
    void selectNumbersFile() {
        FileChooser fileChooser = new FileChooser();//utworzenie obiektu wyboru pliku
        fileChooser.setInitialDirectory(new File(System.getProperty(pathToNumbers)));//ustawienie domyslne sciezk do folderow
        File selectedFile = fileChooser.showOpenDialog(mainStage);//pokazanie okna i przypisanie wybranego pliku
        if(selectedFile != null){
            configurationPathToNumbers = selectedFile.getPath();//przypisanie sciezki do zmiennej globalnej
            fileWithNumbersTextField.setText(selectedFile.getName());//wstawienie w pole tekstowe nazwy pliku
        }
    }
    /**
     * Metoda wybierajaca plik z wiadomoscia do stworzenia konfiguracji
     */
    @FXML
    void selectMessagefile() {
        FileChooser fileChooser = new FileChooser();//utworzenie obiektu wyboru pliku
        fileChooser.setInitialDirectory(new File(System.getProperty(pathToMessages)));//ustawienie domyslne sciezk do folderow
        File selectedFile = fileChooser.showOpenDialog(mainStage);//pokazanie okna i przypisanie wybranego pliku
        if(selectedFile != null){
            configurationPathToMessage = selectedFile.getPath();//przypisanie sciezki do zmiennej globalnej
            fileWithMessageTextField.setText(selectedFile.getName());//wstawienie w pole tekstowe nazwy pliku
        }
    }
    /**
     * Metoda zapisujaca wybrana konfiguracje numerow i wiadomosci
     */
    @FXML
    void saveConfig() {
        if(configurationPathToNumbers == null || configurationPathToMessage == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);//stworzenie alarmu
            alert.setTitle("Warning!");//tytuł okna
            alert.setHeaderText("Nie można utworzyć konfiguracji!");
            alert.setContentText("Aby stworzyć konfiguracje nalezy wybrać pliki z numerami oraz plik z wiadomością");
            alert.show();//wyświetlenie alarmu, nie trzeba czekąć
        }else{
            FileChooser fileChooser = new FileChooser();//utworzenie obiektu wyboru pliku
            fileChooser.setInitialDirectory(new File(System.getProperty(pathToConfigurations)));//ustawienie domyslne sciezk do folderow
            File selectedFile = fileChooser.showOpenDialog(mainStage);//pokazanie okna i przypisanie wybranego pliku
            if(selectedFile != null){
                try {
                    FileReader fileReader = new FileReader(selectedFile);//utworzenie obiektu file readera,a by sprawdzic zawartosc pliku
                    boolean writeFlag = true;//flaga informujaca, czy kontynuowac zapis pliku
                    if(fileReader.read() != -1){//sprawdzenei czy plik ma jakąs zawartosc( slabe ale nie wiem jakie lepsze)
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Uwaga!");
                        alert.setHeaderText("Plik może zawierać inną konfigurację. Zapis nowej konfiguracji spowoduje utratę starej.");
                        alert.setContentText("Czy na pewno chcesz kontynuować?");
                        Optional<ButtonType> result = alert.showAndWait();//wcisniecie przycisku przez uzytkownika
                        if (result.get() == ButtonType.OK){
                            writeFlag = true;//zapisujemy
                        }else{
                            writeFlag = false;//nie zapisujemy
                        }
                    }
                    fileReader.close();//zamkniecie file readera
                    if(writeFlag) {//jesli zdecydowano sie na kontynuacje zapisu to zapisujemy
                        FileWriter fileWriter = new FileWriter(selectedFile);//stworzenie obiektu do zapisania pliku
                        fileWriter.write(configurationPathToNumbers+"\r\n");//zapisanie sciezki do wiadomosci
                        fileWriter.write(configurationPathToMessage+"\r\n");//zapisanie sciezki do wiadomosci
                        fileWriter.flush();//wypchanie wszystkich danych
                        fileWriter.close();//zamkniecie pliku
                        fileWithNumbersTextField.setText("");//wyczyszczenie fielda
                        fileWithMessageTextField.setText("");//wyczyszczenie fielda
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);//stworzenie alarmu
                        alert.initModality(Modality.NONE);//ustawienie możliwośći zignorowania okna - minimalizujemy i mozemy dzialac dlaej w aplikacji
                        alert.setTitle("Configuration Saved!");//tytuł okna
                        alert.setHeaderText("Konifguracja została poprawnie zapisana!");
                        alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                        configurationPathToNumbers = null;//wczyszczenie sciezki
                        configurationPathToMessage = null;//wczyszczenie sciezki
                    }
                }catch (IOException e){
                    configurationPathToNumbers = null;//wczyszczenie sciezki
                    configurationPathToMessage = null;//wczyszczenie sciezki
                    fileWithNumbersTextField.setText("");//wyczyszczenie fielda
                    fileWithMessageTextField.setText("");//wyczyszczenie fielda
                    Alert alert = new Alert(Alert.AlertType.ERROR);//stworzenie alarmu
                    alert.setTitle("Error!");//tytuł okna
                    alert.setHeaderText("Nastąpil błąd podczas zapisywania pliku!");
                    alert.setContentText(e.toString());//Wyświetlene błędu
                    alert.show();//wyświetlenie alarmu, nie trzeba czekąć
                }
            }
        }
    }
    /**
     * Metoda usuwajaca wybrane pliki podczas tworzenia konfiguracji
     */
    @FXML
    void clearConfigTextField() {
        if(configurationPathToNumbers != null){
            configurationPathToNumbers = null;//usuniecie wczesniej wybranej scieśki
        }
        if(configurationPathToMessage != null){
            configurationPathToMessage = null;//usuniecie wczesniej wybranej scieśki
        }
        fileWithNumbersTextField.setText("");//wyczyszczenie pola tekstowego
        fileWithMessageTextField.setText("");//wyczyszczenie pola tekstowego
    }
    /**
     * Metoda usuwajaca zapisana konfiguracje
     */
    @FXML
    void deleteSavedConfig() {
        FileChooser fileChooser = new FileChooser();//utworzenie obiektu wyboru pliku
        fileChooser.setInitialDirectory(new File(System.getProperty(pathToConfigurations)));//ustawienie domyslne sciezk do folderow
        File selectedFile = fileChooser.showOpenDialog(mainStage);//pokazanie okna i przypisanie wybranego pliku
        if(selectedFile != null){
            boolean deleteFlag = false;//domyslneie nie usuwamy
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Uwaga!");
            alert.setHeaderText("Plik zostanie trwale usunięty!");
            alert.setContentText("Czy na pewno chcesz kontynuować?");
            Optional<ButtonType> result = alert.showAndWait();//wcisniecie przycisku przez uzytkownika
            if (result.get() == ButtonType.OK){
                deleteFlag = true;//usuwamy
            }else{
                deleteFlag = false;// nie usuwamy
            }
            if(deleteFlag) {
                selectedFile.delete();//usunięcie pliku
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);//stworzenie alarmu
                alert2.setTitle("File Deleted!");//tytuł okna
                alert2.setHeaderText("Plik został poprawnie usunięty!");
                alert2.show();//wyświetlenie alarmu, nie trzeba czekąć
            }
        }
    }
    /**
     * Metoda wyswietlajaca okno pomocy
     */
    @FXML
    void help() {}
    /**
     * Metoda inicjalizujaca wstepne parametry elementow aplikacji
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxMinutes.setValue("Minutes");//ustawiene wstepnej wartosci komboboxa
        comboBoxHours.setValue("Hours");//ustawiene wstepnej wartosci komboboxa
        comboBoxDays.setValue("Days");//ustawiene wstepnej wartosci komboboxa
        comboBoxMinutes.getItems().addAll("0 min","1 min","2 min","4 min","5 min","6 min","7 min","8 min","9 min","10 min",
                "11 min","12 min","13 min","14 min","15 min","16 min","17 min","18 min","19 min","20 min","21 min","22 min",
                "23 min","24 min","25 min","26 min","27 min","28 min","29 min","30 min","31 min","32 min","33 min","34 min","35 min",
                "36 min","37 min","38 min","39 min","40 min","41 min","42 min","43 min","44 min","45 min","46 min","47 min","48 min",
                "49 min","50 min","51 min","52 min","53 min","54 min","55 min","56 min","57 min","58 min","59 min","60 min");//ustawienie wszystkich wartosci danego komboboxa
        comboBoxHours.getItems().addAll("0 h","1 h","2 h","4 h","5 h","6 h","7 h","8 h","9 h","10 h","11 h","12 h","13 h",
                "14 h","15 h","16 h","17 h","18 h","19 h","20 h","21 h","22 h","23 h");//ustawienie wszystkich wartosci danego komboboxa
        comboBoxDays.getItems().addAll("0 d","1 d","2 d","4 d","5 d","6 d","7 d","8 d","9 d","10 d","11 d","12 d","13 d",
                "14 d","15 d","16 d","17 d","18 d","19 d","20 d","21 d","22 d","23 d","24 d","25 d","26 d","27 d","28 d","29 d","30 d");//ustawienie wszystkich wartosci danego komboboxa
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));//powiązanie kolumny z atrybutem rowa
        eventTimeColumn.setCellValueFactory(new PropertyValueFactory<>("eventTime"));//powiązanie kolumny z atrybutem rowa
        periodicityColumn.setCellValueFactory(new PropertyValueFactory<>("periodicity"));//powiązanie kolumny z atrybutem rowa
        observableListForTableView = FXCollections.observableArrayList();//stworzenie kolekcji ulatwiajacej dostep do obiektow talbicy
        eventTableView.setItems(observableListForTableView);//powiazanie tabeli eventow z observable row
    }
}

/*for(int i = 0; i < numbersArraylist.size();i++){
                    System.out.println(numbersArraylist.get(i));
                }*/

