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

public class DeploymentDisplayActivity extends Activity implements View.OnClickListener
{
    EditText deployName, parentLogger, purpose, centerOffset, height, location;
    Spinner system;
    Button cancelButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deployment_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Deployment");

//        cancelButton = (Button) findViewById(R.id.cancelButton);
//        cancelButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        deployName = (EditText) findViewById(R.id.deployname);
        parentLogger = (EditText) findViewById(R.id.parentLogger);
        purpose = (EditText) findViewById(R.id.purpose);
        centerOffset = (EditText) findViewById(R.id.centeroffset);
        height = (EditText) findViewById(R.id.height);
        location = (EditText) findViewById(R.id.location);

        system = (Spinner) findViewById(R.id.DeploySystem);
        ArrayAdapter<String> siteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getInfo.systemNames);
        siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        system.setAdapter(siteAdapter);
        getInfo(MainActivity.selectedModuleIndex);
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.deployments;
        try
        {
            JSONObject thisDeployment = modules.getJSONObject(projectIndex);
            deployName.setText(thisDeployment.getString("Name"));
            parentLogger.setText(thisDeployment.getString("Parent Logger"));
            purpose.setText(thisDeployment.getString("Purpose"));
            centerOffset.setText(thisDeployment.getString("CenterOffset"));
            height.setText(thisDeployment.getString("Height From Ground"));
            //location.setText(thisSystem.getString("Location"));

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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                // TODO: Write to files and stuff here
                finish();
                break;
//            case (R.id.cancelButton):
//                // Values will not be changed and work will be lost, maybe show a warning here?
//                finish();
//                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
