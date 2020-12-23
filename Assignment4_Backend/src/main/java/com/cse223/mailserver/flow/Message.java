package com.cse223.mailserver.flow;

import java.util.ArrayList;
import java.util.Date;

public class Message {
    private int  ID;
    private MessageBody body;
    private MessageHeader header;
    private Attachments attaches;
    private String time;


    public Message(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time) {
        super();
        ID = iD;
        this.body = body;
        this.header = header;
        this.attaches = attaches;
        this.time = time;
    }

    public int getID() {
        return ID;
    }
    public MessageBody getBody() {
        return body;
    }
    public MessageHeader getHeader() {
        return header;
    }
    public Attachments getAttaches() {
        return attaches;
    }
    public String getTime() {
        return time;
    }



}
