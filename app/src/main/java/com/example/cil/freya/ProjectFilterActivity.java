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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProjectFilterActivity extends Activity implements View.OnClickListener
{
    // Declare variables
    CustomListAdapter adapter = null;
    ArrayList<Boolean> checkValues = new ArrayList<>();
    Button saveButton;

    // Create project list screen
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Display new activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_filter);
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        for(Boolean entry : MainActivity.projectHideValues)
        {
            if (entry) { checkValues.add(true); }
            else { checkValues.add(false); }
        }
        // Options can be checked on the screen
        adapter = new CustomListAdapter(this, R.layout.row, MainActivity.projectEntries, checkValues);
        ListView listView = (ListView) findViewById(R.id.projectList);
        listView.setAdapter(adapter);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.saveButton):
                List<String> checked = new LinkedList<>();
                for (int i = 0; i < MainActivity.projectEntries.size(); i++)
                {
                    if (checkValues.get(i))
                    {
                        checked.add(MainActivity.projectEntries.get(i).getName());
                    }
                }
                MainActivity.listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, checked);
                MainActivity.projectList.setAdapter(MainActivity.listAdapter);
                finish();
        }
    }
}

