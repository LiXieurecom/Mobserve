package eurecom.fr.mycontactlist;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lixi on 11/17/15.
 */
public class Contact implements Serializable {

    private final String id;
    private final String name;
    private final String email;
    private final String phone;

    public Contact(JSONObject jsonObject) throws JSONException {

        id = jsonObject.getString("id");
        phone = jsonObject.getString("phone");
        email = jsonObject.getString("email");
        name = jsonObject.getString("name");

    }

    public Contact() {
        id = "";
        name = "";
        email = "";
        phone = "";
    }

    @Override
    public String toString(){
        return String.format("%s-%s",name,email);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}