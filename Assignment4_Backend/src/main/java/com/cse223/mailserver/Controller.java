package com.cse223.mailserver;

import com.cse223.mailserver.flow.*;
import org.json.simple.parser.ParseException;
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@CrossOrigin
@RestController
//@RequestMapping("/api")
public class Controller {
    Server server = Server.getServer();//Server to get and send data


    @GetMapping("/")
    public String checkServer(){
        return "HELLO WORLD";
    }

    /**
     *Login and SignUp.
     */

    @GetMapping("/login")
    public boolean login(@RequestParam("username") String username , @RequestParam("password") String password) throws IOException, ParseException, java.text.ParseException {
        System.out.println("Username is "+ username+"\nPassword is "+password);
//        server.AutoDelete();
        return server.LOGIN(username , password);
    }

    @PostMapping("/signUp")
    @ResponseBody
    public boolean signUp(@RequestBody UserClass user) throws IOException, ParseException {
        System.out.println("Sign up");
        System.out.println(user);
        return server.signUp(user);
    }

    @GetMapping("/getUserUsername")
    public String getUsername(){
        return server.getUserName();
    }


    /**
     *Compose and move to trash.
     *
     */

    @PostMapping("/compose")
    public boolean compose(@RequestParam(name="type")String type,@RequestParam(name = "title")String title , @RequestParam(name = "body") String body
            ,@RequestParam(name = "receivers") String receivers,@Nullable @RequestParam(name="myFile")MultipartFile[] multipartFiles , @RequestParam("priority") boolean priority ) throws IOException {
        System.out.println("Title is " + title);
        System.out.println("Body is " + body);
        String[] receiverList = receivers.split(",");
        System.out.println(Arrays.toString(receiverList));
        ArrayList<String> recs = new ArrayList<>();
        Collections.addAll(recs, receiverList);
        try {
             return server.createMessage(title, body,handleFiles(multipartFiles), recs, type , priority);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<MultipartFile> handleFiles(MultipartFile[] multipartFiles) throws IOException {
        if(multipartFiles == null)
            return null;
        ArrayList<MultipartFile> files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
//            System.out.println(multipartFile.getSize());
            files.add(multipartFile);

//            String filePath = request.getServletContext().getRealPath("/");
//            multipartFile.transferTo(new File(filePath));
//            File file = new File("F:\\" + multipartFile.getOriginalFilename());
//            System.out.println(file.getAbsolutePath());
//            multipartFile.transferTo(file);
//            System.out.println("Saved at " + file.getAbsolutePath());
        }
        return files;
    }

    @PostMapping("/moveToTrash")
    @ResponseBody
    public void moveToTrash(@RequestBody int[] IDs , @RequestParam(name="type") String type , @RequestParam(name="toTrash") boolean toTrash) throws IOException, ParseException {
        //Here we will delete messages according to mail id.
        System.out.println("IDs are "+ Arrays.toString(IDs));

        for(int i: IDs) {
            if(toTrash)
                server.sendToTrash(type, i);
            else
                server.restoreFromTrash(i);
        }
    }


    /**
     * Operations To get mails.
     */

    @GetMapping("/getMails")
    public ArrayList<MessageCreator> getMails(@RequestParam(name = "type") String type) throws IOException, ParseException {
        System.out.println(type);
        ArrayList<MessageCreator> mails =server.getMails(type);
//        System.out.println(mails);
        return mails;
    }

    @GetMapping("/filter")
    public ArrayList<? extends Message> filterBy(@RequestParam(name = "filterName")String filterName ,
                            @RequestParam(name = "subOrRec") String subOrRec ,
                            @RequestParam(name = "inboxOrSent") String inOrSent){

        System.out.println(filterName);
        System.out.println(subOrRec);
        System.out.println(inOrSent);

        return server.myFilter(filterName, subOrRec, inOrSent);
    }
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

    @PostMapping("/file")
    public void saveFile(HttpServletRequest request,@RequestParam("myFile")MultipartFile[] multipartFiles) throws IOException {

        for (MultipartFile multipartFile : multipartFiles) {
            String directory = multipartFile.getOriginalFilename();

            Path path = Path.of(directory);

            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
    }
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request , @RequestParam(name="id")String id , @RequestParam(name="type") String type,
                                                 @RequestParam(name="name")String file) throws MalformedURLException {
        System.out.println("Pressed!");
        String fileName = "data_base\\";
        fileName+=server.getUserEmail()+"\\"+Constants.ATTACHMENTS +"\\";
        fileName+=type+id+"\\"+file;
        System.out.println(fileName);
        Path filePath = Paths.get(fileName);
        Resource resource = new UrlResource(filePath.toUri());

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

    @GetMapping("/addContact")
    public boolean addContact(@RequestParam(name="email")String email , @RequestParam(name = "name") String contactName) throws IOException, ParseException {
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

    @GetMapping("/filterContacts")
    public ArrayList<Contact> filterContacts(@RequestParam("typeOfSort")String type , @RequestParam("name")String name){
       return server.searchingContact(type, name);
    }

}





