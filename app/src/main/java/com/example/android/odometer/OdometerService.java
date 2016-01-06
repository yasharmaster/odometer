package com.example.android.odometer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class OdometerService extends Service {

    private final IBinder binder = new OdometerBinder();
    public static double distanceInMeters;
    public static Location lastLocation = null;

    public class OdometerBinder extends Binder {
        OdometerService getOdometer() {
            return OdometerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation == null) {
                    lastLocation = location;
                }
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location;
            }
            @Override
            public void onProviderDisabled(String arg0) {
                Toast.makeText(OdometerService.this, "Enable Location Reporting first.", Toast.LENGTH_SHORT).show();
                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }

            @Override
            public void onProviderEnabled(String arg0) {
                Toast.makeText(OdometerService.this,"Location services enabled.",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle bundle) {
            }
        };
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        try{
            locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER , 1000 , 1 , listener );
        }
        catch (SecurityException se){

        }
    }

    public double getMiles() {
        return this.distanceInMeters/1000;
    }

    public OdometerService() {

    }

}
