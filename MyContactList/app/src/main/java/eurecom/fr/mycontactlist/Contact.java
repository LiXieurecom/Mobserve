package eurecom.fr.mycontactlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lixi on 11/17/15.
 */
public class Contact implements Serializable {

    private final String id;
    private final String name;
    private final String email;
    private final String phone;
    private final String url;
    private transient final Bitmap pict;

    public Contact(JSONObject jsonObject) throws JSONException {

        id = jsonObject.getString("id");
        phone = jsonObject.getString("phone");
        email = jsonObject.getString("email");
        name = jsonObject.getString("name");
        url = jsonObject.getString("pict");
        pict = getPict(url);
    }

    public Contact() {
        id = "";
        name = "";
        email = "";
        phone = "";
        url = "";
        pict = null;
    }

    @Override
    public String toString(){
        return String.format("%s-%s",name,email);
    }

    private Bitmap getPict (String url) {
        URL pictUrl = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            Log.d("url:", url);
            pictUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection) pictUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in = (InputStream) conn.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in);
    }

    public Bitmap getPict() {return pict;}

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

    public String getUrl() { return url;}

}