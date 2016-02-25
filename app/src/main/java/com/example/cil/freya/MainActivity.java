package com.example.cil.freya;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    public final static String JSON_TEXT = "MESSAGE";
    static JSONObject selected_project = null;
    static String ProjectFile = "FilterSettings.txt";
    ExpandableListAdapter expandable;
    ExpandableListView expListView;

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

        // create the drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0)
        {
            public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
            public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
        };
        // enable drawer listener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        addDrawerItems();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        // put in icons from drawables
        actionBar.setIcon(R.drawable.sync_icon);
        actionBar.setIcon(R.drawable.upload_icon);
        actionBar.setIcon(R.drawable.search_icon);

        // infate action bar
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // go to getAllRequest
        getInfo.getAllRequests();

        // create expandable list view
        expListView = (ExpandableListView) findViewById(R.id.expList);
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
            // if list is empty, will throw exception
            try
            {
                // create listen and set listener
                expListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    // what happens when the list is clicked on
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        // get the selected name
                        String selected_name = ((TextView) view).getText().toString();
                        selected_project = null;
                        String pTitle;
                        // throws JSON exception if the project cannot be found
                        try
                        {
                            // for everything in the JSON
                            for (int i = 0; i < getInfo.projects.length(); i++)
                            {
                                // get the object
                                JSONObject p = (JSONObject) getInfo.projects.get(i);
                                // search for name
                                String val = p.getString("Name");
                                // if it's the name the user is looking for, assign it to selected name
                                if (val == selected_name)
                                {
                                    selected_project = p;
                                    break;
                                }

                            }
                            //test
                            Toast.makeText(getApplicationContext(), selected_project.getString("Name"), Toast.LENGTH_SHORT).show();
                            // start projectDisplay intent
                            // Intent intent = new Intent(MainActivity.this, ProjectDisplay.class);
                            // startActivity(intent);
                        } catch (JSONException e)
                        {
                            // if list empty, print to logcat
                            e.printStackTrace();
                        }
                    }
                });
                // redisplay the list and set listen
                prepareListData();
            }
            catch (NullPointerException e)
            {
                // usually shown when the app cant connect to the server
                Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Projects");
        listDataHeader.add("Systems");
        listDataHeader.add("Components");
        listDataHeader.add("Service Entries");

        // Adding child data
        List<String> projects = new ArrayList<String>();
        Collections.addAll(projects, getInfo.projectNames);
        projects.remove(0);
        List<String> systems = new ArrayList<String>();
        Collections.addAll(systems, getInfo.systemNames);
        systems.remove(0);
        List<String> components = new ArrayList<String>();
        Collections.addAll(components, getInfo.componentNames);
        components.remove(0);
        List<String> serviceEntries = new ArrayList<String>();
        Collections.addAll(serviceEntries, getInfo.serviceNames);
        serviceEntries.remove(0);

        listDataChild.put(listDataHeader.get(0), projects); // Header, Child data
        listDataChild.put(listDataHeader.get(1), systems);
        listDataChild.put(listDataHeader.get(2), components);
        listDataChild.put(listDataHeader.get(3), serviceEntries);

        expandable = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(expandable);
    }

    // when creating a drawer
    public void addDrawerItems()
    {
        // list of options
        String[] osArray = { "Project Options","Create New Project","Create New Site", "Create New System", "Create New Deployment", "Create New Component", "Create New Document" , "Create New Service Entry"};
        //  display and set listener
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.menu_layout, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    // once GUI  is created
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        // will do stuff once saved states are enabled
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    // drawer listener
    public class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        // got to onItemClick
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) { selectItem(position); }
    }

    // if an item is selected from the drawer
    public void selectItem(int position)
    {
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image*//*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);*/


        switch (position) {
            case 0:
                // start filtering intent
                Intent intent = new Intent(MainActivity.this, ProjectFilterActivity.class);
                startActivity(intent);
                break;

            case 1:
                // start NewSite intent
                intent = new Intent(MainActivity.this, CreateNewProject.class);
                startActivity(intent);
                break;

            case 2:
                // start NewSite intent
                intent = new Intent(MainActivity.this, CreateNewSite.class);
                startActivity(intent);
                break;

            case 3:
                // start NewSite intent
                intent = new Intent(MainActivity.this, CreateNewSystem.class);
                startActivity(intent);
                break;

            case 4:
                // start NewSite intent
                intent = new Intent(MainActivity.this, CreateNewDeployment.class);
                startActivity(intent);
                break;

            case 5:
                // start component intent
                intent = new Intent(MainActivity.this, CreateNewComponent.class);
                startActivity(intent);
                break;

            case 6:
                // start document intent
                intent = new Intent(MainActivity.this, CreateNewDocument.class);
                startActivity(intent);
                break;

            case 7:
                // start document intent
                intent = new Intent(MainActivity.this, CreateNewServiceEntry.class);
                startActivity(intent);
                break;

       /* else if (position == 3)
        {
            // start photo picker intent
            // mDrawerLayout.closeDrawer(mDrawerList);
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }*/
            default:
                // close the drawer
                mDrawerList.setItemChecked(position, true);
                //getActionBar().setTitle(mDrawerList.getItemAtPosition(position).toString());
                mDrawerLayout.closeDrawer(mDrawerList);
        }

    }

    @Override
    // photo picker code, not currently implemented, but in place for implemenation
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        /*switch (requestCode)
        {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }*/
    }

    // automatically generated
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            getInfo.getAllRequests();
            prepareListData();
            return true;
        }
        // if upload is chosen
        if (id == R.id.upload) {
            // start createnewproject intent
            Intent intent = new Intent(MainActivity.this, CreateNewProject.class);
            startActivity(intent);
            return true;
        }
        //  if search is chosen
        if (id == R.id.search) {
            // create new searchactivity intent
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return super.onOptionsItemSelected(item);
        }

        return false;
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