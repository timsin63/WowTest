package com.example.timofey.wowtest;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.here.android.mpa.mapping.MapMarker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by timofey on 05.02.2017.
 */

public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter, com.here.android.mpa.mapping.Map.InfoBubbleAdapter {

    private View view;
    private Context context;
    HashMap<String, Boolean> markerSet;


    public MapInfoAdapter(Context context, HashMap<String, Boolean> markerSet){
        view = LayoutInflater.from(context).inflate(R.layout.info_window, null);
        this.markerSet = markerSet;
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {

        return getGoogleMarkerView(marker);
    }


    @Override
    public View getInfoBubbleContents(MapMarker mapMarker) {

        return getHereApiMarkerView(mapMarker);
    }

    @Override
    public View getInfoBubble(MapMarker mapMarker) {
        return null;
    }



    private View getGoogleMarkerView(Marker marker){
        Resources resources = view.getResources();
        GeoPoint geoPoint = new Gson().fromJson(marker.getSnippet(), GeoPoint.class);

        view.setLayoutParams(new RelativeLayout.LayoutParams(500, RelativeLayout.LayoutParams.WRAP_CONTENT));

        TextView nameView = (TextView) view.findViewById(R.id.name);
        nameView.setText(marker.getTitle());
        ImageView iconView = (ImageView) view.findViewById(R.id.icon_view);

        boolean isImgDownloaded = markerSet.get(marker.getId());
        if(!isImgDownloaded){
            final Marker finalMarker = marker;
            Picasso.with(context).load(Uri.parse(geoPoint.getIconUrl()))
                    .resize((int) resources.getDimension(R.dimen.icon_info_size), (int) resources.getDimension(R.dimen.icon_info_size))
                    .placeholder(R.drawable.fork_n_knife)
                    .into(iconView, new Callback() {
                        @Override
                        public void onSuccess() {
                            markerSet.put(finalMarker.getId(), true);
                            finalMarker.hideInfoWindow();
                            finalMarker.showInfoWindow();
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Picasso.with(context).load(Uri.parse(geoPoint.getIconUrl()))
                    .resize((int) resources.getDimension(R.dimen.icon_info_size), (int) resources.getDimension(R.dimen.icon_info_size))
                    .placeholder(R.drawable.fork_n_knife)
                    .into(iconView);
        }

        setDescription(geoPoint);

        return view;
    }

    private View getHereApiMarkerView(final MapMarker marker){

        Resources resources = view.getResources();


        GeoPoint geoPoint = new Gson().fromJson(marker.getDescription(), GeoPoint.class);

        view.setLayoutParams(new RelativeLayout.LayoutParams(500, RelativeLayout.LayoutParams.WRAP_CONTENT));

        TextView nameView = (TextView) view.findViewById(R.id.name);
        nameView.setText(marker.getTitle());

        ImageView iconView = (ImageView) view.findViewById(R.id.icon_view);

        try{
        if (marker.getTitle().equals(resources.getString(R.string.you_are_here))){
            iconView.setImageResource(R.drawable.human);
            geoPoint = new GeoPoint(marker.getCoordinate().getLatitude(), marker.getCoordinate().getLongitude());
        }

            Picasso.with(context).load(Uri.parse(geoPoint.getIconUrl()))
                    .resize((int) resources.getDimension(R.dimen.icon_info_size), (int) resources.getDimension(R.dimen.icon_info_size))
                    .placeholder(R.drawable.fork_n_knife)
                    .into(iconView);
        } catch (Exception e){}


        try {
            setDescription(geoPoint);
        } catch (Exception e){}


        return view;
    }

    private void setDescription(GeoPoint geoPoint){
        Resources resources = view.getResources();

        TextView addressView = (TextView) view.findViewById(R.id.address);
        String address = geoPoint.getAddress();
        if (!TextUtils.isEmpty(address)){
            addressView.setText(resources.getString(R.string.address) +address);
        } else {
            addressView.setText(null);
        }

        TextView categoryView = (TextView) view.findViewById(R.id.category);
        String categories = geoPoint.getCategory();
        if (!TextUtils.isEmpty(categories)){
            categoryView.setText(resources.getString(R.string.categories) + categories);
        } else {
            categoryView.setText(null);
        }

        TextView distanceView = (TextView) view.findViewById(R.id.distance);
        String distance = geoPoint.getDistance();
        if (!TextUtils.isEmpty(distance)){
            distanceView.setText(resources.getString(R.string.distance) + distance);
        } else {
            distanceView.setText(null);
        }

        TextView phoneView = (TextView) view.findViewById(R.id.phone);
        String phone = geoPoint.getFormattedPhone();
        if (!TextUtils.isEmpty(phone)){
            phoneView.setText(resources.getString(R.string.phone) + phone);
        } else {
            phoneView.setText(null);
        }

        TextView siteUrlView = (TextView) view.findViewById(R.id.site);
        String siteUrl = geoPoint.getUrl();
        if (!TextUtils.isEmpty(siteUrl)){
            siteUrlView.setText(resources.getString(R.string.site) + siteUrl);
        } else {
            siteUrlView.setText(null);
        }
        TextView twitterView = (TextView) view.findViewById(R.id.twitter);
        String twitter = geoPoint.getTwitter();
        if (!TextUtils.isEmpty(twitter)){
            twitterView.setText(resources.getString(R.string.twitter) + twitter);
        } else {
            twitterView.setText(null);
        }
    }
}

