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
public class ContactDetailsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {  
		if (req.getParameter("id") == null) { 
			resp.getWriter().println("ID cannot be empty!");  
			return; 
		}  
		
		// Get the datastore 
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
		
		// Get the entity by key
		Entity contact = null;
		try {  
			contact = datastore.get(KeyFactory.stringToKey(req.getParameter("id")));
			} catch (EntityNotFoundException e) {  
			resp.getWriter().println("Sorry, no contact for the given key"); 
			return; 
		} 
		
		// Let's output the basic HTML headers 
		PrintWriter out = resp.getWriter(); 
		resp.setContentType("text/html"); out.println("<html><head><title>Contacts list</title></head><body>");  
		
		// Let's build the table headers 
		out.println("<table style=\"border: 1px solid black; width: auto; text-align: center;\">" + "<tr><td rowspan=3><img src=\"" + contact.getProperty("pict") + "\" alt=\"Profile picture\" border=1/></td>"); 
		out.println("<td>Name: </td><td>" + contact.getProperty("name") + "</td></tr>" + "<tr><td>Phone: </td><td>" + contact.getProperty("phone") + "</td></tr>" + "<tr><td>Email: </td><td><a href=\"mailto:"  + contact.getProperty("email") + "\">" + contact.getProperty("email") + "</a>" + "</td></tr>"); 
		out.println("</table><a href=\"save?id=" + req.getParameter("id") + "\">Modify</a></body></html>");
	}  
}
