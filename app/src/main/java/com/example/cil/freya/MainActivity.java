package com.example.cil.freya;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
<<<<<<< HEAD
=======
import android.graphics.BitmapFactory;
import android.net.Uri;
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
<<<<<<< HEAD
import android.view.LayoutInflater;
=======
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
<<<<<<< HEAD
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
=======
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
<<<<<<< HEAD
=======
import java.io.FileNotFoundException;
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity implements View.OnClickListener {
<<<<<<< HEAD
//delcare all variables
=======

>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    public final static String JSON_TEXT = "MESSAGE";
    private final int SELECT_PHOTO = 1;
    Bitmap selectedImage = null;
<<<<<<< HEAD
    static JSONObject selected_project = null;
    static JSONArray projects;
    // put JSONs in here
    static String projectNames [];
    static String uniqueID [];
    static String investigators [];
    // URL list
    static String mainURL = "http://sensor.nevada.edu/GS/Services/";
    static String peopleURL = "people/";
    static String projectsURL = "projects/";
    // lists
    ArrayAdapter<String> listAdapter;
    ListView projectList;

// set up GUI here
=======
    Button browseButton, createButton, syncButton;
    ImageView imageView;
    static String projectNames[];
    static String uniqueID [];
    static String investigators [];
    static String mainURL = "http://sensor.nevada.edu/GS/Services/";
    static String peopleURL = "people/";
    static String projectsURL = "projects/";
    static String edgeURL = "edge/";

>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        // create the drawer
=======
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0)
        {
            public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
            public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
        };
<<<<<<< HEAD
        // enable drawer listener
=======
        imageView = (ImageView)findViewById(R.id.imageView);
        browseButton = (Button)findViewById(R.id.browseButton);
        createButton = (Button)findViewById(R.id.createButton);
        syncButton = (Button)findViewById(R.id.sync);
        browseButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        syncButton.setOnClickListener(this);
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        addDrawerItems();
        
        // create action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
<<<<<<< HEAD
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
        getAllRequest();
        
        // load state testing, will be used to implement saving infomation into the app so syncing will not have to occur everytime the user opens the app
        if (savedInstanceState != null)
        {
            projectNames = savedInstanceState.getStringArray(null);
            Toast.makeText(this, "load state", Toast.LENGTH_LONG).show();
        }
        // if no load state
        else
        {
            // if list is empty, will throw exception
            try
            {
                // create listen and set listener
                projectList = (ListView) findViewById(R.id.projectList);
                projectList.setOnItemClickListener(new AdapterView.OnItemClickListener()
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
                            for (int i = 0; i < projects.length(); i++)
                            {
                                // get the object 
                                JSONObject p = (JSONObject) projects.get(i);
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
                            Intent intent = new Intent(MainActivity.this, ProjectDisplay.class);
                            startActivity(intent);
                        } catch (JSONException e)
                        {
                            // if list empty, print to logcat
                            e.printStackTrace();
                        }
                    }
                });
                // redisplay the list and set listen
                listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, projectNames);
                projectList.setAdapter(listAdapter);
            }
            catch (NullPointerException e)
            {
                // usually shown when the app cant connect to the server
                Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
=======
        addDrawerItems();

        getAllRequest();
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
    }

// when creating a drawer
    private void addDrawerItems()
    {
<<<<<<< HEAD
        // list of options
        String[] osArray = { "Project Options","Create New Site", "Create New Component" };
        //  display and set listener
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.menu_layout, osArray);
=======
        String[] osArray = { "Pick Photo", "Two", "Three" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
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

// generated, does nothing
    @Override
    public void onClick(View v) {

    }

// drawer lsitener
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        // got to onItemClick
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) { selectItem(position); }
    }

