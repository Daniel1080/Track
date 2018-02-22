package com.example.daniel.track;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Daniel on 13/02/2018.
 */

public class User implements reg_user {

    int UserID;
    private int UserCount =0;
    String user;
    String name;
    String email;
    byte [] pass;

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
        UsrJS.put("req", 1);
        UsrJS.put("userid", us.getUserID());
        UsrJS.put("user",us.getUser());
        UsrJS.put("name", us.getName());
        UsrJS.put("email", us.getEmail());
        UsrJS.put("password", us.getPass());
            System.out.println("Written to JSON!");
        }
        catch (JSONException e){
        System.out.println("Exception Occurred on writing user to JSON");
        }
        //URL url = new URL(BackendURL, 3000, UsrJS);
        //HttpsURLConnection Connection = (HttpsURLConnection)


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



}
