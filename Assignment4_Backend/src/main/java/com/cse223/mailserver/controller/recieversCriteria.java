package com.cse223.mailserver.controller;


import com.cse223.mailserver.UsersAndMails.Message;

import java.util.ArrayList;

public class recieversCriteria  implements Criteria{
    private String reciever;
    public recieversCriteria(String receiver) {
        reciever =receiver;
    }

    @Override
    public ArrayList<? extends Message> filterCriteria(ArrayList<? extends Message> messages) {
        ArrayList<Message> myList = new ArrayList<>();
        for (Message message : messages) {
            for(String rec : message.getHeader().getReciever())
                if (rec.equals(reciever)){
                    myList.add(message);
                }
        }
        return myList;
    }
}