// if an item is selected from the drawer
    private void selectItem(int position)
    {
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image*//*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);*/
        if (position == 0)
        {
            // start filtering intent
            Intent intent = new Intent(MainActivity.this, ProjectFilterActivity.class);
            startActivity(intent);
        }
        else if(position == 1)
        {
            // start NewSite intent
            Intent intent = new Intent(MainActivity.this, CreateNewSite.class);
            startActivity(intent);
        }
        else if (position == 2)
        {
            // start component intent
            Intent intent = new Intent(MainActivity.this, CreateNewComponent.class);
            startActivity(intent);
        }
        else if (position == 3)
        {
            // start photo picker intent
            // mDrawerLayout.closeDrawer(mDrawerList);
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
<<<<<<< HEAD
        }
        else {
            // close the drawer
=======
        } else
        {
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            mDrawerList.setItemChecked(position, true);
            getActionBar().setTitle(mDrawerList.getItemAtPosition(position).toString());
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
<<<<<<< HEAD
        if (id == R.id.sync) {
            getAllRequest();
            listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, projectNames);
            projectList.setAdapter(listAdapter);
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
=======
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.browseButton):
                Intent listIntent = new Intent(this, ProjectListActivity.class);
                startActivity(listIntent);
                break;
            case (R.id.createButton):
                Intent intent = new Intent(this, CreateNewProject.class);
                startActivity(intent);
                break;
            case (R.id.sync):
                getAllRequest();
                break;
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
        }

        return false;
    }

<<<<<<< HEAD
// the people test JSON. Was our first try. Is included for refrence, but is no longer used
=======
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
    public JSONObject createPeopleJSON() throws JSONException, UnsupportedEncodingException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        jsonParam.put("Creation Date", date);
        jsonParam.put("Email", "test@email.com");
        jsonParam.put("First Name", "Picture");
        jsonParam.put("Last Name", "Tester");
        jsonParam.put("Modification Date", date);
        jsonParam.put("Organization", "wat");
        jsonParam.put("Phone", "(775)555-0000");
        jsonParam.put("Unique Identifier", "0E984725-C51C-4BF4-9960-E1C80E17CCC7");
        if (selectedImage != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bArray = baos.toByteArray();
            String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
            jsonParam.put("Photo", encoded);
        }
        else
        {
            jsonParam.put("Photo", 0);
        }
        return jsonParam;
    }

<<<<<<< HEAD
// get all information from the server
=======

