package it.unito.progiii.progiiiclient.network;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.state.ConnectState;
import it.unito.progiii.progiiiclient.state.StateManager;
import it.unito.progiii.progiiiclient.utils.Constants;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class PullTask extends MessageManager implements Runnable{

    private String address;
    private StateManager stateManager;
    private ObservableList<Email> inbox;

    public PullTask(String address,ObservableList<Email> inbox, StateManager stateManager) {
        this.address = address;
        this.inbox = inbox;
        this.stateManager = stateManager;
    }

    @Override
    protected void composeRequest() {
        request.appendData("operation=pull");
        request.appendData("from="+address);

    }

    @Override
    protected void parseResponse() {
        String content = response.getContent();
        Email[] newEmails = Constants.GSON.fromJson(content,Email[].class);
        for(Email email:newEmails) {
            if(!inbox.contains(email))
                Platform.runLater(() -> inbox.add(email));
        }
        if(newEmails.length!=0)
            Platform.runLater(() -> showNotification(newEmails.length));
    }

    @Override
    public void run() {
        try {
            composeRequest();
            if(communicate()) {
                Platform.runLater( () -> stateManager.setState(ConnectState.CONNECTED));
                parseResponse();
            }else
                Platform.runLater( () -> stateManager.setState(ConnectState.DISCONNECTED));
            clearAll();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(int count) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifica");
        alert.setHeaderText(null);
        alert.setContentText(count == 1 ? "Nuovo messaggio" : count+" nuovi messaggi");
        alert.initOwner(null);
        alert.show();

        // Chiudi automaticamente dopo 4 secondi
        new Thread(() -> {
            try { Thread.sleep(4000); }
            catch (InterruptedException ignored) {}
            Platform.runLater(alert::close);
        }).start();
    }

}
