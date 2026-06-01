package it.unito.progiii.progiiiclient;

import it.unito.progiii.progiiiclient.controllers.DashboardController;
import it.unito.progiii.progiiiclient.controllers.WriteController;
import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UIManager {

    public static void login(String address, ObservableList<Email> inbox) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("dashboard.fxml"));
            Scene scene = new Scene(loader.load(),600,600);
            DashboardController controller = loader.getController();
            controller.setAddress(address);
            controller.setInbox(inbox);
            stage.setTitle(address);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            PopupUtils.showError(e.getClass().getName(),e.getMessage());
        }
    }

    public static void openMessage(String address,ObservableList<Email> inbox,Email email) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("message.fxml"));
            Scene scene = new Scene(loader.load(),600,600);
            stage.setTitle("Messaggio");
            stage.setScene(scene);

            stage.show();
        }catch (IOException e) {
            PopupUtils.showError(e.getClass().getName(),e.getMessage());
        }
    }

    public static void openWrite(String address) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("write.fxml"));
            Scene scene = new Scene(loader.load(),600,600);
            WriteController controller = loader.getController();
            controller.setAddress(address);
            stage.setTitle("Scrivi Email");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            PopupUtils.showError(e.getClass().getName(),e.getMessage());
        }
    }

}
