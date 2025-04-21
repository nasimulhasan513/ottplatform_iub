package iub.ottplatform_iub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import iub.ottplatform_iub.OTTPlatformApplication;
import iub.ottplatform_iub.model.Subscription;
import iub.ottplatform_iub.model.SubscriptionPlan;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SubscriptionDashboardController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<SubscriptionPlan> planTable;
    @FXML
    private TableColumn<SubscriptionPlan, String> planNameColumn;
    @FXML
    private TableColumn<SubscriptionPlan, Double> planPriceColumn;
    @FXML
    private TableColumn<SubscriptionPlan, Integer> planDurationColumn;
    @FXML
    private TableColumn<SubscriptionPlan, String> planStatusColumn;
    @FXML
    private TableColumn<SubscriptionPlan, Void> planActionsColumn;

    @FXML
    private TableView<Subscription> subscriptionTable;
    @FXML
    private TableColumn<Subscription, String> userIdColumn;
    @FXML
    private TableColumn<Subscription, String> planTypeColumn;
    @FXML
    private TableColumn<Subscription, String> startDateColumn;
    @FXML
    private TableColumn<Subscription, String> endDateColumn;
    @FXML
    private TableColumn<Subscription, Double> priceColumn;
    @FXML
    private TableColumn<Subscription, String> statusColumn;
    @FXML
    private TableColumn<Subscription, Void> actionsColumn;

    @FXML
    private TextField searchField;
    @FXML
    private Button logoutButton;

    private ObservableList<SubscriptionPlan> planData = FXCollections.observableArrayList();
    private ObservableList<Subscription> subscriptionData = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome, Subscription Manager!");
        setupPlanTable();
        setupSubscriptionTable();
        loadData();
    }

    private void setupPlanTable() {
        planNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        planPriceColumn
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        planDurationColumn.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getDurationMonths()).asObject());
        planStatusColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().isActive() ? "Active" : "Inactive"));

        planActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                editButton.setOnAction(event -> handleEditPlan(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeletePlan(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void setupSubscriptionTable() {
        userIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserId()));
        planTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlanType()));
        startDateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getStartDate().format(dateFormatter)));
        endDateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getEndDate().format(dateFormatter)));
        priceColumn
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().isActive() ? "Active" : "Inactive"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button manageButton = new Button("Manage");

            {
                manageButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                manageButton.setOnAction(event -> handleManageSubscription(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(manageButton);
                }
            }
        });
    }

    private void loadData() {
        // Load plans
        planData.clear();
        planData.addAll(OTTPlatformApplication.getDataStorageService().loadSubscriptionPlans());
        planTable.setItems(planData);

        // Load subscriptions
        subscriptionData.clear();
        subscriptionData.addAll(OTTPlatformApplication.getDataStorageService().loadSubscriptions());
        subscriptionTable.setItems(subscriptionData);
    }

    @FXML
    private void handleCreatePlan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create_plan.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create New Plan");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditPlan(SubscriptionPlan plan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_plan.fxml"));
            Parent root = loader.load();
            EditPlanController controller = loader.getController();
            controller.setPlan(plan);

            Stage stage = new Stage();
            stage.setTitle("Edit Plan");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePlan(SubscriptionPlan plan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Plan");
        alert.setHeaderText("Are you sure you want to delete this plan?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            OTTPlatformApplication.getDataStorageService().deleteSubscriptionPlan(plan.getPlanId());
            loadData();
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            subscriptionTable.setItems(subscriptionData);
            return;
        }

        ObservableList<Subscription> filteredList = FXCollections.observableArrayList();
        for (Subscription subscription : subscriptionData) {
            if (subscription.getUserId().toLowerCase().contains(searchTerm)) {
                filteredList.add(subscription);
            }
        }
        subscriptionTable.setItems(filteredList);
    }

    @FXML
    private void handleManageSubscription(Subscription subscription) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manage_subscription.fxml"));
            Parent root = loader.load();
            ManageSubscriptionController controller = loader.getController();
            controller.setSubscription(subscription);

            Stage stage = new Stage();
            stage.setTitle("Manage Subscription");
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