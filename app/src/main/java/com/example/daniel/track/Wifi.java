package com.example.daniel.track;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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


    public boolean GetWireless1(Context cont)
    {

        Boolean Processed = false;
        wifiMan = (WifiManager) cont.getSystemService(cont.WIFI_SERVICE);

        AsyncGetWifiNet a = new AsyncGetWifiNet();

        a.execute();

        return Processed;
    }

    private class AsyncGetWifiNet extends AsyncTask<HashMap, Boolean, String> {

        HashMap APhm = new HashMap();
        String Resp = "";
        AsyncHttpClient client = new AsyncHttpClient();


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
            List <ScanResult> wifiLis = wifiMan.getScanResults();
            //Sort Wifi scan results by comparator.
            Collections.sort(wifiLis, comparator);
            if(wifiLis.isEmpty()){Log.d("WIFI", "No Wireless networks found!");}

            APhm.put("AP1", wifiLis.get(0).SSID);
            APhm.put("AP1TIME" , time);
            APhm.put("AP1BSSID", wifiLis.get(0).BSSID.toString());
            APhm.put("AP1LEVEL", wifiLis.get(0).level );
            APhm.put("AP1LON" , "");
            APhm.put("AP1LAT", "");
            APhm.put("AP2", wifiLis.get(1).SSID);
            APhm.put("AP2TIME" , time);
            APhm.put("AP2BSSID", wifiLis.get(1).BSSID.toString().replace(":", "%"));
            APhm.put("AP2LEVEL", wifiLis.get(1).level);
            APhm.put("AP2LON" , "");
            APhm.put("AP2LAT", "");
            APhm.put("AP3", wifiLis.get(2).SSID);
            APhm.put("AP3TIME" , time);
            APhm.put("AP3BSSID", wifiLis.get(2).BSSID.toString().replace(":", "%"));
            APhm.put("AP3LEVEL", wifiLis.get(2).level);
            APhm.put("AP3LON" , "");
            APhm.put("AP3LAT", "");

            System.out.println("This is the first network :::: " + wifiLis.get(0).SSID.toString());
            System.out.println(APhm.get("AP1BSSID") + " " + APhm.get("AP2BSSID")  + " " + APhm.get("AP3BSSID"));

            if(APhm.isEmpty()){Resp = "FAIL";
            Log.d("ASYNC", "Method " + Resp);
            }
            else if (APhm.isEmpty() == false){Resp = "SUCCESS";
            Log.d("ASYNC", "Method " + Resp);
            }


            return Resp;
        }

        @Override
        protected void onPostExecute(String Resp) {

            Log.i("BSSID", "This is the mac to be looked up" + APhm.get("AP1BSSID"));
            RequestParams params = new RequestParams();
            params.put("netid" , APhm.get("AP1BSSID"));

            if(Resp.equals("SUCCESS")){
                Log.i("GET", "Executing Get Req.");
                client.setBasicAuth("AID5de8eada7ff8fd72337913007b346e1f", "b9da51d6f0621b4dd95fa517da6a932d", new AuthScope(AuthScope.ANY_REALM, AuthScope.ANY_PORT));
                client.get(APurl, params, new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        Log.d("Track", "Great Success" + response.toString());


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                        Log.d("Track", "Fail" + e.toString());
                    }



                });





            }




        }


    }








}



















