package com.cse223.mailserver.flow;


import java.util.ArrayList;

public class MessageHeader {
    private String sender;
    private ArrayList<String> reciever;
    private String subject;
    private String folderName;


    public MessageHeader(String sender, ArrayList<String> reciever, String subject,String folderName ){
        this.sender = sender;
        this.reciever = reciever;
        this.subject = subject;
        this.folderName=folderName;
    }


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



}