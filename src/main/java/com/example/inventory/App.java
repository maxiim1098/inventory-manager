package com.example.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Главный класс приложения. Запускает JavaFX приложение.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Загрузка главного интерфейса из FXML
            URL fxmlUrl = getClass().getResource("/view/main.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("FXML файл не найден: /view/main.fxml");
            }

            Parent root = FXMLLoader.load(fxmlUrl);

            // Настройка главного окна
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Подключаем стили

            primaryStage.setTitle("📦 Менеджер инвентаря");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(500);
            primaryStage.show();

            System.out.println("🚀 Приложение успешно запущено");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Критическая ошибка", "Ошибка запуска приложения: " + e.getMessage());
        }
    }

    /**
     * Показывает окно с ошибкой.
     */
    private void showErrorAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        launch(args);
    }
}