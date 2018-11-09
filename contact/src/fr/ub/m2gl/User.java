package fr.ub.m2gl;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class User {
	private String firstName;
	private String lastName;
	
	public User(){
		
	}
	
	public User(String firstName, String lastName){
		this.firstName=firstName;
		this.lastName=lastName;
	}
	
	public void setFirstname(String fname){
		this.firstName=fname;
	}
	
	public void setLastname(String lname){
		this.lastName=lname;
	}
	
	public String getFirstname(){
		return this.firstName;
	}
	
	public String getLastname(){
		return this.lastName;
	}
	
	public void saveToDB(){
		MongoClient mongoClient = new MongoClient();
		try {
    	    MongoDatabase db = mongoClient.getDatabase("myBase");
    	    MongoCollection<Document> collection = db.getCollection("myCollection");

    	    ObjectMapper mapper = new MyObjectMapperProvider().getContext(User.class);
    	    String jsonString = mapper.writeValueAsString(this);
    	    Document doc = Document.parse(jsonString);
    	    collection.insertOne(doc);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	} finally{
    	    mongoClient.close();
    	}
	}
	
	public void updateInDB(User modified){
		MongoClient mongoClient = new MongoClient();
		try {
    	    MongoDatabase db = mongoClient.getDatabase("myBase");
    	    MongoCollection<Document> collection = db.getCollection("myCollection");

    	    ObjectMapper mapper = new MyObjectMapperProvider().getContext(User.class);
    	    String jsonStringOrigin = mapper.writeValueAsString(this);
    	    Document docOrigin=Document.parse(jsonStringOrigin);
    	    String jsonStringModified = mapper.writeValueAsString(modified);
    	    Document docModified=Document.parse(jsonStringModified);
    	    collection.findOneAndReplace(docOrigin, docModified);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	} finally{
    	    mongoClient.close();
    	}
	}
	
	public void deleteFromDB(){
		MongoClient mongoClient = new MongoClient();
		try {
    	    MongoDatabase db = mongoClient.getDatabase("myBase");
    	    MongoCollection<Document> collection = db.getCollection("myCollection");

    	    ObjectMapper mapper = new MyObjectMapperProvider().getContext(User.class);
    	    String jsonString = mapper.writeValueAsString(this);
    	    Document doc = Document.parse(jsonString);
    	    collection.deleteOne(doc);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	} finally{
    	    mongoClient.close();
    	}
	}
}
