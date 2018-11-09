package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Kontroler do MainScreen.fxml ("ramy") naszego programu na której będą umieszczane elementy z poszczególnych plików fxml
 */
public class MainController {

    /**Layout główny programu, to na nim będą umieszczane Pane poszczególnych plików fxml
     */
    @FXML
    private StackPane mainStackPane;

    /**
     * Zmienna inicjalizująca obiekty kontrolek z pliku MainScreen.fxml
     */
    @FXML
    public void initialize(){
        loadMenuScreen();
    }

    /**
     * Metoda ładująca informacje z pliku LoginAndPassword.fxml do okna programu
     */
    @FXML
    public void loadMenuScreen(){
        //ładowanie pliku fxml
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXML/LoginAndPassword.fxml"));
        //inicjalizacja Pane
        Pane pane = null;
        try{
            //ładuje dane z pliku fxml do Pane
            pane = loader.load();
        }catch (IOException e){
            System.out.println("Errror in LoginAndPassword.fxml");
            e.printStackTrace();
        }
        //tworze zmienną Controller WriteLoginAndPassword
        WriteLoginAndPasswordController writeLoginAndPasswordController= loader.getController();
        //ustawiam w klasie kontrolera WriteLoginAndPassword, wskaznik na kontroler głównej ramy programu (MainController)
        writeLoginAndPasswordController.setMainController(this);
        //ustawiam na głównym StackPane programu wygenerowany przed chwilą
        this.setScreen(pane);
    }

    /**
     * Ustawia przesyłany jako argument Pane na głównej ramie programu, usuwajac to co było przedtem
     * @param pane Pane zawierajacy elementy z jakiegoś pliku FXML
     */
    public void setScreen(Pane pane){
        //usuwa poprzednie elementy jakie znajdowały się na stackPane
        mainStackPane.getChildren().clear();
        //ustawia nowe elementy jakie mają sie znalezc na StackPane
        mainStackPane.getChildren().add(pane);
    }


}