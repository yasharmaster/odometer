package com.example.android.odometer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        SharedPreferences sharedpref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String str = sharedpref.getString("distanceCovered", "");
        String weightStr = sharedpref.getString("weightText", "");
        double weight = Double.parseDouble(weightStr);
        resultText = (TextView)findViewById(R.id.resultText);
        double cal = Double.parseDouble(str);
        cal = 0.75*weight*cal*(1.0/1.6)*(1.0/0.453);
        String calorie = String.format("%1$,.2f", cal);
        resultText.setText("Congratulations! You have burned "+calorie+" Cals while running "+ str+" kms.");
    }

    public void newRun(View view){
        finish();
    }
}
