package com.example.handgestureapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.StringNode;

public class MainMenuActivity extends AppCompatActivity {

    UserCredentials myCredentials = null;
    private DatabaseReference loginCred_DBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        myCredentials =(UserCredentials) getIntent().getSerializableExtra("usersCredentials");

        TextView usrnTxt = findViewById(R.id.usernameTextView);
        usrnTxt.setText(myCredentials.getUsername());
        loginCred_DBRef = FirebaseDatabase.getInstance().getReference().child("UsersCredentials");
    }

    private void getToPersonalData(){
        Query query = loginCred_DBRef.orderByChild("username").equalTo(myCredentials.getUsername());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getChildrenCount()==1) {
                    DataSnapshot snap = snapshot.getChildren().iterator().next();
                    String nodId = snap.getKey();
                    String passwd = (String) snap.child("password").getValue().toString();
                    String name = (String) snap.child("name").getValue().toString();
                    String surname = (String) snap.child("surname").getValue().toString();
                    String birthDate = (String) snap.child("birthDate").getValue().toString();
                    String registerDate = (String) snap.child("date_ofRegister").getValue().toString();
                    String gender = (String) snap.child("gender").getValue().toString();

                    int left_gestures = Integer.parseInt((String) snap.child("left_GESTURE").getValue().toString());
                    int right_gestures = Integer.parseInt((String) snap.child("right_GESTURE").getValue().toString());
                    int up_gestures = Integer.parseInt((String) snap.child("up_GESTURE").getValue().toString());
                    int down_gestures = Integer.parseInt((String) snap.child("down_GESTURE").getValue().toString());
                    int number_ofGestures = Integer.parseInt((String) snap.child("number_ofGestures").getValue().toString());

                    UserCredentials credentialsToSend = new UserCredentials(myCredentials.getUsername(),passwd,birthDate,gender,name,surname,number_ofGestures,
                            registerDate, up_gestures,down_gestures,left_gestures,right_gestures);

                    Intent intent = new Intent(getApplicationContext(),PersonalDataActivity.class);
                    intent.putExtra("userData",credentialsToSend);
                    startActivity(intent);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUpdatedCredentials(){
        Query query = loginCred_DBRef.orderByChild("username").equalTo(myCredentials.getUsername());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getChildrenCount()==1) {
                    DataSnapshot snap = snapshot.getChildren().iterator().next();
                    String nodId = snap.getKey();
                    int number_ofGestures = Integer.parseInt((String) snap.child("number_ofGestures").getValue().toString());
                    int left_gestures = Integer.parseInt((String) snap.child("left_GESTURE").getValue().toString());
                    int right_gestures = Integer.parseInt((String) snap.child("right_GESTURE").getValue().toString());
                    int up_gestures = Integer.parseInt((String) snap.child("up_GESTURE").getValue().toString());
                    int down_gestures = Integer.parseInt((String) snap.child("down_GESTURE").getValue().toString());

                    myCredentials.setNumber_ofGestures(number_ofGestures);
                    myCredentials.setUP_GESTURE(up_gestures);
                    myCredentials.setDOWN_GESTURE(down_gestures);
                    myCredentials.setLEFT_GESTURE(left_gestures);
                    myCredentials.setRIGHT_GESTURE(right_gestures);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.personalData:
                getToPersonalData();
                break;
            case R.id.changePassword:
                getToChangePassword();
                break;
            case R.id.mapsButton:
                getToGoogleMaps();
                return true;
            case R.id.logoutButton:
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void getToGoogleMaps() {
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);

    }

    private void getToChangePassword(){
        Intent intent = new Intent(getApplicationContext(),ChangePasswordActivity.class);
        intent.putExtra("username",myCredentials.getUsername());
        intent.putExtra("password", myCredentials.getPassword());
        startActivity(intent);

    }

    public void goBackToMainMenu(View view) {

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void goTakePhoto(View view) {

        getUpdatedCredentials();
        Intent intent = new Intent(getApplicationContext(),TakePhotoActivity.class);
        intent.putExtra("username",myCredentials.getUsername());
        intent.putExtra("gestureNO", String.valueOf(myCredentials.getNumber_ofGestures()));
        intent.putExtra("UP_GESTURE", String.valueOf(myCredentials.getUP_GESTURE()));
        intent.putExtra("DOWN_GESTURE", String.valueOf(myCredentials.getDOWN_GESTURE()));
        intent.putExtra("LEFT_GESTURE", String.valueOf(myCredentials.getLEFT_GESTURE()));
        intent.putExtra("RIGHT_GESTURE", String.valueOf(myCredentials.getRIGHT_GESTURE()));

        startActivity(intent);

    }


    public void goSeeImages(View view) {

        getUpdatedCredentials();
        Intent intent = new Intent(getApplicationContext(),MyGesturesActivity.class);
        intent.putExtra("username",myCredentials.getUsername());
        intent.putExtra("gestureNO", String.valueOf(myCredentials.getNumber_ofGestures()));
        intent.putExtra("UP_GESTURE", String.valueOf(myCredentials.getUP_GESTURE()));
        intent.putExtra("DOWN_GESTURE", String.valueOf(myCredentials.getDOWN_GESTURE()));
        intent.putExtra("LEFT_GESTURE", String.valueOf(myCredentials.getLEFT_GESTURE()));
        intent.putExtra("RIGHT_GESTURE", String.valueOf(myCredentials.getRIGHT_GESTURE()));
        startActivity(intent);

    }

    public void goSeeGestureStats(View view) {

        getUpdatedCredentials();
        SharedPreferences sp = getSharedPreferences("UsersGestureNumbers", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String up = String.valueOf(myCredentials.getUP_GESTURE());
        String down = String.valueOf(myCredentials.getDOWN_GESTURE());
        String left = String.valueOf(myCredentials.getLEFT_GESTURE());
        String right = String.valueOf(myCredentials.getRIGHT_GESTURE());


        editor.putString("UP", up);
        editor.putString("DOWN", down);
        editor.putString("LEFT", left);
        editor.putString("RIGHT", right);
        editor.commit();

        Intent intent = new Intent(getApplicationContext(),StatisticsActivity.class);
        startActivity(intent);
    }
}