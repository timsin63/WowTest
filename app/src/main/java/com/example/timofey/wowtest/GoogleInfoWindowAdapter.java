package com.example.timofey.wowtest;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by timofey on 05.02.2017.
 */

public class GoogleInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View view;
    private Context context;
    HashMap<String, Boolean> markerSet;


    public GoogleInfoWindowAdapter(Context context, HashMap<String, Boolean> markerSet){
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

        view.setLayoutParams(new RelativeLayout.LayoutParams(500, RelativeLayout.LayoutParams.WRAP_CONTENT));

        Log.d("marker", marker.getSnippet());
        Restaurant restaurant = new Gson().fromJson(marker.getSnippet(), Restaurant.class);
        Resources resources = view.getResources();

        TextView nameView = (TextView) view.findViewById(R.id.name);
        nameView.setText(marker.getTitle());
        ImageView iconView = (ImageView) view.findViewById(R.id.icon_view);

        boolean isImgDownloaded = markerSet.get(marker.getId());
        if(!isImgDownloaded){
            Picasso.with(context).load(Uri.parse(restaurant.getIconUrl()))
                    .resize((int) resources.getDimension(R.dimen.icon_info_size), (int) resources.getDimension(R.dimen.icon_info_size))
                    .placeholder(R.drawable.fork_n_knife)
                    .into(iconView, new Callback() {
                        @Override
                        public void onSuccess() {
                            markerSet.put(marker.getId(), true);
                            marker.hideInfoWindow();
                            marker.showInfoWindow();
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Picasso.with(context).load(Uri.parse(restaurant.getIconUrl()))
                    .resize((int) resources.getDimension(R.dimen.icon_info_size), (int) resources.getDimension(R.dimen.icon_info_size))
                    .placeholder(R.drawable.fork_n_knife)
                    .into(iconView);
        }


        TextView addressView = (TextView) view.findViewById(R.id.address);
        String address = restaurant.getAddress();
        if (!TextUtils.isEmpty(address)){
            addressView.setText(resources.getString(R.string.address) +address);
        } else {
            addressView.setText(null);
        }

        TextView categoryView = (TextView) view.findViewById(R.id.category);
        String categories = restaurant.getCategory();
        if (!TextUtils.isEmpty(categories)){
            categoryView.setText(resources.getString(R.string.categories) + categories);
        } else {
            categoryView.setText(null);
        }

        TextView distanceView = (TextView) view.findViewById(R.id.distance);
        String distance = restaurant.getDistance();
        if (!TextUtils.isEmpty(distance)){
            distanceView.setText(resources.getString(R.string.distance) + distance);
        } else {
            distanceView.setText(null);
        }

        TextView phoneView = (TextView) view.findViewById(R.id.phone);
        String phone = restaurant.getFormattedPhone();
        if (!TextUtils.isEmpty(phone)){
            phoneView.setText(resources.getString(R.string.phone) + phone);
        } else {
            phoneView.setText(null);
        }

        TextView siteUrlView = (TextView) view.findViewById(R.id.site);
        String siteUrl = restaurant.getUrl();
        if (!TextUtils.isEmpty(siteUrl)){
            siteUrlView.setText(resources.getString(R.string.site) + siteUrl);
        } else {
            siteUrlView.setText(null);
        }
        TextView twitterView = (TextView) view.findViewById(R.id.twitter);
        String twitter = restaurant.getTwitter();
        if (!TextUtils.isEmpty(twitter)){
            twitterView.setText(resources.getString(R.string.twitter) + twitter);
        } else {
            twitterView.setText(null);
        }

        return view;
    }


}
