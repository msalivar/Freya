package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


/**
 * Created by cil on 2/5/16.
 */
public class CreateNewDocument extends NavigationDrawer implements View.OnClickListener, Spinner.OnItemSelectedListener
{
     int proNumb, siteNumb, deployNumb, componentNumb, serviceNumb;
     String DocumentFile = "DocumentFile.txt";
     EditText info;

        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.document_display);
            super.onCreateDrawer(savedInstanceState);

            getActionBar().setTitle("Create New Document");
            Button saveButton = (Button) findViewById(R.id.saveButton);
            saveButton.setOnClickListener(this);

            Spinner selectedProject = (Spinner) findViewById(R.id.document_project);
            Spinner selectedSite = (Spinner) findViewById(R.id.document_site);
            Spinner selectedDeployment = (Spinner) findViewById(R.id.document_deployment);
            Spinner selectedComponent = (Spinner) findViewById(R.id.document_component);
            Spinner selectedServiceEntry = (Spinner) findViewById(R.id.document_service);

            Modules.spinner(this, getInfo.projectNames, selectedProject);
            Modules.spinner(this, getInfo.siteNames, selectedSite);
            Modules.spinner(this, getInfo.deploymentNames, selectedDeployment);
            Modules.spinner(this, getInfo.componentNames, selectedComponent);
            Modules.spinner(this, getInfo.serviceNames, selectedServiceEntry);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.sync).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try
                {newDocument();} catch (JSONException e) {e.printStackTrace();}
                overridePendingTransition(0, 0);
                try{
                    Modules.write(info, DocumentFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                finish();
                break;

        }

    }

        public void newDocument()throws JSONException
        {
            //Create JSONObject here
            JSONObject JSON = createDocumentJSON();

            JSONArray document = new JSONArray();
            document.put(JSON);

            getInfo.complete.put("Documents", document);
        }

        public JSONObject createDocumentJSON() throws JSONException{
            JSONObject jsonParam = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            String date = sdf.format(new Date());
            EditText info = null;

            jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

            info = (EditText) findViewById(R.id.document_name);
            jsonParam.put("Name",info.getText().toString());

            info = (EditText) findViewById(R.id.doc_notes);
            jsonParam.put("Notes",info.getText().toString());

            info = (EditText) findViewById(R.id.path);
            jsonParam.put("Path", info.getText().toString());

            jsonParam.put("Creation Date", date);

            jsonParam.put("Modification Date", date);

            jsonParam.put("Project", proNumb);

            jsonParam.put("Site", siteNumb);

            jsonParam.put("Deployment", deployNumb);

            jsonParam.put("Component", componentNumb);

            jsonParam.put("Service Entry", serviceNumb);

            return jsonParam;
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
       switch (parent.getId()){
           case(R.id.document_project):
               if (position > 0)
                   proNumb = getInfo.projectNumber[position - 1];
               break;

           case(R.id.document_site):
               if (position > 0)
                   siteNumb = getInfo.siteNumber[position - 1];
               break;

           case (R.id.document_deployment):
               if (position > 0)
                   deployNumb = getInfo.deploymentNumber[position - 1];
               break;

           case (R.id.document_component):
               if (position > 0)
                   componentNumb = getInfo.componentNumber[position - 1];
               break;

           case (R.id.document_service):
               if (position > 0)
                   serviceNumb = getInfo.serviceNumber[position - 1];
               break;
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    // automatically generated
    }


}
