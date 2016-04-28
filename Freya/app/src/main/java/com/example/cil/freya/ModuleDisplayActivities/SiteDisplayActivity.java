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

public class SiteDisplayActivity extends Activity implements View.OnClickListener
{
    EditText alias, landmark, location, site_name, doc_notes;
    Spinner project, permit, landOwner;
    Button saveButton;
    EditText lat, longi, alt;
    boolean unsyncedFlag = false;

    // TODO: Unfinished

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Site");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        alias = (EditText) findViewById(R.id.alias);
        landmark = (EditText) findViewById(R.id.landmark);
        location = (EditText) findViewById(R.id.longitude);
        landOwner = (Spinner) findViewById(R.id.landOwner);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        landOwner.setAdapter(sAdapter);

        site_name = (EditText) findViewById(R.id.site_name);

        permit = (Spinner) findViewById(R.id.permit);
        sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        permit.setAdapter(sAdapter);

        doc_notes = (EditText) findViewById(R.id.doc_notes);
        project = (Spinner) findViewById(R.id.project);
        sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.projectNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        project.setAdapter(sAdapter);

        lat = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        alt = (EditText) findViewById(R.id.altitude);

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
                if (unsyncedFlag)
                {
                    try
                    {
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        getInfo.unsynced.getJSONArray("Sites").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"You cannot delete data already synced to the server", Toast.LENGTH_LONG).show();
                }
                overridePendingTransition(0, 0);
                finish();
                return true;

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
        JSONArray t = modules.getJSONArray("Sites");
        for(int i = 0; i < t.length(); i++)
        {
            if (t.getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private void getInfo(String entryName)
    {
        JSONObject thisService = null;
        try {
            thisService = getInfo.sites.getJSONObject(findEntry(entryName, getInfo.sites));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisService = getInfo.unsynced.getJSONArray("Sites").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {

            alias.setText(thisService.getString("Alias"));
            landmark.setText(thisService.getString("GPS Landmark"));
            //location.setText(thisService.getString("Location"));
            //landOwner.setText(thisService.getString("Notes"));
            site_name.setText(thisService.getString("Name"));
            //permit.setText(thisService.getString("Notes"));
            doc_notes.setText(thisService.getString("Notes"));
            longi.setText(thisService.getString("Longitude"));
            lat.setText(thisService.getString("Latitude"));
            alt.setText(thisService.getString("Elevation"));

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



            if (!thisService.isNull("Permit Holder"))
            {
                int cIndex = thisService.getInt("Permit Holder");
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
                        permit.setSelection(i);
                        break;
                    }
                }
            }
            else { permit.setSelection(0); }


            if (!thisService.isNull("Land Owner"))
            {
                int cIndex = thisService.getInt("Land Owner");
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
                        landOwner.setSelection(i);
                        break;
                    }
                }
            }
            else { landOwner.setSelection(0); }


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
