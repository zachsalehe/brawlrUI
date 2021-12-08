package com.example.test2.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.screens.HomeScreen;
import com.example.test2.screens.ProfileScreen;
import com.example.test2.R;
import com.example.test2.matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * this is the holder activity that is responsible for being a background to display our
 * messages, it uses the other classes in the chat package. Also it connects directly
 * to the database, which is against the single responsibility principle but you can
 * check the design document to see why we decided to make it this way
 */
public class MessageActivity extends AppCompatActivity{

    private RecyclerView.Adapter cChatAdapter;

    TextView username;
    private String matchID, currentUserID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference reference = database.getReference();
    private DatabaseReference mDatabaseChat;
    Intent intent;
    Button send_btn;
    EditText text_message;

    /**
     * sets up the database and retrieves the messages from the database. Due to it being
     * real time it automatically retrieves new messages
     * @param savedInstanceState a seaved instance of the view if the user closes the app
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_screen); //or chat screen
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();

        matchID = getIntent().getExtras().getString("matchID");

        DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchID).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chats");

        getChatMessages();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.chatView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager cChatLayoutManager = new LinearLayoutManager(MessageActivity.this);
        mRecyclerView.setLayoutManager(cChatLayoutManager);
        cChatAdapter = new ChatAdapter(getDataSetChat(), MessageActivity.this);
        mRecyclerView.setAdapter(cChatAdapter);

        username = findViewById(R.id.username);
        intent = getIntent();

        send_btn = findViewById(R.id.button9);
        text_message = findViewById(R.id.chatField);
        send_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String msg = text_message.getText().toString();
                if (!msg.equals("")){
                    System.out.println(msg);
                    sendMessage(currentUserID, matchID, msg);
                }
            }
        });
    }

    /**
     * sends a message to the desired user
     * @param sender user sending the message
     * @param receiver user recieving the message
     * @param message the message
     */
    private void sendMessage(String sender, String receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        mDatabaseChat.push().setValue(hashMap);
    }

    /**
     * retrieves chat messages
     */
    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String message = null;
                    String sender = null;
                    String receiver = null;

                    if (snapshot.child("message").getValue() != null) {
                        message = snapshot.child("message").getValue().toString();
                    } else {
                        System.out.println("message grabbed");
                    }
                    if (snapshot.child("sender").getValue() != null) {
                        sender = snapshot.child("sender").getValue().toString();
                    }
                    if (snapshot.child("receiver").getValue() != null) {
                        receiver = snapshot.child("receiver").getValue().toString();
                    }
                    if (message != null && sender != null && receiver != null) {
                        boolean isCurrentUser = false;
                        if (sender.equals(currentUserID)) {
                            isCurrentUser = true;
                        }
                        Chat newMessage = new Chat(sender, receiver, message, isCurrentUser);
                        resultsChats.add(newMessage);
                        cChatAdapter.notifyDataSetChanged();
                    }
                } else {
                    System.out.println("bbed 2message");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * methods to return to other screens
     * @param view the view of the desired screen
     */
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

    private final ArrayList<Chat> resultsChats = new ArrayList<Chat>();
    private List<Chat> getDataSetChat() {
        return resultsChats;
    }

}
