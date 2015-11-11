package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    public final static String JSON_TEXT = "MESSAGE";
    private final int SELECT_PHOTO = 1;
    Bitmap selectedImage = null;
    Button browseButton, createButton, syncButton;
    ImageView imageView;
    static String projectNames[];
    static String uniqueID [];
    static String mainURL = "http://sensor.nevada.edu/GS/Services/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0)
        {
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };
        imageView = (ImageView)findViewById(R.id.imageView);
        browseButton = (Button)findViewById(R.id.browseButton);
        createButton = (Button)findViewById(R.id.createButton);
        syncButton = (Button)findViewById(R.id.sync);
        browseButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        syncButton.setOnClickListener(this);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        addDrawerItems();
    }

    private void addDrawerItems()
    {
        String[] osArray = { "Pick Photo", "Two", "Three" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
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
        } else
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
        switch (requestCode)
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
        }
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
        }
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
        String json_str = "";
        try {
            json_str = new readMessage().execute("http://sensor.nevada.edu/GS/Services/projects/").get();
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(this, "Sync Unsuccessful" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try
        {
            JSONObject obj = new JSONObject(json_str);
            JSONArray projects = obj.getJSONArray("Projects");
           // Intent intent = new Intent(this, DisplayMessageActivity.class);
            String[] project = new String[projects.length()];
            for (int i = 0; i < projects.length(); i++)
            {
                project[i] = projects.getJSONObject(i).toString();
            }
            ProjectNames(projects);
            //intent.putExtra(JSON_TEXT, project);
            //startActivity(intent);
            Toast.makeText(this, "Sync Successful! ", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(this, "Sync Unsuccessful. Unable to reach Server.", Toast.LENGTH_LONG).show();
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







