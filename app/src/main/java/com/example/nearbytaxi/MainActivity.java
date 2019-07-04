package com.example.nearbytaxi;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import models.Company;
import models.Contact;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements  LocationListener {

    private GoogleMap mMap;
    private Marker mPerth;
    private String phone;
    private String sms;
    private String type;


    private boolean isLocation = false;

    SupportMapFragment mapFragment;
    private ArrayList<Company> companies;
    private static final String TAG = "MainActivity";
    double lat;
    double lon;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        getLocation();

//        createHandler();

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void createHandler(){
            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    isLocation = false;

                }
            },0,120000);

    }



    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        if(!isLocation) {
            OpenfreecabsDetails openfreecabsDetails = new OpenfreecabsDetails(MainActivity.this);
            NetworkUtils networkUtils = new NetworkUtils();
            networkUtils.setLocation(lat, lon);
            openfreecabsDetails.setFreecabsNetWork(networkUtils);
            openfreecabsDetails.execute();
        }
        isLocation = true;

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

    public  void insertPoint(ArrayList<Company> arrayList) {
        companies = arrayList;
        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng myLocation = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(myLocation)
                        .title("My location"));
                googleMap.setMinZoomPreference(8.0f);
                googleMap.setMaxZoomPreference(15.0f);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                GooglemapHelper.defaultMapSettings(googleMap);
                setUpClusterManager(googleMap);
            }
        });
    }

    private void setUpClusterManager(GoogleMap googleMap) {
        mMap = googleMap;

        ClusterManager<User> clusterManager = new ClusterManager<>(this, googleMap);
        mMap.setOnMarkerClickListener (clusterManager);
        List<User> arrays = null;
        for(Company company : companies){
            String name = company.name;
            ArrayList<LatLng> drivers = company.drivers;


            clusterManager.setRenderer(new MarkerClusterRenderer(this, googleMap, clusterManager, company));
            googleMap.setOnCameraIdleListener(clusterManager);


            for (int i = 0; i < drivers.size(); i++){
                arrays = new ArrayList<>();
                ArrayList<Contact> contacts = company.contacts;
                arrays.add(
                        new User(name,contacts,drivers.get(i))
                );

                clusterManager.addItems(arrays);
                clusterManager.cluster();

            }

        }

        clusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<User>() {
                    @Override public boolean onClusterItemClick(User clusterItem) {

                        ArrayList<Contact> contacts = clusterItem.getPhoneNumber();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        View view = inflater.inflate(R.layout.dialog_frame, null);
                        TextView smsTextView = view.findViewById(R.id.sms_number);
                        TextView phoneTextView = view.findViewById(R.id.phone_number);
                        TextView nameTextView = view.findViewById(R.id.name);
                        ImageView icon = view.findViewById(R.id.icon);
                        Button callButton = view.findViewById(R.id.call_button);
                        Button cancelButton = view.findViewById(R.id.cancel_button);
                        Button sendSmsButton = view.findViewById(R.id.sendsms_button);

                        icon.setImageResource(getIconId(clusterItem.getTitle()));
                        getContacts(contacts);
                        smsTextView.setText(sms);
                        phoneTextView.setText(phone);
                        nameTextView.setText(clusterItem.getTitle());
                        builder.setView(view);
                        final AlertDialog alert = builder.create();
                        alert.show();


                        callButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!TextUtils.isEmpty(phone)) {
                                    String dial = "tel:" + phone;
                                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                                }else {
                                    Toast.makeText(MainActivity.this, "Enter a phone number", LENGTH_SHORT).show();
                                }
                            }
                        });

                        sendSmsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(!TextUtils.isEmpty(sms)) {
                                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + sms));
                                    smsIntent.putExtra("sms_body", "");
                                    startActivity(smsIntent);
                                }
                            }
                        });

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.cancel();
                            }
                        });

                        return false;
                    }
                });



    }

    public void getContacts(ArrayList<Contact> contacts){
        type = contacts.get(0).contactNumber[0];

        switch (type){
            case "sms":
                sms = contacts.get(0).contactNumber[1];
            case "phone":
                phone = contacts.get(1).contactNumber[1];
            default:
                break;
        }
    }

    int getIconId(String name){
        switch (name){
            case "NambaTaxi":
                return (R.drawable.namba);
            case "SmsTaxi":
                return  (R.drawable.logo_sms);
            default:
                return (R.drawable.question);
        }
    }


}
