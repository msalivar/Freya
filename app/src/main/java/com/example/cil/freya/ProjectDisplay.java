package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import org.json.JSONException;

public class ProjectDisplay extends Activity
{
    EditText pName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_display);
        pName = (EditText) findViewById(R.id.pName);
        try
        {
            pName.setText(MainActivity.selected_project.getString("Name"));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
