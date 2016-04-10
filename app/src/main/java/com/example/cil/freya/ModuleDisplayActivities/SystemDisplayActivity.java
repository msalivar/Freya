package com.example.cil.freya.ModuleDisplayActivities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
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
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("System");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        details = (EditText) findViewById(R.id.details);
        location = (EditText) findViewById(R.id.installation_location);
        power = (EditText) findViewById(R.id.power);
        sysname = (EditText) findViewById(R.id.sysname);
        manager = (Spinner) findViewById(R.id.manager);
        ArrayAdapter<String> managerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        managerAdapter.setDropDownViewResource(R.layout.spinner_item);
        manager.setAdapter(managerAdapter);
        site = (Spinner) findViewById(R.id.site);
        ArrayAdapter<String> siteAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.siteNames);
        siteAdapter.setDropDownViewResource(R.layout.spinner_item);
        site.setAdapter(siteAdapter);
        getInfo(MainActivity.selectedModuleIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.activity_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch(menu.getItemId()){
            case R.id.cancel_button:
                finish();
                return true;
            case R.id.upload_photo:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
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
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
