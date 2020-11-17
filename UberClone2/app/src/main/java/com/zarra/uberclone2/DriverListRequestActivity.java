package com.zarra.uberclone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DriverListRequestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Button btngetrequests;
    private LocationManager locationManager;
    private LocationListener mLocationListener;
    private ListView mListView;
    private ArrayAdapter mAdapter;
    private ArrayList<String> nearbyDriveRequests;
    private ArrayList<Double> passengerLatitudes;
    private ArrayList<Double> passengerLongitudes;
    private ArrayList<String> requestCarUsernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list_request);

        mListView=findViewById(R.id.requestsListView);
        mListView.setOnItemClickListener(this);
        nearbyDriveRequests=new ArrayList<>();
        passengerLatitudes=new ArrayList<>();
        passengerLongitudes=new ArrayList<>();
        requestCarUsernames=new ArrayList<>();

        mAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,nearbyDriveRequests);
        mListView.setAdapter(mAdapter);
        locationManager = (LocationManager) DriverListRequestActivity.this.getSystemService(LOCATION_SERVICE);


        if(Build.VERSION.SDK_INT<23 || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            initializeLocationListener();
            }

        btngetrequests=findViewById(R.id.btngetrequests);
        btngetrequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 23) {
                    updateList();
                } else {
                    if (ContextCompat.checkSelfPermission(DriverListRequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(DriverListRequestActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);

                    } else {
                        Location currentDriverLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(currentDriverLocation==null)
                            currentDriverLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateRequestsListView(currentDriverLocation);
                    }
                }
            }
        });

    }

    private void updateList() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        Location currentDriverLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateRequestsListView(currentDriverLocation);
    }

    private void updateRequestsListView(Location driverLocation){
        if(driverLocation!=null){

            saveDriverLocationToParse(driverLocation);

            ParseGeoPoint driverCurrentLocation=new ParseGeoPoint(driverLocation.getLatitude(),driverLocation.getLongitude());
            ParseQuery<ParseObject> requestCarQuery=ParseQuery.getQuery("RequestCar");
            requestCarQuery.whereNear("passengerLocation",driverCurrentLocation);
            requestCarQuery.whereDoesNotExist("driver");

            requestCarQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e==null){

                            nearbyDriveRequests.clear();
                            passengerLatitudes.clear();
                            passengerLongitudes.clear();
                            requestCarUsernames.clear();

                    if(objects.size()>0 && e==null){
                        for(ParseObject nearRequest:objects){
                            Double milesDistanceToPassenger=driverCurrentLocation.distanceInMilesTo((ParseGeoPoint) nearRequest.get("passengerLocation"));
                            nearbyDriveRequests.add("There are "+Math.round(milesDistanceToPassenger*10)/10+" miles to "+nearRequest.get("username"));
                            passengerLatitudes.add(((ParseGeoPoint) nearRequest.get("passengerLocation")).getLatitude());
                            passengerLongitudes.add(((ParseGeoPoint) nearRequest.get("passengerLocation")).getLongitude());
                            requestCarUsernames.add(nearRequest.get("username").toString());
                        }
                    } else{
                        Toast.makeText(DriverListRequestActivity.this,"No requests yet",Toast.LENGTH_SHORT).show();
                    }

                        mAdapter.notifyDataSetChanged();
                }
                }
            }

            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.driver_logout)
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(DriverListRequestActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    initializeLocationListener();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                    Location currentDriverLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateRequestsListView(currentDriverLocation);

                }


            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(ContextCompat.checkSelfPermission(DriverListRequestActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            Location driverLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(driverLocation==null)
                driverLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(driverLocation!=null) {
                transitionToMap(driverLocation.getLatitude(), driverLocation.getLongitude(), passengerLatitudes.get(position), passengerLongitudes.get(position),requestCarUsernames.get(position));
            }
        }
    }

    private void transitionToMap(double driverLatitude, double driverLongitude,double requestLatitude,double requestLongitude,String username){
        Intent intent=new Intent(DriverListRequestActivity.this,ViewLocationsMapActivity.class);
        intent.putExtra("dLatitude",driverLatitude);
        intent.putExtra("dLongitude",driverLongitude);
        intent.putExtra("pLatitude",requestLatitude);
        intent.putExtra("pLongitude",requestLongitude);
        intent.putExtra("rUsername",username);
        startActivity(intent);
    }

    private void initializeLocationListener(){
        mLocationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void saveDriverLocationToParse(Location location){
        ParseUser driver=ParseUser.getCurrentUser();
        ParseGeoPoint driverLocation=new ParseGeoPoint(location.getLatitude(),location.getLongitude());
        driver.put("driverLocation",driverLocation);
        driver.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    //Toast.makeText(DriverListRequestActivity.this,"Saved location",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}