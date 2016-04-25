package com.example.cil.freya;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matt on 4/17/2016.
 */
public class ExpandableListHandler
{
    public ExpandableListAdapter expandable;
    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;

    public ExpandableListAdapter prepareListData(Context c) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        //int amount_unsynced = getInfo.unsynced.length();
        List<String> unsynced = new ArrayList<String>();
        int headerCount = 0;

        // Adding child data
        if (getInfo.unsynced.length() > 0)
        {
            try
            {
                for(int i = 0; i < getInfo.unsynced.getJSONArray("Components").length(); i++)
                {
                    unsynced.add(getInfo.unsynced.getJSONArray("Components").getJSONObject(i).getString("Name") + " (Component)");
                }
            } catch (JSONException e) { e.printStackTrace(); }
            listDataHeader.add("Unsynced"); //(" + String.valueOf(amount_unsynced) + ")");
            listDataChild.put(listDataHeader.get(headerCount++), unsynced);
        }

        List<String> people = new ArrayList<String>();
        if (getInfo.peopleNames.length > 0)
        {
            Collections.addAll(people, getInfo.peopleNames);
            people.remove(0);
        }
        listDataHeader.add("People");
        listDataChild.put(listDataHeader.get(headerCount++), people);

        List<String> projects = new ArrayList<String>();
        if (getInfo.projectNames.length > 0)
        {
            Collections.addAll(projects, getInfo.projectNames);
            projects.remove(0);
        }
        listDataHeader.add("Projects");
        listDataChild.put(listDataHeader.get(headerCount++), projects);

        if (getInfo.siteNames.length > 0)
        {
            List<String> sites = new ArrayList<String>();
            for (int i = 0; i < getInfo.siteNames.length; i++)
            {
                if (!getInfo.siteHidden.get(i))
                {
                    sites.add(getInfo.siteNames[i]);
                }
            }
            if (sites.size() > 0)
            {
                sites.remove(0);
                listDataHeader.add("Sites");
                listDataChild.put(listDataHeader.get(headerCount++), sites);
            }
        }

        if (getInfo.systemNames.length > 0)
        {
            List<String> systems = new ArrayList<String>();
            for (int i = 0; i < getInfo.systemNames.length; i++)
            {
                if (!getInfo.systemHidden.get(i))
                {
                    systems.add(getInfo.systemNames[i]);
                }
            }
            if (systems.size() > 0)
            {
                systems.remove(0);
                listDataHeader.add("Systems");
                listDataChild.put(listDataHeader.get(headerCount++), systems);
            }
        }

        if (getInfo.deploymentNames.length > 0)
        {
            List<String> deployments = new ArrayList<String>();
            for (int i = 0; i < getInfo.deploymentNames.length; i++)
            {
                if (!getInfo.deploymentHidden.get(i))
                {
                    deployments.add(getInfo.deploymentNames[i]);
                }
            }
            if (deployments.size() > 0)
            {
                deployments.remove(0);
                listDataHeader.add("Deployments");
                listDataChild.put(listDataHeader.get(headerCount++), deployments);
            }
        }

        if (getInfo.componentNames.length > 0)
        {
            List<String> components = new ArrayList<String>();
            for (int i = 0; i < getInfo.componentNames.length; i++)
            {
                if (!getInfo.componentHidden.get(i))
                {
                    components.add(getInfo.componentNames[i]);
                }
            }
            if (components.size() > 0)
            {
                components.remove(0);
                listDataHeader.add("Components");
                listDataChild.put(listDataHeader.get(headerCount++), components);
            }
        }

        if (getInfo.documentNames.length > 0)
        {
            List<String> documents = new ArrayList<String>();
            for (int i = 0; i < getInfo.documentNames.length; i++)
            {
                if (!getInfo.documentHidden.get(i))
                {
                    documents.add(getInfo.documentNames[i]);
                }
            }
            if (documents.size() > 0)
            {
                documents.remove(0);
                listDataHeader.add("Documents");
                listDataChild.put(listDataHeader.get(headerCount++), documents);
            }
        }

        if (getInfo.serviceNames.length > 0)
        {
            List<String> serviceEntries = new ArrayList<String>();
            for (int i = 0; i < getInfo.serviceNames.length; i++)
            {
                if (!getInfo.serviceHidden.get(i))
                {
                    serviceEntries.add(getInfo.serviceNames[i]);
                }
            }
            if (serviceEntries.size() > 0)
            {
                serviceEntries.remove(0);
                listDataHeader.add("Service Entries");
                listDataChild.put(listDataHeader.get(headerCount++), serviceEntries);
            }
        }

        expandable = new ExpandableListAdapter(c, listDataHeader, listDataChild);
        return expandable;
    }

    public void toggleProject(int projectIndex, boolean toggle)
    {
        // sites, se, document
        JSONArray modules = getInfo.projects;
        try
        {
            JSONObject thisProject = modules.getJSONObject(projectIndex);
            int pIndex = thisProject.getInt("Project");

            JSONArray sites = getInfo.sites;
            for(int i = 0; i < sites.length(); i++)
            {
                if(sites.getJSONObject(i).getInt("Project") == thisProject.getInt("Project"))
                {
                    getInfo.siteHidden.set(i, toggle);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
