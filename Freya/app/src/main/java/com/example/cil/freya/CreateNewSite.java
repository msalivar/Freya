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
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by cil on 11/18/15.
 */
public class CreateNewSite extends MainActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    Button createButton, previousButton,siteButton;
    Spinner proj;
    int numb;
    String SiteFile = "SiteFile.txt";
    EditText info;
    private final int SELECT_PHOTO = 1;
    private final int TAKE_PHOTO = 2;
    Bitmap selectedImage = null;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_site);
        createButton = (Button) findViewById(R.id.newSiteButton);
        createButton.setOnClickListener(this);
        previousButton = (Button) findViewById(R.id.backSiteButton);
        previousButton.setOnClickListener(this);
        siteButton = (Button) findViewById(R.id.sitePhoto);
        siteButton.setOnClickListener(this);
        registerForContextMenu(siteButton);

        proj = (Spinner) findViewById(R.id.project);
        Modules.spinner(this, getInfo.projectNames, proj);

    }


    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case (R.id.newSiteButton):
                try{newSite();} catch (JSONException e) {e.printStackTrace();}

                intent = new Intent(this, CreateNewSystem.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

                try{
                    Modules.write(info, SiteFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}

                break;

            case (R.id.backSiteButton):
                intent = new Intent(this, CreateNewProject.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                try{
                    Modules.write(info, SiteFile, this);}
                catch(FileNotFoundException e){e.printStackTrace();}
                break;

            case (R.id.sitePhoto):
                this.openContextMenu(v);
                break;
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            numb = getInfo.projectNumber[position - 1];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

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
                startActivityForResult(cameraIntent, TAKE_PHOTO);

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
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void newSite() throws JSONException
    {

        //Create JSONObject here
        JSONObject JSON = createSiteJSON();

        JSONArray site = new JSONArray();
        site.put(JSON);

        getInfo.complete.put("Sites", site);
    }

    public JSONObject createSiteJSON() throws JSONException
    {
        JSONObject jsonParam = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);

        /*LocationManager lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);

      /* jsonParam.put("Latitude",location.getLatitude());
        jsonParam.put("Longitude",location.getLongitude());
        jsonParam.put("Altitude",location.getAltitude());
        jsonParam.put("Latitude",location.get());*/
        /*Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        jsonParam.put("Location", location.toString());
*/
        jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

        info = (EditText) findViewById(R.id.site_name);
        jsonParam.put("Name", info.getText().toString());

        info = (EditText) findViewById(R.id.site_notes);
        jsonParam.put("Notes", info.getText().toString());

        info = (EditText) findViewById(R.id.alias);
        jsonParam.put("Alias", info.getText().toString());


        info = (EditText) findViewById(R.id.location);
        jsonParam.put("Location", info.getText().toString());

        info = (EditText) findViewById(R.id.landOwner);
        jsonParam.put("Land Owner", info.getText().toString());

        info = (EditText) findViewById(R.id.permit);
        jsonParam.put("Permit Holder", info.getText().toString());

        jsonParam.put("Time Zone Name", TimeZone.getDefault().getDisplayName());

        jsonParam.put("Time Zone Abbreviation", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));

        //is this the correct format?
        jsonParam.put("Time Zone Offset", TimeZone.getDefault().getRawOffset());

        jsonParam.put("Project", numb);

        info = (EditText) findViewById(R.id.landmark);
        jsonParam.put("GPS Landmark", info.getText().toString());

        if (selectedImage != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bArray = baos.toByteArray();
            String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
            jsonParam.put("Landmark Photo", encoded);
        }
        else
        {
            jsonParam.put("Landmark Photo", 0);
        }

        return jsonParam;
    }


}
