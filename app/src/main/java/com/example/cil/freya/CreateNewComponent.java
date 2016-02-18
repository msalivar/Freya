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
 * Created by sammie on 12/7/15.
 */
public class CreateNewComponent extends MainActivity {

    // display create new component GUI
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_components);
    }


    public void newComponent() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createComponentJSON();

        JSONArray system = new JSONArray();
        system.put(JSON);

        CreateNewProject.complete.put("Component", system);
    }

    public JSONObject createComponentJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());


        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        EditText info = (EditText) findViewById(R.id.projName);
        jsonParam.put("Name", info.getText().toString());

        info = (EditText) findViewById(R.id.manufacturer);
        jsonParam.put("Manufacturer", info.getText().toString());

        info = (EditText) findViewById(R.id.model);
        jsonParam.put("Model",info);

        info = (EditText) findViewById(R.id.serial_number);
        jsonParam.put("Serial Number", info.getText().toString());

        info = (EditText) findViewById(R.id.supplier);
        jsonParam.put("Supplier", info.getText().toString());

        info = (EditText) findViewById(R.id.serial_number);
        jsonParam.put("Serial Number", info.getText().toString());

        info = (EditText) findViewById(R.id.installation);
        jsonParam.put("installation Details", info.getText().toString());

        jsonParam.put("Installation Date", date);

        jsonParam.put("Modification Date", date);

        jsonParam.put("Last Calibrated Date", date);

        jsonParam.put("Creation Date", date);

        return jsonParam;
    }
}
