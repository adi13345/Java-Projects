package Controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class FileToSend {
    private final SimpleStringProperty namefile;
    private final SimpleStringProperty path;
    private final SimpleBooleanProperty status;

    private FileToSend(String namefile, String path, boolean status) {
        this.namefile = new SimpleStringProperty();
        this.path = new SimpleStringProperty(path);
        this.status = new SimpleBooleanProperty(status);
    }

    public String getNameFile() {
        return namefile.get();
    }

    public void setNameFile(String file) {
        this.namefile.set(file);
    }

    public String getPath() {
        return path.get();
    }


    public void setPath(String path) {
        this.path.set(path);
    }

    public boolean isStatus() {
        return status.get();
    }

    public void setStatus(boolean status) {
        this.status.set(status);
    }
}
