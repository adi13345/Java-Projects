package Model;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Klasa sprawdzajaca wszelkie wymagania dotyczace poprawnosci inncyh obiektow, zawierajaca dedykowane metody sprawdzajace
 * Created by Adrian Szymowiat on 17.04.2018.
 */
public class CorrectnessGuard {
    /**
     * Metoda sprawdzajaca poprawnosc wpisanego portu COM
     * Sprawdzenie dotyczy apsektu formalnego, np. czy wpisany ciag zawiera sekwencje "COM", nie jest sprawdzane istnienie danego portu
     * @param string - sprawdzany ciag znakow
     * @return - flaga informujaca, czy wpisany prot COM jest poprawny formalnie
     */
    public boolean isComportCorrect(String string) {
        boolean flag;
        try {
            String port_number = string.substring(3, string.length());//wyodrębnienie kolejnych znakow po pierwszych 3 znakach
            if (!((string.charAt(0) == 'C' || string.charAt(0) == 'c') &&
                    (string.charAt(1) == 'O' || string.charAt(1) == 'o') &&
                    (string.charAt(2) == 'M' || string.charAt(2) == 'm'))) { // np.  C O M 22   sprawdzenie czy wpisany napis zaczyna sie od COM
                flag = false;
                showErrorWindow("Nieprawidłowa nazwa portu COM!", "COM port error");
            } else if (Integer.parseInt(port_number) > 256) {//sprawdzenie czy po slowie "COM" mamy jakąs cyfrę/liczbę
                flag = false;//najwieksza wartosc portu COM dla Widnowsa to 256 (dla NT i NI-VISA jes to wartosc 99)
                showErrorWindow("Numer portu COM nie może byc większy niż 256!", "COM port error");
            } else {
                flag = true;
            }
            return flag;
        } catch (java.lang.NumberFormatException err) {
            flag = false;
            showErrorWindow("Nieprawidłowy numer portu COM!", "COM port error");
            return flag;
        } catch (java.lang.StringIndexOutOfBoundsException er) {
            flag = false;
            showErrorWindow("Nieprawidłowy port COM - musi zawierać minimum 4 znaki!", "COM port error");
            return flag;
        } catch (Exception e) {
            showErrorWindow("Niespodziewany błąd podczas sprawdzania portu!", "COM port error");
            System.out.println(e);
            flag = false;
            return flag;
        }
    }

