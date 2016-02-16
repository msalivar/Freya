package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by cil on 11/10/15.
 */
public class CreateNewProject extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    static String projectsURL = MainActivity.mainURL + MainActivity.edgeURL;
    Spinner prininvest;
    Button createButton;
    JSONArray edge;
    static JSONObject complete = new JSONObject();
    String ProjectFile = "ProjectFile.txt";
    private EditText txtEditor;
    int inNumb;


    //TODO
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_project);
        // create the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0)
        {
            public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
            public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
        };

        // enable drawer listener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        addDrawerItems();



        createButton = (Button) findViewById(R.id.newProjectButton);
        createButton.setOnClickListener(this);
        prininvest = (Spinner) findViewById(R.id.prininvest);

        txtEditor = (EditText) findViewById(R.id.projName);
        try
        {
            components.read(ProjectFile, this);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        try
        {
            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getInfo.investigators);
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prininvest.setAdapter(spinAdapter);
            prininvest.setOnItemSelectedListener(this);
        } catch (NullPointerException e)
        {
            Toast.makeText(this, "Unable to populate People. Sync before trying again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void addDrawerItems()
    {
        // list of options
        String[] osArray = {"Project Options", "Create New Project", "Create New Site", "Create New System", "Create New Deployment", "Create New Component"};
        //  display and set listener
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.menu_layout, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        // got to onItemClick
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    private void selectItem(int position)
    {
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image*//*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);*/


        switch (position)
        {

            default:
                // close the drawer
                mDrawerList.setItemChecked(position, true);
                //getActionBar().setTitle(mDrawerList.getItemAtPosition(position).toString());
                mDrawerLayout.closeDrawer(mDrawerList);
        }

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.newProjectButton):
                try
                {
                    newProject();
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, CreateNewSite.class);
                startActivity(intent);
                try
                {
                    components.read(ProjectFile, this);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            inNumb = getInfo.investNumber[position - 1];
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        //autogenerated, do nothing
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return true;
    }


    public void newProject() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createProjectJSON();

        JSONArray project = new JSONArray();
        project.put(JSON);

        complete.put("Project", project);

        // edge = MainActivity.createEdge(complete);
    }

    public JSONObject createProjectJSON() throws JSONException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        EditText info = null;

        info = (EditText) findViewById(R.id.institutionName);
        jsonParam.put("Institution Name", info.getText().toString());

        info = (EditText) findViewById(R.id.grant);
        jsonParam.put("Grant Number String", info.getText().toString());

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.projName);
        jsonParam.put("Name", info.getText().toString());


        jsonParam.put("Principal Investigator", 1);

        info = (EditText) findViewById(R.id.funding);
        jsonParam.put("Original Funding Agency", info.getText().toString());

        jsonParam.put("Modification Date", date);

        jsonParam.put("Creation Date", date);

        jsonParam.put("Started Date", date);

        return jsonParam;
    }
}
