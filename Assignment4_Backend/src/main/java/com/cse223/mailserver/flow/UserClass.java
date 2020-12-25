package com.cse223.mailserver.flow;
import java.util.ArrayList;


public class UserClass implements User {
    private String userName;
    private String password;
    private String Email;
    private String Birthday;
    private ArrayList<Contact> Contacts;
    private ArrayList<Sent> sentMessage;
    private ArrayList<Inbox> inbox;
    private ArrayList<Draft> draft;
    private ArrayList<Trash> trash;

    public UserClass(String userName, String password, String email, String birthday) {
        super();
        this.userName = userName;
        this.password = password;
        Email = email;
        Birthday = birthday;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<Contact> getContacts() {
        return Contacts;
    }
    public void setContacts(ArrayList<Contact> contacts) {
        Contacts = contacts;
    }
    public ArrayList<Sent> getSentMessage() {
        return sentMessage;
    }
    public void setSentMessage(ArrayList<Sent> sentMessage) { this.sentMessage = sentMessage; }
    public ArrayList<Inbox> getInbox() {
        return inbox;
    }
    public void setInbox(ArrayList<Inbox> inbox) {
        this.inbox = inbox;
    }
    public ArrayList<Draft> getDraft() {
        return draft;
    }
    public void setDraft(ArrayList<Draft> draft) {
        this.draft = draft;
    }
    public ArrayList<Trash> getTrash() {
        return trash;
    }
    public void setTrash(ArrayList<Trash> trash) {
        this.trash = trash;
    }
    public String getEmail() {
        return Email;
    }

    public String getBirthday() {
        return Birthday;
    }


    public void addsentMessage(Sent message){
        if(sentMessage == null)
            sentMessage = new ArrayList<>();
        System.out.println(message);
        System.out.println(sentMessage);
        sentMessage.add(message);
    }
    public void addInboxMessage(Inbox message){
        if(inbox == null)
            inbox = new ArrayList<>();
        System.out.println(message);
        System.out.println(sentMessage);
        inbox.add(message);
    }
    public void addDraftMessage(Draft message){
        if(draft == null)
            draft = new ArrayList<>();
        System.out.println(message);
        System.out.println(sentMessage);
        draft.add(message);
    }
    public void addTrashMessage(Trash message){
        if(trash == null)
            trash = new ArrayList<>();
        System.out.println(message);
        System.out.println(sentMessage);
        trash.add(message);
    }
    public void addContact(Contact myContact){
        if(Contacts == null)
            Contacts = new ArrayList<>();
        Contacts.add(myContact);
    }



}