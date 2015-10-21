package com.example.cil.freya;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayMessageActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        String[] people = getIntent().getStringArrayExtra(MainActivity.JSON_TEXT);
        String message = "";

        try
        {
            for(int j = 0; j < people.length; j++)
            {
                JSONObject obj = new JSONObject(people[j]);

                message += "Creation Date: " + obj.getString("Creation Date") + "\n";
                message += "Email: " + obj.getString("Email") + "\n";
                message += "First Name: " + obj.getString("First Name") + "\n";
                message += "Last Name: " + obj.getString("Last Name") + "\n";
                message += "Modification Date: " + obj.getString("Modification Date") + "\n";
                message += "Organization: " + obj.getString("Organization") + "\n";
                message += "Phone: " + obj.getString("Phone") + "\n";
                message += "\n";

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        // Create the text view
        TextView jsonText = new TextView(this);
        jsonText.setText(message);

        // Set the text view as the activity layout
        setContentView(jsonText);
    }

}
