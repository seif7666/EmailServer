package com.cse223.mailserver.controller;

import com.cse223.mailserver.UsersAndMails.Message;

import java.util.ArrayList;

public class DateCriteria implements Criteria {
    private String myDate;

    public DateCriteria(String date) {
        myDate=date;
    }

    @Override
    public ArrayList<? extends Message> filterCriteria(ArrayList<? extends Message> messages) {
        ArrayList<Message> myList = new ArrayList<>();
        for (Message message : messages) {
            if (message.getTime().equals(myDate)){
                myList.add(message);
            }
        }
        return myList;
    }
}
