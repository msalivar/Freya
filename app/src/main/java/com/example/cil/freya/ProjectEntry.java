package com.example.cil.freya;

public class ProjectEntry
{
    String name;
    boolean checked;

    public ProjectEntry(String name, boolean value)
    {
        this.name = name;
        this.checked = value;
    }

    public String getName()
    {
        return this.name;
    }
    public boolean getValue()
    {
        return this.checked;
    }
}
