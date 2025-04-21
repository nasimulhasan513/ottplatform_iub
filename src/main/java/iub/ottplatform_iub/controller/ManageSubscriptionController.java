package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Subscription;


import java.time.format.DateTimeFormatter;
import java.util.List;

public class ManageSubscriptionController {
    @FXML
    private Label userIdLabel;
    @FXML
    private Label planTypeLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label paymentMethodLabel;

    private Subscription subscription;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
        updateLabels();
    }

    private void updateLabels() {
        userIdLabel.setText(subscription.getUserId());
        planTypeLabel.setText(subscription.getPlanType());
        startDateLabel.setText(subscription.getStartDate().format(dateFormatter));
        endDateLabel.setText(subscription.getEndDate().format(dateFormatter));
        priceLabel.setText(String.format("$%.2f", subscription.getPrice()));
        statusLabel.setText(subscription.isActive() ? "Active" : "Inactive");
        statusLabel.setStyle("-fx-text-fill: " + (subscription.isActive() ? "green" : "red") + ";");
        paymentMethodLabel.setText(subscription.getPaymentMethod());
    }

    @FXML
    private void handleCancelSubscription() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Subscription");
        alert.setHeaderText("Are you sure you want to cancel this subscription?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            subscription.setActive(false);
            // Save the updated subscription
            List<Subscription> subscriptions = OTTPlatformApplication.getDataStorageService().loadSubscriptions();
            for (int i = 0; i < subscriptions.size(); i++) {
                if (subscriptions.get(i).getSubscriptionId().equals(subscription.getSubscriptionId())) {
                    subscriptions.set(i, subscription);
                    break;
                }
            }
            OTTPlatformApplication.getDataStorageService().saveObject(subscriptions, "data/subscriptions.dat");

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Subscription has been cancelled.");
            successAlert.showAndWait();

            Stage stage = (Stage) userIdLabel.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleExtendSubscription() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Extend Subscription");
        alert.setHeaderText(null);
        alert.setContentText("Subscription extension functionality will be implemented here.");
        alert.showAndWait();
    }
}