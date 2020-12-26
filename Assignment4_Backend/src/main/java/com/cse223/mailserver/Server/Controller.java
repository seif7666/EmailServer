package com.cse223.mailserver.Server;



import com.cse223.mailserver.UsersAndMails.Contact;
import com.cse223.mailserver.UsersAndMails.Message;
import com.cse223.mailserver.UsersAndMails.MessageCreator;
import com.cse223.mailserver.UsersAndMails.UserClass;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


@CrossOrigin
@RestController
/*
 * This is the rest controller class.
 */
public class Controller {

    Server server = Server.getServer();//Server to get and send data

    /**
     * This is called when we want to check if server io on.
     * @return Hello World message showing that it is on.
     */
    @GetMapping("/")
    public String checkServer(){
        return "HELLO WORLD";
    }
    //____________________________________
    /*
     *Login and SignUp.
     */
    /**
     * Login function
     * @param username :Email of user
     * @param password :Password entered by user.
     * @return True if user correct.
     */
    @GetMapping("/login")
    public boolean login(@RequestParam("username") String username , @RequestParam("password") String password)  {
        System.out.println("Username is "+ username+"\nPassword is "+password);
        try {
            return server.LOGIN(username, password);
        }catch (Exception e){
            return false;//
        }
    }

    /**
     * Sign up function.
     * @param user: An instance of User class carrying data entered by user in signup process.
     * @return true if email wasn't located.
     */
    @PostMapping("/signUp")
    @ResponseBody
    public boolean signUp(@RequestBody UserClass user) {
        System.out.println("Sign up");
        System.out.println(user);
        try {
            return server.signUp(user);
        }catch (Exception e){
            return false;
        }
    }

    /**
     * @return user's username.
     */
    @GetMapping("/getUserUsername")
    public String getUsername(){
        return server.getUserName();
    }

//______________________________________
    /*
     *Compose and move to trash.
     *
     */

