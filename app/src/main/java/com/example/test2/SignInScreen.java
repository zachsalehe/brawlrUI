package com.example.test2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in_screen);
    }
    public void backToMain(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void goToHome(View view){
        EditText usernameInput = (EditText) findViewById(R.id.username);
        EditText passwordInput = (EditText) findViewById(R.id.password);
        String username = usernameInput.getText().toString();
        System.out.println(username);
        String password = passwordInput.getText().toString();
        User user = DatabaseManager.getUser("asdf");
        System.out.println("---------------------------jjjjjjj----------------------------------");
        System.out.println(user);
//        if(user == null){
//            Context context = getApplicationContext();
//            CharSequence text = "username or password does not match";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }
//        else {
//
//        }
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
}
