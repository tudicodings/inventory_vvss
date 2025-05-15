module inventory {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.controlsfx.controls;

    opens inventory to javafx.base;
    exports inventory;
    opens inventory.controller to javafx.fxml;
    exports inventory.controller;
    exports inventory.model;
    opens inventory.model to javafx.base, javafx.fxml;
}