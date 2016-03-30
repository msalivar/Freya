package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by cil on 11/10/15.
 */
public class CreateNewProject extends MainActivity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    static String projectsURL = MainActivity.mainURL + MainActivity.edgeURL;
    Spinner prininvest;
    Button createButton;
    String ProjectFile = "ProjectFile.txt";
    private EditText txtEditor;
    int inNumb;
    EditText info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_project);
        createButton = (Button) findViewById(R.id.newProjectButton);
        createButton.setOnClickListener(this);
        prininvest = (Spinner) findViewById(R.id.prininvest);

        txtEditor = (EditText) findViewById(R.id.compname);
        try{
            Modules.read(ProjectFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}

        Modules.spinner (this, getInfo.peopleNames, prininvest);

    }
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.newProjectButton):
                try
                {newProject();} catch (JSONException e)
                {e.printStackTrace();}
                Intent intent = new Intent(this, CreateNewSite.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                try{
                    Modules.write(info, ProjectFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                break;
        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            inNumb = getInfo.peopleNumber[position-1];
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

        JSONArray project = new JSONArray();
        project.put(JSON);

        getInfo.complete.put("Projects", project);
    }

    public JSONObject createProjectJSON() throws JSONException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());

        info = (EditText) findViewById(R.id.institutionName);
        jsonParam.put("Institution Name", info.getText().toString());

        info = (EditText) findViewById(R.id.grant);
        jsonParam.put("Grant Number String", info.getText().toString());

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.compname);
        jsonParam.put("Name", info.getText().toString());

        jsonParam.put("Principal Investigator",inNumb);

        info = (EditText) findViewById(R.id.funding);
        jsonParam.put("Original Funding Agency", info.getText().toString());

        jsonParam.put("Modification Date", date);

        jsonParam.put("Creation Date", date);

        jsonParam.put("Started Date", date);

        return jsonParam;
    }
}