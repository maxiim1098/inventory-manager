package com.example.inventory;

import com.example.inventory.model.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    @Test
    void testValidation() {
        // Валидные данные
        Item validItem = new Item("Valid Name", "Valid Description");
        assertTrue(validItem.validate());

        // Слишком короткое имя
        Item shortName = new Item("A", "");
        assertFalse(shortName.validate());

        // Слишком длинное имя
        Item longName = new Item("This name is way too long and exceeds the limit of 50 characters", "");
        assertFalse(longName.validate());

        // Слишком длинное описание
        Item longDesc = new Item("Valid", "A".repeat(256));
        assertFalse(longDesc.validate());

        // Имя = null
        Item nullName = new Item(null, "Description");
        assertFalse(nullName.validate());

        // Граничные значения
        Item minName = new Item("123", "");
        assertTrue(minName.validate());

        Item maxName = new Item("A".repeat(50), "");
        assertTrue(maxName.validate());

        Item maxDesc = new Item("Name", "A".repeat(255));
        assertTrue(maxDesc.validate());
    }
}