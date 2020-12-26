package com.cse223.mailserver.UsersAndMails;
/////////////trash class/////////////
public class Trash extends Message {
	private Message messages;

	public Trash(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time , boolean priority) {
		super(iD, body, header, attaches, time , priority);
	}

	
	
}
