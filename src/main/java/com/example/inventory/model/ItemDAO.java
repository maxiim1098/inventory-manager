package com.example.inventory.model;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Класс для работы с базой данных товаров.
 * Обеспечивает CRUD операции (создание, чтение, обновление, удаление).
 */
public class ItemDAO {
    private static final String DB_URL = "jdbc:sqlite:inventory.db";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Статический блок инициализации базы данных
    static {
        initializeDatabase();
    }

    /**
     * Инициализирует базу данных при первом обращении к классу.
     * Создает таблицу товаров, если она не существует.
     */
    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS items (" +
                    "id TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL CHECK(LENGTH(name) BETWEEN 3 AND 50)," +
                    "description TEXT CHECK(LENGTH(description) <= 255)," +
                    "createdAt TEXT NOT NULL," +
                    "updatedAt TEXT NOT NULL)";

            stmt.execute(sql);
            System.out.println("✅ База данных успешно инициализирована");
        } catch (SQLException e) {
            System.err.println("❌ Ошибка инициализации базы данных: " + e.getMessage());
        }
    }

    /**
     * Устанавливает соединение с базой данных.
     */
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Создает новый товар в базе данных.
     */
    public void create(Item item) {
        if (!item.validate()) {
            throw new IllegalArgumentException("❌ Недопустимые данные товара");
        }

        // Генерация ID, если отсутствует
        if (item.getId() == null) {
            item.setId(UUID.randomUUID());
        }

        String sql = "INSERT INTO items(id, name, description, createdAt, updatedAt) VALUES(?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getId().toString());
            pstmt.setString(2, item.getName());
            pstmt.setString(3, item.getDescription());
            pstmt.setString(4, item.getCreatedAt().format(formatter));
            pstmt.setString(5, item.getUpdatedAt().format(formatter));

            pstmt.executeUpdate();
            System.out.println("➕ Товар создан: " + item.getName());
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при создании товара: " + e.getMessage());
        }
    }

    /**
     * Получает все товары из базы данных.
     */
    public List<Item> readAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Item item = new Item();
                item.setId(UUID.fromString(rs.getString("id")));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt"), formatter));
                item.setUpdatedAt(LocalDateTime.parse(rs.getString("updatedAt"), formatter));
                items.add(item);
            }
            System.out.println("📥 Прочитано товаров: " + items.size());
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при чтении товаров: " + e.getMessage());
        }
        return items;
    }

    /**
     * Обновляет существующий товар в базе данных.
     */
    public void update(Item item) {
        if (!item.validate()) {
            throw new IllegalArgumentException("❌ Недопустимые данные товара");
        }

        String sql = "UPDATE items SET name = ?, description = ?, updatedAt = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setString(3, item.getUpdatedAt().format(formatter));
            pstmt.setString(4, item.getId().toString());

            pstmt.executeUpdate();
            System.out.println("🔄 Товар обновлен: " + item.getName());
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при обновлении товара: " + e.getMessage());
        }
    }

    /**
     * Удаляет товар по его идентификатору.
     */
    public void delete(UUID id) {
        String sql = "DELETE FROM items WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();
            System.out.println("🗑️ Товар удален: " + id);
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при удалении товара: " + e.getMessage());
        }
    }

    /**
     * Находит товар по его идентификатору.
     */
    public Item getById(UUID id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setId(UUID.fromString(rs.getString("id")));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));
                    item.setCreatedAt(LocalDateTime.parse(rs.getString("createdAt"), formatter));
                    item.setUpdatedAt(LocalDateTime.parse(rs.getString("updatedAt"), formatter));
                    return item;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при поиске товара по ID: " + e.getMessage());
        }
        return null;
    }
}