package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.PullTask;
import it.unito.progiii.progiiiclient.state.StateManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class DashboardController {

    @FXML
    private Label userLabel;

    @FXML
    private Label stateLabel;

    private StateManager stateManager;
    private PullTask pullTask;



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
        stateManager = new StateManager();
    }

}
