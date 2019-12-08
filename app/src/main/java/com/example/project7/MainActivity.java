package com.example.project7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText emailField;
    EditText passField;
    Button loginButton;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mauthStateListner;
    //****Autharization varialble****


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Authorization
        mAuth = FirebaseAuth.getInstance();
        emailField = (EditText) findViewById(R.id.emailField);
        passField = (EditText) findViewById(R.id.passField);
        loginButton = (Button) findViewById(R.id.button);
        mauthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Authorization*******


    }



    void signIn(){


        // Firebase for registration
        String email = emailField.getText().toString();
        final String pass = passField.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(MainActivity.this, "empty fields", Toast.LENGTH_LONG).show();

        }
        else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, task.getException().getMessage()+" "+pass, Toast.LENGTH_LONG).show();
                    }

                }
            });


        }
    }
   @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mauthStateListner);
    }

    public void register(View view) {

        Intent intent= new Intent(MainActivity.this,RegistrationActivity.class);
        startActivity(intent);


    }
}
