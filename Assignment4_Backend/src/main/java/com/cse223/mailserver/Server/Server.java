package com.cse223.mailserver.Server;


import com.cse223.mailserver.UsersAndMails.*;
import com.cse223.mailserver.controller.*;
import com.cse223.mailserver.library.doubleLinkedList;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class Server {
    private UserClass user; //userClass to get all  attribute
    private final SaveAndLoad save = new SaveAndLoad(); //make an object with saveAndLoad class

    /**
     * We instantiated 1 class to be called multiple times.
     */
    private static final Server server = new Server(); //using singleton to make one object

    private Server(){}  //private constructor

    public static Server getServer() {
        return server;  //return the object
    }

    ////////////////login///////////////////////////////////////////////////
    public boolean LOGIN(String Email,String password) throws  IOException, ParseException, java.text.ParseException {
        save.makeFirstDirectory(); //make first directory data_base and file account to avoid exception
        Login loginClass=new Login(Email,password); //make object of login class
        //user interface with attribute  getter to use Read-Only-Interface design pattern
        User userInterface = loginClass.ExistOrNot(); //see if user exist

        if(userInterface ==null) { //user not exist
            return false; //return false to front end
        }else {
            user=new UserClass(userInterface.getUserName(), userInterface.getPassword(), userInterface.getEmail(), userInterface.getBirthday()); //make the user that we will deal while program run
            fillCurrentUser(user.getEmail()); //fill UserClass
            AutoDelete();  //auto delete of the trash after 30 days
            return true;
        }

    }

    /////////////////////////signup///////////////////////////////////////////////
    public boolean signUp(UserClass user) throws IOException, ParseException {
        save.makeFirstDirectory(); //make first directory data_base and file account to avoid exception
        signUp signUp=new signUp(); //make object of sign up
        //add Object Of user Here
        this.user = user; //fill user to our user
        return signUp.addUser(user);//add the user to the class

    }
    ///////////////////////message dealer//////////////////////////////////////////////////////////
    public boolean  createMessage(String subject , String body , ArrayList<MultipartFile> attaches, ArrayList<String> reciver , String sentOrDart,boolean priority) throws IOException, ParseException{
        for (String s : reciver) { //check if the one of the receiver not exist
            if (!exist_user(s) || user.getEmail().equals(s)) {
                return false; //return false to front end
            }
        }
        SaveAndLoad saveAndLoad =new SaveAndLoad(); //make object of save and load
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //pattern of date
        LocalDateTime now = LocalDateTime.now(); //get time noe of date
        MessageHeader header = new MessageHeader(user.getEmail(),reciver,subject,sentOrDart,priority); //set message header
        MessageBody Body = new MessageBody(body); //make body
        Attachments Attaches;//attachment of user
        MessageCreator myMessage ; //message creator
        if (sentOrDart.equals(Constants.SENT)) { //message sent
            ArrayList<String> attachementsDealing=saveMultipartFile(attaches,user.getEmail(),Constants.ATTACHMENTS_SENT,saveAndLoad.getMessageID(user.getEmail(),Constants.SENT )); //dealing with attechments
            Attaches = new Attachments(attachementsDealing); //set attachment object
            myMessage = new MessageCreator(header,Body,Attaches,dtf.format(now),saveAndLoad.getMessageID(user.getEmail(),Constants.SENT ),priority); //set message creator

            saveAndLoad.sendMessage(myMessage, Constants.SENT, myMessage.getHeader().getSender());//save message in the sent file of user
            for (int i = 0; i < reciver.size(); i++) { //loop with receiver

                attachementsDealing=saveMultipartFile(attaches,myMessage.getHeader().getReciever().get(i),Constants.ATTACHMENTS_INBOX,saveAndLoad.getMessageID(myMessage.getHeader().getReciever().get(i), Constants.INBOX));
                Attaches = new Attachments(attachementsDealing);
                header = new MessageHeader(user.getEmail(),reciver,subject,Constants.INBOX,priority);//set header of the message
                myMessage = new MessageCreator(header,Body,Attaches,dtf.format(now),saveAndLoad.getMessageID(myMessage.getHeader().getReciever().get(i), Constants.INBOX),priority);//set message creator
                saveAndLoad.sendMessage(myMessage, Constants.INBOX, myMessage.getHeader().getReciever().get(i)); //save to the receiver
            }
        }
        else{//message is draft
            ArrayList<String> attachementsDealing=saveMultipartFile(attaches,user.getEmail(),Constants.ATTACHMENTS_DRAFTS,saveAndLoad.getMessageID(user.getEmail(),Constants.DRAFT ));
            Attaches = new Attachments(attachementsDealing); //set attachments
            myMessage = new MessageCreator(header,Body,Attaches, dtf.format(now),saveAndLoad.getMessageID(user.getEmail(), Constants.DRAFT),priority); //set message creator
            saveAndLoad.sendMessage(myMessage, Constants.DRAFT, myMessage.getHeader().getSender()); //save message of draft to user draft
        }
        if (sentOrDart.equals(Constants.SENT)) //add message to current user
            user.addsentMessage((Sent)myMessage.buildSentMessage());
        else  //add draft message to current user
            user.addDraftMessage((Draft) myMessage.buildDraftMessage());
        return true; //return true to front end if no error
    }


    /////////////////////////////////////////////////Trash//////////////////////////////////////////////////////////////
    public void sendToTrash(String folder, int messageID) throws  IOException, ParseException {

        ArrayList<MessageCreator> previousMessage=save.readMessages(user.getEmail(), folder);//get message to sent to trash
        MessageCreator messageWeWant =null; //set message to null
        for(int i=0;i<previousMessage.size();i++) {
            if(previousMessage.get(i).getID()==messageID) {
                messageWeWant=previousMessage.get(i); //getting message
                previousMessage.remove(i); //delete message from the current folder
            }
            if(messageWeWant==null)return;  //return false if error
            save.sendMessage(messageWeWant, Constants.TRASH, user.getEmail()); //save message to trash
        }
        save.ClearFileContent(user.getEmail(), folder); //clear file directory
        for (MessageCreator messageCreator : previousMessage) {
            save.sendMessage(messageCreator, folder, user.getEmail()); //save other message in the directory
        }

    }
    //////////////restore from trash ///////////////////
    public void restoreFromTrash(int messageID) throws IOException, ParseException {

        ArrayList<MessageCreator> previousMessage=save.readMessages(user.getEmail(), Constants.TRASH);//get all messages of trash
        MessageCreator messageWeWant =null;//set message we want to null
        for(int i=0;i<previousMessage.size();i++) { //iterate to get message we want
            if(previousMessage.get(i).getID()==messageID) {
                messageWeWant=previousMessage.get(i);//if we get message
                previousMessage.remove(i);//remove message from trash
            }
            if(messageWeWant==null)return; //return false if not found
            save.sendMessage(messageWeWant, messageWeWant.getHeader().getFolderName(), user.getEmail());//restore message
        }
        save.ClearFileContent(user.getEmail(), Constants.TRASH); //clear trash
        for (MessageCreator messageCreator : previousMessage) {
            save.sendMessage(messageCreator, Constants.TRASH, user.getEmail());//save other to trash
        }

    }
    ////////////////// Auto delete after 30 days///
    public void AutoDelete() throws java.text.ParseException, IOException, ParseException {

        ArrayList<MessageCreator> messages; //to get messages of trash
        try {
            messages=save.readMessages(user.getEmail(), Constants.TRASH); //get messages
            if(messages == null)
                return; //return if trash is empty
        }catch (Exception e) {
            return ;  //return if happen exception
        }
        //pattern of date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();//get time now to compare
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH); //format date
        Date secondDate = sdf.parse(dtf.format(now)); //formate time now
        for(int i=0;i<messages.size();i++) {
            Date firstDate=new SimpleDateFormat("dd/MM/yyyy").parse(messages.get(i).getTime()); //get time of each message
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());//subtract the date in days
            if(diffInMillies>30) {
                messages.remove(i); //remove message after 30 days
            }
        }
        save.ClearFileContent(user.getEmail(), Constants.TRASH); //clear trash
        for (MessageCreator message : messages) {
            save.sendMessage(message, Constants.TRASH, user.getEmail());//save the file to trash
        }

    }




    //////////////exist user function//////////
    private boolean exist_user(String Email) throws FileNotFoundException, IOException, ParseException{
        ArrayList<User> ExistUser;//arraylist of user
        ExistUser=save.readUsersFromJson();//get users
        for (User value : ExistUser) {
            if (Email.equals(value.getEmail())) { //check user
                return true;//return true if we found user
            }
        }
        return false;//return false if not exist
    }



    public ArrayList<MessageCreator> getMails(String type) throws IOException, ParseException {
        return save.readMessages(user.getEmail() , type); //get message from folder we want
    }

    ////////////////////////////////sort//////////////////////////

    public ArrayList<MessageCreator> sort(String folder,String type) throws  IOException, ParseException {
        ArrayList<MessageCreator> messages = save.readMessages(user.getEmail(), folder);//get message from folder we want
        if (type.contentEquals(Constants.DATE)) {
            return messages;  //return message if sort with date
        } else {
            Sort sortMessages = new Sort(type); //if we want to sort with other type

            return putMessagesInArrayList(sortMessages.sorting(putMessagesInDoubleLinkedList(messages)));//return sorting messages
        }
    }

    public doubleLinkedList putMessagesInDoubleLinkedList(ArrayList<MessageCreator> messages) {
        doubleLinkedList db=new doubleLinkedList(); //get object
        for (MessageCreator message : messages) {
            db.add(message); //add the message
        }
        return db;//return double linked list

    }
    ////////////put message in ArrayList//////////
    public ArrayList<MessageCreator> putMessagesInArrayList(doubleLinkedList messages) {
        ArrayList<MessageCreator> arrayListMessages=new ArrayList<>();//get object of arrayList
        for(int i=0;i<messages.size();i++) {
            arrayListMessages.add((MessageCreator) messages.get(i)); //add messages
        }
        return arrayListMessages;//return arrayList

    }

    //////////////////////////////////////////////Filter/////////////////////////////////////////////////
    public ArrayList<? extends Message> myFilter (String  filterName , String subjectOrReceiversOrBodyOrDateOrPriority , String inboxOrSentOrTrashOrDraft ){
        ArrayList<? extends Message> result =new ArrayList<>() ;
        switch (subjectOrReceiversOrBodyOrDateOrPriority){
            case Constants.SUBJECT:
                Criteria subjectFilter = new SubjectCriteria(filterName);
                result = getMessages(inboxOrSentOrTrashOrDraft, result, subjectFilter);

                break;
            case Constants.RECEIVERS:
                Criteria reciverFilter = new recieversCriteria(filterName);
                result = getMessages(inboxOrSentOrTrashOrDraft, result, reciverFilter);
                break;
            case Constants.BODY:
                Criteria bodyFilter = new BodyCriteria(filterName);
                result = getMessages(inboxOrSentOrTrashOrDraft, result, bodyFilter);
                break;
            case Constants.DATE:
                Criteria dateFilter = new DateCriteria(filterName);
                result = getMessages(inboxOrSentOrTrashOrDraft, result, dateFilter);
                break;
            case Constants.PRIORITY:
                boolean check;
                check= filterName.equals(Constants.TRUE);
                System.out.println("Check is "+check);
                Criteria PriorityCriteria = new PriorityCriteria(check);
                result = getMessages(inboxOrSentOrTrashOrDraft, result, PriorityCriteria);
                System.out.println("Result size is "+result.size());
                break;
        }

        return result;
    }

    private ArrayList<? extends Message> getMessages(String subjectOrReceiversOrBodyOrDateOrPriority, ArrayList<? extends Message> result, Criteria subjectFilter) {
        switch (subjectOrReceiversOrBodyOrDateOrPriority) {
            case Constants.INBOX:
                result = subjectFilter.filterCriteria(user.getInbox());
                break;
            case Constants.SENT:
                result = subjectFilter.filterCriteria(user.getSentMessage());
                break;
            case Constants.TRASH:
                result = subjectFilter.filterCriteria(user.getTrash());
                break;
            case Constants.DRAFT:
                result = subjectFilter.filterCriteria(user.getDraft());
                break;
        }
        return result;
    }



    /**
     * Saves multipart file in required directory
     */
    public  ArrayList<String> saveMultipartFile(ArrayList<MultipartFile> multipartFiles, String Email,String folder,int messageID) throws IOException {
        if (multipartFiles == null)
            return null; //return if no multiple file
        ArrayList<String>files = new ArrayList<>(); //object of Arraylist
        String makemessageAttachementsFolder=Constants.DATABASE_PATH+Email+"//"+Constants.ATTACHMENTS +"//"+folder +messageID;//directory
        File file = new File(makemessageAttachementsFolder);//create file
        file.mkdir();//make directory
        for(MultipartFile multipartFile : multipartFiles) {//iterate with multiple file
            String directory= makemessageAttachementsFolder+"\\" + multipartFile.getOriginalFilename();
            Path path = Path.of(directory);
            Files.copy(multipartFile.getInputStream() ,path, StandardCopyOption.REPLACE_EXISTING);
            files.add(directory); //add directory to save it
        }
        return files; //return files
    }

    public ArrayList<Contact> getContacts(){
        try {
            return save.readContactsFromJson(user.getEmail());//get contacts
        }catch (Exception e){
            e.printStackTrace();
        }
        return null; //null if not found
    }

    public boolean addContact(String name,ArrayList<String> Emails) throws  IOException, ParseException {
        ArrayList<Contact> previousContact ; //get arraylist
        previousContact=save.readContactsFromJson(user.getEmail());//get all contact
        if(previousContact!=null) {
            for (Contact contact : previousContact) {
                if (contact.getName().equals(name)) {
                    return false; //return false if we get name.
                }
            }

        }
        for (String email : Emails) {
            if (!exist_user(email)) {
                return false; //if we get email that not exist
            }
        }
        Contact contact=new Contact(name, Emails); //make contact object
        save.AddContact(user.getEmail(), contact);//save contact
        return true;


    }
    ////delete contact//////////
    public void deleteContact(String name) throws IOException, ParseException {

        ArrayList<Contact> previousContact = save.readContactsFromJson(user.getEmail());//get contact of user

        save.ClearFileContent(user.getEmail(),Constants.CONTACTS);//clear contact file
        for(int i=0; i<previousContact.size();i++) {
            if(previousContact.get(i).getName().equals(name)) {
                previousContact.remove(i); //remove the contact
            }

        }
        for (Contact contact : previousContact) {
            save.AddContact(user.getEmail(), contact);//save other contact
        }
    }

    ////////edit contact///////////////////////
    public void editContact(String Name, String newName, ArrayList<String> newEmails) throws IOException, ParseException {
        ArrayList<Contact> previousContact = save.readContactsFromJson(user.getEmail()); //get contacts

        save.ClearFileContent(user.getEmail(),Constants.CONTACTS);//clear the file contact
        for (Contact contact : previousContact) {
            if (contact.getName().equals(Name)) {
                contact.setEmails(newEmails);//set new Email
                contact.setName(newName);  //set new name
            }

        }
        for (Contact contact : previousContact) {
            save.AddContact(user.getEmail(), contact);// save it
        }
    }


    public ArrayList<Contact> searchingContact (String nameOrEmail , String attribute){
        ArrayList<Contact> result ;
        SearchingContacts searched = new SearchingContacts(user.getContacts());
        if (nameOrEmail.equals("name")){
            result=searched.searchingByName(attribute);
        }else {
            result=searched.searchingByEmails(attribute);
        }

        return result;
    }
    //////////////////filling current user /////////////////////////////
    public void fillCurrentUser(String Email ) throws  IOException, ParseException {
        /////////////////////get messages////////
        ArrayList<MessageCreator> sent= save.readMessages(Email,Constants.SENT);
        ArrayList<MessageCreator> Inbox= save.readMessages(Email,Constants.INBOX);
        ArrayList<MessageCreator> draft= save.readMessages(Email,Constants.DRAFT);
        ArrayList<MessageCreator> trash= save.readMessages(Email,Constants.TRASH);
        ArrayList<Contact> contact= save.readContactsFromJson(user.getEmail());
        if(Inbox!=null) {
            for (MessageCreator inbox : Inbox) {
                user.addInboxMessage((Inbox) inbox.buildInboxMessage());//add inbox  message
            }
        }
        if(sent!=null) {
            for (MessageCreator messageCreator : sent) {
                user.addsentMessage((Sent) messageCreator.buildSentMessage()); //add sent messages
            }
        }
        if(draft!=null) {
            for (MessageCreator messageCreator : draft) {
                user.addDraftMessage((Draft) messageCreator.buildDraftMessage());//add draft message
            }
        }
        if (trash !=null){
            for (MessageCreator messageCreator : trash) {
                user.addTrashMessage((Trash) messageCreator.builTrashMessage());//add draft messages
            }
        }
        if(contact!=null){
            for (Contact value : contact) {
                user.addContact(value); //get contact messages
            }
        }
    }
    //////////////////two layer sort contact//////////////
    public ArrayList<Contact> sortContact() throws  IOException, ParseException{
        ArrayList<Contact> contacts=save.readContactsFromJson(user.getEmail());//get contact
        if(contacts==null) {
            return null; //return null if there isn't contact
        }else {
            for (int i = 0; i < contacts.size() - 1; i++) {
                for (int j = 0; j < contacts.size() - i - 1; j++){
                    int compare = contacts.get(j).getName().compareToIgnoreCase(contacts.get(j+1).getName());//compare two contact
                    if (compare > 0)
                    {
                        ////////////swap if we need //////////
                        Contact contact=new Contact(contacts.get(j+1).getName(),contacts.get(j+1).getEmails());
                        contacts.get(j+1).setName(contacts.get(j).getName());
                        contacts.get(j+1).setEmails(contacts.get(j).getEmails());
                        contacts.get(j).setName(contact.getName());
                        contacts.get(j).setEmails(contact.getEmails());
                    }
                }

            }
            ///////////////sort emails of the contact////
            for (Contact contact : contacts) {
                ArrayList<String> contactsEmail = contact.getEmails();//get emails to sort them
                Collections.sort(contactsEmail);
                //add emails
                //arraylist of sorted emails
                ArrayList<String> contactsEmailSorted = new ArrayList<>(contactsEmail);
                contact.setEmails(contactsEmailSorted); //add sorted emails to contact
            }
            return contacts;
        }
    }

    //////////get user emails
    public String getUserEmail(){
        return user.getEmail();
    }
    ////////////get user name //////////////////
    public String getUserName(){
        return user.getUserName();
    }
}