    /**
     * Metoda wyswietlajaca komunikat o bledzie
     * @param message - wiadomosc w oknie bledu
     */
    private void showErrorWindow(String message, String error_type) {
        JOptionPane.showMessageDialog(new JFrame(), message, error_type,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Meroda sprawdzajaca, czy w odpowiedzi modulu znajduje sie odpowiednia napis
     * @param arrayList - bufor wiadomosci
     * @return flag - flaga informujaca, czy znaleziono pozadany napis
     */
    public boolean checkAnswer(ArrayList<String> arrayList, String answer) {
        boolean flag = false;//flaga informujaca, czy znaleziono "OK"
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).contains(answer)) {//sprawdzenie, czy jest odpowiedź typu OK
                flag = true;//ustawienie flagi na true
                break;// zaprzestanie dalszego sprawdzania
            }
        }
        return flag;//zwrócenie flagi
    }

    /**
     * Metoda losujaca czas oczekiwanai wątku
     * @return - czas oczekiania wątku
     */
    public int randomDelay() {
        int delay = 0;//zmienna przechowująca delay
        Random generator = new Random();//utworzenie obiektu do wygenerowania liczby
        delay = generator.nextInt(1000) + 200;//wygenerowania delayu z przedzialu 200 ms - 1200 ms
        return delay;//zwrócenie wygenerowanego delayu
    }

    /**
     * Metoda tworzaca napis informacyjny o statusie baterii
     * @param arrayList - bufor odebranych wiadomości
     * @return - status baterii i poziom naladowania
     * @throws IndexOutOfBoundsException - wyjatek w przypadku gdy bufor odbranych wiadomosci nie zostanie poprawnei zapelniony
     */
    public String getBatteryStatus(ArrayList<String> arrayList) throws IndexOutOfBoundsException{
        if (checkAnswer(arrayList, "ERROR")) {//sprawdzenie czy nie ma bledu
            return "Battery error";
        } else {
            StringBuilder stringBuilder = new StringBuilder();//obiekt do budowania napisu
            String string_1 = arrayList.get(1).trim().substring(6, 7).trim();//wyciągniecie informacji o statusie baterii
            String string_2 = arrayList.get(1).trim().substring(8, 11).trim();//wyciągnięcie informacji o naładowaniu baterii
            switch (Integer.valueOf(string_1)) {//w zalezosci od zwroconej wartości ustawiany odpowiedni komunikat
                case 0:
                    stringBuilder.append("Powered by battery; ");
                    break;
                case 1:
                    stringBuilder.append("Battery connected,not powered by it; ");
                    break;
                case 2:
                    stringBuilder.append("Battery not connected; ");
                    break;
                case 3:
                    stringBuilder.append("Power fault, calls inhibited; ");
                    break;
                default:
                    stringBuilder.append("--");
                    break;
            }
            stringBuilder.append("Power: " + string_2 + "%");//zlaczenie napisu
            return stringBuilder.toString();//zwrocenie pozadanego stringa
        }
    }
    /**
     * Metoda tworzaca napis informacyjny o poziomie odiberanej mocy
     * @param arrayList - bufor odbieranych wiadomosci
     * @return - odbierana moc
     * @throws IndexOutOfBoundsException - wyjatek w przypadku gdy bufor odbranych wiadomosci nie zostanie poprawnei zapelniony
     */
    public String getPowerStatus(ArrayList<String> arrayList) throws IndexOutOfBoundsException {
        String power = null;//zwracana moc w postaci napisu
        if (checkAnswer(arrayList, "ERROR")) {//sprawdzenie czy nie ma bledu
            return "Power error";
        } else {
            String string_1 = arrayList.get(1).trim().substring(6, 8).trim();//wyciągniecie informacji mocy
            String string_2[] = string_1.split(",");//obrobienie informacji o odiberanej mocy
            int value = Integer.valueOf(string_2[0]);//konwersja na int
            if (value == 99) {//gdy nie mozna okreslic poziomu mocy
                power = "Not detectable";
            } else if (value >= 31) {
                power = "-51 dBm or greater";
            } else if (value == 0) {
                power = "-113 dBm or less";
            } else if (value > 0 && value < 31) {
                int var = value * 2 - 113;//odpowiednei przlieczenie
                StringBuilder stringBuilder = new StringBuilder();//obiekt do zbudowania napisu
                stringBuilder.append(Integer.toString(var) + " dBm");//zbodwanie napisu
                power = stringBuilder.toString();//konwersja na czystego stringa
            } else {
                power = "Special Error";//jak cos innego to w sumie nie wiem
            }
        }
        return power;//zwrocenie napisu poziomy odbieranej mocy
    }
    /**
     * Metoda tworzaca napis informujacy o statusie rejestracji
     * @param arrayList - bufor odbieranych wiadomosci
     * @return - tatus rejestracji
     * @throws IndexOutOfBoundsException - wyjatek w przypadku gdy bufor odbranych wiadomosci nie zostanie poprawnei zapelniony
     */
    public String getRegistrationStatus(ArrayList<String> arrayList) throws IndexOutOfBoundsException {
        if (checkAnswer(arrayList, "ERROR")) {//sprawdzenie czy nie ma bledu
            return "Registration error";
        } else {
            StringBuilder stringBuilder = new StringBuilder();//obiekt do budowania napisu
            String string_1 = arrayList.get(1).trim().substring(7, 8).trim();//wyciągniecie informacji o statusie rejestracji
            String string_2 = arrayList.get(1).trim().substring(9, 10).trim();//wyciągnięcie informacji o technologii dostepu
            switch(Integer.valueOf(string_1)){
                case 0:
                    stringBuilder.append("Not Registered, no searching; ");
                    break;
                case 1:
                    stringBuilder.append("Registered, home network; ");
                    break;
                case 2:
                    stringBuilder.append("Not Registered, searching; ");
                    break;
                case 3:
                    stringBuilder.append("Registration denied; ");
                    break;
                case 4:
                    stringBuilder.append("Unknown; ");
                    break;
                case 5:
                    stringBuilder.append("Registered, roaming; ");
                    break;
                case 6:
                    stringBuilder.append("Registered for SMS, home network; ");
                    break;
                case 7:
                    stringBuilder.append("Registered for SMS, roaming; ");
                    break;
                case 8:
                    stringBuilder.append("Emergency bearer services only; ");
                    break;
                case 9:
                    stringBuilder.append("Registered \"CSFB not preferred\", home network; ");
                    break;
                case 10:
                    stringBuilder.append("registered \"CSFB not preferred\", roaming; ");
                    break;
                default:
                    stringBuilder.append("--; ");
                    break;

            }
            switch (Integer.valueOf(string_2)){
                case 0:
                    stringBuilder.append("GSM");
                    break;
                case 1:
                    stringBuilder.append("GSM compact");
                    break;
                case 2:
                    stringBuilder.append("UTRAN");
                    break;
                case 3:
                    stringBuilder.append("GSM w/EGPRS");
                    break;
                case 4:
                    stringBuilder.append("UTRAN w/HSDPA");
                    break;
                case 5:
                    stringBuilder.append("UTRAN w/HSUPA");
                    break;
                case 6:
                    stringBuilder.append("\nUTRAN w/HSDPA and HSUPA");
                    break;
                case 7:
                    stringBuilder.append("E-UTRAN");
                    break;
                default:
                    stringBuilder.append("--");
                    break;
            }
            return stringBuilder.toString();//zwrocenie pozadanego stringa
        }
    }
    /**
     * Metoda wyciagajaca numery z napisu
     * @param numbers - napis zawierajacy numery wpisane w polu tekstowym (domyslnie oddzielone średnikiem)
     * @return - tablica numerow
     */
    public String[] getNumbers(String numbers){
        String[] splitedNumbers = numbers.split(";");//podzielenei napisu
        return  splitedNumbers;//zwrocenei tablicy numerow
    }
    /**
     * Metoda sprawdzajaca poprawnosc numerow - czy nie zawieraja niepozadanych znakow, czy nie przekraczaja 15 cyfr i czy nie maja mniej niz 9 cyfr
     * @param buforOfNumbers - bufor z numerami
     * @return - informacja, czy numery sa poprawne
     */
    public boolean checkNumbers(String[] buforOfNumbers){
        try {
            boolean flag = true;//flaga informujaca czy wpisane numery zachowuja przyjeta poprawnosc
            for (int i = 0; i < buforOfNumbers.length; i++) {//sprawdzenie kazdego nuemru w buforze
                if(buforOfNumbers[i].startsWith("+")){
                    buforOfNumbers[i] = buforOfNumbers[i].substring(1);
                }
                if (buforOfNumbers[i].length() > 15 || buforOfNumbers[i].length() < 9){
                    flag = false;//numer za dlugi - flaga na false
                    break;//wyjście z pętli, niepotrzebne dalsze sprawdzanie
                }
                Long.valueOf(buforOfNumbers[i]);//konwersja stringa na int, jak sa znak to rzuci błędem który jest łapany poniżej
            }
            return flag;
        }catch(NumberFormatException e){
            System.out.println("Nieprawidłowy format numeru - numer nie może zawierać znaków");
            return false;
        }
    }
    /**
     * Metoda sprawdzajaca poprawnosc wiadomosci - nie moze przekraczac 160 znakow
     * @param message - sprawdzana wiadomosc
     * @return - informacja, czy wiadomosc poprawna
     */
    public boolean checkMessage(String message){
        if(message.length() <= 160){
            return true;
        }else{
            return false;
        }
    }
    /**
     * Metoda odczytujaca numery z pliku
     * @param path - sciezak do pliku
     * @return - tablica numerow
     * @throws IOException - blad wjescia/wyjscia podczas wczytyania pliku
     */
    public ArrayList<String> readNumbersFromFile(String path) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        ArrayList<String> numbersArrayList = new ArrayList<>();//bufor z numerami
        String line;//linijka tekstu z pliku
        while ((line = bufferedReader.readLine()) != null) {
            numbersArrayList.add(line.trim());//dodanie numeru (bez bialych znakow)
        }
        bufferedReader.close();//zamkniecie pliku
        return numbersArrayList;
    }
    /**
     * Metoda wczytujaca wiadomosc tekstowa
     * @param path - sciezka do pliku
     * @return - wiadomosc do wyslania
     * @throws IOException - blad wjescia/wyjscia podczas wczytyania pliku
     */
    public String readMessageFromFile(String path) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        ArrayList<String> numbersArrayList = new ArrayList<>();//bufor z widomoscia
        String line;//linijka tekstu z pliku
        while ((line = bufferedReader.readLine()) != null) {
            numbersArrayList.add(line+"\r\n");//dodanie linjiki wiadomosci (bez bialych znakow)
        }
        bufferedReader.close();//zamkniecie pliku
        StringBuilder stringBuilder = new StringBuilder();//bufor do zbudowania napisu wiadomosci
        for(int i = 0; i < numbersArrayList.size(); i++){
            stringBuilder.append(numbersArrayList.get(i));//dodawanie kolejnych elementow do bufora
        }
        return stringBuilder.toString();//zwrocenie wiadomosci
    }
    /**
     * Metoda sprawdzająca, czy wybrana data nie jest niektualna (nie można wysłać wiadomości w przeszłości)
     * @param localDate - aktualna data
     * @param localTime - aktualny czas (dodatkowo + 2 min)
     * @return - inofmracja, czy data nie jest przedawniona
     */
    public boolean checkDateAndTime(LocalDate localDate, LocalTime localTime){
        //System.out.println("Wybrana data: "+localDate+"   "+localTime);
        //System.out.println("Aktualna data: "+LocalDate.now()+"  "+LocalTime.now());
        if(localDate.compareTo(LocalDate.now()) > 0){//jesli wybrana data jest pozniej niz aktjalna to oki
            return true;
        }else if(localDate.compareTo(LocalDate.now()) == 0){//jesli ten sam dzien
            if (localTime.compareTo(LocalTime.now().plusMinutes(1)) > 0) {//dodanie 1 min delayu zabezpieczajacego
                return true;
            } else {
                return false;//jesli czas nieaktualny to false
            }
        } else {
            return false;//jesli data nieaktualna to false
        }
    }
    /**
     * Metoda zwracajaca okresowosc wpisana w pole tekstowe
     * @param text - wpisane napis w polu tekstowym
     * @return - pierwszy element tablicy to wpisany napis, drugi to okresowosc (minuty(!) bedace napisem)
     */
    public String[] getPeriodicity(String text){
        boolean localCorrectnessFlag = true;//flaga informujaca, ze podany tekst jest poprawny
        String[] returnedBufor = new String[2];
        int amountOfMinutes = -1;//ilośc minut definiujaca okresowosc wysylania wiadomosci



        if(!localCorrectnessFlag){
            returnedBufor[0] = null;//jesli wpisany napis jest niepoprawny to zwracane wartości są null
            returnedBufor[1] = null;
        }else{//gdy wpisany tekstu poprawny
            returnedBufor[0] = text;//podstawienie tekstu wpisanego w polu tekstwym
            returnedBufor[1] = Integer.toString(amountOfMinutes);//podstawienie minut w postaci napisu
        }
        return returnedBufor;//zwrocenie bufora
    }
    /**
     *
     * @param checkDate
     * @return
     */
    public boolean checkTimeToSend(LocalDate checkDate, LocalTime localTime){
        boolean flag;//flaga informujaca, czy data poadana jako argument jest juz przeszloscia
        if(checkDate.compareTo(LocalDate.now()) == 0){//jesli wybrana data jest pozniej niz aktjalna to oki
         if(localTime.compareTo(LocalTime.now()) <= 0){
             flag = true;
         }else{
             flag = false;
         }
        }else{
            flag = false;
        }
        //System.out.println("Flaga nanannananana---->"+flag);
        return flag;
    }
    /**
     * Metoda sprawdzajaca, czy podczas uruchamiania automatycznego powiadomienia zostala wlaczona opcja okresowosci
     * @param minutes - minuty pobrane z comboboxa
     * @param hours - godziny pobrane z comboboxa
     * @param days - dni pobrane z comboboxa
     * @return - informacja, czy okresowosc zostala ustawiona
     */
    public boolean checkEnabledPeriodicity(String minutes, String hours, String days){
        if(minutes.equals("Minutes") || minutes.equals("0 min")){
            if(hours.equals("Hours") || hours.equals("0 h")){
                if(days.equals("Days") || days.equals("0 d")){
                    return false;
                }else{
                    return true;
                }
            }else{
                return true;
            }
        }else{
            return true;
        }
    }
    /**
     * Metoda przeliczajaca wybrany format okresowosci (minuty,godziny,dni) na minuty
     * @param minutes - minuty pobrane z comboboxa
     * @param hours - godziny pobrane z comboboxa
     * @param days - dni pobrane z comboboxa
     * @return - okresowosc w minutach
     */
    public int computePeriodicityToMinutes(String minutes, String hours, String days){
        int minutesValue = 0;//wartosc czasu w miunutach
        int hoursValue = 0;//wartosc czasu w godzinach
        int daysValue = 0;//wartosc czasu w dniach
        if(!minutes.equals("Minutes") && !minutes.equals("0 min")) {
            String[] min_bufor = minutes.split(" ");//uzyskanie liczby minut - format np. "13 min"
            minutesValue = Integer.valueOf(min_bufor[0]);//konwersja napisu na int
        }
        if(!hours.equals("Hours") && !hours.equals("0 h")){
            String[] h_bufor = hours.split(" ");//uzyskanie ilosci godzin - format np. "13 h"
            hoursValue =(60*Integer.valueOf(h_bufor[0]));//konwersja napisu na int
        }
        if(!days.equals("Days") && !days.equals("0 d")){
            String[] d_bufor = days.split(" ");//uzyskanie ilosci dni- format np. "13 d"
            daysValue = (1440*Integer.valueOf(d_bufor[0]));//konwersja napisu na int
        }
        return (minutesValue+hoursValue+daysValue);//suma
    }
    /**
     * Metoda tworząca napis raport z wysłanej wiadomośći
     * @param arrayList - arraylistinformacje o poprawności wysłania
     * @param numbers - numery osob, do ktorych byla wysylana wiadomosc
     * @param message - wyslana wiadomosc
     * @return - napis okna poiwadomienia
     */
    public String createStatement(ArrayList<String> arrayList, String[] numbers, String message){
        StringBuffer stringBuffer = new StringBuffer();//bufor do stworzenia napisu
        stringBuffer.append("Wysłano wiadomość: "+"\n"+"\n");
        stringBuffer.append(message.trim()+"\n"+"\n");
        stringBuffer.append("Do numerów: "+"\n"+"\n");
        for(int i = 1; i <= arrayList.size(); i++){
            stringBuffer.append(Integer.toString(i)+". -> "+numbers[i-1]+" -> "+arrayList.get(i-1)+"\n");
        }
        return stringBuffer.toString().trim();//zwrocenei napisu widomości
    }
    /**
     * Metoda wyodrebniajaca nazwe pliku i usuwajca jego rozszerzenie
     * @param path - pelna sicezka do pliku
     * @return - nazwa pliku
     */
    public String removeExtension(String path) {
        String separator = System.getProperty("file.separator");//seprator pliku
        String filename;//nazwa pliku
        int lastSeparatorIndex = path.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = path;
        } else {
            filename = path.substring(lastSeparatorIndex + 1);
        }
        int extensionIndex = filename.lastIndexOf(".");//wzięcie ostatniej kropki w nazwie liku
        if (extensionIndex == -1)//jesli nie ma kropek
            return filename;
        return filename.substring(0, extensionIndex);//jesli sa - zwrocenie nazwy bez tego, co po ostatniej kropce
    }
}
//--->getBatteryStatus()---
    /*
    AT+CBC AT command returns the battery status of the device.Possible values are,
    0 MT is powered by the battery
    1 MT has a battery connected, but is not powered by it
    2 MT does not have a battery connected
    3 Recognized power fault, calls inhibited
    */
