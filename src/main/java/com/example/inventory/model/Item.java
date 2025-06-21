package com.example.inventory.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, представляющий товар в инвентаре.
 * Содержит информацию о товаре: id, название, описание,
 * дату создания и последнего обновления.
 */
public class Item {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Конструктор по умолчанию.
     * Автоматически устанавливает даты создания и обновления при создании объекта.
     */
    public Item() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Конструктор с параметрами.
     */
    public Item(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    /**
     * Проверяет валидность данных товара.
     */
    public boolean validate() {
        return name != null &&
                name.length() >= 3 &&
                name.length() <= 50 &&
                (description == null || description.length() <= 255);
    }

    // Геттеры и сеттеры

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * Устанавливает название товара и обновляет дату изменения.
     */
    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание товара и обновляет дату изменения.
     */
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}