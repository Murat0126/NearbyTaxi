package com.example.nearbytaxi;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;

import models.Contact;

public class User implements ClusterItem {
    private final String username;
    private  ArrayList<Contact> contacts;
    private final LatLng latLng;
    public User(String username, ArrayList<Contact> contacts, LatLng latLng) {
        this.username = username;
        this.latLng = latLng;
        this.contacts = contacts;
    }
    @Override
    public LatLng getPosition() {
        return latLng;
    }
    @Override
    public String getTitle() {
        return username;
    }
    public ArrayList<Contact> getPhoneNumber() {
        return contacts;
    }
    @Override
    public String getSnippet() {
        return "";
    }
}