
package restaurant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author LEONIDAS
 */
public class ReservationController implements Initializable {

    @FXML
    private AnchorPane anchorReserve;
    @FXML
    private Label labelReserve;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldSurname;
    @FXML
    private TextField textFieldPhone;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnConfirm;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> tableNumbers = FXCollections.observableArrayList();
        for (int i = 1; i <= 10; i++) {
            tableNumbers.add("Table " + i);
        }
        choiceBox.setItems(tableNumbers);
        
        //User input handling
        textFieldName.setTextFormatter(new TextFormatter<>(change
                -> (change.getControlNewText().matches("[a-zA-Z]*")) ? change : null));
        textFieldSurname.setTextFormatter(new TextFormatter<>(change
                -> (change.getControlNewText().matches("[a-zA-Z]*")) ? change : null));
        textFieldPhone.setTextFormatter(new TextFormatter<>(change
                -> (change.getControlNewText().matches("[0-9]*")) ? change : null));
        datePicker.getEditor().setDisable(true);
    }

    @FXML
    private void handleBtnReturn(ActionEvent event) throws IOException {
        if (event.getSource() == btnReturn) {
            btnReturn.getScene().getWindow().hide();

            Parent main = FXMLLoader.load(getClass().getResource("Main.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(main);

            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }
    }

    @FXML
    private void handleBtnConfirm(ActionEvent event) {
        //Check for all fields
        if (textFieldName.getText().isEmpty() || textFieldSurname.getText().isEmpty()
                || textFieldPhone.getText().isEmpty() || datePicker.getValue() == null || choiceBox.getValue() == null) {
            // If any field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }

        String selectedTable = choiceBox.getValue().toString();
        LocalDate selectedDate = datePicker.getValue();
        if (!isTableAvailable(selectedDate, selectedTable)) {
            // If the table is not available for the selected date
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The selected table is not available for the chosen date. Please choose another table.");
            alert.showAndWait();
            return;
        }

        addReservation(selectedDate, selectedTable);

        //Reservation Details
        StringBuilder reservationDetails = new StringBuilder();
        reservationDetails.append("Customer: ").append(textFieldName.getText()).append(" ").append(textFieldSurname.getText()).append("\n");
        reservationDetails.append("Phone: ").append(textFieldPhone.getText()).append("\n");
        reservationDetails.append("Date: ").append(datePicker.getValue()).append("\n");
        reservationDetails.append("Table: ").append(choiceBox.getValue()).append("\n");

        //Success Message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Reservation placed successfully!");
        alert.showAndWait();

        //Export reservation details to reservations.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("reservations.txt", true))) {
            writer.println(reservationDetails.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<LocalDate, List<String>> reservationsMap = new HashMap<>();

    private void addReservation(LocalDate date, String tableNumber) {
        // Check if date key exists in the map
        if (reservationsMap.containsKey(date)) {
            reservationsMap.get(date).add(tableNumber);
        } else {
            // If the date key doesn't exist
            List<String> reservations = new ArrayList<>();
            reservations.add(tableNumber);
            reservationsMap.put(date, reservations);
        }
    }

    private boolean isTableAvailable(LocalDate date, String tableNumber) {
        // Check if the reservationsMap contains the date key
        if (reservationsMap.containsKey(date)) {
            return !reservationsMap.get(date).contains(tableNumber);
        } else {
            return true;
        }
    }
}
