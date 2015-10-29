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
import java.util.UUID;

/**
 * Created by cil on 10/22/15.
 */
public class HTTP_Post extends AsyncTask<String, Void, Void>
{
    JSONObject JSON = null;

    @Override
    protected Void doInBackground(String... params)
    {
        String sb = "";
        URL url = null;
        HttpURLConnection urlConnection = null;

        try
        {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            try
            {
                MainActivity mainActivity = new MainActivity();
                String uniqueID = UUID.randomUUID().toString();

                JSON = mainActivity.createJSON(uniqueID);
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(JSON.toString());
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                {
                    sb += (line + "\n");
                }
                br.close();

                System.out.println("" + sb);

            } else
            {
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e)
        {

            e.printStackTrace();

        } catch (IOException e)
        {

            e.printStackTrace();

        } finally
        {

            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }
}
