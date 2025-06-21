package com.example.inventory;

/**
 * Вспомогательный класс для получения информации о системе.
 */
public class SystemInfo {

    /**
     * Возвращает версию Java.
     */
    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Возвращает версию JavaFX.
     */
    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }
}