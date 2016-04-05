package com.example.cil.freya;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by cil on 3/31/16.
 */
public class FileHandler
{

    static String filename = "backup";
    static int writeAccepted,  readAccepted;

   static public void permCheck (){
        if (ContextCompat.checkSelfPermission(StaticContextHandler.getAppContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {ActivityCompat.requestPermissions((Activity) StaticContextHandler.getAppContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, writeAccepted);
        }
    }

    //TODO: get to work
     public static void writeToFile(String write) throws FileNotFoundException
    {
        permCheck();
        try {
            FileOutputStream FileOut = StaticContextHandler.getAppContext().openFileOutput(filename, StaticContextHandler.getAppContext().MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(FileOut);
            outputWriter.write(write);
            outputWriter.close();
        } catch (Exception e) { e.printStackTrace();}
    }


    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults)
    {
        int id;
        switch(permsRequestCode)
        {
            case 200:
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){

                }
                //else
                break;
        }
    }
}
