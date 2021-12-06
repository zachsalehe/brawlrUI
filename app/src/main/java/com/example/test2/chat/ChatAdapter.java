package com.example.test2.chat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(List<Chat> chatList, Context context){
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: create itemchat
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false); //we need to create an item_chat
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage()); //if it is the user sending
        // TODO: no need to manually modify color, simply change drawable bubble background
        if(chatList.get(position).getCurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setBackgroundColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("F4F4F4"));

        }else{ //if user is receiving message
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setBackgroundColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("F4F4F4"));
        }
    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
