package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.User;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        // String email = "testuploader@gmail.com"; // emailField.getText();
        // String password = "testuploader"; // passwordField.getText();

        String email = "testreviewer@gmail.com";
        String password = "testreviewer";

        // email and password validation
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password");
            return;
        }

        User user = OTTPlatformApplication.getDataStorageService().findUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            errorLabel.setText("Invalid email or password");
            return;
        }

        OTTPlatformApplication.setCurrentUser(user);
        try {
            loadDashboard(user.getUserType());
        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            errorLabel.setText("Error loading registration page");
            e.printStackTrace();
        }
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
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}