package com.example.nearbytaxi;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String FREECABS_BASE_URL =
            "https://openfreecabs.org/nearest";

    private final static String SLASH = "/";
    private static double LATITUDE;
    private static double LONGITUDE;
    private URL openfreecabsURl;

    public void setLocation(double latitude, double longitude) {
        LATITUDE = latitude;
        LONGITUDE = longitude;
    }

    String getResponseFromHttpUrl() throws IOException {

        Uri BuildUri = Uri.parse(FREECABS_BASE_URL + SLASH + LATITUDE + SLASH + LONGITUDE).buildUpon()
                .build();
        try {
            openfreecabsURl = new URL(BuildUri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = (HttpURLConnection) openfreecabsURl.openConnection();

        try{
            InputStream input = urlConnection.getInputStream();
            Scanner scanner =  new Scanner(input);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}