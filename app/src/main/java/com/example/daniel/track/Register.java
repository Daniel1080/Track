package com.example.daniel.track;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    String User = "";
    String Pass = "";
    String Email = "";
    String Name = "";

    EditText txtUser = (EditText) findViewById(R.id.txtUserR);
    EditText txtPass = (EditText) findViewById(R.id.txtPass);
    EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
    EditText txtName = (EditText) findViewById(R.id.txtName);

    Button btnReg = (Button) findViewById(R.id.btnRegister);
    Button btnBack = (Button) findViewById(R.id.btnBack);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User = txtUser.toString();
                Pass = txtPass.toString();
                Email = txtEmail.toString();
                Name = txtName.toString();
            }
        });
    }
    private Boolean chkInputs(String User, String Pass, String Email, String Name){
        boolean inputsOk = false;

        String[] inputs = {User, Pass, Email, Name};
        int length = inputs.length;
        int x = 0;

        while(x <= length){
            if(inputs[x].length() == 0){return inputsOk;}
            else if (inputs[x].length() > 0){inputsOk = true;}
        }
        if(inputs[2].contains("@")){inputsOk = true;}
        else if(inputs[1].length() < 8){return  inputsOk = false;}


        return inputsOk;
        }

    }








}
