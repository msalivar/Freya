package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
public class CreateNewProject extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    static String projectsURL = MainActivity.mainURL + MainActivity.edgeURL;
    Spinner prininvest;
    String ProjectFile = "ProjectFile.txt";
    private EditText txtEditor;
    int inNumb;
    EditText info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_display);

        // Set Title
        getActionBar().setTitle("Create New Project");

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        prininvest = (Spinner) findViewById(R.id.prininvest);

        txtEditor = (EditText) findViewById(R.id.compname);

        try{
            Modules.read(ProjectFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}

        Modules.spinner(this, getInfo.peopleNames, prininvest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.sync).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try
                {newProject();} catch (JSONException e){e.printStackTrace();}
                overridePendingTransition(0, 0);
                try{
                    Modules.write(info, ProjectFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                finish();
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