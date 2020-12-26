package com.cse223.mailserver.Server;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;

///////////////////class of constants we use/////////////////
public  class Constants {

    public static final String SUBJECT = "Subject";
    public static final String BODY = "Body";
    public static final String DATE = "Date";
    public static final String PRIORITY = "Priority";
    public static final String TRUE = "Primary";
    public static final String FALSE = "Default";


    public static final String SENDER = "Sender";
    public static final String RECEIVERS = "Receivers";

    public static final String INBOX = "Inbox";
    public static final String SENT = "Sent";
    public static final String TRASH = "Trash";
    public static final String DRAFT = "Draft";

    public static final String CONTACTS = "Contacts";
    public static final String ATTACHMENTS = "Attachments";
    public static final String ATTACHMENTS_SENT = "sent";
    public static final String ATTACHMENTS_INBOX = "Inbox";
    public static final String ATTACHMENTS_DRAFTS = "AttachmentsDrafts";

    public static final String DATABASE_PATH = "Database//";
    public static final String INDEX_JSON_PATH = "//index.json";
    public static final String ACCOUNTS_PATH = DATABASE_PATH + "Accounts";
    public static final String ACCOUNTS_JSON_PATH = ACCOUNTS_PATH+"//Users.json";


    public static final String CONTACTS_NAME = "ContactName";
    public static final String MESSAGE = "Message";


    /**
     * Method was put here as it can be accessed everywhere.
     * Convert array of files to list of files, null if array is null
     * @param multipartFiles:array of files
     * @return list of files
     */
    public static ArrayList<MultipartFile> handleFiles(MultipartFile[] multipartFiles){
        if(multipartFiles == null)
            return null;
        ArrayList<MultipartFile> files = new ArrayList<>();
        Collections.addAll(files, multipartFiles);
        return files;
    }


}