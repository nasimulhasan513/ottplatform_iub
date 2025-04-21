package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.SubscriptionPlan;

public class CreatePlanController {
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField durationField;
    @FXML
    private TextArea featuresField;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleCreate() {
        if (nameField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                priceField.getText().isEmpty() || durationField.getText().isEmpty() ||
                featuresField.getText().isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        try {
            double price = Double.parseDouble(priceField.getText());
            int duration = Integer.parseInt(durationField.getText());

            if (price <= 0 || duration <= 0) {
                errorLabel.setText("Price and duration must be positive numbers");
                return;
            }

            SubscriptionPlan plan = new SubscriptionPlan(
                    OTTPlatformApplication.getDataStorageService().generateUniqueId(),
                    nameField.getText(),
                    descriptionField.getText(),
                    price,
                    duration,
                    featuresField.getText());

            OTTPlatformApplication.getDataStorageService().saveSubscriptionPlan(plan);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Plan created successfully!");
            alert.showAndWait();

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            errorLabel.setText("Please enter valid numbers for price and duration");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}