package com.example.parkankaraandroid;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    //constants
    private final static String TAG = "DataAccess";

    //properties
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    static boolean firstTimeOpened = true;
    private Timer timer;
    private TimerTask timerTask;
    DownloadData downloadData;
    String url;
    private ArrayList<CarPark> carParks;

    public DataAccess() {
        downloadData = new DownloadData();
        //url = "http://parkankara.developerplatforms.com/api/park";

        url = "https://parkankara-59ec4.firebaseio.com/CarPark.json";
        carParks = new ArrayList<CarPark>();

        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {

                try {
                    DownloadData downloadData = new DownloadData();
                    downloadData.execute(url);

                } catch (Exception e){
                    e.printStackTrace();
                }

                //printArrayList();
            }
        };
        timer.schedule(timerTask, 0,10000);

    }


    public void printArrayList()
    {
        for( CarPark cp : carParks){
            Log.d(TAG, "printCarParks: " + cp.getName());
        }
    }

    public ArrayList<CarPark> getCarParks(){
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
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //carParks.clear();

            System.out.println("FLAG: ACCESSED TO DATA");

            try {

                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: ENTERED THE TRY STATEMENT");

                Iterator<String> keys = jsonObject.keys();

                if(firstTimeOpened) {
                    while (keys.hasNext()) {
                        String key = keys.next();
                        HashMap<String, String> hashMap = new HashMap<>();
                        String values = jsonObject.getString(key);
                        Log.d(TAG, "onPostExecute: FIRST TIME ENTERED" + values);

                        JSONObject jsonObject1 = new JSONObject(values);
                        hashMap.put("name", jsonObject1.get("name").toString());
                        hashMap.put("currentCars", jsonObject1.get("currentCars").toString());
                        hashMap.put("fullCapacity", jsonObject1.get("fullCapacity").toString());
                        hashMap.put("latitude", jsonObject1.get("latitude").toString());
                        hashMap.put("longitude", jsonObject1.get("longitude").toString());
                        hashMap.put("address", jsonObject1.get("address").toString());

                        CarPark carPark = new CarPark(hashMap);
                        carPark.addPropertyChangeListener(new PropertyChangeListener() {
                            @Override
                            public void propertyChange(PropertyChangeEvent evt) {
                                Log.d(TAG, "propertyChange: " + evt.getPropertyName() + " Old Value: " +evt.getOldValue() + "New Value: " + evt.getNewValue());
                                pcs.firePropertyChange(evt.toString(), evt.getOldValue(), evt.getNewValue());
                            }
                        });
                        carParks.add(carPark);

                    }
                    firstTimeOpened = false;
                }else{
                    int i = 0;
                    while(keys.hasNext()){
                        String key = keys.next();
                        String values = jsonObject.getString(key);
                        Log.d(TAG, "onPostExecute: NOT THE FIRST TIME ENTERED" + values);

                        JSONObject jsonObject1 = new JSONObject(values);
                        //int oldValue = carParks.get(i).getEmptySpace();
                        //String name = carParks.get(i).toString();
                        carParks.get(i).setEmptySpace( Integer.parseInt( jsonObject1.get("fullCapacity").toString() ) - Integer.parseInt( jsonObject1.get("currentCars").toString() ) );
                        //int newValue = carParks.get(i).getEmptySpace();
                        //pcs.firePropertyChange(name, oldValue, newValue);
                        i++;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.println(s);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        this.pcs.addPropertyChangeListener(listener);
    }
}