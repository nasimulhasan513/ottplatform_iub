package iub.ottplatform_iub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import iub.ottplatform_iub.service.DataStorageService;
import iub.ottplatform_iub.model.User;

public class OTTPlatformApplication extends Application {
    private static DataStorageService dataStorageService;
    private static User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        dataStorageService = new DataStorageService();

        // Load login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("OTT Platform");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static DataStorageService getDataStorageService() {
        return dataStorageService;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}