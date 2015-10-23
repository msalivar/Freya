package com.example.cil.freya;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cil on 10/22/15.
 */
public class HTTP_Get extends AsyncTask<String, Void, String>
{
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.connect();
            int code = urlConnection.getResponseCode();
            if (code == 200)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
                br.close();
                return sb.toString();
            }
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
