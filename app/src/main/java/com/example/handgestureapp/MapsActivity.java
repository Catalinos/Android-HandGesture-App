package com.example.handgestureapp;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.handgestureapp.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String buffer;
    StorageReference storageREF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storageREF = FirebaseStorage.getInstance().getReference("locations/mapsLocations.json");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





    }

    private void addMarkers(GoogleMap mMap, double latitude, double longitude, String markerTitle){
        LatLng MTA = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(MTA).title(markerTitle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MTA));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageREF.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    String path = localfile.getPath();
                    try {
                        FileReader fileReader = new FileReader(path);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        while((buffer = bufferedReader.readLine())!=null){
                            stringBuilder.append(buffer);
                        }
                        buffer = stringBuilder.toString();
                        try {
                            JSONObject mainObject = new JSONObject(buffer);
                            JSONArray mapsLocations = mainObject.getJSONArray("MapsLocations");

                            for(int i=0;i<mapsLocations.length(); ++i){
                                JSONObject object = mapsLocations.getJSONObject(i);
                                String markerName = object.getString("MarkerName");
                                double latitude = object.getDouble("Latitude");
                                double longitude = object.getDouble("Longitude");
                                addMarkers(mMap,latitude,longitude,markerName);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}