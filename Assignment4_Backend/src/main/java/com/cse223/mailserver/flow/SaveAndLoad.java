package com.cse223.mailserver.flow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SaveAndLoad {

	@SuppressWarnings("unchecked")
	public ArrayList<User> readUsersFromJson() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj ;
		try {
			obj = parser.parse(new FileReader(Constants.ACCOUNTS_JSON_PATH));
		}catch(Exception e) {
			return null;
		}





		ArrayList<UserClass> usersArrayList=new ArrayList<UserClass>();
		ArrayList<User> usersReadOnlyArrayList=new ArrayList<User>();

		JSONArray employeeList = (JSONArray) obj;
		for(int i=0;i<employeeList.size();i++) {
			JSONObject objects = (JSONObject) employeeList.get(i);

			String user=(String) objects.get("user");
			Gson gson=new Gson();
			UserClass json= gson.fromJson(user,UserClass.class);

			User userReadOnly=new UserClass(json.getUserName(),json.getPassword(),json.getEmail(),json.getBirthday());
			usersReadOnlyArrayList.add(userReadOnly);
			usersArrayList.add(json);


		}
		return usersReadOnlyArrayList;
	}

	public JSONArray readJsonArray() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader(Constants.ACCOUNTS_JSON_PATH));
		}catch(Exception E){
			return new JSONArray();

		}

		JSONArray employeeList = (JSONArray) obj;
		return employeeList;

	}

	public void sendMessage(MessageCreator messageCreator,String type,String mail) throws IOException, ParseException {
		JSONArray  arrayOfSent;
		if(readJsonArrayOfPreviousSent(mail,type)!=null) {
			arrayOfSent=readJsonArrayOfPreviousSent(mail,type);
		}else {
			arrayOfSent=new JSONArray();
		}

		Gson gson=new Gson();
		String json= gson.toJson(messageCreator);
		JSONObject userJson2=new JSONObject();
		userJson2.put(Constants.Sent,json);
		arrayOfSent.add(userJson2);

		FileWriter fileWriter=new FileWriter(Constants.DATABASE_PATH+mail+"//"+type+Constants.INDEX_JSON_PATH);
		fileWriter.write(arrayOfSent.toJSONString());
		fileWriter.flush();
		fileWriter.close();
		System.out.println(json);
	}

	public JSONArray readJsonArrayOfPreviousSent(String Email,String type) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(Constants.DATABASE_PATH + Email + "//" + type + Constants.INDEX_JSON_PATH));
			JSONArray prevoiusSent = (JSONArray) obj;
			return prevoiusSent;
		}catch (Exception ignored){
			return new JSONArray();
		}

	}

	@SuppressWarnings("unchecked")
	public ArrayList<MessageCreator> readMessages(String Email,String type) throws FileNotFoundException, IOException, ParseException {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(Constants.DATABASE_PATH + Email + "//" + type + Constants.INDEX_JSON_PATH));


			ArrayList<MessageCreator> MessagesArrayList = new ArrayList<MessageCreator>();

			JSONArray employeeList = (JSONArray) obj;
			for (int i = 0; i < employeeList.size(); i++) {
				JSONObject objects = (JSONObject) employeeList.get(i);

				String user = (String) objects.get(Constants.Sent);
				Gson gson = new Gson();
				MessageCreator json = gson.fromJson(user, MessageCreator.class);
				MessagesArrayList.add(json);
			}
			return MessagesArrayList;
		}catch (Exception e){
			return null;
		}
	}

	public int getMessageID(String Email,String folder) throws FileNotFoundException, IOException, ParseException {
		ArrayList<MessageCreator> folderMessage = readMessages(Email,folder);
		if(folderMessage==null) {
			return 1;
		}else {
			MessageCreator lastMessage=folderMessage.get(folderMessage.size()-1);
			return (lastMessage.getID())+1;
		}
	}
	public void ClearFileContent(String mail,String type) throws IOException, ParseException {
		JSONArray  arrayOfSent;
		arrayOfSent=new JSONArray();
		FileWriter fileWriter=new FileWriter(Constants.DATABASE_PATH +mail+"//"+type+ Constants.INDEX_JSON_PATH);
		fileWriter.write("");
		fileWriter.flush();
		fileWriter.close();
	}


	public JSONArray readJsonArrayOfPreviousContact(String Email) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(Constants.DATABASE_PATH+ Email + "//" + Constants.CONTACTS +  Constants.INDEX_JSON_PATH));
			JSONArray prevoiusSent = (JSONArray) obj;
			return prevoiusSent;
		}catch (Exception ignored){
			return new JSONArray();
		}

	}

	@SuppressWarnings("unchecked")
	public ArrayList<Contact> readContactsFromJson(String Email) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj ;
		try {
			obj = parser.parse(new FileReader(Constants.DATABASE_PATH+Email+"//"+Constants.CONTACTS+ Constants.INDEX_JSON_PATH));
		}catch(Exception e) {
			return null;
		}
		ArrayList<Contact> usersArrayList=new ArrayList<Contact>();

		JSONArray Contacts = (JSONArray) obj;
		for(int i=0;i<Contacts.size();i++) {
			JSONObject objects = (JSONObject) Contacts.get(i);

			String Contact=(String) objects.get(Constants.CONTACTS);
			Gson gson=new Gson();
			Contact json= gson.fromJson(Contact,Contact.class);

			usersArrayList.add(json);
		}
		return usersArrayList;
	}

	public void AddContact(String userEmail,Contact contact) throws IOException, ParseException {
		JSONArray  arrayOfContact;
		if(readJsonArrayOfPreviousContact(userEmail)!=null) {
			arrayOfContact=readJsonArrayOfPreviousContact(userEmail);
		}else {
			arrayOfContact=new JSONArray();
		}

		Gson gson=new Gson();
		String json= gson.toJson(contact);;
		JSONObject userJson2=new JSONObject();
		userJson2.put(Constants.CONTACTS,json);
		arrayOfContact.add(userJson2);

		FileWriter fileWriter=new FileWriter(Constants.DATABASE_PATH+userEmail+"//"+Constants.CONTACTS+ Constants.INDEX_JSON_PATH);
		fileWriter.write(arrayOfContact.toJSONString());
		fileWriter.flush();
		fileWriter.close();
		System.out.println(userJson2);
	}

}