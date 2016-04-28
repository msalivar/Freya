package com.example.cil.freya.ModuleDisplayActivities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cil.freya.MainActivity;
import com.example.cil.freya.R;
import com.example.cil.freya.getInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class SiteDisplayActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    EditText alias, landmark, location, site_name, doc_notes;
    Spinner project, permit, landOwner;
    Button saveButton;
    EditText lat, longi, alt;
    boolean unsyncedFlag = false;
    int permissionCheckTake, permissionCheckUpload;
    private Uri imageUri;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    Bitmap selectedImage = null;
    int projNumb, landNumb, permitNumb;
    JSONObject thisService = null;


    // TODO: Unfinished

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Site");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        Button takephoto = (Button) findViewById(R.id.takePhoto);
        takephoto.setOnClickListener(this);

        Button uploadphoto = (Button) findViewById(R.id.uploadPhoto);
        uploadphoto.setOnClickListener(this);

        Button location = (Button) findViewById(R.id.installation_location);
        location.setOnClickListener(this);

        alias = (EditText) findViewById(R.id.alias);
        landmark = (EditText) findViewById(R.id.landmark);
        landOwner = (Spinner) findViewById(R.id.landOwner);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        landOwner.setAdapter(sAdapter);

        site_name = (EditText) findViewById(R.id.site_name);

        permit = (Spinner) findViewById(R.id.permit);
        sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        permit.setAdapter(sAdapter);

        doc_notes = (EditText) findViewById(R.id.doc_notes);
        project = (Spinner) findViewById(R.id.project);
        sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.projectNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        project.setAdapter(sAdapter);

        lat = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        alt = (EditText) findViewById(R.id.altitude);

        getInfo(MainActivity.selectedModuleName);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                if (unsyncedFlag)
                {

                    try
                    {
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
                        String date = sdf.format(new Date());


                        thisService.put("Latitude", Float.parseFloat(lat.getText().toString()));
                        thisService.put("Longitude", Float.parseFloat(longi.getText().toString()));

                        //TODO: Fix this, but later. It posts and that's all that matters
                        String el = "1.234";
                        if (Float.parseFloat(alt.getText().toString()) == 0)
                            thisService.put("Elevation", Float.parseFloat(el));
                        else
                            thisService.put("Elevation", Float.parseFloat(alt.getText().toString()));

                        thisService.put("Modification Date", date);

                        EditText info = (EditText) findViewById(R.id.site_name);
                        thisService.put("Name", info.getText().toString());

                        info = (EditText) findViewById(R.id.doc_notes);
                        thisService.put("Notes", info.getText().toString());

                        info = (EditText) findViewById(R.id.alias);
                        thisService.put("Alias", info.getText().toString());

                        thisService.put("Land Owner", landNumb);

                        thisService.put("Permit Holder", permitNumb);

                        thisService.put("Time Zone Name", TimeZone.getDefault().getDisplayName());

                        thisService.put("Time Zone Abbreviation", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));

                        thisService.put("Time Zone Offset", (TimeZone.getDefault().getRawOffset()/1000)/60);

                        thisService.put("Project", projNumb);

                        info = (EditText) findViewById(R.id.landmark);
                        thisService.put("GPS Landmark", info.getText().toString());

                        if (selectedImage != null)
                        {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                            byte[] b = baos.toByteArray();
                            String hexTest = String.format("%x", new BigInteger(1, b));
                            thisService.put("Landmark Photo", hexTest);
                        }

                        else { thisService.put("Landmark Photo", 0); }
                        MainActivity.ListHandler.addChild("Unsynced", thisService.getString("Name"));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"Cannot edit data already synced to the Server", Toast.LENGTH_LONG);
                }

                finish();
                break;

            case R.id.uploadPhoto:
                uploadPhotoPermissions();
                uploadPhoto();
                break;

            case R.id.takePhoto:
                takePhotoPermissions();
                takePhoto();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case (R.id.project):
                if (position > 0)
                    projNumb = getInfo.projectNumber[position - 1];
                break;

            case (R.id.landOwner):
                if (position > 0)
                    landNumb = getInfo.peopleNumber[position - 1];
                break;

            case (R.id.permit):
                if (position > 0)
                    permitNumb = getInfo.peopleNumber[position - 1];
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


public void takePhotoPermissions(){
        permissionCheckTake = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if(permissionCheckTake != 0){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                2);
        }

        // Checks again so that the camera can open
        permissionCheckTake = ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA);

        }

