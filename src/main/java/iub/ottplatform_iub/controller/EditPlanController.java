package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.SubscriptionPlan;

public class EditPlanController {
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
    private CheckBox activeCheckBox;
    @FXML
    private Label errorLabel;

    private SubscriptionPlan plan;

    public void setPlan(SubscriptionPlan plan) {
        this.plan = plan;
        nameField.setText(plan.getName());
        descriptionField.setText(plan.getDescription());
        priceField.setText(String.valueOf(plan.getPrice()));
        durationField.setText(String.valueOf(plan.getDurationMonths()));
        featuresField.setText(plan.getFeatures());
        activeCheckBox.setSelected(plan.isActive());
    }

    @FXML
    private void handleSave() {
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

            plan.setName(nameField.getText());
            plan.setDescription(descriptionField.getText());
            plan.setPrice(price);
            plan.setDurationMonths(duration);
            plan.setFeatures(featuresField.getText());
            plan.setActive(activeCheckBox.isSelected());

            OTTPlatformApplication.getDataStorageService().updateSubscriptionPlan(plan);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Plan updated successfully!");
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