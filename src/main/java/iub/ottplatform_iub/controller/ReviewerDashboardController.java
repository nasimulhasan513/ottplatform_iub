package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Content;

import java.io.IOException;
import java.util.List;

public class ReviewerDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox contentList;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Content Reviewer!");

        // Initialize status filter
        statusFilter.getItems().addAll("All", "Pending", "Approved", "Rejected");
        statusFilter.setValue("Pending");

        loadContent();
    }

    private void loadContent() {
        // Clear all existing content boxes
        if (contentList != null) {
            contentList.getChildren().clear();
            contentList.getChildren().removeAll(contentList.getChildren());
        }

        List<Content> contents = OTTPlatformApplication.getDataStorageService().loadContent();

        if (contents != null) {
            for (Content content : contents) {
                if (statusFilter.getValue().equals("All") ||
                        (statusFilter.getValue().equals("Pending") && !content.isApproved()) ||
                        (statusFilter.getValue().equals("Approved") && content.isApproved())) {
                    VBox contentBox = createContentBox(content);
                    if (contentList != null) {
                        contentList.getChildren().add(contentBox);
                    }
                }
            }
        }
    }

    private VBox createContentBox(Content content) {
        VBox box = new VBox(5);
        box.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");

        Label title = new Label("Title: " + content.getTitle());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Label description = new Label("Description: " + content.getDescription());
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #000000;");

        HBox detailsBox = new HBox(10);
        Label genreLabel = new Label("Genre: " + content.getGenre());
        genreLabel.setStyle("-fx-text-fill: #000000;");

        Label languageLabel = new Label("Language: " + content.getLanguage());
        languageLabel.setStyle("-fx-text-fill: #000000;");

        Label yearLabel = new Label("Year: " + content.getYear());
        yearLabel.setStyle("-fx-text-fill: #000000;");

        Label ratingLabel = new Label("Rating: " + content.getRating());
        ratingLabel.setStyle("-fx-text-fill: #000000;");

        detailsBox.getChildren().addAll(genreLabel, languageLabel, yearLabel, ratingLabel);

        Label status = new Label("Status: " + (content.isApproved() ? "Approved" : "Pending"));
        status.setStyle("-fx-text-fill: " + (content.isApproved() ? "green" : "orange") + ";");

        Button approveButton = new Button("Approve");
        approveButton.setOnAction(e -> handleApproveContent(content));
        approveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        approveButton.setVisible(!content.isApproved());

        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(e -> handleRejectContent(content));
        rejectButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        rejectButton.setVisible(!content.isApproved());

        HBox buttonBox = new HBox(10, approveButton, rejectButton);
        buttonBox.setStyle("-fx-alignment: center;");

        box.getChildren().addAll(title, description, detailsBox, status, buttonBox);
        return box;
    }

    @FXML
    private void handleApproveContent(Content content) {
        content.setApproved(true);
        OTTPlatformApplication.getDataStorageService().saveContent(content);
        loadContent();
    }

    @FXML
    private void handleRejectContent(Content content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reject Content");
        alert.setHeaderText("Are you sure you want to reject this content?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            OTTPlatformApplication.getDataStorageService().deleteContent(content);
            loadContent();
        }
    }

    @FXML
    private void handleRefresh() {
        loadContent();
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