package it.unito.progiii.progiiiclient.network;

import java.util.ArrayList;

public class MessageBuffer {

    private ArrayList<String> responseComponents;

    public MessageBuffer() {
        responseComponents = new ArrayList<>();
    }

    public void appendData(String data) {
        responseComponents.add(data);
    }

    public boolean hasContent() {
        return responseComponents.contains("CONTENT");
    }

    private int contentIndex() {
        return responseComponents.indexOf("CONTENT");
    }

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        if(hasContent()) {

        }
        return sb.toString();
    }

}
