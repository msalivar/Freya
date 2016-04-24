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

public class CreateNewComponent extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    String ComponentFile = "ComponentFile.txt";
    EditText info;
    int deploymentNumb;
    Bitmap selectedImage;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    private Uri imageUri;
    boolean writeAccepted, cameraAccepted;
    private int permissionCheckUpload;
    private int permissionCheckTake;

    public CreateNewComponent()
    {
        super();
    }


    // display create new component GUI
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_display);

        // Sets title in action bar
        getActionBar().setTitle("Create New Component");

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        Spinner deployment = (Spinner) findViewById(R.id.deployment);
        Modules.spinner(this, getInfo.deploymentNames, deployment);

        Button takephoto = (Button) findViewById(R.id.takePhoto);
        takephoto.setOnClickListener(this);

        Button uploadphoto = (Button) findViewById(R.id.uploadPhoto);
        uploadphoto.setOnClickListener(this);

        try{
            Modules.read(ComponentFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.sync).setVisible(false);
        menu.findItem(R.id.upload).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            deploymentNumb = getInfo.deploymentNumber[position - 1];
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
    public void onNothingSelected(AdapterView<?> parent)
    {
        // automatically generated
    }

    public void newComponent() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createComponentJSON();

        if (getInfo.unsynced.isNull("Components"))
        {
            JSONArray component = new JSONArray();
            component.put(JSON);
            getInfo.unsynced.put("Components", component);
        }
        else
        {
            getInfo.unsynced.getJSONArray("Components").put(JSON);
        }
        MainActivity.expListView.setAdapter(MainActivity.ListHandler.prepareListData(MainActivity.getContext()));
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try {newComponent();} catch (JSONException e) {e.printStackTrace();}
                overridePendingTransition(0, 0);
                try{
                    Modules.write(info, ComponentFile, this);}
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
        }
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



    public JSONObject createComponentJSON() throws JSONException{
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());


        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.compname);
        jsonParam.put("Name", info.getText().toString());

        info = (EditText) findViewById(R.id.manufacturer);
        jsonParam.put("Manufacturer", info.getText().toString());

        info = (EditText) findViewById(R.id.model);
        jsonParam.put("Model",info.getText().toString());

        info = (EditText) findViewById(R.id.serial_number);
        jsonParam.put("Serial Number", info.getText().toString());

        // may need to be a spinner
        info = (EditText) findViewById(R.id.vendor);
        jsonParam.put("Vendor", info.getText().toString());

        info = (EditText) findViewById(R.id.supplier);
        jsonParam.put("Supplier", info.getText().toString());

        info = (EditText) findViewById(R.id.installation);
        jsonParam.put("Installation Details", info.getText().toString());

        info = (EditText) findViewById(R.id.wiring_notes);
        jsonParam.put("Wiring Notes", info.getText().toString());

        // needs spinner
        jsonParam.put("Deployment", deploymentNumb);

        jsonParam.put("Installation Date", date);

        jsonParam.put("Modification Date", date);

        jsonParam.put("Last Calibrated Date", date);

        jsonParam.put("Creation Date", date);

        if (selectedImage != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();
            String hexTest = String.format("%x", new BigInteger(1, b));
            jsonParam.put("Photo", hexTest);
        }
        else { jsonParam.put("Photo", 0); }

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