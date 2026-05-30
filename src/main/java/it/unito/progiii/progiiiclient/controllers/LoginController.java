package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.network.MessageBuffer;
import it.unito.progiii.progiiiclient.utils.Constants;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.Socket;

public class LoginController {

    @FXML
    private TextField emailInput;

    private MessageBuffer composeLoadRequest(String address) {
        MessageBuffer request = new MessageBuffer();
        request.appendData("operation=load");
        request.appendData("from="+address);
        request.appendData("END");
        return request;
    }

    @FXML
    private void submit() {
        String input = emailInput.getText().trim();
        if(input.isEmpty())
            PopupUtils.showError("Accesso fallito","Inserire un indirizzo email, es: foobar@example.com");
        else if(!input.matches(Constants.EMAIL_REGEX))
            PopupUtils.showError("Accesso fallito","Indirizzo email non valido");
        else{
            MessageBuffer request = composeLoadRequest(input);

        }
    }

}
