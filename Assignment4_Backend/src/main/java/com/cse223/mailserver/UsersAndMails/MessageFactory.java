package com.cse223.mailserver.UsersAndMails;

import com.cse223.mailserver.Server.Constants;

/////////////////message factory class/////////////////
public class MessageFactory {

	public Message getMessage (String name ,MessageHeader header, MessageBody body,Attachments attaches, String time,int ID,boolean priority){
		if (name.equals(Constants.INBOX))
			return new Inbox(ID,body,header,attaches,time,priority);//return inbox message 
		if (name.equals(Constants.SENT))
			return new Sent(ID,body,header,attaches,time,priority);//return sent message 
		if (name.equals(Constants.TRASH))
			return new Trash(ID,body,header,attaches,time,priority); //return trash
		if (name.equals(Constants.DRAFT))
			return new Draft(ID,body,header,attaches,time,priority);//return draft message 

		return null;
	}
}
