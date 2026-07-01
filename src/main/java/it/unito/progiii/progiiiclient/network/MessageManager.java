package it.unito.progiii.progiiiclient.network;

import it.unito.progiii.progiiiclient.utils.Constants;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public abstract class MessageManager {

    protected MessageBuffer request;
    protected MessageBuffer response;

    public MessageManager() {
        request = new MessageBuffer();
        response = new MessageBuffer();
    }

    public MessageBuffer getRequest() {
        return request;
    }

    public MessageBuffer getResponse() {
        return response;
    }

    private void receiveResponse(Scanner in) {
        boolean end = false;
        while (!end && in.hasNextLine()) {
            String data = in.nextLine();
            if (data.equals("END"))
                end = true;
            else
                response.appendData(data);
        }
    }

    private void sendRequest(PrintWriter out) {
        for(String data: request.getMessageData())
            out.println(data);
        out.println("END");
    }

    protected boolean communicate() {
        boolean flag = true;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost",Constants.PORT));
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            sendRequest(out);
            receiveResponse(in);
        }catch (IOException e) {
            flag = false;
        }
        return flag;
    }


    protected abstract void composeRequest();
    protected abstract void parseResponse();

    protected void clearAll() {
        request.clear();
        response.clear();
    }

}
