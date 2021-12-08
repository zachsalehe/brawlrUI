package com.example.test2.screens;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;

import com.bumptech.glide.Glide;
import com.example.test2.R;
import com.example.test2.matches.MatchesActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * this class is responsible for showing the profile screen of the app, it also allows the user
 * to edit their information. This runs counter to the single responsibility principle, look at the
 * design doc why we made it this way
 */
public class ProfileScreen extends AppCompatActivity {

    private EditText mNameField, mFightingStyleField, mWeightField, mHeightField, mBiographyField, mControversialField;
    private ImageView mProfileImage;

    private DatabaseReference mUserDatabase;

    private String userId, name, style, weight, height, biography, opinion, profileImageUrl;

    private Uri resultUri;

    /**
     * connects to the database when the screen is created and loads all the information from the
     * user into the fields, it also allows for the editing of these fields. However editing and
     * retrieval is done in different methods
     * @param savedInstanceState the previous state of the app if the user closed the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile_screen);

        mNameField = (EditText) findViewById(R.id.name);
        mFightingStyleField = (EditText) findViewById(R.id.fightingStyle);
        mWeightField = (EditText) findViewById(R.id.weight);
        mHeightField = (EditText) findViewById(R.id.height);
        mBiographyField = (EditText) findViewById(R.id.biography);
        mControversialField = (EditText) findViewById(R.id.opinion);

        mProfileImage = (ImageView) findViewById(R.id.profileImage);

        Button mConfirm = (Button) findViewById(R.id.confirm);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        ActivityResultLauncher<Intent> onActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            resultUri = data.getData();
                            mProfileImage.setImageURI(resultUri);
                        }
                    }
                });


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                onActivityResultLauncher.launch(intent);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

    }

    /**
     * gets the information of the current user being viewed, when this is call the first time it
     * puts a listener on the app which will change any user information automatically if it is
     * changed on the database
     */
    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();
                        mNameField.setText(name);
                    }
                    if(map.get("style")!=null){
                        style = map.get("style").toString();
                        mFightingStyleField.setText(style);
                    }
                    if(map.get("weight")!=null){
                        weight = map.get("weight").toString();
                        mWeightField.setText(weight);
                    }
                    if(map.get("height")!=null){
                        height = map.get("height").toString();
                        mHeightField.setText(height);
                    }
                    if(map.get("biography")!=null){
                        biography = map.get("biography").toString();
                        mBiographyField.setText(biography);
                    }
                    if(map.get("opinion")!=null){
                        opinion = map.get("opinion").toString();
                        mControversialField.setText(opinion);
                    }
                    Glide.clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        if ("default".equals(profileImageUrl)) {
                            mProfileImage.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * saves the changed user information into the database
     */
    private void saveUserInformation() {
        name = mNameField.getText().toString();
        style = mFightingStyleField.getText().toString();
        weight = mWeightField.getText().toString();
        height = mHeightField.getText().toString();
        biography = mBiographyField.getText().toString();
        opinion = mControversialField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("style", style);
        userInfo.put("weight", weight);
        userInfo.put("height", height);
        userInfo.put("biography", biography);
        userInfo.put("opinion", opinion);

        mUserDatabase.updateChildren(userInfo);
        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileImageUrl", uri.toString());
                            mUserDatabase.updateChildren(newImage);

                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            finish();
                        }
                    });
                }
            });
        }else{
            finish();
        }
    }

    /**
     * goes to the home screen
     * @param view home screen view
     */
    public void homeScreen(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    /**
     * goes to the profile screen
     * @param view profile screen views
     */
    public void profileScreen(View view){
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }

    /**
     * goes to the matches screen
     * @param view the matches screen
     */
    public void matchesScreen(View view){
        Intent intent = new Intent(this, MatchesActivity.class);
        startActivity(intent);
    }
}