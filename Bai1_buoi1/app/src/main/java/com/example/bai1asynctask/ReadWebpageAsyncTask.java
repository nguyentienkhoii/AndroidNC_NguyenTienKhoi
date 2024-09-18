package com.example.bai1asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadWebpageAsyncTask extends Activity {

    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.TextView01);
    }

    public void readWebpage(View view) {
        new DownloadWebPageTask().execute("https://www.youtube.com/");
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL urlObject = new URL(url);
                    connection = (HttpURLConnection) urlObject.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    response = result.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }
}
