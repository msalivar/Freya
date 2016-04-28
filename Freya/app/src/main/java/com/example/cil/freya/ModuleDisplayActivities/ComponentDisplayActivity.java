package com.example.cil.freya.ModuleDisplayActivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ComponentDisplayActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    EditText installation, manufacturer, model, compname, serial_number, supplier, vendor, wiring_notes;
    Spinner deployment;
    Button saveButton;
    EditText info;
    int deploymentNumb;
    boolean unsyncedFlag = false;
    JSONObject thisComponent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Component");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        installation = (EditText) findViewById(R.id.installation);
        manufacturer = (EditText) findViewById(R.id.manufacturer);
        model = (EditText) findViewById(R.id.model);
        compname = (EditText) findViewById(R.id.compname);
        serial_number = (EditText) findViewById(R.id.serial_number);
        supplier = (EditText) findViewById(R.id.supplier);
        vendor = (EditText) findViewById(R.id.vendor);
        wiring_notes = (EditText) findViewById(R.id.wiring_notes);
        deployment = (Spinner) findViewById(R.id.deployment);
        ArrayAdapter<String> componentAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.deploymentNames);
        componentAdapter.setDropDownViewResource(R.layout.spinner_item);
        deployment.setAdapter(componentAdapter);
        deployment.setOnItemSelectedListener(this);
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
                        getInfo.unsynced.getJSONArray("Components").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                else {
                    Toast.makeText(getBaseContext(),"You cannot delete data already synced to the server", Toast.LENGTH_LONG).show();
                }
             /*   try
                {
                    new CRUD.deleteMessage().execute(getInfo.components.getJSONObject(findEntry(MainActivity.selectedModuleName)).getString("Unique Identifier"));
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }*/


                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            deploymentNumb = getInfo.deploymentNumber[position - 1];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {}


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
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
                        String date = sdf.format(new Date());

                        info = (EditText) findViewById(R.id.compname);
                        thisComponent.put("Name", info.getText().toString());

                        info = (EditText) findViewById(R.id.manufacturer);
                        thisComponent.put("Manufacturer", info.getText().toString());

                        info = (EditText) findViewById(R.id.model);
                        thisComponent.put("Model",info.getText().toString());

                        info = (EditText) findViewById(R.id.serial_number);
                        thisComponent.put("Serial Number", info.getText().toString());

                        // may need to be a spinner
                        info = (EditText) findViewById(R.id.vendor);
                        thisComponent.put("Vendor", info.getText().toString());

                        info = (EditText) findViewById(R.id.supplier);
                        thisComponent.put("Supplier", info.getText().toString());

                        info = (EditText) findViewById(R.id.installation);
                        thisComponent.put("Installation Details", info.getText().toString());

                        info = (EditText) findViewById(R.id.wiring_notes);
                        thisComponent.put("Wiring Notes", info.getText().toString());

                        // needs spinner
                        thisComponent.put("Deployment", deploymentNumb);

                        thisComponent.put("Modification Date", date);

                        thisComponent.put("Last Calibrated Date", date);
                        MainActivity.ListHandler.addChild("Unsynced", thisComponent.getString("Name"));

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


    private int findUnsyncedEntry(String name, JSONObject modules) throws JSONException
    {
        for(int i = 0; i < modules.getJSONArray("Components").length(); i++)
        {
            if (modules.getJSONArray("Components").getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }

    private void getInfo(String entryName)
    {

        try {
            thisComponent = getInfo.components.getJSONObject(findEntry(entryName, getInfo.components));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisComponent = getInfo.unsynced.getJSONArray("Components").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            installation.setText(thisComponent.getString("Installation Details"));
            manufacturer.setText(thisComponent.getString("Manufacturer"));
            model.setText(thisComponent.getString("Model"));
            compname.setText(thisComponent.getString("Name"));
            serial_number.setText(thisComponent.getString("Serial Number"));
            supplier.setText(thisComponent.getString("Supplier"));
            vendor.setText(thisComponent.getString("Vendor"));
            wiring_notes.setText(thisComponent.getString("Wiring Notes"));

            if (!thisComponent.isNull("Deployment"))
            {
                int deploymentIndex = thisComponent.getInt("Deployment");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.deployments.length(); i++)
                {
                    if (getInfo.deployments.getJSONObject(i).getInt("Deployment") == deploymentIndex)
                    {
                        JSONObject dude = getInfo.deployments.getJSONObject(i);
                        theirName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.deploymentNames.length; i++)
                {
                    if (Objects.equals(getInfo.deploymentNames[i], theirName))
                    {
                        deployment.setSelection(i);
                        break;
                    }
                }
            }
            else { deployment.setSelection(0); }
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
