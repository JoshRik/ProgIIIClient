package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.MessageManager;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.HashMap;

public class ReplyController extends MessageManager {

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private Label contentLabel; //label of message

    @FXML
    private TextArea contentTextArea;

    @FXML
    private CheckBox replyAll;

    private String address;
    private Email email;

    public void setEmail(Email email) {
        this.email = email;
        contentLabel.setText(email.getText());
        subjectLabel.setText(email.getSubject());
        senderLabel.setText(email.getSender());
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    protected void parseResponse() {
        HashMap<String,String> headers = response.getHeaders();
        int status = Integer.parseInt(headers.get("status"));
        if(status==5)
            PopupUtils.showError("Risposta non inviata","Errore interno del server");
        else if(status==4)
            PopupUtils.showError("Risposta non inviata","Messaggio inesistente");
        else{
            closeStage();
            PopupUtils.showNotification("Risposta inviata");
        }
    }

    @Override
    protected void composeRequest() {
        request.appendData("operation="+(replyAll.isSelected() ? "reply-all" : "reply"));
        request.appendData("from="+address);
        request.appendData("id="+email.getId());
        request.appendData("CONTENT");
        for (String line :contentTextArea.getText().split("\n"))
            request.appendData(line.equals("END") ? "\"END\"" : line);
    }

    @FXML
    private void submit() {
        composeRequest();
        if(communicate())
            parseResponse();
        else
            PopupUtils.showError("Messaggio non inviato","Errore di connessione");
    }

    @FXML
    private void closeStage() {
        Stage stage = (Stage) contentTextArea.getScene().getWindow();
        stage.close();
    }
}
