package com.example.daniel.track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.SSLCertificateSocketFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScope;

/**
 * Created by Daniel on 13/02/2018.
 */

public class Wifi extends User {


    public static String APurl = "https://api.wigle.net/api/v2/network/detail";


    public void GetWirelessData1(Context cont){
        WifiManager wifiMan = (WifiManager) cont.getSystemService(cont.WIFI_SERVICE);
        wifiMan.setWifiEnabled(true);
        Date time = Calendar.getInstance().getTime();
        wifiMan.startScan();


        Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
            }
        };


        List <ScanResult> wifiLis = wifiMan.getScanResults();
        Collections.sort(wifiLis, comparator);
        HashMap APhm = new HashMap();

        APhm.put("AP1", wifiLis.get(0).SSID);
        APhm.put("AP1TIME" , time);
        APhm.put("AP1BSSID", wifiLis.get(0).BSSID.toString().replace(":", "%"));
        APhm.put("AP1LEVEL", wifiLis.get(0).level );
        APhm.put("AP2", wifiLis.get(1).SSID);
        APhm.put("AP2TIME" , time);
        APhm.put("AP2BSSID", wifiLis.get(1).BSSID.toString());
        APhm.put("AP2LEVEL", wifiLis.get(1).level);
        APhm.put("AP3", wifiLis.get(2).SSID);
        APhm.put("AP3TIME" , time);
        APhm.put("AP3BSSID", wifiLis.get(2).BSSID.toString());
        APhm.put("AP3LEVEL", wifiLis.get(2).level);


        System.out.println("This is the first network :::: " + wifiLis.get(0).SSID.toString());
        System.out.println(APhm.get("AP1BSSID") + " " + APhm.get("AP2BSSID")  + " " + APhm.get("AP3BSSID"));

        GetApLocations2(APhm);

    }


    public HashMap GetApLocations2(HashMap APs){
        HashMap APloc = new HashMap();
        APloc.put("AP1lon", 0);
        APloc.put("AP1lat", 0);
        APloc.put("AP2lon", 0);
        APloc.put("AP2lat", 0);
        APloc.put("AP3lon", 0);
        APloc.put("AP3lat", 0);

        GetLocGetReq(APs, APloc);

        return APloc;

    }

    public void GetLocGetReq(HashMap APs, HashMap Aploc){

        RequestParams params = new RequestParams();


        params.put("netid", APs.get("AP1BSSID"));


        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("AID5de8eada7ff8fd72337913007b346e1f", "b9da51d6f0621b4dd95fa517da6a932d", new AuthScope(AuthScope.ANY_REALM, AuthScope.ANY_PORT));

        String CurrURL = "https://api.wigle.net/api/v2/profile/user";

        client.get(CurrURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
            Log.d("Track", "Great Success" + response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header [] headers, Throwable e, JSONObject response){
                Log.d("Track", "Fail" + e.toString());
            }
        });

    }










}



















