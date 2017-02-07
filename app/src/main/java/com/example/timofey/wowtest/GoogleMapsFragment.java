package com.example.timofey.wowtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.timofey.wowtest.FoursquareConnector.EXTRA_RESTAURANTS;


public class GoogleMapsFragment extends SupportMapFragment implements OnMapReadyCallback {



    public static final int LOCATION_REQUEST_CODE = 7855;
    public static final String TAG = "FRAGMENT_GOOGLE_MAPS";
    public static final String BROADCAST_ACTION = "BROADCAST_GOOGLE_MARKERS";

    private View view = null;
    private GoogleMap googleMap;

    public GoogleMapsFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        if (view == null){
            view = inflater.inflate(R.layout.fragment_google_maps, container, false);
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;


        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.googleMap.setMyLocationEnabled(true);

            Location location = ((MainActivity) getActivity()).getCurrentLocation();

            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                this.googleMap.animateCamera(cameraUpdate);


                FoursquareConnector foursquareConnector = new FoursquareConnector(getActivity().getApplicationContext(),location.getLatitude(), location.getLongitude(), BROADCAST_ACTION);
                foursquareConnector.execute();

                getActivity().registerReceiver(restaurantsReceiver, new IntentFilter(BROADCAST_ACTION));
            }

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

    }


    BroadcastReceiver restaurantsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<GeoPoint> list = (ArrayList<GeoPoint>) intent.getSerializableExtra(EXTRA_RESTAURANTS);
            HashMap<String, Boolean> markerSet = new HashMap<>();

            for (GeoPoint geoPoint : list) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                markerOptions.title(geoPoint.getName());

                String restaurantJson = new Gson().toJson(geoPoint);
                markerOptions.snippet(restaurantJson);

                Marker marker = googleMap.addMarker(markerOptions);

                markerSet.put(marker.getId(), false);

            }
            googleMap.setInfoWindowAdapter(new MapInfoAdapter(getContext(), markerSet));
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (restaurantsReceiver != null) {
            getActivity().unregisterReceiver(restaurantsReceiver);
        }
    }
}
