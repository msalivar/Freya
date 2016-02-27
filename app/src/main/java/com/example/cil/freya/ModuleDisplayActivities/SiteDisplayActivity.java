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

public class SiteDisplayActivity extends Activity //implements View.OnClickListener
{
    /*EditText alias, landmark, location, landOwner, site_name, permit, doc_notes;
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
        ArrayAdapter<String> pAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInfo.projectNames);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project.setAdapter(pAdapter);
        getInfo(MainActivity.selectedModuleIndex);
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.services;
        try
        {
            JSONObject thisService = modules.getJSONObject(projectIndex);
            alias.setText(thisService.getString("Alias"));
            landmark.setText(thisService.getString("GPS Landmark"));
            location.setText(thisService.getString("Location"));
            landOwner.setText(thisService.getString("Notes"));
            site_name.setText(thisService.getString("Notes"));
            permit.setText(thisService.getString("Notes"));
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

            if (!thisService.isNull("Creator"))
            {
                int cIndex = thisService.getInt("Creator");
                String cName = "error: not found";
                for (int i = 0; i < getInfo.people.length(); i++)
                {
                    if (getInfo.people.getJSONObject(i).getInt("Person") == cIndex)
                    {
                        JSONObject dude = getInfo.people.getJSONObject(i);
                        cName = dude.getString("First Name") + " " + dude.getString("Last Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.peopleNames.length; i++)
                {
                    if (Objects.equals(getInfo.peopleNames[i], cName))
                    {
                        creator.setSelection(i);
                        break;
                    }
                }
            }
            else { creator.setSelection(0); }

            if (!thisService.isNull("System"))
            {
                int sIndex = thisService.getInt("System");
                String sName = "error: not found";
                for (int i = 0; i < getInfo.systems.length(); i++)
                {
                    if (getInfo.systems.getJSONObject(i).getInt("System") == sIndex)
                    {
                        JSONObject dude = getInfo.systems.getJSONObject(i);
                        sName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.systemNames.length; i++)
                {
                    if (Objects.equals(getInfo.systemNames[i], sName))
                    {
                        system.setSelection(i);
                        break;
                    }
                }
            }
            else { system.setSelection(0); }

            if (!thisService.isNull("Component"))
            {
                int compIndex = thisService.getInt("Component");
                String compName = "error: not found";
                for (int i = 0; i < getInfo.components.length(); i++)
                {
                    if (getInfo.components.getJSONObject(i).getInt("Component") == compIndex)
                    {
                        JSONObject dude = getInfo.components.getJSONObject(i);
                        compName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.componentNames.length; i++)
                {
                    if (Objects.equals(getInfo.componentNames[i], compName))
                    {
                        component.setSelection(i);
                        break;
                    }
                }
            }
            else { component.setSelection(0); }

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
    }*/
}
