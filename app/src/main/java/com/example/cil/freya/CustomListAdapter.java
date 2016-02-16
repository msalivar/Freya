package com.example.cil.freya;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CustomListAdapter extends ArrayAdapter
{
    public CustomListAdapter(Context context, int resource)
    {
        super(context, resource);
    }
//    // Declare variables
//    private ArrayList<ProjectEntry> projectList;
//    private ArrayList<Boolean> mHideValues;
//    Context mContext;
//
//    // Create custom adapter for project entries
//    public CustomListAdapter(Context context, int textViewResourceId, ArrayList<ProjectEntry> resource, ArrayList<Boolean> hideValues)
//    {
//        super(context, textViewResourceId, resource);
//        this.projectList = new ArrayList<>();
//        this.projectList.addAll(resource);
//        mContext = context;
//        mHideValues = hideValues;
//    }
//
//    // Display Projects  in a list with checkboxes
//    // Enable the ability to check the boxes
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent)
//    {
//        final ProjectEntry entry;
//        if (convertView == null)
//        {
//            LayoutInflater vi = ((Activity)mContext).getLayoutInflater();
//            convertView = vi.inflate(R.layout.row, parent, false);
//
//            entry = new ProjectEntry();
//            entry.name = (TextView) convertView.findViewById(R.id.textView1);
//            entry.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);
//            entry.checked.setChecked(mHideValues.get(position));
//            convertView.setTag(entry);
//        }
//        else
//        {
//            entry = (ProjectEntry) convertView.getTag();
//        }
//
//        entry.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
//            {
//                if (entry.checked.isChecked())
//                {
//                    mHideValues.set(position, true);
//                    entry.value = true;
//                }
//                else
//                {
//                    mHideValues.set(position, false);
//                    entry.value = false;
//                }
//            }
//        });
//
//        ProjectEntry temp_entry = projectList.get(position);
//        //Log.d("Entry Value: ", String.valueOf(entry.checked.isChecked()));
//        entry.name.setText(temp_entry.text);
//        entry.checked.setChecked(temp_entry.value);
//        entry.checked.setTag(temp_entry);
//
//        return convertView;
//    }
}