package restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author LEONIDAS
 */
public class Restaurant extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Connection with FXML
        Parent main = FXMLLoader.load(getClass().getResource("Main.fxml"));

        //Main scene creation
        Scene scene = new Scene(main);

        //CSS connection
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        //Primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Metropolitan Restaurant");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

}
