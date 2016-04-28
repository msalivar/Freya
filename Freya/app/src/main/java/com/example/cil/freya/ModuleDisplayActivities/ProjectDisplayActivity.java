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

public class ProjectDisplayActivity extends Activity implements View.OnClickListener
{
    EditText grant, name, funding, institution;
    Spinner investigator;
    Button saveButton;
    boolean unsyncedFlag = false;
    JSONObject thisProject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Project");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        grant = (EditText) findViewById(R.id.grant);
        name = (EditText) findViewById(R.id.compname);
        funding = (EditText) findViewById(R.id.funding);
        institution = (EditText) findViewById(R.id.institutionName);
        investigator = (Spinner) findViewById(R.id.prininvest);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        spinAdapter.setDropDownViewResource(R.layout.spinner_item);
        investigator.setAdapter(spinAdapter);
        getInfo(MainActivity.selectedModuleName);
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

            case R.id.delete_button:
                if (unsyncedFlag){
                    try
                    {
                        getInfo.unsynced.getJSONArray("Projects").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"You cannot delete data already synced to the server", Toast.LENGTH_LONG).show();
                }
                finish();
                return true;
            // TODO: local deletes only right now
          /*  case R.id.delete_button:
                try
                {
                    String tobeDeleted = getInfo.projects.getJSONObject(findEntry(MainActivity.selectedModuleName)).getString("Unique Identifier");
                    boolean check = true;

                    for (int i = 0; i < getInfo.deleted.size(); i++){
                        if (tobeDeleted == getInfo.deleted.get(i)){
                            check = false;
                        }
                    }

                if (check){
                    getInfo.deleted.add(tobeDeleted);
                    getInfo.deletedDisplay.put(getInfo.projects.getJSONObject(findEntry(MainActivity.selectedModuleName)));
                    MainActivity.ListHandler.addChild("Deleted", getInfo.projects.getJSONObject(findEntry(MainActivity.selectedModuleName)).getString("Name"));
                }

                else {
                    Toast.makeText(getBaseContext(), "Already in Delete Queue", Toast.LENGTH_LONG);
                }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                finish();
                return true;*/
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    private int findEntry(String name, JSONArray modules) throws JSONException
    {
        for(int i = 0; i < modules.length(); i++)
        {
            if (modules.getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private int findUnsyncedEntry(String name, JSONObject modules) throws JSONException
    {
        for(int i = 0; i < modules.getJSONArray("Projects").length(); i++)
        {
            if (modules.getJSONArray("Projects").getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private void getInfo(String entryName)
    {
        try {
            thisProject = getInfo.projects.getJSONObject(findEntry(entryName, getInfo.projects));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisProject = getInfo.unsynced.getJSONArray("Projects").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {
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
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