//--->getPowerStatus()<---
    /*
    0 - (-113) dBm or less
    1 - (-111) dBm
    2..30 - (-109)dBm..(-53)dBm / 2 dBm per step
    31 - (-51)dBm or greater
    99 - not known or not detectable
    */
//--->getRegistrationStatus()<---
    /*
    0 not registered, MT is not currently searching a new operator to register to
    1 registered, home network
    2 not registered, but MT is currently searching a new operator to register to
    3 registration denied
    4 unknown (e.g. out of GERAN/UTRAN/E-UTRAN coverage)
    5 registered, roaming
    6 registered for "SMS only", home network (applicable only when indicates E-UTRAN)
    7 registered for "SMS only", roaming (applicable only when indicates E-UTRAN)
    8 attached for emergency bearer services only (see NOTE 2) (not applicable)
                9 registered for "CSFB not preferred", home network (applicable only when indicates E-UTRAN)
    10 registered for "CSFB not preferred", roaming (applicable only when indicates E-UTRAN)

    Possible values for access technology are,
    0 GSM
    1 GSM Compact
    2 UTRAN
    3 GSM w/EGPRS
    4 UTRAN w/HSDPA
    5 UTRAN w/HSUPA
    6 UTRAN w/HSDPA and HSUPA
    7 E-UTRAN
    */
