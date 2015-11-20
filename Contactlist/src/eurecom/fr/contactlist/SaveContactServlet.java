package eurecom.fr.contactlist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class SaveContactServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {  
		resp.setContentType("text/html"); 
		
		// Let's output the basic HTML headers 
		PrintWriter out = resp.getWriter(); 
		out.println("<html><head><title>Modify a contact</title></head><body>"); 
		
		// Get the datastore 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		
		// Get the entity by key 
		Entity contact = null; 
		String name = "", phone = "", pict = "", email = ""; 
		try {  
			contact = datastore.get(KeyFactory.stringToKey(req.getParameter("id")));  
			name = (contact.getProperty("name") != null) ? (String) contact.getProperty("name") : "";
			phone = (contact.getProperty("phone") != null) ? (String) contact.getProperty("phone") : "";
			email = (contact.getProperty("email") != null) ? (String) contact.getProperty("email") : ""; 
			pict = (contact.getProperty("pict") != null) ? (String) contact.getProperty("pict") : "";
		} catch (EntityNotFoundException e) {  
			resp.getWriter().println("<p>Creating a new contact</p>");
		} catch (NullPointerException e) {
			// id parameter not present in the URL
			resp.getWriter().println("<p>Creating a new contact</p>");
		} 
		out.println("<form action=\"save\" method=\"post\" name=\"contact\">");
		
		// Let's build the form 
		out.println("<label>Name: </label><input name=\"name\" value=\"" + name + "\"/><br/>" + "<label>Phone: </label><input name=\"phone\" value=\"" + phone + "\"/><br/>" + "<label>Email: </label><input name=\"email\" value=\"" + email + "\"/><br/>" + "<label>Picture: </label><input name=\"pict\" value=\"" + pict + "\"/><br/>"); 
		out.println("<br/><input type=\"submit\" value=\"Continue\"/></form></body></html>"); 
	}
	/**   
	 * Save a contact in the DB. The contact can be new or already existent.   
	 * **/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Retrieve informations from the request  String contactName = req.getParameter("name");
		String contactName = req.getParameter("name");
		String contactPhone = req.getParameter("phone");
		String contactEmail = req.getParameter("email");
		String contactPict = req.getParameter("pict");   
		
		// Take a reference of the datastore  
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Generate or retrieve the key associated with an existent contact  
		// Create or modify the entity associated with the contact  
		Entity contact;  
		contact = new Entity("Contact", contactName);  
		contact.setProperty("name", contactName); 
		contact.setProperty("phone", contactPhone); 
		contact.setProperty("email", contactEmail); 
		contact.setProperty("pict", contactPict); 
		
		// Save in the Datastore 
		datastore.put(contact);   
		
		resp.getWriter().println("Contact " + contactName + " saved with key " +  KeyFactory.keyToString(contact.getKey()) + "!");
	}  
}
