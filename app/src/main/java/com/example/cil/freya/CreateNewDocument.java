package com.example.cil.freya;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

;


/**
 * Created by cil on 2/5/16.
 */
public class CreateNewDocument extends MainActivity implements OnItemSelectedListener
{
             int proNumb, siteNumb, deployNumb, componentNumb, serviceNumb;

        @Override
        protected void onCreate (Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.create_new_document);

            Spinner selectedProject = (Spinner) findViewById(R.id.document_project);
            Spinner selectedSite = (Spinner) findViewById(R.id.document_site);
            Spinner selectedDeployment = (Spinner) findViewById(R.id.document_deployment);
            Spinner selectedComponent = (Spinner) findViewById(R.id.document_component);
            Spinner selectedServiceEntry = (Spinner) findViewById(R.id.document_service);

            Modules.spinner(this, getInfo.projectNames, selectedProject);
            Modules.spinner(this, getInfo.siteNames, selectedSite);
            Modules.spinner(this, getInfo.deploymentNames, selectedDeployment);
            Modules.spinner(this, getInfo.componentNames, selectedComponent);
            Modules.spinner(this, getInfo.serviceNames, selectedServiceEntry);
        }

        public void newDocument()throws JSONException
        {
            //Create JSONObject here
            JSONObject JSON = createDocumentJSON();

            CreateNewProject.complete.put("Document", JSON);
        }

        public JSONObject createDocumentJSON() throws JSONException{
            JSONObject jsonParam = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            String date = sdf.format(new Date());
            EditText info = null;

            jsonParam.put("Unique Identifier", UUID.randomUUID().toString());

            info = (EditText) findViewById(R.id.document_name);
            jsonParam.put("Name",info.getText().toString());

            info = (EditText) findViewById(R.id.doc_notes);
            jsonParam.put("Notes",info.getText().toString());

            info = (EditText) findViewById(R.id.path);
            jsonParam.put("Path", info.getText().toString());

            jsonParam.put("Modification Date", date);

            jsonParam.put("Project", proNumb);

            jsonParam.put("Site", siteNumb);

            jsonParam.put("Deployment", deployNumb);

            jsonParam.put("Component", componentNumb);

            jsonParam.put("Service Entry", serviceNumb);

            return jsonParam;
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
       switch (view.getId()){
           case(R.id.document_project):
               if (position > 0)
                   proNumb = getInfo.projectNumber[position - 1];
               break;

           case(R.id.document_site):
               if (position > 0)
                   siteNumb = getInfo.siteNumber[position - 1];
               break;

           case (R.id.document_deployment):
               if (position > 0)
                   deployNumb = getInfo.deploymentNumber[position - 1];
               break;

           case (R.id.document_component):
               if (position > 0)
                   componentNumb = getInfo.componentNumber[position - 1];
               break;

           case (R.id.document_service):
               if (position > 0)
                   serviceNumb = getInfo.serviceNumber[position - 1];
               break;
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    // automatically generated
    }
}
