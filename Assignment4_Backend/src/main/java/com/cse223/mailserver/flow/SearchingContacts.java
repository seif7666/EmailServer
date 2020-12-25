package com.cse223.mailserver.flow;

import java.util.ArrayList;

public class SearchingContacts {

    ArrayList<Contact> contacts;

    public SearchingContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<Contact> searchingByName(String name){
        ArrayList<Contact> contactsResults = new ArrayList<>();
        for (Contact contact : contacts){
            if (contact.getName().contains(name)){
                contactsResults.add(contact);
            }
        }
        return contactsResults;
    }

    public ArrayList<Contact> searchingByEmails(String Email){
        if(contacts == null)
            contacts = new ArrayList<>();
        ArrayList<Contact> contactsResults = new ArrayList<>();
        for (Contact contact : contacts){
            for (String email : contact.getEmails())
            if (email.contains(Email)){
                contactsResults.add(contact);
            }
        }
        return contactsResults;
    }


}
