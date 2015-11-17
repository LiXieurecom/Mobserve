package fr.eurecom.android.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Jeckie on 2015/11/1.
 */
public class Preferences extends PreferenceActivity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load the preferences from an xml resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
