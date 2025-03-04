
package restaurant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author LEONIDAS
 */
public class MainController implements Initializable {

    @FXML
    private Button btnMakeOrder;
    @FXML
    private Label labelWelcome;
    @FXML
    private Button btnExit;
    @FXML
    private VBox vBoxMain;
    @FXML
    private Label labelSignIn;
    @FXML
    private AnchorPane anchorSignIn;
    @FXML
    private TextField textFieldUser;
    @FXML
    private TextField textFieldPass;
    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnReserve;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleBtnAction(ActionEvent event) throws Exception {
        if (event.getSource() == btnMakeOrder) {
            //Hiding previous scene
            btnMakeOrder.getScene().getWindow().hide();

            //Scene switch
            Parent main = FXMLLoader.load(getClass().getResource("Order.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(main);

            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }
    }

    @FXML
    private void handleBtnExit(ActionEvent event) {
        if (event.getSource() == btnExit) {
            System.exit(0);
        }
    }

    @FXML
    private void handleBtnReserve(ActionEvent event) throws IOException {
        if (event.getSource() == btnReserve) {
            btnReserve.getScene().getWindow().hide();

            Parent main = FXMLLoader.load(getClass().getResource("Reservation.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(main);

            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }
    }

    @FXML
    private void handleBtnSignIn(ActionEvent event) throws IOException {
        if (event.getSource() == btnSignIn) {
            String username = textFieldUser.getText();
            String password = textFieldPass.getText();

            //Validation
            boolean isAuthenticated = validateCredentials(username, password);

            //Successful Authentication
            if (isAuthenticated) {
                btnSignIn.getScene().getWindow().hide();

                Parent main = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(main);

                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();

                //Failed Authentication
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Sign in Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            }
        }
    }

    private boolean validateCredentials(String username, String password) {
        //Validating username and password from txt
        try (BufferedReader reader = new BufferedReader(new FileReader("signin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].trim().equals(username) && parts[1].trim().equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
