package com.cse223.mailserver.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.cse223.mailserver.UsersAndMails.User;
import org.json.simple.parser.ParseException;
///////////////////login class//////////////
public class Login {
	private String password; //string password 
	private String Email; //string mail
	private Proxy proxy;//proxy 
	
	public Login(String Email,String password) {
		this.Email=Email; //////set email
		this.password=password;////set password
		
	}
	public User ExistOrNot() throws FileNotFoundException, IOException, ParseException {
		proxy=new Proxy(Email,password);//use proxy to see if exist 
		return proxy.logIn(); //return the case of proxy
		
	}

}
