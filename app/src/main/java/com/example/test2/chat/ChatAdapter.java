package com.example.test2.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.R;

import java.util.List;

/**
 * the chat adapter creates little bubble boxes for the messages, they are created for
 * each individual message
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private final List<Chat> chatList;
    private final Context context;

    /**
     * constructor
     * @param chatList the list of chat messages
     * @param context the screen context
     */
    public ChatAdapter(List<Chat> chatList, Context context){
        this.chatList = chatList;
        this.context = context;
    }

    /**
     * this is a method that creates little bubble boxes
     * @param parent the parent of the bubble boxes (messageActivity)
     * @param viewType the type of the bubble boxes
     * @return the return value (objects for our screen)
     */
    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new ChatViewHolders(layoutView);
    }

    /**
     * a method to take the bubble boxes we create and bind them to the chat view
     * @param holder the bubble box itself
     * @param position the position it should be displayed at
     */
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        // getting drawable resources for the chat bubble backgrounds
        Drawable clientUserBubble = context.getResources().getDrawable(R.drawable.user_bubble);
        Drawable matchUserBubble = context.getResources().getDrawable(R.drawable.other_bubble);
        if(chatList.get(position).isCurrentUser()){
            holder.mContainer.setGravity(Gravity.RIGHT);
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setBackground(clientUserBubble);
        }else{
            holder.mContainer.setGravity(Gravity.LEFT);
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setBackground(matchUserBubble);
        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
