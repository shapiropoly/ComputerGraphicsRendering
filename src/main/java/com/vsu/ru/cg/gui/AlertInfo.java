package com.vsu.ru.cg.gui;

import javafx.scene.control.Alert;

public class AlertInfo {
    public static void info(String title, String header, String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(info);
        alert.showAndWait();
    }

    public static void error(Exception e, String className) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка в: " + className);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
