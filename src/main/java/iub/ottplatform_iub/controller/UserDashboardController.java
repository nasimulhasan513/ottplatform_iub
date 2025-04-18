package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Content;
import iub.ottplatform_iub.model.Subscription;
import iub.ottplatform_iub.model.User;

import java.io.IOException;
import java.util.List;

public class UserDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox contentList;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> genreFilter;
    @FXML
    private ComboBox<String> languageFilter;
    @FXML
    private Button logoutButton;
    @FXML
    private Button upgradeButton;
    @FXML
    private Label subscriptionStatus;

    @FXML
    public void initialize() {
        User currentUser = OTTPlatformApplication.getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

        // Initialize filters
        genreFilter.getItems().addAll("All", "Action", "Comedy", "Drama", "Horror", "Sci-Fi");
        genreFilter.setValue("All");

        languageFilter.getItems().addAll("All", "English", "Bengali", "Hindi", "Tamil");
        languageFilter.setValue("All");

        // Check subscription status
        checkSubscriptionStatus();

        // Load content
        loadContent();
    }

    private void checkSubscriptionStatus() {
        Subscription subscription = OTTPlatformApplication.getDataStorageService()
                .findSubscriptionByUserId(OTTPlatformApplication.getCurrentUser().getUserId());

        if (subscription != null && subscription.isActive() && !subscription.isExpired()) {
            subscriptionStatus.setText("Subscription: " + subscription.getPlanType() +
                    " (Expires: " + subscription.getEndDate() + ")");
            upgradeButton.setVisible(false);
        } else {
            subscriptionStatus.setText("No active subscription");
            upgradeButton.setVisible(true);
        }
    }

    private void loadContent() {
        contentList.getChildren().clear();
        List<Content> contents = OTTPlatformApplication.getDataStorageService().loadContent();

        for (Content content : contents) {
            if (content.isApproved()) {
                VBox contentBox = createContentBox(content);
                contentList.getChildren().add(contentBox);
            }
        }
    }

    private VBox createContentBox(Content content) {
        VBox box = new VBox(5);
        box.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");

        Label title = new Label(content.getTitle());
        title.setStyle("-fx-font-weight: bold;");

        Label details = new Label(content.getGenre() + " | " + content.getLanguage() + " | " + content.getYear());

        Button previewButton = new Button("Preview");
        previewButton.setOnAction(e -> handlePreviewContent(content));

        Button addToWatchlistButton = new Button("Add to Watchlist");
        addToWatchlistButton.setOnAction(e -> handleAddToWatchlist(content));

        box.getChildren().addAll(title, details, previewButton, addToWatchlistButton);
        return box;
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        String selectedGenre = genreFilter.getValue();
        String selectedLanguage = languageFilter.getValue();

        contentList.getChildren().clear();
        List<Content> contents = OTTPlatformApplication.getDataStorageService().loadContent();

        for (Content content : contents) {
            if (content.isApproved() &&
                    (selectedGenre.equals("All") || content.getGenre().equals(selectedGenre)) &&
                    (selectedLanguage.equals("All") || content.getLanguage().equals(selectedLanguage)) &&
                    (content.getTitle().toLowerCase().contains(searchTerm) ||
                            content.getDescription().toLowerCase().contains(searchTerm))) {
                VBox contentBox = createContentBox(content);
                contentList.getChildren().add(contentBox);
            }
        }
    }

    @FXML
    private void handlePreviewContent(Content content) {
        Subscription subscription = OTTPlatformApplication.getDataStorageService()
                .findSubscriptionByUserId(OTTPlatformApplication.getCurrentUser().getUserId());

        if (subscription != null && subscription.isActive() && !subscription.isExpired()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Content Preview");
            alert.setHeaderText(content.getTitle());
            alert.setContentText("Description: " + content.getDescription() +
                    "\n\nGenre: " + content.getGenre() +
                    "\nLanguage: " + content.getLanguage() +
                    "\nYear: " + content.getYear() +
                    "\nRating: " + content.getRating());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Subscription Required");
            alert.setHeaderText(null);
            alert.setContentText("Please upgrade to a premium subscription to access this content.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddToWatchlist(Content content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Watchlist");
        alert.setHeaderText(null);
        alert.setContentText("Added to watchlist: " + content.getTitle());
        alert.showAndWait();
    }

    @FXML
    private void handleUpgrade() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/subscription.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Upgrade Subscription");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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