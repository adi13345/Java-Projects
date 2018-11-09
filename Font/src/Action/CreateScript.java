package Action;

import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;

/**
 * Klasa do tworzenie skryptow konfiguracyjnych
 * Created by Adrian Szymowiat on 18.08.2017.
 */
public class CreateScript {
    //pola niezawarte w dokumentacji <- komendy
    //------------------------------------------------------------------------------------------------------------------
    private static final String systemview_ = "system-view";
    private static final String sysname_ = "sysname";
    private static final String interfacevlanif1_ = "interface Vlanif1";
    private static final String vlan_ = "vlan";
    private static final String interface_ ="interface";
    private static final String port_ = "port";
    private static final String ipaddress_ = "ip address";
    private static final String ethernet_ = "Ethernet";
    private static final String gigabitethernet_ = "GigabitEthernet";
    private static final String port_link_type_ = "port link-type";
    private static final String plt_access_ = "access";
    private static final String plt_trunk_ = "trunk";
    private static final String port_trunk_allow_pass_ = "port trunk allow-pass";
    private static final String quit_ = "quit";
    private static final String space_ = " ";
    private static final String enter_ ="\r\n";
    private static final String defaultsubnetmask_ = "30";
    private static final String defaultinterface_one_ = "0/0/1";
    private static final String defaultinterface_two_ = "0/0/2";
    private static final String defaultvlan_one_ = "2";
    private static final String defaultvlan_two_ = "3";
    private static final String defaultipaddress_ = "10.10.10.10";
    //------------------------------------------------------------------------------------------------------------------
    //pola niezawarte w dokumentacji <- zmienne do konfiguracji
    //------------------------------------------------------------------------------------------------------------------
    private static String devicename_;
    private static String numberofdevicesinring;
    private static String ipaddr_;
    private static String subnetmask_;
    private static String deviceswithgigabitethernetports_;
    private static final int maxvlan_ = 4095;
    private static final int maxnumberofdeivces_ = 254;
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Dynamiczna tablica numerów VLAN-ów potrzebnych do konfiguracji ringa
     */
    private ArrayList<String> vlans_arraylist_ = new ArrayList<>();
    /**
     * dynamiczna tablica adresów IP potrzebnych do konfiguracji ringa
     */
    private ArrayList<String> ipaddresses_arraylist_ = new ArrayList<>();
    /**
     * Kontruktor klasy CreateScript
     */
    public CreateScript(){
        try {
            parseConfiguration();//wczytaania parametrów
            if(Integer.valueOf(numberofdevicesinring) > maxnumberofdeivces_) {//sprawdzenie czy liczba podancyh urzadzen w konfiguracji nie przekracza maksymalnej
                JOptionPane.showMessageDialog(null, "Zbyt dużo urzadzeń!\nNależy zmniejszyć ilość urządzeń w pliku ringconfiguration.xml");//komunikat o zbyt dużej ilości urządzen w pliku konfiguracyjnym
            }else{
                generateVlans(Integer.valueOf(numberofdevicesinring));//stworzenie odpowiedniej ilości vlan-ów
                generateAddresses(Integer.valueOf(numberofdevicesinring));//stworzenie odpowiedniej ilości adresów
                for (int i = 1; i <= Integer.valueOf(numberofdevicesinring); i++) {
                    String filename = "Scripts\\" + devicename_ + Integer.toString(i) + ".txt";//nazwa pliku
                    PrintWriter print = new PrintWriter(filename);//otwarcie pliku w celu nadpisanai go
                    switchConfiguration(print, i);//wywolanie nizej zdefiniowanej metody
                    print.close();//zamkniecie
                }
                vlans_arraylist_.clear();//wyczyszczenie tablicy
                ipaddresses_arraylist_.clear();//wyczyszczenie tablicy
                JOptionPane.showMessageDialog(null, "Pomyślnie wygenerowano skrypty!\nPliki znajdują się w folderze Scripts.");//komunikat o pomyślnym wygenerowaniu skryptów
            }
        }catch(IOException e){
            System.out.println("Error CreatScript Constructor: " + e);//komunikat o bledzie na konsole
            JOptionPane.showMessageDialog(null,"Błąd podczas generowania skryptów!\n" + e );//komunikat o bledzie
        }catch(Exception er){
            System.out.println("Error CreatScript Constructor: " + er);//komunikat o bledzie na konsole
            JOptionPane.showMessageDialog(null,"Błąd podczas generowania skryptów!\n" + er );//komunikat o bledzie
        }
    }
    private void switchConfiguration(PrintWriter printWriter, int param) {
        try {
            String port ="";//zmienna stringowa w zaleznosci cyz urzadzenie ma porty typu gigabiteth i czy eth
            if(deviceswithgigabitethernetports_.indexOf(Integer.toString(param)) == -1) {//sprawdzenei czy w konfiguracj isa jakies urzadzenia
                port = ethernet_;//porty ethernetowe
            }else{
                port =gigabitethernet_;//porty giga ethernetowe
            }
            printWriter.println(systemview_);//przejście w widok konfiguracji
            printWriter.println(sysname_+space_+devicename_+Integer.toString(param));//ustawienie nazwy urządzenia
            printWriter.println(vlan_ + space_ + vlans_arraylist_.get(param-1));//ustawienie vlanu
            printWriter.println(interface_ + space_ + port + space_ + defaultinterface_one_);//ustawienie interfejsu
            printWriter.println(port_link_type_ + space_ + plt_trunk_);//ustawienie typu łącza
            printWriter.println(port_trunk_allow_pass_ + space_ + vlan_ + space_+vlans_arraylist_.get(param-1));//ustawienie przepuszczania vlan-ów
            printWriter.println(quit_);//wyjscie
            printWriter.println(interface_ + space_ + vlan_ + space_ + vlans_arraylist_.get(param-1));//wejście na konkretny vlan
            printWriter.println(ipaddress_ + space_ + ipaddresses_arraylist_.get((param-1)*2) + space_ + subnetmask_);//ustawienie adresu ip
            printWriter.println(quit_);//wyjście
            if(param == 1){
                printWriter.println(vlan_ + space_ + vlans_arraylist_.get(Integer.valueOf(numberofdevicesinring)-1));//ustawienie vlanu
                printWriter.println(interface_ + space_ + port + space_ + defaultinterface_two_);//ustawienie interfejsu
                printWriter.println(port_link_type_ + space_ + plt_trunk_);//ustawienie typu łącza
                printWriter.println(port_trunk_allow_pass_ + space_ + vlan_ + space_+vlans_arraylist_.get(Integer.valueOf(numberofdevicesinring)-1));//ustawienie przepuszczania vlan-ów
                printWriter.println(quit_);//wyjscie
                printWriter.println(interface_ + space_ + vlan_ + space_ + vlans_arraylist_.get(Integer.valueOf(numberofdevicesinring)-1));//wejście na konkretny vlan
                printWriter.println(ipaddress_ + space_ + ipaddresses_arraylist_.get((Integer.valueOf(numberofdevicesinring)*2)-1) + space_ + subnetmask_);//ustawienie adresu ip
                printWriter.println(quit_);//wyjście
            }else{
                printWriter.println(vlan_ + space_ + vlans_arraylist_.get(param-2));//ustawienie vlanu
                printWriter.println(interface_ + space_ + port + space_ + defaultinterface_two_);//ustawienie interfejsu
                printWriter.println(port_link_type_ + space_ + plt_trunk_);//ustawienie typu łącza
                printWriter.println(port_trunk_allow_pass_ + space_ + vlan_ + space_+ vlans_arraylist_.get(param-2));//ustawienie przepuszczania vlan-ów
                printWriter.println(quit_);//wyjscie
                printWriter.println(interface_ + space_ + vlan_ + space_ + vlans_arraylist_.get(param-2));//wejście na konkretny vlan
                printWriter.println(ipaddress_ + space_ + ipaddresses_arraylist_.get((param-1)*2-1) + space_ + subnetmask_);//ustawienie adresu ip
                printWriter.println(quit_);//wyjście
            }
            printWriter.println(quit_);//wyjśie
        } catch (Exception e) {
            System.out.println("Error SWITCHCONFIGURATION method: " + e);//komunikat o bledzie na konsole
        }
    }
    /**
     * Metoda wczytująca parametry zawarte w pliku ringconfiguration.xml
     */
    public void parseConfiguration(){
        try {
            //------>parsowanie danych z pliku xml<------
            File file = new File("ringconfiguration.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            //wczytanie danych
            devicename_ = doc.getElementsByTagName("DeviceName").item(0).getTextContent();
            numberofdevicesinring = doc.getElementsByTagName("NumberOfDevices").item(0).getTextContent();
            ipaddr_ = doc.getElementsByTagName("IPAddress").item(0).getTextContent();
            subnetmask_ = doc.getElementsByTagName("SubnetMask").item(0).getTextContent();
            deviceswithgigabitethernetports_ = doc.getElementsByTagName("IDDevicesGigabitEthernet").item(0).getTextContent();
        }catch(FileNotFoundException er){
            JOptionPane.showMessageDialog(null,"Błąd podczas wczytywania parametrów!\n" + er );//komunikat o bledzie
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Błąd podczas wczytywania parametrów\n" + e );//komunikat o bledzie
        }
    }
    /**
     * Metoda generujaca vlan-yy w zaleznosci do podanego parametru(ilosci urzadzen)
     * @param param - ilosc vlan-ow do stworzenia
     */
    private void generateVlans(int param){
        int vlan =0 ;//numer vlanu początkowa wartośc nie jets brana pod uwagę
        int vlanhopvalue= 10;//różnica pomiędzy kolejnymi vlanami, np. dla wartości równej 10 vlany będą się nazywały odpowiednio vlan 10, vlan 20 itd.
        for (int i = 1; i <= param; i++){
            vlan = vlan + vlanhopvalue;//zwiększenei vlanu
            vlans_arraylist_.add(Integer.toString(vlan));//dodanie vlanu do listy
        }
    }
    /**
     * Metoda generujaca odpowiednei adresy IP
     * @param param - ilosc urzadzen w ringu
     */
    private void generateAddresses(int param){
        ipaddr_ = ipaddr_.replace('.','%');//zamiana znaków, bo coś się chlipie
        String[] bufor = ipaddr_.split("%");//podział oktetów adresu ip
        StringBuilder stringBuilder = new StringBuilder();//stworzenie obiektu typu StringBuilder
        int part_one;//ostatni oktet adresu ip z jednej storny interfejsu
        int part_two;//ostatni oktet adresu ip z drugiej strony interfejsu
        if(Integer.valueOf(subnetmask_) == 30 && Integer.valueOf(numberofdevicesinring) <= 64){//dla maski 30 będą inna metoda tworzenia adresów i jak mamy wiecej jak 64 urzadzeni to sie po prostu nie da
            for(int i = 1; i <= param;i++){
                stringBuilder.setLength(0);//wyczyszczenie buforu
                part_one = (1 + ((i-1)*4));//nadanie wartosci ostatniemu oktetowi
                part_two = (2 + ((i-1)*4));//nadanie wartosci ostatniemu oktetowi
                stringBuilder.append(bufor[0]+"."+bufor[1]+"."+bufor[2]+"."+part_one);//stworzenie adresu
                ipaddresses_arraylist_.add(stringBuilder.toString());//dodanei adresu do tablicy
                stringBuilder.setLength(0);//wyczyszczenie buforu
                stringBuilder.append(bufor[0]+"."+bufor[1]+"."+bufor[2]+"."+part_two);//stworzenei adresu
                ipaddresses_arraylist_.add(stringBuilder.toString());//dodanei adresu do tablicy
                stringBuilder.setLength(0);//wyczyszczenie buforu
            }
        }else{
            for(int i = 1; i <= param;i++) {
                stringBuilder.setLength(0);//wyczyszczenie buforu
                stringBuilder.append(bufor[0] + "." + bufor[1] + "." + Integer.toString((int)(Math.floor(256/Integer.valueOf(numberofdevicesinring))*(i-1))) + "." + 1);//stworzenie adresu
                ipaddresses_arraylist_.add(stringBuilder.toString());//dodanei adresu do tablicy
                stringBuilder.setLength(0);//wyczyszczenie buforu
                stringBuilder.append(bufor[0] + "." + bufor[1] + "." + Integer.toString((int)(Math.floor(256/Integer.valueOf(numberofdevicesinring))*(i-1))) + "." + 2);//stworzenie adresu
                ipaddresses_arraylist_.add(stringBuilder.toString());//dodanei adresu do tablicy
                stringBuilder.setLength(0);//wyczyszczenie buforu
            }
        }
    }
}
