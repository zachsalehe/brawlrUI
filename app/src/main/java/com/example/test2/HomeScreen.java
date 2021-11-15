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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class HomeScreen extends AppCompatActivity {
//    private ArrayList<String> al;
//    private ArrayAdapter<String> arrayAdapter;
    private cards cards_data[];
    private com.example.test2.cards.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String currentUid;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        getOtherUsers();
//        al = new ArrayList<>();

        rowItems = new ArrayList<cards>();
//        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.name, al );
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("no").child(currentUid).setValue(true);
                Toast.makeText(HomeScreen.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yes").child(currentUid).setValue(true);
                Toast.makeText(HomeScreen.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(HomeScreen.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getOtherUsers() {
        DatabaseReference notUserDb = FirebaseDatabase.getInstance().getReference().child("Users");
        notUserDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && !snapshot.child("connections").child("no").hasChild(currentUid) && !snapshot.child("connections").child("yes").hasChild(currentUid)) {
                    cards item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString());
                    rowItems.add(item);
//                    al.add(snapshot.child("name").getValue().toString());
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

    public void messagesScreen(View view){
        Intent intent = new Intent(this, MessagesScreen.class);
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
