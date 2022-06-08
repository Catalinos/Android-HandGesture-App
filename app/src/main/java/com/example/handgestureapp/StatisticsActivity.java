package com.example.handgestureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;
import com.anychart.anychart.Pie;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        SharedPreferences sp = getSharedPreferences("UsersGestureNumbers", Context.MODE_PRIVATE);

        int up = Integer.parseInt(sp.getString("UP",""));
        int down = Integer.parseInt(sp.getString("DOWN",""));
        int left = Integer.parseInt(sp.getString("LEFT",""));
        int right = Integer.parseInt(sp.getString("RIGHT",""));

        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Up gestures", up));
        data.add(new ValueDataEntry("Down gestures", down));
        data.add(new ValueDataEntry("Left gestures", left));
        data.add(new ValueDataEntry("Right gestures", right));

        pie.data(data);

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }
}