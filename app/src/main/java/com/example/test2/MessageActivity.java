package com.example.test2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.test2.R.id.button9;

/**
 * Normally this should be a client-side-only class but because
 * we're going for a pretty abstract implementation at the moment,
 * this class will allow both user1 (client) and some user2 to enter messages
 * into a chat log.
 */
public class MessageActivity extends AppCompatActivity {
    /**
     * Instantiates a new database of the message history
     * for the current chat.
     */
    ArrayList<String> history = new ArrayList<>();
    TextView username;
    private String currentUserID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    Intent intent;
    ImageButton send_btn;
    EditText text_message;
    Button button;
    TextView textView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_screen); //or chat screen
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        //getSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Meow");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish()
            }
        })
        */
        button = findViewById(button9);

        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                textView.setText("button.setOnClickListener(new View.OnClickListener() code is working");
            }
        });
    }





    /**
     * Method to send messages from "sender" to "receiver" in database.
     * @param sender UserID of User sending message
     * @param receiver UserID of User receiving message
     * @param message Message being sent
     */
    private void sendMessage(String sender, String receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }
}
