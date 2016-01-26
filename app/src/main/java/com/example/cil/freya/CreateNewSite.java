package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by cil on 11/18/15.
 */
public class CreateNewSite extends Activity implements View.OnClickListener
{
    static String projectsURL = MainActivity.mainURL + MainActivity.edgeURL;
    Button createButton, previousButton;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_site);
        createButton = (Button) findViewById(R.id.newSiteButton);
        createButton.setOnClickListener(this);
        previousButton = (Button) findViewById(R.id.backSiteButton);
        previousButton.setOnClickListener(this);
    }


    public void onClick (View v){
        Intent intent;
        switch (v.getId()){
            case (R.id.newSiteButton):
                try
                { newSite();} catch (JSONException e) {e.printStackTrace();}

                intent = new Intent(this, CreateNewSystem.class);
                startActivity(intent);
                break;

            case (R.id.backSiteButton):
                intent = new Intent(this, CreateNewProject.class);
                startActivity(intent);
                break;
        }
    }


    public void newSite()throws JSONException{

        //Create JSONObject here
        JSONObject JSON = createSiteJSON();

        JSONArray between = new JSONArray();
        between.put(JSON);

        JSONObject attrs = new JSONObject();
        attrs.put ("Attrs",between);


        JSONArray site = new JSONArray();
        site.put(attrs);

        CreateNewProject.complete.put("Site", site);

    }

    public JSONObject createSiteJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        EditText info = (EditText) findViewById(R.id.alias_label);
        jsonParam.put("Alias", info.getText().toString());
        info = (EditText) findViewById(R.id.landmark);
        jsonParam.put("GPS Landmark", info.getText().toString());
        jsonParam.put("Land Owner", null);
        jsonParam.put("Landmark Photo", null);
        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Location",info.getText().toString());
        info = (EditText) findViewById(R.id.site_name);
        jsonParam.put("Name",info.getText().toString());
        info = (EditText) findViewById(R.id.notes);
        jsonParam.put("Notes",info.getText().toString());
        info = (EditText) findViewById(R.id.permit);
        jsonParam.put("Permit Holder",info.getText().toString());
        jsonParam.put("Project",null);
        jsonParam.put("Site",null);
        jsonParam.put("Time Zone Abbreviation", null);
        jsonParam.put("Time Zone Name", null);
        jsonParam.put("Time Zone Offset", null);
        jsonParam.put("Alias",info);
        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());
        return jsonParam;
    }
}