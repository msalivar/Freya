package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by cil on 1/21/16.
 */
public class CreateNewDeployment extends Activity implements View.OnClickListener
{
    Button createButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_deployment);
        createButton = (Button) findViewById(R.id.newDeploymentButton);
        createButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.backDeploymentButton);
        backButton.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case (R.id.newDeploymentButton):
                try
                {newDeployment();} catch (JSONException e) {e.printStackTrace();}
                intent = new Intent(this, CreateNewComponent.class);
                startActivity(intent);
                break;

            case (R.id.backDeploymentButton):
                intent = new Intent(this, CreateNewSystem.class);
                startActivity(intent);
                break;
        }
    }

    public void newDeployment() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createDeploymentJSON();

        JSONArray system = new JSONArray();
        system.put(JSON);

        CreateNewProject.complete.put("Deployment", system);
    }

    public JSONObject createDeploymentJSON() throws JSONException{

        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        EditText info = null;

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.projName);
        jsonParam.put("Name", info);

        info = (EditText) findViewById(R.id.purpose);
        jsonParam.put("Purpose", info);

        info = (EditText) findViewById(R.id.centeroffset);
        jsonParam.put("Center Offset", info);

        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Location", info);

        info = (EditText) findViewById(R.id.height);
        jsonParam.put("Height From Ground", info);

        info = (EditText) findViewById(R.id.parentLogger);

        jsonParam.put("Parent Logger", info);

        jsonParam.put("Established Date", date);

        jsonParam.put("Abandoned Date", null);

        jsonParam.put("Creation Date", date);

        jsonParam.put("Modification Date", date);

        return jsonParam;
    }

}
