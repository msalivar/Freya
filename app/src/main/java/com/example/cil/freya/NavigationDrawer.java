package com.example.cil.freya;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by sammie on 4/14/16.
 */
public class NavigationDrawer extends Activity{
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;

    protected void onCreateDrawer(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0)
        {
            public void onDrawerClosed(View view) { super.onDrawerClosed(view); }
            public void onDrawerOpened(View drawerView) { super.onDrawerOpened(drawerView); }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        addDrawerItems();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        // put in icons from drawables
        actionBar.setIcon(R.drawable.upload_icon);
        actionBar.setIcon(R.drawable.search_icon);

    }


    // when creating a drawer
    private void addDrawerItems()
    {
        // list of options
        String[] osArray = { "Create New Project","Create New Site", "Create New System", "Create New Deployment", "Create New Component", "Create New Document" , "Create New Service Entry"};
        //  display and set listener
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, R.layout.navigation_drawer, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    // once GUI  is created
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        // will do stuff once saved states are enabled
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    // drawer listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        // got to onItemClick
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) { selectItem(position, view); }
    }

    // if an item is selected from the drawer
    public void selectItem(int position, View view)
    {
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image*//*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);*/


        Intent intent;
        switch (position) {
            case 0:
                // start NewSite intent
                intent = new Intent(view.getContext(), CreateNewProject.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case 1:
                // start NewSite intent
                intent = new Intent(NavigationDrawer.this, CreateNewSite.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case 2:
                // start NewSite intent
                intent = new Intent(NavigationDrawer.this, CreateNewSystem.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case 3:
                // start NewSite intent
                intent = new Intent(NavigationDrawer.this, CreateNewDeployment.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case 4:
                // start component intent
                intent = new Intent(NavigationDrawer.this, CreateNewComponent.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case 5:
                // start document intent
                intent = new Intent(NavigationDrawer.this, CreateNewDocument.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;

            case 6:
                // start document intent
                intent = new Intent(NavigationDrawer.this, CreateNewServiceEntry.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;

            default:
                // close the drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
        }
    }


}
