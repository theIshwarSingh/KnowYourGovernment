package com.tappy.knowyourgovt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import static android.R.drawable.ic_dialog_alert;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<Official> Olist = new ArrayList<Official>();
    private RecyclerView reView;
    private OAdapter Adapter;
    private Official official;
    String pincode = "";
    private static final String TAG = "MainActivity";
    private static final int NEW = 1;
    private static final int EDIT = 2;
    private Locator locator;
    private String location = null;
    Official officialTobeSent;
    private Intent intent;
    static boolean locFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reView = (RecyclerView) findViewById(R.id.reView);
        Adapter = new OAdapter(Olist, this);
        reView.setAdapter(Adapter);
        reView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reView.setHasFixedSize(true);

        int result = 0;
        result = networkCheckOnAddButton(this);

        if (result != 1) {
            locator = new Locator(this);
        }

    }

    private String dolocationWork(double latit, double longit) {
        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latit, longit, 1);
                StringBuilder sb = new StringBuilder();
                for (Address ad : addresses) {

                    for (int i = 0; i < 1; i++)
                        sb.append(ad.getAddressLine(1));
                }
                return sb.toString();
            } catch (IOException e) {
                return null;
            }
        }
        Toast.makeText(this, "TRY AGAIN", Toast.LENGTH_LONG).show();
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.aboutme: {
                Toast.makeText(MainActivity.this, "About Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivityForResult(intent, NEW);
                return true;
            }

            case R.id.sbar: {
                CivicDownloader(this);
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9) {
           // Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location can't be determine - PERMISSION DENIED", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }

    public void setList(HashMap<String, ArrayList<Official>> maplist) {

        HashMap<String, ArrayList<Official>> objlist = new HashMap<String, ArrayList<Official>>();

        objlist = maplist;
        Olist.clear();
        for (Map.Entry<String, ArrayList<Official>> map : objlist.entrySet()) {
            location = map.getKey();
            ((TextView) findViewById(R.id.postalAdd)).setText(map.getKey());
            Olist.addAll(map.getValue());
        }
        Adapter.notifyDataSetChanged();
    }


    public int networkCheckOnAddButton(Context context) {

        int result = 0;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Intent data = getIntent();
            builder.setTitle("No Network Connection");
            builder.setIcon(ic_dialog_alert);
            builder.setMessage("Please Connect to the Internet");
            AlertDialog dialog = builder.create();
            dialog.show();
            result = 1;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        int pos = reView.getChildLayoutPosition(v);
        officialTobeSent = Olist.get(pos);
        Intent intent = new Intent(MainActivity.this, OActivity.class);
        intent.putExtra("officialTobeSent", officialTobeSent);
        intent.putExtra("officialPosition", pos);
        intent.putExtra("location", location);
        startActivityForResult(intent, NEW);
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, " Long Click View Used", Toast.LENGTH_LONG).show();
        return false;
    }


    public void locatorData(double lati, double longi) {

      //  Log.d(TAG, "setData: Lat: " + lati + ", Lon: " + longi);
        String address = dolocationWork(lati, longi);
        StringTokenizer stringTokenizer = new StringTokenizer(address, " ");
        String pinCode = null;
        while (stringTokenizer.hasMoreTokens()) {
            pincode = stringTokenizer.nextToken();
        }
        ((TextView) findViewById(R.id.postalAdd)).setText(address);
        official = new Official();
        official.setZipCode(pincode);
        new CivicInfoDownloader(MainActivity.this).execute(official);
    }





    public void noLocationFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent data = getIntent();
        builder.setTitle("PLEASE ENABLE LOCATION SERVICES");
        builder.setMessage("ENABLE LOCATION SERVICES FOR A PRECISE SEARCH ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent noloc = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(noloc);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog location = builder.create();
        location.show();
    }


    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }

    public void CivicDownloader(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Intent data = getIntent();
        builder.setTitle("Search For Location");
        builder.setMessage("Enter a City, State or a Zip Code:");
        final EditText zipCode = new EditText(this);
        zipCode.setInputType(InputType.TYPE_CLASS_TEXT);
        zipCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        zipCode.setGravity(Gravity.CENTER);
        builder.setView(zipCode);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                official = new Official();
                pincode = zipCode.getText().toString();
                official.setZipCode(pincode);
                new CivicInfoDownloader(MainActivity.this).execute(official);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog download = builder.create();
        download.show();
    }

}
