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

public class ServiceEntryDisplayActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    EditText name, operations, SEnotes;
    Spinner project, creator, system, component;
    Button saveButton;
    private int permissionCheckUpload;
    private int permissionCheckTake;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    private Uri imageUri;
    Bitmap selectedImage;
    int projNumb, creatorNumb, systemNumb, componentNumb;

    boolean unsyncedFlag = false;
    JSONObject thisService = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_entry_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Service Entry");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        name = (EditText) findViewById(R.id.SEname);
        operations = (EditText) findViewById(R.id.operation);
        SEnotes = (EditText) findViewById(R.id.SEnotes);

        project = (Spinner) findViewById(R.id.SEprojects);
        ArrayAdapter<String> pAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.projectNames);
        pAdapter.setDropDownViewResource(R.layout.spinner_item);
        project.setAdapter(pAdapter);

        creator = (Spinner) findViewById(R.id.creator);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.peopleNames);
        cAdapter.setDropDownViewResource(R.layout.spinner_item);
        creator.setAdapter(cAdapter);

        system = (Spinner) findViewById(R.id.SEsystem);
        ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.systemNames);
        sAdapter.setDropDownViewResource(R.layout.spinner_item);
        system.setAdapter(sAdapter);

        component = (Spinner) findViewById(R.id.SEcomponent);
        ArrayAdapter<String> compAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.componentNames);
        compAdapter .setDropDownViewResource(R.layout.spinner_item);
        component.setAdapter(compAdapter );

        Button takephoto = (Button) findViewById(R.id.takePhoto);
        takephoto.setOnClickListener(this);

        Button uploadphoto = (Button) findViewById(R.id.uploadPhoto);
        uploadphoto.setOnClickListener(this);

        getInfo(MainActivity.selectedModuleName);
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
                        getInfo.unsynced.getJSONArray("Service Entries").remove(findUnsyncedEntry(MainActivity.selectedModuleName, getInfo.unsynced));

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


                        EditText info = (EditText) findViewById(R.id.SEname);
                        thisService.put("Name", info.getText().toString());

                        thisService.put("Date", date);

                        info = (EditText) findViewById(R.id.SEnotes);
                        thisService.put("Notes", info.getText().toString());

                        info = (EditText) findViewById(R.id.operation);
                        thisService.put("Operation", info.getText().toString());

                        thisService.put("Project", projNumb);

                        thisService.put("Creator", creatorNumb);

                        thisService.put("System", systemNumb);

                        thisService.put("Component", componentNumb);

                        thisService.put("Modification Date", date);


                        if (selectedImage != null)
                        {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                            byte[] b = baos.toByteArray();
                            String hexTest = String.format("%x", new BigInteger(1, b));
                            thisService.put("Photo", hexTest);
                        }

                        else { thisService.put("Photo", 0); }
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
        }
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
        for(int i = 0; i < modules.getJSONArray("Service Entries").length(); i++)
        {
            if (modules.getJSONArray("Service Entries").getJSONObject(i).getString("Name").equals(name))
            {
                return i;
            }
        }
        return 0;
    }


    private void getInfo(String entryName)
    {

        try {
            thisService = getInfo.services.getJSONObject(findEntry(entryName, getInfo.services));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            thisService = getInfo.unsynced.getJSONArray("Service Entries").getJSONObject(findUnsyncedEntry(entryName, getInfo.unsynced));
            unsyncedFlag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try
        {

            name.setText(thisService.getString("Name"));
            operations.setText(thisService.getString("Operation"));
            SEnotes.setText(thisService.getString("Notes"));

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

            if (!thisService.isNull("Creator"))
            {
                int cIndex = thisService.getInt("Creator");
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
                        creator.setSelection(i);
                        break;
                    }
                }
            }
            else { creator.setSelection(0); }

            if (!thisService.isNull("System"))
            {
                int sIndex = thisService.getInt("System");
                String sName = "error: not found";
                for (int i = 0; i < getInfo.systems.length(); i++)
                {
                    if (getInfo.systems.getJSONObject(i).getInt("System") == sIndex)
                    {
                        JSONObject dude = getInfo.systems.getJSONObject(i);
                        sName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.systemNames.length; i++)
                {
                    if (Objects.equals(getInfo.systemNames[i], sName))
                    {
                        system.setSelection(i);
                        break;
                    }
                }
            }
            else { system.setSelection(0); }

            if (!thisService.isNull("Component"))
            {
                int compIndex = thisService.getInt("Component");
                String compName = "error: not found";
                for (int i = 0; i < getInfo.components.length(); i++)
                {
                    if (getInfo.components.getJSONObject(i).getInt("Component") == compIndex)
                    {
                        JSONObject dude = getInfo.components.getJSONObject(i);
                        compName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.componentNames.length; i++)
                {
                    if (Objects.equals(getInfo.componentNames[i], compName))
                    {
                        component.setSelection(i);
                        break;
                    }
                }
            }
            else { component.setSelection(0); }

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
