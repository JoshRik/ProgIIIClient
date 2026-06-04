package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.UIManager;
import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.MessageBuffer;
import it.unito.progiii.progiiiclient.utils.ConnectUtils;
import it.unito.progiii.progiiiclient.utils.Constants;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class LoginController {

    @FXML
    private TextField emailInput;

    private MessageBuffer request;
    private MessageBuffer response;

    private void composeLoadRequest(String address) {
        request.appendData("operation=load");
        request.appendData("from="+address);
    }

    @FXML
    private void initialize() {
        request = new MessageBuffer();
        response = new MessageBuffer();
    }


    @FXML
    private void submit() {
        String input = emailInput.getText().trim();
        if(input.isEmpty())
            PopupUtils.showError("Accesso fallito","Inserire un indirizzo email, es: foobar@example.com");
        else if(!input.matches(Constants.EMAIL_REGEX))
            PopupUtils.showError("Accesso fallito","Indirizzo email non valido");
        else {
            composeLoadRequest(input);
            if(ConnectUtils.communicate(request,response)) {
                HashMap<String,String> headers = response.getHeaders();
                int status = Integer.parseInt(headers.get("status"));
                if(status==0) {
                    String content = response.getContent();
                    Email[] emails = Constants.GSON.fromJson(content,Email[].class);
                    ObservableList<Email> inbox = FXCollections.observableArrayList(Arrays.asList(emails));
                    UIManager.login(input,inbox);
                    closeStage();
                }else
                    PopupUtils.showError("Accesso fallito","Indirizzo email non trovato");
            }else
                PopupUtils.showError(null,"Server non raggiungibile");
        }
        request.clear();
        response.clear();
    }

    public void closeStage() {
        Stage stage = (Stage) emailInput.getScene().getWindow();
        stage.close();
    }

}
