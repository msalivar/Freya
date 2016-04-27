package com.example.cil.freya;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

public class ProjectFilterActivity extends MainActivity implements View.OnClickListener
{
    // Declare variables
    CustomListAdapter adapter = null;
    Button saveButton;

    public ProjectFilterActivity()
    {
        super();
    }

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
            if (entry) { MainActivity.checkValues.add(true); }
            else { MainActivity.checkValues.add(false); }
        }
        // Options can be checked on the screen
        adapter = new CustomListAdapter(this, R.layout.row, MainActivity.projectEntries, MainActivity.checkValues);
        ListView listView = (ListView) findViewById(R.id.projectList);
        listView.setAdapter(adapter);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.saveButton):
                String toWrite = "";
                for (int i = 0; i < MainActivity.projectEntries.size(); i++)
                {
                    toWrite += MainActivity.projectEntries.get(i).getName() + ";" + MainActivity.checkValues.get(i).toString() + ',';
                }
                try
                {
                    write(toWrite);
                    List<String> checked = new LinkedList<>();
                    for (int i = 0; i < MainActivity.projectEntries.size(); i++)
                    {
                        if (MainActivity.checkValues.get(i))
                        {
                            checked.add(MainActivity.projectEntries.get(i).getName());
                        }
                    }
                    // TODO: Update Adapter
                    //MainActivity.listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, checked);
                    //MainActivity.projectList.setAdapter(MainActivity.listAdapter);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                finish();
        }
    }

    public void write(String input) throws FileNotFoundException
    {
        try {
            FileOutputStream FileOut = openFileOutput(MainActivity.ProjectFile, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(FileOut);
            outputWriter.write(input);
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

