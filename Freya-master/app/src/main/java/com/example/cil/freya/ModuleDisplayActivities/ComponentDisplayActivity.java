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

public class ComponentDisplayActivity extends Activity implements View.OnClickListener
{
    EditText installation, manufacturer, model, compname, serial_number, supplier, vendor, wiring_notes;
    Spinner deployment;
    Button cancelButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_display);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
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
        ArrayAdapter<String> componentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInfo.deploymentNames);
        componentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deployment.setAdapter(componentAdapter);
        getInfo(MainActivity.selectedModuleIndex);
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.components;
        try
        {
            JSONObject thisComponent = modules.getJSONObject(projectIndex);
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