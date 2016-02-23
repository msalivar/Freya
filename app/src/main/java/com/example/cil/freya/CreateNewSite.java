package com.example.cil.freya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by cil on 11/18/15.
 */
public class CreateNewSite extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button createButton, previousButton;
    Spinner proj;
    int numb;
    private final int SELECT_PHOTO = 1;
    Bitmap selectedImage = null;
    String SiteFile = "SiteFile.txt";
    EditText info;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_site);
        createButton = (Button) findViewById(R.id.newSiteButton);
        createButton.setOnClickListener(this);
        previousButton = (Button) findViewById(R.id.backSiteButton);
        previousButton.setOnClickListener(this);

        proj = (Spinner) findViewById(R.id.project);
        Modules.spinner (this, getInfo.projectNames, proj);

    }


    public void onClick (View v){
        Intent intent;
        switch (v.getId()){
            case (R.id.newSiteButton):
                try
                { newSite();} catch (JSONException e) {e.printStackTrace();}

                intent = new Intent(this, CreateNewSystem.class);
                startActivity(intent);
                try{
                    Modules.write(info, SiteFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}

                break;

            case (R.id.backSiteButton):
                intent = new Intent(this, CreateNewProject.class);
                startActivity(intent);
                try{
                    Modules.write(info, SiteFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                break;
        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            numb = getInfo.projectNumber[position-1];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void newSite()throws JSONException{

        //Create JSONObject here
        JSONObject JSON = createSiteJSON();

        getInfo.complete.put("Sites", JSON);
    }

    public JSONObject createSiteJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.site_name);
        jsonParam.put("Name",info.getText().toString());

        info = (EditText) findViewById(R.id.doc_notes);
        jsonParam.put("Notes",info.getText().toString());

        info = (EditText) findViewById(R.id.alias);
        jsonParam.put("Alias", info.getText().toString());

        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Location",info.getText().toString());

        info = (EditText) findViewById(R.id.landOwner);
        jsonParam.put("Land Owner",info.getText().toString());

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