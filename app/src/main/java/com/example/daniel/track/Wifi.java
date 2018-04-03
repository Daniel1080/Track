package com.example.daniel.track;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;

/**
 * Created by Daniel on 13/02/2018.
 */

public class Wifi extends User {


    public static String APurl = "https://api.wigle.net/api/v2/network/detail";
    public Boolean success = false;
    WifiManager wifiMan;
    AsyncHttpClient client = new AsyncHttpClient();
    public HashMap APhm = new HashMap();

    public HashMap getAPhm() {




        return APhm;
    }

    public void GetWireless1(Context cont) {

        Boolean Processed = false;
        wifiMan = (WifiManager) cont.getSystemService(cont.WIFI_SERVICE);

        AsyncGetWifiNet a = new AsyncGetWifiNet();
        a.execute();
    }


    private class AsyncGetWifiNet extends AsyncTask<HashMap, Boolean, String> {


        String Resp = "";


        @Override
        protected String doInBackground(HashMap... hashMaps) {

            Date time = Calendar.getInstance().getTime();


            wifiMan.setWifiEnabled(true);
            wifiMan.startScan();

            //Setup comparator to sort wireless results by signal strength.
            Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult lhs, ScanResult rhs) {
                    return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
                }
            };

            //Get Wifi scan results from Wifi man
            List<ScanResult> wifiLis = wifiMan.getScanResults();
            //Sort Wifi scan results by comparator.
            Collections.sort(wifiLis, comparator);
            if (wifiLis.isEmpty()) {
                Log.d("WIFI", "No Wireless networks found!");
            }

            APhm.put("AP1", wifiLis.get(0).SSID);
            APhm.put("AP1TIME", time);
            APhm.put("AP1BSSID", wifiLis.get(0).BSSID.toString());
            APhm.put("AP1LEVEL", wifiLis.get(0).level);
            APhm.put("AP1LON", "");
            APhm.put("AP1LAT", "");
            APhm.put("AP2", wifiLis.get(1).SSID);
            APhm.put("AP2TIME", time);
            APhm.put("AP2BSSID", wifiLis.get(1).BSSID.toString());
            APhm.put("AP2LEVEL", wifiLis.get(1).level);
            APhm.put("AP2LON", "");
            APhm.put("AP2LAT", "");
            APhm.put("AP3", wifiLis.get(2).SSID);
            APhm.put("AP3TIME", time);
            APhm.put("AP3BSSID", wifiLis.get(2).BSSID.toString());
            APhm.put("AP3LEVEL", wifiLis.get(2).level);
            APhm.put("AP3LON", "");
            APhm.put("AP3LAT", "");

            System.out.println("This is the first network :::: " + wifiLis.get(0).SSID.toString());
            System.out.println(APhm.get("AP1BSSID") + " " + APhm.get("AP2BSSID") + " " + APhm.get("AP3BSSID"));

            if (APhm.isEmpty()) {
                Resp = "FAIL";
                Log.d("ASYNC", "Method " + Resp);
            } else if (APhm.isEmpty() == false) {
                Resp = "SUCCESS";
                Log.d("ASYNC", "Method " + Resp);
            }


            return Resp;
        }

        @Override
        protected void onPostExecute(String Resp) {

            if (Resp == "SUCCESS") {
                Log.i("WIFI", "Lookup Success!");
                success = true;
                //GetLonLat(APhm);


            } else if (Resp == "FAILURE") {
                Log.d("FAILED", "WIFI man failure!");
                success = false;


            }
            GetLonLat g = new GetLonLat();
            g.run();

        }


        public class GetLonLat implements Runnable {
            int APcount = 1 ;

            public void run(){Log.i("GetLonLat", "EXECUTED");

             android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);

                while(APcount <= 3){

                    if(APcount == 1){GetWigle(APcount, APhm);}
                    else if(APcount == 2){GetWigle(APcount, APhm);}
                    else if(APcount == 3){GetWigle(APcount, APhm);}

                    APcount+=1;
                }
            }







        }



        public void ProcessLonLat(HashMap APhm){
            Log.d("ProLonLat", "EXECUTED!");
        }
        public void GetWigle(final int APcount, final HashMap APhm){

            Log.i("GET", "Executing Get Req.");


            RequestParams params = new RequestParams();


            client.setBasicAuth("AID5de8eada7ff8fd72337913007b346e1f", "b9da51d6f0621b4dd95fa517da6a932d", new AuthScope(AuthScope.ANY_REALM, AuthScope.ANY_PORT));

            switch(APcount){
                case 1:
                    params.put("netid", APhm.get("AP1BSSID"));
                    break;
                case 2:
                    params.put("netid", APhm.get("AP2BSSID"));
                    break;
                case 3:
                    params.put("netid", APhm.get("AP3BSSID"));
                    break;
            }

            client.get(APurl, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("Track", "Great Success" + response.toString());

                    try {
                        if (response.get("success").toString() == "false") {

                            //Switch to work out which AP Lon Lat to update.

                            switch (APcount){
                                case 1:

                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                            }

                        }
                        if (response.get("success").toString() == "true") {

                            switch (APcount){
                                case 1:
                                    Log.d( "CURRENT :::", response.getJSONArray("results").toString());
                                    JSONArray jsAP1 = response.getJSONArray("results");
                                    System.out.println("This should be the trilat" + jsAP1.getJSONObject(0).getString("trilong").toString());

                                    APhm.put("AP1LON", jsAP1.getJSONObject(0).getString("trilong"));
                                    APhm.put("AP1LAT", jsAP1.getJSONObject(0).getString("trilat"));

                                    break;
                                case 2:
                                    Log.d( "CURRENT :::", response.getJSONArray("results").toString());
                                    JSONArray jsAP2 = response.getJSONArray("results");

                                    APhm.put("AP2LON", jsAP2.getJSONObject(0).getString("trilong"));
                                    APhm.put("AP2LAT", jsAP2.getJSONObject(0).getString("trilat"));
                                    break;
                                case 3:
                                    Log.d( "CURRENT :::", response.getJSONArray("results").toString());
                                    JSONArray jsAP3 = response.getJSONArray("results");

                                    APhm.put("AP3LON", jsAP3.getJSONObject(0).getString("trilong"));
                                    APhm.put("AP3LAT", jsAP3.getJSONObject(0).getString("trilat"));
                                    break;
                            }


                            if(APcount == 3){        }



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    Log.d("Track", "Fail" + e.toString());
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                }

            });

        }
    }


    }






















