package com.example.cil.freya;

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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by cil on 11/18/15.
 */
public class CreateNewSite extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener
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
    int permissionCheckTake, permissionCheckUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_display);

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
        menu.findItem(R.id.upload).setVisible(false);
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

    public void newSite() throws JSONException
    {
        //Create JSONObject here
        JSONObject JSON = createSiteJSON();

        if (getInfo.unsynced.isNull("Sites"))
        {
            JSONArray site   = new JSONArray();
            site .put(JSON);
            getInfo.unsynced.put("Sites", site );
        }
        else
        {
            getInfo.unsynced.getJSONArray("Sites").put(JSON);
        }
        MainActivity.ListHandler.addChild("Unsynced", JSON.getString("Name"));
    }

    public JSONObject createSiteJSON() throws JSONException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());
        //from gps
        jsonParam.put("Latitude", lat.getText().toString());
        jsonParam.put("Longitude", longi.getText().toString());
        jsonParam.put("Elevation", alt.getText().toString());

        jsonParam.put("Creation Date", date);

        jsonParam.put("Modification Date", date);

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch(menu.getItemId()){
            case R.id.cancel_button:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

}
