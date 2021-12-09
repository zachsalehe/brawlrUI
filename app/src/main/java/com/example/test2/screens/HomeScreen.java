package com.example.test2.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test2.R;
import com.example.test2.cards.arrayAdapter;
import com.example.test2.cards.cards;
import com.example.test2.matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is responsible for handling the home screen and for making swiping work in the app
 * This class also interacts with the database which goes against the single responsibility
 * principle, check the design document for why we made this decision
 */
public class HomeScreen extends AppCompatActivity {
    private cards cards_data[];
    private com.example.test2.cards.arrayAdapter arrayAdapter;
    private int i;
    public boolean matchSuccess = false;

    private FirebaseAuth mAuth;
    private String currentUid;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;

    public HomeScreen() {
    }

    /**
     * onCreate runs code upon the creation of the class using the current
     * state of the app.
     * The function will manage the overall pulling of users from the database to the app,
     * create cards to prepare for the swiping feature, and remove cards from the array upon
     * swiping actions.
     * @param savedInstanceState this is a saved instance that is loaded if the user closes the app
     *                           while on this screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super references AppCompatActivity
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        getOtherUsers();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            /**
             * Pops off the first object in the cards array after a card has been swiped on,
             * regardless of the direction of the swipe.
             */
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            /**
             * Removes the currently displayed user card from the client user's
             * connections if the client swiped left. (In the "no" child of "connections")
             * @param dataObject this is the object of the user that was just swiped left on
             */
            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("no").child(currentUid).setValue(true);
                Toast.makeText(HomeScreen.this, "left", Toast.LENGTH_SHORT).show();
            }

            /**
             * Adds the currently displayed user card to the client user's connections
             * if the client swiped right. (In the "yes" child of "connections").
             * @param dataObject this is the the object of the user that was just swiped right on
             */
            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yes").child(currentUid).setValue(true);
                isMatch(userId);
                Toast.makeText(HomeScreen.this, "right", Toast.LENGTH_SHORT).show();
            }

            /**
             * Use this to continuously add more user cards from the database into the
             * Adapter array in order to be displayed. It'd probably be nice to implement this
             * feature for scalability reasons as the user database gets larger.
             * @param itemsInAdapter an array of user items in our adapter
             */
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            /**
             * Listener for scrolling action.
             * @param scrollProgressPercent the percent of the screen that has been scrolled
             */
            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(HomeScreen.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Sets up a listener for when new cards are added into the client user's connections
     * table.
     * @param userId the id of the user that has just been matched with
     */
    protected void isMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUid).child("connections").child("yes").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Connects client user and matched user in the matches table.
             * @param snapshot snapshot of the current state of the database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(HomeScreen.this, "new Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUid).child("ChatId").setValue(key);
                    usersDb.child(currentUid).child("connections").child("matches").child(snapshot.getKey()).child("ChatId").setValue(key);
                    matchSuccess = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * getOtherUsers() adds a listener to the Firebase database and updates the swiping cards
     * list (rowItems, and arrayAdapter) accordingly whenever a new user (who is not the current
     * client user) is added into the database.
     *
     */
    public void getOtherUsers() {
        DatabaseReference notUserDb = FirebaseDatabase.getInstance().getReference().child("Users");

        notUserDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && !snapshot.child("connections").child("no").hasChild(currentUid) && !snapshot.child("connections").child("yes").hasChild(currentUid)) {
                    String profileImageUrl = "default";
                    if (!snapshot.child("profileImageUrl").getValue().equals("default")) {
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    cards item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), profileImageUrl);
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            /**
             * account information is displayed in the swipe feed. Although these methods are not implemented
             * they are required to exist to add a childEventListener
             * @param snapshot a database snapshot
             * @param previousChildName the name of the previous card
             */
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            /**
             * deleted accounts can no longer show up in the swiping feed.
             * @param snapshot a snapshot of the database
             */
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void setCurrentUid(String currentID){
        currentUid = currentID;
    }
    /**
     * Intent tells the app to go to the home screen.
     * @param view the home screen view
     */
    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    /**
     * Intent tells the app to go to the user's profile screen.
     * @param view profile screen view
     */
    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }


    /**
     * Intent tells the app to go to the list of matches screen.
     * @param view the matches screen view
     */
    public void matchesScreen(View view){
        Intent intent = new Intent(this, MatchesActivity.class);
        startActivity(intent);
    }

    /**
     * Logs out the user from the current mAuth instance.
     * @param view the main screen view
     */
    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(HomeScreen.this, MainActivityScreen.class);
        startActivity(intent);
        finish();
    }
}
