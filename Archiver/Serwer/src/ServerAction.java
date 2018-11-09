import java.io.File;
import java.io.IOException;

/**
 * Klasa odpowiedzialna za obsluge zadan klienta
 */
public class ServerAction {
    /**
     * Metoda oblsugujaca zadania od klienta
     * @param command zadanie klienta
     * @return odpowiedz serwera - wiadomosc jaka zostanie wyslana do klienta
     */
    public static String ServerResponse(String command, String id) {
        try {
            String ID = id;//id danego klienta
            String serverresponse;//wiadomosc serwera
            String[] servercommand_bufor = command.split(":");//podzielenie żądania klienta na fragmenty
            switch (servercommand_bufor[0]) {//sprawdzenie jakiej kategorii jest żądanei klientow (bez sprawdzenia parametrow)
                case "LOGOUT":
                    serverresponse = "State:<250>Client logout";//wiadomosc do klienta o wylogowywaniu
                    break;
                case "SHOW_FILES":
                    serverresponse = getUserPath((ID));//wywolanie nizej zdefiniowanej metody - wyslanie do klienta nazw plikow, jakie posiada
                    break;
                case "TAKE_FILE":
                    String[] bufor = servercommand_bufor[1].split("%");//rozdzielnie informacji o rozmiarze pliku i jego nazwie
                    serverresponse = isFileChanged(id, Long.valueOf(bufor[0]), bufor[1], bufor[2]);//informacja ze mozna otrzymywac pliki
                    break;
                case "DELETE_FILES":
                    serverresponse = deleteFiles(servercommand_bufor[1],id);//wywloanie nizej zdefiniowanej metody usuwajacej podane pliki
                    break;
                case "GIVE_FILE":
                    serverresponse = "OK:<240>Sending file";
                    break;
                default:
                    serverresponse = "Error:<700>Unknown request";//gdy serwer nie potrafi obsluzyc żądania
                    break;
            }
            return serverresponse;//odpowiedz serwera
        }catch (Exception e){
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas obsługi żądanie od klienta o ID: "+id+e);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas obsługi żądanie od klienta o ID: "+id+e+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas obsługi żądanie od klienta o ID: "+id+e);//komunikat do logow
            return "Error:<999>Server occured a uknown error";
        }
    }

