package com.cse223.mailserver.flow;

import com.cse223.mailserver.library.doubleLinkedList;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
    private UserClass user;
    private User userInterface;
    private ArrayList<User> AllUsers;
    private SaveAndLoad save = new SaveAndLoad();

    /**
     * We instantiated 1 class to be called multiple times.
     */
    private static final Server server = new Server();

    private Server(){}

    public static Server getServer() {
        return server;
    }

    ////////////////login///////////////////////////////////////////////////
    public boolean LOGIN(String Email,String password) throws FileNotFoundException, IOException, ParseException, java.text.ParseException {
        save.makeFirstDirectory();
        Login loginClass=new Login(Email,password);
        userInterface=loginClass.ExistOrNot();

        if(userInterface==null) {
            return false;
        }else {
            user=new UserClass(userInterface.getUserName(),userInterface.getPassword(),userInterface.getEmail(),userInterface.getBirthday());
            fillCurrentUser(user.getEmail());
            AutoDelete();
            return true;
        }

    }
    public void setAllUsers() throws FileNotFoundException, IOException, ParseException {
        SaveAndLoad saveAndLoad=new SaveAndLoad();
        AllUsers=saveAndLoad.readUsersFromJson();
    }
    /////////////////////////signup///////////////////////////////////////////////
    public boolean signUp(UserClass user) throws IOException, ParseException {
        save.makeFirstDirectory();
        signUp signUp=new signUp();
        //add Object Of user Here
        System.out.println(user);
        this.user = user;
        return signUp.addUser(user);

    }
    ///////////////////////message dealer//////////////////////////////////////////////////////////
    public boolean  createMessage(String subject , String body , ArrayList<MultipartFile> attaches, ArrayList<String> reciver , String sentOrDart,boolean priority) throws IOException, ParseException{
        for(int i=0; i<reciver.size();i++) {
            if(!exist_user(reciver.get(i))||user.getEmail().equals(reciver.get(i))){
                return false;
            }
        }
        SaveAndLoad saveAndLoad =new SaveAndLoad();
        Date date = new Date();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        MessageHeader header = new MessageHeader(user.getEmail(),reciver,subject,sentOrDart,priority);
        MessageBody Body = new MessageBody(body);
        Attachments Attaches;
        MessageCreator myMessage ;
        if (sentOrDart.equals(Constants.SENT)) {
            ArrayList<String> attachementsDealing=saveMultipartFile(attaches,user.getEmail(),Constants.ATTACHMENTS_SENT,saveAndLoad.getMessageID(user.getEmail(),Constants.SENT ));
            Attaches = new Attachments(attachementsDealing);
            myMessage = new MessageCreator(header,Body,Attaches,dtf.format(now).toString().toString(),saveAndLoad.getMessageID(user.getEmail(),Constants.SENT ),priority);

            saveAndLoad.sendMessage(myMessage, Constants.SENT, myMessage.getHeader().getSender());
            for (int i = 0; i < reciver.size(); i++) {

                attachementsDealing=saveMultipartFile(attaches,myMessage.getHeader().getReciever().get(i),Constants.ATTACHMENTS_INBOX,saveAndLoad.getMessageID(myMessage.getHeader().getReciever().get(i), Constants.INBOX));
                Attaches = new Attachments(attachementsDealing);
                header = new MessageHeader(user.getEmail(),reciver,subject,Constants.INBOX,priority);
                myMessage = new MessageCreator(header,Body,Attaches,dtf.format(now).toString().toString(),saveAndLoad.getMessageID(myMessage.getHeader().getReciever().get(i), Constants.INBOX),priority);
                saveAndLoad.sendMessage(myMessage, Constants.INBOX, myMessage.getHeader().getReciever().get(i));
            }
        }
        else{
            ArrayList<String> attachementsDealing=saveMultipartFile(attaches,user.getEmail(),Constants.ATTACHMENTS_DRAFTS,saveAndLoad.getMessageID(user.getEmail(),Constants.DRAFT ));
            Attaches = new Attachments(attachementsDealing);
            myMessage = new MessageCreator(header,Body,Attaches, dtf.format(now),saveAndLoad.getMessageID(user.getEmail(), Constants.DRAFT),priority);
            saveAndLoad.sendMessage(myMessage, Constants.DRAFT, myMessage.getHeader().getSender());
        }
        if (sentOrDart.equals(Constants.SENT))
            user.addsentMessage((Sent)myMessage.buildSentMessage());
        else
            user.addDraftMessage((Draft) myMessage.buildDraftMessage());
        return true;
    }


    /////////////////////////////////////////////////Trash//////////////////////////////////////////////////////////////
    public boolean sendToTrash(String folder, int messageID) throws FileNotFoundException, IOException, ParseException {
        SaveAndLoad saveAndLoad =new SaveAndLoad();
        ArrayList<MessageCreator> previousMessage=saveAndLoad.readMessages(user.getEmail(), folder);
        MessageCreator messageWeWant =null;
        for(int i=0;i<previousMessage.size();i++) {
            if(previousMessage.get(i).getID()==messageID) {
                messageWeWant=previousMessage.get(i);
                previousMessage.remove(i);
            }
            if(messageWeWant==null)return false;
            saveAndLoad.sendMessage(messageWeWant, Constants.TRASH, user.getEmail());
        }
        saveAndLoad.ClearFileContent(user.getEmail(), folder);
        for(int i=0;i<previousMessage.size();i++) {
            saveAndLoad.sendMessage(previousMessage.get(i), folder, user.getEmail());
        }
        return true;

    }

    public boolean restoreFromTrash(int messageID) throws IOException, ParseException {
        SaveAndLoad saveAndLoad =new SaveAndLoad();
        ArrayList<MessageCreator> previousMessage=saveAndLoad.readMessages(user.getEmail(), Constants.TRASH);
        MessageCreator messageWeWant =null;
        for(int i=0;i<previousMessage.size();i++) {
            if(previousMessage.get(i).getID()==messageID) {
                messageWeWant=previousMessage.get(i);
                previousMessage.remove(i);
            }
            if(messageWeWant==null)return false;
            saveAndLoad.sendMessage(messageWeWant, messageWeWant.getHeader().getFolderName(), user.getEmail());
        }
        saveAndLoad.ClearFileContent(user.getEmail(), Constants.TRASH);
        for(int i=0;i<previousMessage.size();i++) {
            saveAndLoad.sendMessage(previousMessage.get(i), Constants.TRASH, user.getEmail());
        }
        return true;

    }

    public void AutoDelete() throws java.text.ParseException, FileNotFoundException, IOException, ParseException {

        SaveAndLoad saveAndLoad =new SaveAndLoad();
        ArrayList<MessageCreator> messages;
        try {
            messages=saveAndLoad.readMessages(user.getEmail(), Constants.TRASH);
            if(messages == null)
                return;
        }catch (Exception e) {
            return ;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date secondDate = sdf.parse(dtf.format(now));
        for(int i=0;i<messages.size();i++) {
            Date firstDate=new SimpleDateFormat("dd/MM/yyyy").parse(messages.get(i).getTime());
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            if(diffInMillies>=30) {
                messages.remove(i);
            }
        }
        saveAndLoad.ClearFileContent(user.getEmail(), Constants.TRASH);
        for(int i=0;i<messages.size();i++) {
            saveAndLoad.sendMessage(messages.get(i), Constants.TRASH, user.getEmail());
        }

    }





    private boolean exist_user(String Email) throws FileNotFoundException, IOException, ParseException{
        SaveAndLoad saveAndLoad =new SaveAndLoad();
        ArrayList<User> ExistUser;
        ExistUser=saveAndLoad.readUsersFromJson();
        for(int i=0;i<ExistUser.size();i++){
            if(Email.equals((String) ExistUser.get(i).getEmail())){
                return true;
            }
        }
        return false;
    }



    public ArrayList<MessageCreator> getMails(String type) throws IOException, ParseException {
        return save.readMessages(user.getEmail() , type);
    }

    ////////////////////////////////sort//////////////////////////

    public ArrayList<MessageCreator> sort(String folder,String type) throws FileNotFoundException, IOException, ParseException {
        if(type.equals(Constants.PRIORITY)){

        }
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<MessageCreator> messages = saveAndLoad.readMessages(user.getEmail(), folder);
        System.out.println(type);
        if (type.contentEquals(Constants.DATE)) {
            return messages;
        } else {
            Sort sortMessages = new Sort(type);

            return putMessagesInArrayList(sortMessages.sorting(putMessagesInDoubleLinkedList(messages)));
        }
    }

    public doubleLinkedList putMessagesInDoubleLinkedList(ArrayList<MessageCreator> messages) {
        doubleLinkedList db=new doubleLinkedList();
        for(int i=0;i<messages.size();i++) {
            db.add(messages.get(i));
        }
        return db;

    }

    public ArrayList<MessageCreator> putMessagesInArrayList(doubleLinkedList messages) {
        ArrayList<MessageCreator> arrayListMessages=new ArrayList<MessageCreator>();
        for(int i=0;i<messages.size();i++) {
            arrayListMessages.add((MessageCreator) messages.get(i));
        }
        return arrayListMessages;

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
                if (filterName.equals(Constants.TRUE))
                    check=true;
                else
                    check=false;
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
            return null;
        ArrayList<String>files = new ArrayList<>();
        String makemessageAttachementsFolder=Constants.DATABASE_PATH+Email+"//"+Constants.ATTACHMENTS +"//"+folder +messageID;
        System.out.println("Make message " +makemessageAttachementsFolder);
        File file = new File(makemessageAttachementsFolder);
        file.mkdir();
        for(MultipartFile multipartFile : multipartFiles) {
            String directory= makemessageAttachementsFolder+"\\" + multipartFile.getOriginalFilename();
            Path path = Path.of(directory);
            Files.copy(multipartFile.getInputStream() ,path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Directory " + directory);
            files.add(directory);
        }
        return files;
    }

    public ArrayList<Contact> getContacts(){
        try {
            return save.readContactsFromJson(user.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addContact(String name,ArrayList<String> Emails) throws FileNotFoundException, IOException, ParseException {
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<Contact> previousContact = new ArrayList<>();
        previousContact=saveAndLoad.readContactsFromJson(user.getEmail());
        if(previousContact!=null) {
            for(int i=0;i<previousContact.size();i++) {
                if(previousContact.get(i).getName().equals(name)) {
                    return false;
                }
            }

        }
        for(int i=0;i<Emails.size();i++) {
            if(!exist_user(Emails.get(i))) {
                return false;
            }
        }
        Contact contact=new Contact(name, Emails);
        saveAndLoad.AddContact(user.getEmail(), contact);
        return true;


    }
    public boolean deleteContact(String name) throws IOException, ParseException {
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<Contact> previousContact = saveAndLoad.readContactsFromJson(user.getEmail());

        saveAndLoad.ClearFileContent(user.getEmail(),Constants.CONTACTS);
        for(int i=0; i<previousContact.size();i++) {
            if(previousContact.get(i).getName().equals(name)) {
                previousContact.remove(i);
            }

        }
        for(int i=0; i<previousContact.size();i++) {
            saveAndLoad.AddContact(user.getEmail(),previousContact.get(i));
        }
        return true;
    }
    public boolean editContact(String Name,String newName,ArrayList<String> newEmails) throws IOException, ParseException {
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<Contact> previousContact = saveAndLoad.readContactsFromJson(user.getEmail());

        saveAndLoad.ClearFileContent(user.getEmail(),Constants.CONTACTS);
        for(int i=0; i<previousContact.size();i++) {
            if(previousContact.get(i).getName().equals(Name)) {
                //previousContact.get(i).getEmails().remove(i);
                previousContact.get(i).setEmails(newEmails);
                previousContact.get(i).setName(newName);
            }

        }
        for(int i=0; i<previousContact.size();i++) {
            saveAndLoad.AddContact(user.getEmail(), previousContact.get(i));
        }
        return true;
    }

    /**
     * Copies source file to dest file
     */
    private static   void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        sourceChannel = new FileInputStream(source).getChannel();
        destChannel = new FileOutputStream(dest).getChannel();
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        sourceChannel.close();
        destChannel.close();
    }

//            File file = new File("F:\\" + multipartFile.getOriginalFilename());
//            System.out.println(file.getAbsolutePath());
//            multipartFile.transferTo(file);copy file
//            System.out.println("Saved at " + file.getAbsolutePath());

    public ArrayList<Contact> searchingContact (String nameOrEmail , String attribute){
        ArrayList<Contact> result = new ArrayList<>();
        SearchingContacts searched = new SearchingContacts(user.getContacts());
        if (nameOrEmail.equals("name")){
            result=searched.searchingByName(attribute);
        }else {
            result=searched.searchingByEmails(attribute);
        }

        return result;
    }

    public void fillCurrentUser(String Email ) throws FileNotFoundException, IOException, ParseException {
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<MessageCreator> sent= saveAndLoad.readMessages(Email,Constants.SENT);
        ArrayList<MessageCreator> Inbox= saveAndLoad.readMessages(Email,Constants.INBOX);
        ArrayList<MessageCreator> draft= saveAndLoad.readMessages(Email,Constants.DRAFT);
        ArrayList<MessageCreator> trash= saveAndLoad.readMessages(Email,Constants.TRASH);
        ArrayList<Contact> contact= saveAndLoad.readContactsFromJson(user.getEmail());
        if(Inbox==null) {
        }else {
            for(int i=0; i<Inbox.size();i++) {
                user.addInboxMessage((Inbox) Inbox.get(i).buildInboxMessage());
            }
        }
        if(sent==null) {
        }else {
            for(int i=0; i<sent.size();i++) {
                user.addsentMessage((Sent) sent.get(i).buildSentMessage());
            }
        }
        if(draft==null) {
        }else {
            for(int i=0; i<draft.size();i++) {
                user.addDraftMessage((Draft) draft.get(i).buildDraftMessage());
            }
        }
        if(trash==null) {
        }else {
            for(int i=0; i<trash.size();i++) {
                user.addTrashMessage((Trash) trash.get(i).builTrashMessage());
            }
        }
        if(contact==null) {
        }else {
            for(int i=0; i<contact.size();i++) {
                user.addContact(contact.get(i));
            }
        }
    }

    public ArrayList<Contact> sortContact() throws FileNotFoundException, IOException, ParseException{
        SaveAndLoad saveAndLoad=new SaveAndLoad();
        ArrayList<Contact> contacts=saveAndLoad.readContactsFromJson(user.getEmail());
        if(contacts==null) {
            return null;
        }else {
            for (int i = 0; i < contacts.size() - 1; i++) {
                for (int j = 0; j < contacts.size() - i - 1; j++){
                    int compare = contacts.get(j).getName().compareToIgnoreCase(contacts.get(j+1).getName());
                    if (compare > 0)
                    {
                        Contact contact=new Contact(contacts.get(j+1).getName(),contacts.get(j+1).getEmails());
                        contacts.get(j+1).setName(contacts.get(j).getName());
                        contacts.get(j+1).setEmails(contacts.get(j).getEmails());
                        contacts.get(j).setName(contact.getName());
                        contacts.get(j).setEmails(contact.getEmails());
                    }
                }

            }
            for(int i=0;i<contacts.size();i++) {
                ArrayList<String>contactsEmail=contacts.get(i).getEmails();
                ArrayList<String>contactsEmailSorted=new ArrayList<String>();
                Collections.sort(contactsEmail);
                for(String counter: contactsEmail){
                    contactsEmailSorted.add(counter);
                }
                contacts.get(i).setEmails(contactsEmailSorted);
            }
            return contacts;
        }
    }


    public String getUserEmail(){
        return user.getEmail();
    }
    public String getUserName(){
        return user.getUserName();
    }
}