package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.network.MessageBuffer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WriteController {

    @FXML
    private TextArea contentText;

    @FXML
    private TextField receiversInput;

    @FXML
    private TextField subjectInput;

    @FXML
    private void sendEmail() {
        String content,receivers,subject;
        content = contentText.getText();
        receivers = receiversInput.getText();
        subject = subjectInput.getText();
        close();
    }

    private void close() {
        Scene root = contentText.getScene();
        Stage stage= (Stage) root.getWindow();
        stage.close();
    }


}
