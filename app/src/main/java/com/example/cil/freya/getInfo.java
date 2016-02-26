package com.example.cil.freya;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by cil on 2/5/16.
 */
public class getInfo
{
    // put JSONs in here
    //  static String projectUID [];
    // 0 value is "choose investigator"
    static JSONArray projects;
    static String projectNames[];
    static int  projectNumber[];
    static JSONArray people;
    static String peopleNames[];
    static int peopleNumber[];
    static JSONArray sites;
    static String siteNames[];
    static int siteNumber[];
    static JSONArray systems;
    static String systemNames[];
    static int systemNumber[];
    static JSONArray deployments;
    static String deploymentNames[];
    static int deploymentNumber[];
    static JSONArray components;
    static String componentNames[];
    static int componentNumber[];
    static JSONArray documents;
    static String documentNames[];
    static int documentNumber[];
    static JSONArray services;
    static String serviceNames[];
    static int serviceNumber[];

    static JSONObject complete = new JSONObject();


    static public void getAllRequests (){
        getPeople();
        getProjects();
        getSites();
        getSystems();
        getDeployment();
        getComponent();
        getDocuments();
        getService();
    }

    static public JSONArray convertObjectToArray(String convert, String Type)
    {
        JSONArray array = null;
        try
        {
            JSONObject obj = new JSONObject(convert);
            array = obj.getJSONArray(Type);
        } catch (JSONException e)
        {e.printStackTrace();}

        return array;
    }

