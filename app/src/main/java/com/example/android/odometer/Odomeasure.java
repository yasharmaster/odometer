package com.example.android.odometer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Odomeasure extends AppCompatActivity {

    private OdometerService odometer;
    private boolean bound = false;
    private boolean running = false;
    private boolean locked = false;
    Button startBtn;
    Button stopBtn;
    TextView distanceV;

    static double distance=0.0;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            OdometerService.OdometerBinder odometerBinder =
                    (OdometerService.OdometerBinder) binder;
            odometer = odometerBinder.getOdometer();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, OdometerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }

    private void watchMileage() {
        final TextView distanceView = (TextView) findViewById(R.id.distance);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                     distance = 0.0;    //Tweek here
                    if (odometer != null) {
                        distance = odometer.getMiles();
                    }
                    String distanceStr = String.format("%1$,.2f", distance);
                    distanceView.setText(distanceStr);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odomeasure);
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button)findViewById(R.id.stopBtn);
        stopBtn.setEnabled(false);
        distanceV = (TextView) findViewById(R.id.distance);
    }

    public void startRun(View view) {
        running = true;
        watchMileage();
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        locked = true;
    }


    public void stopRun(View view) {
        if (locked) {
            Toast.makeText(this, "First Unlock The Buttons", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedpref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpref.edit();

            editor.putString("distanceCovered", distanceV.getText().toString());
            editor.apply();
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);

            OdometerService.lastLocation = null;
            OdometerService.distanceInMeters=0.0;
            distance=0.0;
            finish();
        }
    }

    public void lockRun(View view) {
        Button lockBtn = (Button) findViewById(R.id.lockBtn);
        if (locked) {
            lockBtn.setBackgroundResource(R.drawable.ic_unlock);
            locked = false;
        } else {
            if(!running){
                Toast.makeText(this, "Press Start button first.", Toast.LENGTH_SHORT).show();
            }
            else {
                lockBtn.setBackgroundResource(R.drawable.ic_lock);
                locked = true;
            }
        }
    }
}
