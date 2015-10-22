package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity implements View.OnClickListener {

    public final static String JSON_TEXT = "MESSAGE";
    Button create, read, update, delete;
    TextView createText;
    JSONObject saved = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = (Button) findViewById(R.id.create);
        read = (Button) findViewById(R.id.read);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);


        create.setOnClickListener(this);
        read.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case (R.id.create):
                new writeMessage().execute();
                break;
            case (R.id.read):
                getAllRequest();
                break;
            case (R.id.delete):
                new deleteMessage().execute();
                break;
            case (R.id.update):
                new updateMessage().execute();
                break;
        }
    }

    public JSONObject createJSON(String id) throws JSONException {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        jsonParam.put("Creation Date", date);
        EditText info = (EditText) findViewById(R.id.email);
        jsonParam.put("Email", info.getText().toString());
        info = (EditText) findViewById(R.id.firstName);
        jsonParam.put("First Name", info.getText().toString());
        info = (EditText) findViewById(R.id.lastName);
        jsonParam.put("Last Name", info.getText().toString());
        jsonParam.put("Modification Date", date);
        info = (EditText) findViewById(R.id.organization);
        jsonParam.put("Organization", info.getText().toString());
        info = (EditText) findViewById(R.id.phone);
        jsonParam.put("Phone", info.getText().toString());
        jsonParam.put("Photo", 0);
        jsonParam.put("Unique Identifier", id.toString());
        return jsonParam;
    }

    public void getAllRequest()
    {
        String json_str = "";
        try {
            json_str = new readMessage().execute("http://sensor.nevada.edu/GS/Services/people/").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try
        {
            JSONObject obj = new JSONObject(json_str);
            JSONArray persons = obj.getJSONArray("People");
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            String[] people = new String[persons.length()];
            for (int i = 0; i < persons.length(); i++)
            {
                people[i] = persons.getJSONObject(i).toString();
            }
            intent.putExtra(JSON_TEXT, people);
            startActivity(intent);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public class readMessage extends AsyncTask<String, Void, String>
    {
        private Exception exception;
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
            URL url = null;
            int one = 1;
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
                String uniqueID = UUID.randomUUID().toString();
                JSONObject JSON = createJSON(uniqueID);

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

    public class deleteMessage extends AsyncTask<Void, Void, Void>
    {
        EditText id = (EditText) findViewById(R.id.uniqueID);
        String uniqueID = id.getText().toString();
        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;


            String test = "http://sensor.nevada.edu/GS/Services/people/" + uniqueID;
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
        EditText id = (EditText) findViewById(R.id.uniqueID);
        String uniqueID = id.getText().toString();
        @Override
        protected Void doInBackground(Void... params) {
            String sb = "";
            URL url = null;
            int one = 1;
            HttpURLConnection urlConnection = null;
            String test = "http://sensor.nevada.edu/GS/Services/people/"+uniqueID;
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
                JSONObject JSON = createJSON(uniqueID);

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







