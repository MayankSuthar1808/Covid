package com.example.hp.covid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView statusTextView;

    public void onClick(View view) {

        Intent intent = new Intent(getApplicationContext(),WebView.class);
        startActivity(intent);
    }


    public class DownloadTask extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url= new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data= reader.read();

                }

                return result;

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);

                String activeCases = jsonObject.getString("active");
                String confirmedCases = jsonObject.getString("confirmed");
                String recoveredCases = jsonObject.getString("recovered");
                String deaths = jsonObject.getString("deaths");

                //Log.i("status", activeCases);


                statusTextView.setText("Confirmed Cases : " +confirmedCases + "\r\n"  + "Active Cases : " + activeCases + "\r\n" + "Recovered Cases : " + recoveredCases + "\r\n" + "Deaths : " + deaths);

                /*

                JSONArray arr = new JSONArray(activeCases);

                String status = "";


                for(int i= 0 ; i< arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String summary= jsonPart.getString("active");
                    if (!summary.equals("")) {
                        status += active;
                    }

                    //statusTextView.setText(status);

                    Log.i("status", status);



                }
                */
                /*

                Object obj = new JSONParser().parse(new FileReader("data.json"));
                JSONObject jo = (JSONObject) obj;
                JSONObject internal = (JSONObject)jo.get("data");
                JSONObject summary = (JSONObject)internal.get("summary");
                System.out.print((long)summary.get("total"));
                */

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);

        DownloadTask task = new DownloadTask();
        task.execute("https://api.covidindiatracker.com/total.json");


    }
}