    /**
     * Metoda zwracaja liste plikow danego usera
     * @param ID - id usera, ktorego pliki maja zosta zwrocone
     * @return - pliki w danym folderze
     */
    private static String getUserPath(String ID) {
        String files;
        try {
            String path = getPath(ID) ;//sciezka do folderu usera
            if(path != null) {//gdy udalo sie odnalezc danego usera i jego sicezke
                File file = new File(path);//sciezka do katalogu
                File[] filelist = file.listFiles();//lista wyszstkich plikow w danym folderze
                StringBuilder stringBuilder = new StringBuilder();//bufor na dane o plikach
                for (int j = 0; j < filelist.length; j++) {
                    stringBuilder.append(filelist[j].getName()+"%");
                }
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());//usuwamy ostatni "%"
                files = stringBuilder.toString();//konwersja na Stringa zbudowanego napisu
                return "OK:<205>" + files;//wiadomosc do usera
            }else{
                return "Error:<305>Cannot find specifed path";//gdy, nie ma okreslonego katalogu
            }
        }catch(StringIndexOutOfBoundsException er){//gdy w fodlerze nie ma plikow
            System.out.println(ConsoleFrame.getTimeandDate() + "Folder użytkownika o ID "+ID+" jest pusty");//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Folder użytkownika o ID "+ID+" jest pusty"+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Folder użytkownika o ID "+ID+" jest pusty");//komunikat do logow
            return "Error:<306>Specified folder is empty";//gdy cos pojdzie nie tak
        }catch (Exception e){
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił nieoczekiany błąd podczas wczytywania plikow użytkownika o ID "+ID+": "+e);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił nieoczekiany błąd podczas wczytywania plikow użytkownika o ID "+ID+": "+e+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił nieoczekiany błąd podczas wczytywania plikow użytkownika o ID "+ID+": "+e);//komunikat do logow
            return "Error:<600>Fatal Error";//gdy cos pojdzie nie tak
        }
    }

    /**
     * Metoda sprawdzajaca czy plik sie zmienil
     * @param id - id klienta
     * @param filename - nazwa pliku
     * @param filesize - rozmiar pliku
     * @return - odpowiedz, czy plik sie zmienil, czy nie
     */
    private static String isFileChanged(String id, long filesize, String filename, String modificationdate) {//PRZETESTOWAC
        try {
            String answer = "OK:<210>Getting file";//odpowiedz serwera
            String path = getPath(id);//sciezka do folderu usera
            File file = new File(path);//sciezka do katalogu
            File[] filelist = file.listFiles();//lista wyszstkich plikow w danym folderze
            System.out.println(path);
            for (int i = 0; i < filelist.length; i++) {
                System.out.println(filelist[i]);
                if (filename.equals(filelist[i].getName())) {//sprawdzamy kazda nazwe pliku w folderze czy jest
                    answer = "Error:<308>The filename already exists";//odpowiedz serwera ze plik
                    System.out.println(filename + "    "+ filelist[i]);
                    System.out.println(filelist[i].length() +"    "+ filesize);
                    System.out.println(filelist[i].lastModified() +"    "+ modificationdate);
                    if (filelist[i].length() == filesize && modificationdate.equals(Long.toString(filelist[i].lastModified()))  ) {//sprawdzamy rozmiar danego pliku w folderze i date modyfikacji
                        answer = "Error:<370>File already exists";
                        break;
                    }
                }
            }

            return answer;
        }catch (Exception er){
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas odbierania pliku od klienta o ID: "+id+" typ błędu: "+er);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas odbierania pliku od klienta o ID: "+id+" typ błędu: "+er+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas odbierania pliku od klienta o ID: "+id+" typ błędu: "+er);//komunikat do logow
            return "Error:<416>Failed to load path through unknown reason";//w przypadku gdy nie udalo sie pobrac pliku
        }
    }

    /**
     * Medosa usuwajaca pliki uzytkownika
     * @param files_to_delete - lista plikow(napis) do usuniecia
     * @param id - id uzytkownika
     * @return - odpowiedz serwera
     */
    private static String deleteFiles(String files_to_delete, String id){
        try {
            System.out.println(ConsoleFrame.getTimeandDate() + "Rozpoczęto usuwanie plików użytkownika o ID: "+id);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Rozpoczęto usuwanie plików użytkownika o ID: "+id+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Rozpoczęto usuwanie plików użytkownika o ID: "+id);//komunikat do logow
            String answer = "OK:<230>Files deleted";
            String path = getPath(id);//sciezka do folderu usera
            File file = new File(path);//sciezka do katalogu
            File[] filelist = file.listFiles();//lista wyszstkich plikow w danym folderze
            String[] file_to_delete_bufor = files_to_delete.split("%");//bufor plikow do usuniecia
            for (int j = 0; j < file_to_delete_bufor.length; j++) {//przelatujemy wszystkie pliki, ktore musimy usunac
                for (int i = 0; i < filelist.length; i++)//przelatujemy wszystkie pliki w folderze
                    if (file_to_delete_bufor[j].equals(filelist[i].getName())) {//sprawdzamy kazda nazwe pliku w folderze czy jest
                        filelist[i].delete();//usuniecie pliku
                    }
            }
            System.out.println(ConsoleFrame.getTimeandDate() + "Ukończono usuwanie plików użytkownika o ID: "+id);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Ukończono usuwanie plików użytkownika o ID: "+id+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Ukończono usuwanie plików użytkownika o ID: "+id);//komunikat do logow
            return answer;//odpowiedz serwera
        }catch (Exception e){
            System.out.println(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas usuwania plikow klienta o ID: "+id+" typ błędu: "+e);//komunikat na konsole Intellij
            ConsoleFrame.textarea_.append(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas usuwania plikow klienta o ID: "+id+" typ błędu: "+e+"\n");//komunikat o bledzie
            Main.saveLogs(ConsoleFrame.getTimeandDate() + "Nastąpił niespodziewany błąd podczas usuwania plikow klienta o ID: "+id+" typ błędu: "+e);//komunikat do logow
            return "Error:<418>Cannot delete files through unknown reason";
        }
    }

    /**
     * Metoda zwracajaca sciezke danego uzytkownika
     * @param client_id - id klienta
     * @return - sciezka danego uzytkownika
     */
    public static String getPath(String client_id){
        String path = null;
        for (int i = 0; i < Users.userstable.size(); i++) {//przeszukjemy cala tablice userow
            if (Users.userstable.get(i)[2].equals(client_id)) {//sprawdzamy,ktorej sciezke odpowiada id klienta wyzylajacego żądanie
                path = Users.userstable.get(i)[3];//przypisanie danej sciezki
            }
        }
        return path;
    }
}
