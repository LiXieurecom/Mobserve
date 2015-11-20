package eurecom.fr.mycontactlist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixi on 11/17/15.
 */
public class JsonLoader extends AsyncTaskLoader<List<Contact>> {
    public  JsonLoader(Context context){
        super(context);
        Log.i("main", "loader created");
    }

    @Override
    public List<Contact> loadInBackground() {
        Log.i("main", "loader in Background");
        URL page = null;
        try {
            page = new URL("http://lxlcontactlist.appspot.com/contactlist?respType=json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuffer text = new StringBuffer();
        HttpURLConnection conn = null;
        try {
            Log.i("main","before conn");
            conn = (HttpURLConnection) page.openConnection();
            Log.i("main","after conn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Log.i("main", "before connection");
            conn.connect();
            Log.i("main", "after connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader in = null;
        String jsonString=null;
        try {
            in = new InputStreamReader((InputStream) conn.getContent(),"UTF-8");
            Reader reader = in;
            jsonString = readAll(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonString!=null){
            List<Contact> result;
            try {
                JSONArray listOfContact = new JSONArray(jsonString);
                int len = listOfContact.length();
                result = new ArrayList<Contact>(len);
                Log.i("main","json string length is "+len);
                for(int i=0;i<len;i++) {
                    result.add(new Contact(listOfContact.getJSONObject(i)));
                }
            }catch (JSONException e){
                //TODO handle
                e.printStackTrace();
                return null;


            }
            return result;

        }else {


            return null;
        }
    }

    private String readAll(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder(4096);
        for (CharBuffer buf = CharBuffer.allocate(512); (reader.read(buf)) > -1; buf
                .clear()) {
            builder.append(buf.flip());
        }
        return builder.toString();
    }

}
