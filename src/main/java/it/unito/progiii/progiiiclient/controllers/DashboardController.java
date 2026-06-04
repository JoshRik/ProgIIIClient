package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.UIManager;
import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.PullTask;
import it.unito.progiii.progiiiclient.state.StateManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController {

    @FXML
    private BorderPane root;

    @FXML
    private Label userLabel;

    @FXML
    private Label stateLabel;

    @FXML
    private ListView<Email> inboxView;

    private String address;
    private ObservableList<Email> inbox;
    private StateManager stateManager;
    private PullTask pullTask;
    private ScheduledExecutorService pullScheduler;


    @FXML
    private void initialize(){
        stateManager = new StateManager();
        stateLabel.textProperty().bind(stateManager.stateProperty().map( state -> switch (state) {
            case DISCONNECTED -> "Non connesso";
            case CONNECTED -> "Connesso";
        }));
        stateLabel.textFillProperty().bind(stateManager.stateProperty().map( state -> switch (state) {
            case CONNECTED -> Color.GREEN;
            case DISCONNECTED -> Color.RED;
        }));
        root.setOnMousePressed(event -> {
            Node target = (Node) event.getTarget();

            while (target != null) {
                if (target == inboxView) {
                    return; // click sulla ListView
                }
                target = target.getParent();
            }

            // click fuori dalla ListView
            inboxView.getSelectionModel().clearSelection();
        });
    }

    public void setInbox(ObservableList<Email> inbox) {
        this.inbox = inbox;
        inboxView.setItems(inbox);
        inboxView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount()==2) {
                Email email = inboxView.getSelectionModel().getSelectedItem();
                if(email!=null) {
                    UIManager.openMessage(address,inbox,email);
                    inboxView.getSelectionModel().clearSelection();
                }
            }
            if(inboxView.getSelectionModel().getSelectedIndex()<0)
                inboxView.getSelectionModel().clearSelection();
        });

    }

    private void startScheduler() {
        pullScheduler = Executors.newSingleThreadScheduledExecutor();
        pullScheduler.scheduleAtFixedRate(pullTask,0,2, TimeUnit.SECONDS);
    }

    public void putParameters(String address,ObservableList<Email> inbox) {
        this.address = address;
        this.inbox = inbox;
        userLabel.setText(address);
        pullTask = new PullTask(address,inbox,stateManager);
        inboxView.setItems(inbox);
        startScheduler();
    }

    @FXML
    private void write() {
        UIManager.openWrite(address);
    }

    public void stopScheduler() {
        pullScheduler.shutdownNow();
    }

}
