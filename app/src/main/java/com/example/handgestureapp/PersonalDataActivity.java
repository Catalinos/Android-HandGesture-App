package com.example.handgestureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

public class PersonalDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    UserCredentials myCredentials;

    String genderChoice;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        myCredentials = (UserCredentials) getIntent().getSerializableExtra("userData");

        reference = FirebaseDatabase.getInstance().getReference().child("UsersCredentials");
        Query query = reference.orderByChild("username").equalTo(myCredentials.getUsername());
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

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        initializeUsersData();


    }

    private void initializeUsersData() {
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButtonData);
        dateButton.setText(myCredentials.getBirthDate());

        EditText editSurname = findViewById(R.id.surnameData);
        EditText editName = findViewById(R.id.nameDataText);

        TextView gestureNO = findViewById(R.id.numberOfGesture);
        TextView regDate = findViewById(R.id.regDateView);

        editSurname.setText(myCredentials.getSurname());
        editName.setText(myCredentials.getName());

        gestureNO.setText(String.valueOf(myCredentials.getNumber_ofGestures()));
        regDate.setText(myCredentials.getDate_ofRegister());

        Spinner spinner = findViewById(R.id.genderSpinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gendersENG, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        String gender = myCredentials.getGender();
        if (gender.equals("Male"))
            spinner.setSelection(0);
        if (gender.equals("Female"))
            spinner.setSelection(1);
        if (gender.equals("None"))
            spinner.setSelection(2);

    }

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
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

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {

        return getMonthFormat(month) + " " + day + " " + year;

    }

    private String getMonthFormat(int month) {

        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return months[month];

    }

    public void openDatePicker(View view) {

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

    private boolean checkForModifications() {
        EditText editSurname = findViewById(R.id.surnameData);
        EditText editName = findViewById(R.id.nameDataText);
        Spinner spinner = findViewById(R.id.genderSpinner2);

        String newDate = dateButton.getText().toString();
        String newGender = spinner.getSelectedItem().toString();
        String newName = editName.getText().toString();
        String newSurname = editSurname.getText().toString();

        boolean isModified = false;

        if (!myCredentials.getName().equals(newName)) {
            myCredentials.setName(newName);
            isModified = true;
        }

        if (!myCredentials.getSurname().equals(newSurname)) {
            myCredentials.setSurname(newSurname);
            isModified = true;
        }
        if (!myCredentials.getBirthDate().equals(newDate)) {
            myCredentials.setBirthDate(newDate);
            isModified = true;
        }
        if (!myCredentials.getGender().equals(newGender)) {
            myCredentials.setGender(newGender);
            isModified = true;
        }
        return isModified;
    }

    public void updateDataToDB(View view) {

        if (!checkForModifications()) {
            Toast.makeText(getApplicationContext(), "Nothing to update", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Query query = reference.orderByChild("username").equalTo(myCredentials.getUsername());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DataSnapshot snap = snapshot.getChildren().iterator().next();
                    String usersID = snap.getKey();
                    reference.child(usersID).setValue(myCredentials);
                    Toast.makeText(getApplicationContext(), "Your personal data was updated", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}