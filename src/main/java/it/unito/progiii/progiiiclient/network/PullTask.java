package it.unito.progiii.progiiiclient.network;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.state.StateManager;
import it.unito.progiii.progiiiclient.utils.Constants;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PullTask implements Runnable{

    private boolean open;
    private StateManager stateManager;
    private ObservableList<Email> inbox;

    public PullTask(ObservableList<Email> inbox, StateManager stateManager) {
        this.inbox = inbox;
        this.stateManager = stateManager;
        open = false;
    }

    public void stop() {
         open = false;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress("localhost", Constants.PORT));
            stateManager.putConnected();

        }catch (IOException e) {
            stateManager.putDisconnected();
        }
    }
}
