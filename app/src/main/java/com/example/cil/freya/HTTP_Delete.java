package com.example.cil.freya;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cil on 10/22/15.
 */
public class HTTP_Delete extends AsyncTask<String, Void, Void>
{
    @Override
    protected Void doInBackground(String... uniqueID) {
        URL url;
        HttpURLConnection urlConnection = null;

        String test = "http://sensor.nevada.edu/GS/Services/people/" + uniqueID[0];

        try {
            url = new URL(test);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();
            int HttpResult = urlConnection.getResponseCode();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } finally{

            if(urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }
}

