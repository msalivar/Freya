package com.example.cil.freya;

public class ProjectEntry
{
    // Declare variables
    String name;
    boolean checked;

    // Constructor
    public ProjectEntry(String name, boolean value)
    {
        this.name = name;
        this.checked = value;
    }

    // Get the projected name
    public String getName()
    {
        return this.name;
    }
    
    // See if the option is checked on the screen
    public boolean getValue()
    {
        return this.checked;
    }
}
