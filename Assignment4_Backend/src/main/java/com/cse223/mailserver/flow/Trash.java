package com.cse223.mailserver.flow;

public class Trash extends Message {
	private Message messages;

	public Trash(int iD, MessageBody body, MessageHeader header, Attachments attaches, String time) {
		super(iD, body, header, attaches, time);
	}

	public Message getFromTrash() {
		return null;
	}
	public void Autodelete() {
		
	}
}
