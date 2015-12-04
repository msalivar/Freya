package com.example.cil.freya;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProjectListActivity extends Activity
{
    ListView projectList;
    ArrayAdapter<String> listAdapter;
    String[] projects;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        if (savedInstanceState != null)
        {
            projects = savedInstanceState.getStringArray(null);
            Toast.makeText(this, "load state", Toast.LENGTH_LONG).show();

        } else
        {
            try
            {
                projects = new String[MainActivity.projectNames.length];
                for (int i = 0; i < MainActivity.projectNames.length; i++)
                {
                    projects[i] = MainActivity.projectNames[i];
                   // SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                    //SharedPreferences.Editor editor = preferences.edit();

                }


                projectList = (ListView) findViewById(R.id.projectList);
                listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, projects);
                projectList.setAdapter(listAdapter);
            }catch (NullPointerException e){
                Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}

 /*   //@Override
    public void onSavedInstanceState (Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putStringArray(null, projects);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause (Bundle savedInstanceState){
    super.onPause();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(projects);
        editor.commit();
    }

    public void onRestoreInstanceState (Bundle savedInstanceState){

        super.onRestoreInstanceState(savedInstanceState);
        projects = savedInstanceState.getStringArray(null);
        Toast.makeText(this, "Restore", Toast.LENGTH_LONG).show();

    }*/




