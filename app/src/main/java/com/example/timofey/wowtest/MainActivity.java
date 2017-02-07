package com.example.timofey.wowtest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import static com.example.timofey.wowtest.GoogleMapsFragment.LOCATION_REQUEST_CODE;
import static com.example.timofey.wowtest.HereMapsFragment.WRITE_EXTERNAL_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "CU02QTDIBCJWNID4YYO05334BNJIEL5CF4EYXFY3WICW0YHR";

    private int providerCode = 0;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {

            boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            finish();
            startActivity(getIntent());
        } else if (requestCode == WRITE_EXTERNAL_REQUEST_CODE) {
            boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            finish();
            startActivity(getIntent());
        }
    }


    private void setHereApiProvider(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        HereMapsFragment hereMapsFragment = (HereMapsFragment) fragmentManager.findFragmentByTag(HereMapsFragment.TAG);

        if (hereMapsFragment == null){
            hereMapsFragment = new HereMapsFragment();
        }

        fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.activity_main, hereMapsFragment, HereMapsFragment.TAG);
        fragmentTransaction.commit();
    }

    private void setGoogleApiProvider(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        GoogleMapsFragment googleMapsFragment = (GoogleMapsFragment) fragmentManager.findFragmentByTag(GoogleMapsFragment.TAG);

        if (googleMapsFragment == null){
            googleMapsFragment = new GoogleMapsFragment();
        }

        fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.activity_main, googleMapsFragment, GoogleMapsFragment.TAG);
        fragmentTransaction.commit();
    }

    public Location getCurrentLocation(){

        Location location = null;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location == null){
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }
        return location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.choose_provider:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dialog_title);

                builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setSingleChoiceItems(R.array.providers, providerCode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        providerCode = which;
                    }
                });

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (providerCode == 1){
                            setHereApiProvider();
                        } else {
                            setGoogleApiProvider();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }


}
