package com.cse223.mailserver.UsersAndMails;


import java.util.ArrayList;
////////////////message header class////////////
public class MessageHeader {
    private String sender;
    private ArrayList<String> reciever; //fields of messgaes header 
    private String subject;
    private String folderName;
    private boolean priority;

/////////////set attribut of message header ///////////////
    public MessageHeader(String sender, ArrayList<String> reciever, String subject,String folderName ,boolean priority){
        this.sender = sender;
        this.reciever = reciever;
        this.subject = subject;
        this.folderName=folderName;
        this.priority=priority;
    }

//setters and getters of message header
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ArrayList<String> getReciever() {
        return reciever;
    }

    public void setReciever(ArrayList<String> reciever) {
        this.reciever = reciever;
    }

    public String getSubject() {
        return subject;
    }


    public String getFolderName() {
        return folderName;
    }


    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MessageHeader{");
        sb.append("sender='").append(sender).append('\'');
        sb.append(", reciever=").append(reciever);
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", folderName='").append(folderName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}