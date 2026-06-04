package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.MessageBuffer;
import it.unito.progiii.progiiiclient.utils.ConnectUtils;
import it.unito.progiii.progiiiclient.utils.AddressUtils;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Optional;

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
    private MessageBuffer request;
    private MessageBuffer response;

    public void setInbox(ObservableList<Email> inbox) {
        this.inbox = inbox;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(Email email) {
        this.email = email;
        senderLabel.setText(email.getSender());
        subjectLabel.setText(email.getSubject());
        dateLabel.setText(email.getSendDate().toString());
        contentTextArea.setText(email.getText());
    }

    @FXML
    private void initialize() {
        request = new MessageBuffer();
        response = new MessageBuffer();
    }

    @FXML
    private void reply() {

    }

    @FXML
    private void replyAll() {
    }

    @FXML
    private void forward() {
        TextInputDialog receiversDialog = new TextInputDialog("");
        receiversDialog.setContentText("Inoltra");
        receiversDialog.setHeaderText("Inserire i destinatari separati da spazi");
        Optional<String> result = receiversDialog.showAndWait();
        result.ifPresent( to -> {
            if(!to.isEmpty() && AddressUtils.checkReceiversValid(to)) {
                composeForwardRequest(to);
                if(ConnectUtils.communicate(request,response)) {
                    HashMap<String,String> headers = response.getHeaders();
                    int status = Integer.parseInt(headers.get("status"));
                    if(status==5)
                        PopupUtils.showError(null,"Errore interno al server");
                    else if (status==4)
                        PopupUtils.showError(null,"Messaggio inesistente");
                    else
                        PopupUtils.showInfo("Messaggio inoltrato con successo");
                }
                else
                    PopupUtils.showError(null,"Server non raggiungibile");
            }else
                PopupUtils.showError(null,"Valore vuoto o indirizzi email non validi");
        });
    }

    @FXML
    private void delete() {
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setHeaderText(null);
        conf.setTitle("Conferma eliminazione");
        conf.setContentText("Sei sicuro di voler eliminare il messaggio?");
        Optional<ButtonType> result = conf.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            composeDeleteRequest();
            if(ConnectUtils.communicate(request,response))
                parseDeleteResponse();
            else
                PopupUtils.showError(null,"Server non raggiungibile");
        }
        clearMessage();
    }

    private void composeDeleteRequest() {
        request.appendData("operation=delete");
        request.appendData("from="+address);
        request.appendData("id="+email.getId());
    }

    private void composeForwardRequest(String receivers) {
        request.appendData("operation=forward");
        request.appendData("to="+receivers);
        request.appendData("id="+email.getId());
    }

    private void parseDeleteResponse() {
        HashMap<String,String > headers = response.getHeaders();
        int status = Integer.parseInt(headers.get("status"));
        if(status==5)
            PopupUtils.showError(null,"errore interno del server");
        else if(status==4)
            PopupUtils.showError(null,"Messaggio inesistente");
        else {
            inbox.remove(email);
            Stage stage = (Stage) contentTextArea.getScene().getWindow();
            stage.close();
        }
    }

    private void clearMessage() {
        request.clear();
        response.clear();
    }


}
