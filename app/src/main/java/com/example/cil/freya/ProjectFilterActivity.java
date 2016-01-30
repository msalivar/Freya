package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class ProjectFilterActivity extends Activity implements View.OnClickListener
{
    // Declare variables
    ListView projectList;
    Button saveButton;

    // Create project list screen
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Display new activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_filter);
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        projectList = (ListView)findViewById(R.id.projectList);
        // Options can be checked on the screen
        CustomListAdapter adapter = new CustomListAdapter(this, MainActivity.projectEntries);
        projectList.setAdapter(adapter);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.saveButton):
                List<String> checked = new LinkedList<>();
                for (ProjectEntry projectEntry : MainActivity.projectEntries)
                {
                    if (projectEntry.getValue())
                    {
                        checked.add(projectEntry.getName());
                    }
                }
                MainActivity.listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, checked);
                MainActivity.projectList.setAdapter(MainActivity.listAdapter);
                finish();
        }
    }
}

