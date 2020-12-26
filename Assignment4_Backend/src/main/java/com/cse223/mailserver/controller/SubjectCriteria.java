package com.cse223.mailserver.controller;

import com.cse223.mailserver.UsersAndMails.Message;

import java.util.ArrayList;

public class SubjectCriteria implements Criteria {
    private String mySubject;

    public SubjectCriteria(String Subject) {
        mySubject=Subject;
    }

    @Override
    public ArrayList<? extends Message> filterCriteria(ArrayList<? extends Message> messages) {
        ArrayList<Message> myList = new ArrayList<>();
        for (Message message : messages) {
            if (message.getHeader().getSubject().contains(mySubject)){
                myList.add(message);
            }
        }
        return myList;
    }
}
