package com.example.cil.freya.ModuleDisplayActivities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cil.freya.MainActivity;
import com.example.cil.freya.R;
import com.example.cil.freya.SearchActivity;
import com.example.cil.freya.getInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class DocumentDisplayActivity extends Activity implements View.OnClickListener
{
    EditText name, notes, path;
    Spinner project, site, deployment, component, service_entry;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_display);

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

        getInfo(MainActivity.selectedModuleIndex);
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
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.documents;
        try
        {
            JSONObject thisProject = modules.getJSONObject(projectIndex);
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                // TODO: Write to files and stuff here
                finish();
                break;
//            case (R.id.cancelButton):
//                // Values will not be changed and work will be lost, maybe show a warning here?
//                finish();
//                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
