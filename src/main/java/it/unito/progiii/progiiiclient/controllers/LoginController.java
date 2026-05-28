package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.utils.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class LoginController {

    @FXML
    private TextField emailInput;

    private void errPrint(String err) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Errore");
        error.setHeaderText("Accesso non riuscito");
        error.setContentText(err);
        error.showAndWait();
    }

    @FXML
    private void submit() {
        String input = emailInput.getText();
        if(input.isEmpty())
            errPrint("Inserire un indirizzo email");
        else if(!input.matches(Constants.EMAIL_REGEX))
            errPrint("Indirizzo email non valido");
        else{

        }
    }

}
