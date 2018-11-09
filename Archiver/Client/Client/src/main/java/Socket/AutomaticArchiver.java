package Socket;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import static Controllers.WriteLoginAndPasswordController.connectSocket;

public class AutomaticArchiver implements Runnable {
    /**
     * Lista plikow do archiwizacji
     */
    private ArrayList<File> files_to_archive = new ArrayList<>();
    /**
     * Lista wszystkich plików klient
     */
    private ArrayList<File> fileClientList;
    /**
     * Kolekcja plikow do zarchiwizowania
     */
    private Properties properties; // properties("nazwa_pliku","sciezka%godzina");
    /**
     * Kontruktor klasy
     */
    public AutomaticArchiver(Properties properties, ArrayList fileClientList){
        this.fileClientList =new ArrayList<>();
        this.properties = new Properties();
        this.properties = properties;
        this.fileClientList = fileClientList;
    }
    private Thread checktime;
    /**
     * Flaga informujaca, czy watek ma dzialac, czy nie
     */
    public boolean flag = true;
    /**
     * Cialo wątku
     */
    @Override
    public void run() {
        checktime = new Thread(new CheckTime());//uruchomienei licznia sprawdzajacego czas
        checktime.start();//uruchomienie watku
        while(flag){
            try {
                for(int i=0; i < fileClientList.size();i++){//leicmy po wszystkich plikach na liscie
                  if(properties.containsKey(fileClientList.get(i).getName())){//sprawdzamy czy properties zawiera klucz o danej nazwe
                      String key = fileClientList.get(i).getName();//podstawiamy jesli tak
                      System.out.println("Warotsc properties: "+properties.getProperty(key));
                      System.out.println("Aktualny stan czasu: "+CheckTime.getTime());
                      if(properties.getProperty(key).equals(CheckTime.getTime()));{//sprawdzamy czy juz czas na archiwizacje tego pliku
                          files_to_archive.add(fileClientList.get(i));
                     }
                  }
                }
                if(files_to_archive.isEmpty()){}
                else {
                    Thread FileToArchiveThread = new Thread(new FileToArchive(files_to_archive));
                    FileToArchiveThread.start();
                }
                Thread.sleep(60000);//uspanie watku na 60 sekund
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
