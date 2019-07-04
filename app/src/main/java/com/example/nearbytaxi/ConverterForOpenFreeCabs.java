package com.example.nearbytaxi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import models.Company;

import java.util.ArrayList;

public class ConverterForOpenFreeCabs {

    static ArrayList<Company> convertToServicesList(String searchRezults) {
        ArrayList<Company> companies = new ArrayList<>();
        try{
            JSONObject rootObject = new JSONObject(searchRezults);
            JSONArray resultsJSON = rootObject.getJSONArray("companies");

            for (int i = 0; i<resultsJSON.length(); i++) {
                JSONObject company = resultsJSON.getJSONObject(i);
                companies.add(Company.valueOf(company));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return companies;
    }

}

