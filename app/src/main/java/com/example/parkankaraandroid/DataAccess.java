package com.example.parkankaraandroid;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;



public class DataAccess {
    //
    private Timer timer;
    private TimerTask timerTask;
    DownloadData downloadData;
    String url;
    private ArrayList<CarPark> carParks;

    public DataAccess() {

        downloadData = new DownloadData();
        url = "http://parkankara.developerplatforms.com/api/park";

        carParks = new ArrayList<CarPark>();

        downloadData.execute(url);

        checkDatabase();

    }



    public void checkDatabase(){
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {

                try {
                    JSONObject jsonObject = new JSONObject(downloadData.doInBackground());
                    Iterator<String> keys = jsonObject.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        HashMap<String, String> hashMap = (HashMap<String, String>) jsonObject.get(key);
                        CarPark carPark = new CarPark(hashMap);
                        carParks.add(carPark);

                    }
                }catch (Exception e){

                }

                printArrayList();
            }
        };
        timer.schedule(timerTask,10000);
    }


    public void printArrayList()
    {
        for( CarPark cp : carParks){
            System.out.println(cp.getName());
        }
    }

    public ArrayList getCarParks(){
        return carParks;
    }


    private class DownloadData extends AsyncTask<String,Void,String >{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data > 0){

                    char charachter = (char) data;
                    result += charachter;

                    data = inputStreamReader.read();
                }

                return result;
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println("FLAG: ACCESSED TO DATA");

            try {

                JSONObject jsonObject = new JSONObject(s);
                Iterator<String> keys = jsonObject.keys();

                while(keys.hasNext()){
                    String key = keys.next();
                    HashMap<String, String> hashMap = (HashMap<String, String>) jsonObject.get(key);
                    CarPark carPark = new CarPark(hashMap);
                    carParks.add(carPark);

                }
            }catch (Exception e){

            }
        }
    }
}