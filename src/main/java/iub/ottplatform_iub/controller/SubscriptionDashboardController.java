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
import iub.ottplatform_iub.model.User;

import java.io.IOException;
import java.util.List;

public class SubscriptionDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox planList;
    @FXML
    private VBox subscriptionList;
    @FXML
    private VBox promotionList;
    @FXML
    private TextField searchField;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Subscription Manager!");
        loadPlans();
        loadSubscriptions();
        loadPromotions();
    }

    private void loadPlans() {
        planList.getChildren().clear();
        // This would be replaced with actual plan data
        Label placeholder = new Label("No subscription plans available");
        planList.getChildren().add(placeholder);
    }

    private void loadSubscriptions() {
        subscriptionList.getChildren().clear();
        List<Subscription> subscriptions = OTTPlatformApplication.getDataStorageService().loadSubscriptions();

        for (Subscription subscription : subscriptions) {
            VBox subscriptionBox = createSubscriptionBox(subscription);
            subscriptionList.getChildren().add(subscriptionBox);
        }
    }

    private VBox createSubscriptionBox(Subscription subscription) {
        VBox box = new VBox(5);
        box.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");

        Label userLabel = new Label("User: " + subscription.getUserId());
        Label planLabel = new Label("Plan: " + subscription.getPlanType());
        Label statusLabel = new Label("Status: " + (subscription.isActive() ? "Active" : "Inactive"));
        statusLabel.setStyle("-fx-text-fill: " + (subscription.isActive() ? "green" : "red") + ";");

        Button manageButton = new Button("Manage");
        manageButton.setOnAction(e -> handleManageSubscription(subscription));
        manageButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        box.getChildren().addAll(userLabel, planLabel, statusLabel, manageButton);
        return box;
    }

    private void loadPromotions() {
        promotionList.getChildren().clear();
        // This would be replaced with actual promotion data
        Label placeholder = new Label("No active promotions");
        promotionList.getChildren().add(placeholder);
    }

    @FXML
    private void handleCreatePlan() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create Plan");
        alert.setHeaderText(null);
        alert.setContentText("Plan creation form will be available here.");
        alert.showAndWait();
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        subscriptionList.getChildren().clear();

        List<Subscription> subscriptions = OTTPlatformApplication.getDataStorageService().loadSubscriptions();
        for (Subscription subscription : subscriptions) {
            if (subscription.getUserId().toLowerCase().contains(searchTerm)) {
                VBox subscriptionBox = createSubscriptionBox(subscription);
                subscriptionList.getChildren().add(subscriptionBox);
            }
        }
    }

    @FXML
    private void handleCreatePromotion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create Promotion");
        alert.setHeaderText(null);
        alert.setContentText("Promotion creation form will be available here.");
        alert.showAndWait();
    }

    @FXML
    private void handleManageSubscription(Subscription subscription) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Manage Subscription");
        alert.setHeaderText(null);
        alert.setContentText("Subscription management options will be available here.");
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