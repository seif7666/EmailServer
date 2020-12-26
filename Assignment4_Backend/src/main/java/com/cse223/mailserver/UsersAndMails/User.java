package com.cse223.mailserver.UsersAndMails;

import java.util.Date;
/**
 * Read-Only-Interface
 */

public interface  User {
	
	public String getUserName() ; //get user name 
	public String getPassword();//get user pasword
	public String getEmail(); //get user email
	public String  getBirthday();//get user birthday
	
	

}
