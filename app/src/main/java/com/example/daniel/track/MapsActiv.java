package com.example.daniel.track;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.ServiceTestCase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
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
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.analysis.function.Constant;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;

import static com.example.daniel.track.R.id.mapView;


public class MapsActiv extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Switch togTrk;
    TextView txtTrkUser;
    ConnectivityManager conMgr;
    ListView LocList;
    Wifi w;
    HashMap APS = new HashMap();
    ArrayAdapter<String> ListAdap;
    List<String> AP_List;
    String User;
    WifiService WS;
    boolean isBound = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        User = intent.getStringExtra("User");
        togTrk = (Switch) findViewById(R.id.swTrk);
        txtTrkUser = (TextView) findViewById(R.id.txtTrkUsr);
        txtTrkUser.setText(User);
        LocList = (ListView) findViewById(R.id.LocList);
        w = new Wifi(this);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        AP_List = new ArrayList<String>(Arrays.asList(""));
        ListAdap = new ArrayAdapter<String>(this, R.layout.customlistlayout, AP_List);
        LocList.setAdapter(ListAdap);
        Respond respRec = new Respond();
        IntentFilter statusFilt = new IntentFilter(Intent.ACTION_SEND);




        LocalBroadcastManager.getInstance(this).registerReceiver(respRec, statusFilt);

        Intent i = new Intent(this, WifiService.class);
        bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);



        togTrk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    toggleTracking();
                }
                else if(isChecked == false){ }

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

        NetworkInfo netInf = conMgr.getActiveNetworkInfo();
        Log.d("NetConn", "con mgr started " + netInf);
        Log.d("Current Net", netInf.getState().toString());
        String conStat = netInf.getState().toString();
        System.out.println(conStat);

        if (conStat == "CONNECTED") {
            Log.i("NetStatus", "Device has network access!");
            Toast.makeText(this, "Connected!", Toast.LENGTH_LONG);
            Context WCont = this;



            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

            Calendar cal = Calendar.getInstance();


            Intent MapstoService = new Intent(MapsActiv.this, WifiService.class);
            PendingIntent pendINT = PendingIntent.getService(this, 0, MapstoService, 0);

            long currTime = System.currentTimeMillis();
            long tenMins =  30 * 1000;

            am.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), tenMins, pendINT);
            Log.d("ALARM ", am.getNextAlarmClock().toString());

            //w.GetWireless1(WCont);
            //ExecuteULL();
           // WS.WifiEXECUTE(WCont);

        } else {
            Toast.makeText(this, "Check Internet Connectivity!", Toast.LENGTH_LONG);
        }

    }

    public void ExecuteULL(){

        UpdateLocList Up = new UpdateLocList();
        Up.run();

    }

    public class UpdateLocList implements Runnable {

        @Override
        public void run() {

            float zIndex = 0;

            Log.i("APS", "THIS IS" + APS.toString());
            Log.i("APS", "THIS IS LON" + APS.get("AP1LON"));
            Log.i("APS", "THIS IS LAT" + APS.get("AP1LAT"));

            LatLng AP1 = null;
            LatLng AP2 = null;
            LatLng AP3 = null;
            double Rad1 = 0;
            double Rad2 = 0;
            double Rad3 = 0;

            if (APS.get("AP1LON") != null && APS.get("AP1LAT") != null) {

                try {

                    double AP1lat = Double.parseDouble(APS.get("AP1LAT").toString());
                    double AP1lon = Double.parseDouble(APS.get("AP1LON").toString());
                    Log.i("MAP", "COORDS 1 " + AP1lon + " " + AP1lat);

                    double Sig1 = Double.parseDouble(APS.get("AP1LEVEL").toString());
                    double Freq1 = Double.parseDouble(APS.get("AP1FREQ").toString());
                    Rad1 = calculateDistance(Sig1, Freq1);
                    String sRad1 = String.valueOf(Rad1).substring(0,4);

                    AP_List.add(APS.get("AP1").toString() + " " + APS.get("AP1TIME").toString().substring(0,20) + " User distance from: " + sRad1 + "m");
                    ListAdap.notifyDataSetChanged();



                    AP1 = new LatLng(AP1lat, AP1lon);
                    mMap.addMarker(new MarkerOptions().position(AP1).title(APS.get("AP1").toString() + " " + APS.get("AP1TIME"))).setZIndex(zIndex);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AP1, 15));

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

                 AP2 = new LatLng(AP2lat, AP2lon);
                double Sig2 = Double.parseDouble(APS.get("AP2LEVEL").toString());
                double Freq2 = Double.parseDouble(APS.get("AP2FREQ").toString());

                 Rad2 = calculateDistance(Sig2, Freq2);
                String sRad2 = String.valueOf(Rad2).substring(0,4);

                AP_List.add(APS.get("AP2").toString() + " " + APS.get("AP2TIME").toString().substring(0,20)+ " User distance from: " + sRad2 + "m");
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

                AP3 = new LatLng(AP3lat, AP3lon);


                double Sig3 = Double.parseDouble(APS.get("AP3LEVEL").toString());
                double Freq3 = Double.parseDouble(APS.get("AP3FREQ").toString());

                Rad3 = calculateDistance(Sig3, Freq3);
                String sRad3 = String.valueOf(Rad3).substring(0,4);

                    AP_List.add(APS.get("AP3").toString() + " " + APS.get("AP3TIME").toString().substring(0,20)+ " User distance from: " + sRad3 + "m");
                    ListAdap.notifyDataSetChanged();

                    mMap.addMarker(new MarkerOptions().position(AP3).title(APS.get("AP3").toString() + " " + APS.get("AP3TIME"))).setZIndex(zIndex);
                Circle cir3 = mMap.addCircle(new CircleOptions().center(AP3).radius(Rad3).strokeColor(Color.RED).fillColor(Color.YELLOW).zIndex(zIndex));}
                catch (NumberFormatException e){
                    Log.e("NEexcep", "Marker 3");
                    }
            }

            try{

                double [] [] positions = new double[][] {{AP1.latitude, AP1.longitude}, {AP2.latitude, AP2.longitude}, {AP3.latitude, AP3.longitude}};
                double [] distances = new double[] {Rad1, Rad2, Rad3};

                NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
                LeastSquaresOptimizer.Optimum optimum = solver.solve();
                double [] centroid = optimum.getPoint().toArray();

                Log.i("USER", "Assumed user loc is " + centroid[0] + " " + centroid[1] );

                LatLng User = new LatLng(centroid[0], centroid[1]);

                mMap.addMarker(new MarkerOptions().position(User).title("User"));




            }catch (NumberFormatException e){Log.e("EXCEP", e.toString()); }

        }
    }
            //https://stackoverflow.com/questions/11217674/how-to-calculate-distance-from-wifi-router-using-signal-strength
        public double calculateDistance(double signalLevelInDb, double freqInMHz) {
            double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
            return Math.pow(10.0, exp);
        }

        private ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                WifiService.MyLocalBinder localbinder = (WifiService.MyLocalBinder) service;
                WS = localbinder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBound = false;
            }
        };

    private class Respond extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("onRec", "On rec called!");
            APS = (HashMap) intent.getSerializableExtra("APS");
            Log.d("Onrec", "Rec APS "  + APS.toString());


            ExecuteULL();
        }
    }





}
