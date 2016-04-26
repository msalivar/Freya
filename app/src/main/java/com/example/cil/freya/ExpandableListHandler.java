package com.example.cil.freya;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Matt on 4/17/2016.
 */
public class ExpandableListHandler
{
    public List<String> listDataHeader = Arrays.asList("Unsynced", "People", "Projects", "Sites",
            "Systems", "Deployments", "Components", "Documents", "Service Entries");
    public HashMap<String, List<String>> listDataChild = new HashMap<>();
    public ExpandableListAdapter expandable;

    public ArrayList<String> unsyncedChildren = new ArrayList<>();
    public ArrayList<String> peopleChildren = new ArrayList<>();
    public ArrayList<String> projectChildren = new ArrayList<>();
    public ArrayList<String> siteChildren = new ArrayList<>();
    public ArrayList<String> systemChildren = new ArrayList<>();
    public ArrayList<String> deploymentChildren = new ArrayList<>();
    public ArrayList<String> componentChildren = new ArrayList<>();
    public ArrayList<String> documentChildren = new ArrayList<>();
    public ArrayList<String> serviceChildren = new ArrayList<>();

    public ExpandableListHandler(Context c)
    {
        for (String str : getInfo.peopleNames)
        {
            if (str.equals("Choose Person")) { continue; }
            peopleChildren.add(str);
        }
        for (String str : getInfo.projectNames)
        {
            if (str.equals("Choose Project")) { continue; }
            projectChildren.add(str);
        }

        listDataChild.put("Unsynced", unsyncedChildren);
        listDataChild.put("People", peopleChildren);
        listDataChild.put("Projects", projectChildren);
        listDataChild.put("Sites", siteChildren);
        listDataChild.put("Systems", systemChildren);
        listDataChild.put("Deployments", deploymentChildren);
        listDataChild.put("Components", componentChildren);
        listDataChild.put("Documents", documentChildren);
        listDataChild.put("Service Entries", serviceChildren);

        expandable = new ExpandableListAdapter(c, listDataHeader, listDataChild);
    }

    public void resetChildren()
    {
        peopleChildren = new ArrayList<>();
        projectChildren = new ArrayList<>();
        siteChildren = new ArrayList<>();
        systemChildren = new ArrayList<>();
        deploymentChildren = new ArrayList<>();
        componentChildren = new ArrayList<>();
        documentChildren = new ArrayList<>();
        serviceChildren = new ArrayList<>();
        expandable.notifyDataSetChanged();
    }

    public void addChild(String parentName, String childName) {
        switch (parentName)
        {
            case "Unsynced":
                if (!unsyncedChildren.contains(childName)) { unsyncedChildren.add(childName); }
                listDataChild.put(parentName, unsyncedChildren);
                break;
            case "People":
                if (!peopleChildren.contains(childName)) { peopleChildren.add(childName); }
                listDataChild.put(parentName, peopleChildren);
                break;
            case "Projects":
                if (!projectChildren.contains(childName)) { projectChildren.add(childName); }
                listDataChild.put(parentName, projectChildren);
                break;
            case "Sites":
                if (!siteChildren.contains(childName)) { siteChildren.add(childName); }
                listDataChild.put(parentName, siteChildren);
                break;
            case "Systems":
                if (!systemChildren.contains(childName)) { systemChildren.add(childName); }
                listDataChild.put(parentName, systemChildren);
                break;
            case "Deployments":
                if (!deploymentChildren.contains(childName)) { deploymentChildren.add(childName); }
                listDataChild.put(parentName, deploymentChildren);
                break;
            case "Components":
                if (!componentChildren.contains(childName)) { componentChildren.add(childName); }
                listDataChild.put(parentName, componentChildren);
                break;
            case "Documents":
                if (!documentChildren.contains(childName)) { documentChildren.add(childName); }
                listDataChild.put(parentName, documentChildren);
                break;
            case "Service Entries":
                if (!serviceChildren.contains(childName)) { serviceChildren.add(childName); }
                listDataChild.put(parentName, serviceChildren);
                break;
        }
        expandable.notifyDataSetChanged();
    }

