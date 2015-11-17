package fr.eurecom.android.preferences;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HelloPreferences extends AppCompatActivity {

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Initialize preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = preferences.getString("username", "n/a");
                String password = preferences.getString("password", "n/a");
                // A toast is a view containing a quick little message for the user
                showPrefs(username, password);
            }
        });
    }

    private void showPrefs(String username, String password) {
        Toast.makeText(this, "You kept user: " + username + " and password: " + password, Toast.LENGTH_LONG).show();
    }

    public void openDialog(View v) {
        // Create out AlterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Reset username and password?");
        builder.setCancelable(true);
        builder.setPositiveButton("I agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Resetting Credential", Toast.LENGTH_LONG).show();
                reset_preferences();
                createNotification();
            }
        });
        builder.setNegativeButton("No, no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Keep Credential", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public  void reset_preferences() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("username", null);
        edit.putString("password", null);
        edit.commit(); //Apply changes
        Toast.makeText(this, "Reset user name and password", Toast.LENGTH_LONG).show();
    }

    public void createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //prepare intent which is triggered if the notification is selected
        Intent intent = new Intent(this, HelloPreferences.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Hello Preferences")
                .setContentText("Successfully reset user Credential")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(activity)
                .setAutoCancel(true)
                .addAction(R.drawable.icon, "Call", activity)
                .addAction(R.drawable.icon, "More", activity)
                .build();
        notificationManager.notify(0, notification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_preferences, menu);
        return true;
    }

    // This method is called once the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Launch Preference activity
            Intent i = new Intent(HelloPreferences.this, Preferences.class);
            startActivity(i);
            Toast.makeText(HelloPreferences.this, "Here you can store your user credentials.", Toast.LENGTH_LONG).show();
            Log.i("Main", "sent an intent to the preference class");
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    public  void exitApp(View v) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
