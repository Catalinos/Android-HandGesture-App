package com.example.handgestureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.handgestureapp.databinding.ActivityMainBinding;
import com.example.handgestureapp.databinding.ActivityMyGesturesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyGesturesActivity extends AppCompatActivity {

    ActivityMyGesturesBinding binding;
    StorageReference storageREF;
    String username;
    ArrayList<Bitmap> myBitmapList;
    ArrayList<String> myGestureNames;
    ArrayList<GestureData> gestureDataArrayList;

    int gestureNumber;
    int whenToStop = 0;
    ProgressDialog  myprogdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gestures);

        Bundle bundle = getIntent().getExtras();

        username = bundle.getString("username");
        String up = bundle.getString("UP_GESTURE");
        String down = bundle.getString("DOWN_GESTURE");
        String right = bundle.getString("RIGHT_GESTURE");
        String left = bundle.getString("LEFT_GESTURE");
        String gestureNO = bundle.getString("gestureNO");
        gestureNumber = Integer.parseInt(gestureNO);

        gestureDataArrayList = new ArrayList<>();
        myBitmapList = new ArrayList<Bitmap>();
        myGestureNames = new ArrayList<String>();
        myprogdialog= new ProgressDialog(MyGesturesActivity.this);
        myprogdialog.setMessage("Fetching all images");
        myprogdialog.setCancelable(false);
        myprogdialog.show();


        fetchImages("UP",up);
        fetchImages("DOWN",down);
        fetchImages("LEFT",left);
        fetchImages("RIGHT",right);



    }

    private boolean fetchImages(String gestureName, String number_ofGestures){
        int numbers = Integer.parseInt(number_ofGestures);

        if(numbers >= 1){
            for(int i = 1; i<=numbers; ++i){
                String filepath = username + "/" + gestureName + "_" + String.valueOf(i) + ".jpg";
                Toast.makeText(getApplicationContext(), filepath, Toast.LENGTH_SHORT).show();
                storageREF = FirebaseStorage.getInstance().getReference(filepath);

                try {
                    File localfile = File.createTempFile("tempfile", ".jpg");
                    storageREF.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            myBitmapList.add(bitmap);
                            String title = "Gesture named defined is: " + gestureName;
                            myGestureNames.add(title);
                            whenToStop ++;
                            if(gestureNumber == whenToStop){
                                myprogdialog.dismiss();
                                ListAdapter myCustom = new ListAdapter(getApplicationContext(),myBitmapList,myGestureNames);
                                ListView listView = (ListView) findViewById(R.id.gestureListView);
                                listView.setAdapter(myCustom);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        else{
            myprogdialog.dismiss();
        }
        return true;
    }

    public void goBackToOurMenu(View view) {
    }
}