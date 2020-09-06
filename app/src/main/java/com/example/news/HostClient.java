package com.example.news;

import android.os.AsyncTask;
import android.util.Log;

import com.example.news.ui.home.HomeFragment;
import com.loopj.android.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HostClient extends AsyncTask<Void, Void, Void> {
    private String TAG = "HostClient";
    String url2 = "https://covid-dashboard.aminer.cn/api/event/5f05f3f69fced0a24b2f84ee";
    String data;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // JSON Data Fetching
            Log.d("CONNECTION", "connecting");
            URL url = new URL("ur2");
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setConnectTimeout(5000);
            httpUrlConnection.setReadTimeout(10_1000);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("Accept-Charset", "utf-8");
            if (httpUrlConnection.getResponseCode() != 200) {
                Log.d("CONNECTION", "Error Connection");
                throw new RuntimeException("Failed : HTTP error code : " + httpUrlConnection.getResponseCode());
            }
            Log.d("CONNECTION", "Connection Successful");
            // Reading JSON Data File
            InputStream inputStream = httpUrlConnection.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferReader.readLine();
            data = line;
            System.out.println(line.length());
            int Count = 0;
//            while(line != null) {
//                line = bufferReader.readLine();
//                data = data + line;
//                Count++;
//                System.out.println(line);
//                //if (Count >= 100) break;
//            }
            httpUrlConnection.disconnect();
            inputStream.close();
        }
        catch (MalformedURLException e){
            Log.d("GET", "cannot connect to url bc " + e.toString() );
        } catch (IOException e) {
            Log.d("GET", "cannot connect to url bc " + e.toString() );
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //HomeFragment.data = this.data;
        //HomeFragment.textView.setText("onPostExecute");
        HomeActivity.data.setText(data);
    }
}
