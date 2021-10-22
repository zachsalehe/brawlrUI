package com.example.test2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class createAccountScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.create_account);
    }
}
