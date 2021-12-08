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

        // saving the full list of users in a usersDb variable
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        // authenticating current client user and grabbing client's user ID in currentUid
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        // Adds all users in the database who are not the current client user into the
        // rowItems array to be displayed for swiping.
        getOtherUsers();

        // this is where arrayAdapter will get the raw user information from
        rowItems = new ArrayList<cards>();

        // the swipe feed will be grabbing card info from the arrayAdapter
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        // sets up a container that contains the cards
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

//         Listener to indicate that the card has been clicked on.
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
    private void isMatch(String userId) {
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
        // creates a reference of the full list of users from the database
        DatabaseReference notUserDb = FirebaseDatabase.getInstance().getReference().child("Users");

        // adds event listeners to look for changes in the notUserDb database reference
        notUserDb.addChildEventListener(new ChildEventListener() {
            @Override
//             onChildAdded() will add any newly added user into the client user's swiping cards
//             list.
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
             * TODO: It would be nice to do something with onChildChanged to ensure only updated
             * account information is displayed in the swipe feed.
             * @param snapshot
             * @param previousChildName
             */
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            /**
             * TODO: It'd probably be nice to do something with onChildRemoved to make sure
             * deleted accounts can no longer show up in the swiping feed.
             * @param snapshot
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
