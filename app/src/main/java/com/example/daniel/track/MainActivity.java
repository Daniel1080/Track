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
    login l = new User();
    Boolean Authed = false;
    Boolean Authentication =  false;
    User UserObj = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        btnLogin = (Button) findViewById(R.id.btnLoginL);
        btnRegister = (Button) findViewById(R.id.btnRegisterL);
        txtPass = (TextView) findViewById(R.id.txtPassL);
        txtUser = (TextView) findViewById(R.id.txtUserL);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("OnClick", "Login on click executing.");
                Login();
                if (Logged == true){Toast.makeText(getApplicationContext(), "User Authenticated",Toast.LENGTH_LONG).show();
                    switchMap(view);
                }
                else if(Logged == false){Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_LONG).show();}

            }
        });
    }
    public void switchRegister(View view){
        Intent intent= new Intent(this, Register.class);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(450);
        startActivity(intent);
    }
    public  void switchMap(View view){

        Intent mapInt = new Intent(this, MapsActiv.class);
        Vibrator v2 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v2.vibrate(500);
        startActivity(mapInt);

    }
    public void Login(){


        String Usr = txtUser.getText().toString();
        String Pass = txtPass.getText().toString();

        System.out.println("Login Method");
        Log.i("Usr", Usr.toString());
        Log.i("Pass", Pass.toString());
        System.out.println(txtPass);
        System.out.println(Usr);

        if (Usr.trim().length() < 1){
            Authed = false;
            Toast.makeText(getApplicationContext(), "Please enter a username!",Toast.LENGTH_LONG).show();
            txtPass.setText("");
        }
        if(Pass.trim().length() < 1){
            Authed = false;
            Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_LONG).show();
        }


        MainLoginBack M = new MainLoginBack();



        M.execute(Usr, Pass);

    }
    public void CheckLoginResult(Boolean Authentication){
        Log.i("CheckLoginResult", "EXECUTING" + Authed.toString());
        //Authentication = UserObj.getAuthenticated();

        Log.i("AUTH FINAL", "Val of Authentic" + Authentication);


    }
    private class MainLoginBack extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... Login) {

            Log.d("This is a back", "Vals are" + Login[0] + " " + Login[1] );
            Authentication = UserObj.LoginUser(Login[0], Login[1]);
            Log.d("AUTH", "VAL BACK" + Authentication);

            return "";
        }
        protected void onPostExecute(String Result){

            Log.d("AUTHmain", "Back Val " + Authentication);

            CheckLoginResult(Authentication);
        }
    }








}



