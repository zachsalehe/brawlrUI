package com.example.test2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseManager {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "error";
    private static User retrievedUser;

    public static void addUser(User user){

        // Add a new document with a generated ID
        db.collection("brawlrUsers").document(user.getId())
                .set(user);
    }
    public static User getUser(String username){
        db.collection("brawlrUsers").document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);

                    }
                });
        return new User();
    }
}
