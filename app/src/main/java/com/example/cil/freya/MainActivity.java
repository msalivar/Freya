package com.example.cil.freya;

import android.app.ActionBar;
<<<<<<< HEAD
=======
import android.app.Activity;
>>>>>>> 50082870b3c74adf8d2edb614aa90ab2e40bcd9d
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    public final static String JSON_TEXT = "MESSAGE";
    private final int SELECT_PHOTO = 1;
    Bitmap selectedImage = null;
    static JSONObject selected_project = null;
    static JSONArray projects;
    static String projectNames [];
    static String uniqueID [];
    static String investigators [];
    static String mainURL = "http://sensor.nevada.edu/GS/Services/";
    static String peopleURL = "people/";
    static String projectsURL = "projects/";
    ArrayAdapter<String> listAdapter;
    ListView projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0)
        {
            public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
            public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        addDrawerItems();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
       // actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.sync_icon);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        actionBar.setIcon(R.drawable.upload_icon);

        getAllRequest();
        if (savedInstanceState != null)
        {
            projectNames = savedInstanceState.getStringArray(null);
            Toast.makeText(this, "load state", Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                projectList = (ListView) findViewById(R.id.projectList);
                projectList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selected_name = ((TextView) view).getText().toString();
                        selected_project = null;
                        String pTitle;
                        try
                        {
                            for (int i = 0; i < projects.length(); i++)
                            {
                                    JSONObject p = (JSONObject) projects.get(i);
                                    String val = p.getString("Name");
                                    if (val == selected_name)
                                    {
                                        selected_project = p;
                                        break;
                                    }
                            }
                            Toast.makeText(getApplicationContext(), selected_project.getString("Name"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ProjectDisplay.class);
                            startActivity(intent);
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, projectNames);
                projectList.setAdapter(listAdapter);
            }
            catch (NullPointerException e)
            {
                Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void addDrawerItems()
    {
        String[] osArray = { "Project Options", "Map" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.menu_layout, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) { selectItem(position); }
    }

    private void selectItem(int position)
    {
        if (position == 0)
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        else if(position == 1)
        {
            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = MapsActivity.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.map_fragment, fragment).commit();
        }
        else
        {
            mDrawerList.setItemChecked(position, true);
            getActionBar().setTitle(mDrawerList.getItemAtPosition(position).toString());
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
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
        if (mDrawerToggle.onOptionsItemSelected(item)) { return true; }

        //noinspection SimplifiableIfStatement
        if (id == R.id.sync) {
                getAllRequest();
                listAdapter = new ArrayAdapter<>(this, R.layout.list_view_layout, projectNames);
                projectList.setAdapter(listAdapter);
            return true;
        }
        if(id == R.id.upload){
            Intent intent = new Intent(MainActivity.this, CreateNewProject.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.upload){
            Intent intent = new Intent(MainActivity.this, CreateNewProject.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        /*switch (v.getId())
        {
            case (R.id.browseButton):
                Intent listIntent = new Intent(this, .class);
                startActivity(listIntent);
                break;
        }*/
    }

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


    public void getAllRequest()
    {
        String projects_str = "";
        String people_str = "";
        try {
            projects_str = new readMessage().execute(mainURL+projectsURL).get();
            people_str = new readMessage().execute(mainURL+peopleURL).get();
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(this, "Sync Unsuccessful" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try
        {
            JSONObject proj_obj = new JSONObject(projects_str);
            projects = proj_obj.getJSONArray("Projects");
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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    void ProjectNames (JSONArray projects){
        try
        {
            if (projects != null)
            {
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
            Toast.makeText(this, "Unable to Populate Project List" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public class readMessage extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-length", "0");
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.connect();
                int code = urlConnection.getResponseCode();
                if (code == 200)
                {
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
                e.printStackTrace();
                return "error";
            } finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
        }
    }

    public class writeMessage extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
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

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(JSON.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
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
                    System.out.println(urlConnection.getResponseMessage());
                }
            } catch (IOException | JSONException e) {

                e.printStackTrace();

            } finally{

                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

    public class deleteMessage extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            String test = "http://sensor.nevada.edu/GS/Services/people/0E984725-C51C-4BF4-9960-E1C80E17CCC7";
            try {
                url = new URL(test);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();
                int HttpResult = urlConnection.getResponseCode();

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            } finally{

                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

    public class updateMessage extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            String sb = "";
            URL url = null;
            int one = 1;
            HttpURLConnection urlConnection = null;
            String test = "http://sensor.nevada.edu/GS/Services/people/0E984725-C51C-4BF4-9960-E1C80E27ABA3";
            try {
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

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(JSON.toString());
                out.close();

                int HttpResult = urlConnection.getResponseCode();
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

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            } catch (JSONException e) {

                e.printStackTrace();

            } finally{

                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }
}







