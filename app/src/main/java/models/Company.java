package models;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Company {
    public String name;
    public String imageUrl;
    public ArrayList<Contact> contacts;
    public ArrayList<LatLng> drivers;

    public static Company valueOf(JSONObject jsonObject){
        Company company = new Company();
        try {
            company.name = jsonObject.getString("name");
            company.imageUrl = jsonObject.getString("icon");
            JSONArray contactsJSON = jsonObject.getJSONArray("contacts");
            JSONArray driversJSON = jsonObject.getJSONArray("drivers");



            company.contacts = new ArrayList<>();
            for(int i = 0; i < contactsJSON.length(); i++) {
                JSONObject contact = contactsJSON.getJSONObject(i);
                company.contacts.add(Contact.valueOf(contact));
            }

            company.drivers = new ArrayList<>();
            for(int i = 0; i < driversJSON.length(); i++) {
                JSONObject driver = driversJSON.getJSONObject(i);
                company.drivers.add(Drivers.convertToPoint(driver));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return company;
    }

}