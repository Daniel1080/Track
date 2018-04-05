package com.example.daniel.track;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.daniel.track.R.id.mapView;

public class MapsActiv extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Switch togTrk;
    ConnectivityManager conMgr;
    ListView LocList;
    Wifi w = new Wifi();
    HashMap APS = new HashMap();
    ArrayAdapter<String> ListAdap;
    List<String> AP_List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        togTrk = (Switch) findViewById(R.id.swTrk);
        LocList = (ListView) findViewById(R.id.LocList);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        AP_List = new ArrayList<String>(Arrays.asList(""));
        ListAdap = new ArrayAdapter<String>(this, R.layout.customlistlayout, AP_List);
        LocList.setAdapter(ListAdap);


        togTrk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    toggleTracking();
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng Sunderland = new LatLng(54.90465, -1.38222);
        googleMap.addMarker(new MarkerOptions().position(Sunderland).title("Sun"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(Sunderland));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Sunderland, 15));
    }

    public void toggleTracking() {

        Log.d("Running tracking check!", "toggle Tracking function called. ");
        if (togTrk.isChecked() == true) {
            Toast.makeText(this, "Tracking toggled!", Toast.LENGTH_SHORT).show();
        }
        ;
        NetworkInfo netInf = conMgr.getActiveNetworkInfo();
        Log.d("NetConn", "con mgr started " + netInf);
        Log.d("Current Net", netInf.getState().toString());
        String conStat = netInf.getState().toString();
        System.out.println(conStat);

        if (conStat == "CONNECTED") {
            Log.i("NetStatus", "Device has network access!");
            Toast.makeText(this, "Connected!", Toast.LENGTH_LONG);
            Context WCont = this;
            w.GetWireless1(WCont);
            UpdateLocList Up = new UpdateLocList();
            Up.run();
        } else {
            Toast.makeText(this, "Check Internet Connectivity!", Toast.LENGTH_LONG);
        }

    }

    public class UpdateLocList implements Runnable {

        @Override
        public void run() {

            float zIndex = 0;
            APS = w.getAPhm();
            Log.i("APS", "THIS IS" + APS.toString());
            Log.i("APS", "THIS IS LON" + APS.get("AP1LON"));
            Log.i("APS", "THIS IS LAT" + APS.get("AP1LAT"));

            if (APS.get("AP1LON") != null && APS.get("AP1LAT") != null) {

                try {

                    double AP1lat = Double.parseDouble(APS.get("AP1LAT").toString());
                    double AP1lon = Double.parseDouble(APS.get("AP1LON").toString());
                    Log.i("MAP", "COORDS 1 " + AP1lon + " " + AP1lat);

                    double Sig1 = Double.parseDouble(APS.get("AP1LEVEL").toString());
                    double Freq1 = Double.parseDouble(APS.get("AP1FREQ").toString());
                    double Rad1 = calculateDistance(Sig1, Freq1);
                    String sRad1 = String.valueOf(Rad1).substring(0,4);

                    AP_List.add(APS.get("AP1").toString() + " " + APS.get("AP1TIME").toString().substring(0,20) + " User distance from:" + sRad1 + "m");
                    ListAdap.notifyDataSetChanged();



                    LatLng AP1 = new LatLng(AP1lat, AP1lon);
                    mMap.addMarker(new MarkerOptions().position(AP1).title(APS.get("AP1").toString() + " " + APS.get("AP1TIME"))).setZIndex(zIndex);

                    Circle cir1 = mMap.addCircle(new CircleOptions().center(AP1).radius(Rad1).strokeColor(Color.RED).fillColor(Color.GREEN).zIndex(zIndex));

                    zIndex += zIndex +1;

                }catch (NumberFormatException e){
                    Log.e("NEexcep", "Marker 1");
                    }

            }

            if (APS.get("AP2LON") != null ) {

                try{
                double AP2lat = Double.parseDouble(APS.get("AP2LAT").toString());
                double AP2lon = Double.parseDouble(APS.get("AP2LON").toString());
                Log.i("MAP", "COORDS 2 " + AP2lon + " " + AP2lat);

                LatLng AP2 = new LatLng(AP2lat, AP2lon);
                double Sig2 = Double.parseDouble(APS.get("AP2LEVEL").toString());
                double Freq2 = Double.parseDouble(APS.get("AP2FREQ").toString());

                double Rad2 = calculateDistance(Sig2, Freq2);
                String sRad2 = String.valueOf(Rad2).substring(0,4);

                AP_List.add(APS.get("AP2").toString() + " " + APS.get("AP2TIME").toString().substring(0,20)+ " User distance from:" + sRad2 + "m");
                ListAdap.notifyDataSetChanged();

                mMap.addMarker(new MarkerOptions().position(AP2).title(APS.get("AP2").toString() + " " + APS.get("AP2TIME"))).setZIndex(zIndex);
                Circle cir2 = mMap.addCircle(new CircleOptions().center(AP2).radius(Rad2).strokeColor(Color.RED).fillColor(Color.BLUE).zIndex(zIndex));

                }catch (NumberFormatException e){
                    Log.e("NEexcep", "Marker 2");
                    }
          }

            if (APS.get("AP3LON") != null) {

                try{
                double AP3lat = Double.parseDouble(APS.get("AP3LAT").toString());
                double AP3lon = Double.parseDouble(APS.get("AP3LON").toString());
                Log.i("MAP", "COORDS 3 " + AP3lon + " " + AP3lat);

                LatLng AP3 = new LatLng(AP3lat, AP3lon);


                double Sig3 = Double.parseDouble(APS.get("AP3LEVEL").toString());
                double Freq3 = Double.parseDouble(APS.get("AP3FREQ").toString());

                double Rad3 = calculateDistance(Sig3, Freq3);
                String sRad3 = String.valueOf(Rad3).substring(0,4);

                    AP_List.add(APS.get("AP3").toString() + " " + APS.get("AP3TIME").toString().substring(0,20)+ " User distance from:" + sRad3 + "m");
                    ListAdap.notifyDataSetChanged();

                    mMap.addMarker(new MarkerOptions().position(AP3).title(APS.get("AP3").toString() + " " + APS.get("AP3TIME"))).setZIndex(zIndex);
                Circle cir3 = mMap.addCircle(new CircleOptions().center(AP3).radius(Rad3).strokeColor(Color.RED).fillColor(Color.YELLOW).zIndex(zIndex));}
                catch (NumberFormatException e){
                    Log.e("NEexcep", "Marker 3");
                    }


            }

        }
            //https://stackoverflow.com/questions/11217674/how-to-calculate-distance-from-wifi-router-using-signal-strength
        public double calculateDistance(double signalLevelInDb, double freqInMHz) {
            double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
            return Math.pow(10.0, exp);
        }


    }

}
