package com.example.test2;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final DatabaseReference myRef = db.getReference("users");
    private static final String TAG = "error";
    private static User retrievedUser;

    public static void addUser(User user){

        // Add a new document with a generated ID
        myRef.child(user.getId()).setValue(user);
    }

    public static User getUser(String username){
        myRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    System.out.println("-------------------------------RETRIEVAL SUCCESSFUL---------------------------------");
                    Map returnedData = (Map<String, Object>) task.getResult().getValue();
                    User user = UserManager.createUser((String) returnedData.get("id"),
                            (HashMap) returnedData.get("loginInfo"),
                            (String) returnedData.get("fightingStyle"),
                            (String) returnedData.get("biography"),
                            (String) returnedData.get("controversialOpinions"));
                    retrievedUser = user;
                    System.out.println(retrievedUser.getId());
                }
            }
        });
        System.out.println("-------------------------------------user next line-----------------------");
        System.out.println(retrievedUser);
        return retrievedUser;
    }
}
