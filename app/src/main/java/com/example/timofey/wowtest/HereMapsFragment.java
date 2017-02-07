package com.example.timofey.wowtest;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.timofey.wowtest.FoursquareConnector.EXTRA_RESTAURANTS;


public class HereMapsFragment extends Fragment {

    public static final int WRITE_EXTERNAL_REQUEST_CODE = 153;
    public static final String TAG = "HERE_MAPS_FRAGMENT";
    public static final String BROADCAST_ACTION = "BROADCAST_HERE_OBJECTS";
    View view;
    MapFragment mapFragment;
    Map map;

    public HereMapsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_here_maps, container, false);
        }

        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.here_map_fragment);

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            mapFragment.init(new EngineListener());
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_REQUEST_CODE);
        }

        return view;
    }

    private class EngineListener implements OnEngineInitListener{

        @Override
        public void onEngineInitializationCompleted(Error error) {
            if (error == OnEngineInitListener.Error.NONE) {

                map = mapFragment.getMap();

                Location location = ((MainActivity) getActivity()).getCurrentLocation();
                setCurrentLocation(location);

                FoursquareConnector foursquareConnector = new FoursquareConnector(getActivity().getApplicationContext(),
                        location.getLatitude(), location.getLongitude(), BROADCAST_ACTION);
                foursquareConnector.execute();

                getActivity().registerReceiver(hereObjectsReceiver, new IntentFilter(BROADCAST_ACTION));

                mapFragment.getMapGesture().addOnGestureListener(new HereMapsGestureListener());

            } else {
                Log.e(TAG, getString(R.string.here_maps_initialize_error));
            }
        }
    }


    private void setCurrentLocation(Location location){
        GeoCoordinate currentGeoCoordinate = new GeoCoordinate(location.getLatitude(), location.getLongitude());

        map.setCenter(currentGeoCoordinate, Map.Animation.LINEAR);
        map.setZoomLevel(15, Map.Animation.LINEAR);

        MapMarker currentPosition = new MapMarker()
                .setCoordinate(currentGeoCoordinate)
                .setTitle(getString(R.string.you_are_here));

        Image humanImg = new Image();
        try {
            humanImg.setImageResource(R.drawable.human);
            currentPosition.setIcon(humanImg);
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        map.addMapObject(currentPosition);
    }


    BroadcastReceiver hereObjectsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<GeoPoint> list = (ArrayList<GeoPoint>) intent.getSerializableExtra(EXTRA_RESTAURANTS);
            HashMap<String, Boolean> markerSet = new HashMap<>();

            for (GeoPoint geoPoint : list){
                MapMarker restaurantMarker = new MapMarker();
                restaurantMarker.setCoordinate(new GeoCoordinate(geoPoint.getLatitude(), geoPoint.getLongitude()));
                restaurantMarker.setTitle(geoPoint.getName());
                restaurantMarker.setDescription(new Gson().toJson(geoPoint));

                markerSet.put(restaurantMarker.getTitle(), false);

                map.addMapObject(restaurantMarker);
            }

            map.setInfoBubbleAdapter(new MapInfoAdapter(getContext(), markerSet));
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (hereObjectsReceiver != null){
            getActivity().unregisterReceiver(hereObjectsReceiver);
        }
    }
}
