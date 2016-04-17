package com.example.cil.freya;

import android.content.Context;

import org.json.JSONException;

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
        //int amount_unsynced = getInfo.complete.length();
        List<String> unsynced = new ArrayList<String>();

        // Adding child data
        if (getInfo.complete.length() > 0)
        {
            listDataHeader.add("Unsynced"); //(" + String.valueOf(amount_unsynced) + ")");
            if (getInfo.complete.length() > 0)
            {
                try
                {
                    for(int i = 0; i < getInfo.complete.getJSONArray("Components").length(); i++)
                    {
                        unsynced.add(getInfo.complete.getJSONArray("Components").getJSONObject(i).getString("Name") + " (Component)");
                    }
                } catch (JSONException e) { e.printStackTrace(); }
            }

        }
        listDataHeader.add("People");
        listDataHeader.add("Projects");
        listDataHeader.add("Sites");
        listDataHeader.add("Systems");
        listDataHeader.add("Deployments");
        listDataHeader.add("Components");
        listDataHeader.add("Documents");
        listDataHeader.add("Service Entries");

        // Adding child data
        List<String> people = new ArrayList<String>();
        if (getInfo.peopleNames.length> 0)
        {
            Collections.addAll(people, getInfo.peopleNames);
            people.remove(0);
        }
        List<String> projects = new ArrayList<String>();
        if (getInfo.projectNames.length> 0)
        {
            Collections.addAll(projects, getInfo.projectNames);
            projects.remove(0);
        }
        List<String> sites = new ArrayList<String>();
        if (getInfo.siteNames.length > 0)
        {
            Collections.addAll(sites, getInfo.siteNames);
            sites.remove(0);
        }
        List<String> systems = new ArrayList<String>();
        if (getInfo.systemNames.length > 0)
        {
            Collections.addAll(systems, getInfo.systemNames);
            systems.remove(0);
        }
        List<String> deployments = new ArrayList<String>();
        if (getInfo.deploymentNames.length > 0)
        {
            Collections.addAll(deployments, getInfo.deploymentNames);
            deployments.remove(0);
        }
        List<String> components = new ArrayList<String>();
        if (getInfo.componentNames.length > 0)
        {
            Collections.addAll(components, getInfo.componentNames);
            components.remove(0);
        }
        List<String> documents = new ArrayList<String>();
        if (getInfo.documentNames.length > 0)
        {
            Collections.addAll(documents, getInfo.documentNames);
            documents.remove(0);
        }
        List<String> serviceEntries = new ArrayList<String>();
        if (getInfo.serviceNames.length > 0)
        {
            Collections.addAll(serviceEntries, getInfo.serviceNames);
            serviceEntries.remove(0);
        }

        if (getInfo.complete.length() > 0)
        {
            listDataChild.put(listDataHeader.get(0), unsynced);
            listDataChild.put(listDataHeader.get(1), people); // Header, Child data
            listDataChild.put(listDataHeader.get(2), projects);
            listDataChild.put(listDataHeader.get(3), sites);
            listDataChild.put(listDataHeader.get(4), systems);
            listDataChild.put(listDataHeader.get(5), deployments);
            listDataChild.put(listDataHeader.get(6), components);
            listDataChild.put(listDataHeader.get(7), documents);
            listDataChild.put(listDataHeader.get(8), serviceEntries);
        }
        else
        {
            listDataChild.put(listDataHeader.get(0), people); // Header, Child data
            listDataChild.put(listDataHeader.get(1), projects);
            listDataChild.put(listDataHeader.get(2), sites);
            listDataChild.put(listDataHeader.get(3), systems);
            listDataChild.put(listDataHeader.get(4), deployments);
            listDataChild.put(listDataHeader.get(5), components);
            listDataChild.put(listDataHeader.get(6), documents);
            listDataChild.put(listDataHeader.get(7), serviceEntries);
        }

        expandable = new ExpandableListAdapter(c, listDataHeader, listDataChild);
        return expandable;
    }
}
