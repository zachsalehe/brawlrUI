package com.example.test2.matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test2.chat.MessageActivity;
import com.example.test2.R;

/**
 * this is inheriting from other view holders, we are taking the idea of view holders and making
 * our thumbnail fit the way we want it to look on the screen
 */
public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;

    /**
     * it is the constructor
     * @param itemView the matches view
     */
    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
        mMatchName = (TextView) itemView.findViewById(R.id.MatchName);

        mMatchImage = (ImageView) itemView.findViewById(R.id.MatchImage);
    }

    /**
     * opens the chat thumbnail you click on
     * @param view the matches view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), MessageActivity.class);
        Bundle b = new Bundle();
        b.putString("matchID", mMatchId.getText().toString());
        System.out.println(mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
