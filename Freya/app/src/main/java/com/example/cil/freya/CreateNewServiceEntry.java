package com.example.cil.freya;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
    Button createButton, backButton, SEButton;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    Bitmap selectedImage;
    ImageView imageView;

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
        backButton = (Button) findViewById(R.id.backServiceButton);
        backButton.setOnClickListener(this);
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
    // photo picker code, not currently implemented, but in place for implemenation
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
               // if (resultCode == Activity.RESULT_OK)
             //   {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                //}
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
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                // start the image capture Intent
                startActivityForResult(cameraIntent, TAKE_PHOTO);

                return true;
            default:
                return false;
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
                new CRUD.writeMessage().execute(getInfo.complete);
              /*  try
                {newServiceEntry();} catch (JSONException e) {e.printStackTrace();}
                intent = new Intent(this, CreateNew.class);
                startActivity(intent);
                try{
                    Modules.write(info, ServiceEntryFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}*/
                break;

            case (R.id.backServiceButton):
                intent = new Intent(this, CreateNewDocument.class);
                startActivity(intent);
                overridePendingTransition(0,0);
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
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bArray = baos.toByteArray();
            String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
            jsonParam.put("Photo", encoded);
        }
        else { jsonParam.put("Photo", null);}

        return jsonParam;
    }


}
