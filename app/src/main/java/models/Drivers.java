package models;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class Drivers{

    public static LatLng convertToPoint(JSONObject jsonObject){
        LatLng point;
        try {
            double lat = jsonObject.getDouble("lat");
            double lon = jsonObject.getDouble("lon");
            point = new LatLng(lat, lon);
        } catch (JSONException e) {
            e.printStackTrace();
            point = new LatLng(0,0);
        }

        return point;
    }
}

