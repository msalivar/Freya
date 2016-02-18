package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import android.widget.AdapterView.OnItemSelectedListener;


/**
 * Created by cil on 2/5/16.
 */
public class CreateNewDocument extends Activity implements OnItemSelectedListener
{
//   Spinner selectedProject;
//    Spinner selectedSite;
//    Spinner selectedDeployment;
//    Spinner selectedComponent;
//    Spinner selectedServiceEntry;
//
//        @Override
//        protected void onCreate (Bundle savedInstanceState)
//        {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.create_new_document);
//
//            selectedProject = (Spinner) findViewById(R.id.document_project);
//            selectedSite = (Spinner) findViewById(R.id.document_site);
//            selectedDeployment = (Spinner) findViewById(R.id.document_deployment);
//            selectedComponent = (Spinner) findViewById(R.id.document_component);
//            selectedServiceEntry = (Spinner) findViewById(R.id.document_service);
//
//            try
//            {
//                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.projectNames);
//                spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                selectedProject.setAdapter(spinAdapter);
//                selectedProject.setOnItemSelectedListener(this);
//            }catch (NullPointerException e){
//                Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//
//            try
//            {
//                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.siteNames);
//                spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                selectedSite.setAdapter(spinAdapter);
//                selectedSite.setOnItemSelectedListener(this);
//            }catch (NullPointerException e){
//                Toast.makeText(this, "Unable to populate Sites. Sync before trying again.", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//
//            try
//            {
//                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.deploymentNames);
//                spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                selectedDeployment.setAdapter(spinAdapter);
//                selectedDeployment.setOnItemSelectedListener(this);
//            }catch (NullPointerException e){
//                Toast.makeText(this, "Unable to populate Deployment. Sync before trying again.", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//
//            try
//            {
//                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.componentNames);
//                spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                selectedComponent.setAdapter(spinAdapter);
//                selectedComponent.setOnItemSelectedListener(this);
//            }catch (NullPointerException e){
//                Toast.makeText(this, "Unable to populate Component. Sync before trying again.", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//
//            try
//            {
//                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.serviceNames);
//                spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                selectedServiceEntry.setAdapter(spinAdapter);
//                selectedServiceEntry.setOnItemSelectedListener(this);
//            }catch (NullPointerException e){
//                Toast.makeText(this, "Unable to populate Projects. Sync before trying again.", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//
//        }
//
//        public void newDocument()throws JSONException{
//            //Create JSONObject here
//            JSONObject JSON = createDocumentJSON();
//
//            CreateNewProject.complete.put("Document", JSON);
//        }
//
//        public JSONObject createDocumentJSON() throws JSONException{
//            JSONObject jsonParam = new JSONObject();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
//            String date = sdf.format(new Date());
//            EditText info = null;
//
//            jsonParam.put("Unique Identifier", UUID.randomUUID().toString());
//
//            info = (EditText) findViewById(R.id.document_name);
//            jsonParam.put("Name",info.getText().toString());
//
//            info = (EditText) findViewById(R.id.doc_notes);
//            jsonParam.put("Notes",info.getText().toString());
//
//            info = (EditText) findViewById(R.id.path);
//            jsonParam.put("Alias", info.getText().toString());
//
//            jsonParam.put("Modification Date", date);
//
//            jsonParam.put("Creation Date", date);
//
//            //not finished
//
//            return jsonParam;
//        }
//
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // still need to fill out, get switch for multiple spinners?

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
