package com.cse223.mailserver.flow;

public class Draft extends Message {

	public Draft(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time , boolean priority) {
		super(iD, body, header, attaches, time , priority);
	}

	private Message messages;

	public Message getMessages() {
		return messages;
	}
	

}
