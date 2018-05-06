package com.nex.blub.PiCo.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JSONUtils {

    // Log-Tag für diese Klasse
    private static final String TAG = "JSONUtils";


    public static JSONObject getValuesByDeviceName(String jsonString, String deviceName) {
        JSONArray jsonArray = null;
        JSONObject object = null;

        try {
            jsonArray = new JSONArray(jsonString);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
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
                Log.d(TAG, e.getMessage());
            }
        }

        return object;
    }


    public static List<HistoryData> convertStringToHistoryData(String jsonString) {
        List<HistoryData> list = new ArrayList<>();
        JSONObject object = null;

        try {
            object = new JSONObject(jsonString);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        if (object == null) {
            return null;
        }

        Iterator<String> it = object.keys();

        while(it.hasNext()) {
            try {
                JSONObject values = object.getJSONObject(it.next());
                Integer ts = values.getInt("ts");
                Double temp = values.getDouble("temp");
                Double hum = values.getDouble("hum");
                list.add(new HistoryData(ts, temp, hum));
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }

        return list;
    }
}
