package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class signInScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
    }
    public void backToMain(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
