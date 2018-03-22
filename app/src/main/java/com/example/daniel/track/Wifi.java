package com.example.daniel.track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

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

/**
 * Created by Daniel on 13/02/2018.
 */

public class Wifi extends User {



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
        APhm.put("AP1BSSID", wifiLis.get(0).BSSID);
        APhm.put("AP1LEVEL", wifiLis.get(0).level );
        APhm.put("AP2", wifiLis.get(1).SSID);
        APhm.put("AP2TIME" , time);
        APhm.put("AP2BSSID", wifiLis.get(1).BSSID);
        APhm.put("AP2LEVEL", wifiLis.get(1).level);
        APhm.put("AP3", wifiLis.get(2).SSID);
        APhm.put("AP3TIME" , time);
        APhm.put("AP3BSSID", wifiLis.get(2).BSSID);
        APhm.put("AP3LEVEL", wifiLis.get(2).level);


        System.out.println("This is the first network :::: " + wifiLis.get(0).SSID.toString());
        System.out.println(APhm.get("AP1BSSID") + " " + APhm.get("AP2BSSID")  + " " + APhm.get("AP3BSSID"));

    }


//    public HashMap GetApLocations2(HashMap APs){
//
//
//
//
//    }




}
