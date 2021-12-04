package com.example.test2.matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test2.HomeScreen;
import com.example.test2.ProfileScreen;
import com.example.test2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    // RecyclerViews are Android Studio components that make it easy to display large lists of
    // dynamic data.
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();

    private String currentUserID;

    /**
     * onCreate is a typical initiating method seen in Activity classes. It makes a
     * constructor call to parent class AppCompatActivity and takes in the current state
     * of the app.
     * @param savedInstanceState Current app state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        // Gets the user ID of the current client user.
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Creates the RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        // Disables scrolling of nested scrolling views
        mRecyclerView.setNestedScrollingEnabled(false);
        // Fixes the size of the Recycler View
        mRecyclerView.setHasFixedSize(true);

        // The LinearLayoutManager specifies an orientation for each item in the layout
        // of RecyclerView.
        mMatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        // A RecyclerView's Adapter binds the data from the database to the views.
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        // Calls getUserMathId() upon creation to port in ALL of the client user's current matches
        getUserMatchId();
    }

    /**
     * getUserMatchId() creates a Firebase location reference of all the client user's
     * current matches.
     */
    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Creates a listener for any additions to the client user's list of matched users
             * @param snapshot DataSnapshot of changed data
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Taking in a match's database key, FetchMatchInformation will create a Firebase database
     * reference of the match's complete user information.
     * @param key ID of the given matched user
     */
    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Upon any changes in the userDb, add in all the given user's information into the
             * following parameters.
             * @param dataSnapshot DataSnapshot of changed data
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("profileImageUrl").getValue() != null) {
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }

                    // Adds the new user match data and creates a new MatchObj to contain the data
                    MatchesObject obj = new MatchesObject(userId, name, profileImageUrl);
                    // Adds new user match objet to ArrayList of client user's matches
                    resultsMatches.add(obj);
                    // Tells the Adapter that there's a new data object so that the view can
                    // be modified accordingly
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Returns the current list of the client user's matches
    private List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }

    // switches to HomeScreen activity upon trigger
    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    // switches to ProfileScreen activity upon trigger
    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }
}
