package com.example.szklarniaparapetowa;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.ArrayList;

public class JsonHandling extends AsyncTask<Void, Void, String> {
    private static String sensorUrl = "http://projektz.heliohost.org/test/db_test.php";
    private static String executorUrl = "http://projektz.heliohost.org/test/excitation.php";
    private static String url = "http://projektz.heliohost.org/test/db_test.php";
    private static String url2 = "http://projektz.heliohost.org/test/excitation.php";
    DatabaseHelper db = new DatabaseHelper(MainActivity.mycontext);

    public JsonHandling() {}

    public void setExecutorUrl(String urlIn){
        url = executorUrl + "?" + urlIn;
    }


    public void setSensorUrl(){
        url=sensorUrl;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(url);
        return jsonStr;
    }

    public void getData(String jsonStr){
        JSONObject object = new JSONObject();
        if (jsonStr != null) {
            try {
                JSONArray jsonArray  = new JSONArray(jsonStr);
                for (int n = 0; n < jsonArray.length(); n++) {
                    try {
                        object = jsonArray.getJSONObject(n);
                        String date = object.getString("timestamp");
                        double temp = Double.valueOf(object.getString("temperature"));
                        double humidity = Double.valueOf(object.getString("humidity"));
                        double light = Double.valueOf(object.getString("light"));
                        db.putDataFromJson(date, temp, light, humidity);
                    } catch (Exception e) {}
                }
            } catch (final JSONException e) { }
        }
    }
}

