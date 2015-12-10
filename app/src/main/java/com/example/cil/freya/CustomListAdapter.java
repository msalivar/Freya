package com.example.cil.freya;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter
{
    // Declare variables
    ProjectEntry[] items = null;
    Context context;
    
    // Create custom adapter for project entries
    public CustomListAdapter(Context context, ProjectEntry[] resource)
    {
        super(context, R.layout.row, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.items = resource;
    }

    // Display Projects  in a list with checkboxes
    // Enable the ability to check the boxes
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        name.setText(items[position].getName());
        if(items[position].getValue())
        cb.setChecked(true);
        else
        cb.setChecked(false);
        return convertView;
    }
}
