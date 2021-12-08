package com.example.test2.screens;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test2.R;

/**
 * this is the class that is responsible for displaying a users bio and for editing anything the
 * user wants to edit about the bio
 */
public class BiographicalScreen extends AppCompatActivity implements View.OnClickListener {

    ImageView image1;
    Button button1;
    ImageView image2;
    Button button2;
    ImageView image3;
    Button button3;

    protected final BetterActivityResult<Intent,
                ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);

    /**
     * adds listeners to our buttons and gets our image selectors upon the app starting
     * @param savedInstanceState the state of the app in case the user closes the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.biographical_input);

        image1 = (ImageView) findViewById(R.id.imageView6);
        button1 = (Button) findViewById(R.id.button6);
        button1.setOnClickListener(this);
        image2 = (ImageView) findViewById(R.id.imageView7);
        button2 = (Button) findViewById(R.id.button7);
        button2.setOnClickListener(this);
        image3 = (ImageView) findViewById(R.id.imageView8);
        button3 = (Button) findViewById(R.id.button8);
        button3.setOnClickListener(this);
    }

    /**
     * returns to the account creation screen
     * @param view the account creation view
     */
    public void backToCreate(View view) {

        Intent intent = new Intent(this, CreateAccountScreen.class);
        startActivity(intent);
    }

    /**
     * all of these are methods used to upload images from the phone to the app
     */
    public void uploadImage1() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Uri data = result.getData().getData();
                image1.setImageURI(data);
            }
        });
    }
    public void uploadImage2() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Uri data = result.getData().getData();
                image2.setImageURI(data);
            }
        });
    }
    public void uploadImage3() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Uri data = result.getData().getData();
                image3.setImageURI(data);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button6:
                uploadImage1();
                break;
            case R.id.button7:
                uploadImage2();
                break;
            case R.id.button8:
                uploadImage3();
                break;
        }
    }
    public void goToHome(View view){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
}
