package com.example.daniel.track;

import android.net.SSLCertificateSocketFactory;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Daniel on 13/02/2018.
 */

public class User implements reg_user , login{

    int UserID;
    private int UserCount =0;
    String user;
    String name;
    String email;
    byte [] pass;
    Boolean AUTH = false;

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
    private byte [] getPass() {
        return pass;
    }

    public User(){}

     public User(String user, String Name, String email, byte [] pass) {
        UserID = UserCount +1;
        this.user = user;
        this.name = Name;
        this.email = email;
        this.pass = pass;
    }
    public void CallReg(String User, String Name, String email, String pass){
        new BackgroundReg().execute(User, Name, email, pass);
    }
    public Boolean LoginUser(String username, String Pass){
        AUTH = false;
        Boolean Authenticated = false;
        byte [] hashPass2 = null;
              hashPass2  = HashPass(Pass);
        JSONObject LoginReq = new JSONObject();

        try {
            LoginReq.put("task" , "2");
            LoginReq.put("pass" , hashPass2);
            LoginReq.put("user" , username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new BackgroundLogin().execute(LoginReq);
        if (AUTH){Authenticated = true;}
        if (AUTH = false){Authenticated = false;}
        return Authenticated;
    }

    public boolean RegUsr(String User, String Name, String email, String pass){

        Boolean Done = false;
        String befHash = pass;
        byte [] afHash = null;
        pass = null;
        afHash = HashPass(befHash);
        User us1 = new User(User,Name, email, afHash);

        AddUsertoDB(us1);

        us1 = null;
        Done = true;
        if(Done){System.out.println("Completed User Registration");}
        return  Done;
    }
    private boolean Authenticate(){
        boolean authorised;
        return authorised = true;
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
    private byte [] HashPass(String befHash){
        System.out.println("HashPass Called");
        MessageDigest md = null;
        byte [] passBytes = null;

        try{
        md = MessageDigest.getInstance("MD5");
        md.update(befHash.getBytes());
         passBytes = md.digest();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return passBytes;

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
            System.out.println("Executing Background Login");

            LoginOk = SendLoginReq(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (LoginOk == true){AUTH = true;}
        }
    }
    private Boolean SendLoginReq(JSONObject logreq){

        Boolean LoginOK = false;

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
            }
            System.out.println(result);
            if (result.toString() == "OK"){
                LoginOK =true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return LoginOK;

    }


 }
