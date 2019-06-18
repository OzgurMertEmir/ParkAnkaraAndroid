package com.example.parkankaraandroid;
import android.os.AsyncTask;
import android.util.Log;

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

import static android.support.constraint.Constraints.TAG;


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

        //checkDatabase();

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
                Log.d(TAG, "doInBackground: Exception with DoInBackground");
                System.out.println(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println("FLAG: ACCESSED TO DATA");

            try {

                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: ENTERED THE TRY STATEMENT");

                Iterator<String> keys = jsonObject.keys();


                while(keys.hasNext()){
                    String key = keys.next();
                    HashMap<String, String> hashMap = new HashMap<>();
                    String values = jsonObject.getString(key);
                    System.out.println(values);

                    JSONObject jsonObject1 = new JSONObject(values);
                    hashMap.put("name", jsonObject1.get("name").toString());
                    hashMap.put("currentCars",jsonObject1.get("currentCars").toString());
                    hashMap.put("fullCapacity",jsonObject1.get("fullCapacity").toString());
                    hashMap.put("latitude",jsonObject1.get("latitude").toString());
                    hashMap.put("longitude",jsonObject1.get("longitude").toString());
                    hashMap.put("address",jsonObject1.get("address").toString());

                    CarPark carPark = new CarPark(hashMap);
                    carParks.add(carPark);

                }
            }catch (Exception e){
                System.out.println(e);
            }

            System.out.println(s);
        }
    }
}