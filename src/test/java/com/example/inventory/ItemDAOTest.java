package com.example.inventory;

import com.example.inventory.model.Item;
import com.example.inventory.model.ItemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {
    private ItemDAO itemDAO;
    private Item testItem;

    @BeforeEach
    void setUp() {
        itemDAO = new ItemDAO();
        testItem = new Item("JUnit Test Item", "Тестовый товар");
        cleanupDatabase();
    }

    private void cleanupDatabase() {
        List<Item> items = itemDAO.readAll();
        for (Item item : items) {
            itemDAO.delete(item.getId());
        }
    }

    @Test
    void testCreateAndRead() {
        itemDAO.create(testItem);
        List<Item> items = itemDAO.readAll();

        assertFalse(items.isEmpty(), "Список не должен быть пустым");
        assertEquals(1, items.size(), "Должен быть ровно один элемент");
        assertEquals(testItem.getName(), items.get(0).getName(), "Имена должны совпадать");
    }

    @Test
    void testUpdate() {
        itemDAO.create(testItem);

        testItem.setName("Updated Name");
        testItem.setDescription("Updated Description");
        itemDAO.update(testItem);

        Item updated = itemDAO.getById(testItem.getId());
        assertNotNull(updated, "Элемент не должен быть null");
        assertEquals("Updated Name", updated.getName(), "Имя должно обновиться");
        assertEquals("Updated Description", updated.getDescription(), "Описание должно обновиться");
    }

    @Test
    void testDelete() {
        itemDAO.create(testItem);
        itemDAO.delete(testItem.getId());

        Item deleted = itemDAO.getById(testItem.getId());
        assertNull(deleted, "Элемент должен быть удален");
    }

    @Test
    void testGetById() {
        itemDAO.create(testItem);
        Item found = itemDAO.getById(testItem.getId());

        assertNotNull(found, "Элемент должен быть найден");
        assertEquals(testItem.getId(), found.getId(), "ID должны совпадать");
    }
}