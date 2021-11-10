package com.example.test2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        RelativeLayout layout;
        layout = findViewById(R.id.relativeLayout);
        layout.setOnTouchListener(new OnSwipeTouchListener(HomeScreen.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Toast.makeText(HomeScreen.this, "Swipe Left gesture detected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Toast.makeText(HomeScreen.this, "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }

    public void messagesScreen(View view){
        Intent intent = new Intent(this, MessagesScreen.class);
        startActivity(intent);
    }
}
