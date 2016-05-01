package com.example.cil.freya.ModuleDisplayActivities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
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
import java.util.UUID;

public class SystemDisplayActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    EditText details, power, sysname, location;
    Spinner manager, site;
    Button saveButton;
    boolean unsyncedFlag = false;
    private int permissionCheckUpload;
    private int permissionCheckTake;
    Bitmap selectedImage;
    int managerNumb, siteNumb;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    private Uri imageUri;
    EditText lat, longi, alt;
    JSONObject thisSystem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("System");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        Button takephoto = (Button) findViewById(R.id.takePhoto);
        takephoto.setOnClickListener(this);

        Button uploadphoto = (Button) findViewById(R.id.uploadPhoto);
        uploadphoto.setOnClickListener(this);

        details = (EditText) findViewById(R.id.details);
        location = (EditText) findViewById(R.id.installation_location);
        power = (EditText) findViewById(R.id.power);
        sysname = (EditText) findViewById(R.id.sysname);
        manager = (Spinner) findViewById(R.id.manager);
        ArrayAdapter<String> managerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        managerAdapter.setDropDownViewResource(R.layout.spinner_item);
        manager.setAdapter(managerAdapter);
        site = (Spinner) findViewById(R.id.site);
        ArrayAdapter<String> siteAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.siteNames);
        siteAdapter.setDropDownViewResource(R.layout.spinner_item);
        site.setAdapter(siteAdapter);
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

                        EditText info = (EditText) findViewById(R.id.details);
                        thisSystem.put("Details", info.getText().toString());

                         info = (EditText) findViewById(R.id.sysname);
                        thisSystem.put("Name", info.getText().toString());

                        info = (EditText) findViewById(R.id.power);
                        thisSystem.put("Power", info.getText().toString());

                        thisSystem.put("Unique Identifier", UUID.randomUUID().toString());

                        //from people
                        thisSystem.put("Manager", managerNumb);

                        info = (EditText) findViewById(R.id.installation_location);
                        thisSystem.put("Installation Location", info.getText().toString());

                        thisSystem.put("Modification Date", date);

                        // TODO: Test this part
                        if (selectedImage != null)
                        {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                            byte[] b = baos.toByteArray();
                            String hexTest = String.format("%x", new BigInteger(1, b));
                            thisSystem.put("Photo", hexTest);
                        }
                        else { thisSystem.put("Photo", 0); }

                        // from site
                        thisSystem.put("Site", siteNumb);

                        MainActivity.ListHandler.addChild("Unsynced", thisSystem.getString("Name"));

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

            case (R.id.uploadPhoto):
                uploadPhotoPermissions();
                uploadPhoto();
                break;

            case (R.id.takePhoto):
                takePhotoPermissions();
                takePhoto();
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
                        getInfo.unsynced.getJSONArray("Systems").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

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
        for(int i = 0; i < modules.getJSONArray("Systems").length(); i++)
        {
            if (modules.getJSONArray("Systems").getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }

    private void getInfo(String entryName)
    {

        try {
            thisSystem = getInfo.systems.getJSONObject(findEntry(entryName, getInfo.systems));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisSystem = getInfo.unsynced.getJSONArray("Systems").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {
            details.setText(thisSystem.getString("Details"));
            location.setText(thisSystem.getString("Installation Location"));
            power.setText(thisSystem.getString("Power"));
            sysname.setText(thisSystem.getString("Name"));

            if (!thisSystem.isNull("Manager"))
            {
                int personIndex = thisSystem.getInt("Manager");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.people.length(); i++)
                {
                    if (getInfo.people.getJSONObject(i).getInt("Person") == personIndex)
                    {
                        JSONObject dude = getInfo.people.getJSONObject(i);
                        theirName = dude.getString("First Name") + " " + dude.getString("Last Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.peopleNames.length; i++)
                {
                    if (Objects.equals(getInfo.peopleNames[i], theirName))
                    {
                        manager.setSelection(i);
                        break;
                    }
                }
            }
            else { manager.setSelection(0); }

            if (!thisSystem.isNull("Site"))
            {
                int siteIndex = thisSystem.getInt("Site");
                String siteName = "error: not found";
                for (int i = 0; i < getInfo.sites.length(); i++)
                {
                    if (getInfo.sites.getJSONObject(i).getInt("Site") == siteIndex)
                    {
                        JSONObject dude = getInfo.sites.getJSONObject(i);
                        siteName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.siteNames.length; i++)
                {
                    if (Objects.equals(getInfo.siteNames[i], siteName))
                    {
                        site.setSelection(i);
                        break;
                    }
                }
            }
            else { site.setSelection(0); }

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
