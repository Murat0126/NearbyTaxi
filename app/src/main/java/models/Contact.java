package models;

import org.json.JSONException;
import org.json.JSONObject;

public class Contact {

    public String [] contactNumber;

    public static Contact valueOf(JSONObject jsonObject){
        Contact contact = new Contact();
        try {
            String type = jsonObject.getString("type");
            String dateContacts = jsonObject.getString("contact");
            contact.contactNumber = new String [] {type,dateContacts};

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contact;
    }
}
