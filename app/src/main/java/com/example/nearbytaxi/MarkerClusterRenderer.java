package com.example.nearbytaxi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import models.Company;

public class MarkerClusterRenderer extends DefaultClusterRenderer<User> {
    private static final int MARKER_DIMENSION = 48;
    private final IconGenerator iconGenerator;
    private final ImageView markerImageView;



    private Company company;
    MainActivity mainActivity;



    public MarkerClusterRenderer(Context context, GoogleMap map, ClusterManager<User> clusterManager, Company company) {
        super(context, map, clusterManager);
        iconGenerator = new IconGenerator(context);
        markerImageView = new ImageView(context);
        markerImageView.setLayoutParams(new ViewGroup.LayoutParams(MARKER_DIMENSION, MARKER_DIMENSION));
        iconGenerator.setContentView(markerImageView);
        this.company = company;
    }
    @Override
    protected void onBeforeClusterItemRendered(User item, MarkerOptions markerOptions) {

            markerImageView.setImageResource(getIconId(item.getTitle()));
            Bitmap icon = iconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            markerOptions.title(item.getTitle());

    }


        int getIconId(String name){
        switch (name){
            case "NambaTaxi":
                return (R.drawable.namba);
            case "SmsTaxi":
                return  (R.drawable.logo_sms);
            default:
                return (R.drawable.question);
        }
    }


    @Override
    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<User> listener) {
        super.setOnClusterClickListener(listener);
    }

}