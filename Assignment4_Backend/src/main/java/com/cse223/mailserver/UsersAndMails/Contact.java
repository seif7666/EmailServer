package com.cse223.mailserver.UsersAndMails;

import java.util.ArrayList;
////////////contact class
public class Contact {
    private String name;   //name of contact
    private ArrayList<String> emails; //emails of contact

    public Contact(String name, ArrayList<String> emails) {
        this.name = name; //set name 
        this.emails = emails;//set emails
    }

    public String getName() {
        return name;//get name 
    }

    public void setName(String name) {
        this.name = name; //get name
    }

    public ArrayList<String> getEmails() {
        return emails;//get emails
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Contact{");
        sb.append("name='").append(name).append('\'');
        sb.append(", emails=").append(emails);
        sb.append('}');
        return sb.toString();
    }
}
