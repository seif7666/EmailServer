package com.cse223.mailserver.controller;





import com.cse223.mailserver.UsersAndMails.Message;

import java.util.ArrayList;

public class BodyCriteria implements Criteria {
    private String myBody;

    public BodyCriteria(String body) {
        myBody=body;
    }

    @Override
    public ArrayList<? extends Message> filterCriteria(ArrayList<? extends Message> messages) {
        ArrayList<Message> myList = new ArrayList<>();
        for (Message message : messages) {
            if (message.getBody().getBody().contains(myBody)){
                myList.add(message);
            }
        }
        return myList;
    }
}
