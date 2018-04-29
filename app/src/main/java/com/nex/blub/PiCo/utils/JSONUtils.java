package com.nex.blub.PiCo.utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;


public class JSONUtils {


    public static JSONObject getValuesByDeviceName(String jsonString, String deviceName) {
        JSONArray jsonArray = null;
        JSONObject object = null;

        try {
            jsonArray = new JSONArray(jsonString);
        } catch (Exception e) {
            Log.d("JSONUtils", e.getMessage());
        }

        if (jsonArray == null) {
            return null;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tempObject = jsonArray.getJSONObject(i);
                JSONArray devices = tempObject.getJSONArray("devices");
                if (devices.getString(0).equals(deviceName)) {
                    object = tempObject.getJSONObject("values");
                }
            } catch (Exception e) {
                Log.d("JSONUtils", e.getMessage());
            }
        }

        return object;
    }
}
