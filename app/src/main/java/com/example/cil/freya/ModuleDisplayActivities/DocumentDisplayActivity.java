package com.example.cil.freya.ModuleDisplayActivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cil.freya.MainActivity;
import com.example.cil.freya.R;
import com.example.cil.freya.getInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DocumentDisplayActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    int proNumb, siteNumb, deployNumb, componentNumb, serviceNumb;
    EditText name, notes, path;
    Spinner project, site, deployment, component, service_entry;
    Button saveButton;
    boolean unsyncedFlag = false;

    JSONObject thisProject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Document");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        name = (EditText) findViewById(R.id.document_name);
        notes = (EditText) findViewById(R.id.doc_notes);
        path = (EditText) findViewById(R.id.path);

        project = (Spinner) findViewById(R.id.document_project);
        ArrayAdapter<String> pAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.projectNames);
        pAdapter.setDropDownViewResource(R.layout.spinner_item);
        project.setAdapter(pAdapter);

        site = (Spinner) findViewById(R.id.document_site);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.siteNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        site.setAdapter(sAdapter);

        deployment = (Spinner) findViewById(R.id.document_deployment);
        ArrayAdapter<String> dAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.deploymentNames);
        dAdapter.setDropDownViewResource(R.layout.spinner_item);
        deployment.setAdapter(dAdapter);

        component = (Spinner) findViewById(R.id.document_component);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.componentNames);
        cAdapter.setDropDownViewResource(R.layout.spinner_item);
        component.setAdapter(cAdapter);

        service_entry = (Spinner) findViewById(R.id.document_service);
        ArrayAdapter<String> serAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.serviceNames);
        serAdapter.setDropDownViewResource(R.layout.spinner_item);
        service_entry.setAdapter(serAdapter);

        getInfo(MainActivity.selectedModuleName);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                if (unsyncedFlag)
                {

                    try
                    {
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
                        String date = sdf.format(new Date());

                        EditText info = (EditText) findViewById(R.id.document_name);
                        thisProject.put("Name", info.getText().toString());

                        info = (EditText) findViewById(R.id.doc_notes);
                        thisProject.put("Notes", info.getText().toString());

                        info = (EditText) findViewById(R.id.path);
                        thisProject.put("Path", info.getText().toString());

                        thisProject.put("Modification Date", date);

                        thisProject.put("Project", proNumb);

                        thisProject.put("Site", siteNumb);

                        thisProject.put("Deployment", deployNumb);

                        thisProject.put("Component", componentNumb);

                        thisProject.put("Service Entry", serviceNumb);

                        MainActivity.ListHandler.addChild("Unsynced", thisProject.getString("Name"));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"Cannot edit data already synced to the Server", Toast.LENGTH_LONG);
                }

                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case (R.id.document_project):
                if (position > 0)
                    proNumb = getInfo.projectNumber[position - 1];
                break;

            case (R.id.document_site):
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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.activity_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch(menu.getItemId()){
            case R.id.cancel_button:
                finish();
                return true;

            case R.id.delete_button:
                if (unsyncedFlag)
                {
                    try
                    {
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        getInfo.unsynced.getJSONArray("Documents").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                Toast.makeText(getBaseContext(),"You cannot delete data already synced to the server", Toast.LENGTH_LONG).show();
            }
                overridePendingTransition(0, 0);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    private int findEntry(String name, JSONArray modules) throws JSONException
    {
        for(int i = 0; i < modules.length(); i++)
        {
            if (modules.getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private int findUnsyncedEntry(String name, JSONObject modules) throws JSONException
    {
        for(int i = 0; i < modules.getJSONArray("Documents").length(); i++)
        {
            if (modules.getJSONArray("Documents").getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }

    private void getInfo(String entryName)
    {

        try {
            thisProject = getInfo.documents.getJSONObject(findEntry(entryName, getInfo.documents));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisProject = getInfo.unsynced.getJSONArray("Documents").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            name.setText(thisProject.getString("Name"));
            notes.setText(thisProject.getString("Notes"));
            path.setText(thisProject.getString("Path"));

            if (!thisProject.isNull("Project"))
            {
                int pIndex = thisProject.getInt("Project");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.projects.length(); i++)
                {
                    if (getInfo.projects.getJSONObject(i).getInt("Project") == pIndex)
                    {
                        JSONObject dude = getInfo.projects.getJSONObject(i);
                        theirName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.projectNames.length; i++)
                {
                    if (Objects.equals(getInfo.projectNames[i], theirName))
                    {
                        project.setSelection(i);
                        break;
                    }
                }
            }
            else { project.setSelection(0); }

            if (!thisProject.isNull("Site"))
            {
                int personIndex = thisProject.getInt("Site");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.sites.length(); i++)
                {
                    if (getInfo.sites.getJSONObject(i).getInt("Site") == personIndex)
                    {
                        JSONObject dude = getInfo.sites.getJSONObject(i);
                        theirName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.siteNames.length; i++)
                {
                    if (Objects.equals(getInfo.siteNames[i], theirName))
                    {
                        site.setSelection(i);
                        break;
                    }
                }
            }
            else { site.setSelection(0); }

            if (!thisProject.isNull("Deployment"))
            {
                int personIndex = thisProject.getInt("Deployment");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.deployments.length(); i++)
                {
                    if (getInfo.deployments.getJSONObject(i).getInt("Deployment") == personIndex)
                    {
                        JSONObject dude = getInfo.deployments.getJSONObject(i);
                        theirName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.deploymentNames.length; i++)
                {
                    if (Objects.equals(getInfo.deploymentNames[i], theirName))
                    {
                        deployment.setSelection(i);
                        break;
                    }
                }
            }
            else { deployment.setSelection(0); }


            if (!thisProject.isNull("Component"))
            {
                int personIndex = thisProject.getInt("Component");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.components.length(); i++)
                {
                    if (getInfo.components.getJSONObject(i).getInt("Component") == personIndex)
                    {
                        JSONObject dude = getInfo.components.getJSONObject(i);
                        theirName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.componentNames.length; i++)
                {
                    if (Objects.equals(getInfo.componentNames[i], theirName))
                    {
                        component.setSelection(i);
                        break;
                    }
                }
            }
            else { component.setSelection(0); }


            if (!thisProject.isNull("Service Entry"))
            {
                int personIndex = thisProject.getInt("Service Entry");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.services.length(); i++)
                {
                    if (getInfo.services.getJSONObject(i).getInt("Service Entry") == personIndex)
                    {
                        JSONObject dude = getInfo.services.getJSONObject(i);
                        theirName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.serviceNames.length; i++)
                {
                    if (Objects.equals(getInfo.serviceNames[i], theirName))
                    {
                        service_entry.setSelection(i);
                        break;
                    }
                }
            }
            else { service_entry.setSelection(0); }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
