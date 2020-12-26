package com.cse223.mailserver.UsersAndMails;
//////////////////message creator class//////////////////

import com.cse223.mailserver.Server.Constants;

public class MessageCreator {
    private MessageBody body;
    private MessageHeader header;
    private Attachments attaches;
    private String time;
    private int ID;			///////////////fields of message
    private Message messageSent;
    private Message messageInbox;
    private Message messageDraft;
    private Message messageTrash;
    private boolean priority=false;
    private MessageFactory factory= new MessageFactory();

////////////////set attribute of messageCreator//////////////
    public MessageCreator( MessageHeader header, MessageBody body,Attachments attaches, String time,int ID,boolean priority) {
        this.body = body;
        this.header = header;
        this.attaches = attaches;
        this.time = time;
        this.ID =ID;
        this.priority=priority;
    }

    /////////setters and getters of the message/////////////////
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
///////////////////build sent message////////////////
    public Message buildSentMessage() {
        messageSent= factory.getMessage(Constants.SENT,header,body,attaches,time,ID,priority);
        return  messageSent;
    }
    
///////////////////build inbox message////////////////

    public Message buildInboxMessage() {
        messageInbox= factory.getMessage(Constants.INBOX,header,body,attaches,time,ID,priority);
        return  messageInbox;
    }
    
    
///////////////////build draft message////////////////
    public Message buildDraftMessage() {
        messageDraft= factory.getMessage(Constants.DRAFT,header,body,attaches,time,ID,priority);
        return  messageDraft;
    }
    
    
///////////////////build trash message////////////////
    public Message builTrashMessage() {
        messageTrash= factory.getMessage(Constants.TRASH,header,body,attaches,time,ID,priority);
        return  messageTrash;
    }
//to string function////////////////
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MessageCreator{");
        sb.append("header=").append(header);
        sb.append(", ID=").append(ID);
        sb.append('}');
        return sb.toString();
    }
}