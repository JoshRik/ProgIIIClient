package it.unito.progiii.progiiiclient.utils;

import javafx.scene.control.Alert;

public class PopupUtils {

    public static void showError(String description, String reason) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Errore");
        error.setHeaderText(description);
        error.setContentText(reason);
        error.showAndWait();
    }

    public static void showInfo(String message) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Informazione");
        info.setContentText(message);
        info.showAndWait();
    }

}
