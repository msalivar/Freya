package com.example.cil.freya;

import android.content.Context;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by cil on 2/11/16.
 */
public class components
{
<<<<<<< HEAD
        static public void write(EditText info,String fileName, Context ctx) throws FileNotFoundException {
=======
    static public void write(EditText info,String fileName, Context ctx) throws FileNotFoundException {
>>>>>>> 4c1e3c4c41db9d96e966f57846c6bcf25b194adb
        try {
            FileOutputStream FileOut = ctx.openFileOutput(fileName, ctx.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(FileOut);
            outputWriter.write(info.getText().toString());
            outputWriter.close();
        } catch (Exception e) { e.printStackTrace();}
    }

    static public void read(String fileName, Context ctx) throws FileNotFoundException {
        try {
            FileInputStream FileIn = ctx.openFileInput(fileName);
            InputStreamReader InputRead= new InputStreamReader(FileIn);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String ReadString = String.copyValueOf(inputBuffer,0,charRead);
                s += ReadString;
            }
            InputRead.close();

        } catch (Exception e) {e.printStackTrace();}
    }
}
