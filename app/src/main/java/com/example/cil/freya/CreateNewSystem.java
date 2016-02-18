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
 * Created by cil on 11/19/15.
 */
public class CreateNewSystem extends MainActivity implements View.OnClickListener
{
    Button createButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_systems);
        createButton = (Button) findViewById(R.id.newSystemButton);
        createButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.backSystemButton);
        backButton.setOnClickListener(this);

        /*try {
            read();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case (R.id.newSystemButton):
                try
                {newSystem();} catch (JSONException e) {e.printStackTrace();}
                intent = new Intent(this, CreateNewDeployment.class);
<<<<<<< HEAD
                 startActivity(intent);
=======
                startActivity(intent);
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb
//                try {
//                    write();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                break;

            case (R.id.backSystemButton):
                intent = new Intent(this, CreateNewSite.class);
                startActivity(intent);
//                try {
//                    write();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                break;
        }
    }

    public void newSystem() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createSystemJSON();

        JSONArray system = new JSONArray();
        system.put(JSON);

        CreateNewProject.complete.put("System", system);
    }

    public JSONObject createSystemJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        jsonParam.put("Create Date", date);
        EditText info = (EditText) findViewById(R.id.details);
        jsonParam.put("Details", info.getText().toString());

        //from gps
        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Installation Location",info);

<<<<<<< HEAD
        info = (EditText) findViewById(R.id.name);
=======
        //info = (EditText) findViewById(R.id.name);
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb
        jsonParam.put("Name", info.getText().toString());

        jsonParam.put("Create Date", date);

        jsonParam.put("Installation Date", date);



        jsonParam.put("Manager", null);
        jsonParam.put("Modification Date", date);

        info = (EditText) findViewById(R.id.power);
        jsonParam.put("Power", info.getText().toString());
        jsonParam.put("Site", null);
        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        return jsonParam;
    }

}
