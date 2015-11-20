package eurecom.fr.mycontactlist;

import android.os.AsyncTask;

/**
 * Created by lxllx on 2015/11/20.
 */
public class ModifyTask extends AsyncTask<ResponseHandler, Void, String> {

    private ResponseHandler rh;

    @Override
    protected String doInBackground(ResponseHandler... rhs) {
        rh = rhs[0];
        return rh.start();
    }
    @Override
    protected void onPostExecute(String response) {
        rh.respond(response);
    }

}

