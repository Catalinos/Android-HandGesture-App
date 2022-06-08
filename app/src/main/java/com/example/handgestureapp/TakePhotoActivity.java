package com.example.handgestureapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class TakePhotoActivity extends AppCompatActivity {

    ImageTaken myImageDetails;

    ImageView imageTaken;
    String gestureType;
    String filepath;

    int number_ofGestures;
    int left_gest;
    int right_gest;
    int up_gest;
    int down_gest;

    String username;

    FirebaseStorage storage;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        Bundle bundle = getIntent().getExtras();

        username = bundle.getString("username");
        String gestureNumbers = bundle.getString("gestureNO");
        String up = bundle.getString("UP_GESTURE");
        String down = bundle.getString("DOWN_GESTURE");
        String right = bundle.getString("RIGHT_GESTURE");
        String left = bundle.getString("LEFT_GESTURE");


        number_ofGestures = Integer.parseInt(gestureNumbers) + 1;
        left_gest = Integer.parseInt(left);
        right_gest = Integer.parseInt(right);
        down_gest = Integer.parseInt(down);
        up_gest = Integer.parseInt(up);

        storage = FirebaseStorage.getInstance();

    }

    public void takePhotoAction(View view) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA
            },100);

        }
        else
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageTaken = findViewById(R.id.imageTaken);
            imageTaken.setImageBitmap(bitmap);
        }
    }

    public void setUpGesture(View view) {

        gestureType = "UP";
        setDarkBackground(1);
    }

    public void setDownGesture(View view) {
        gestureType = "DOWN";
        setDarkBackground(2);
    }

    public void setLeftGesture(View view) {
        gestureType = "LEFT";
        setDarkBackground(3);
    }

    public void setRightGesture(View view) {
        gestureType = "RIGHT";
        setDarkBackground(4);
    }

    private void setDarkBackground(int whomToSet){

        CardView upCard = findViewById(R.id.upGestureCard);
        CardView downCard = findViewById(R.id.downGestureCard);
        CardView leftCard = findViewById(R.id.leftGestureCard);
        CardView rightCard = findViewById(R.id.rightGestureCard);

        upCard.setCardBackgroundColor(Color.WHITE);
        downCard.setCardBackgroundColor(Color.WHITE);
        leftCard.setCardBackgroundColor(Color.WHITE);
        rightCard.setCardBackgroundColor(Color.WHITE);

        switch (whomToSet) {
            case 1:
                upCard.setCardBackgroundColor(Color.BLACK);
                break;
            case 2:
                downCard.setCardBackgroundColor(Color.BLACK);
                break;
            case 3:
                leftCard.setCardBackgroundColor(Color.BLACK);
                break;
            case 4:
                rightCard.setCardBackgroundColor(Color.BLACK);
                break;
        }

    }

    private void updateGestureNumbers(){

        switch (gestureType) {
            case "UP":
                up_gest += 1;
                filepath = username + "/" + gestureType + "_"+ String.valueOf(up_gest) + ".jpg";
                break;
            case "DOWN":
                down_gest += 1;
                filepath = username + "/" + gestureType + "_"+ String.valueOf(down_gest) + ".jpg";
                break;
            case "LEFT":
                left_gest += 1;
                filepath = username + "/" + gestureType + "_"+ String.valueOf(left_gest) + ".jpg";
                break;
            case "RIGHT":
                right_gest += 1;
                filepath = username + "/" + gestureType + "_"+ String.valueOf(right_gest) + ".jpg";
                break;
        }

    }

    private void updateUsersData(){
        reference = FirebaseDatabase.getInstance().getReference().child("UsersCredentials");
        Query query = reference.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getChildrenCount()==1) {
                    DataSnapshot snap = snapshot.getChildren().iterator().next();
                    String nodId = snap.getKey();
                    reference.child(nodId).child("number_ofGestures").setValue(String.valueOf(number_ofGestures));
                    reference.child(nodId).child("up_GESTURE").setValue(String.valueOf(up_gest));
                    reference.child(nodId).child("down_GESTURE").setValue(String.valueOf(down_gest));
                    reference.child(nodId).child("left_GESTURE").setValue(String.valueOf(left_gest));
                    reference.child(nodId).child("right_GESTURE").setValue(String.valueOf(right_gest));

                    Toast.makeText(getApplicationContext(),"Database updated!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void uploadImageToDB(View view) {


        // Uploading the image
        updateGestureNumbers();
        Toast.makeText(getApplicationContext(),"To upload "+ filepath + " image to database", Toast.LENGTH_SHORT).show();

        StorageReference myImgRef = storage.getReference().child(filepath);

        // Get the data from an ImageView as bytes
        imageTaken.setDrawingCacheEnabled(true);
        imageTaken.buildDrawingCache();

        Bitmap bitmap = imageTaken.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = myImgRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                Toast.makeText(getApplicationContext(),"UPLOADED "+ filepath + " image to database", Toast.LENGTH_SHORT).show();
                updateUsersData();
                finish();
            }
        });


    }
}