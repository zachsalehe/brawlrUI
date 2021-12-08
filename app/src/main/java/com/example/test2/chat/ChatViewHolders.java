package com.example.test2.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.R;

/**
 * this is a class that creates view holder for our bubble boxes (look at chatAdapter)
 */
public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer;
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = (TextView) itemView.findViewById(R.id.message);
        mContainer = (LinearLayout) itemView.findViewById(R.id.chatContainer);
    }

    @Override
    public void onClick(View view) {
    }
}
