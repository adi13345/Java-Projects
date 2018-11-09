package Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *     Klasa zachowujace dane do jednego wiersza tableViewClient
 */
public class FileRow {
    private final StringProperty nameFile;// = new SimpleStringProperty();
    private final StringProperty time;// = new SimpleStringProperty();

    public FileRow(String nameFile, String time) {
        this.nameFile = new SimpleStringProperty(nameFile);
        this.time = new SimpleStringProperty(time);
    }

    public String getNameFile() {
        return nameFile.get();
    }

    public StringProperty nameFileProperty() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile.set(nameFile);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }
}