package com.cse223.mailserver.flow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
		FileWriter fileWriter=new FileWriter("data_base//accounts//Users.json");
		fileWriter.write(previousUsers.toJSONString());
		fileWriter.flush();
		fileWriter.close();
		File file = new File("data_base//" + user.getEmail());
		file.mkdir();
		File file1 = new File("data_base//" + user.getEmail()+"//"+Constants.Inbox);
		file1.mkdir();
		File indexFileInbox = new File("data_base//" + user.getEmail() + "//"+Constants.Inbox+"//index.json");
		indexFileInbox.createNewFile();
		File file2 = new File("data_base//" + user.getEmail()+"//"+Constants.Sent);
		file2.mkdir();
		File indexFileSent = new File("data_base//" + user.getEmail()+ "//"+Constants.Sent+"//index.json");
		indexFileSent.createNewFile();
		File file3 = new File("data_base//" + user.getEmail()+"//"+Constants.Trash);
		file3.mkdir();
		File indexFileTrash = new File("data_base//" + user.getEmail() + "//"+Constants.Trash+"//index.json");
		indexFileTrash.createNewFile();
		File file4 = new File("data_base//" + user.getEmail()+"//"+Constants.Draft);
		file4.mkdir();
		File indexFileDraft = new File("data_base//" + user.getEmail()+ "//"+Constants.Draft+"//index.json");
		indexFileDraft.createNewFile();
		File file5 = new File("data_base//" + user.getEmail()+"//"+Constants.CONTACTS);
		file5.mkdir();
		File indexFileContacts = new File("data_base//" + user.getEmail()+ "//"+Constants.CONTACTS+"//index.json");
		indexFileContacts.createNewFile();
		File file6 = new File("data_base//" + user.getEmail()+"//"+Constants.ATTACHEMENTS);
		file6.mkdir();
		File file7 = new File("data_base//" + user.getEmail()+"//"+Constants.ATTACHEMENTS+"//"+Constants.ATTACHEMENTS_SENT);
		file7.mkdir();
		File file8 = new File("data_base//" + user.getEmail()+"//"+Constants.ATTACHEMENTS+"//"+Constants.ATTASHEMENTS_DRAFTS);
		file8.mkdir();
		File file9 = new File("data_base//" + user.getEmail()+"//"+Constants.ATTACHEMENTS+"//"+Constants.ATTACHEMENTS_INBOX);
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
