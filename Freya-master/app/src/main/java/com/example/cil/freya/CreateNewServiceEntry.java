package com.example.cil.freya;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by cil on 2/18/16.
 */
public class CreateNewServiceEntry extends MainActivity implements View.OnClickListener, Spinner.OnItemSelectedListener
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_service_entry);

        Spinner project = (Spinner) findViewById(R.id.SEprojects);
        Spinner creator = (Spinner) findViewById(R.id.creator);
        Spinner system = (Spinner) findViewById(R.id.SEsystem);
        Spinner component = (Spinner) findViewById(R.id.SEcomponent);

        Modules.spinner(this, getInfo.projectNames, project);
        Modules.spinner(this, getInfo.peopleNames, creator);
        Modules.spinner(this, getInfo.systemNames, system);
        Modules.spinner(this, getInfo.componentNames, component);

        createButton = (Button) findViewById(R.id.newServiceButton);
        createButton.setOnClickListener(this);
        SEButton = (Button) findViewById(R.id.SEPhoto);
        SEButton.setOnClickListener(this);
        registerForContextMenu(SEButton);

        try {Modules.read(ServiceEntryFile, this);}
        catch (FileNotFoundException e) {e.printStackTrace();}
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Pick One");
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
                if (!hasPermission(MainActivity.writePerm[0])) { requestPermissions(MainActivity.writePerm, MainActivity.writeRequestCode); }
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

        getInfo.complete.put("ServiceEntries", service);
    }

    @Override
    public void onClick(View v)
    {        Intent intent;
        switch (v.getId())
        {
            case (R.id.newServiceButton):
                try {newServiceEntry();} catch (JSONException e) {e.printStackTrace();}
                try{
                    Modules.write(info, ServiceEntryFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                finish();
                break;

            case (R.id.SEPhoto):
                this.openContextMenu(v);
                break;
        }
    }



    public JSONObject createServiceEntryJSON() throws JSONException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String date = sdf.format(new Date());

        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

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

        if (selectedImage != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encoded = Base64.encodeToString(b, Base64.DEFAULT);
            // Not sure if this is needed ^ TODO: testing needed
            jsonParam.put("Photo", encoded);
        }
        else { jsonParam.put("Photo", 0); }

        return jsonParam;
    }


}
