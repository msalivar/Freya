package com.example.cil.freya.ModuleDisplayActivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cil.freya.MainActivity;
import com.example.cil.freya.Modules;
import com.example.cil.freya.R;
import com.example.cil.freya.getInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProjectDisplayActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    EditText grant, name, funding, institution;
    Spinner investigator;
    Button saveButton;
    boolean unsyncedFlag = false;
    JSONObject thisProject = null;
    int inNumb;

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
        Modules.spinner(this, getInfo.peopleNames, investigator);

        getInfo(MainActivity.selectedModuleName);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                if (unsyncedFlag)
                {
                    try
                    {
                        // TODO: Write to files and stuff here
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
                        String date = sdf.format(new Date());

                        EditText info = (EditText) findViewById(R.id.institutionName);
                        thisProject.put("Institution Name", info.getText().toString());

                        info = (EditText) findViewById(R.id.grant);
                        thisProject.put("Grant Number String", info.getText().toString());

                        info = (EditText) findViewById(R.id.compname);
                        thisProject.put("Name", info.getText().toString());

                        thisProject.put("Principal Investigator", inNumb);

                        info = (EditText) findViewById(R.id.funding);
                        thisProject.put("Original Funding Agency", info.getText().toString());

                        thisProject.put("Modification Date", date);

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"Cannot edit data already synced to the Server", Toast.LENGTH_LONG);
                }

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
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        getInfo.unsynced.getJSONArray("Projects").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

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
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
