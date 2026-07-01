package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.MessageManager;
import it.unito.progiii.progiiiclient.utils.Constants;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class LoginController extends MessageManager {

    @FXML
    private TextField emailInput;

    private String input;

    @FXML
    private void initialize() {
        input = "";
    }

    @Override
    protected void composeRequest() {
        request.appendData("operation=load");
        request.appendData("from="+emailInput.getText());
    }

    private void openDashboard(ObservableList<Email> inbox) {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.CP_ROOT+"dashboard.fxml"));
            Scene scene = new Scene(loader.load(),600,600);
            DashboardController controller = loader.getController();
            stage.setTitle(input);
            stage.setScene(scene);
            controller.putParameters(input,inbox);
            stage.show();
        }catch (IOException e) {
            PopupUtils.showError(e.getClass().getName(),e.getMessage());
        }
    }

    @Override
    protected void parseResponse() {
        HashMap<String,String> headers = response.getHeaders();
        int status = Integer.parseInt(headers.get("status"));
        if(status==0) {
            String content = response.getContent();
            Email[] emails = Constants.GSON.fromJson(content,Email[].class);
            ObservableList<Email> inbox = FXCollections.observableArrayList(Arrays.asList(emails));
            openDashboard(inbox);
            closeStage();
        }else
            PopupUtils.showError("Accesso fallito","Indirizzo email inserito inesistente");
    }


    @FXML
    private void submit() {
        input = emailInput.getText().trim();
        if(input.isEmpty())
            PopupUtils.showError("Accesso fallito","Inserire un indirizzo email, es: foobar@example.com");
        else if(!input.matches(Constants.EMAIL_REGEX))
            PopupUtils.showError("Accesso fallito","Indirizzo email non valido");
        else {
            composeRequest();
            if(communicate())
                parseResponse();
            else
                PopupUtils.showError("Accesso fallito","Server non raggiungibile");
        }
        clearAll();
    }

    public void closeStage() {
        Stage stage = (Stage) emailInput.getScene().getWindow();
        stage.close();
    }

}
