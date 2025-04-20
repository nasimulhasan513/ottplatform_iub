package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.User;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> userTypeComboBox;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        userTypeComboBox.getItems().addAll(
                "BASIC",
                "PREMIUM",
                "CONTENT_UPLOADER",
                "CONTENT_REVIEWER",
                "FINANCE_MANAGER",
                "SUBSCRIPTION_MANAGER",
                "SYSTEM_ADMIN");
        userTypeComboBox.setValue("BASIC");
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String userType = userTypeComboBox.getValue();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        if (!isValidEmail(email)) {
            errorLabel.setText("Invalid email format");
            return;
        }

        if (OTTPlatformApplication.getDataStorageService().findUserByEmail(email) != null) {
            errorLabel.setText("Email already registered");
            return;
        }

        User newUser = new User(
                OTTPlatformApplication.getDataStorageService().generateUniqueId(),
                username,
                email,
                password,
                userType);

        OTTPlatformApplication.getDataStorageService().saveUser(newUser);
        OTTPlatformApplication.setCurrentUser(newUser);

        try {
            loadDashboard(userType);
        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            errorLabel.setText("Error loading login page");
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {

        if (email.isEmpty()) {
            return false;
        }

        if (!email.contains("@")) {
            return false;
        }

        if (!email.contains(".")) {
            return false;
        }

        if (email.contains(" ")) {
            return false;
        }

        return true;

    }

    private void loadDashboard(String userType) throws IOException {
        String fxmlFile;
        switch (userType) {
            case "BASIC":
            case "PREMIUM":
                fxmlFile = "/fxml/user_dashboard.fxml";
                break;
            case "CONTENT_UPLOADER":
                fxmlFile = "/fxml/uploader_dashboard.fxml";
                break;
            case "CONTENT_REVIEWER":
                fxmlFile = "/fxml/reviewer_dashboard.fxml";
                break;
            case "FINANCE_MANAGER":
                fxmlFile = "/fxml/finance_dashboard.fxml";
                break;
            case "SUBSCRIPTION_MANAGER":
                fxmlFile = "/fxml/subscription_dashboard.fxml";
                break;
            case "SYSTEM_ADMIN":
                fxmlFile = "/fxml/admin_dashboard.fxml";
                break;
            default:
                fxmlFile = "/fxml/user_dashboard.fxml";
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}