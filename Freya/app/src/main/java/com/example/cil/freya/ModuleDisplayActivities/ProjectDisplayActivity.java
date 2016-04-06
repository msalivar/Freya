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

public class ProjectDisplayActivity extends Activity implements View.OnClickListener
{
    EditText grant, name, funding, institution;
    Spinner investigator;
    Button cancelButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Project");

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        grant = (EditText) findViewById(R.id.grant);
        name = (EditText) findViewById(R.id.compname);
        funding = (EditText) findViewById(R.id.funding);
        institution = (EditText) findViewById(R.id.institutionName);
        investigator = (Spinner) findViewById(R.id.prininvest);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInfo.peopleNames);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        investigator.setAdapter(spinAdapter);
        getInfo(MainActivity.selectedModuleIndex);
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.projects;
        try
        {
            JSONObject thisProject = modules.getJSONObject(projectIndex);
            grant.setText(thisProject.getString("Grant Number String"));
            name.setText(thisProject.getString("Name"));
            funding.setText(thisProject.getString("Original Funding Agency"));
            institution.setText(thisProject.getString("Institution Name"));

            if (!thisProject.isNull("Principal Investigator"))
            {
                int personIndex = thisProject.getInt("Principal Investigator");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.people.length(); i++)
                {
                    if (getInfo.people.getJSONObject(i).getInt("Person") == personIndex)
                    {
                        JSONObject dude = getInfo.people.getJSONObject(i);
                        theirName = dude.getString("First Name") + " " + dude.getString("Last Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.peopleNames.length; i++)
                {
                    if (Objects.equals(getInfo.peopleNames[i], theirName))
                    {
                        investigator.setSelection(i);
                        break;
                    }
                }
            }
            else { investigator.setSelection(0); }
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
