package com.example.project7;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {





    EditText email;
    EditText pass;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressDialog = new ProgressDialog(this);
        email = findViewById(R.id.emailRegistrationTV);
        pass = findViewById(R.id.passwordRegistrationTV);

        firebaseAuth = FirebaseAuth.getInstance();


    }

    // Firebase is used to register the user.

    public void registerUser(View view) {

        String e = email.getText().toString().trim();
        String p = pass.getText().toString().trim();

        if(TextUtils.isEmpty(e)){
            Toast.makeText(this,"please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(p)){
            Toast.makeText(this,"please enter password",Toast.LENGTH_LONG).show();
            return;

        }

        progressDialog.setMessage("registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "user registered", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(RegistrationActivity.this,AccountActivity.class);
                    //startActivity(intent);


                }
                else {
                    Toast.makeText(RegistrationActivity.this, "user not registered", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    public void login(View view) {
        Intent intent = new Intent (RegistrationActivity.this,MainActivity.class);
        startActivity(intent);


    }





}
