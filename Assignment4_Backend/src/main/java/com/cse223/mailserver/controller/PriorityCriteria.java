package com.cse223.mailserver.controller;

import com.cse223.mailserver.UsersAndMails.Message;

import java.util.ArrayList;

public class PriorityCriteria implements Criteria {
    private boolean priority;

    public PriorityCriteria(boolean priority) {
        this.priority = priority;
    }

    @Override
    public ArrayList<? extends Message> filterCriteria(ArrayList<? extends Message> messages) {
        ArrayList<Message> myList = new ArrayList<>();
        for (Message message : messages) {
            if (message.getHeader().isPriority()==priority){
                myList.add(message);
            }
        }
        return myList;
    }
}
