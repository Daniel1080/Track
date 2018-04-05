package com.example.daniel.track;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    TextView txtUser;
    TextView txtPass;
    Boolean Logged = false;
    Boolean Authed = false;
    Boolean Authentication =  false;
    User UserObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        btnLogin = (Button) findViewById(R.id.btnLoginL);
        btnRegister = (Button) findViewById(R.id.btnRegisterL);
        txtPass = (TextView) findViewById(R.id.txtPassL);
        txtUser = (TextView) findViewById(R.id.txtUserL);
        UserObj = new User(this);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("OnClick", "Login on click executing.");
                Login();

            }
        });
    }
    public void switchRegister(View view){
        Intent intent= new Intent(this, Register.class);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(450);
        startActivity(intent);
    }
    public  void switchMap(int view){

        Intent mapInt = new Intent(this, MapsActiv.class);

        Vibrator v2 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v2.vibrate(500);
        startActivity(mapInt);

    }
    public void Login(){

        Boolean usOK = false;
        Boolean passOK = false;

        String Usr = txtUser.getText().toString();
        String Pass = txtPass.getText().toString();

        System.out.println("Login Method");
        Log.i("Usr", Usr.toString());
        Log.i("Pass", Pass.toString());
        System.out.println(txtPass);
        System.out.println(Usr);

        if (Usr.trim().length() < 1){
            usOK = false;
            Toast.makeText(getApplicationContext(), "Please enter a username!",Toast.LENGTH_LONG).show();
            txtPass.setText("");
            return;
        }
        else {usOK = true;}
        if(Pass.trim().length() < 1){
            passOK = false;
            Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_LONG).show();
            return;
        }else{passOK = true;}

        if(usOK & passOK == true ){

        Log.d("This is a back", "Vals are" + Usr + " " + Pass );
          UserObj.LoginUser(Usr, Pass);
           Log.d("AUTH", "VAL BACK" + Authentication);
        }
           //CheckLoginResult();
    }
    public void CheckLoginResult(){

        Authentication = UserObj.getAUTH();

        Log.i("AUTH FINAL", "Val of Authentic "  + Authentication);

        if(Authentication == true){

            switchMap(R.layout.activity_maps);
        }


    }
//    private class MainLoginBack extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... Login) {
//
//            Log.d("This is a back", "Vals are" + Login[0] + " " + Login[1] );
//            UserObj.LoginUser(Login[0], Login[1]);
//            Log.d("AUTH", "VAL BACK" + Authentication);
//            CheckLoginResult(Authentication);
//
//            return "";
//        }
//        @Override
//        protected void onPostExecute(String Result){
//
//            Log.d("AUTHmain", "Back Val " + Authentication);
//
//        }
  }












