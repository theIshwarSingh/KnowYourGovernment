package com.tappy.knowyourgovt;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;



public class CivicInfoDownloader extends AsyncTask<Official, Void, String> {

    MainActivity act_main;

    Official official;
    private int count;


    private String apiurl = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private String search = "&address=";
    private String apiKey = "AIzaSyBAn4ZZaVkqZX5LVwzXXQscfAuoMmSW-e4";
    private String dataUrlMore = apiurl+apiKey+search;

    private static final String TAG="CivicInfoDownloader";

    String adLine1 = null, adLine2 = null;
    String city = null, state = null, zip= null, type1 = null;
    String  id1 = null, photo = null, channeltype = null, channelid = null;
    String urlsForObject = null;
    String address1 = null, address2 = null, address3 = null, address4 = null, address5 = null;
    String posname= null;
    String officerName = null, address = null, phone = null, email = null, offParty = null;
    String locationName;
    String channelForObject;
    JSONArray ja = null;
    JSONObject jobj = null;
    ArrayList<Official> offialDataList =  new ArrayList<Official>();


    HashMap<String, ArrayList<Official>> objectList = new HashMap<String, ArrayList<Official>>();

    public CivicInfoDownloader(MainActivity ma) {
        act_main = ma;
    }

    @Override
    protected String doInBackground(Official... params) {

        String completeURL = dataUrlMore+params[0].getZipCode();

        Uri dataUri = Uri.parse(completeURL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {


            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            //return null;

            sb.append("[{\"name\": \"NULL\", \"divisonid\": \"123\", \"official\": \"NULL\"}]");
            return sb.toString();
        }

        Log.d(TAG, "doInBackground: " + sb.toString());


        return sb.toString();


    }

    @Override
    protected void onPostExecute(String s) {

        parseJSON(s);
        act_main.setList(objectList);

    }


    private HashMap<String, ArrayList<Official>> parseJSON(String s) {

        ArrayList<Official> officialList = new ArrayList<>();
        ArrayList<Official> dataArrayList = new ArrayList<>();
        try {

            JSONObject jObj = new JSONObject(s);
            JSONObject response = jObj.getJSONObject("normalizedInput");
            count = response.length();
            String city = jObj.getJSONObject("normalizedInput").getString("city");
            String state = jObj.getJSONObject("normalizedInput").getString("state");
            String zipCode = jObj.getJSONObject("normalizedInput").getString("zip");

            if(city!=null || state!=null || zipCode!=null)
            {
                locationName = city +", "+ state +" "+ zipCode;
            }
            JSONObject jObj1 = new JSONObject(s);
            JSONObject jObj2 = new JSONObject(s);

            JSONArray jArrayOffices = jObj1.getJSONArray("offices");
            Log.d(TAG, "21: " +jArrayOffices.length());

            for(int i =0; i < jArrayOffices.length(); i++)
            {
                JSONObject jb1 = jArrayOffices.getJSONObject(i);
                String positionType = jb1.getString("name");

            }

            for(int i1 =0; i1 < jArrayOffices.length(); i1++)
            {


                Log.d("length", String.valueOf(jArrayOffices.length()));
                posname = jArrayOffices.getJSONObject(i1).getString("name");
                JSONArray offices = (jArrayOffices.getJSONObject(i1).getJSONArray("officialIndices"));
                Log.d("officiesIndicies1" + ": ", offices.toString());

                for (int j = 0; j < jArrayOffices.getJSONObject(i1).getJSONArray("officialIndices").length(); j++)
                {
                    Official DataFetcher = new Official();
                    int id11 = offices.getInt(j);

                    DataFetcher.setoffDesig(posname);

                    officerName = jObj1.getJSONArray("officials").getJSONObject(id11).getString("name");
                    DataFetcher.setoName(officerName);
                    try
                    {
                        if(jObj1.getJSONArray("officials").getJSONObject(id11).has("party"))
                        {
                            offParty = jObj1.getJSONArray("officials").getJSONObject(id11).getString("party");
                        }
                        else {
                            offParty = "Unknown";
                        }
                        DataFetcher.setParty(offParty);
                    }

                    catch (JSONException e)
                    {
                        Log.e(TAG, "doInBackground3: ", e);
                    }
                    if(jObj1.getJSONArray("officials").getJSONObject(id11).has("address"))
                    {
                        try
                        {
                            {
                                JSONObject line1 = jObj2.getJSONArray("officials").getJSONObject(id11).getJSONArray("address").getJSONObject(0);
                                if (line1.has("line1")) {
                                    adLine1 = line1.getString("line1");
                                }
                                if (line1.has("line2")) {
                                    adLine2 = line1.getString("line2");
                                }
                                if (line1.has("city")) {
                                    city = line1.getString("city");
                                }
                                if (line1.has("state")) {
                                    state = line1.getString("state");
                                }
                                if (line1.has("line2")) {
                                    zip = line1.getString("zip");
                                }
                                DataFetcher.setadl1(adLine1);
                                DataFetcher.setadl2(adLine2);
                                DataFetcher.setCity(city);
                                DataFetcher.setstate(state);
                                DataFetcher.setZipCode(zip);



                                Log.i("Address", "[" + j + "]" + ":" + adLine1 + " " + adLine2 + " " + city + " " + state + " " + zip);

                            }
                        }
                        catch (JSONException e)
                        {
                            Log.e(TAG, "JSONEXCEPTION: ", e);
                        }
                    }
                    if(jObj1.getJSONArray("officials").getJSONObject(id11).has("phones"))
                    {

                        phone = (jObj1.getJSONArray("officials").getJSONObject(id11).getJSONArray("phones").get(0).toString());
                        DataFetcher.setPhone(phone);

                    }

                    if(jObj1.getJSONArray("officials").getJSONObject(id11).has("urls"))
                    {

                        urlsForObject =  jObj1.getJSONArray("officials").getJSONObject(id11).getJSONArray("urls").get(0).toString();

                        DataFetcher.setUrls(urlsForObject);
                        DataFetcher.setweb(urlsForObject);


                    }

                    if(jObj1.getJSONArray("officials").getJSONObject(id11).has("photoUrl"))
                    {

                        photo = jObj1.getJSONArray("officials").getJSONObject(id11).getString("photoUrl");
                        DataFetcher.setimgUrls(photo);

                    }
                    else
                    {
                        photo = "NoPhoto";
                        DataFetcher.setimgUrls(photo);
                    }

                    if(jObj1.getJSONArray("officials").getJSONObject(id11).has("emails"))
                    {

                        email = jObj1.getJSONArray("officials").getJSONObject(id11).getJSONArray("emails").get(0).toString();
                        DataFetcher.setEmail(email);

                    }

                    if(jObj1.getJSONArray("officials").getJSONObject(id11).has("channels"))
                    {
                        HashMap<String, String> hashMap = new HashMap<String, String>();

                        hashMap.put("GooglePlus","898989");
                        hashMap.put("Facebook","898989");
                        hashMap.put("Twitter","898989");
                        hashMap.put("YouTube","898989");

                        JSONArray jsonChannels = jObj1.getJSONArray("officials").getJSONObject(id11).getJSONArray("channels");

                        for ( int pntr = 0; pntr < jsonChannels.length(); pntr++)
                        {

                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("GooglePlus")) {
                                hashMap.put("GooglePlus", jsonChannels.getJSONObject(pntr).getString("id"));
                            }

                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("Facebook")) {
                                hashMap.put("Facebook", jsonChannels.getJSONObject(pntr).getString("id"));
                            }
                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("Twitter")) {
                                hashMap.put("Twitter", jsonChannels.getJSONObject(pntr).getString("id"));
                            }

                            if (jsonChannels.getJSONObject(pntr).getString("type").equals("YouTube")) {
                                hashMap.put("YouTube", jsonChannels.getJSONObject(pntr).getString("id"));
                            }


                        }

                        DataFetcher.setchannel(hashMap);


                    }





                    dataArrayList.add(DataFetcher);

                    objectList.put(locationName, dataArrayList);
                }
            }

            return objectList;

        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }



}
