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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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
    Button createButton, photoButton;
    String SystemFile = "SystemFile.txt";
    EditText info;
    Bitmap selectedImage;
    int managerNumb, siteNumb;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    private Uri imageUri;
    boolean writeAccepted, cameraAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_display);

        getActionBar().setTitle("Create New System");
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        photoButton = (Button) findViewById(R.id.photoButton);
        photoButton.setOnClickListener(this);
        registerForContextMenu(photoButton);

        Spinner manager = (Spinner) findViewById(R.id.manager);
        Spinner site = (Spinner) findViewById(R.id.site);

        Modules.spinner (this, getInfo.peopleNames, manager);
        Modules.spinner (this, getInfo.siteNames, site);

        try{
            Modules.read(SystemFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.sync).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        switch(menu.getItemId()){
            case R.id.upload_photo:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Pick One:");
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        super.onContextItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.choose_photo:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                return true;
            case R.id.take_photo:
                if (!hasPermission(MainActivity.readPerm[0])) { requestPermissions(MainActivity.readPerm, MainActivity.readRequestCode); }
                if (!hasPermission(MainActivity.cameraPerm[0])) { requestPermissions(MainActivity.cameraPerm, MainActivity.cameraRequestCode); }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(),  "HotPic.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                startActivityForResult(intent, TAKE_PHOTO);
                return true;
            default:
                return false;
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

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults)
    {
        switch(permsRequestCode)
        {
            case 200:
                writeAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
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

    public void onClick(View v)
    {
        Intent intent;
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

            case (R.id.photoButton):
                this.openContextMenu(v);
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
        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Installation Location",info);

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
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String hexTest = String.format("%x", new BigInteger(1, b));
            jsonParam.put("Photo", hexTest);
        }
        else { jsonParam.put("Photo", 0); }

        // from site
        jsonParam.put("Site", siteNumb);

        return jsonParam;
    }

}