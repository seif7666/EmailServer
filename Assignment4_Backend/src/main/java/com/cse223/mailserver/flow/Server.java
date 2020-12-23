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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


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
        Login loginClass=new Login(Email,password);
        userInterface=loginClass.ExistOrNot();

        if(userInterface==null) {
            return false;
        }else {
            user=new UserClass(userInterface.getUserName(),userInterface.getPassword(),userInterface.getEmail(),userInterface.getBirthday());
            fillCurrentUser(user.getEmail());
//            AutoDelete();
            return true;
        }

    }
    public void setAllUsers() throws FileNotFoundException, IOException, ParseException {
        SaveAndLoad saveAndLoad=new SaveAndLoad();
        AllUsers=saveAndLoad.readUsersFromJson();
    }
    /////////////////////////signup///////////////////////////////////////////////
    public boolean signUp(UserClass user) throws IOException, ParseException {
        signUp signUp=new signUp();
        //add Object Of user Here
        System.out.println(user);
        this.user = user;
        return signUp.addUser(user);

    }
    ///////////////////////message dealer//////////////////////////////////////////////////////////
    public boolean  createMessage(String subject , String body , ArrayList<MultipartFile> attaches, ArrayList<String> reciver , String sentOrDart) throws IOException, ParseException{
        SaveAndLoad saveAndLoad =new SaveAndLoad();
        Date date = new Date();

        MessageHeader header = new MessageHeader(user.getEmail(),reciver,subject,sentOrDart);
        MessageBody Body = new MessageBody(body);
        Attachments Attaches;
        MessageCreator myMessage ;
        if (sentOrDart.equals(Constants.Sent)) {
            ArrayList<String> attachementsDealing=saveMultipartFile(attaches,user.getEmail(),Constants.ATTACHEMENTS_SENT,saveAndLoad.getMessageID(user.getEmail(),Constants.Sent ));
            Attaches = new Attachments(attachementsDealing);
            myMessage = new MessageCreator(header,Body,Attaches,date.toString(),saveAndLoad.getMessageID(user.getEmail(),Constants.Sent ));

            saveAndLoad.sendMessage(myMessage, Constants.Sent, myMessage.getHeader().getSender());
            for (int i = 0; i < reciver.size(); i++) {
                if (!exist_user(myMessage.getHeader().getReciever().get(i))) {
                    return false;
                }
                attachementsDealing=saveMultipartFile(attaches,myMessage.getHeader().getReciever().get(i),Constants.ATTACHEMENTS_INBOX,saveAndLoad.getMessageID(myMessage.getHeader().getReciever().get(i), Constants.Inbox));
                Attaches = new Attachments(attachementsDealing);
                header = new MessageHeader(user.getEmail(),reciver,subject,Constants.Inbox);
                myMessage = new MessageCreator(header,Body,Attaches,date.toString(),saveAndLoad.getMessageID(myMessage.getHeader().getReciever().get(i), Constants.Inbox));
                saveAndLoad.sendMessage(myMessage, Constants.Inbox, myMessage.getHeader().getReciever().get(i));
            }
        }
        else{
            ArrayList<String> attachementsDealing=saveMultipartFile(attaches,user.getEmail(),Constants.ATTASHEMENTS_DRAFTS,saveAndLoad.getMessageID(user.getEmail(),Constants.Draft ));
            Attaches = new Attachments(attachementsDealing);
            myMessage = new MessageCreator(header,Body,Attaches,date.toString(),saveAndLoad.getMessageID(user.getEmail(), Constants.Draft));
            saveAndLoad.sendMessage(myMessage, Constants.Draft, myMessage.getHeader().getSender());
        }
        if (sentOrDart.equals(Constants.Sent))
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
            saveAndLoad.sendMessage(messageWeWant, Constants.Trash, user.getEmail());
        }
        saveAndLoad.ClearFileContent(user.getEmail(), folder);
        for(int i=0;i<previousMessage.size();i++) {
            saveAndLoad.sendMessage(previousMessage.get(i), folder, user.getEmail());
        }
        return true;

    }

    public boolean restoreFromTrash(int messageID) throws IOException, ParseException {
        SaveAndLoad saveAndLoad =new SaveAndLoad();
        ArrayList<MessageCreator> previousMessage=saveAndLoad.readMessages(user.getEmail(), Constants.Trash);
        MessageCreator messageWeWant =null;
        for(int i=0;i<previousMessage.size();i++) {
            if(previousMessage.get(i).getID()==messageID) {
                messageWeWant=previousMessage.get(i);
                previousMessage.remove(i);
            }
            if(messageWeWant==null)return false;
            saveAndLoad.sendMessage(messageWeWant, messageWeWant.getHeader().getFolderName(), user.getEmail());
        }
        saveAndLoad.ClearFileContent(user.getEmail(), Constants.Trash);
        for(int i=0;i<previousMessage.size();i++) {
            saveAndLoad.sendMessage(previousMessage.get(i), Constants.Trash, user.getEmail());
        }
        return true;

    }

    public void AutoDelete() throws java.text.ParseException, FileNotFoundException, IOException, ParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        SaveAndLoad saveAndLoad =new SaveAndLoad();
        ArrayList<MessageCreator> messages;
        try {
            messages=saveAndLoad.readMessages(user.getEmail(), Constants.Trash);
        }catch (Exception e) {
            return ;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDate d2 = LocalDate.parse(dtf.format(now), DateTimeFormatter.ISO_LOCAL_DATE);
        for(int i=0;i<messages.size();i++) {
            LocalDate d1 = LocalDate.parse((CharSequence) new SimpleDateFormat("dd/MM/yyyy").parse((messages.get(i).getTime())), DateTimeFormatter.ISO_LOCAL_DATE);
            Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());
            long diffDays = diff.toDays();
            if(diffDays>=30) {
                messages.remove(i);
            }
        }
        saveAndLoad.ClearFileContent(user.getEmail(), Constants.Trash);
        for(int i=0;i<messages.size();i++) {
            saveAndLoad.sendMessage(messages.get(i), Constants.Trash, user.getEmail());
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
    public ArrayList<? extends Message> myFilter (String  filterName , String subjectOrReceiversOrBodyOrDate , String inboxOrSentOrTrashOrDraft ){
        ArrayList<? extends Message> result =new ArrayList<>() ;
        switch (subjectOrReceiversOrBodyOrDate){
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
        }

        return result;
    }

    private ArrayList<? extends Message> getMessages(String inboxOrSentOrTrashOrDraft, ArrayList<? extends Message> result, Criteria subjectFilter) {
        switch (inboxOrSentOrTrashOrDraft) {
            case Constants.Inbox:
                result = subjectFilter.filterCriteria(user.getInbox());
                break;
            case Constants.Sent:
                result = subjectFilter.filterCriteria(user.getSentMessage());
                break;
            case Constants.Trash:
                result = subjectFilter.filterCriteria(user.getTrash());
                break;
            case Constants.Draft:
                result = subjectFilter.filterCriteria(user.getDraft());
                break;
        }
        return result;
    }

    public void fillCurrentUser(String Email ) throws FileNotFoundException, IOException, ParseException {
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<MessageCreator> sent= saveAndLoad.readMessages(Email,Constants.Sent);
        ArrayList<MessageCreator> Inbox= saveAndLoad.readMessages(Email,Constants.Inbox);
        ArrayList<MessageCreator> draft= saveAndLoad.readMessages(Email,Constants.Draft);
        ArrayList<MessageCreator> trash= saveAndLoad.readMessages(Email,Constants.Trash);
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
    }

    /**
     * Saves multipart file in required directory
     */
    public  ArrayList<String> saveMultipartFile(ArrayList<MultipartFile> multipartFiles, String Email,String folder,int messageID) throws IOException {
        if (multipartFiles == null)
            return null;
        ArrayList<String>files = new ArrayList<>();
        String makemessageAttachementsFolder="data_base\\"+Email+"\\"+Constants.ATTACHEMENTS +"\\"+folder +messageID;
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

    public boolean addContact(String Contact) throws FileNotFoundException, IOException, ParseException {
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<String> previousContact = new ArrayList<>();
        previousContact=saveAndLoad.readContactsFromJson(user.getEmail());
        if(previousContact!=null) {
            for(int i=0;i<previousContact.size();i++) {
                if(previousContact.get(i).equals(Contact)) {
                    return false;
                }
            }

        }
        saveAndLoad.AddContact(user.getEmail(), Contact);
        return true;


    }
    public boolean deleteContact(String Contact) throws IOException, ParseException {
        SaveAndLoad saveAndLoad = new SaveAndLoad();
        ArrayList<String> previousContact = new ArrayList<>();
        saveAndLoad.ClearFileContent(user.getEmail(),Constants.CONTACTS);
        for(int i=0; i<previousContact.size();i++) {
            if(previousContact.get(i).equals(Contact)) {
                previousContact.remove(i);
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


    public String getUserEmail(){
        return user.getEmail();
    }
}