package com.cse223.mailserver.UsersAndMails;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
//////////////////class of attachments 
public class Attachments {
    private ArrayList<String> attaches; //arraylist of saved directory 

    public Attachments(ArrayList<String> attaches) {
        super();
        this.attaches = attaches; //set attachments 
    }

    public ArrayList<String> getAttaches() {
        return attaches;//return attachments 
    }



}
