package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.network.MessageBuffer;
import it.unito.progiii.progiiiclient.utils.AddressUtils;
import it.unito.progiii.progiiiclient.utils.ConnectUtils;
import it.unito.progiii.progiiiclient.utils.Constants;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class WriteController {

    @FXML
    private TextArea contentText;

    @FXML
    private TextField receiversInput;

    @FXML
    private TextField subjectInput;

    private String address;

    private MessageBuffer request;
    private MessageBuffer response;

    @FXML
    private void initialize() {
        request = new MessageBuffer();
        response = new MessageBuffer();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private void composeRequest() {
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

    public void parseResponse() {
        HashMap<String,String> headers = response.getHeaders();
        int status = Integer.parseInt(headers.get("status"));
        if(status==5) {
            PopupUtils.showError("Messaggio non inviato","Errore interno del server");
        }else if(status==3)
            PopupUtils.showError("Messaggio non inviato","uno o più indirizzi email non esiste");
        else if(status==1)
            PopupUtils.showError(null,"richiesta malformata");
        else{
            PopupUtils.showInfo("Messaggio inviato");
            close();
        }
    }


    @FXML
    private void sendEmail() {
        if(receiversInput.getText().isEmpty())
            PopupUtils.showError("Messaggio non inviato","Elenco dei destinatari vuoto");
        else if(!AddressUtils.checkReceiversValid(receiversInput.getText()))
            PopupUtils.showError("Messaggio non inviato","Uno o più indirizzi email sono stati scritti in forma errata");
        else {
            composeRequest();
            if(ConnectUtils.communicate(request,response))
                parseResponse();
            else
                PopupUtils.showError(null,"Connessione interrotta");
        }
        request.clear();
        response.clear();
    }

    @FXML
    private void close() {
        Scene root = contentText.getScene();
        Stage stage= (Stage) root.getWindow();
        stage.close();
    }

}
