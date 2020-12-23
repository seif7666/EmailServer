package com.cse223.mailserver.flow;

import java.util.ArrayList;
import java.util.Date;

public class Sent extends Message {

    public Sent(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time) {
        super(iD, body, header, attaches, time);

    }


}