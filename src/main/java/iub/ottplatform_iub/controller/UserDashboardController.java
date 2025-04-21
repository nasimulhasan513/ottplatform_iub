package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Content;
import iub.ottplatform_iub.model.Subscription;
import iub.ottplatform_iub.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.util.List;

public class UserDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<Content> contentTable;
    @FXML
    private TableColumn<Content, String> titleColumn;
    @FXML
    private TableColumn<Content, String> genreColumn;
    @FXML
    private TableColumn<Content, String> languageColumn;
    @FXML
    private TableColumn<Content, Integer> yearColumn;
    @FXML
    private TableColumn<Content, Void> actionsColumn;
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

    private ObservableList<Content> contentList;

    @FXML
    public void initialize() {
        User currentUser = OTTPlatformApplication.getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

        // Initialize filters
        genreFilter.getItems().addAll("All", "Action", "Comedy", "Drama", "Horror", "Sci-Fi");
        genreFilter.setValue("All");

        languageFilter.getItems().addAll("All", "English", "Bengali", "Hindi", "Tamil");
        languageFilter.setValue("All");

        // Initialize table columns
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        languageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLanguage()));
        yearColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getYear()).asObject());

        // Set up actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button previewButton = new Button("Preview");
            private final HBox buttons = new HBox(5, previewButton);

            {
                previewButton.setOnAction(event -> {
                    Content content = getTableView().getItems().get(getIndex());
                    handlePreviewContent(content);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        // Check subscription status
        checkSubscriptionStatus();

        // Load content
        loadContent();
    }

    private void checkSubscriptionStatus() {
        Subscription subscription = OTTPlatformApplication.getDataStorageService()
                .findSubscriptionByUserId(OTTPlatformApplication.getCurrentUser().getUserId());

        if (subscription != null && subscription.isActive() && !subscription.isExpired()) {
            subscriptionStatus.setText("You are subscribed to " + subscription.getPlanType() +
                    " (Expires: " + subscription.getEndDate() + ")");
            subscriptionStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            upgradeButton.setVisible(false);
        } else {
            subscriptionStatus.setText("No active subscription");
            subscriptionStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            upgradeButton.setVisible(true);
        }
    }

    private void loadContent() {
        contentList = FXCollections.observableArrayList();
        List<Content> contents = OTTPlatformApplication.getDataStorageService().loadContent();

        for (Content content : contents) {
            System.out.println(content.getTitle());
            if (content.isApproved()) {
                contentList.add(content);
            }
        }
        contentTable.setItems(contentList);
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        String selectedGenre = genreFilter.getValue();
        String selectedLanguage = languageFilter.getValue();

        ObservableList<Content> filteredList = FXCollections.observableArrayList();
        for (Content content : contentList) {
            if ((selectedGenre.equals("All") || content.getGenre().equals(selectedGenre)) &&
                    (selectedLanguage.equals("All") || content.getLanguage().equals(selectedLanguage)) &&
                    (content.getTitle().toLowerCase().contains(searchTerm) ||
                            content.getDescription().toLowerCase().contains(searchTerm))) {
                filteredList.add(content);
            }
        }
        contentTable.setItems(filteredList);
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