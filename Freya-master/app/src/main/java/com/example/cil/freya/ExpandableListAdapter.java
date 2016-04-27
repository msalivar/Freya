package com.example.cil.freya;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, final int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.search_list_view, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        final Button hideButton = (Button) convertView.findViewById(R.id.hideButton);

        // TODO: This doesn't work... button sometimes doesn't click on first try
        /*hideButton.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    v.performClick();
                }
            }
        });*/

        if (Objects.equals(MainActivity.ListHandler.listDataHeader.get(groupPosition), "Unsynced")
                || Objects.equals(MainActivity.ListHandler.listDataHeader.get(groupPosition), "People")||
                Objects.equals(MainActivity.ListHandler.listDataHeader.get(groupPosition), "Deleted"))
        {
            hideButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            hideButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    boolean result;
                    if (hideButton.getText().equals("Enable"))
                    {
                        hideButton.setText("Disable");
                        hideButton.setBackgroundColor(ContextCompat.getColor(_context, R.color.text_color));
                        result = false;
                    } else
                    {
                        hideButton.setText("Enable");
                        hideButton.setBackgroundColor(ContextCompat.getColor(_context, R.color.button_color));
                        result = true;
                    }
                    switch (MainActivity.ListHandler.listDataHeader.get(groupPosition))
                    {
                        case "Projects":
                            MainActivity.ListHandler.toggleProject(childPosition, result);
                            break;
                        case "Systems":
                            MainActivity.ListHandler.toggleSystem(childPosition, result);
                            break;
                        case "Components":
                            break;
                        case "Service Entries":
                            break;
                        case "Deployments":
                            MainActivity.ListHandler.toggleDeployment(childPosition, result);
                            break;
                        case "Sites":
                            MainActivity.ListHandler.toggleSite(childPosition, result);
                            break;
                        case "Documents":
                            break;
                    }
                    //MainActivity.expListView.setAdapter(MainActivity.ListHandler.prepareListData(MainActivity.getContext()));
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.main_list_view, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
