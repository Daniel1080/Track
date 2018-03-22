package com.example.daniel.track;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.daniel.track.R.id.mapView;

public class MapsActiv extends FragmentActivity implements OnMapReadyCallback{

    GoogleMap mMap;
    Switch togTrk;
    ConnectivityManager conMgr;
    Wifi w = new Wifi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        togTrk = (Switch) findViewById(R.id.swTrk);
        conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        togTrk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){toggleTracking();}


            }
        });




    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Down Under"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    public void toggleTracking(){

    Log.d("Running tracking check!", "toggle Tracking function called. ");
    if(togTrk.isChecked() == true){
        Toast.makeText(this, "Tracking toggled!", Toast.LENGTH_SHORT).show();};
        NetworkInfo netInf = conMgr.getActiveNetworkInfo();
        Log.d("NetConn", "con mgr started" + netInf);
        Log.d("Current Net", netInf.getState().toString());
        String conStat = netInf.getState().toString();
        System.out.println(conStat);

        if(conStat == "CONNECTED"){Log.i("NetStatus", "Device has network access!");
        Toast.makeText(this, "Connected!", Toast.LENGTH_LONG);
        Context WCont = this;
        w.GetWirelessData1(WCont);

        }
        else{Toast.makeText(this , "Check Internet Connectivity!", Toast.LENGTH_LONG);}







    }
}
