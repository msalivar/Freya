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
import java.util.UUID;

/**
 * Created by cil on 2/18/16.
 */
public class CreateNewServiceEntry extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    EditText info;
    int projNumb, creatorNumb, systemNumb, componentNumb;
    String ServiceEntryFile = "ServiceEntryFile.txt";
    Button createButton, SEButton;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    private Uri imageUri;
    Bitmap selectedImage;
    ImageView imageView;
    boolean writeAccepted, cameraAccepted;
    private int permissionCheckUpload;
    private int permissionCheckTake;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_entry_display);

        getActionBar().setTitle("Create New Service Entry");
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        Spinner project = (Spinner) findViewById(R.id.SEprojects);
        Spinner creator = (Spinner) findViewById(R.id.creator);
        Spinner system = (Spinner) findViewById(R.id.SEsystem);
        Spinner component = (Spinner) findViewById(R.id.SEcomponent);

        Modules.spinner(this, getInfo.projectNames, project);
        Modules.spinner(this, getInfo.peopleNames, creator);
        Modules.spinner(this, getInfo.systemNames, system);
        Modules.spinner(this, getInfo.componentNames, component);

        Button takephoto = (Button) findViewById(R.id.takePhoto);
        takephoto.setOnClickListener(this);

        Button uploadphoto = (Button) findViewById(R.id.uploadPhoto);
        uploadphoto.setOnClickListener(this);

        try {Modules.read(ServiceEntryFile, this);}
        catch (FileNotFoundException e) {e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.sync).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // photo picker code, not currently implemented, but in place for implemenation
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (parent.getId())
        {
            case (R.id.SEprojects):
                if (position > 0)
                    projNumb = getInfo.projectNumber[position - 1];
                break;

            case (R.id.creator):
                if (position > 0)
                    creatorNumb = getInfo.peopleNumber [position - 1];
                break;

            case (R.id.SEsystem):
                if (position > 0)
                    systemNumb = getInfo.systemNumber[position - 1];
                break;

            case (R.id.SEcomponent):
                if (position > 0)
                    componentNumb = getInfo.componentNumber[position - 1];
                break;
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        //autogenerated
    }

    public void newServiceEntry() throws JSONException
    {
        //Create JSONObject here
        JSONObject JSON = createServiceEntryJSON();

        JSONArray service = new JSONArray();
        service.put(JSON);

        getInfo.complete.put("Service Entries", service);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try {newServiceEntry();} catch (JSONException e) {e.printStackTrace();}
                try{
                    Modules.write(info, ServiceEntryFile, this);}
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



    public JSONObject createServiceEntryJSON() throws JSONException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());

        info = (EditText) findViewById(R.id.SEname);
        jsonParam.put("Name", info.getText().toString());

        jsonParam.put("Date", date);

        info = (EditText) findViewById(R.id.SEnotes);
        jsonParam.put("Notes", info.getText().toString());

        info = (EditText) findViewById(R.id.operation);
        jsonParam.put("Operation", info.getText().toString());

        jsonParam.put("Project", projNumb);

        jsonParam.put("Creator", creatorNumb);

        jsonParam.put("System", systemNumb);

        jsonParam.put("Component", componentNumb);

        jsonParam.put("Modification Date", date);

        jsonParam.put("Creation Date", date);

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

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


}
