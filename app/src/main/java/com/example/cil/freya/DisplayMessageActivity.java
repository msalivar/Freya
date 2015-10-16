package com.example.cil.freya;

import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get the message from the intent
        String[] people = getIntent().getStringArrayExtra(MainActivity.JSON_TEXT);
        String message = "";
        for (String str : people)
        {
            message += str + "\n";
        }

        // Create the text view
        TextView jsonText = new TextView(this);
        jsonText.setText(message);

        // Set the text view as the activity layout
        setContentView(jsonText);
    }

}
