package com.example.inventory.controller;

import com.example.inventory.model.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер для формы добавления/редактирования товара.
 * Управляет вводом данных и валидацией.
 */
public class ItemFormController implements Initializable {
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;

    private Item item; // Редактируемый товар
    private boolean saved = false; // Флаг сохранения

    /**
     * Инициализация контроллера.
     * Устанавливает ограничения на ввод для текстовых полей.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ограничение длины названия (50 символов)
        nameField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 50 ? change : null));

        // Ограничение длины описания (255 символов)
        descriptionField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
    }

    /**
     * Устанавливает товар для редактирования.
     */
    public void setItem(Item item) {
        this.item = item;
        if (item.getName() != null) {
            nameField.setText(item.getName());
        }
        if (item.getDescription() != null) {
            descriptionField.setText(item.getDescription());
        }
    }

    /**
     * Обрабатывает нажатие кнопки "Сохранить".
     * Проверяет валидность данных и закрывает форму.
     */
    @FXML
    private void handleSave() {
        try {
            validateInput(); // Проверка введенных данных

            // Обновление данных товара
            item.setName(nameField.getText().trim());
            item.setDescription(descriptionField.getText().trim());

            saved = true; // Установка флага сохранения
            closeForm();  // Закрытие формы
        } catch (Exception e) {
            showAlert("Ошибка валидации", e.getMessage());
        }
    }

    /**
     * Проверяет корректность введенных данных.
     */
    private void validateInput() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Название товара не может быть пустым");
        }

        if (name.length() < 3 || name.length() > 50) {
            throw new IllegalArgumentException(
                    "Название должно быть от 3 до 50 символов\n" +
                            "Текущая длина: " + name.length()
            );
        }

        if (description.length() > 255) {
            throw new IllegalArgumentException(
                    "Описание должно быть не более 255 символов\n" +
                            "Текущая длина: " + description.length()
            );
        }
    }

    /**
     * Обрабатывает нажатие кнопки "Отмена".
     * Закрывает форму без сохранения изменений.
     */
    @FXML
    private void handleCancel() {
        saved = false;
        closeForm();
    }

    /**
     * Закрывает окно формы.
     */
    private void closeForm() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Показывает диалоговое окно с ошибкой.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Проверяет, были ли сохранены изменения.
     */
    public boolean isSaved() {
        return saved;
    }
}