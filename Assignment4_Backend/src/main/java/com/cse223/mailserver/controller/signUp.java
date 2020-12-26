package com.cse223.mailserver.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.cse223.mailserver.Server.Constants;
import com.cse223.mailserver.UsersAndMails.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class signUp {
	private String userName;
	private String password;
	private String Email;
	private Date birthday;
	private SaveAndLoad saveAndLoad=new SaveAndLoad();

	public boolean addUser(User user) throws IOException, ParseException {
		JSONArray previousUsers;
		if(exist_signup(user)) return false;
		if(saveAndLoad.readJsonArray()!=null) {
			previousUsers=saveAndLoad.readJsonArray();
		}else {
			previousUsers=new JSONArray();
		}

		JSONObject userJson=new JSONObject();
		JSONArray  arrayOfUsers=new JSONArray();
		Gson gson=new Gson();
		String json= gson.toJson(user);
		JSONObject userJson2=new JSONObject();
		userJson2.put("user",json);
		previousUsers.add(userJson2);



		//arrayOfUsers.put("User", arrayOfUsers);
		FileWriter fileWriter=new FileWriter(Constants.ACCOUNTS_JSON_PATH);
		fileWriter.write(previousUsers.toJSONString());
		fileWriter.flush();
		fileWriter.close();
		File file = new File(Constants.DATABASE_PATH + user.getEmail());
		file.mkdir();
		File file1 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.INBOX);
		file1.mkdir();
		File indexFileInbox = new File(Constants.DATABASE_PATH + user.getEmail() + "//"+Constants.INBOX +Constants.INDEX_JSON_PATH);
		indexFileInbox.createNewFile();
		File file2 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.SENT);
		file2.mkdir();
		File indexFileSent = new File(Constants.DATABASE_PATH + user.getEmail()+ "//"+Constants.SENT +Constants.INDEX_JSON_PATH);
		indexFileSent.createNewFile();
		File file3 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.TRASH);
		file3.mkdir();
		File indexFileTrash = new File(Constants.DATABASE_PATH + user.getEmail() + "//"+Constants.TRASH +Constants.INDEX_JSON_PATH);
		indexFileTrash.createNewFile();
		File file4 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.DRAFT);
		file4.mkdir();
		File indexFileDraft = new File(Constants.DATABASE_PATH + user.getEmail()+ "//"+Constants.DRAFT +Constants.INDEX_JSON_PATH);
		indexFileDraft.createNewFile();
		File file5 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.CONTACTS);
		file5.mkdir();
		File indexFileContacts = new File(Constants.DATABASE_PATH + user.getEmail()+ "//"+Constants.CONTACTS+Constants.INDEX_JSON_PATH);
		indexFileContacts.createNewFile();
		File file6 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.ATTACHMENTS);
		file6.mkdir();
		File file7 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.ATTACHMENTS +"//"+Constants.ATTACHMENTS_SENT);
		file7.mkdir();
		File file8 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.ATTACHMENTS +"//"+Constants.ATTACHMENTS_DRAFTS);
		file8.mkdir();
		File file9 = new File(Constants.DATABASE_PATH + user.getEmail()+"//"+Constants.ATTACHMENTS +"//"+Constants.ATTACHMENTS_INBOX);
		file9.mkdir();
		return true;



	}

	public boolean exist_signup(User user) throws FileNotFoundException, IOException, ParseException{
		ArrayList<User> ExistUser;
		if(saveAndLoad.readUsersFromJson()==null)
			return false;
		ExistUser=saveAndLoad.readUsersFromJson();
		for(int i=0;i<ExistUser.size();i++){
			if(user.getEmail().equals((String) ExistUser.get(i).getEmail())){
				return true;
			}
		}
		return false;
	}


	
	 
	

}
