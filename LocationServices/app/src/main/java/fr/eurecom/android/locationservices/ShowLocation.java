package fr.eurecom.android.locationservices;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowLocation extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    protected LocationManager locationManager = null;
    private String provider;
    Location location;
    TextView latitudeField;
    TextView longitudeField;
    public static final int MY_PERMISSIONS_LOCATION = 0;
    private GoogleMap googleMap;
    static final LatLng NICE = new LatLng(43.7031, 7.02661);
    static final LatLng EURECOM = new LatLng(43.614376, 7.070450);
    PendingIntent pendingIntent;
    public SharedPreferences sharedPreferences;
    private static final String PROX_ALERT_INTENT = "fr.eurecom.locationservices.android.lbs.ProximityAlert";
    private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
    private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);
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

        Criteria criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission: ", "To be checked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_LOCATION);
            return;
        } else
            Log.i("Permission: ", "GRANTED");
        latitudeField = (TextView) findViewById(R.id.TextView04);
        longitudeField = (TextView) findViewById(R.id.TextView02);
        location = locationManager.getLastKnownLocation(provider);
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);
        updateLocationView();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = getSharedPreferences("location", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_location, menu);
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

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location", "LOCATION CHANGED!!!");
        this.location = location;
        updateLocationView();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Log.i("GPS", "not enabled");
            // Build an alert dialog here that requests the user
            // to enable location services when he clicks over "ok"
            enableLocationSettings();
        } else {
            Log.i("GPS", "enabled");
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Access:", "Now permissions are granted");
                    // permission was granted, yay!
                } else {
                    Log.i("Access:", " permissions are denied");
                    //disable the functionality that depends on this permission.
                }
                break;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_LOCATION);
            }
        } else {
            return;
        }
        locationManager.removeUpdates(this);
    }

    public void changMapType(View view){
        Log.i("showLocation", "Entered");
        switch (view.getId()){
            case R.id.button01:
                if(googleMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
        }
    }

    public void updateLocationView(){
        if (location != null){
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latitudeField.setText(String.valueOf(lat));
            longitudeField.setText(String.valueOf(lng));
        } else{
            Log.i("showLocation","NULL");
        }
    }

    @Override
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;
        CameraPosition cameraPosition = new CameraPosition.Builder().target(EURECOM).zoom(17).bearing(90).tilt(30).build();
        googleMap.addMarker(new MarkerOptions().position(NICE).title("Nice").snippet("Enjoy French Riviera"));
        googleMap.addMarker(new MarkerOptions().position(EURECOM).title("EURECOM").snippet("ENJOY STUDY!"));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this,"Your are clicking:" + latLng.toString(), Toast.LENGTH_LONG).show();
        LatLng point = latLng;

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission: ", "To be checked");
            return;
        } else {
            Log.i("Permission: ", "GRANTED");
        }
        saveCoordinatesInPreferences((float) point.latitude, (float) point.longitude);
        locationManager.addProximityAlert(point.latitude, point.longitude, 20, -1, proximityIntent);
        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new ProximityIntentReceiver(), filter);
        Log.i("Registred", "proximity");
        Toast.makeText(getBaseContext(), "Added a proximity Alert", Toast.LENGTH_LONG).show();
        ++count;
    }

    private void saveCoordinatesInPreferences(float latitude, float longitude) {
        SharedPreferences prefs = this.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putFloat(POINT_LATITUDE_KEY, latitude);
        prefsEditor.putFloat(POINT_LONGITUDE_KEY, longitude);
        prefsEditor.commit();
    }
}