    /**
     * The compose method that is called when a mail is sent.
     * @param type: type of mail whether sent or draft.
     * @param title: Title of mail.
     * @param body: Body of mail.
     * @param receivers: All receivers' emails.
     * @param multipartFiles: Attachments sent, null if empty.
     * @param priority: Priority of email, whether primary or default.
     * @return true if mail is valid, false otherwise.
     */
    @PostMapping("/compose")
    public boolean compose(@RequestParam(name="type")String type,@RequestParam(name = "title")String title , @RequestParam(name = "body") String body
            ,@RequestParam(name = "receivers") String receivers,@Nullable @RequestParam(name="myFile")MultipartFile[] multipartFiles , @RequestParam("priority") boolean priority )  {
        System.out.println("Title is " + title);
        System.out.println("Body is " + body);
        String[] receiverList = receivers.split(",");
        System.out.println(Arrays.toString(receiverList));
        ArrayList<String> recs = new ArrayList<>();
        Collections.addAll(recs, receiverList);
        try {
            return server.createMessage(title, body, Constants.handleFiles(multipartFiles), recs, type , priority);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Called when some mails are to be deleted or restored.
     * @param IDs: List of mails' IDs to be deleted.
     * @param type: Type of mail whether sent,inbox or draft.
     * @param toTrash: To specify whether mails are restored or deleted.
     */
    @PostMapping("/moveToTrash")
    @ResponseBody
    public void moveToTrash(@RequestBody int[] IDs , @RequestParam(name="type") String type , @RequestParam(name="toTrash") boolean toTrash){
        //Here we will delete messages according to mail id.
        System.out.println("IDs are "+ Arrays.toString(IDs));

        try {
            for (int i : IDs) {
                if (toTrash)
                    server.sendToTrash(type, i);
                else
                    server.restoreFromTrash(i);
            }
        }catch (Exception ignored){}
    }
//________________________________________

    /*
     * Operations To get mails.
     */

    /**
     * This function is called when we want to send mails to front.
     * @param type:folder of mails
     * @return list of mails of required folder.
     */
    @GetMapping("/getMails")
    public ArrayList<MessageCreator> getMails(@RequestParam(name = "type") String type)  {
        System.out.println(type);
        try {
            return server.getMails(type);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * This is called when we want to search for mails.
     * @param filterName word to be searched.
     * @param subOrRec type of filter.
     * @param inOrSent folder to be searched.
     * @return list of filtered mails.
     */
    @GetMapping("/filter")
    public ArrayList<? extends Message> filterBy(@RequestParam(name = "filterName")String filterName ,
                                                 @RequestParam(name = "subOrRec") String subOrRec ,
                                                 @RequestParam(name = "inboxOrSent") String inOrSent){

        System.out.println(filterName);
        System.out.println(subOrRec);
        System.out.println(inOrSent);

        return server.myFilter(filterName, subOrRec, inOrSent);
    }

    /**
     * This is called when user wants to sort mails.
     * @param folder to be sorted.
     * @param type sorting type.
     * @return list of sorted mails.
     */
    @GetMapping("/sort")
    public ArrayList<MessageCreator> getSorted(@RequestParam(name = "folder") String folder , @RequestParam(name="type") String type){
        try {
            System.out.println("Folder is "+folder);
            System.out.println("Type is "+type);
            ArrayList<MessageCreator> mess = server.sort(folder, type);
            System.out.println("Size is  "+ mess.size());
            return mess;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sorting according to priority.
     * @param inbox folder to be sorted.
     * @return sorted list.
     */
    @GetMapping("/sortPrior")
    public ArrayList<Message> sortPrior(@RequestParam(name ="inboxOrSent") String inbox){
        ArrayList<Message> list = new ArrayList<>();
        ArrayList<? extends Message> primary = server.myFilter(Constants.TRUE ,Constants.PRIORITY , inbox);
        ArrayList<? extends Message> defaultList = server.myFilter(Constants.FALSE , Constants.PRIORITY , inbox);
        if(primary != null)
            list.addAll(primary);
        if(defaultList != null)
            list.addAll(defaultList);
        System.out.println(list);

        return list.isEmpty() ? null : list;
    }
    //_____________________________________

    /**
     * Download an attachment.
     * @param request:HTTP request.
     * @param id of mail.
     * @param type of folder.
     * @param file :name of file to be downloaded.
     * @return HTTP response carrying required file to be downloaded.
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request , @RequestParam(name="id")String id , @RequestParam(name="type") String type,
                                                 @RequestParam(name="name")String file)  {
        System.out.println("Pressed!");
        String fileName = Constants.DATABASE_PATH;
        fileName+=server.getUserEmail()+"\\"+Constants.ATTACHMENTS +"\\";
        fileName+=type+id+"\\"+file;
        System.out.println(fileName);
        Path filePath = Paths.get(fileName);
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        }catch (Exception e){
            return null;
        }
        // Try to determine file's content type

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");
        }
        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    //_______________________________________
    /*
     * Contacts Operations.
     */

    /**
     * Get contacts sorted alphabetically.
     * @return sorted list of contacts.
     */
    @GetMapping("/getContacts")
    @ResponseBody
    public ArrayList<Contact> getContacts(){
        try {
            ArrayList<Contact> list = server.sortContact();
            System.out.println("List of contacts : " + list);
            return list;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Add a contact.
     * @param email emails of contact separated by ",".
     * @param contactName name of Contact.
     * @return true if operation was done successfully.
     */
    @GetMapping("/addContact")
    public boolean addContact(@RequestParam(name="email")String email , @RequestParam(name = "name") String contactName)  {
        String[]  contactsList = email.split(",");
        ArrayList<String> emails = new ArrayList<>();
        Collections.addAll(emails , contactsList);
        System.out.println("Contact name is "+ contactName);
        System.out.println("Emails are "+emails);
        try {
            return server.addContact(contactName, emails);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete  contacts.
     * @param names of contacts to be deleted.
     */
    @PostMapping("/deleteContacts")
    @ResponseBody
    public void deleteContacts(@RequestBody ArrayList<String> names){
        try {
            for (String name : names)
                server.deleteContact(name);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Edit a contact by knowing the old name,new name and new emails.
     * @param emails new emails the user entered.
     * @param newName of contact.
     * @param oldName old name to specify which contact to be edited.
     * @return true if it was successful.
     */
    @GetMapping("/editContacts")
    public boolean editContacts(@RequestParam("email")String emails , @RequestParam("newName")String newName , @RequestParam("oldName") String oldName){
        ArrayList<String> emailsList = new ArrayList<>();
        String[] list = emails.split(",");
        Collections.addAll(emailsList , list);
        try {
            server.editContact(oldName, newName, emailsList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Search contacts according to name or email.
     * @param type of filter
     * @param name to be filtered.
     * @return list of filtered clients.
     */
    @GetMapping("/filterContacts")
    public ArrayList<Contact> filterContacts(@RequestParam("typeOfSort")String type , @RequestParam("name")String name){
        return server.searchingContact(type, name);
    }

}



//    @PostMapping("/file")
//    public void saveFile(HttpServletRequest request,@RequestParam("myFile")MultipartFile[] multipartFiles) throws IOException {
//
//        for (MultipartFile multipartFile : multipartFiles) {
//            String directory = multipartFile.getOriginalFilename();
//
//            Path path = Path.of(directory);
//
//            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//        }
//    }