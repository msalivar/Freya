package com.example.cil.freya;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cil.freya.ModuleDisplayActivities.ComponentDisplayActivity;
import com.example.cil.freya.ModuleDisplayActivities.DeploymentDisplayActivity;
import com.example.cil.freya.ModuleDisplayActivities.DocumentDisplayActivity;
import com.example.cil.freya.ModuleDisplayActivities.ProjectDisplayActivity;
import com.example.cil.freya.ModuleDisplayActivities.ServiceEntryDisplayActivity;
import com.example.cil.freya.ModuleDisplayActivities.SiteDisplayActivity;
import com.example.cil.freya.ModuleDisplayActivities.SystemDisplayActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends NavigationDrawer implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public final static String JSON_TEXT = "MESSAGE";
    static String ProjectFile = "FilterSettings.txt";
    ExpandableListAdapter expandable;
    ExpandableListView expListView;
    public static int selectedModuleIndex = -1;
    public static String[] readPerm = {"android.permission.READ_EXTERNAL_STORAGE"};
    public static String[] cameraPerm = {"android.permission.CAMERA"};
    public static String[] writePerm = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    public static int readRequestCode = 200;
    public static int cameraRequestCode = 201;
    protected static GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;

    // URL list
    static String mainURL = "http://sensor.nevada.edu/GS/Services/";
    static String peopleURL = "people/";
    static String projectsURL = "projects/";
    static String siteURL = "sites/";
    static String systemURL = "systems/";
    static String deploymentURL = "deployments/";
    static String componentURL = "components/";
    static String documentURL = "documents/";
    static String serviceURL = "service_entries/";
    static String edgeURL = "edge/";

    // lists
    static ArrayList<ProjectEntry> projectEntries = new ArrayList<>();
    static ArrayList<Boolean> projectHideValues = new ArrayList<>();
    static ArrayList<Boolean> checkValues = new ArrayList<>();
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer(savedInstanceState);

        //TODO
        GPSAccessPermission();

        // go to getAllRequest
        getInfo.getAllRequests(this);

        // create expandable list view
        expListView = (ExpandableListView) findViewById(R.id.expList);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                // listDataHeader.get(groupPosition)
                // listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)
                selectedModuleIndex = childPosition;
                switch (listDataHeader.get(groupPosition))
                {
                    case "Projects":
                        Intent project = new Intent(MainActivity.this, ProjectDisplayActivity.class);
                        startActivity(project);
                        break;
                    case "Systems":
                        Intent system = new Intent(MainActivity.this, SystemDisplayActivity.class);
                        startActivity(system);
                        break;
                    case "Components":
                        Intent component = new Intent(MainActivity.this, ComponentDisplayActivity.class);
                        startActivity(component);
                        break;
                    case "Service Entries":
                        Intent serviceEntry = new Intent(MainActivity.this, ServiceEntryDisplayActivity.class);
                        startActivity(serviceEntry);
                        break;
                    case "Deployments":
                        Intent deployment = new Intent(MainActivity.this, DeploymentDisplayActivity.class);
                        startActivity(deployment);
                        break;
                    case "Sites":
                        Intent site = new Intent(MainActivity.this, SiteDisplayActivity.class);
                        startActivity(site);
                        break;
                    case "Documents":
                        Intent document = new Intent(MainActivity.this, DocumentDisplayActivity.class);
                        startActivity(document);
                        break;
                    case "Unsynced":
                        break;
                }
                return false;
            }
        });

        prepareListData();

        // load state testing, will be used to implement saving information into the app so syncing will not have to occur every time the user opens the app
        if (savedInstanceState != null)
        {
            getInfo.projectNames = savedInstanceState.getStringArray(null);
            Toast.makeText(this, "load state", Toast.LENGTH_LONG).show();
        }
        // if no load state
        else
        {
            // usually shown when the app cant connect to the server
            //Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
        }

        buildGoogleApiClient();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("People");
        listDataHeader.add("Projects");
        listDataHeader.add("Sites");
        listDataHeader.add("Systems");
        listDataHeader.add("Deployments");
        listDataHeader.add("Components");
        listDataHeader.add("Documents");
        listDataHeader.add("Service Entries");
        listDataHeader.add("Unsynced");


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
        List<String> unsynced = new ArrayList<String>();
        if (getInfo.complete.length() > 0)
        {
            Collections.addAll(unsynced, "Name");
        }

        listDataChild.put(listDataHeader.get(0), people); // Header, Child data
        listDataChild.put(listDataHeader.get(1), projects);
        listDataChild.put(listDataHeader.get(2), sites);
        listDataChild.put(listDataHeader.get(3), systems);
        listDataChild.put(listDataHeader.get(4), deployments);
        listDataChild.put(listDataHeader.get(5), components);
        listDataChild.put(listDataHeader.get(6), documents);
        listDataChild.put(listDataHeader.get(7), serviceEntries);
        listDataChild.put(listDataHeader.get(8), unsynced);

        expandable = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(expandable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRestart(){

        super.onRestart();
        List<String> unsynced = new ArrayList<String>();
        if (getInfo.complete.length() > 0)
        {
            Collections.addAll(unsynced, "Name");
        }
        listDataChild.put(listDataHeader.get(8), unsynced);
        expandable = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(expandable);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void GPSAccessPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck != 0){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
           System.out.print(mLastLocation);
        } else {
            // Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }


    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }


    static class AsyncParams{
        JSONObject complete;
        Context cxt;

        AsyncParams(JSONObject complete, Context cxt){
            this.complete = complete;
            this.cxt = cxt;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.sync) {
            getInfo.getAllRequests(this);
            prepareListData();
            return true;
        }
        // if upload is chosen
        if (id == R.id.upload) {
            Context cxt = getApplicationContext();
            AsyncParams par = new AsyncParams(getInfo.complete, cxt);
            new CRUD.writeMessage().execute(par);
            overridePendingTransition(0, 0);
            return true;
        }

        return false;
    }


    public static boolean isMarshmellow()
    {
        return (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);
    }

// the people test JSON. Was our first try. Is included for refrence, but is no longer used
//    public JSONObject createPeopleJSON() throws JSONException, UnsupportedEncodingException
//    {
//        JSONObject jsonParam = new JSONObject();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
//        String date = sdf.format(new Date());
//        jsonParam.put("Creation Date", date);
//        jsonParam.put("Email", "test@email.com");
//        jsonParam.put("First Name", "Picture");
//        jsonParam.put("Last Name", "Tester");
//        jsonParam.put("Modification Date", date);
//        jsonParam.put("Organization", "wat");
//        jsonParam.put("Phone", "(775)555-0000");
//        jsonParam.put("Unique Identifier", "0E984725-C51C-4BF4-9960-E1C80E17CCC7");
//        if (selectedImage != null)
//        {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] bArray = baos.toByteArray();
//            String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
//            jsonParam.put("Photo", encoded);
//        }
//        else
//        {
//            jsonParam.put("Photo", 0);
//        }
//        return jsonParam;
//    }
}