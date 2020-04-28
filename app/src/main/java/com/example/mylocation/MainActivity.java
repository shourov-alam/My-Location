package com.example.mylocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    TextView detailAdd,locality,division,country,country_code,lat,lon,dist,subloc;
    LinearLayout map;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private  double Lat;
    private  double Lon;
    private Geocoder geocoder;
    private List<Address> addresses;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        detailAdd=findViewById(R.id.detailAddTV);
        locality=findViewById(R.id.localityTV);
        division=findViewById(R.id.divisionTV);
        country=findViewById(R.id.countryTV);
        country_code=findViewById(R.id.countryCodeTV);
        lat=findViewById(R.id.latitudeTV);
        lon=findViewById(R.id.longitudeTV);
        map=findViewById(R.id.googleMap);
        swipeRefreshLayout=findViewById(R.id.swipe);
        dist=findViewById(R.id.districtTV);
        subloc=findViewById(R.id.sublocalityTV);


        call();







       // fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        //createLocationRequest();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.com/maps/search/?api=1&query=" + Lat + "," + Lon + "&travelmode=driving";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                call();

               /* final Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        call();

                    }
                });

                 thread.run();


                final Thread thread2=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

                thread2.run(); */

               //  swipeRefreshLayout.setRefreshing(false);


             /*   new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        call();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000); */

            }
        });

    }


    private void call() {


        geocoder = new Geocoder(this, Locale.ENGLISH);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Lat = location.getLatitude();
                    Lon = location.getLongitude();

                    try {
                        addresses=geocoder.getFromLocation(Lat,Lon,1);
                        Address address = addresses.get(0);

                        String detail_add = address.getAddressLine(0);


                        String locality_info = address.getLocality();
                        String dis=address.getSubAdminArea();
                        String division_info = address.getAdminArea();
                        String country_info = address.getCountryName();
                        String countrycode_info = address.getCountryCode();
                        String sub_locality_info = address.getSubLocality();


                        detailAdd.setText(detail_add);
                        subloc.setText(sub_locality_info);
                        locality.setText(locality_info);
                        dist.setText(dis);
                        division.setText(division_info);
                        country.setText(country_info);
                        country_code.setText(countrycode_info);
                        lat.setText(String.valueOf(Lat));
                        lon.setText(String.valueOf(Lon));


                        swipeRefreshLayout.setRefreshing(false);



                        //  Toast.makeText(getApplicationContext(),d,Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // Toast.makeText(getApplicationContext(),Lat+" "+Long,Toast.LENGTH_SHORT).show();



                }
            }
        };

       // fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();


    }


    private void createLocationRequest(){

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},101);


            return;
        }


        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null);


    }





}

