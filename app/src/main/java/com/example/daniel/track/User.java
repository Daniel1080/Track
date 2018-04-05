package com.example.daniel.track;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.SSLCertificateSocketFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.auth.AUTH;

/**
 * Created by Daniel on 13/02/2018.
 */

public class User implements reg_user , login{

    int UserID;
    private int UserCount =0;
    String user;
    String name;
    String email;
    String pass;
    Boolean AUTH = false;
    Boolean Authenticated = false;
    Context context;

    


    Boolean getAuthenticated() {
        return Authenticated;
    }

    public Boolean getAUTH() {
        return AUTH;
    }

    private int getUserID() {
        return UserID;
    }
    private int getUserCount() {
        return UserCount;
    }
    private String getUser() {
        return user;
    }
    public String getName() {
        return name;
    }
    private String getEmail() {
        return email;
    }
    private String getPass() {
        return pass;
    }

    public User(Context cont){
        this.context = cont;
    }
    public User(){}



     public User(String user, String Name, String email, String pass) {
        UserID = UserCount +1;
        this.user = user;
        this.name = Name;
        this.email = email;
        this.pass = pass;
    }

    public void CallReg(String User, String Name, String email, String pass){
        new BackgroundReg().execute(User, Name, email, pass);
    }
    public void LoginUser(String username, String Pass){
        AUTH = false;
        BackgroundLogin BL = new BackgroundLogin();

        String hashPass2 = null;
              hashPass2  = HashPass(Pass);
        JSONObject LoginReq = new JSONObject();

        try {
            LoginReq.put("task" , "2");
            LoginReq.put("user" , username);
            LoginReq.put("pass" , hashPass2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BL.execute(LoginReq);

        Log.i("LoginUser", "This is val of Authenticated and AUTH " + Authenticated + " " + AUTH);
    }

    public boolean RegUsr(String User, String Name, String email, String pass){

        Boolean Done = false;
        String befHash = pass;
        String afHash = null;
        pass = null;
        afHash = HashPass(befHash);
        User us1 = new User(User,Name, email, afHash);

        AddUsertoDB(us1);

        us1 = null;
        Done = true;
        if(Done){System.out.println("Completed User Registration");}
        return  Done;
    }


    private void AddUsertoDB(User us){

        JSONObject UsrJS = new JSONObject();

        String usr_js = us.getUser();

        String BackendURL = "https://35.178.101.198";

        try{
        UsrJS.put("task", 1);
        UsrJS.put("user",us.getUser());
        UsrJS.put("name", us.getName());
        UsrJS.put("email", us.getEmail());
        UsrJS.put("password", us.getPass());
            System.out.println("Written to JSON!");
        }
        catch (JSONException e){
        System.out.println("Exception Occurred on writing user to JSON");
        }

        try {
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            StringBuilder result = new StringBuilder();
            URL url = new URL("https://35.176.133.209/");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
            conn.setHostnameVerifier(hostnameVerifier);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(UsrJS.toString());
            conn.connect();

            if(conn.getResponseCode() == 200){
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine())!= null){
                     result.append(line);
                }
                System.out.println(line);
            }

            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String HashPass(String befHash){
        System.out.println("HashPass Called");
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(befHash.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for(int i=0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

       return "";

    }

    private Boolean SendLoginReq(JSONObject logreq){

        Boolean LoginOK = false;

        String dbUser = "" ;
        String dbPass = "" ;
        String reqPass = "";
        String reqUser = "";

        try {
            reqUser = logreq.getString("user");
            reqPass = logreq.getString("pass");
            Log.d("req user val" , "this is val req usrr" + reqUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            StringBuilder result = new StringBuilder();
            URL url = new URL("https://35.176.133.209/");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
            conn.setHostnameVerifier(hostnameVerifier);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(logreq.toString());
            conn.connect();

            if (conn.getResponseCode() == 200) {
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                System.out.println("This is the returned result from login " + result);
                try {
                    JSONObject ServLogRes = new JSONObject(result.toString());
                    dbUser = ServLogRes.getString("dbuser");
                    dbPass = ServLogRes.getString("dbpass");

                    System.out.println("These are the returned values as strings " + dbUser + " " + dbPass);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("req user val" , "this is val req usrr" + reqUser);
        LoginOK = CheckReturn(dbUser , dbPass , reqUser, reqPass);

        Log.i("AUTH", "Val of Login ok " + LoginOK);

        return LoginOK;
    }

    public boolean CheckReturn(String dbUser, String dbPass, String reqUser, String reqPass){
        boolean match = false;
        boolean match2 = false;
        boolean both = false;
        int passMatch1 = 0;
        int passMatch2 = 0;
        int passRes = 1;

        Log.d("req user val" , "this is val req usrr" + reqUser);
        System.out.println("dbuser is " + dbUser + " requser is " + reqUser);
        System.out.println(reqPass + " " + dbPass);

        if(Objects.equals(dbUser, reqUser)) {
            System.out.println("Username matches");
            match = true;
            Log.i("match Val" , " " + match);
        }
        else{
            Log.i("Username" , "Username does not match " + match);
            match = false;}

        passMatch1 = dbPass.compareTo(reqPass);
        passMatch2 = reqPass.compareTo(dbPass);
        Log.d("PASScheck", "These are the vals " + passMatch1 + " " + passMatch2);
        passRes = passMatch1 + passMatch2;
        Log.d("PASScheck", "Result is " + passRes);

        if (passRes == 0){

            System.out.println("Pass Hashes match");
            match2 = true;
            Log.i("match2 Val" , " " + match2);
        }

        Log.d("Match val " , "This is" + match);

        if(match == true && match2 ==true){ both = true;}
        else{both = false;}
        Log.i("BOTH " , "Val of both " + both);
        return both;
    }



    private class BackgroundReg extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            System.out.println("Back Task");
            System.out.println("Params " + params[0]);
            RegUsr(params[0], params[1], params[2], params[3]);
            return null;
        }
    }


    private class BackgroundLogin extends AsyncTask<JSONObject, Void, Void> {

        public Boolean LoginOk = false;

        @Override
        protected Void doInBackground(JSONObject... params) {
            System.out.println("1 Executing Background Login");
            LoginOk = SendLoginReq(params[0]);
            if (LoginOk == true){AUTH = true; }
            Log.i("AUTH", "Postex of Backlog " + LoginOk + " Val of AUTH " + AUTH);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {


            ((MainActivity)context).CheckLoginResult();

        }


    }

 }
