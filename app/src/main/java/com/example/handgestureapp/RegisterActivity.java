package com.example.handgestureapp;

import static java.util.List.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;


    private String genderChoice = null;

    private DatabaseReference registerCred_DBRef;

    private UserCredentials myCredentials = null;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View credentialView = findViewById(R.id.credentialView);
//        credentialView.setBackgroundDrawable(R.drawable.custom_rectangle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // For the database
        registerCred_DBRef = FirebaseDatabase.getInstance().getReference().child("UsersCredentials");

        // For date Picker
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());


        // For Gender Picker
        Spinner spinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gendersENG, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day,month,year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }

        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year){

        return getMonthFormat(month) + " " + day + " " + year;

    }

    private String getMonthFormat(int month){

        String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
        return months[month];

    }

    public void openDatePicker (View view){

        datePickerDialog.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(position).toString();
        genderChoice = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean getInsertedCredentials(){
        EditText username = findViewById(R.id.usernameText);
        EditText password = findViewById(R.id.passwordText);
        EditText repeatedPass = findViewById(R.id.repeatedPassword);

        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String repeatedString = repeatedPass.getText().toString();
        String birthDate = dateButton.getText().toString();

        Spinner spinner = findViewById(R.id.genderSpinner);
        String genderChoice = spinner.getSelectedItem().toString();

        if(usernameString.isEmpty())
            username.setError("Introduceti numele!");
        if(passwordString.isEmpty())
            password.setError("Introduceti parola!");
        if(repeatedString.isEmpty())
            repeatedPass.setError("Repetati parola!");

        if(!repeatedString.equals(passwordString))
            repeatedPass.setError("Parolele nu coincid!");

        myCredentials = null;

        if(passwordString.equals(repeatedString) && !passwordString.isEmpty()){
            if (!usernameString.isEmpty()) {
                myCredentials = new UserCredentials(usernameString,passwordString,birthDate,genderChoice,"","",0,
                        getTodaysDate(), 0, 0, 0, 0);
                return true;
            }
        }

        return false;
    }

    public void sendForConfirmation(View view) {

        if(getInsertedCredentials()){
            Query query = registerCred_DBRef.orderByChild("username").equalTo(myCredentials.getUsername());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.getChildrenCount() >= 1){
                        Toast.makeText(getApplicationContext(), "Username-ul exista deja!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        registerCred_DBRef.push().setValue(myCredentials);
                        Toast.makeText(getApplicationContext(), "Signup was successful! Welcome, " + myCredentials.getUsername(), Toast.LENGTH_SHORT).show();
                        goToLoginActivity();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void goToLoginActivity(){

        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.putExtra("username",myCredentials.getUsername());
        intent.putExtra("password",myCredentials.getPassword());
        intent.putExtra("CODE",100);
        startActivity(intent);
        finish();
    }

}