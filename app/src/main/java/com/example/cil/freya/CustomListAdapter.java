package com.example.cil.freya;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter
{
    // Declare variables
    private ArrayList<ProjectEntry> projectList;
    private ArrayList<Boolean> mHideValues;
    Context mContext;

    // Create custom adapter for project entries
    public CustomListAdapter(Context context, int textViewResourceId, ArrayList<ProjectEntry> resource, ArrayList<Boolean> hideValues)
    {
        super(context, textViewResourceId, resource);
        this.projectList = new ArrayList<>();
        this.projectList.addAll(resource);
        mContext = context;
        mHideValues = hideValues;
    }

    // Display Projects  in a list with checkboxes
    // Enable the ability to check the boxes
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ProjectEntry entry;
        if (convertView == null)
        {
            LayoutInflater vi = ((Activity)mContext).getLayoutInflater();
            convertView = vi.inflate(R.layout.row, parent, false);

            entry = new ProjectEntry();
            entry.name = (TextView) convertView.findViewById(R.id.textView1);
            entry.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(entry);
        }
        else
        {
            entry = (ProjectEntry) convertView.getTag();
        }

        entry.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (entry.checked.isChecked())
                {
                    mHideValues.set(position, true);
                }
                else
                {
                    mHideValues.set(position, false);
                }
            }
        });

        ProjectEntry temp_entry = projectList.get(position);
        entry.name.setText(temp_entry.text);
        entry.checked.setChecked(temp_entry.value);
        entry.checked.setTag(temp_entry);

        return convertView;
    }
}
