package eurecom.fr.contactlist;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;


@SuppressWarnings("serial")
public class DeleteContact extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		dataStore.delete(KeyFactory.stringToKey(req.getParameter("id")));
	}
}
