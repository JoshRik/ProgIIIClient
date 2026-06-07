package it.unito.progiii.progiiiclient.controllers;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.network.MessageManager;
import it.unito.progiii.progiiiclient.network.PullTask;
import it.unito.progiii.progiiiclient.state.StateManager;
import it.unito.progiii.progiiiclient.utils.AddressUtils;
import it.unito.progiii.progiiiclient.utils.Constants;
import it.unito.progiii.progiiiclient.utils.PopupUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController extends MessageManager {

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
    private Email currentSelected;
    private String operation;


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
        setMouseListener();
        operation="";
    }

    private void setMouseListener() {
        root.setOnMousePressed(event -> {
            Node target = (Node) event.getTarget();

            while (target != null) {
                if (target == inboxView)
                    return; // click sulla ListView
                target = target.getParent();
            }

            // click fuori dalla ListView
            inboxView.getSelectionModel().clearSelection();
            currentSelected = inboxView.getSelectionModel().getSelectedItem();
        });
    }

    private void setInboxListener() {
        inboxView.setOnMouseClicked(mouseEvent -> {
            currentSelected = inboxView.getSelectionModel().getSelectedItem();
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
        setInboxListener();
        startScheduler();
        setCloseOperation();
    }

    private void setCloseOperation() {
        Stage stage = (Stage) inboxView.getScene().getWindow();
        stage.setOnCloseRequest((event) -> {
            pullScheduler.shutdownNow();
        });
    }

    @FXML
    private void write() {
        Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.CP_ROOT+"write.fxml"));
            Scene scene = new Scene(loader.load(),600,600);
            WriteController controller = loader.getController();
            controller.setAddress(address);
            stage.setTitle("Scrivi Email");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            PopupUtils.showError(e.getClass().getName(),e.getMessage());
        }
    }

    @FXML
    private void details() {
        if(currentSelected!=null) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.CP_ROOT+"details.fxml"));
            try {
                Scene scene = new Scene(loader.load(),580,580);
                DetailsController controller = loader.getController();
                controller.setEmail(currentSelected);
                stage.setTitle("Visualizza messaggio");
                stage.setScene(scene);
                stage.show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void delete() {
        if(currentSelected!=null) {
            operation="delete";
            composeRequest();
            if(communicate()) {
                parseResponse();
            }else
                PopupUtils.showError("Accesso non riuscito","errore di connessione");
            clearAll();
            operation="";
        }
    }

    @FXML
    private void forward() {
        if(currentSelected!=null) {
            operation="forward";
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Inserire destinatari");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                String recipients = value.trim();
                if(recipients.isEmpty())
                    PopupUtils.showError("Innoltro non riuscito","Inserire una serie di indirizzi email");
                else if(!AddressUtils.checkReceiversValid(recipients))
                    PopupUtils.showError("Innoltro non riuscito","sono stati inseriti indirizzi email non validi");
                else{
                    composeForwardRequest(recipients);
                    if(communicate())
                        parseResponse();
                    else
                        PopupUtils.showError("Innoltro non riuscito","Problema di connessione al server");
                }
            });
            resetSelection();
        }
    }

    private void resetSelection() {
        currentSelected = null;
        inboxView.getSelectionModel().clearSelection();
    }

    @FXML
    private void reply() {
        if(currentSelected!=null) {
            Stage stage = new Stage();
            try {
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.CP_ROOT+"reply.fxml"));
                Scene scene = new Scene(loader.load(),600,600);
                ReplyController controller = loader.getController();
                controller.setAddress(address);
                controller.setEmail(currentSelected);
                stage.setTitle("Scrivi Email");
                stage.setScene(scene);
                stage.show();
            }catch (IOException e) {
                PopupUtils.showError(e.getClass().getName(),e.getMessage());
            }
        }
    }

    private void composeForwardRequest(String recipients) {
        composeRequest();
        request.appendData("to="+recipients);
    }

    @Override
    protected void composeRequest() {
        request.appendData("operation="+operation);
        request.appendData("from="+address);
        request.appendData("id="+currentSelected.getId());
    }

    @Override
    protected void parseResponse() {
        HashMap<String,String> headers = response.getHeaders();
        int status = Integer.parseInt(headers.get("status"));
        if(status == 1)
            PopupUtils.showError("Operazione fallita","richiesta malformata");
        else if(status == 2)
            PopupUtils.showError("Operazione fallita","operazione sconosciuta");
        else if(status == 3)
            PopupUtils.showError("Operazione fallita","uno o più indirizzi email non esiste");
        else if(status == 4)
            PopupUtils.showError("Operazioen fallita","messaggio inesistente");
        else if(status == 5)
            PopupUtils.showError("Operazione fallita","Errore interno del server");
        else {
            if(operation.equals("delete")) {
                inbox.remove(currentSelected);
                resetSelection();
            }
        }
    }
}
