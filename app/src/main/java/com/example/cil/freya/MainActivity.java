package com.example.cil.freya;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener {

    Button create, read, update, delete;
    TextView createText;
    JsonReader test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = (Button) findViewById(R.id.create);
        read = (Button) findViewById(R.id.read);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        createText = (TextView) findViewById(R.id.editText);

        create.setOnClickListener(this);
        read.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case (R.id.create):
                writeMessage();
                createText.setText("create");
                break;

            case (R.id.read):
                new readMessage().execute();
                createText.setText("read");
                break;
            case (R.id.delete):
                new deleteMessage().execute();
                createText.setText("delete");
                break;
            case (R.id.update):
                createText.setText("update");
                break;
        }
    }

    public void writeMessage() {
/*        String sb = "";
        URL url = null;
        int one = 1;
        HttpURLConnection urlConnection = null;

        String test = "http://sensor.nevada.edu/GS/Services/people/";
        try {
            url = new URL(test);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("Creation Date", "Tue, 14 Jul 2015 20:03:03 GMT");
            jsonParam.put("Email", "test@email.com");
            jsonParam.put("First Name", "Matt");
            jsonParam.put("Last Name", "Salivar");
            jsonParam.put("Modification Date", "Tue, 14 Jul 2015 20:03:03 GMT");
            jsonParam.put("Organization", "UNR");
            jsonParam.put("Person", one);
            jsonParam.put("Phone", "(775)313-7829");
            jsonParam.put("Photo", null);
            jsonParam.put("Unique Identifier", "0E984725-C51C-4BF4-9960-E1C80E27ABB7");

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonParam.toString());
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb += (line + "\n");
                }
                br.close();

                System.out.println("" + sb);

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {

            e.printStackTrace();

        } finally{

            if(urlConnection != null)
                urlConnection.disconnect();
        }*/
    }

    public class readMessage extends AsyncTask<String, Void, URL> {

        @Override
        protected URL doInBackground(String... params) {

            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader bufferedReader = null;
            JsonReader jsonReader = null;
            URL url = null;
            try {

                url = new URL("http://sensor.nevada.edu/GS/Services/people");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);

                connection.setRequestMethod("GET");

            //CURRENTLY WORKING ON THIS. HOW DOES THE INPUT STREAM WORK?
                is = new InputStream(url);
                jsonReader = new JsonReader(is);
                createJSON(jsonReader); //CHECKING TO SEE IF THIS WORKS, NOT FULLY IMPLEMENTED
            //BELOW THIS LINE WORKS

                // this section gets the header and outputs it to a log
                Map<String, List<String>> map = connection.getHeaderFields();

                System.out.println("Printing All Response Header for URL: "
                        + url.toString() + "\n");

                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }

                System.out.println("\nGet Response Header By Key ...\n");
                List<String> contentLength = map.get("Content-Length");

                if (contentLength == null) {
                    System.out
                            .println("'Content-Length' doesn't present in Header!");
                } else {
                    for (String header : contentLength) {
                        System.out.println("Content-Length: " + header);
                    }
                }


                // end section

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return url;
            }
        }

        protected void onPostExecute(URL url) {
            System.out.println(url);
        }
    }

    public class deleteMessage extends AsyncTask<String, Void, URL> {
        @Override
        protected URL doInBackground(String... params) {
            // things done while execution in progress go here
            URL url = null;
            try {
                url = new URL("http://sensor.nevada.edu/GS/Services/people");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);

                connection.setRequestMethod("GET");

                // new readMessage().execute();     // find correct JSON

                // do work checking if readMessage returned anything

                connection.disconnect();

                connection.setRequestMethod("DELETE");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // do deletion stuff
                    return url; // temp
                } else {
                    System.out.println(connection.getResponseMessage());
                }

                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // onPostExecute
            // onPreExecute
            // onProgressUpdate

            return url;
        }
    }

    public class updateMessage extends AsyncTask<String, Void, URL> {
        @Override
        protected URL doInBackground(String... params) {
            // things done while execution in progress go here
            URL url = null;
            try {
                url = new URL("http://sensor.nevada.edu/GS/Services/people");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);

                connection.setRequestMethod("UPDATE");

                // update here


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    return url; // temp
                } else {
                    System.out.println(connection.getResponseMessage());
                }

                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // onPostExecute
            // onPreExecute
            // onProgressUpdate

            return url;
        }
    }


    public void createJSON(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();  //begins the new json object
        while (jsonReader.hasNext()) { //while there is another object avaible (People)
            String objectName = jsonReader.nextName(); // grabs the object name
            if (jsonReader.peek() == JsonToken.NULL) { // if there is an array availible
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    String creationData = jsonReader.nextName();
                    System.out.println(creationData);

                }
            }
        }

    }
}




// written by stack Overflow user Roland Ilig. fallback for if JSONReader doesn't work.
// http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java

/*public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}*/








