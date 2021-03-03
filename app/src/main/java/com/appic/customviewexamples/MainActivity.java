package com.appic.customviewexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.appic.customtimepicker.TimeSelector;

public class MainActivity extends AppCompatActivity {

    TextView tvHour;
    TextView tvMin;
    TimeSelector timeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvHour=findViewById(R.id.tvHour);
        tvMin=findViewById(R.id.tvMin);

        timeSelector=findViewById(R.id.timeSelctor);
        timeSelector.setItemSelectedListener(new TimeSelector.onTimeSelectionListener() {
            @Override
            public void onHourTimeSelected(String hour) {
                tvHour.setText(hour);
            }

            @Override
            public void onMinuteTimeSelected(String minute) {
                tvMin.setText(minute);

            }
        });

    }


    public void launchPlayStore(Context context, String packageName) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }
}