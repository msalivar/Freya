package com.example.cil.freya.ModuleDisplayActivities;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cil.freya.MainActivity;
import com.example.cil.freya.R;
import com.example.cil.freya.getInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SiteDisplayActivity extends Activity implements View.OnClickListener
{
    EditText alias, landmark, location, landOwner, site_name, permit, doc_notes;
    Spinner project;
    Button cancelButton, saveButton;

    // TODO: Unfinished

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_display);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        alias = (EditText) findViewById(R.id.alias);
        landmark = (EditText) findViewById(R.id.landmark);
        location = (EditText) findViewById(R.id.location);
        landOwner = (EditText) findViewById(R.id.landOwner);
        site_name = (EditText) findViewById(R.id.site_name);
        permit = (EditText) findViewById(R.id.permit);
        doc_notes = (EditText) findViewById(R.id.doc_notes);
        project = (Spinner) findViewById(R.id.project);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInfo.projectNames);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project.setAdapter(sAdapter);
        getInfo(MainActivity.selectedModuleIndex);
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.sites;
        try
        {
            JSONObject thisService = modules.getJSONObject(projectIndex);
            alias.setText(thisService.getString("Alias"));
            landmark.setText(thisService.getString("GPS Landmark"));
            //location.setText(thisService.getString("Location"));
            //landOwner.setText(thisService.getString("Notes"));
            site_name.setText(thisService.getString("Name"));
            //permit.setText(thisService.getString("Notes"));
            doc_notes.setText(thisService.getString("Notes"));

            if (!thisService.isNull("Project"))
            {
                int pIndex = thisService.getInt("Project");
                String pName = "error: not found";
                for (int i = 0; i < getInfo.projects.length(); i++)
                {
                    if (getInfo.projects.getJSONObject(i).getInt("Project") == pIndex)
                    {
                        JSONObject dude = getInfo.projects.getJSONObject(i);
                        pName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.projectNames.length; i++)
                {
                    if (Objects.equals(getInfo.projectNames[i], pName))
                    {
                        project.setSelection(i);
                        break;
                    }
                }
            }
            else { project.setSelection(0); }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                // TODO: Write to files and stuff here
                finish();
                break;
            case (R.id.cancelButton):
                // Values will not be changed and work will be lost, maybe show a warning here?
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}