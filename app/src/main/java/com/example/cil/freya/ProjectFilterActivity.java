package com.example.cil.freya;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class ProjectFilterActivity extends Activity
{
    // Declare variables
    ListView projectList;
    ProjectEntry[] listItems;

    // Create project list screen
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Display new activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_filter);
        projectList = (ListView)findViewById(R.id.projectList);
        
        // Populate list with a template
        // Options can be checked on the screen
        listItems = new ProjectEntry[5];
        listItems[0] = new ProjectEntry("One", false);
        listItems[1] = new ProjectEntry("Two", true);
        listItems[2] = new ProjectEntry("Three", true);
        listItems[3] = new ProjectEntry("Four", true);
        listItems[4] = new ProjectEntry("Five", true);
        CustomListAdapter adapter = new CustomListAdapter(this, listItems);
        projectList.setAdapter(adapter);
    }
}

