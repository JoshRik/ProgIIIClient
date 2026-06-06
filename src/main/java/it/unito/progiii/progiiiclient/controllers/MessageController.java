package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MessageController {

    @FXML
    private TextArea contentTextArea;

    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label dateLabel;

    private Email email;

    public void setEmail(Email email) {
        this.email = email;
        senderLabel.setText(email.getSender());
        subjectLabel.setText(email.getSubject());
        dateLabel.setText(email.getSendDate().toString());
        contentTextArea.setText(email.getText());
    }

}
