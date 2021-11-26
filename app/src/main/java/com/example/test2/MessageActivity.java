package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * Normally this should be a client-side-only class but because
 * we're going for a pretty abstract implementation at the moment,
 * this class will allow both user1 (client) and some user2 to enter messages
 * into a chat log.
 */
public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
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




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_screen); //or chat screen
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        Toolbar toolbar = findViewById(R.id.toolbar);
        //getSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Meow");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        toolbar.getNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish()
            }
        })
        */

        username = findViewById(R.id.username);
        intent = getIntent();
        String userid = intent.getStringExtra("userid");

        send_btn = findViewById(R.id.button);
        send_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(currentUserID, userid, msg);
                }
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                System.out.println(reference.child("Unit1"));
                dataSnapshot.getValue(Chat.class);
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                System.out.println("Error");
            }
        };
    }
    private void sendMessage(String sender, String receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }
}
