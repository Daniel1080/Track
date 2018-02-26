package com.example.daniel.track;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity{

    String User = "";
    String Pass = "";
    String Email = "";
    String Name = "";

    TextView txtUser;
    TextView txtPass;
    TextView txtEmail;
    TextView txtName;

    Button btnReg;
    Button btnBack;

    reg_user r = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();

        txtUser = (EditText) findViewById(R.id.txtUserR);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtName = (EditText) findViewById(R.id.txtName);

        btnReg = (Button) findViewById(R.id.btnRegister);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean InputsOk = false;

                User = txtUser.getText().toString();
                Pass = txtPass.getText().toString();
                Email = txtEmail.getText().toString();
                Name = txtName.getText().toString();

                InputsOk = chkInputs(User, Pass, Email, Name);

                if(InputsOk){System.out.println("Inputs good");
                r.CallReg(User, Name, Email, Pass);}
                else{System.out.println("Inputs not good");}

            }
        });
    }

    private Boolean chkInputs(String User, String Pass, String Email, String Name){
        boolean inputsOk = false;

        String[] inputs = {User, Pass, Email, Name};
        int length = inputs.length;
        int x = 0;

        while(x < length){
            if(inputs[x].length() == 0){return inputsOk;}
            else if (inputs[x].length() > 0){inputsOk = true;}
            x+=1;
        }
        if(inputs[2].contains("@")){inputsOk = true;}
        else if(inputs[1].length() < 8){return  inputsOk = false;}


        return inputsOk;
        }



    }









