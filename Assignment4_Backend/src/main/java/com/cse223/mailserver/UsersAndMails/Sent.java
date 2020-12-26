package com.cse223.mailserver.UsersAndMails;

import java.util.ArrayList;
import java.util.Date;
//////////////sent message class////////////
public class Sent extends Message {

    public Sent(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time , boolean priority) {
        super(iD, body, header, attaches, time , priority);

    }


}