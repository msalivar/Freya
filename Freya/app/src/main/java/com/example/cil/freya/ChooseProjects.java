package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;

// class to choose projects from
public class ChooseProjects extends MainActivity
{

    // when create, display projects list
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_projects);
    }
}
