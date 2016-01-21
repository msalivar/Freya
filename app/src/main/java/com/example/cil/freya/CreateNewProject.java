package com.example.cil.freya;

import android.app.Activity;
<<<<<<< HEAD
import android.os.AsyncTask;
=======
import android.content.Intent;
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

<<<<<<< HEAD
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
=======
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd

/**
 * Created by cil on 11/10/15.
 */
public class CreateNewProject extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
<<<<<<< HEAD
        // create projects URL from mainactivity. made this way so that it can be easily editable 
    static String projectsURL = MainActivity.mainURL+ MainActivity.projectsURL;
    Spinner prininvest;
    Button createButton;
    
    // create the GUI
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // display
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_project);
        // give buttons names
        createButton = (Button) findViewById(R.id.post);
        createButton.setOnClickListener(this);
        // give soinner names
        prininvest = (Spinner) findViewById(R.id.prininvest);

        try {
            // put the investigator names into the spinner. 
            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, MainActivity.investigators);
            // adjust spinner values
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prininvest.setAdapter(spinAdapter);
            prininvest.setOnItemSelectedListener(this);

        }catch (NullPointerException e){
            // usually shown when unable to sync to server
            Toast.makeText(this, "Unable to populate People. Sync before trying again.", Toast.LENGTH_LONG).show();
            // print error to log cat
            e.printStackTrace();
        }
    }
    
    // listener for buttons
    public void onClick(View v)
    {
        // get button ID. Only one button right now
        switch (v.getId())
        {
            case (R.id.post):
                // if post, write message
                new writeMessage().execute();
=======
    static String projectsURL = MainActivity.mainURL + MainActivity.edgeURL;
    Spinner prininvest;
    Button createButton;
    JSONArray edge;
    static JSONObject complete = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_project);
        createButton = (Button) findViewById(R.id.post);
        createButton.setOnClickListener(this);
        prininvest = (Spinner) findViewById(R.id.prininvest);
        try
        {
            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.investigators);
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prininvest.setAdapter(spinAdapter);
            prininvest.setOnItemSelectedListener(this);
        }catch (NullPointerException e){
            Toast.makeText(this, "Unable to populate People. Sync before trying again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.post):
                try
                {newProject();} catch (JSONException e)
                {e.printStackTrace();}
                Intent intent = new Intent(this, CreateNewSite.class);
                startActivity(intent);
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
                // Toast.makeText(CreateNewProject.this, "Post Successful", Toast.LENGTH_LONG).show();
                break;
        }
    }

<<<<<<< HEAD
// auto generated
    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        // Spinner selection
    }

// auto generated
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // Do nothing
    }

// using the write message template as seen in main.java uses the URL set up at the top
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

                // error checking. If a JSON value is null, it throws an error and then leave this function. success is a check that
                // leads down to the catch. displays the correct error
                    if ( JSON.getString("Grant Number String").length() == 0)
                    {
                        success = 2;
                        throw new JSONException("Need Grant Number");
                    }
                    if ( JSON.getString("Name").length() == 0)
                    {
                        success = 3;
                        throw new JSONException("Need Project Name");
                    }
                    if ( JSON.getString("Original Funding Agency").length() == 0)
                    {
                        success = 4;
                        throw new JSONException("Need Original Funding Agency");
                    }
                    if ( JSON.getString("Institution Name").length() == 0)
                    {
                        success = 5;
                        throw new JSONException("Need Institution Name");
                    }
                    if ( JSON.getInt("Principal Investigator") == 0)
                    {
                        success = 6;
                        throw new JSONException("Choose Principal investigator");
                    }


                    //  JSON.getJSONObject()
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
                // error throwing to user. Informs the user which fields need to be filled out
                switch(success){
                    case 1:  Toast.makeText(CreateNewProject.this, "Post successful.", Toast.LENGTH_LONG).show();
                        break;
                    case 2: Toast.makeText(CreateNewProject.this, "Need Grant Number", Toast.LENGTH_LONG).show();
                        break;
                    case 3: Toast.makeText(CreateNewProject.this, "Need Project Name", Toast.LENGTH_LONG).show();
                        break;
                    case 4: Toast.makeText(CreateNewProject.this, "Need Original Funding Agency", Toast.LENGTH_LONG).show();
                        break;
                    case 5: Toast.makeText(CreateNewProject.this, "Need Institution", Toast.LENGTH_LONG).show();
                        break;
                    case 6: Toast.makeText(CreateNewProject.this, "Choose Investigator", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(CreateNewProject.this, "Could not connect to server.", Toast.LENGTH_LONG).show();
                }

            }
        }

//creating the project JSON
        public JSONObject createProjectJSON() throws JSONException
        {
            JSONObject jsonParam = new JSONObject();
            // date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            String date = sdf.format(new Date());
            // fill date
            jsonParam.put("Creation Date", date);
            // get text from grant field
            EditText info = (EditText) findViewById(R.id.grant);

            jsonParam.put("Grant Number String", info.getText().toString());
            // get text from institution field
            info = (EditText) findViewById(R.id.institution);
             // insert in JSON
            jsonParam.put("Institution Name", info.getText().toString());
            // get date
            jsonParam.put("Modification Date", date);
             // insert in JSON
            info = (EditText) findViewById(R.id.name);
             // insert in JSON
            jsonParam.put("Name", info.getText().toString());
            // get text from funding field
            info = (EditText) findViewById(R.id.funding);
             // insert in JSON
            jsonParam.put("Original Funding Agency", info.getText().toString());
            //info = (EditText) findViewById(R.id.prininvest);
            // get user from spinner
            Spinner spinner=(Spinner) findViewById(R.id.prininvest);
            int investigator = spinner.getSelectedItemPosition();
           // investigator -= 1;
            //String text = spinner.getSelectedItem().toString();
            //Integer investigator = Integer.parseInt(text);
            // put in JSON
            jsonParam.put("Principal Investigator", investigator);
            // put user in JSON
            jsonParam.put("Started Date", date);
            // create unqiue id, put to JSON
=======
    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        //autogenerated, do nothing
    }

    public void newProject() throws JSONException
    {

                //Create JSONObject here
                JSONObject JSON = createProjectJSON();

                JSONArray between = new JSONArray();
                between.put(JSON);

                JSONObject attrs = new JSONObject();
                attrs.put("Attrs", between);

                JSONArray project = new JSONArray();
                project.put(attrs);

                complete.put("Project", project);

          // edge = MainActivity.createEdge(complete);
}

        public JSONObject createProjectJSON() throws JSONException
        {
            JSONObject jsonParam = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            String date = sdf.format(new Date());
            jsonParam.put("Create Date", date);
            EditText info = (EditText) findViewById(R.id.grant);
            jsonParam.put("Grant Number String", info.getText().toString());
            info = (EditText) findViewById(R.id.institutionName);
            jsonParam.put("Institution Name", info.getText().toString());
            jsonParam.put("Modification Date", date);
            info = (EditText) findViewById(R.id.projName);
            jsonParam.put("Name", info.getText().toString());
            info = (EditText) findViewById(R.id.funding);
            jsonParam.put("Original Funding Agency", info.getText().toString());
           // info = (EditText) findViewById(R.id.prininvest);
           // Integer investigator = Integer.parseInt(info.getText().toString());
            jsonParam.put("Principal Investigator", 1);
            jsonParam.put("Started Date", date);
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            jsonParam.put("Unique Identifier", UUID.randomUUID().toString());
            return jsonParam;
        }
}
