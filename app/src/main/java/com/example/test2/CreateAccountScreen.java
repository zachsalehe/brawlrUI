package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountScreen extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail, mPassword, mCPassword, mName;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.create_account);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
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

        mRegister = (Button) findViewById(R.id.goSignIn2);

        mEmail = (EditText) findViewById(R.id.username3);
        mPassword = (EditText) findViewById(R.id.password2);
        mCPassword = (EditText) findViewById(R.id.password3);
        mName = (EditText) findViewById(R.id.username2);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String cPassword = mCPassword.getText().toString();
                final String name = mName.getText().toString();
                if (name.equals("") || password.equals("") || email.equals("") || !password.equals(cPassword)) {
                    Toast.makeText(CreateAccountScreen.this, "sign up error", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(CreateAccountScreen.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            DatabaseReference currentUserDb2 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("connections").child("no").child(userId);
                            HashMap<String, Object> userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("profileImageUrl", "default");
                            currentUserDb2.setValue(true);
                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    public void backToMain(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toBiographical(View view) {

        Intent intent = new Intent(this, BiographicalScreen.class);
        startActivity(intent);
    }

}
