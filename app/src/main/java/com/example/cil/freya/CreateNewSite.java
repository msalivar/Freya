package com.example.cil.freya;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by cil on 11/18/15.
 */
public class CreateNewSite extends NavigationDrawer implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    Spinner proj;
    int numb;
    String SiteFile = "SiteFile.txt";
    EditText info;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    Bitmap selectedImage = null;
    ImageView imageView;
    private Uri imageUri;
    boolean writeAccepted, cameraAccepted;
    EditText lat, longi, alt;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_display);
        super.onCreateDrawer(savedInstanceState);

        getActionBar().setTitle("Create New Site");
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        Button takephoto = (Button) findViewById(R.id.takePhoto);
        takephoto.setOnClickListener(this);

        Button uploadphoto = (Button) findViewById(R.id.uploadPhoto);
        uploadphoto.setOnClickListener(this);

        Button location = (Button) findViewById(R.id.installation_location);
        location.setOnClickListener(this);

        proj = (Spinner) findViewById(R.id.project);
        Modules.spinner(this, getInfo.projectNames, proj);

        lat = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        alt = (EditText) findViewById(R.id.altitude);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.sync).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try{newSite();} catch (JSONException e) {e.printStackTrace();}
                overridePendingTransition(0, 0);

                try{
                    Modules.write(info, SiteFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                finish();

                break;

            case R.id.uploadPhoto:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;

            case R.id.takePhoto:
                if (!hasPermission(MainActivity.readPerm[0])) { requestPermissions(MainActivity.readPerm, MainActivity.readRequestCode); }
                if (!hasPermission(MainActivity.cameraPerm[0])) { requestPermissions(MainActivity.cameraPerm, MainActivity.cameraRequestCode); }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(),  "HotPic.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                startActivityForResult(intent, TAKE_PHOTO);
                break;

            case (R.id.installation_location):
                String message = String.format("%f",MainActivity.mLastLocation.getLatitude());
                lat.setText(message);
                message = String.format("%f",MainActivity.mLastLocation.getLongitude());
                longi.setText(message);
                message = String.format("%f",MainActivity.mLastLocation.getAltitude());
                alt.setText(message);
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            numb = getInfo.projectNumber[position - 1];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

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
                    try
                    {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case TAKE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    Uri thisUri = imageUri;
                    getContentResolver().notifyChange(thisUri, null);
                    ContentResolver cr = getContentResolver();
                    try
                    {
                        selectedImage = android.provider.MediaStore.Images.Media.getBitmap(cr, thisUri);
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults)
    {
        switch(permsRequestCode)
        {
            case 200:
                writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
            case 201:
                cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    private boolean hasPermission(String permission)
    {
        if(MainActivity.isMarshmellow())
        {
            return(checkSelfPermission(permission)==PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    public void newSite() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createSiteJSON();

        JSONArray site = new JSONArray();
        site.put(JSON);

        getInfo.complete.put("Sites", site);
    }

    public JSONObject createSiteJSON() throws JSONException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);

        //from gps
        jsonParam.put("Latitude", lat.getText().toString());
        jsonParam.put("Longitude", longi.getText().toString());
        jsonParam.put("Altitude", alt.getText().toString());

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.site_name);
        jsonParam.put("Name", info.getText().toString());

        info = (EditText) findViewById(R.id.doc_notes);
        jsonParam.put("Notes", info.getText().toString());

        info = (EditText) findViewById(R.id.alias);
        jsonParam.put("Alias", info.getText().toString());

        info = (EditText) findViewById(R.id.landOwner);
        jsonParam.put("Land Owner", info.getText().toString());

        info = (EditText) findViewById(R.id.permit);
        jsonParam.put("Permit Holder", info.getText().toString());

        jsonParam.put("Time Zone Name", TimeZone.getDefault().getDisplayName());

        jsonParam.put("Time Zone Abbreviation", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));

        //is this the correct format?
        //milliseconds to minutes

        jsonParam.put("Time Zone Offset", (TimeZone.getDefault().getRawOffset()/1000)/60);

        jsonParam.put("Project", numb);

        info = (EditText) findViewById(R.id.landmark);
        jsonParam.put("GPS Landmark", info.getText().toString());

        if (selectedImage != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();
            String hexTest = String.format("%x", new BigInteger(1, b));
            jsonParam.put("Landmark Photo", hexTest);
        }

        else { jsonParam.put("Landmark Photo", 0); }

        return jsonParam;
    }


}
