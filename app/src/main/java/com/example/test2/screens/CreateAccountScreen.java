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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * this class takes care fo creating users, although it does have interactions with the database
 * which goes agains the single responsibility principle you can check the design document as to why
 * we decided to make it this way
 */
public class CreateAccountScreen extends AppCompatActivity {

    private EditText mEmail, mPassword, mCPassword, mName;
    public boolean signupSuccess = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    /**
     * this method sets up the database and creates listeners for changes in the sate of our
     * database
     * @param savedInstanceState this is the saved state of this screen in case the user closes the
     *                           app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.create_account);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            /**
             * this updates all the information on the screen and on the database in case there is
             * a change on the database. If a user is successfully created it also changes to the
             * home screen view
             * @param firebaseAuth this is the auth object of our database
             */
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(CreateAccountScreen.this, HomeScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        Button mRegister = (Button) findViewById(R.id.goSignIn2);

        mEmail = (EditText) findViewById(R.id.username3);
        mPassword = (EditText) findViewById(R.id.password2);
        mCPassword = (EditText) findViewById(R.id.password3);
        mName = (EditText) findViewById(R.id.username2);

        mRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * this registers a user onto our database
             * @param view this is the current view of create account scren
             */
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String cPassword = mCPassword.getText().toString();
                final String name = mName.getText().toString();
                createAccount(name, cPassword, password, email);
            }
        });
    }

    /**
     * this method is responsible for creating accounts on the database, although it goes against
     * the single responsiblity principle we need it to be in this view so it can change data on
     * the app as sson as there are changes
     * @param name the name of the user
     * @param cPassword the check password
     * @param password the password
     * @param email the email adress
     */
    protected void createAccount(String name, String cPassword, String password, String email){
        if (name.equals("") || password.equals("") || email.equals("") || !password.equals(cPassword)) {
            Toast.makeText(CreateAccountScreen.this, "sign up error", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountScreen.this, new OnCompleteListener<AuthResult>() {
            /**
             * this checks that our user was successfully created.
             * @param task the task that the database should do
             */
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(CreateAccountScreen.this, "sign up error", Toast.LENGTH_SHORT).show();
                }else{
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    DatabaseReference currentUserDb2 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("connections").child("no").child(userId);
                    Map userInfo = new HashMap<>();
                    userInfo.put("name", name);
                    userInfo.put("profileImageUrl", "default");
                    currentUserDb2.setValue(true);
                    currentUserDb.updateChildren(userInfo);
                    signupSuccess = true;
                }
            }
        });
    }

    /**
     * start the database listeners
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    /**
     * end the database liseteners
     */
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    /**
     * returns to main
     * @param view the main view
     */
    public void backToMain(View view) {

        Intent intent = new Intent(this, MainActivityScreen.class);
        startActivity(intent);
    }

    /**
     * goes to biographical info
     * @param view the biographical info view
     */
    public void toBiographical(View view) {

        Intent intent = new Intent(this, BiographicalScreen.class);
        startActivity(intent);
    }

}
