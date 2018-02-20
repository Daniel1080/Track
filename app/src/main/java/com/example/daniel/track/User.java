package com.example.daniel.track;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Daniel on 13/02/2018.
 */

public class User implements reg_user {

    int UserID;
    private int UserCount =0;
    String user;
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

    private String getEmail() {
        return email;
    }

    private byte [] getPass() {
        return pass;
    }

    public User(){}

     public User(String user, String email, byte [] pass) {
        UserID = UserCount +1;
        this.user = user;
        this.email = email;
        this.pass = pass;
    }

    public boolean RegUsr(String User, String email, String pass){


        Boolean Done = false;
        String befHash = pass;
        byte [] afHash = null;
        pass = null;

        afHash = HashPass(befHash);

        User us1 = new User(User,email, afHash);
        AddUsertoDB(us1);
        us1 = null;
        Done = true;
        return  Done;
    }
    private boolean Authenticate(){
        boolean authorised;

        return authorised = true;
    }
    private void AddUsertoDB(User us){

        JSONObject UsrJS = new JSONObject();

        String usr_js = us.getUser();

        try{
        UsrJS.put("userID", us.getUserID());
        UsrJS.put("user",us.getUser());
        UsrJS.put("email", us.getEmail());
        UsrJS.put("password", us.getPass());

        }
        catch (JSONException e){
        System.out.println("Exception Occurred on writing user to JSON");
        }


    }
    private byte [] HashPass(String befHash){
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

}
