package com.example.test2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.create_account);
    }

    public void backToMain(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toBiographical(View view) {
        // TODO: move the reading of the input to InputManager
        EditText emailInput = (EditText) findViewById(R.id.fightStyleInput);
        EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        EditText passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);
        if (passwordInput.getText().toString().equals(passwordConfirm.getText().toString())){
            User user = UserManager.createUser(emailInput.getText().toString(),
                    usernameInput.getText().toString(), passwordInput.getText().toString());
            DatabaseManager.addUser(user);
            Intent intent = new Intent(this, BiographicalScreen.class).putExtra("user", user);
            startActivity(intent);
        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "Passwords do not match";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

}
