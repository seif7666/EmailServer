package com.cse223.mailserver.flow;

import java.util.ArrayList;
import java.util.Date;

public class Inbox extends Message {
    private String from;

    public Inbox(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time , boolean priority) {
        super(iD, body, header, attaches, time , priority);
        
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }



}
