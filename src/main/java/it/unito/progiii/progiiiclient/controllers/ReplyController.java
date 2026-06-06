package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.MessageBuffer;
import it.unito.progiii.progiiiclient.network.MessageManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ReplyController extends MessageManager {

    @FXML
    private TextArea contentTextArea;

    private Email email;

    @Override
    protected void parseResponse() {

    }

    @Override
    protected void composeRequest() {

    }
}
