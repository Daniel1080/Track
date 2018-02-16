package com.example.daniel.track;

/**
 * Created by Daniel on 13/02/2018.
 */

public class User implements reg_user {

    int UserID;
    private int UserCount =0;
    String user;
    String email;
    String pass;

    public User(){}

     public User(String user, String email, String pass) {
        UserID = UserCount +1;
        this.user = user;
        this.email = email;
        this.pass = pass;
    }

    public boolean RegUsr(String User, String email, String pass){
        Boolean Done = false;
        User us1 = new User(User,email,pass);
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




    }
}
