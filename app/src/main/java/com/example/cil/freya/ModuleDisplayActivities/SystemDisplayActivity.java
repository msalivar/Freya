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

public class SystemDisplayActivity extends Activity implements View.OnClickListener
{
    EditText details, location, power, sysname;
    Spinner manager, site;
    Button cancelButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("System");

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        details = (EditText) findViewById(R.id.details);
        location = (EditText) findViewById(R.id.installation_location);
        power = (EditText) findViewById(R.id.power);
        sysname = (EditText) findViewById(R.id.sysname);
        manager = (Spinner) findViewById(R.id.manager);
        ArrayAdapter<String> managerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInfo.peopleNames);
        managerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manager.setAdapter(managerAdapter);
        site = (Spinner) findViewById(R.id.site);
        ArrayAdapter<String> siteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInfo.siteNames);
        siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        site.setAdapter(siteAdapter);
        getInfo(MainActivity.selectedModuleIndex);
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.systems;
        try
        {
            JSONObject thisSystem = modules.getJSONObject(projectIndex);
            details.setText(thisSystem.getString("Details"));
            location.setText(thisSystem.getString("Installation Location"));
            power.setText(thisSystem.getString("Power"));
            sysname.setText(thisSystem.getString("Name"));

            if (!thisSystem.isNull("Manager"))
            {
                int personIndex = thisSystem.getInt("Manager");
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
                        manager.setSelection(i);
                        break;
                    }
                }
            }
            else { manager.setSelection(0); }

            if (!thisSystem.isNull("Site"))
            {
                int siteIndex = thisSystem.getInt("Site");
                String siteName = "error: not found";
                for (int i = 0; i < getInfo.sites.length(); i++)
                {
                    if (getInfo.sites.getJSONObject(i).getInt("Site") == siteIndex)
                    {
                        JSONObject dude = getInfo.sites.getJSONObject(i);
                        siteName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.siteNames.length; i++)
                {
                    if (Objects.equals(getInfo.siteNames[i], siteName))
                    {
                        site.setSelection(i);
                        break;
                    }
                }
            }
            else { site.setSelection(0); }

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
