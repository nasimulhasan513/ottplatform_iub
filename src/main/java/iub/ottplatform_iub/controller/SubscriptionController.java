package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Subscription;

import java.time.LocalDate;

public class SubscriptionController {
    @FXML
    private ComboBox<String> planComboBox;
    @FXML
    private ComboBox<String> durationComboBox;
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField expiryField;
    @FXML
    private TextField cvvField;
    @FXML
    private Label priceLabel;
    @FXML
    private Button subscribeButton;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        planComboBox.getItems().addAll("Basic", "Premium");
        planComboBox.setValue("Basic");

        durationComboBox.getItems().addAll("1 Month", "3 Months", "6 Months", "1 Year");
        durationComboBox.setValue("1 Month");

        updatePrice();

        planComboBox.setOnAction(e -> updatePrice());
        durationComboBox.setOnAction(e -> updatePrice());
    }

    private void updatePrice() {
        String plan = planComboBox.getValue();
        String duration = durationComboBox.getValue();

        double basePrice = plan.equals("Basic") ? 9.99 : 19.99;
        int months = Integer.parseInt(duration.split(" ")[0]);

        double totalPrice = basePrice * months;
        priceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
    }

    @FXML
    private void handleSubscribe() {
        if (cardNumberField.getText().isEmpty() ||
                expiryField.getText().isEmpty() ||
                cvvField.getText().isEmpty()) {
            errorLabel.setText("Please fill in all payment details");
            return;
        }

        if (!isValidCardNumber(cardNumberField.getText())) {
            errorLabel.setText("Invalid card number");
            return;
        }

        if (!isValidExpiry(expiryField.getText())) {
            errorLabel.setText("Invalid expiry date (MM/YY)");
            return;
        }

        if (!isValidCVV(cvvField.getText())) {
            errorLabel.setText("Invalid CVV");
            return;
        }

        String plan = planComboBox.getValue();
        String duration = durationComboBox.getValue();
        int months = Integer.parseInt(duration.split(" ")[0]);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(months);

        Subscription subscription = new Subscription(
                OTTPlatformApplication.getDataStorageService().generateUniqueId(),
                OTTPlatformApplication.getCurrentUser().getUserId(),
                plan.toUpperCase(),
                startDate,
                endDate,
                Double.parseDouble(priceLabel.getText().replace("Total Price: $", "")),
                "Credit Card");

        OTTPlatformApplication.getDataStorageService().saveSubscription(subscription);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Subscription Successful");
        alert.setHeaderText(null);
        alert.setContentText("Your subscription has been activated!");
        alert.showAndWait();

        Stage stage = (Stage) subscribeButton.getScene().getWindow();
        stage.close();
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }

    private boolean isValidExpiry(String expiry) {
        return expiry.matches("(0[1-9]|1[0-2])/\\d{2}");
    }

    private boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3,4}");
    }
}