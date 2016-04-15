package com.example.cil.freya.ModuleDisplayActivities;

import android.os.Bundle;
import android.app.Activity;

import com.example.cil.freya.R;

public class PeopleDisplayActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("People");
    }

}
