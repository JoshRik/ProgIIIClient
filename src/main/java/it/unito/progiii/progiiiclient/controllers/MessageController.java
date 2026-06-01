package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MessageController {

    @FXML
    private TextArea contentTextArea;

    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label dateLabel;

    private String address;
    private ObservableList<Email> inbox;
    private Email email;

    public void setInbox(ObservableList<Email> inbox) {
        this.inbox = inbox;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Email getEmail() {
        return email;
    }


}
