package it.unito.progiii.progiiiclient.utils;

import it.unito.progiii.progiiiclient.network.MessageBuffer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectUtils {

    public static void sendMessage(MessageBuffer message, PrintWriter out) {
        for(String data: message.getMessageData())
            out.println(data);
        out.println("END");
    }

    public static void receiveMessage(MessageBuffer message, Scanner in) {
        boolean end = false;
        while (!end && in.hasNextLine()) {
            String data = in.nextLine();
            if (data.equals("END"))
                end = true;
            else
                message.appendData(data);
        }
    }

    public static boolean communicate(MessageBuffer request,MessageBuffer response) {
        boolean flag = true;
        try (
                Socket socket = new Socket("localhost",Constants.PORT);
                Scanner in = new Scanner(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream())
                ) {
            sendMessage(request,out);
            receiveMessage(response,in);
        }catch (IOException e) {
            flag = false;
        }
        return flag;
    }

}
