package com.example.timofey.wowtest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.timofey.wowtest.MainActivity.CLIENT_ID;

/**
 * Created by timofey on 03.02.2017.
 */



public class FoursquareConnector extends AsyncTask<Void, Void, String>{

    private static final String SECRET_KEY = "MDTK4KFJI5YDUGZCA4LYQDPHV1VDST53Q4LX2B5W2LGIH4LY";
    private static final String TAG = "FOURSQUARE_CONNECTOR";

    private static final String RESTAURANTS_FSQ_ID_CODE = "4d4b7105d754a06374d81259";
    public static final String EXTRA_RESTAURANTS = "EXTRA_RESTAURANTS";
    private static final String SEARCH_RADIUS = "3000";


    private double latitude, longitude;
    private String url;
    private Context context;
    String broadcastAction;


    public FoursquareConnector(Context context, double latitude, double longitude, String broadcastAction) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
        this.broadcastAction = broadcastAction;

    }


    @Override
    protected String doInBackground(Void... params) {

        OkHttpClient httpClient = new OkHttpClient();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd");
        String date = dateFormat.format(new Date());
        url = "https://api.foursquare.com/v2/venues/search?ll=" + latitude + "," + longitude +
                "&client_id=" + CLIENT_ID + "&client_secret=" + SECRET_KEY +"&v=" + date +
                "&categoryId=" + RESTAURANTS_FSQ_ID_CODE + "&radius=" + SEARCH_RADIUS;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Intent intent = new Intent(broadcastAction);
        intent.putExtra(EXTRA_RESTAURANTS, parseResponse(s));
        context.sendBroadcast(intent);
    }


    private ArrayList<GeoPoint> parseResponse(String response){

        ArrayList<GeoPoint> geoPoints = new ArrayList<>();

        try {
            JSONObject fullObject = new JSONObject(response);
            JSONObject meta = fullObject.getJSONObject("meta");


            if (meta.getString("code").equals("200")){
                JSONArray venuesArray = fullObject.getJSONObject("response").getJSONArray("venues");

                for (int i = 0; i < venuesArray.length(); i++) {

                    Log.d(TAG, String.valueOf(venuesArray.get(i)));
                    JSONObject currentObj = venuesArray.getJSONObject(i);
                    String name = currentObj.getString("name");
                    JSONObject location = venuesArray.getJSONObject(i).getJSONObject("location");
                    String address;
                    try {
                        address = location.getString("address");
                    } catch (JSONException e){
                        address = null;
                    }

                    double latitude = Double.parseDouble(location.getString("lat"));
                    double longitude = Double.parseDouble(location.getString("lng"));

                    String categories = null;
                    try {
                        JSONArray categoriesArray = currentObj.getJSONArray("categories");

                        for (int j = 0; j < categoriesArray.length(); j++) {
                            if (categories == null) {
                                categories = categoriesArray.getJSONObject(j).getString("name");
                            } else {
                                categories += ", " + categoriesArray.getJSONObject(j).getString("name");
                            }
                        }
                    } catch (JSONException e){
                        Log.e(TAG, e.getLocalizedMessage());
                    }

                    int distance;
                    try {
                        distance = Integer.parseInt(location.getString("distance"));
                    } catch (JSONException e){
                        distance = 0;
                    }

                    String iconUrl;
                    try {
                        JSONObject icon = currentObj.getJSONArray("categories").getJSONObject(0).getJSONObject("icon");
                        iconUrl = icon.getString("prefix") + "bg_88" + icon.getString("suffix");
                    } catch (JSONException e){
                        iconUrl = null;
                    }

                    String url;
                    try {
                        url = currentObj.getString("url");
                    } catch (JSONException e){
                        url = null;
                    }
                    JSONObject contact = currentObj.getJSONObject("contact");
                    String phone;
                    String formattedPhone;
                    String twitter;
                    try{
                        phone = contact.getString("phone");
                        formattedPhone = contact.getString("formattedPhone");
                    } catch (JSONException e){
                        phone = null;
                        formattedPhone = null;
                    }
                    try {
                        twitter = contact.getString("twitter");
                    } catch (JSONException e){
                        twitter = null;
                    }

                    geoPoints.add(new GeoPoint(name, address, latitude, longitude, categories, distance, iconUrl, url, phone, formattedPhone, twitter));
                }
             }

        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return geoPoints;
    }
}
