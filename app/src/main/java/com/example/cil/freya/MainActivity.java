package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.JsonToken;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.JsonReader;
import android.util.JsonWriter;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;



public class MainActivity  extends Activity implements View.OnClickListener {

    Button create, read, update, delete;
    TextView createText;
    JsonReader test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = (Button) findViewById(R.id.create);
        read = (Button) findViewById(R.id.read);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        createText = (TextView) findViewById(R.id.editText);

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
                readMessage(test);
                createText.setText("create");
                break;

            case (R.id.read):
                createText.setText("read");
                break;
            case (R.id.delete):
                createText.setText("delete");
                break;
            case (R.id.update):
                createText.setText("update");
                break;
        }
    }




    public boolean readMessage(JsonReader reader)  {
        String id = null;
        String text = null;
        //User user = null;
        List geo = null;
        URL url = null;

        try {
            url = new URL("http://sensor.nevada.edu/GS/Services/people/"); //TODO: set as global var
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();



            int responseCode = urlConnection.getResponseCode();
            urlConnection.disconnect();
            return responseCode != 200;
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


       /* reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Creation Date")) {
                id = reader.nextString();
            }/* else if (name.equals("text")) {
                text = reader.nextString();
            } else if (name.equals("geo") && reader.peek() != JsonToken.NULL) {
                geo = readDoublesArray(reader);
            } else if (name.equals("user")) {
                user = readUser(reader);
            } else {
                reader.skipValue();
            }*/
       /* }
        reader.endObject();

        } catch( IOException ex) {
            System.err.println ("Test");
        }

        return id;*/
    }





}
