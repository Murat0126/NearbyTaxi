package com.example.nearbytaxi;

import android.util.Log;

public class ServicesTaxiModel {

    private String name = "";
    private String smsContact = "";
    private String phoneContact = "";
    private double latitude;
    private double longitude;
    private String icon = "";


    public  String  getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDriversLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double[] getLocation(){
        return new double[] {latitude, longitude};
    }

    public void setSmsContact(String contact) {
        smsContact = contact;
    }

    public  String getSmsContact(){
        return smsContact;

    }

    public void setPhoneContact(String contact) {
        phoneContact = contact;
    }

    public String getPhoneContact(){
        return phoneContact;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public  String getIcon(){
        return icon;
    }
}
