package com.example.cil.freya;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by cil on 2/11/16.
 */
public class Modules
{
    static public void write(EditText info,String fileName, Context ctx) throws FileNotFoundException {
        try {
            FileOutputStream FileOut = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
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

    static public void spinner (Context cxt, String names[], Spinner proj){

        try
        {
            ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(cxt, R.layout.spinner_item, names);
            spinAdapter.setDropDownViewResource(R.layout.spinner_item);
            proj.setAdapter(spinAdapter);
            proj.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) cxt);
        }catch (NullPointerException e){
            Toast.makeText(cxt, "Unable to populate lists. Sync before trying again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    static String findEntry(String title)
    {
        String name = null;

        String titleNames[] = {"Projects", "Sites", "Systems", "Deployments", "Components", "Documents", "Service Entries"};
        for (int j = 0; j < titleNames.length; j++){
            try
            {
                for(int i = 0; i < getInfo.unsynced.getJSONArray(titleNames[j]).length(); i++)
                   {
                       JSONObject inside = getInfo.unsynced.getJSONArray(titleNames[j]).getJSONObject(i);
                       String find = inside.getString("Name");
                       if (find == title){
                           name = titleNames[j];
                        }
                   }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return name;
    }


//    static public void startDate(Context cxt){
//        Calendar cal = Calendar.getInstance();
//        return new DatePickerDialog( getActivity(),cxt,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get (Calendar.DAY_OF_MONTH));
//    }

}
