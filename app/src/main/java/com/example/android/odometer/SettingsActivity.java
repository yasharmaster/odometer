package com.example.android.odometer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    TextView username;
    TextView weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedpref = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String nameString = sharedpref.getString("usernameText", "");
        String weightString = sharedpref.getString("weightText","");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        username =(TextView)findViewById(R.id.username);
        weight = (TextView)findViewById(R.id.weight);
        username.setText(nameString);
        weight.setText(weightString);
    }

    public void usernameInput(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nme = input.getText().toString();
                username.setText(nme);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void weightInput(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your weight in kilograms.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameString = input.getText().toString();
                weight.setText(nameString);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void saveInfo(View view) {
        String regex = "\\d+\\.\\d+" +"|"+"\\d*" ;
        String regex2 = "[0]+"+"|"+"[0]+\\.[0]+";
        SharedPreferences sharedpref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();

        if ( weight.getText().toString().isEmpty() ||
                weight.getText().toString().charAt(0) == '-' ||
                !(weight.getText().toString().matches(regex)) ||
                weight.getText().toString().matches(regex2) ) {
            Toast.makeText(this, "Please enter valid weight.", Toast.LENGTH_SHORT).show();

        }

        else if(Double.parseDouble(weight.getText().toString()) < 1 ||
                Double.parseDouble(weight.getText().toString()) > 500){
            Toast.makeText(this, "Weight must be between 1 and 500 kilograms.", Toast.LENGTH_SHORT).show();
        }
        else if (username.getText().length() > 10 ||
                username.getText().length() < 3){
            Toast.makeText(this, "Username must be between 3 to 12 characters.", Toast.LENGTH_SHORT).show();
        }
        else {
            editor.putString("usernameText", username.getText().toString());
            editor.putString("weightText", weight.getText().toString());
            editor.apply();
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void goBack(View view){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
