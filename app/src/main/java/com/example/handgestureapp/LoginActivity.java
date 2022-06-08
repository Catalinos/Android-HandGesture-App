package com.example.handgestureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    private String username = null;
    private String password = null;

    private EditText usernameEdit = null;
    private EditText passwordEdit = null;

    private DatabaseReference loginCred_DBRef;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();


        int CODE = bundle.getInt("CODE");

        usernameEdit = findViewById(R.id.usernameLoginText);
        passwordEdit = findViewById(R.id.passwordLoginText);

        sp = getSharedPreferences("UserCredentialsPreferenes", Context.MODE_PRIVATE);

        String usernameShared = sp.getString("username","");
        String passwordShared = sp.getString("password","");

        if(CODE == 100) {

            username = bundle.getString("username");
            password = bundle.getString("password");

            usernameEdit.setText(username);
            passwordEdit.setText(password);
        }
        else{
            usernameEdit.setText(usernameShared);
            passwordEdit.setText(passwordShared);
        }

        loginCred_DBRef = FirebaseDatabase.getInstance().getReference().child("UsersCredentials");

    }


    public void sendForConfirmationLogin(View view) {

        username = usernameEdit.getText().toString();
        password = passwordEdit.getText().toString();

        if(username.isEmpty())
            usernameEdit.setError("Introduceti username-ul!");

        if(password.isEmpty())
            passwordEdit.setError("Introduceti parola!");

        if(!password.isEmpty() && !username.isEmpty()){
            Query query = loginCred_DBRef.orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
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

                        UserCredentials credentialsToSend = new UserCredentials(username,passwd,birthDate,gender,name,surname,number_ofGestures,
                                registerDate, up_gestures,down_gestures,left_gestures,right_gestures);

                        if(passwd.equals(password)){

                            CheckBox checkedSaved = findViewById(R.id.keepDataSavedCheckBox);
                            if(checkedSaved.isChecked()){
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username",username);
                                editor.putString("password",passwd);
                                editor.commit();
                            }

                            Toast.makeText(getApplicationContext(),"Welcome, " + username, Toast.LENGTH_SHORT);
                            Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
                            intent.putExtra("usersCredentials",credentialsToSend);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Numele sau parola sunt gresite!", Toast.LENGTH_SHORT);
                            usernameEdit.setError("");
                            passwordEdit.setError("");
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }
}