package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Content;
import iub.ottplatform_iub.model.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploaderDashboardController {
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
    private TableColumn<Content, String> ratingColumn;
    @FXML
    private TableColumn<Content, String> statusColumn;
    @FXML
    private Button uploadButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private TextField yearField;
    @FXML
    private ComboBox<String> ratingComboBox;
    @FXML
    private Label filePathLabel;
    @FXML
    private Label thumbnailPathLabel;
    @FXML
    private Label errorLabel;

    private File selectedVideoFile;
    private File selectedThumbnailFile;
    private ObservableList<Content> contentList;

    @FXML
    public void initialize() {
        User currentUser = OTTPlatformApplication.getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

        // Initialize combo boxes
        genreComboBox.getItems().addAll("Action", "Comedy", "Drama", "Horror", "Sci-Fi");
        languageComboBox.getItems().addAll("English", "Bengali", "Hindi", "Tamil");
        ratingComboBox.getItems().addAll("G", "PG", "PG-13", "R", "NC-17");

        // Initialize table columns
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        languageColumn.setCellValueFactory(cellData -> cellData.getValue().languageProperty());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        contentList = FXCollections.observableArrayList();
        contentTable.setItems(contentList);

        loadContent();
    }

    private void loadContent() {
        contentList.clear(); // Clear existing content
        List<Content> contents = OTTPlatformApplication.getDataStorageService().loadContent();
        System.out.println("Loading content for user: " + OTTPlatformApplication.getCurrentUser().getUserId());

        for (Content content : contents) {
            System.out.println("Checking content: " + content.getTitle() +
                    ", Uploader ID: " + content.getUploaderId() +
                    ", Current User ID: " + OTTPlatformApplication.getCurrentUser().getUserId());

            if (content.getUploaderId().equals(OTTPlatformApplication.getCurrentUser().getUserId())) {
                System.out.println("Adding content to table: " + content.getTitle());
                contentList.add(content);
            }
        }
        System.out.println("Total content loaded in table: " + contentList.size());
    }

    @FXML
    private void handleSelectVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"));

        selectedVideoFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedVideoFile != null) {
            filePathLabel.setText(selectedVideoFile.getName());
        }
    }

    @FXML
    private void handleSelectThumbnail() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Thumbnail Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        selectedThumbnailFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedThumbnailFile != null) {
            thumbnailPathLabel.setText(selectedThumbnailFile.getName());
        }
    }

    @FXML
    private void handleUpload() {
        if (titleField.getText().isEmpty() ||
                descriptionField.getText().isEmpty() ||
                genreComboBox.getValue() == null ||
                languageComboBox.getValue() == null ||
                yearField.getText().isEmpty() ||
                ratingComboBox.getValue() == null ||
                selectedVideoFile == null ||
                selectedThumbnailFile == null) {
            errorLabel.setText("Please fill in all fields and select files");
            return;
        }

        try {
            int year = Integer.parseInt(yearField.getText());
            if (year < 1900 || year > 2100) {
                errorLabel.setText("Invalid year");
                return;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid year format");
            return;
        }

        Content content = new Content(
                OTTPlatformApplication.getDataStorageService().generateUniqueId(),
                titleField.getText(),
                descriptionField.getText(),
                genreComboBox.getValue(),
                languageComboBox.getValue(),
                Integer.parseInt(yearField.getText()),
                ratingComboBox.getValue(),
                OTTPlatformApplication.getCurrentUser().getUserId());

        content.setFilePath(selectedVideoFile.getAbsolutePath());
        content.setThumbnailPath(selectedThumbnailFile.getAbsolutePath());

        OTTPlatformApplication.getDataStorageService().saveContent(content);

        // Add the new content to the table
        contentList.add(content);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upload Successful");
        alert.setHeaderText(null);
        alert.setContentText("Your content has been uploaded and is pending review.");
        alert.showAndWait();

        // Clear form
        clearForm();
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        genreComboBox.setValue(null);
        languageComboBox.setValue(null);
        yearField.clear();
        ratingComboBox.setValue(null);
        filePathLabel.setText("");
        thumbnailPathLabel.setText("");
        selectedVideoFile = null;
        selectedThumbnailFile = null;
        errorLabel.setText("");
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