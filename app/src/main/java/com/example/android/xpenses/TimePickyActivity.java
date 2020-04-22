package com.example.android.xpenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class TimePickyActivity extends Activity {

    ArrayList<Integer> singleDigit = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker_layout);
        singleDigit.add(0);
        singleDigit.add(1);
        singleDigit.add(2);
        singleDigit.add(3);
        singleDigit.add(4);
        singleDigit.add(5);
        singleDigit.add(6);
        singleDigit.add(7);
        singleDigit.add(8);
        singleDigit.add(9);


        Button okBtn = findViewById(R.id.ok_picker);
        Button cancelBtn = findViewById(R.id.cancel_picker);
        final TimePicker timePicker = findViewById(R.id.time_picker);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hourToSet = String.valueOf(timePicker.getCurrentHour());
                String minToSet = String.valueOf(timePicker.getCurrentMinute());
                minToSet = getIntToSet(minToSet);
                String ampm = "am";

                if(Integer.parseInt(hourToSet) == 0){
                    hourToSet = Integer.toString(12);
                }
                else if(Integer.parseInt(hourToSet) >= 12){
                    hourToSet = Integer.toString(Integer.parseInt(hourToSet) - 12);
                    if(Integer.parseInt(hourToSet) == 0) hourToSet = Integer.toString(12);
                    ampm = "pm";
                }

                hourToSet = getIntToSet(hourToSet);

                String timeString = hourToSet+" : "+minToSet+" "+ampm;

                Intent mainIntent = new Intent();
                mainIntent.putExtra("TIME_TO_SET", timeString);
                setResult(RESULT_OK, mainIntent);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getIntToSet(String intToSet) {
        String zero = "0";
        if(singleDigit.contains(Integer.parseInt(intToSet))){
            intToSet = zero + intToSet;
        }
        return intToSet;
    }
}
