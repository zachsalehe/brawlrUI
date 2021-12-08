package com.example.test2.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test2.R;

/**
 * this is responsible for the main activity of our app, that is the one that you see when you
 * open the app for the first time. Although it has database interactions you can see the design
 * document for as to why we made it this way
 */
public class MainActivityScreen extends AppCompatActivity {

    private Button mLogin, mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mLogin = (Button) findViewById(R.id.signIn);
        mRegister = (Button) findViewById(R.id.createAccount);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityScreen.this, SignInScreen.class);
                startActivity(intent);
                finish();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityScreen.this, CreateAccountScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void createAccountScreen(View view){
        Intent intent = new Intent(this, CreateAccountScreen.class);
        startActivity(intent);
    }
    public void signInScreen(View view) {

        Intent intent = new Intent(this, SignInScreen.class);
        startActivity(intent);
    }

}