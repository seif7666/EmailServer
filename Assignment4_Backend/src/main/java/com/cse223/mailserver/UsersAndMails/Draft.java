package com.cse223.mailserver.UsersAndMails;

public class Draft extends Message {
/////////draft class////////////////
	public Draft(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time , boolean priority) {
		super(iD, body, header, attaches, time , priority);
	}

	private Message messages;

	public Message getMessages() {
		return messages; //return the messages that draft extends
	}
	

}
