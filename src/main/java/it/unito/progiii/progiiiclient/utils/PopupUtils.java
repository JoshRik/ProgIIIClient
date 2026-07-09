package it.unito.progiii.progiiiclient.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class PopupUtils {

    public static void showError(String description, String reason) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(description);
        error.setHeaderText(null);
        error.setContentText(reason);
        error.showAndWait();
    }

    public static void showNotification(String message) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Notifica");
        info.setContentText(message);
        info.initOwner(null);
        info.show();

        // Chiudi automaticamente dopo 2.5 secondi
        new Thread(() -> {
            try { Thread.sleep(2500); }
            catch (InterruptedException ignored) {}
            Platform.runLater(info::close);
        }).start();


    }

}