    public void removeChild(String parentName, String childName) {
        switch (parentName)
        {
            case "Unsynced":
                while (unsyncedChildren.contains(childName))
                {
                    unsyncedChildren.remove(childName);
                }
                listDataChild.put(parentName, unsyncedChildren);
                break;
            case "People":
                while (peopleChildren.contains(childName))
                {
                    peopleChildren.remove(childName);
                }
                listDataChild.put(parentName, peopleChildren);
                break;
            case "Projects":
                while (projectChildren.contains(childName))
                {
                    projectChildren.remove(childName);
                }
                listDataChild.put(parentName, projectChildren);
                break;
            case "Sites":
                while (siteChildren.contains(childName))
                {
                    siteChildren.remove(childName);
                }
                listDataChild.put(parentName, siteChildren);
                break;
            case "Systems":
                while (systemChildren.contains(childName))
                {
                    systemChildren.remove(childName);
                }
                listDataChild.put(parentName, systemChildren);
                break;
            case "Deployments":
                while (deploymentChildren.contains(childName))
                {
                    deploymentChildren.remove(childName);
                }
                listDataChild.put(parentName, deploymentChildren);
                break;
            case "Components":
                while (componentChildren.contains(childName))
                {
                    componentChildren.remove(childName);
                }
                listDataChild.put(parentName, componentChildren);
                break;
            case "Documents":
                while (documentChildren.contains(childName))
                {
                    documentChildren.remove(childName);
                }
                listDataChild.put(parentName, documentChildren);
                break;
            case "Service Entries":
                while (serviceChildren.contains(childName))
                {
                    serviceChildren.remove(childName);
                }
                listDataChild.put(parentName, serviceChildren);
                break;
        }
        expandable.notifyDataSetChanged();
    }

    public void toggleProject(int index, boolean toggle)
    {
        JSONArray modules = getInfo.projects;
        try
        {
            JSONObject thisEntry = modules.getJSONObject(index);
            JSONArray sites = getInfo.sites;
            for(int i = 0; i < sites.length(); i++)
            {
                if(sites.getJSONObject(i).get("Project") == thisEntry.get("Project"))
                {
                    if (toggle) { removeChild("Sites", sites.getJSONObject(i).getString("Name")); }
                    else { addChild("Sites", sites.getJSONObject(i).getString("Name")); }
                }
            }
            JSONArray services = getInfo.services;
            for(int i = 0; i < services.length(); i++)
            {
                if(services.getJSONObject(i).get("Project") == thisEntry.get("Project"))
                {
                    if (toggle) { removeChild("Service Entries", services.getJSONObject(i).getString("Name")); }
                    else { addChild("Service Entries", services.getJSONObject(i).getString("Name")); }
                }
            }
            JSONArray documents = getInfo.documents;
            for(int i = 0; i < documents.length(); i++)
            {
                if(documents.getJSONObject(i).get("Project") == thisEntry.get("Project"))
                {
                    if (toggle) { removeChild("Documents", documents.getJSONObject(i).getString("Name")); }
                    else { addChild("Documents", documents.getJSONObject(i).getString("Name")); }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void toggleSite(int index, boolean toggle)
    {
        JSONArray modules = getInfo.sites;
        try
        {
            JSONObject thisEntry = modules.getJSONObject(index);
            JSONArray systems = getInfo.systems;
            for(int i = 0; i < systems.length(); i++)
            {
                if(systems.getJSONObject(i).get("Site") == thisEntry.get("Site"))
                {
                    if (toggle) { removeChild("Systems", systems.getJSONObject(i).getString("Name")); }
                    else { addChild("Systems", systems.getJSONObject(i).getString("Name")); }
                }
            }
            JSONArray documents = getInfo.documents;
            for(int i = 0; i < documents.length(); i++)
            {
                if(documents.getJSONObject(i).get("Site") == thisEntry.get("Site"))
                {
                    if (toggle) { removeChild("Documents", documents.getJSONObject(i).getString("Name")); }
                    else { addChild("Documents", documents.getJSONObject(i).getString("Name")); }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void toggleSystem(int index, boolean toggle)
    {
        JSONArray modules = getInfo.systems;
        try
        {
            JSONObject thisEntry = modules.getJSONObject(index);
            JSONArray services = getInfo.services;
            for(int i = 0; i < services.length(); i++)
            {
                if(services.getJSONObject(i).get("System") == thisEntry.get("System"))
                {
                    if (toggle) { removeChild("Service Entries", services.getJSONObject(i).getString("Name")); }
                    else { addChild("Service Entries", services.getJSONObject(i).getString("Name")); }
                }
            }
            JSONArray deployments = getInfo.deployments;
            for(int i = 0; i < deployments.length(); i++)
            {
                if(deployments.getJSONObject(i).get("System") == thisEntry.get("System"))
                {
                    if (toggle) { removeChild("Deployments", deployments.getJSONObject(i).getString("Name")); }
                    else { addChild("Deployments", deployments.getJSONObject(i).getString("Name")); }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void toggleDeployment(int index, boolean toggle)
    {
        JSONArray modules = getInfo.deployments;
        try
        {
            JSONObject thisEntry = modules.getJSONObject(index);
            JSONArray components = getInfo.components;
            for(int i = 0; i < components.length(); i++)
            {
                if(components.getJSONObject(i).get("Deployment") == thisEntry.get("Deployment"))
                {
                    if (toggle) { removeChild("Components", components.getJSONObject(i).getString("Name")); }
                    else { addChild("Components", components.getJSONObject(i).getString("Name")); }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
