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

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private String currentUserID;
    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(String currentUserID, List<Chat> chatList, Context context){
        this.currentUserID = currentUserID;
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false); //we need to create an item_chat
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage()); //if it is the user sending
        // getting drawable resources for the bubble backgrounds
        Drawable clientUserBubble = context.getResources().getDrawable(R.drawable.user_bubble);
        Drawable matchUserBubble = context.getResources().getDrawable(R.drawable.other_bubble);
        // TODO: no need to manually modify color, simply change drawable bubble background
        if(chatList.get(position).getSender() == this.currentUserID){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setBackground(clientUserBubble);

        }else{
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setBackground(matchUserBubble);
        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
