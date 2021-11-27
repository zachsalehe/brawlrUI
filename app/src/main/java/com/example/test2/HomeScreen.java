package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.protobuf.Value;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class HomeScreen extends AppCompatActivity {
    private cards cards_data[];
    private com.example.test2.cards.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String currentUid;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;

    /**
     * onCreate runs code upon the creation of the class using the current
     * state of the app.
     * The function will manage the overall pulling of users from the database to the app,
     * create cards to prepare for the swiping feature, and remove cards from the array upon
     * swiping actions.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
             * regardless of the direction of the s
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
             * @param dataObject
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
             * @param dataObject
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
             * @param itemsInAdapter
             */
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            /**
             * Listener for scrolling action.
             * @param scrollProgressPercent
             */
            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        /**
         * Listener to indicate that the card has been clicked on.
         */
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(HomeScreen.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUid).child("connections").child("yes").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(HomeScreen.this, "new Connection", Toast.LENGTH_LONG).show();
                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUid).setValue(true);
                    usersDb.child(currentUid).child("connections").child("matches").child(snapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

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

    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }

//    public void messagesScreen(View view){
//        Intent intent = new Intent(this, MessagesScreen.class);
//        startActivity(intent);
//    }

    public void matchesScreen(View view){
        Intent intent = new Intent(this, MatchesActivity.class);
        startActivity(intent);
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(HomeScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
