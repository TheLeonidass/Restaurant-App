
package restaurant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author LEONIDAS
 */
public class DashboardController implements Initializable {

    @FXML
    private Label labelDashboard;
    @FXML
    private Button btnExit;
    @FXML
    private AnchorPane anchorManageRestaurant;
    @FXML
    private Button btnOrders;
    @FXML
    private Button btnReservations;
    @FXML
    private Button btnStatistics;
    @FXML
    private AnchorPane anchorOrders;
    @FXML
    private AnchorPane anchorReservations;
    @FXML
    private AnchorPane anchorStatistics;
    @FXML
    private CategoryAxis xAxisMenu;
    @FXML
    private BarChart<String, Integer> barChartOrders;
    @FXML
    private Button btnSignOut;
    @FXML
    private TextArea textAreaOrders;
    @FXML
    private TextArea textAreaReservations;
    @FXML
    private BarChart<String, Integer> barChartMenu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleBtnExit(ActionEvent event) {
        if (event.getSource() == btnExit) {
            System.exit(0);
        }
    }

    @FXML
    private void handleBtnOrders(ActionEvent event) {
        if (event.getSource() == btnOrders) {
            anchorOrders.setVisible(true);
            anchorReservations.setVisible(false);
            anchorStatistics.setVisible(false);

            StringBuilder ordersText = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("order.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ordersText.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            textAreaOrders.setText(ordersText.toString());
        }
    }

    @FXML
    private void handleBtnReservations(ActionEvent event) {
        if (event.getSource() == btnReservations) {
            anchorReservations.setVisible(true);
            anchorOrders.setVisible(false);
            anchorStatistics.setVisible(false);

            StringBuilder reservationsText = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader("reservations.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    reservationsText.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            textAreaReservations.setText(reservationsText.toString());
        }
    }

    @FXML
    private void handleBtnStatistics(ActionEvent event) {
        if (event.getSource() == btnStatistics) {
            anchorStatistics.setVisible(true);
            anchorReservations.setVisible(false);
            anchorOrders.setVisible(false);

            populateOrdersBarChart();
            populateMenuBarChart();
        }
    }

    private void populateOrdersBarChart() {
        // Count the number of orders by reading the word "Customer"
        int totalOrders = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("order.txt"))) {
            String line;
            boolean inOrderSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Customer:")) {
                    totalOrders++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display total number of orders
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Total Orders", totalOrders));
        barChartOrders.getData().clear();
        barChartOrders.getData().add(series);
    }

    private void populateMenuBarChart() {
        // Count number of orders for each menu item
        Map<String, Integer> menuItemCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("order.txt"))) {
            String line;
            boolean inOrderSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    inOrderSection = false;
                } else if (line.startsWith("Ordered Items:")) {
                    inOrderSection = true;
                } else if (inOrderSection) {
                    // Parsing menu item
                    String menuItem = line.substring(0, line.lastIndexOf(" - "));
                    menuItemCounts.put(menuItem, menuItemCounts.getOrDefault(menuItem, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : menuItemCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChartMenu.getData().clear();
        barChartMenu.getData().add(series);

        xAxisMenu.setCategories(FXCollections.observableArrayList(menuItemCounts.keySet()));

    }

    @FXML
    private void handleBtnSignOut(ActionEvent event) throws IOException {
        if (event.getSource() == btnSignOut) {
            btnSignOut.getScene().getWindow().hide();

            Parent main = FXMLLoader.load(getClass().getResource("Main.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(main);

            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }
    }

}
