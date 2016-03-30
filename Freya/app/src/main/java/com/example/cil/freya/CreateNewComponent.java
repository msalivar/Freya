package com.example.cil.freya;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CreateNewComponent extends MainActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    Button createButton, backButton, CompButton;
    String ComponentFile = "ComponentFile.txt";
    EditText info;
    int deploymentNumb;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;

    // display create new component GUI
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_components);
        createButton = (Button) findViewById(R.id.newComponentButton);
        createButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.backComponentButton);
        backButton.setOnClickListener(this);

        Spinner deployment = (Spinner) findViewById(R.id.deployment);
        Modules.spinner(this, getInfo.deploymentNames, deployment);
        CompButton = (Button) findViewById(R.id.CompPhoto);
        CompButton.setOnClickListener(this);
        registerForContextMenu(CompButton);

        try{
            Modules.read(ComponentFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            deploymentNumb = getInfo.deploymentNumber[position - 1];
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

        JSONArray component = new JSONArray();
        component.put(JSON);

        getInfo.complete.put("Components", component);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Pick One");
        inflater.inflate(R.menu.context_menu, menu);
    }


    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case (R.id.newComponentButton):
                try
                {newComponent();} catch (JSONException e) {e.printStackTrace();}
                intent = new Intent(this, CreateNewDocument.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                try{
                    Modules.write(info, ComponentFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                break;

            case (R.id.backComponentButton):
                intent = new Intent(this, CreateNewDeployment.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                try{
                    Modules.write(info, ComponentFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                break;
            case (R.id.CompPhoto):
                this.openContextMenu(v);
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

        // needs photo
        jsonParam.put("Photo", 0);

        return jsonParam;
    }
}