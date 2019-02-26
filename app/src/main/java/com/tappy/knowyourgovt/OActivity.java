package com.tappy.knowyourgovt;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.R.drawable.ic_dialog_alert;

public class OActivity extends AppCompatActivity {


    Official offset = new Official();
    private TextView location;
    private TextView design;
    private TextView oName;
    private TextView party;
    private TextView ad1;
    private TextView ad2;
    private TextView ad3;
    private TextView phone;
    private TextView eMail;
    private TextView web;
    private ImageButton gplusImgButton;
    private ImageButton fbImgButton;
    private ImageButton ytImgButton;
    private ImageButton tweetImgButton;
    private ImageView smimgview;
    private String showadd = null;

    private static final int NEW = 1;

    HashMap<String, String> hashMapChannels = new HashMap<String, String>();

    private static final String TAG = "OActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.officialactivity);

        ActionBar actionBar=getSupportActionBar();
        Intent intent = getIntent();

        offset = (Official) intent.getSerializableExtra("officialTobeSent");
        showadd = (String) intent.getSerializableExtra("location");
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintId);


        if (offset.getParty().equals("Republican")) {
            constraintLayout.setBackgroundColor(Color.RED);
        } else if (offset.getParty().equals("Democratic")) {
            constraintLayout.setBackgroundColor(Color.BLUE);
        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
        }

        if (offset != null) {

            location = (TextView) findViewById(R.id.location);
            location.setBackgroundColor(getResources().getColor(R.color.back_purple));
            location.setText(showadd);

            design = (TextView) findViewById(R.id.designation);
            design.setText(offset.getoDesig());

            oName = (TextView) findViewById(R.id.nOfficial);
            oName.setText(offset.getoName());

            party = (TextView) findViewById(R.id.party);
            party.setText("(" + offset.getParty() + ")");

            ad1 = (TextView) findViewById(R.id.ad1);
            ad1.setText(offset.getadl1() + " " + offset.getadl2() + " " + offset.getCity() +
                    ", " + offset.getstate() + " " + offset.getZipCode());
            ad1.setLinkTextColor(Color.GREEN);

            Linkify.addLinks(((TextView) findViewById(R.id.ad1)), Linkify.MAP_ADDRESSES);

            phone = (TextView) findViewById(R.id.phone);
            phone.setText(offset.getPhone());
            phone.setLinkTextColor(Color.GREEN);
            Linkify.addLinks(((TextView) findViewById(R.id.phone)), Linkify.PHONE_NUMBERS);


            eMail = (TextView) findViewById(R.id.email);
            if (offset.getEmail() == null) {
                eMail.setText("No Email Provided");
            } else {
                eMail.setText(offset.getEmail());
                eMail.setLinkTextColor(Color.GREEN);
                Linkify.addLinks(((TextView) findViewById(R.id.email)), Linkify.EMAIL_ADDRESSES);
            }
            web = (TextView) findViewById(R.id.web);
            if (offset.getweb() == null) {
                web.setText("No Website Exist");
            } else {
                web.setText(offset.getweb());
                web.setLinkTextColor(Color.GREEN);
                Linkify.addLinks(((TextView) findViewById(R.id.web)), Linkify.WEB_URLS);
            }


            gplusImgButton = (ImageButton) findViewById(R.id.gplus);
            fbImgButton = (ImageButton) findViewById(R.id.fb);
            ytImgButton = (ImageButton) findViewById(R.id.yt);
            tweetImgButton = (ImageButton) findViewById(R.id.tweet);

            hashMapChannels = offset.getchannel();

            try {
                if (hashMapChannels.get("GooglePlus").equals("898989")) {
                    gplusImgButton.setVisibility(View.INVISIBLE);
                }

                if (hashMapChannels.get("Facebook").equals("898989")) {
                    fbImgButton.setVisibility(View.INVISIBLE);
                }

                if (hashMapChannels.get("Twitter").equals("898989")) {
                    tweetImgButton.setVisibility(View.INVISIBLE);
                }

                if (hashMapChannels.get("YouTube").equals("898989")) {
                    ytImgButton.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e){

            }

                int result = 0;
                result = networkCheckOnAddButton(this);
                if (result == 1) {
                    smimgview = (ImageView) findViewById(R.id.imageSmall);
                    smimgview.setImageResource(R.drawable.placeholder);
                } else {
                    if (!offset.getimgUrls().equals("NoPhoto")) {
                        smimgview = (ImageView) findViewById(R.id.imageSmall);
                        DownloadImage(offset.getimgUrls());
                    } else {
                        smimgview = (ImageView) findViewById(R.id.imageSmall);
                        smimgview.setImageResource(R.drawable.missingimage);
                    }
                }
                smimgview.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        openPhotoActivity(view);

                    }

                });


            }
        }


        @Override
        protected void onResume () {
            super.onResume();
        }


        @Override
        protected void onPause () {
            super.onPause();
        }

    public void openPhotoActivity(View view) {
        Intent photoActivity = new Intent(OActivity.this, PhotoActivity.class);
        photoActivity.putExtra("officialTobeSent", offset);
        photoActivity.putExtra("location", showadd);
        startActivityForResult(photoActivity, NEW);
        Toast.makeText(OActivity.this, "CLICKED ON IMAGE VIEW", Toast.LENGTH_LONG).show();

    }


    public int networkCheckOnAddButton(Context context) {

        int result = 0;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Toast.makeText(this, "Connected to the Internet!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            Intent data = getIntent();
            builder.setTitle("CHECK YOUR CONNECTION");
            builder.setIcon(ic_dialog_alert);
            builder.setMessage("NO CONNECTION == NO DATA, CHECK YOUR CONNECTION");
            AlertDialog dialog = builder.create();
            dialog.show();
            result = 1;

        }
        return result;
    }


    public void fbClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + offset.getchannel().get("Facebook");
        String urlToUse;

        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;

            } else {
                urlToUse = "fb://page/" + offset.getchannel().get("Facebook");
            }
        } catch (PackageManager.NameNotFoundException e) {

            urlToUse = FACEBOOK_URL;
        }
        Intent fbIntent = new Intent(Intent.ACTION_VIEW);
        fbIntent.setData(Uri.parse(urlToUse));
        startActivity(fbIntent);

    }

    public void tweetClicked(View v) {
        Intent tweetintent = null;
        String name = offset.getchannel().get("Twitter");
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            tweetintent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            tweetintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            tweetintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(tweetintent);
    }

    public void gPlusClicked(View v) {
        String name = offset.getchannel().get("GooglePlus");
        Intent gplusintent = null;
        try {
            gplusintent = new Intent(Intent.ACTION_VIEW);
            gplusintent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            gplusintent.putExtra("customAppUri", name);
            startActivity(gplusintent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void yTClicked(View v) {
        String name = offset.getchannel().get("YouTube");
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }


    private void DownloadImage(final String imageURL) {
        Log.d(TAG, "DownloadImage: " + imageURL);

        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d(TAG, "onImageLoadFailed: ");
                        picasso.load(R.drawable.brokenimage)
                                .into(smimgview);
                    }
                })
                .build();

        picasso.load(imageURL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(smimgview);
    }
}