    // get peopleJSON from server
    static public void getPeople()
    {
        String people_str = null;
        // try to read from URL
        try {
            // set up from the URL
            people_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.peopleURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        // try to fill JSON
        people = convertObjectToArray(people_str, "People");
        // create people list
        PeopleNames(people);
    }

    // get stuff from the people JSON
    static void PeopleNames (JSONArray people) {
        // tries to get the JSON
        try {
            // if there are entries, keeps app from crashing
            if (people != null) {
                // gather investigator list
                peopleNames = new String[people.length() + 1];
                peopleNames[0] = "Choose Person";
                peopleNumber = new int[people.length()];
                // combine first and last name. put in investigator array
                for (int i = 0; i < people.length(); i++) {
                    JSONObject p = (JSONObject) people.get(i);
                    peopleNames[i + 1] = p.getString("First Name") + " " + p.getString("Last Name");
                    peopleNumber[i] = p.getInt("Person");
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
    }


    static public void getProjects()
    {
        String projects_str = "";
        try {
            // set up from the URL
            projects_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.projectsURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        projects = convertObjectToArray(projects_str,"Projects");
        // create projects list
        ProjectNames(projects);
    }


    // get projects json
    static void ProjectNames (JSONArray projects){
        // tries to get the JSON
        try
        {
            // if there are entries, keeps it from crashing the app
            if (projects != null)
            {
                // gets list of project names and their unique ids
                projectNames = new String[projects.length()+1];
                projectNames[0] = "Choose Project";
                projectNumber = new int [projects.length()];
                // projectUID = new String [projects.length()];
                for (int i = 0; i < projects.length(); i++)
                {
                    JSONObject p = (JSONObject) projects.get(i);
                    projectNames[i+1] = p.getString("Name");
                    /// projectUID[i] = p.getString("Unique Identifier");
                    projectNumber[i] = projects.getJSONObject(i).getInt("Project");
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
    }

    // get siteJSON from server
    static public void getSites()
    {
        String site_str = null;
        // try to read from URL
        try {
            // set up from the URL
            site_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.siteURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        // try to fill JSON
        sites = convertObjectToArray(site_str, "Sites");
        // create people list
        siteNames(sites);
    }

    static void siteNames (JSONArray sites){
        // tries to get the JSON
        try
        {
            // if there are entries, keeps it from crashing the app
            if (sites != null)
            {
                // gets list of project names and their unique ids
                siteNames = new String[sites.length()+1];
                siteNames[0] = "Select Site";
                siteNumber = new int [sites.length()];
                for (int i = 0; i < sites.length(); i++)
                {
                    JSONObject p = (JSONObject) sites.get(i);
                    siteNames[i+1] = p.getString("Name");
                    siteNumber[i] = sites.getJSONObject(i).getInt("Site");
                }
            }
        } catch (JSONException e) { e.printStackTrace();}
    }


    static public void getSystems()
    {
        String system_str = null;
        // try to read from URL
        try {
            // set up from the URL
            system_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.systemURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        // try to fill JSON
        systems = convertObjectToArray(system_str, "Systems");
        // create people list
        systemNames(systems);
    }

    static void systemNames (JSONArray system){
        // tries to get the JSON
        try
        {
            // if there are entries, keeps it from crashing the app
            if (system != null)
            {
                // gets list of project names and their unique ids
                systemNames = new String[system.length()+1];
                systemNames [0] = "Choose System";
                systemNumber = new int [system.length()];
                for (int i = 0; i < system.length(); i++)
                {
                    JSONObject p = (JSONObject) system.get(i);
                    systemNames[i+1] = p.getString("Name");
                    systemNumber[i] = system.getJSONObject(i).getInt("System");
                }
            }
        } catch (JSONException e) { e.printStackTrace();}
    }

    static public void getDeployment()
    {
        String deployment_str = null;
        // try to read from URL
        try {
            // set up from the URL
            deployment_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.deploymentURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        // try to fill JSON
        deployments = convertObjectToArray(deployment_str, "Deployments");
        // create people list
        deploymentNames(deployments);
    }

    static void deploymentNames (JSONArray deployment){
        // tries to get the JSON
        try
        {
            // if there are entries, keeps it from crashing the app
            if (deployment != null)
            {
                // gets list of project names and their unique ids
                deploymentNames = new String[deployment.length()+1];
                deploymentNames[0] = "Choose Deployment";
                deploymentNumber = new int [deployment.length()];
                for (int i = 0; i < deployment.length(); i++)
                {
                    JSONObject p = (JSONObject) deployment.get(i);
                    deploymentNames[i+1] = p.getString("Name");
                    deploymentNumber[i] = deployment.getJSONObject(i).getInt("Deployment");
                }
            }
        } catch (JSONException e) { e.printStackTrace();}
    }

    static public void getComponent()
    {
        String component_str = null;
        // try to read from URL
        try {
            // set up from the URL
            component_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.componentURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        // try to fill JSON
        components = convertObjectToArray(component_str, "Components");
        // create people list
        componentNames(components);
    }

    static void componentNames (JSONArray component){
        // tries to get the JSON
        try
        {
            // if there are entries, keeps it from crashing the app
            if (component != null)
            {
                // gets list of project names and their unique ids
                componentNames = new String[component.length()+1];
                componentNames[0] = "Choose Component";
                componentNumber = new int [component.length()];
                for (int i = 0; i < component.length(); i++)
                {
                    JSONObject p = (JSONObject) component.get(i);
                    componentNames[i+1] = p.getString("Name");
                    componentNumber[i] = component.getJSONObject(i).getInt("Component");
                }
            }
        } catch (JSONException e) { e.printStackTrace();}
    }

    static public void getDocuments()
    {
        String document_str = null;
        // try to read from URL
        try {
            // set up from the URL
            document_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.documentURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        // try to fill JSON
        // TODO: Why does this become null?
        documents = convertObjectToArray(document_str, "Components");
        // create people list
        documentNames(documents);
    }

    static void documentNames (JSONArray document){
        // tries to get the JSON
        try
        {
            // if there are entries, keeps it from crashing the app
            if (document != null)
            {
                // gets list of project names and their unique ids
                documentNames = new String[document.length()+1];
                documentNames [0] = "Choose Document";
                documentNumber = new int [document.length()];
                for (int i = 0; i < document.length(); i++)
                {
                    JSONObject p = (JSONObject) document.get(i);
                    documentNames[i+1] = p.getString("Name");
                    documentNumber[i] = document.getJSONObject(i).getInt("Document");
                }
            }
        } catch (JSONException e) { e.printStackTrace();}
    }

    static public void getService()
    {
        String service_str = null;
        // try to read from URL
        try {
            // set up from the URL
            service_str = new CRUD.readMessage().execute(MainActivity.mainURL+MainActivity.serviceURL).get();
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
        // try to fill JSON
        services = convertObjectToArray(service_str, "ServiceEntries");
        // create people list
        serviceNames(services);
    }

    static void serviceNames (JSONArray service){
        // tries to get the JSON
        try
        {
            // if there are entries, keeps it from crashing the app
            if (service != null)
            {
                // gets list of project names and their unique ids
                serviceNames = new String[service.length()+1];
                serviceNames[0] = "Choose Service";
                serviceNumber = new int [service.length()];
                for (int i = 0; i < service.length(); i++)
                {
                    JSONObject p = (JSONObject) service.get(i);
                    serviceNames[i+1] = p.getString("Name");
                    serviceNumber[i] = service.getJSONObject(i).getInt("Service Entry");
                }
            }
        } catch (JSONException e) { e.printStackTrace();}
    }

}
