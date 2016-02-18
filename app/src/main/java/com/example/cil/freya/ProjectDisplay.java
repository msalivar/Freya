package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import org.json.JSONException;

// class to display projects list
public class ProjectDisplay extends Activity
{
    EditText pName;

    // when created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // set layout
        super.onCreate(savedInstanceState);
        // projects list
        setContentView(R.layout.activity_project_display);
        pName = (EditText) findViewById(R.id.pName);
        try
        {
            // get projects name from main activty. and display in activities list
            pName.setText(MainActivity.selected_project.getString("Name"));
        } catch (JSONException e)
        {
            // print exception to log cat
            e.printStackTrace();
        }
    }
}