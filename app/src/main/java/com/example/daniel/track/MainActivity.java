package com.example.daniel.track;

import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    TextView txtUser;
    TextView txtPass;
    Boolean Logged = false;
    login l = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User UserObj = new User();
        Intent intent = getIntent();
        btnLogin = (Button) findViewById(R.id.btnLoginL);
        btnRegister = (Button) findViewById(R.id.btnRegisterL);
        txtPass = (TextView) findViewById(R.id.txtPassL);
        txtUser = (TextView) findViewById(R.id.txtUserL);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("OnClick", "Login on click executing.");
                Logged = Login();
                if (Logged == true){Toast.makeText(getApplicationContext(), "User Authenticated",Toast.LENGTH_LONG).show();}
            }
        });
    }
    public void switchRegister(View view){
        Intent intent= new Intent(this, Register.class);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        v.vibrate(450);
        startActivity(intent);
    }
    public Boolean Login(){
        Boolean Authed = false;

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


        Authed = l.LoginUser(Usr, Pass);
        Log.d("Value of Auth aflogin" , "Auth is " + Authed);
        return Authed;
    }








}



