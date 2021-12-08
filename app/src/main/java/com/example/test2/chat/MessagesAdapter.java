package com.example.test2.chat;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * this is a class responsible for displaying the text to be shown in the individual
 * text boxes
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>
{
    private final List<Chat> userMessagesList;
    private FirebaseAuth mAuth;

    /**
     * constructor for the message adapter
     * @param userMessagesList a list of messages to be displayed
     */
    public MessagesAdapter(List<Chat> userMessagesList)
    {
        this.userMessagesList = userMessagesList;
    }


    /**
     * it creates a view holder for our messages and connects it to our view
     */
    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView senderMessageText, receiverMessageText;
        public CircleImageView receiverProfileImage;
        public ImageView messageSenderPicture, messageReceiverPicture;


        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            senderMessageText = (TextView) itemView.findViewById(R.id.username);
            receiverMessageText = (TextView) itemView.findViewById(R.id.username2);
        }
    }


    /**
     * creates the desired number of places to write our messages and displays it on the screen
     * @param viewGroup a view that contatins other view (view of message box containing view of
     *                  message)
     * @param i an iterator variable
     * @return it returns a view ready to have a message added to it
     */
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_messages_screen, viewGroup, false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }


    /**
     * this binds the actual messages to the message text boxes that are meant to hold them. It
     * connects to the database and also gets the most recent messages to be displayed
     * @param messageViewHolder the view that will hold our messages to be displayed
     * @param i an iterator variable
     */
    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i)
    {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Chat chat = userMessagesList.get(i);

        String fromUserID = chat.getSender();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild("image"))
                {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        messageViewHolder.receiverMessageText.setVisibility(View.GONE);
        messageViewHolder.receiverProfileImage.setVisibility(View.GONE);
        messageViewHolder.senderMessageText.setVisibility(View.GONE);


    }




    @Override
    public int getItemCount()
    {
        return userMessagesList.size();
    }

}