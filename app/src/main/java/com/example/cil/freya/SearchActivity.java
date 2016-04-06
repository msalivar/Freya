package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends MainActivity implements View.OnClickListener
{
    // Declare Variables
    EditText keyword;
    String[] types;
    ArrayList<String> values;
    Spinner typeSpinner;
    ListView searchView;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Open search activity layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        keyword = (EditText) findViewById(R.id.keyword);

        // Populate spinner with different modules
        types = new String[7];
        types[0] = "Project";
        types[1] = "Site";
        types[2] = "System";
        types[3] = "Deployment";
        types[4] = "Component";
        types[5] = "Document";
        types[6] = "Service Entry";

        // Display populated spinner on layout
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, types);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinAdapter);
        searchView = (ListView) findViewById(R.id.searchView);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case (R.id.searchButton):
                values = getSearchResults(keyword.getText().toString(), typeSpinner.getSelectedItem().toString());
                if (values.size() == 0)
                {
                    values.add("No Results Found");
                }
                ArrayAdapter<String> lAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, values);
                searchView.setAdapter(lAdapter);
                break;
        }
    }

    private ArrayList<String> getSearchResults(String keyword, String moduleType)
    {
        ArrayList<String> values = new ArrayList<>();
        JSONArray module;
        try
        {
            switch (moduleType)
            {
                case "Project":
                    module = getInfo.projects;
                    for (int i = 0; i < module.length(); i++)
                    {
                        JSONObject obj = module.getJSONObject(i);
                        if (obj.get("Grant Number String").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Institution Name").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Name").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Original Funding Agency").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (getPerson((Integer)obj.get("Principal Investigator")).toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                    }
                    break;
                case "Site":
                    module = getInfo.sites;
                    for (int i = 0; i < module.length(); i++)
                    {
                        JSONObject obj = module.getJSONObject(i);
                        if (obj.get("Alias").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Altitude").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("GPS Landmark").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Name").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Notes").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (getProject((Integer)obj.get("Project")).toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                    }
                    break;
                case "System":
                    module = getInfo.systems;
                    for (int i = 0; i < module.length(); i++)
                    {
                        JSONObject obj = module.getJSONObject(i);
                        if (obj.get("Details").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Installation Location").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Name").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Power").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (getPerson((Integer)obj.get("Manager")).toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (getSite((Integer)obj.get("Site")).toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                    }
                    break;
                case "Deployment":
                    module = getInfo.deployments;
                    for (int i = 0; i < module.length(); i++)
                    {
                        JSONObject obj = module.getJSONObject(i);
                        if (obj.get("Name").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Parent Logger").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Purpose").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("CenterOffset").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (obj.get("Height From Ground").toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                        if (getSystem((Integer)obj.get("System")).toString().toLowerCase().contains(keyword.toLowerCase()))
                        {
                            values.add(obj.get("Name").toString());
                        }
                    }
                    break;
                case "Component":
                    module = getInfo.components;
                    break;
                case "Document":
                    module = getInfo.documents;
                    break;
                case "Service Entry":
                    module = getInfo.services;
                    break;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return values;
    }

    public String getPerson(int personIndex)
    {
        try
        {
            String theirName = "error: not found";
            for (int i = 0; i < getInfo.people.length(); i++)
            {
                if (getInfo.people.getJSONObject(i).getInt("Person") == personIndex)
                {
                    JSONObject dude = getInfo.people.getJSONObject(i);
                    theirName = dude.getString("First Name") + " " + dude.getString("Last Name");
                    break;
                }
            }
            for (int i = 0; i < getInfo.peopleNames.length; i++)
            {
                if (Objects.equals(getInfo.peopleNames[i], theirName))
                {
                    return getInfo.peopleNames[i];
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public String getProject(int projectIndex)
    {
        try
        {
            String theirName = "error: not found";
            for (int i = 0; i < getInfo.projects.length(); i++)
            {
                if (getInfo.projects.getJSONObject(i).getInt("Project") == projectIndex)
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
                    return getInfo.projectNames[i];
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public String getSite(int siteIndex)
    {
        try
        {
            String theirName = "error: not found";
            for (int i = 0; i < getInfo.sites.length(); i++)
            {
                if (getInfo.sites.getJSONObject(i).getInt("Site") == siteIndex)
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
                    return getInfo.siteNames[i];
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public String getSystem(int systemIndex)
    {
        try
        {
            String theirName = "error: not found";
            for (int i = 0; i < getInfo.systems.length(); i++)
            {
                if (getInfo.systems.getJSONObject(i).getInt("System") == systemIndex)
                {
                    JSONObject dude = getInfo.systems.getJSONObject(i);
                    theirName = dude.getString("Name");
                    break;
                }
            }
            for (int i = 0; i < getInfo.systemNames.length; i++)
            {
                if (Objects.equals(getInfo.systemNames[i], theirName))
                {
                    return getInfo.systemNames[i];
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
