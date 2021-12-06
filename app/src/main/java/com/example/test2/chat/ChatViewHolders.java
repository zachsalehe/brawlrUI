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

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMessage;
    public LinearLayout mContainer;
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = (TextView) itemView.findViewById(R.id.message); //update with messageID
        mContainer = (LinearLayout) itemView.findViewById(R.id.container); //update with Linear Layout ID
    }

    @Override
    public void onClick(View view) {
    }
}
