package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Normally this should be a client-side-only class but because
 * we're going for a pretty abstract implementation at the moment,
 * this class will allow both user1 (client) and some user2 to enter messages
 * into a chat log.
 */
public class MessageActivity extends AppCompatActivity{
    /**
     * Instantiates a new database of the message history
     * for the current chat.
     */

    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;
    ArrayList<String> history = new ArrayList<>();
    TextView username;
    private String currentUserID;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    Intent intent;
    Button sendBtn;
    EditText text_message;
    private EditText MessageInputText;
    private DatabaseReference RootRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_screen); //or chat screen
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        
        RootRef = FirebaseDatabase.getInstance().getReference();
        /**
        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Meow");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.getNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
        **/

        username = findViewById(R.id.username);
        intent = getIntent();
        String userid = intent.getStringExtra("userid");

        sendBtn = findViewById(R.id.button9);
        text_message = findViewById(R.id.type_messages);
        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                sendMessage();
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

    private void sendMessage()
    {
        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "first write your message...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef = "Messages/" + messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = "Messages/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                    .child(messageSenderID).child(messageReceiverID).push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("Sender", messageSenderID);
            messageTextBody.put("Receiver", messageReceiverID);
            messageTextBody.put("chatID", messagePushID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(MessageActivity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MessageActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    MessageInputText.setText("");
                }
            });
        }
    }

    /*
    private void sendMessage(String sender, String receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }
    */

    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }
}
