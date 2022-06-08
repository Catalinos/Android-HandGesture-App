package com.example.handgestureapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Bitmap> bitmaps;
    ArrayList<String> gestureNames;
    LayoutInflater inflater;


    public ListAdapter(Context context, ArrayList<Bitmap> bitmaps, ArrayList<String> gestureNames){
        this.context = context;
        this.bitmaps = bitmaps;
        this.gestureNames = gestureNames;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gestureNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.gesture_list,null);
        TextView txtView = (TextView) view.findViewById(R.id.gestureName);
        ImageView imgView = (ImageView) view.findViewById(R.id.gesturePhoto);
        txtView.setText(gestureNames.get(i));
        imgView.setImageBitmap(bitmaps.get(i));
        return view;
    }
}
