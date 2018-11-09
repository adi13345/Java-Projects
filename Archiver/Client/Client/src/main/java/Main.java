import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Główna klasa programu, odpala działanie programu. Jest rozszerzeniem JavaFX klasy aplikacja
 */
public class Main  extends Application{

    /**
     * Metoda statyczna rozpoczynająca działanie programu
     * @param args
     */
    public static void main(String[]args){
        launch(args);
    }

    /**
     * Metoda  Metoda startująca pragram. Tworzy ramę główną programu
     * @param primaryStage główny teatr programu
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
        //Loader ładujący dane z pliku fxml
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXML/MainScreen.fxml"));
        //ładuje dane z loadera na StackPane(StackPane to layout)
        StackPane stackPane =loader.load();
        //tworze nową scene do której wkładam stworzony wcześniej
        Scene scene = new Scene(stackPane);
        //teatr na którym wszystko się będzie znajdować - główna rama programu
        Stage stage = new Stage();
        //ustawam na Stage scene którą przed chwilą zainicjalizowałem
        stage.setScene(scene);
        //tytuł programu
        stage.setTitle("Client");
        //czy program ma być skalowalny
        stage.setResizable(false);
        //pokazuje stage
        stage.show();
    }
}
