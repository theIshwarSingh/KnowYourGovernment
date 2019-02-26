package com.tappy.knowyourgovt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.R.drawable.ic_dialog_alert;


public class PhotoActivity extends AppCompatActivity {

    Official offs = new Official();
    String airshow = null;
    private TextView pLocation;
    private TextView pDesign;
    private TextView pName;
    private ImageView imageEnlarge;
    private String TAG = "PhotoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoact);
        ActionBar actionBar=getSupportActionBar();
        Intent intent = getIntent();

        offs = (Official)intent.getSerializableExtra("officialTobeSent");
        airshow = (String)intent.getSerializableExtra("location");
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.photoConst);

        if(offs.getParty().equals("Republican")) {
            constraintLayout.setBackgroundColor(Color.RED);
        }
        else if(offs.getParty().equals("Democratic"))
        {
            constraintLayout.setBackgroundColor(Color.BLUE);
        }
        else
        {
            constraintLayout.setBackgroundColor(Color.BLACK);
        }


        if(offs != null) {

            pLocation = (TextView)findViewById(R.id.pLocation);
            pLocation.setBackgroundColor(getResources().getColor(R.color.back_purple));
            pLocation.setText(airshow);


            pDesign = (TextView)findViewById(R.id.pDesig);
            pDesign.setText(offs.getoDesig());

            pName = (TextView) findViewById(R.id.pName);
            pName.setText(offs.getoName());

        }


        int result = 0;
        result = networkCheckOnAddButton(this);

        if(result == 1)
        {
            imageEnlarge = (ImageView)findViewById(R.id.imageEnlarge);
            imageEnlarge.setImageResource(R.drawable.placeholder);

        }
        else {

            if (!offs.getimgUrls().equals("NoPhoto")) {
                imageEnlarge = (ImageView) findViewById(R.id.imageEnlarge);
                ImageDownloader(offs.getimgUrls());
            } else {
                imageEnlarge = (ImageView) findViewById(R.id.imageEnlarge);
                imageEnlarge.setImageResource(R.drawable.missingimage);
            }

        }




    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    @Override
    protected  void onPause(){
        super.onPause();
    }

    public int networkCheckOnAddButton(Context context) {

        int result = 0;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Toast.makeText(this, "CONNECTED TO INTERNET", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Intent data = getIntent();
            builder.setTitle("CHECK CONNECTION");
            builder.setIcon(ic_dialog_alert);
            builder.setMessage("NO CONNECTION = NO DATA");
            AlertDialog dialog = builder.create();
            dialog.show();
            result = 1;

        }
        return result;
    }

    private void ImageDownloader(final String urlImg) {

        Log.d(TAG, "ImageDownloader: " + urlImg);

        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d(TAG, "onImageLoadFailed: ");
                        picasso.load(R.drawable.brokenimage)
                                .into(imageEnlarge);
                    }
                })
                .build();

        picasso.load(urlImg)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(imageEnlarge);
    }
}