>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
    public void getAllRequest()
    {
        String projects_str = "";
        String people_str = "";
<<<<<<< HEAD
        // try to read from URL
        try {
            // set up from the URL
            projects_str = new readMessage().execute(mainURL+projectsURL).get();
            people_str = new readMessage().execute(mainURL+peopleURL).get();
        } catch (InterruptedException | ExecutionException e) {
            // usually thrown if the app is unable to sync to the server
            Toast.makeText(this, "Sync Unsuccessful" + e, Toast.LENGTH_LONG).show();
            // print to logcat
=======
        try {
            projects_str = new readMessage().execute(mainURL+projectsURL).get();
            people_str = new readMessage().execute(mainURL+peopleURL).get();
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(this, "Sync Unsuccessful" + e, Toast.LENGTH_LONG).show();
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            e.printStackTrace();
        }
        // try to fill JSON
        try
        {
<<<<<<< HEAD
            
            JSONObject proj_obj = new JSONObject(projects_str);
            // gather projects JSON. Put all names in string
            projects = proj_obj.getJSONArray("Projects");
            String[] project = new String[projects.length()];
            for (int i = 0; i < projects.length(); i++)
            {
                project[i] = projects.getJSONObject(i).toString();
            }
            // create projects list
            ProjectNames(projects);
            
            // gather people JSON. Put all in names in string
            JSONObject peep_obj = new JSONObject(people_str);
            JSONArray peoples = peep_obj.getJSONArray("People");
            String[] people = new String[peoples.length()];
            for (int i = 0; i < peoples.length(); i++)
            {
                people[i] = peoples.getJSONObject(i).toString();
            }
            // create people list
            PeopleNames(peoples);

            // confirm that action was successful 
            Toast.makeText(this, "Sync Successful! ", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            // usually thrown if the app can't connect to the server
            Toast.makeText(this, "Sync Unsuccessful. Unable to reach Server.", Toast.LENGTH_LONG).show();
            // print to log cat
=======
            JSONObject proj_obj = new JSONObject(projects_str);
            JSONArray projects = proj_obj.getJSONArray("Projects");
            String[] project = new String[projects.length()];
            for (int i = 0; i < projects.length(); i++)
            {
                project[i] = projects.getJSONObject(i).toString();
            }
            ProjectNames(projects);

            JSONObject peep_obj = new JSONObject(people_str);
            JSONArray peoples = peep_obj.getJSONArray("People");
            String[] people = new String[peoples.length()];
            for (int i = 0; i < peoples.length(); i++)
            {
                people[i] = peoples.getJSONObject(i).toString();
            }
            PeopleNames(peoples);

            Toast.makeText(this, "Sync Successful! ", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(this, "Sync Unsuccessful. Unable to reach Server.", Toast.LENGTH_LONG).show();
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
// get stuff from the people JSON
    void PeopleNames (JSONArray people) {
        // tries to get the JSON
        try {
            // if there are entries, keeps app from crashing
            if (people != null) {
                // gather investigator list
                investigators = new String[people.length() + 1];
                investigators[0] = "Choose Investigator";
                // combine first and last name. put in investigator array
                for (int i = 0; i < people.length(); i++) {
                    JSONObject p = (JSONObject) people.get(i);
                    investigators[i + 1] = p.getString("First Name") + " " + p.getString("Last Name");
                }
            }
        } catch (JSONException e) {
            //  throws if unable to connect to server, no people JSON
            Toast.makeText(this, "Unable to Populate People List" + e, Toast.LENGTH_LONG).show();
            // print to log cat
=======
    void PeopleNames (JSONArray people)
    {
        try
        {
            if (people != null)
            {
                investigators = new String[people.length()];
                for (int i = 0; i < people.length(); i++)
                {
                    JSONObject p = (JSONObject) people.get(i);
                    investigators[i] = p.getString("First Name") + " " + p.getString("Last Name");
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Unable to Populate People List" + e, Toast.LENGTH_LONG).show();
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
// get projects json
    void ProjectNames (JSONArray projects){
         // tries to get the JSON
        try
        {
            // if there are enteries, keeps it from crashing the app
            if (projects != null)
            {
                // gets list of project names and their unique ids
=======
    void ProjectNames (JSONArray projects){
        try
        {
            if (projects != null)
            {
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
                projectNames = new String[projects.length()];
                uniqueID = new String [projects.length()];
                for (int i = 0; i < projects.length(); i++)
                {
                    JSONObject p = (JSONObject) projects.get(i);
                    projectNames[i] = p.getString("Name");
                    uniqueID[i] = p.getString("Unique Identifier");
                }
            }
        } catch (JSONException e) {
<<<<<<< HEAD
            // thrown if unabvle to connect to server or if json is empty
=======
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            Toast.makeText(this, "Unable to Populate Project List" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


// general  readmessge template. We tried moving each CRUD to their own class, but since they are done in a thread it did not work correctly
    public class readMessage extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                // connect to URL. "GET" code
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-length", "0");
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.connect();
                int code = urlConnection.getResponseCode();
                // if it was succesfully created
                if (code == 200)
                {
                    // read in from URL
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
                }
                return "error";
            } catch (Exception e) {
                // prints error to logcat
                e.printStackTrace();
                return "error";
            } finally {
                // if the connection was succesfull, disconnect
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
        }
    }

// write message to people URL. Same issue. Used as template
    public class writeMessage extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            // connect to URL
            String sb = "";
            URL url;
            HttpURLConnection urlConnection = null;
            String test = "http://sensor.nevada.edu/GS/Services/people/";
            try {
                url = new URL(test);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();

                //Create JSONObject here
                JSONObject JSON = createPeopleJSON();

                // get output writer
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(JSON.toString());
                out.close();

                // if connection is good
                int HttpResult = urlConnection.getResponseCode();
                // output
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb += (line + "\n");
                    }
                    br.close();

                    System.out.println("" + sb);

                }else{
                    //  prints wrror code from server
                    System.out.println(urlConnection.getResponseMessage());
                }
            } catch (IOException | JSONException e) {
<<<<<<< HEAD
                // print to stack trace
=======

>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
                e.printStackTrace();

            } finally{
                // if connection was successful  disconnect
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

//  delete test. SAme problem as above
    public class deleteMessage extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
<<<<<<< HEAD
            // URL is attached to theunique ID to identify which part of the JSON needs to be deleted
=======
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            String test = "http://sensor.nevada.edu/GS/Services/people/0E984725-C51C-4BF4-9960-E1C80E17CCC7";
            try {
                // connect to URL
                url = new URL(test);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();
                int HttpResult = urlConnection.getResponseCode();

            } catch (MalformedURLException e) {
                // print to log cat
                e.printStackTrace();

            } catch (IOException e) {
<<<<<<< HEAD
                // print to log cat
                e.printStackTrace();

            } finally{
                // if connection was successful, disconnect
=======

                e.printStackTrace();

            } finally{

>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

<<<<<<< HEAD
// update test. Same as aboce
=======
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
    public class updateMessage extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            String sb = "";
            URL url = null;
            int one = 1;
            HttpURLConnection urlConnection = null;
<<<<<<< HEAD
            // update via Unqiue ID
=======
>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
            String test = "http://sensor.nevada.edu/GS/Services/people/0E984725-C51C-4BF4-9960-E1C80E27ABA3";
            try {
                // connect to URL
                url = new URL(test);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();

                //Create JSONObject here
                JSONObject JSON = createPeopleJSON();

                // output to URL
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(JSON.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                // if connection is good
                if(HttpResult == HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb += (line + "\n");
                    }
                    br.close();

                    System.out.println("" + sb);

                }else{
                    System.out.println(urlConnection.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                // output to log cat
                e.printStackTrace();

            } catch (IOException e) {
                // output to log cat
                e.printStackTrace();

            } catch (JSONException e) {
                // output to log cat
                e.printStackTrace();

            } finally{
                // if connection is successful, disconnect
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }
<<<<<<< HEAD
=======


    static JSONArray createEdge(JSONObject current) throws JSONException
    {
        JSONArray edge = new JSONArray();
        JSONArray notHere = new JSONArray();
        JSONObject outside = new JSONObject();

         if (current.has ("Person")){
            edge.put (current);
        } else {
             outside.put("Person", notHere);

             edge.put(outside);
             outside = new JSONObject();
         }

        if (current.has ("Project")){
            edge.put (current);
        } else {
            outside.put("Project", notHere);

            edge.put(outside);
            outside = new JSONObject();
        }

        if (current.has ("Site")){
            edge.put (current);
        } else {
            outside.put("Site", notHere);

            edge.put(outside);
            outside = new JSONObject();
        }

        if (current.has ("System")){
            edge.put (current);
        } else {
            outside.put("System", notHere);

            edge.put(outside);
            outside = new JSONObject();
        }

        if (current.has ("Deployment")){
            edge.put (current);
        } else {
            outside.put("Deployment", notHere);

            edge.put(outside);
            outside = new JSONObject();
        }

        if (current.has ("Component")){
            edge.put (current);
        } else {
            outside.put("Component", notHere);

            edge.put(outside);
            outside = new JSONObject();
        }


        if (current.has ("Document")){
            edge.put (current);
        } else {
            outside.put("Document", notHere);

            edge.put(outside);
            outside = new JSONObject();
        }


        if (current.has ("Service Entry")){
            edge.put (current);
        } else
        {
            outside.put("Service Entry", notHere);

            edge.put(outside);
        }

        return edge;
    }

>>>>>>> 148cff6e8e33bc0448d5d9882d9d06026c2a3bbd
}







