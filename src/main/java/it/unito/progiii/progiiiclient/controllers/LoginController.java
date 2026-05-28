package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.utils.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class LoginController {

    @FXML
    private TextField emailInput;

    @FXML
    private Label errorLabel;

    @FXML
    private void submit() {
        String input = emailInput.getText();
        if(!input.matches(Constants.EMAIL_REGEX))
            errorLabel.setText("Indirizzo email non valido");
        else{
            try(Socket socket = new Socket("localhost",Constants.PORT)){

            }catch (IOException e) {
                errorLabel.setText("Connessione non riuscita");
            }
        }
    }

}
