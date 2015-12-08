package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SearchActivity extends Activity
{
    String[] types;
    Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        types = new String[8];
        types[0] = "People";
        types[1] = "Project";
        types[2] = "Site";
        types[3] = "System";
        types[4] = "Deployment";
        types[5] = "Component";
        types[6] = "Document";
        types[7] = "Service Entry";
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, types);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinAdapter);
    }
}
