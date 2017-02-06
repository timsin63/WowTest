package com.example.timofey.wowtest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

import static com.example.timofey.wowtest.GoogleMapsFragment.LOCATION_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FSQ_CONNECT = 3459;
    public static final String CLIENT_ID = "CU02QTDIBCJWNID4YYO05334BNJIEL5CF4EYXFY3WICW0YHR";


    GoogleMapsFragment googleMapsFragment;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        googleMapsFragment = (GoogleMapsFragment) fragmentManager.findFragmentByTag(GoogleMapsFragment.TAG);
//
//        if (googleMapsFragment == null){
//            googleMapsFragment = new GoogleMapsFragment();
//        }
//        fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.map, googleMapsFragment, GoogleMapsFragment.TAG);
//        fragmentTransaction.commit();


//        Intent intent = FoursquareOAuth.getConnectIntent(getApplicationContext(), CLIENT_ID);
//        startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CODE_FSQ_CONNECT:
//                AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
//            /* ... */
//                break;
//        }
//    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {

            boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            finish();
            startActivity(getIntent());
        }
    }

}
