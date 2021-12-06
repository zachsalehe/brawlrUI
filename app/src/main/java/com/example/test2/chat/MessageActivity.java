package com.example.test2.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.HomeScreen;
import com.example.test2.ProfileScreen;
import com.example.test2.R;
import com.example.test2.matches.MatchesActivity;
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

public class MessageActivity extends AppCompatActivity{
    /**
     * Instantiates a new database of the message history
     * for the current chat.
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter cChatAdapter;
    private RecyclerView.LayoutManager cChatLayoutManager;

    TextView username;
    private String matchId;
    private String currentUserID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    Intent intent;
    Button send_btn;
    EditText text_message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_screen); //or chat screen
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.chatView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        cChatLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mRecyclerView.setLayoutManager(cChatLayoutManager);
        cChatAdapter = new ChatAdapter(currentUserID, getDataSetChat(), MessageActivity.this);
        mRecyclerView.setAdapter(cChatAdapter);

        username = findViewById(R.id.username);
        intent = getIntent();
        matchId = intent.getStringExtra("matchID");

        send_btn = findViewById(R.id.button9);
        text_message = findViewById(R.id.chatField);
        send_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String msg = text_message.getText().toString();
                if (!msg.equals("")){
                    System.out.println(msg);
                    System.out.println("Yay it worked!");
                    sendMessage(currentUserID, matchId, msg);
                }
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                System.out.println(reference.child("Unit1"));
                dataSnapshot.getValue(com.example.test2.chat.Chat.class);
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
    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }

    public void matchesScreen(View view) {
        Intent intent = new Intent(this, MatchesActivity.class);
        startActivity(intent);
    }

    private ArrayList<Chat> resultsChats = new ArrayList<Chat>();
    private List<Chat> getDataSetChat() {
        return resultsChats;
    }

}
