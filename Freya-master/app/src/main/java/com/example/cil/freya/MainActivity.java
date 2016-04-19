package com.example.cil.freya;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
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

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends NavigationDrawer implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static MainActivity instance;
    public final static String JSON_TEXT = "MESSAGE";
    static String ProjectFile = "FilterSettings.txt";
    public static ExpandableListView expListView;
    public static int selectedModuleIndex = -1;
    public static String[] readPerm = {"android.permission.READ_EXTERNAL_STORAGE"};
    public static String[] cameraPerm = {"android.permission.CAMERA"};
    public static String[] writePerm = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    public static int readRequestCode = 200;
    public static int cameraRequestCode = 201;
    protected static GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    public static ExpandableListHandler ListHandler = new ExpandableListHandler();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer(savedInstanceState);
        instance = this;

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
                switch (ListHandler.listDataHeader.get(groupPosition))
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

                        //TODO: not working yet
                      /*  String test = ListHandler.listDataChild.get(childPosition).toString();
                      switch (test){
                            case "Projects":
                                project = new Intent(MainActivity.this, ProjectDisplayActivity.class);
                                startActivity(project);
                                break;
                            case "Systems":
                                system = new Intent(MainActivity.this, SystemDisplayActivity.class);
                                startActivity(system);
                                break;
                            case "Components":
                                component = new Intent(MainActivity.this, ComponentDisplayActivity.class);
                                startActivity(component);
                                break;
                            case "Service Entries":
                                serviceEntry = new Intent(MainActivity.this, ServiceEntryDisplayActivity.class);
                                startActivity(serviceEntry);
                                break;
                            case "Deployments":
                                deployment = new Intent(MainActivity.this, DeploymentDisplayActivity.class);
                                startActivity(deployment);
                                break;
                            case "Sites":
                                site = new Intent(MainActivity.this, SiteDisplayActivity.class);
                                startActivity(site);
                                break;
                            case "Documents":
                                document = new Intent(MainActivity.this, DocumentDisplayActivity.class);
                                startActivity(document);
                                break;

                        }
*/
                        break;

                }
                return false;
            }
        });

        expListView.setAdapter(ListHandler.prepareListData(this));

        // load state testing, will be used to implement saving information into the app so syncing will not have to occur every time the user opens the app
        if (savedInstanceState != null)
        {
            getInfo.projectNames = savedInstanceState.getStringArray(null);
            Toast.makeText(this, "load state", LENGTH_LONG).show();
        }
        // if no load state
        else
        {
            // usually shown when the app cant connect to the server
            //Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
        }

        buildGoogleApiClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.cancel_button).setVisible(false);
        return super.onCreateOptionsMenu(menu);
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

    public static Context getContext()
    {
        return instance;
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
        JSONObject unsynced;
        Context cxt;

        AsyncParams(JSONObject  unsynced, Context cxt){
            this.unsynced =  unsynced;
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
            expListView.setAdapter(ListHandler.prepareListData(this));
            return true;
        }
        // if upload is chosen
        if (id == R.id.upload) {
            Context cxt = getApplicationContext();

            AsyncParams par = new AsyncParams(getInfo. unsynced, cxt);
            new CRUD.writeMessage().execute(par);

            // update here

            getInfo.unsynced = new JSONObject();
            overridePendingTransition(0, 0);
            return true;
        }

        return false;
    }


    public static boolean isMarshmellow()
    {
        return (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);
    }

// the people test JSON. Was our first try. Is included for reference, but is no longer used
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