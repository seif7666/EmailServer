package com.cse223.mailserver.library;

import java.io.Serializable;

public class node implements Serializable {
	public Object data;
	public node next=null;
	public node(Object data) {
		this.data=data;
		
	}

}
