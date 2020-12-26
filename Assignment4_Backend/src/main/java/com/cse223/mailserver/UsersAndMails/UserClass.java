package com.cse223.mailserver.UsersAndMails;
import java.util.ArrayList;

////////user class implement interface User///////////////
public class UserClass implements User {
    private String userName;
    private String password;
    private String Email;
    private String Birthday;   ///fields of user class
    private ArrayList<Contact> Contacts;
    private ArrayList<Sent> sentMessage;
    private ArrayList<Inbox> inbox;
    private ArrayList<Draft> draft;
    private ArrayList<Trash> trash;
////////////////////set attribute of user ///////////////////////
    public UserClass(String userName, String password, String email, String birthday) {
        super();
        this.userName = userName;
        this.password = password;
        Email = email;
        Birthday = birthday;
    }
    
    //////////setters and getters of user///////////////////
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

/////////////add sent message to user ////////////////////
    public void addsentMessage(Sent message){
        if(sentMessage == null)
            sentMessage = new ArrayList<>();// new arrayList  if first time 
        sentMessage.add(message); //add new  message 
    }
/////////////add inbox  message to user ////////////////////
    public void addInboxMessage(Inbox message){
        if(inbox == null)
            inbox = new ArrayList<>();//new Arraylist if first time 
        inbox.add(message); //add new message 
    }
/////////////add draft message to user ////////////////////
    public void addDraftMessage(Draft message){
        if(draft == null)
            draft = new ArrayList<>();//return new if first time 
        draft.add(message);//add new message
    }
/////////////add Trash message to user ////////////////////
    public void addTrashMessage(Trash message){
        if(trash == null)
            trash = new ArrayList<>();//return new if the first time
        trash.add(message);//add message 
    }
/////////////add add contact to user ////////////////////
    public void addContact(Contact myContact){
        if(Contacts == null)
            Contacts = new ArrayList<>(); //return new if the first time ;
        Contacts.add(myContact); //add contact
    }



}