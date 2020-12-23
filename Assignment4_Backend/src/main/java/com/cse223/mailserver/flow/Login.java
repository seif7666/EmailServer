package com.cse223.mailserver.flow;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

public class Login {
	private String password;
	private String Email;
	private Proxy proxy;
	
	public Login(String Email,String password) {
		this.Email=Email;
		this.password=password;
		
	}
	public User ExistOrNot() throws FileNotFoundException, IOException, ParseException {
		proxy=new Proxy(Email,password);
		return proxy.logIn();
		
	}

}
