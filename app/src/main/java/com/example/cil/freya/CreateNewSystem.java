package com.example.cil.freya;

import android.app.Activity;
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

/**
 * Created by cil on 11/19/15.
 */
public class CreateNewSystem extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    Button createButton, backButton, photoButton;
    String SystemFile = "SystemFile.txt";
    EditText info;
    int managerNumb, siteNumb;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_systems);
        createButton = (Button) findViewById(R.id.newSystemButton);
        createButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.backSystemButton);
        backButton.setOnClickListener(this);
        photoButton = (Button) findViewById(R.id.photoButton);
        photoButton.setOnClickListener(this);
        registerForContextMenu(photoButton);

        Spinner manager = (Spinner) findViewById(R.id.manager);
        Spinner site = (Spinner) findViewById(R.id.site);

        Modules.spinner (this, getInfo.people, manager);
        Modules.spinner (this, getInfo.siteNames, site);

        try{
            Modules.read(SystemFile, this);}
        catch(FileNotFoundException e){e.printStackTrace();}
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

    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case (R.id.newSystemButton):
                try
                {newSystem();} catch (JSONException e) {e.printStackTrace();}
                intent = new Intent(this, CreateNewDeployment.class);
                startActivity(intent);
                try{
                    Modules.write(info, SystemFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                break;

            case (R.id.backSystemButton):
                intent = new Intent(this, CreateNewSite.class);
                startActivity(intent);
                try{
                    Modules.write(info, SystemFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                break;

            case (R.id.photoButton):
                this.openContextMenu(v);
                break;
        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        switch (view.getId())
        {
            case (R.id.compname):
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

        jsonParam.put("Installation Date", date);

        jsonParam.put("Modification Date", date);

        // need photo info
        jsonParam.put ("Photo", null);

        // from site
        jsonParam.put("Site", siteNumb);

        return jsonParam;
    }

}