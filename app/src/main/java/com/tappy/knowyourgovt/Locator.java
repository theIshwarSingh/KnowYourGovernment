package com.tappy.knowyourgovt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;
public class Locator {

    private MainActivity act_main;
    private LocationManager  manager;
    private LocationListener loclisten;
    public Locator(){};
    public Locator(MainActivity activity) {
        act_main = activity;

        if (checkPermission()) {
            setUpLocationManager();
            determineLocation();
        }
    }

    public void setUpLocationManager() {

        if (manager != null)
            return;

        if (!checkPermission())
            return;
        manager = (LocationManager) act_main.getSystemService(LOCATION_SERVICE);
        loclisten = new LocationListener() {
            public void onLocationChanged(Location location) {
                act_main.locatorData(location.getLatitude(), location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 0, loclisten);
    }

    public void shutdown() {
        manager.removeUpdates(loclisten);
        manager = null;
    }
    public void determineLocation() {

        if (!checkPermission())
            return;

        if (manager == null)
            setUpLocationManager();

        if (manager != null) {
            Location loc = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {
                double lat=loc.getLatitude();
                double longi=loc.getLongitude();

                act_main.locatorData(lat,longi);
                return;
            }
        }

        if (manager != null) {
            Location loc = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null) {
                act_main.locatorData(loc.getLatitude(), loc.getLongitude());
            }
        }

        if (manager != null) {
            Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                act_main.locatorData(loc.getLatitude(), loc.getLongitude());
                return;
            }
        }
        act_main.noLocationFound();
        return;
    }
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(act_main, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(act_main,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }
}
