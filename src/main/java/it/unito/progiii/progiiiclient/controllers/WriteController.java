package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.network.MessageManager;
import it.unito.progiii.progiiiclient.utils.AddressUtils;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;

public class WriteController extends MessageManager {

    @FXML
    private TextArea contentText;

    @FXML
    private TextField receiversInput;

    @FXML
    private TextField subjectInput;

    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    protected void composeRequest() {
        String to,subject,content;
        to = receiversInput.getText();
        subject = subjectInput.getText();
        content = contentText.getText();
        request.appendData("operation=send");
        request.appendData("from="+address);
        request.appendData("to="+to);
        request.appendData("subject="+subject);
        request.appendData("CONTENT");
        for (String line:content.split("\n"))
            request.appendData(line.equals("END") ? "\"END\"" : line);
    }

    @Override
    public void parseResponse() {
        HashMap<String,String> headers = response.getHeaders();
        int status = Integer.parseInt(headers.get("status"));
        if(status==5) {
            PopupUtils.showError("Messaggio non inviato","Errore interno del server");
        }else if(status==3)
            PopupUtils.showError("Messaggio non inviato","uno o più indirizzi email non esiste");
        else if(status==1)
            PopupUtils.showError(null,"richiesta malformata");
        else
            close();
    }


    @FXML
    private void sendEmail() {
        if(receiversInput.getText().isEmpty())
            PopupUtils.showError("Messaggio non inviato","Elenco dei destinatari vuoto");
        else if(!AddressUtils.checkReceiversValid(receiversInput.getText()))
            PopupUtils.showError("Messaggio non inviato","Uno o più indirizzi email sono stati scritti in forma errata");
        else {
            composeRequest();
            if(communicate())
                parseResponse();
            else
                PopupUtils.showError(null,"Connessione interrotta");
        }
        clearAll();
    }

    @FXML
    private void close() {
        Scene root = contentText.getScene();
        Stage stage= (Stage) root.getWindow();
        stage.close();
    }

}
