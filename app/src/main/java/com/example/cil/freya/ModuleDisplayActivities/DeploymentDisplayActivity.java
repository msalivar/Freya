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
import java.util.UUID;

public class DeploymentDisplayActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    EditText deployName, parentLogger, purpose, centerOffset, height, location;
    Spinner system;
    Button saveButton;
    EditText lat, longi, alt;
    boolean unsyncedFlag = false;
    JSONObject thisDeployment = null;
    int sysNum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deployment_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Deployment");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        deployName = (EditText) findViewById(R.id.deployname);
        parentLogger = (EditText) findViewById(R.id.parentLogger);
        purpose = (EditText) findViewById(R.id.purpose);
        centerOffset = (EditText) findViewById(R.id.centeroffset);
        height = (EditText) findViewById(R.id.height);

        Button location = (Button) findViewById(R.id.installation_location);
        location.setOnClickListener(this);

        lat = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        alt = (EditText) findViewById(R.id.altitude);

        system = (Spinner) findViewById(R.id.DeploySystem);
        Modules.spinner(this, getInfo.systemNames, system);
        getInfo(MainActivity.selectedModuleName);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            sysNum = getInfo.systemNumber[position - 1];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        //autogenerated
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.installation_location):
                String message = String.format("%f",MainActivity.mLastLocation.getLatitude());
                lat.setText(message);
                message = String.format("%f",MainActivity.mLastLocation.getLongitude());
                longi.setText(message);
                message = String.format("%f",MainActivity.mLastLocation.getAltitude());
                alt.setText(message);
                break;

            case (R.id.saveButton):
                if (unsyncedFlag)
                {

                    try
                    {
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
                        String date = sdf.format(new Date());

                        thisDeployment.put("Unique Identifier", UUID.randomUUID().toString());

                        EditText info = (EditText) findViewById(R.id.deployname);
                        thisDeployment.put("Name", info.getText().toString());

                        info = (EditText) findViewById(R.id.purpose);
                        thisDeployment.put("Purpose", info.getText().toString());

                        info = (EditText) findViewById(R.id.centeroffset);
                        thisDeployment.put("Center Offset", info.getText().toString());

                        //from gps
                        thisDeployment.put("Latitude", lat.getText().toString());
                        thisDeployment.put("Longitude", longi.getText().toString());
                        thisDeployment.put("Elevation", alt.getText().toString());

                        info = (EditText) findViewById(R.id.height);
                        thisDeployment.put("Height From Ground", info.getText().toString());

                        info = (EditText) findViewById(R.id.parentLogger);
                        thisDeployment.put("Parent Logger", info.getText().toString());

                        thisDeployment.put("System", sysNum);

                        thisDeployment.put("Abandoned Date", date);

                        thisDeployment.put("Modification Date", date);

                        MainActivity.ListHandler.addChild("Unsynced", thisDeployment.getString("Name"));

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
                        getInfo.unsynced.getJSONArray("Deployments").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

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
        for(int i = 0; i < modules.getJSONArray("Deployments").length(); i++)
        {
            if (modules.getJSONArray("Deployments").getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private void getInfo(String entryName)
    {

        try {
            thisDeployment = getInfo.deployments.getJSONObject(findEntry(entryName, getInfo.deployments));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisDeployment = getInfo.unsynced.getJSONArray("Deployments").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            deployName.setText(thisDeployment.getString("Name"));
            parentLogger.setText(thisDeployment.getString("Parent Logger"));
            purpose.setText(thisDeployment.getString("Purpose"));
            centerOffset.setText(thisDeployment.getString("Center Offset"));
            height.setText(thisDeployment.getString("Height From Ground"));
            longi.setText(thisDeployment.getString("Longitude"));
            lat.setText(thisDeployment.getString("Latitude"));
            alt.setText(thisDeployment.getString("Elevation"));

            if (!thisDeployment.isNull("System"))
            {
                int sIndex = thisDeployment.getInt("System");
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