public void takePhoto(){
        if(permissionCheckTake == 0) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "HotPic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PHOTO);
        }

        }

public void uploadPhotoPermissions(){
        permissionCheckUpload = ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permissionCheckUpload != 0){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
        3);
        }

        // Checks again so gallery can open
        permissionCheckUpload = ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE);

        }

public void uploadPhoto(){
        if(permissionCheckUpload == 0) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        }


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
        {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode)
        {
        case SELECT_PHOTO:

        try
        {
final Uri imageUri = imageReturnedIntent.getData();
final InputStream imageStream = getContentResolver().openInputStream(imageUri);
        selectedImage = BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }

        break;

        case TAKE_PHOTO:

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

        break;
        }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.activity_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch(menu.getItemId()){
            case R.id.cancel_button:
                finish();
                return true;
            case R.id.delete_button:
                if (unsyncedFlag)
                {
                    try
                    {
                        MainActivity.ListHandler.removeChild("Unsynced", MainActivity.selectedModuleName);
                        getInfo.unsynced.getJSONArray("Sites").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(),"You cannot delete data already synced to the server", Toast.LENGTH_LONG).show();
                }
                overridePendingTransition(0, 0);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    private int findEntry(String name, JSONArray modules) throws JSONException
    {
        for(int i = 0; i < modules.length(); i++)
        {
            if (modules.getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private int findUnsyncedEntry(String name, JSONObject modules) throws JSONException
    {
        JSONArray t = modules.getJSONArray("Sites");
        for(int i = 0; i < t.length(); i++)
        {
            if (t.getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private void getInfo(String entryName)
    {

        try {
            thisService = getInfo.sites.getJSONObject(findEntry(entryName, getInfo.sites));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisService = getInfo.unsynced.getJSONArray("Sites").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {

            alias.setText(thisService.getString("Alias"));
            landmark.setText(thisService.getString("GPS Landmark"));
            //location.setText(thisService.getString("Location"));
            //landOwner.setText(thisService.getString("Notes"));
            site_name.setText(thisService.getString("Name"));
            //permit.setText(thisService.getString("Notes"));
            doc_notes.setText(thisService.getString("Notes"));
            longi.setText(thisService.getString("Longitude"));
            lat.setText(thisService.getString("Latitude"));
            alt.setText(thisService.getString("Elevation"));

            if (!thisService.isNull("Project"))
            {
                int pIndex = thisService.getInt("Project");
                String pName = "error: not found";
                for (int i = 0; i < getInfo.projects.length(); i++)
                {
                    if (getInfo.projects.getJSONObject(i).getInt("Project") == pIndex)
                    {
                        JSONObject dude = getInfo.projects.getJSONObject(i);
                        pName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.projectNames.length; i++)
                {
                    if (Objects.equals(getInfo.projectNames[i], pName))
                    {
                        project.setSelection(i);
                        break;
                    }
                }
            }
            else { project.setSelection(0); }



            if (!thisService.isNull("Permit Holder"))
            {
                int cIndex = thisService.getInt("Permit Holder");
                String cName = "error: not found";
                for (int i = 0; i < getInfo.people.length(); i++)
                {
                    if (getInfo.people.getJSONObject(i).getInt("Person") == cIndex)
                    {
                        JSONObject dude = getInfo.people.getJSONObject(i);
                        cName = dude.getString("First Name") + " " + dude.getString("Last Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.peopleNames.length; i++)
                {
                    if (Objects.equals(getInfo.peopleNames[i], cName))
                    {
                        permit.setSelection(i);
                        break;
                    }
                }
            }
            else { permit.setSelection(0); }


            if (!thisService.isNull("Land Owner"))
            {
                int cIndex = thisService.getInt("Land Owner");
                String cName = "error: not found";
                for (int i = 0; i < getInfo.people.length(); i++)
                {
                    if (getInfo.people.getJSONObject(i).getInt("Person") == cIndex)
                    {
                        JSONObject dude = getInfo.people.getJSONObject(i);
                        cName = dude.getString("First Name") + " " + dude.getString("Last Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.peopleNames.length; i++)
                {
                    if (Objects.equals(getInfo.peopleNames[i], cName))
                    {
                        landOwner.setSelection(i);
                        break;
                    }
                }
            }
            else { landOwner.setSelection(0); }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
