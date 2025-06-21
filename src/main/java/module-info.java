module com.example.inventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.inventory to javafx.graphics;
    opens com.example.inventory.controller to javafx.fxml;
    opens com.example.inventory.model to javafx.base;

    exports com.example.inventory;
    exports com.example.inventory.controller;
    exports com.example.inventory.model;
}