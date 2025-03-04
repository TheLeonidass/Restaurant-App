
package restaurant;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
public class OrderController implements Initializable {
    //Menou Arrays
    private final String[] foodItems = {"Burger", "French Fries", "Pizza", "Coca - Cola", "Nuggets"};
    private final double[] prices = {6.5, 4.2, 13, 3.6, 4};

    @FXML
    private Button btnReturn;
    @FXML
    private AnchorPane anchorOrder;
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldSurname;
    @FXML
    private TextField textFieldPhone;
    @FXML
    private TextField textFieldAddress;
    @FXML
    private CheckBox checkBoxOne;
    @FXML
    private CheckBox checkBoxTwo;
    @FXML
    private CheckBox checkBoxThree;
    @FXML
    private CheckBox checkBoxFour;
    @FXML
    private CheckBox checkBoxFive;
    @FXML
    private Label labelPriceOne;
    @FXML
    private Label labelPriceTwo;
    @FXML
    private Label labelPriceThree;
    @FXML
    private Label labelPriceFour;
    @FXML
    private Label labelPriceFive;

    private CheckBox[] checkBoxes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkBoxes = new CheckBox[]{checkBoxOne, checkBoxTwo, checkBoxThree, checkBoxFour, checkBoxFive};
        //User input handling
        textFieldPhone.setTextFormatter(new TextFormatter<>(change
                -> (change.getControlNewText().matches("[0-9]*")) ? change : null));
        textFieldName.setTextFormatter(new TextFormatter<>(change
                -> (change.getControlNewText().matches("[a-zA-Z]*")) ? change : null));
        textFieldSurname.setTextFormatter(new TextFormatter<>(change
                -> (change.getControlNewText().matches("[a-zA-Z]*")) ? change : null));
        textFieldAddress.setTextFormatter(new TextFormatter<>(change
                -> (change.getControlNewText().matches("[a-zA-Z0-9 ]*")) ? change : null));

    }

    @FXML
    private void handleBtnReturn(ActionEvent event) throws Exception {
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
        //Check for all the fields
        if (textFieldName.getText().isEmpty() || textFieldSurname.getText().isEmpty()
                || textFieldPhone.getText().isEmpty() || textFieldAddress.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields with your personal details.");
            alert.showAndWait();
            return;
        }

        if (!checkBoxOne.isSelected() && !checkBoxTwo.isSelected() && !checkBoxThree.isSelected()
                && !checkBoxFour.isSelected() && !checkBoxFive.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one food item.");
            alert.showAndWait();
            return;
        }
        //Customer Info
        String name = textFieldName.getText();
        String surname = textFieldSurname.getText();
        String phone = textFieldPhone.getText();
        String address = textFieldAddress.getText();

        //Order details
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Customer: ").append(name).append(" ").append(surname).append("\n");
        orderDetails.append("Phone: ").append(phone).append("\n");
        orderDetails.append("Address: ").append(address).append("\n");
        orderDetails.append("Ordered Items:\n");

        double totalAmount = 0.0;
        for (int i = 0; i < foodItems.length; i++) {
            if (checkBoxes[i].isSelected()) {
                orderDetails.append(foodItems[i]).append(" - $").append(prices[i]).append("\n");
                totalAmount += prices[i];
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Order placed successfully!\nTotal Amount: $" + totalAmount);
        alert.showAndWait();

        //Exporting order info at order.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("order.txt", true))) {
            writer.println(orderDetails.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
