package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity implements View.OnClickListener
{

    public final static String JSON_TEXT = "MESSAGE";
    Button create, read, update, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        EditText id = (EditText) findViewById(R.id.uniqueID);
        String uniqueID = id.getText().toString();

        switch (v.getId())
        {
            case (R.id.create):
                new HTTP_Post().execute();
                break;
            case (R.id.read):
                GetAllPeople();
                break;
            case (R.id.delete):
                new HTTP_Delete().execute(uniqueID);
                break;
            case (R.id.update):
                new HTTP_Put().execute(uniqueID);
                break;
        }
    }

    public JSONObject createJSON(String id) throws JSONException
    {
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

    public void GetAllPeople()
    {
        String json_str = "";
        try {
            json_str = new HTTP_Get().execute("http://sensor.nevada.edu/GS/Services/people/").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try
        {
            JSONObject obj = new JSONObject(json_str);
            JSONArray persons = obj.getJSONArray("People");
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            startActivity(intent);
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


}







