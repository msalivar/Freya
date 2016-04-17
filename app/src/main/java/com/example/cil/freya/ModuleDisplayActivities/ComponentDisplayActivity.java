package com.example.cil.freya.ModuleDisplayActivities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
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
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class ComponentDisplayActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener
{
    EditText installation, manufacturer, model, compname, serial_number, supplier, vendor, wiring_notes;
    Spinner deployment;
    Button saveButton;
    EditText info;
    int deploymentNumb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_display);

        // Sets Name of Screen in top left corner
        getActionBar().setTitle("Component");

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        installation = (EditText) findViewById(R.id.installation);
        manufacturer = (EditText) findViewById(R.id.manufacturer);
        model = (EditText) findViewById(R.id.model);
        compname = (EditText) findViewById(R.id.compname);
        serial_number = (EditText) findViewById(R.id.serial_number);
        supplier = (EditText) findViewById(R.id.supplier);
        vendor = (EditText) findViewById(R.id.vendor);
        wiring_notes = (EditText) findViewById(R.id.wiring_notes);
        deployment = (Spinner) findViewById(R.id.deployment);
        ArrayAdapter<String> componentAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, getInfo.deploymentNames);
        componentAdapter.setDropDownViewResource(R.layout.spinner_item);
        deployment.setAdapter(componentAdapter);
        deployment.setOnItemSelectedListener(this);
        getInfo(MainActivity.selectedModuleIndex);
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
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
            deploymentNumb = getInfo.deploymentNumber[position - 1];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {}

    public void editComponent() throws JSONException
    {
        //Create JSONObject here
        JSONObject JSON = createComponentJSON();

        if (getInfo.complete.isNull("Components"))
        {
            JSONArray component = new JSONArray();
            component.put(JSON);
            getInfo.complete.put("Components", component);
        }
        else
        {
            getInfo.complete.getJSONArray("Components").put(JSON);
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

        /*if (selectedImage != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();
            String hexTest = String.format("%x", new BigInteger(1, b));
            jsonParam.put("Photo", hexTest);
        }*/
        //else { jsonParam.put("Photo", 0); }
        jsonParam.put("Photo", 0);

        return jsonParam;
    }

    private void getInfo(int projectIndex)
    {
        JSONArray modules = getInfo.components;
        try
        {
            JSONObject thisComponent = modules.getJSONObject(projectIndex);
            installation.setText(thisComponent.getString("Installation Details"));
            manufacturer.setText(thisComponent.getString("Manufacturer"));
            model.setText(thisComponent.getString("Model"));
            compname.setText(thisComponent.getString("Name"));
            serial_number.setText(thisComponent.getString("Serial Number"));
            supplier.setText(thisComponent.getString("Supplier"));
            vendor.setText(thisComponent.getString("Vendor"));
            wiring_notes.setText(thisComponent.getString("Wiring Notes"));

            if (!thisComponent.isNull("Deployment"))
            {
                int deploymentIndex = thisComponent.getInt("Deployment");
                String theirName = "error: not found";
                for (int i = 0; i < getInfo.deployments.length(); i++)
                {
                    if (getInfo.deployments.getJSONObject(i).getInt("Deployment") == deploymentIndex)
                    {
                        JSONObject dude = getInfo.deployments.getJSONObject(i);
                        theirName = dude.getString("Name");
                        break;
                    }
                }
                for (int i = 0; i < getInfo.deploymentNames.length; i++)
                {
                    if (Objects.equals(getInfo.deploymentNames[i], theirName))
                    {
                        deployment.setSelection(i);
                        break;
                    }
                }
            }
            else { deployment.setSelection(0); }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.saveButton):
                try
                {
                    editComponent();
                }
                catch(JSONException e)
                {
                    Log.e("Component Edit Error", e.toString());
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Please use cancel or save to exit.", Toast.LENGTH_SHORT).show();
    }
}
