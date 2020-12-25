package com.cse223.mailserver.flow;



import java.util.ArrayList;
import java.util.Date;

public class MessageCreator {
    private MessageBody body;
    private MessageHeader header;
    private Attachments attaches;
    private String time;
    private int ID;
    private Message messageSent;
    private Message messageInbox;
    private Message messageDraft;
    private Message messageTrash;
    private boolean priority=false;


    public MessageCreator( MessageHeader header, MessageBody body,Attachments attaches, String time,int ID,boolean priority) {
        this.body = body;
        this.header = header;
        this.attaches = attaches;
        this.time = time;
        this.ID =ID;
        this.priority=priority;
    }

    public MessageBody getBody() {
        return body;
    }
    public void setBody(MessageBody body) {
        this.body = body;
    }
    public MessageHeader getHeader() {
        return header;
    }
    public void setHeader(MessageHeader header) {
        this.header = header;
    }
    public Attachments getAttaches() {
        return attaches;
    }
    public void setAttaches(Attachments attaches) {
        this.attaches = attaches;
    }
    public Message getMessage() {
        return messageSent;
    }
    public void setMessage(Sent message) {
        this.messageSent = message;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Message buildSentMessage() {
        messageSent= new Sent(ID,body,header,attaches,time , priority);
        return  messageSent;
    }
    public Message buildInboxMessage() {
        messageInbox= new Inbox(ID,body,header,attaches,time , priority);
        return  messageInbox;
    }
    public Message buildDraftMessage() {
        messageDraft= new Draft(ID,body,header,attaches,time , priority);
        return  messageDraft;
    }
    public Message builTrashMessage() {
        messageTrash= new Trash(ID,body,header,attaches,time , priority);
        return  messageTrash;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MessageCreator{");
        sb.append("header=").append(header);
        sb.append(", ID=").append(ID);
        sb.append('}');
        return sb.toString();
    }
}