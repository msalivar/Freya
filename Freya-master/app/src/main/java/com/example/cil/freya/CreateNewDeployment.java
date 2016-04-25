package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by cil on 1/21/16.
 */
public class CreateNewDeployment extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    String DeploymentFile = "DeploymentFile.txt";
    EditText info = null;
    int sysNum;
    EditText lat, longi, alt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deployment_display);

        getActionBar().setTitle("Create New Deployment");
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        Spinner deploysys = (Spinner) findViewById(R.id.DeploySystem);
        Modules.spinner(this, getInfo.systemNames, deploysys);

        Button location = (Button) findViewById(R.id.installation_location);
        location.setOnClickListener(this);

        lat = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        alt = (EditText) findViewById(R.id.altitude);

        try{
            Modules.read(DeploymentFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.sync).setVisible(false);
        menu.findItem(R.id.upload).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try
                {newDeployment();} catch (JSONException e) {e.printStackTrace();}
                overridePendingTransition(0, 0);
                try{
                    Modules.write(info, DeploymentFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                finish();
                break;

            case (R.id.installation_location):
                String message = String.format("%f",MainActivity.mLastLocation.getLatitude());
                lat.setText(message);
                message = String.format("%f",MainActivity.mLastLocation.getLongitude());
                longi.setText(message);
                message = String.format("%f",MainActivity.mLastLocation.getAltitude());
                alt.setText(message);
                break;

        }
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

    public void newDeployment() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createDeploymentJSON();

        if (getInfo.unsynced.isNull("Deployments"))
        {
            JSONArray deployment = new JSONArray();
            deployment.put(JSON);
            getInfo.unsynced.put("Deployments", deployment);
        }
        else
        {
            getInfo.unsynced.getJSONArray("Deployments").put(JSON);
        }
        MainActivity.expListView.setAdapter(MainActivity.ListHandler.prepareListData(MainActivity.getContext()));
    }

    public JSONObject createDeploymentJSON() throws JSONException{

        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.deployname);
        jsonParam.put("Name", info.getText().toString());

        info = (EditText) findViewById(R.id.purpose);
        jsonParam.put("Purpose", info.getText().toString());

        info = (EditText) findViewById(R.id.centeroffset);
        jsonParam.put("Center Offset", info.getText().toString());

        //from gps
        jsonParam.put("Latitude", lat.getText().toString());
        jsonParam.put("Longitude", longi.getText().toString());
        jsonParam.put("Elevation", alt.getText().toString());

        info = (EditText) findViewById(R.id.height);
        jsonParam.put("Height From Ground", info.getText().toString());

        info = (EditText) findViewById(R.id.parentLogger);
        jsonParam.put("Parent Logger", info.getText().toString());

        jsonParam.put("System", sysNum);

        jsonParam.put("Established Date", date);

        jsonParam.put("Abandoned Date", date);

        jsonParam.put("Creation Date", date);

        jsonParam.put("Modification Date", date);

        return jsonParam;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch(menu.getItemId()){
            case R.id.cancel_button:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

}
