package com.example.test2.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * responsible for signing in users, also has database interactions which runs counter to the
 * single responsibility principle. Look at the design document why we did so.
 */
public class SignInScreen extends AppCompatActivity {

    private EditText mEmail, mPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    public boolean isSignedIn = false;

    /**
     * sets up the screen and connects the view to the real time database and does user sign
     * in, this also runs counter to single responsibility, we once again did this since it is
     * required by the realtime firebase database in order to log in the user using the most recent
     * credentials on the database.
     * @param savedInstanceState state of the app if the app was closed by the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in_screen);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(SignInScreen.this, HomeScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        Button mRegister = (Button) findViewById(R.id.goSignIn);

        mEmail = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                signIn(username, password);
            }
        });
    }

    /**
     * This method is used to sign in the user
     * @param username the username
     * @param password the password
     */
    protected void signIn(String username, String password){
        if (username.equals("") || password.equals("")) {
            Toast.makeText(SignInScreen.this, "sign up error", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(SignInScreen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(SignInScreen.this, "sign up error", Toast.LENGTH_SHORT).show();
                }
                else {
                    isSignedIn = true;
                }
            }
        });
    }

    /**
     * add listener when the app starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    /**
     * removes the listener when the app stops
     */
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    /**
     * returns back to main
     * @param view the main view
     */
    public void backToMain(View view) {

        Intent intent = new Intent(this, MainActivityScreen.class);
        startActivity(intent);
    }

    /**
     * returns back to the hom screen
     * @param view the home screen view
     */
    public void goToHome(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
}
