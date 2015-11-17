package eurecom.fr.Intent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Implicit extends AppCompatActivity {

    private Uri imageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_implicit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callIntent(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button01:
                EditText textBrow = (EditText) findViewById(R.id.editText01);
                String url = textBrow.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://" + url));
                Intent chooser = Intent.createChooser(intent, "Surf the Internet via");
                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivity(chooser);
                }
                break;
            case R.id.button02:
                EditText textName = (EditText) findViewById(R.id.editText02);
                String name = textName.getText().toString();
                intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + name));
                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
                break;
            case R.id.button03:
                intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
                break;
            case R.id.button04:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:37.7749,-122.4194"));
                chooser = Intent.createChooser(intent, "Using:");
                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivity(chooser);
                }
                break;
            case R.id.button05:
                EditText textAddress = (EditText) findViewById(R.id.editText03);
                String address = textAddress.getText().toString();
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:37.7749,-122.4194?q=" + Uri.encode(address)));
                chooser = Intent.createChooser(intent, "Using:");
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivity(chooser);
                }
                break;
            case R.id.button06:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File tmpImage;
                try {
                    tmpImage = createImageFile();
                } catch (IOException e) {
                    Toast.makeText(this, "Creating File Failed!", Toast.LENGTH_LONG).show();
                    break;
                }
                imageURI = Uri.fromFile(tmpImage);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                chooser = Intent.createChooser(intent, "Take the picture via");
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(chooser, 1);
                }
                break;
            case R.id.button07:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("content://contacts/people/"));
                startActivity(intent);
                break;
            case R.id.button08:
                intent = new Intent(Intent.ACTION_EDIT,
                        Uri.parse("content://contacts/people/1"));
                startActivity(intent);
                break;
            case R.id.button09:
                intent = new Intent(Intent.ACTION_EDIT);
                //intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(Events.TITLE, "Learn Android");
                intent.putExtra(Events.EVENT_LOCATION, "Home suit home");
                intent.putExtra(Events.DESCRIPTION, "Download Examples");

                // Setting dates
                GregorianCalendar calDate = new GregorianCalendar(2015, 11, 1);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());

                // make it a full day event
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                // make it a recurring Event
                intent.putExtra(Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

                // Making it private and shown as busy
                intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
                intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageURI);
                if(bitmap == null){
                    System.out.println("FAIL");
                } else {
                    System.out.println("SAVED!!");
                    Toast.makeText(this, "Picture stored in " + imageURI, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e){
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return  new File(storageDir, imageFileName);
    }
}
