package com.example.cil.freya;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cil on 10/22/15.
 */
public class HTTP_Put extends AsyncTask<String, Void, Void>
{
    JSONObject JSON = null;

    @Override
    protected Void doInBackground(String... uniqueID) {
        String sb = "";
        URL url = null;
        int one = 1;
        HttpURLConnection urlConnection = null;
        String test = "http://sensor.nevada.edu/GS/Services/people/";
        String u = "0E984725-C51C-4BF4-9960-E1C80A27ABB3";
        try {
            url = new URL(test);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            try
            {
                JSON = new MainActivity().createJSON(u);


                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(JSON.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb += (line + "\n");
                    }
                    br.close();

                    System.out.println("" + sb);

                }else{
                    System.out.println(urlConnection.getResponseMessage());
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e)
            {
                e.printStackTrace();

            }finally{

                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
    }


}

