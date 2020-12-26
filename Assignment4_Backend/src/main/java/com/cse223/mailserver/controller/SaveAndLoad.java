package com.cse223.mailserver.controller;

import java.io.*;
import java.util.ArrayList;

import com.cse223.mailserver.Server.Constants;
import com.cse223.mailserver.UsersAndMails.Contact;
import com.cse223.mailserver.UsersAndMails.MessageCreator;
import com.cse223.mailserver.UsersAndMails.User;
import com.cse223.mailserver.UsersAndMails.UserClass;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//////////////save and load class ////////////////////
public class SaveAndLoad {
//////get prevois users////////////////////////
	@SuppressWarnings("unchecked")
	public ArrayList<User> readUsersFromJson() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();//json parser
		Object obj ;
		try {
			obj = parser.parse(new FileReader(Constants.ACCOUNTS_JSON_PATH));//read previos users 
		}catch(Exception e) {
			return null;//if first user 
		}
		ArrayList<UserClass> usersArrayList=new ArrayList<UserClass>();//arraylist of user class
		ArrayList<User> usersReadOnlyArrayList=new ArrayList<User>();//array list of user interface
		JSONArray employeeList = (JSONArray) obj;//json array of users 
		for(int i=0;i<employeeList.size();i++) {
			JSONObject objects = (JSONObject) employeeList.get(i);
/////////////get user json object////////////
			String user=(String) objects.get("user");
			Gson gson=new Gson();
			UserClass json= gson.fromJson(user, UserClass.class);//parse to class user
//////////////set user read only class //////////////////////////
			User userReadOnly=new UserClass(json.getUserName(),json.getPassword(),json.getEmail(),json.getBirthday());
			usersReadOnlyArrayList.add(userReadOnly);
			usersArrayList.add(json); //add the user 


		}
		return usersReadOnlyArrayList;//return the users 
	}
////////////////read json arraylist of previous users  /////////
	public JSONArray readJsonArray() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();//parser 
		Object obj;
		try {
			obj = parser.parse(new FileReader(Constants.ACCOUNTS_JSON_PATH)); ///get users 
		}catch(Exception E){
			return new JSONArray();//return new json array if we didn't have prevois 

		}

		JSONArray users = (JSONArray) obj;
		return users;//return the users 

	}
///////////put  message to folder//////////
	public void sendMessage(MessageCreator messageCreator, String type, String mail) throws IOException, ParseException {
		JSONArray  arrayOfSent;///json arraylist 
		if(readJsonArrayOfPreviousSent(mail,type)!=null) {
			arrayOfSent=readJsonArrayOfPreviousSent(mail,type);//get previous if exist 
		}else {
			arrayOfSent=new JSONArray(); //new if there isn't previous message in folder 
		}
//////////Gson to pare the class/////////////
		Gson gson=new Gson();
		String json= gson.toJson(messageCreator); ///////json of class 
		JSONObject userJson2=new JSONObject();
		userJson2.put(Constants.MESSAGE,json);//put json in json object 
		arrayOfSent.add(userJson2);//add json object 
///////////////////////put the message in the file //////////////////////
		FileWriter fileWriter=new FileWriter(Constants.DATABASE_PATH+mail+"//"+type+ Constants.INDEX_JSON_PATH);
		fileWriter.write(arrayOfSent.toJSONString());
		fileWriter.flush();
		fileWriter.close();////////close file 
	}
/////////////get previous json array of the message  
	public JSONArray readJsonArrayOfPreviousSent(String Email,String type) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();//////parser to pare json 
		try {
			Object obj = parser.parse(new FileReader(Constants.DATABASE_PATH + Email + "//" + type + Constants.INDEX_JSON_PATH));
			JSONArray prevoiusSent = (JSONArray) obj; //get prevois messages 
			return prevoiusSent; //return messages 
		}catch (Exception ignored){
			return new JSONArray(); //return new json arraylist if there isn't previous message 
		}

	}
///////////////////////get previous messages 
	@SuppressWarnings("unchecked")
	public ArrayList<MessageCreator> readMessages(String Email,String type) throws FileNotFoundException, IOException, ParseException {
		try {
			JSONParser parser = new JSONParser();//json parser 
			Object obj = parser.parse(new FileReader(Constants.DATABASE_PATH + Email + "//" + type + Constants.INDEX_JSON_PATH));


			ArrayList<MessageCreator> MessagesArrayList = new ArrayList<MessageCreator>();//arraylist of messages 

			JSONArray employeeList = (JSONArray) obj;
			for (int i = 0; i < employeeList.size(); i++) {////itereate on each json object to parse it to the class 
				JSONObject objects = (JSONObject) employeeList.get(i);
///////////////parse json to the class //////////////
				String user = (String) objects.get(Constants.MESSAGE);
				Gson gson = new Gson();//Gson to parse the json 
				MessageCreator json = gson.fromJson(user, MessageCreator.class);
				MessagesArrayList.add(json);//add messgaes 
			}
			return MessagesArrayList;///return arraylist of the messages
		}catch (Exception e){
			return null;
		}
	}
