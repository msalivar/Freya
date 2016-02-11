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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by cil on 11/18/15.
 */
public class CreateNewSite extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    static String projectsURL = MainActivity.mainURL + MainActivity.edgeURL;
    Button createButton, previousButton;
    Spinner proj;
    int numb;
    private final int SELECT_PHOTO = 1;
    Bitmap selectedImage = null;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_site);
        createButton = (Button) findViewById(R.id.newSiteButton);
        createButton.setOnClickListener(this);
        previousButton = (Button) findViewById(R.id.backSiteButton);
        previousButton.setOnClickListener(this);


        proj = (Spinner) findViewById(R.id.project);

        try
        {
            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getInfo.projectNames);
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            proj.setAdapter(spinAdapter);
            proj.setOnItemSelectedListener(this);
        }catch (NullPointerException e){
            Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public void onClick (View v){
        Intent intent;
        switch (v.getId()){
            case (R.id.newSiteButton):
                try
                { newSite();} catch (JSONException e) {e.printStackTrace();}

                intent = new Intent(this, CreateNewSystem.class);
                startActivity(intent);
                break;

            case (R.id.backSiteButton):
                intent = new Intent(this, CreateNewProject.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        numb = getInfo.projectNumber[position];
    }

    public void newSite()throws JSONException{
        //Create JSONObject here
        JSONObject JSON = createSiteJSON();

        CreateNewProject.complete.put("Site", JSON);
    }

    public JSONObject createSiteJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        EditText info = null;

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.site_name);
        jsonParam.put("Name",info.getText().toString());

        info = (EditText) findViewById(R.id.notes);
        jsonParam.put("Notes",info.getText().toString());

        info = (EditText) findViewById(R.id.alias);
        jsonParam.put("Alias", info.getText().toString());

        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Location",info.getText().toString());

        info = (EditText) findViewById(R.id.land_owner);
        jsonParam.put("Land Owner", info.getText().toString());

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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

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
                        ImageView imageView = null;
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}