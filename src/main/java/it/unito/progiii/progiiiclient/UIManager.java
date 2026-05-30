package it.unito.progiii.progiiiclient;

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
            stage.setTitle(address);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            PopupUtils.showError(e.getClass().getName(),e.getMessage());
        }
    }

}
