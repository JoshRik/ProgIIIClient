package it.unito.progiii.progiiiclient.model;

import java.time.LocalDate;
import java.util.Vector;

public class Email {

    private long id;
    private String sender;
    private Vector<String> receivers;
    private LocalDate sendDate;
    private String subject;
    private String text;

    public Email(long id, String sender, LocalDate sendDate, String title, String text) {
        this.id = id;
        this.sender = sender;
        this.receivers = new Vector<>();
        this.sendDate = sendDate;
        this.subject = title;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public Vector<String> getReceivers() {
        return receivers;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getClass().isInstance(obj)) {
            Email tmp = (Email)obj;
            return tmp.id == id;
        }else
            return false;
    }

    public void addReceiver(String receiver) {
        if(receivers.contains(receiver))
            receivers.add(receiver);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(subject).append("\n");
        sb.append(sender).append("\n");
        return sb.toString();
    }
}
