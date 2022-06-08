package com.example.handgestureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {

    private String username;
    private String password;

    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle bundle = getIntent().getExtras();

        username = bundle.getString("username");
        password = bundle.getString("password");

        dbRef = FirebaseDatabase.getInstance().getReference().child("UsersCredentials");

    }

    public void resetUsersPassword(View view) {

        EditText currentPassET = findViewById(R.id.currentPassword);
        EditText newPassET = findViewById(R.id.newPasswordToChange);
        EditText repeated_newPassET = findViewById(R.id.newPasswordRepeatedToChange);

        password = currentPassET.getText().toString();
        String newPass = newPassET.getText().toString();
        String repeated_newPass = repeated_newPassET.getText().toString();

        Query query = dbRef.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getChildrenCount()==1) {
                    DataSnapshot snap = snapshot.getChildren().iterator().next();
                    String nodId = snap.getKey();
                    String passwd = (String) snap.child("password").getValue();

                    if(passwd.equals(password)){
                        if(repeated_newPass.equals(newPass) && !newPass.isEmpty()){
                            dbRef.child(nodId).child("password").setValue(newPass);
                            Toast.makeText(getApplicationContext(), "Password has been changed!", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else
                            Toast.makeText(getApplicationContext(), "Parolele nu se potrivesc!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Nu ati introdus corect parola actuala!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
}