package com.example.test2.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test2.R;

import java.util.List;

/**
 * this is the matches adapter, it is a class that is used as part of our matches activity to
 * display chat thumbnails
 */
public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders>{
    private final List<Match> matchesList;
    private final Context context;

    public MatchesAdapter(List<Match> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    /**
     * this creates a view holder which is basically what an iframe is in web development, it is a
     * peice of UI that is sourced from a different area of our program
     * @param parent the parent would be the matches activity
     * @param viewType this is the type of the view we want to show
     * @return it returns a MatchesViewHolder object which we actually defined somewhere else
     */
    @NonNull
    @Override
    public MatchesViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolders rcv = new MatchesViewHolders(layoutView);
        return rcv;
    }

    /**
     * here we are editing the chat thumbnail we are creating and addid the user id and name into
     * it
     * @param holder the holder is the thumbnail
     * @param position the position is the position of the thumbnail
     */
    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolders holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchName.setText(matchesList.get(position).getName());

        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
