package com.cse223.mailserver.flow;

public class Draft extends Message {

	public Draft(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time) {
		super(iD, body, header, attaches, time);
	}

	private Message messages;

	public Message getMessages() {
		return messages;
	}
	

}
