package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Label totalUsersLabel;
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

        // Count total users
        long totalUsers = subscriptions.stream()
                .map(Subscription::getUserId)
                .distinct()
                .count();
        totalUsersLabel.setText(String.valueOf(totalUsers));
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