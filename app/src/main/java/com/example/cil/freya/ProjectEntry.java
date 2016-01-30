package com.example.cil.freya;

import android.widget.CheckBox;
import android.widget.TextView;

public class ProjectEntry
{
    // Declare variables
    TextView name;
    CheckBox checked;

    // Constructor
    public ProjectEntry()
    {
    }

    // Get the projected name
    public String getName()
    {
        return name.toString();
    }
    
    // See if the option is checked on the screen
    public boolean getValue()
    {
        return checked.isChecked();
    }
}
