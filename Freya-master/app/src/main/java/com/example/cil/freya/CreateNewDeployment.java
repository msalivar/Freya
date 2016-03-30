package com.example.cil.freya;

import android.content.Intent;
import android.os.Bundle;
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
public class CreateNewDeployment extends MainActivity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    Button createButton;
    String DeploymentFile = "DeploymentFile.txt";
    EditText info = null;
    int sysNum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_deployment);
        createButton = (Button) findViewById(R.id.newDeploymentButton);
        createButton.setOnClickListener(this);

        Spinner deploysys = (Spinner) findViewById(R.id.DeploySystem);
        Modules.spinner(this, getInfo.systemNames, deploysys);

        try{
            Modules.read(DeploymentFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}
    }

    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case (R.id.newDeploymentButton):
                try
                {newDeployment();} catch (JSONException e) {e.printStackTrace();}
                overridePendingTransition(0, 0);
                try{
                    Modules.write(info, DeploymentFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                finish();
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

        JSONArray deployment = new JSONArray();
        deployment.put(JSON);

        getInfo.complete.put("Deployments", deployment);
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

        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Location", info.getText().toString());

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



}