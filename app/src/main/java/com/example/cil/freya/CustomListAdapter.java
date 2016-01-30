package com.example.cil.freya;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter
{
    // Declare variables
    private ArrayList<ProjectEntry> projectList;
    Context context;
    
    // Create custom adapter for project entries
    public CustomListAdapter(Context context, ArrayList<ProjectEntry> resource)
    {
        super(context, R.layout.row, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.projectList = new ArrayList<ProjectEntry>();
        this.projectList.addAll(resource);
    }

    // Display Projects  in a list with checkboxes
    // Enable the ability to check the boxes
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ProjectEntry entry = null;
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.row, parent, false);
            entry = new ProjectEntry();
            entry.name = (TextView) convertView.findViewById(R.id.textView1);
            entry.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(entry);
            entry.checked.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    ProjectEntry this_entry = (ProjectEntry) cb.getTag();
                    if (cb.isChecked())
                    {
                        this_entry.checked.setChecked(true);
                    } else
                    {
                        this_entry.checked.setChecked(false);
                    }
                }
            });
        }
        else
        {
            entry = (ProjectEntry) convertView.getTag();
        }

        ProjectEntry temp_entry = projectList.get(position);
        entry.name.setText(temp_entry.getName());
        entry.checked.setChecked(temp_entry.getValue());
        entry.checked.setTag(temp_entry);

        return convertView;
    }
}
