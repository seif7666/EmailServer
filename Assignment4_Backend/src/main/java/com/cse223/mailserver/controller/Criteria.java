package com.cse223.mailserver.controller;

import com.cse223.mailserver.UsersAndMails.Message;

import java.util.ArrayList;

public interface Criteria {
    public ArrayList<? extends Message> filterCriteria (ArrayList<? extends Message> messages);
}
