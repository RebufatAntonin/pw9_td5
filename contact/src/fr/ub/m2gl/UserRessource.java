package fr.ub.m2gl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


//Ajouter* / Modifier* / Supprimer* / Afficher un* / Afficher list*

@Path("/users")
public class UserRessource {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    public String addUser(User u){
    	u.saveToDB();
	    return "Utilisateur " + u.getFirstname() + " " + u.getLastname() + " added successfully.";

    }
	
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listUser() {
    	ArrayList<User> userList=new ArrayList<User>();
    	MongoClient mongoClient = new MongoClient();
		try {
    	    MongoDatabase db = mongoClient.getDatabase("myBase");
    	    MongoCollection<Document> collection = db.getCollection("myCollection");
    	    ArrayList<Document> docs=collection.find().into(new ArrayList<Document>());
    	    for(Document user:docs){
    	    	if(user.getString("lastname")!=null){
    	    		User u=new User(user.getString("firstname"), user.getString("lastname"));
    	    		userList.add(u);
    	    	}
    	    }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	} finally{
    	    mongoClient.close();
    	}
		return userList; 
    }
    
    @GET
    @Path("/{lastname}/{firstname}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("lastname") String lastname, @PathParam("firstname") String firstname ) {
    	User user = null;
    	MongoClient mongoClient = new MongoClient();
		try {
    	    MongoDatabase db = mongoClient.getDatabase("myBase");
    	    MongoCollection<Document> collection = db.getCollection("myCollection");
    	    ArrayList<Document> docs=collection.find().into(new ArrayList<Document>());
    	    for(Document doc:docs){
    	    	if(doc.getString("lastname")!=null && doc.getString("lastname").equals(lastname) && doc.getString("firstname").equals(firstname)){
    	    		user=new User(doc.getString("firstname"), doc.getString("lastname"));
    	    	}
    	    }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	} finally{
    	    mongoClient.close();
    	}
		return user; 
    }
    
    @DELETE
	@Path("/{lastname}/{firstname}")
    public void deleteUser(@PathParam("lastname") String lastname, @PathParam("firstname") String firstname ) {
		User user=new User(firstname, lastname);
		user.deleteFromDB();
	}
    
    @PUT
    @Path("/{lastname}/{firstname}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUser(@PathParam("lastname") String lastname, @PathParam("firstname") String firstname , User modified) {
		User user=new User(firstname, lastname);
		user.updateInDB(modified);
	}
    
}
