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
			obj = parser.parse(new FileReader("data_base//accounts//Users.json"));
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
			obj = parser.parse(new FileReader("data_base//accounts//Users.json"));
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

		FileWriter fileWriter=new FileWriter("data_base//"+mail+"//"+type+"//index.json");
		fileWriter.write(arrayOfSent.toJSONString());
		fileWriter.flush();
		fileWriter.close();
		System.out.println(json);
	}

	public JSONArray readJsonArrayOfPreviousSent(String Email,String type) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("data_base//" + Email + "//" + type + "//index.json"));
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
			Object obj = parser.parse(new FileReader("data_base//" + Email + "//" + type + "//index.json"));


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
		FileWriter fileWriter=new FileWriter("data_base//"+mail+"//"+type+"//index.json");
		fileWriter.write("");
		fileWriter.flush();
		fileWriter.close();
	}


	public JSONArray readJsonArrayOfPreviousContact(String Email) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("data_base//" + Email + "//" + Constants.CONTACTS + "//index.json"));
			JSONArray prevoiusContact = (JSONArray) obj;
			return prevoiusContact;
		}catch (Exception ignored){
			return new JSONArray();
		}

	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> readContactsFromJson(String Email) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj ;
		try {
			obj = parser.parse(new FileReader("data_base//"+Email+"//"+Constants.CONTACTS+"//"+"index.json"));
		}catch(Exception e) {
			return null;
		}
		ArrayList<String> ContactsArrayList=new ArrayList<String>();

		JSONArray employeeList = (JSONArray) obj;
		for(int i=0;i<employeeList.size();i++) {
			JSONObject objects = (JSONObject) employeeList.get(i);
			//System.out.println(objects);
			String user=(String) objects.get(Constants.CONTACTS);


			ContactsArrayList.add(user);


		}
		return ContactsArrayList;
	}

	public void AddContact(String Email,String Contact) throws IOException, ParseException {
		JSONArray  arrayOfContact;
		if(readJsonArrayOfPreviousContact(Email)!=null) {
			arrayOfContact=readJsonArrayOfPreviousContact(Email);
		}else {
			arrayOfContact=new JSONArray();
		}


		JSONObject userJson2=new JSONObject();
		userJson2.put(Constants.CONTACTS,Contact);
		arrayOfContact.add(userJson2);

		FileWriter fileWriter=new FileWriter("data_base//"+Email+"//"+Constants.CONTACTS+"//index.json");
		fileWriter.write(arrayOfContact.toJSONString());
		fileWriter.flush();
		fileWriter.close();
		System.out.println(userJson2);
	}

}