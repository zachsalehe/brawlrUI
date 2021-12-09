package com.example.test2.matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test2.screens.HomeScreen;
import com.example.test2.screens.ProfileScreen;
import com.example.test2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * this displays the base of the matches screen. The matches screen works by having a background and
 * overlaying different screen segments on top so that the screen can change dynamically depending
 * on the number of open chats the user has. The matches screen is the first screen you see when you
 * click on chat on the app, the one before opening an actual chat with another user
 */
public class MatchesActivity extends AppCompatActivity {

    private RecyclerView.Adapter mMatchAdapter;

    private String currentUserID;

    /**
     * this sets up the matches view and calls on the matches adapter which will overlay on the
     * view to display caht information
     * @param savedInstanceState a saved instance of the screen in case the user closes the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_matches);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.matchView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mMatchLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchLayoutManager);
        mMatchAdapter = new MatchesAdapter(getDataSetMatch(), MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchAdapter);

        getUserMatchId();
    }

    /**
     * this function is responsible for adding a database listener to our match data in order to
     * automatically update the matches screen if new matches have been made
     */
    private void getUserMatchId() {

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Uh oh!");
            }
        });
    }

    /**
     * this function gets the match information of our users for it to be displayed on the matches
     * screen
     * @param key
     */
    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                    if(dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }

                    Match obj = new Match(userId, name, profileImageUrl);
                    resultsMatches.add(obj);
                    mMatchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private final ArrayList<Match> resultsMatches = new ArrayList<Match>();
    private List<Match> getDataSetMatch() {
        return resultsMatches;
    }

    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }
}
