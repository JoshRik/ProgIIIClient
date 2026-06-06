package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.UIManager;
import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.MessageManager;
import it.unito.progiii.progiiiclient.utils.Constants;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;

public class LoginController extends MessageManager {

    @FXML
    private TextField emailInput;

    @Override
    protected void composeRequest() {
        request.appendData("operation=load");
        request.appendData("from="+emailInput.getText());
    }

    @Override
    protected void parseResponse() {
        HashMap<String,String> headers = response.getHeaders();
        int status = Integer.parseInt(headers.get("status"));
        if(status==0) {
            String content = response.getContent();
            Email[] emails = Constants.GSON.fromJson(content,Email[].class);
            ObservableList<Email> inbox = FXCollections.observableArrayList(Arrays.asList(emails));
            UIManager.login(emailInput.getText().trim(),inbox);
            closeStage();
        }else
            PopupUtils.showError("Accesso fallito","Indirizzo email non trovato");
    }


    @FXML
    private void submit() {
        String input = emailInput.getText().trim();
        if(input.isEmpty())
            PopupUtils.showError("Accesso fallito","Inserire un indirizzo email, es: foobar@example.com");
        else if(!input.matches(Constants.EMAIL_REGEX))
            PopupUtils.showError("Accesso fallito","Indirizzo email non valido");
        else {
            composeRequest();
            if(communicate())
                parseResponse();
            else
                PopupUtils.showError(null,"Server non raggiungibile");
        }
        clearAll();
    }

    public void closeStage() {
        Stage stage = (Stage) emailInput.getScene().getWindow();
        stage.close();
    }

}
