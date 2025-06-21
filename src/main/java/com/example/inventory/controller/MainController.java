package com.example.inventory.controller;

import com.example.inventory.model.Item;
import com.example.inventory.model.ItemDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Главный контроллер приложения.
 * Управляет основным интерфейсом, таблицей товаров и операциями.
 */
public class MainController {
    private final ItemDAO itemDAO = new ItemDAO(); // DAO для работы с товарами
    private final ObservableList<Item> items = FXCollections.observableArrayList(); // Список товаров

    // Элементы интерфейса
    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> idColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> descriptionColumn;
    @FXML private TableColumn<Item, LocalDateTime> createdAtColumn;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;

    /**
     * Инициализация контроллера.
     * Настраивает таблицу, загружает данные, устанавливает обработчики.
     */
    @FXML
    public void initialize() {
        configureTableColumns(); // Настройка колонок таблицы
        loadData();              // Загрузка данных
        setupSearchFilter();     // Настройка поиска
        setupSorting();          // Настройка сортировки
    }

    /**
     * Настраивает колонки таблицы товаров.
     */
    private void configureTableColumns() {
        idColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getId().toString()));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Форматирование даты в колонке
        createdAtColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? "" : formatter.format(item));
            }
        });
    }

    /**
     * Загружает данные из базы и обновляет таблицу.
     */
    private void loadData() {
        items.setAll(itemDAO.readAll());
        itemsTable.setItems(items);
        System.out.println("🔄 Данные обновлены. Товаров: " + items.size());
    }

    /**
     * Настраивает фильтрацию по поисковому запросу.
     */
    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                filterItems(newVal.toLowerCase())
        );
    }

    /**
     * Фильтрует товары по поисковому запросу.
     */
    private void filterItems(String filter) {
        if (filter.isEmpty()) {
            itemsTable.setItems(items);
            return;
        }

        ObservableList<Item> filteredItems = FXCollections.observableArrayList();
        for (Item item : items) {
            boolean nameMatches = item.getName().toLowerCase().contains(filter);
            boolean descMatches = item.getDescription() != null &&
                    item.getDescription().toLowerCase().contains(filter);

            if (nameMatches || descMatches) {
                filteredItems.add(item);
            }
        }
        itemsTable.setItems(filteredItems);
    }

    /**
     * Настраивает сортировку товаров.
     */
    private void setupSorting() {
        sortComboBox.getItems().addAll(
                "По названию (А-Я)",
                "По названию (Я-А)",
                "По дате (новые)"
        );
        sortComboBox.getSelectionModel().selectFirst();
        handleSort(); // Применяем сортировку при инициализации
    }

    /**
     * Применяет выбранную сортировку (обработчик из FXML).
     */
    @FXML
    private void handleSort() {
        String selected = sortComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Comparator<Item> comparator = switch (selected) {
            case "По названию (А-Я)" -> Comparator.comparing(Item::getName);
            case "По названию (Я-А)" -> Comparator.comparing(Item::getName).reversed();
            case "По дате (новые)" -> Comparator.comparing(Item::getCreatedAt).reversed();
            default -> null;
        };

        if (comparator != null) {
            FXCollections.sort(items, comparator);
        }
    }

    // Обработчики действий пользователя

    @FXML
    private void handleAdd() {
        showItemForm(new Item());
    }

    @FXML
    private void handleEdit() {
        Item selected = itemsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showItemForm(selected);
        } else {
            showAlert("Ошибка", "Выберите товар для редактирования");
        }
    }

    @FXML
    private void handleDelete() {
        Item selected = itemsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            confirmAndDelete(selected);
        } else {
            showAlert("Ошибка", "Выберите товар для удаления");
        }
    }

    /**
     * Подтверждает удаление и удаляет товар.
     */
    private void confirmAndDelete(Item item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удаление товара");
        alert.setContentText("Вы уверены, что хотите удалить: " + item.getName() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                itemDAO.delete(item.getId());
                items.remove(item);
                loadData();
                showAlert("Успех", "Товар удален: " + item.getName());
            }
        });
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    /**
     * Показывает форму для редактирования товара.
     */
    private void showItemForm(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/item-form.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.setItem(item);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(item.getName() == null ? "Новый товар" : "Редактирование: " + item.getName());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaved()) {
                saveItem(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть форму: " + e.getMessage());
        }
    }

    /**
     * Сохраняет товар в базу данных.
     */
    private void saveItem(Item originalItem) {
        Item savedItem = new Item();
        savedItem.setName(originalItem.getName());
        savedItem.setDescription(originalItem.getDescription());

        itemDAO.create(savedItem);
        loadData();

        showAlert("Успех", "Товар сохранен: " + savedItem.getName());
    }

    /**
     * Показывает информационное окно.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}