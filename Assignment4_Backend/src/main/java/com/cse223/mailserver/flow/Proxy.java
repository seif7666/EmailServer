package com.cse223.mailserver.flow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class Proxy {
	private String Email;
	private String password;
	private UserClass userClass;
	private SaveAndLoad saveAndLoad;
	public Proxy(String Email,String password) {
		this.Email=Email;
		this.password=password;
		saveAndLoad=new SaveAndLoad();
	}
	
	
	
	
	public User logIn() throws FileNotFoundException, IOException, ParseException {
		ArrayList<User> ExistUser;
		ExistUser=saveAndLoad.readUsersFromJson();
        for(int i=0;i<ExistUser.size();i++){
           if(Email.equals((String) ExistUser.get(i).getEmail())&&password.equals((String) ExistUser.get(i).getPassword())){
                   return ExistUser.get(i);
            }
        }
        return null;
		
	}
	
	
}
