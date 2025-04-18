module iub.ottplatform_iub {
    requires javafx.controls;
    requires javafx.fxml;

    opens iub.ottplatform_iub to javafx.fxml;
    opens iub.ottplatform_iub.controller to javafx.fxml;

    exports iub.ottplatform_iub;
    exports iub.ottplatform_iub.controller;
}