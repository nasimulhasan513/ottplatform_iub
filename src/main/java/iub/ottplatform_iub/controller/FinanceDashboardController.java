package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Subscription;

import java.io.IOException;
import java.util.List;

public class FinanceDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label totalRevenueLabel;
    @FXML
    private Label activeSubscriptionsLabel;
    @FXML
    private VBox refundList;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Finance Manager!");
        updateDashboard();
    }

    private void updateDashboard() {
        List<Subscription> subscriptions = OTTPlatformApplication.getDataStorageService().loadSubscriptions();

        // Calculate total revenue
        double totalRevenue = subscriptions.stream()
                .filter(Subscription::isActive)
                .mapToDouble(Subscription::getPrice)
                .sum();
        totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));

        // Count active subscriptions
        long activeCount = subscriptions.stream()
                .filter(Subscription::isActive)
                .count();
        activeSubscriptionsLabel.setText(String.valueOf(activeCount));

        // Load refund requests (placeholder)
        loadRefundRequests();
    }

    private void loadRefundRequests() {
        refundList.getChildren().clear();
        // This would be replaced with actual refund request data
        Label placeholder = new Label("No refund requests pending");
        refundList.getChildren().add(placeholder);
    }

    @FXML
    private void handleGenerateReport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Generated");
        alert.setHeaderText(null);
        alert.setContentText("Financial report has been generated successfully.");
        alert.showAndWait();
    }

    @FXML
    private void handleConfigurePayment() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Configuration");
        alert.setHeaderText(null);
        alert.setContentText("Payment gateway configuration options will be available here.");
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        OTTPlatformApplication.setCurrentUser(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}