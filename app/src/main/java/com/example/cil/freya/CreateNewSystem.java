package com.example.cil.freya;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

/**
 * Created by cil on 11/19/15.
 */
public class CreateNewSystem extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    String SystemFile = "SystemFile.txt";
    EditText info;
    Bitmap selectedImage;
    int managerNumb, siteNumb;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    private Uri imageUri;
    boolean writeAccepted, cameraAccepted;
    EditText lat, longi, alt;
    private int permissionCheckUpload;
    private int permissionCheckTake;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_display);
       
        getActionBar().setTitle("Create New System");
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        Button takephoto = (Button) findViewById(R.id.takePhoto);
        takephoto.setOnClickListener(this);

        Button uploadphoto = (Button) findViewById(R.id.uploadPhoto);
        uploadphoto.setOnClickListener(this);

        Button location = (Button) findViewById(R.id.installation_location);
        location.setOnClickListener(this);

        Spinner manager = (Spinner) findViewById(R.id.manager);
        Spinner site = (Spinner) findViewById(R.id.site);

        Modules.spinner (this, getInfo.peopleNames, manager);
        Modules.spinner (this, getInfo.siteNames, site);

        lat = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        alt = (EditText) findViewById(R.id.altitude);

        try{
            Modules.read(SystemFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.sync).setVisible(false);
        return super.onCreateOptionsMenu(menu);
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
                    Bitmap bitmap;
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


    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try
                {newSystem();} catch (JSONException e) {e.printStackTrace();}
                overridePendingTransition(0, 0);
                try{
                    Modules.write(info, SystemFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                finish();
                break;

            case (R.id.uploadPhoto):
                uploadPhotoPermissions();
                uploadPhoto();
                break;

            case (R.id.takePhoto):
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
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case (R.id.manager):
                if (position > 0)
                    managerNumb = getInfo.peopleNumber[position - 1];
                break;
            case (R.id.site):
                if (position > 0)
                    siteNumb = getInfo.siteNumber[position - 1];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // automatically generated
    }

    public void newSystem() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createSystemJSON();

        JSONArray system = new JSONArray();
        system.put(JSON);

        getInfo.complete.put("Systems", system);
    }

    public JSONObject createSystemJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());

        EditText info = (EditText) findViewById(R.id.details);
        jsonParam.put("Details", info.getText().toString());

        //from gps
        jsonParam.put("Latitude", lat.getText().toString());
        jsonParam.put("Longitude", longi.getText().toString());
        jsonParam.put("Altitude", alt.getText().toString());

        info = (EditText) findViewById(R.id.sysname);
        jsonParam.put("Name", info.getText().toString());

        info = (EditText) findViewById(R.id.power);
        jsonParam.put("Power", info.getText().toString());

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        //from people
        jsonParam.put("Manager", managerNumb);

        jsonParam.put("Creation Date", date);

       // jsonParam.put("Installation Date", date);

        jsonParam.put("Modification Date", date);

        // TODO: Test this part
        if (selectedImage != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();
            String hexTest = String.format("%x", new BigInteger(1, b));
            jsonParam.put("Photo", hexTest);
        }
        else { jsonParam.put("Photo", 0); }

        // from site
        jsonParam.put("Site", siteNumb);

        return jsonParam;
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