///////////generate message id //////////////////////
	public int getMessageID(String Email,String folder) throws FileNotFoundException, IOException, ParseException {
		ArrayList<MessageCreator> folderMessage = readMessages(Email,folder);//get message from folder we want 
		if(folderMessage==null) {
			return 1;//return 1 if didn't have previous message 
		}else {
			MessageCreator lastMessage=folderMessage.get(folderMessage.size()-1);//get last message id 
			return (lastMessage.getID())+1; /////return new message id 
		}
	}
	
	///////////////clear file ////////////
	public void ClearFileContent(String mail,String type) throws IOException, ParseException {
		JSONArray  arrayOfSent;
		arrayOfSent=new JSONArray();
		FileWriter fileWriter=new FileWriter(Constants.DATABASE_PATH +mail+"//"+type+ Constants.INDEX_JSON_PATH);//get the file we want 
		fileWriter.write("");///clear the file 
		fileWriter.flush();
		fileWriter.close();//close file 
	}


	public JSONArray readJsonArrayOfPreviousContact(String Email) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();/////parser of json 
		try {
			Object obj = parser.parse(new FileReader(Constants.DATABASE_PATH+ Email + "//" + Constants.CONTACTS +  Constants.INDEX_JSON_PATH));/////get directory and object
			JSONArray prevoiusSent = (JSONArray) obj;
			return prevoiusSent; //return the prvious contact
		}catch (Exception ignored){
			return new JSONArray();//return new if user don't have contact
		}

	}
/////////////get prevois contact////////////////////
	@SuppressWarnings("unchecked")
	public ArrayList<Contact> readContactsFromJson(String Email) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();//parser of json 
		Object obj ;
		try {
			obj = parser.parse(new FileReader(Constants.DATABASE_PATH+Email+"//"+Constants.CONTACTS+ Constants.INDEX_JSON_PATH));//get previos
		}catch(Exception e) {
			return null;// return null if user didn't have contact
		}
		ArrayList<Contact> usersArrayList=new ArrayList<Contact>();///array list of user

		JSONArray Contacts = (JSONArray) obj;////get json from file
		for(int i=0;i<Contacts.size();i++) {
			JSONObject objects = (JSONObject) Contacts.get(i);

			String Contact=(String) objects.get(Constants.CONTACTS);//parsing to class 
			Gson gson=new Gson();
			Contact json= gson.fromJson(Contact,Contact.class);
///////////add the contact///////////////
			usersArrayList.add(json);
		}
		return usersArrayList;//return contact 
	}
/////////////add contact function////////////////////////
	public void AddContact(String userEmail, Contact contact) throws IOException, ParseException {
		JSONArray  arrayOfContact;//to get previous contact
		if(readJsonArrayOfPreviousContact(userEmail)!=null) {
			arrayOfContact=readJsonArrayOfPreviousContact(userEmail);//get previous contact 
		}else {
			arrayOfContact=new JSONArray(); //new arraylist if now found previos contact
		}
/////////////////change class to json/////////////
		Gson gson=new Gson();
		String json= gson.toJson(contact);;
		JSONObject userJson2=new JSONObject();
		userJson2.put(Constants.CONTACTS,json);
		arrayOfContact.add(userJson2);/////add json object to the array list 
//////////////write new contact to file///////////////////////
		FileWriter fileWriter=new FileWriter(Constants.DATABASE_PATH+userEmail+"//"+Constants.CONTACTS+ Constants.INDEX_JSON_PATH);
		fileWriter.write(arrayOfContact.toJSONString());
		fileWriter.flush();
		fileWriter.close();
	}
//////////////////make first directory////////////////////////
	public void makeFirstDirectory() throws IOException {
		File file9 = new File(Constants.DATABASE_PATH);
		file9.mkdir(); //make data_base folder 
		File file10 = new File(Constants.ACCOUNTS_PATH  );
		file10.mkdir();//make accounts folder

		File indexFileDraft = new File(Constants.ACCOUNTS_JSON_PATH );
		indexFileDraft.createNewFile();//make index jason file in accounts 
	}

}