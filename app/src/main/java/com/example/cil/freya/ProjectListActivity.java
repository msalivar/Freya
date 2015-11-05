package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectListActivity extends Activity
{
    ListView projectList;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        String[] projects = {"A", "B", "C", "A", "B", "C", "A", "B", "C", "A", "B", "C", "A", "B", "C", "A", "B", "C"};
        projectList = (ListView)findViewById(R.id.projectList);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, projects);
        projectList.setAdapter(listAdapter);
    }
}
