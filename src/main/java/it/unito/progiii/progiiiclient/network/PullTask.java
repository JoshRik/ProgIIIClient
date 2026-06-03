package it.unito.progiii.progiiiclient.network;

import it.unito.progiii.progiiiclient.model.Email;
import it.unito.progiii.progiiiclient.state.ConnectState;
import it.unito.progiii.progiiiclient.state.StateManager;
import it.unito.progiii.progiiiclient.utils.ConnectUtils;
import it.unito.progiii.progiiiclient.utils.Constants;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class PullTask implements Runnable{

    private String address;
    private StateManager stateManager;
    private ObservableList<Email> inbox;
    private MessageBuffer request;
    private MessageBuffer response;

    public PullTask(String address,ObservableList<Email> inbox, StateManager stateManager) {
        this.address = address;
        this.inbox = inbox;
        this.stateManager = stateManager;
        request = new MessageBuffer();
        response = new MessageBuffer();
    }

    private void composeRequest() {
        request.appendData("operation=pull");
        request.appendData("from="+address);

    }

    private void parse() {
        String content = response.getContent();
        Email[] newEmails = Constants.GSON.fromJson(content,Email[].class);
        for(Email email:newEmails) {
            if(!inbox.contains(email))
                inbox.add(email);
        }
    }

    @Override
    public void run() {
        try (
                Socket socket = new Socket("localhost",Constants.PORT);
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        ){
            Platform.runLater(() -> stateManager.setState(ConnectState.CONNECTED));
            composeRequest();
            ConnectUtils.sendMessage(request,out);
            ConnectUtils.receiveMessage(response,in);
            parse();
            request.clear();
            response.clear();
        }catch (IOException e) {
            Platform.runLater(() -> stateManager.setState(ConnectState.DISCONNECTED));
        }
    }
}
