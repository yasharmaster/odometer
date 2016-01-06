package com.example.android.odometer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    Intent intentSettings;
    TextView welcomeHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences sharedpref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String nameString = sharedpref.getString("usernameText","");
        welcomeHome = (TextView)findViewById(R.id.welcomeHome);
        welcomeHome.setText("Welcome "+ nameString + "!");
        intentSettings = new Intent(this,SettingsActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        else if (id == R.id.action_about) {
            Intent intentAbout = new Intent(this, AboutActivity.class);
            startActivity(intentAbout);
            return true;
        }
        if (id == R.id.action_exit) {
            finish();
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goForRun(View view){
        Intent i = new Intent(this,Odomeasure.class);
        SharedPreferences sharedpref = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        if(sharedpref.getString("usernameText","").isEmpty()||sharedpref.getString("weightText","").isEmpty()){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:
                        startActivity(intentSettings);
                            finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to fill in your details first. Do you want to fill now?").setPositiveButton("Yes",dialogClickListener)
                    .setNegativeButton("No",dialogClickListener).show();
        }
        else {
            startActivity(i);
        }
    }

}
