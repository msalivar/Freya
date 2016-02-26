package com.example.cil.freya;

import android.widget.CheckBox;
import android.widget.TextView;

public class ProjectEntry
{
    // Declare variables
    TextView name;
    CheckBox checked;
    String text;
    Boolean value;

    // Constructor
    public ProjectEntry()
    {
        text = "ph";
        value = true;
    }

    // Get the projected name
    public String getName()
    {
        return text;
    }

    // See if the option is checked on the screen
    public boolean getValue()
    {
        return checked.isChecked();
    }
}