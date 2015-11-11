package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.Date;

/**
 * Created by cil on 11/10/15.
 */
public class CreateNewProject extends Activity implements View.OnClickListener
{
    static String projectsURL = MainActivity.mainURL+ "projects/";

    Button createButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_project);
        createButton = (Button)findViewById(R.id.post);
        createButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.post):
                new writeMessage().execute();
                // Toast.makeText(CreateNewProject.this, "Post Successful", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public class writeMessage extends AsyncTask<Void, Void, Void>
{
    int success = 0;
    @Override
    protected Void doInBackground(Void... params) {
        String sb = "";
        URL url;
        HttpURLConnection urlConnection = null;
        String test = projectsURL;
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

            //Create JSONObject here
            JSONObject JSON = createProjectJSON();

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(JSON.toString());
            out.close();

            int HttpResult = urlConnection.getResponseCode();

            if(HttpResult == HttpURLConnection.HTTP_OK){
                success = 1;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb += (line + "\n");
                }
                br.close();

                System.out.println("" + sb);

            }else{
                System.out.println(urlConnection.getResponseMessage());

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();

        } finally{

            if(urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }
    protected void onPostExecute(Void param) {
        if (success ==1){
            Toast.makeText(CreateNewProject.this, "Post successful.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(CreateNewProject.this, "Could not connect to server.", Toast.LENGTH_LONG).show();
        }
    }
}

        public JSONObject createProjectJSON() throws JSONException
        {
            JSONObject jsonParam = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            String date = sdf.format(new Date());
            jsonParam.put("Creation Date", date);
            EditText info = (EditText) findViewById(R.id.grant);
            jsonParam.put("Grant Number String", info.getText().toString());
            info = (EditText) findViewById(R.id.institutionName);
            jsonParam.put("Institution Name", info.getText().toString());
            jsonParam.put("Modification Date", date);
            info = (EditText) findViewById(R.id.projName);
            jsonParam.put("Name", info.getText().toString());
            info = (EditText) findViewById(R.id.funding);
            jsonParam.put("Original Funding Agency", info.getText().toString());
            info = (EditText) findViewById(R.id.prinInvest);
            jsonParam.put("Principal Investigator", info.getText().toString());
            info = (EditText) findViewById(R.id.project);
            jsonParam.put("Project", info.getText().toString());
            jsonParam.put("Start Date", date);
            jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

            return jsonParam;
        }
}
