package com.example.cil.freya;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hannah on 1/29/2016.
 */
public class CRUD {

    public static class readMessage extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                // connect to URL. "GET" code
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-length", "0");
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.connect();
                int code = urlConnection.getResponseCode();
                // if it was succesfully created
                if (code == 200)
                {
                    // read in from URL
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
                // prints error to logcat
                e.printStackTrace();
                return "error";
            } finally {
                // if the connection was succesfull, disconnect
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
        }
    }

    // write message to edge URL. Same issue. Used as template
    public static class writeMessage extends AsyncTask<MainActivity.AsyncParams, Void, returnParam>
    {

        @Override
        protected returnParam doInBackground(MainActivity.AsyncParams... params) {
            // connect to URL
            String sb = "";
            URL url;
            HttpURLConnection urlConnection = null;
            String test = "http://sensor.nevada.edu/GS/Services/edge/";
            try {
                url = new URL(test);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();


                // get output writer
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(params[0].complete.toString());
                out.close();

                // if connection is good
                int HttpResult = urlConnection.getResponseCode();
                // output
                if(HttpResult == HttpURLConnection.HTTP_OK){

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb += (line + "\n");
                    }
                    br.close();

                    System.out.println("" + sb);

                }else{
                    //  prints error code from server
                    System.out.println(urlConnection.getResponseMessage());
                }
            } catch (IOException e) {
                // print to stack trace
                e.printStackTrace();

            } finally{
                // if connection was successful  disconnect
                if(urlConnection != null)
                    urlConnection.disconnect();
            }


            returnParam par = null;
            try
            {
                par = new returnParam(urlConnection.getResponseCode(), params[0].cxt);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return par;
        }

        @Override
        protected void onPostExecute (returnParam result){
            if (result.response == 200)
            {
                Toast.makeText(result.cxt, "Post Successful", Toast.LENGTH_LONG).show();
                getInfo.complete = new JSONObject();
            }
            else
                Toast.makeText(result.cxt, "Error: " + result.response, Toast.LENGTH_LONG).show();
        }

    }

    static class returnParam{
        int response;
        Context cxt;

        returnParam(int response, Context cxt){
            this.response = response;
            this.cxt = cxt;
        }
    }



    //  delete test. SAme problem as above
    public static class deleteMessage extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            // URL is attached to theunique ID to identify which part of the JSON needs to be deleted
            String test = "https://sensor.nevada.edu/GS/Services/people/" + params [0] + "/";
            try {
                // connect to URL
                url = new URL(test);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();
                int HttpResult = urlConnection.getResponseCode();

            } catch (MalformedURLException e) {
                // print to log cat
                e.printStackTrace();

            } catch (IOException e) {
                // print to log cat
                e.printStackTrace();

            } finally{
                // if connection was successful, disconnect
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

    // update test. Same as aboce
    public static class updateMessage extends AsyncTask<String, JSONObject, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            String sb = "";
            URL url = null;
            int one = 1;
            HttpURLConnection urlConnection = null;
            // update via Unqiue ID
            String test = "https://sensor.nevada.edu/GS/Services/people/" + params [0] + "/";
            try {
                // connect to URL
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


                // output to URL
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(params[1].toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                // if connection is good
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
            } catch (MalformedURLException e) {
                // output to log cat
                e.printStackTrace();

            } catch (IOException e) {
                // output to log cat
                e.printStackTrace();

            }  finally{
                // if connection is successful, disconnect
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

}
