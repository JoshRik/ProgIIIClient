package it.unito.progiii.progiiiclient.network;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageBuffer {

    private ArrayList<String> messageData;

    public MessageBuffer() {
        messageData = new ArrayList<>();
    }

    public void appendData(String data) {
        messageData.add(data);
    }

    public ArrayList<String> getMessageData() {
        return new ArrayList<>(messageData);
    }

    public boolean hasContent() {
        return messageData.contains("CONTENT");
    }

    private int contentIndex() {
        return messageData.indexOf("CONTENT");
    }

    public HashMap<String,String> getHeaders() {
        HashMap<String,String> headers = new HashMap<>();
        int index = hasContent() ? contentIndex() : messageData.size();
        for (int J=0;J<index;J++) {
            String header,key,value;
            header = messageData.get(J);
            String[] headerComponents = header.split("=");
            key = headerComponents[0];
            value = headerComponents[1];
            headers.put(key,value);
        }
        return headers;
    }

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        if(hasContent()) {
            for(int K=contentIndex()+1;K<messageData.size();K++)
                sb.append(messageData.get(K)).append("\n");
        }
        return sb.toString();
    }

    public int numberOfLines() {
        return messageData.size();
    }


}
