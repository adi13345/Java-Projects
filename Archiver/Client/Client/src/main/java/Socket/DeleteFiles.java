package Socket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class DeleteFiles implements Runnable {
    /**
     * Gniazdo polaczeniowe klienta
     */
    private Socket socket;
    /**
     * Lista plikow do usniecia
     */
    private ArrayList<String> files_to_delete = new ArrayList<>();
    /**
     * Kontruktor watku
     * @param files_to_delete - pliki do usuniecia
     */
    public DeleteFiles(ArrayList<String> files_to_delete){
        this.files_to_delete = files_to_delete;
    }
    /**
     * Watek odpowiedzialny za usuwanie plikow
     */
    @Override
    public void run() {

    }
}
