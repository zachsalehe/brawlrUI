package com.example.test2;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import com.example.test2.matches.MatchesActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

//public class ProfileScreen extends AppCompatActivity implements View.OnClickListener {
public class ProfileScreen extends AppCompatActivity {

    private EditText mNameField, mFightingStyleField, mWeightField, mHeightField, mBiographyField, mControversialField;
    private Button mConfirm;
    private ImageView mProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId, name, style, weight, height, biography, opinion, profileImageUrl;

    private Uri resultUri;


//    ImageView image1;
//    Button button1;
//    ImageView image2;
//    Button button2;
//    ImageView image3;
//    Button button3;

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

        mConfirm = (Button) findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        ActivityResultLauncher<Intent> onActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
//                            doSomeOperations();
                            final Uri imageUri = data.getData();
                            resultUri = imageUri;
                            mProfileImage.setImageURI(resultUri);
                        }
                    }
                });


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
//                startActivityForResult(intent, 1);
                onActivityResultLauncher.launch(intent);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

//        image1 = (ImageView) findViewById(R.id.imageView6);
//        button1 = (Button) findViewById(R.id.button6);
//        button1.setOnClickListener(this);
//        image2 = (ImageView) findViewById(R.id.imageView7);
//        button2 = (Button) findViewById(R.id.button7);
//        button2.setOnClickListener(this);
//        image3 = (ImageView) findViewById(R.id.imageView8);
//        button3 = (Button) findViewById(R.id.button8);
//        button3.setOnClickListener(this);
    }

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
                        switch(profileImageUrl){
                            case "default":
//                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                mProfileImage.setImageResource(R.mipmap.ic_launcher);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

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
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            finish();
                            return;
                        }
                    });
                }
            });
        }else{
            finish();
        }
    }


//    public void uploadImage1() {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        activityLauncher.launch(intent, result -> {
//            if (result.getResultCode() == Activity.RESULT_OK) {
//                // There are no request codes
//                Uri data = result.getData().getData();
//                image1.setImageURI(data);
//            }
//        });
//    }
//    public void uploadImage2() {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        activityLauncher.launch(intent, result -> {
//            if (result.getResultCode() == Activity.RESULT_OK) {
//                // There are no request codes
//                Uri data = result.getData().getData();
//                image2.setImageURI(data);
//            }
//        });
//    }
//    public void uploadImage3() {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        activityLauncher.launch(intent, result -> {
//            if (result.getResultCode() == Activity.RESULT_OK) {
//                // There are no request codes
//                Uri data = result.getData().getData();
//                image3.setImageURI(data);
//            }
//        });
//    }

//    @Override
//    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.button6:
//                uploadImage1();
//                break;
//            case R.id.button7:
//                uploadImage2();
//                break;
//            case R.id.button8:
//                uploadImage3();
//                break;
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
//            final Uri imageUri = data.getData();
//            resultUri = imageUri;
//            mProfileImage.setImageURI(resultUri);
//        }
//    }

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
}