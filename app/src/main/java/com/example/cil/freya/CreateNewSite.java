package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by cil on 11/18/15.
 */
<<<<<<< HEAD
public class CreateNewSite extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
=======
public class CreateNewSite extends MainActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb
    static String projectsURL = MainActivity.mainURL + MainActivity.edgeURL;
    Button createButton, previousButton;
    Spinner proj;
    int numb;
    private final int SELECT_PHOTO = 1;
    Bitmap selectedImage = null;
<<<<<<< HEAD
=======
    EditText info = null;
    String SiteFile = "SiteFile.txt";
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_site);
        createButton = (Button) findViewById(R.id.newSiteButton);
        createButton.setOnClickListener(this);
        previousButton = (Button) findViewById(R.id.backSiteButton);
        previousButton.setOnClickListener(this);


<<<<<<< HEAD
        proj = (Spinner) findViewById(R.id.project);

        try
        {
            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getInfo.projectNames);
=======
        //proj = (Spinner) findViewById(R.id.project);

        try
        {
            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GetInfo.projectNames);
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            proj.setAdapter(spinAdapter);
            proj.setOnItemSelectedListener(this);
        }catch (NullPointerException e){
            Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public void onClick (View v){
        switch (v.getId()){
            case (R.id.newSiteButton):
                try
                { newSite();} catch (JSONException e) {e.printStackTrace();}

                Intent intent = new Intent(this, CreateNewSystem.class);
                startActivity(intent);
                try {
                    components.write(info, SiteFile, this);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case (R.id.backSiteButton):
                intent = new Intent(this, CreateNewProject.class);
                startActivity(intent);
                try {
                    components.write(info, SiteFile, this);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
<<<<<<< HEAD
        numb = getInfo.projectNumber[position];
=======
        numb = GetInfo.projectNumber[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb
    }

    public void newSite()throws JSONException{

<<<<<<< HEAD
                //Create JSONObject here
                JSONObject JSON = createSiteJSON();
=======
        //Create JSONObject here
        JSONObject JSON = createSiteJSON();
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb

        CreateNewProject.complete.put("Site", JSON);
    }

    public JSONObject createSiteJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
<<<<<<< HEAD
        EditText info = null;
=======
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.site_name);
        jsonParam.put("Name",info.getText().toString());

        info = (EditText) findViewById(R.id.notes);
        jsonParam.put("Notes",info.getText().toString());

        info = (EditText) findViewById(R.id.alias);
        jsonParam.put("Alias", info.getText().toString());

        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Location",info.getText().toString());
        info = (EditText) findViewById(R.id.name);
        jsonParam.put("Name",info.getText().toString());
        info = (EditText) findViewById(R.id.note);
        jsonParam.put("Notes",info.getText().toString());
        info = (EditText) findViewById(R.id.permit);
        jsonParam.put("Permit Holder", info.getText().toString());

        jsonParam.put("Time Zone Name", TimeZone.getDefault().getDisplayName());

        jsonParam.put("Time Zone Abbreviation", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));

        //is this the correct format?
        jsonParam.put("Time Zone Offset", TimeZone.getDefault().getRawOffset());

        jsonParam.put("Project", numb);

        info = (EditText) findViewById(R.id.landmark);
        jsonParam.put("GPS Landmark", info.getText().toString());

        //I have no idea how photos work
        //jsonParam.put("Landmark Photo", null);
//        if (selectedImage != null)
//        {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            byte[] bArray = baos.toByteArray();
//            String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
//            jsonParam.put("Landmark Photo", encoded);
//        }
//        else
//        {
//            jsonParam.put("Landmark Photo", 0);
//        }

        return jsonParam;
    }
}
