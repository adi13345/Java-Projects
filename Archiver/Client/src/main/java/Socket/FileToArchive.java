package Socket;

import Controllers.LogInController;

import java.io.File;
import java.util.ArrayList;

import static Controllers.WriteLoginAndPasswordController.connectSocket;

public class FileToArchive implements Runnable {
    /**
     * Lista plików do archiwizacji
     */
    private ArrayList<File> files_to_archive = new ArrayList();

    /**
     * Kontruktor klasy
     * @param fileClientList lista plikow jakie wybral klient do archwizacji
     */
    public FileToArchive(ArrayList fileClientList){
        this.files_to_archive = fileClientList;
    }
    @Override
    public void run() {
        try {
            //LogInController.labelSendFile.setText("Files are archiving, please wait");
            for(int i = 0; i < files_to_archive.size(); i++) {
                File file = files_to_archive.get(i);
                long Length = file.length();
                String fileName = file.getName();
                String fileLength = Long.toString(Length);
                String lastModification = Long.toString(file.lastModified());
                System.out.println(files_to_archive.get(i).getPath());
                Thread sendfilethread = new Thread(new SendFile(connectSocket.getSocket(), files_to_archive.get(i).getPath(), fileLength, fileName, lastModification,true));
                sendfilethread.start();
                sendfilethread.join();
            }
            files_to_archive.clear();
            //LogInController.labelSendFile.setText("Status:OK");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
