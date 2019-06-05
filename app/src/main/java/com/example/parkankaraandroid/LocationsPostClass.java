package com.example.parkankaraandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LocationsPostClass extends ArrayAdapter<String> {

    private final ArrayList<String> cpName;
    private final ArrayList<String> cpAddress;
    private final ArrayList<String> cpCondition;
    private final Activity context;
    SharedPreferences sharedPreferences;
    Set<String> favorites;


    /**
     *
     * @param cpName name of all the car parks in the database
     * @param cpAddress address of all the car parks in the database
     * @param cpCondition condition of all the car parks in the database
     * @param context context of the  parent activity
     */
    public LocationsPostClass(ArrayList<String> cpName, ArrayList<String> cpAddress, ArrayList<String> cpCondition, Activity context) {
        super(context, R.layout.locations_post_class, cpName);
        this.cpName = cpName;
        this.cpAddress = cpAddress;
        this.cpCondition = cpCondition;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);
        //sharedPreferences = context.getPreferences(Context.MODE_PRIVATE);
        favorites = new HashSet<>( sharedPreferences.getStringSet("CarParkName", new HashSet<String>()) );
    }

    /**
     *
     * @param position current index of  all arrays
     * @param convertView the old View to use if possible
     * @param parent The parent that this view will be  attached to
     * @return View corresponding to the data at the specified location
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View carParkInfo = layoutInflater.inflate(R.layout.locations_post_class, null, true);

        //Find components
        TextView cpNameText = carParkInfo.findViewById(R.id.cpName);
        TextView locationText = carParkInfo.findViewById(R.id.address);
        TextView conditionText = carParkInfo.findViewById(R.id.condition);
        Button favButton = carParkInfo.findViewById(R.id.favoriteButton);

        //Adding click listener to the button
        favButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v The view that was clicked
             */
            @Override
            public void onClick(View v) {
                favorites.add(cpName.get(position));
                //sharedPreferences.edit().putStringSet("CarParkName", favorites ).apply();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("CarParkName", favorites).apply();
                editor.commit();
                Toast.makeText(context, cpName.get(position)  + " added to favorites", Toast.LENGTH_LONG).show();
                System.out.println(cpName.get(position));
            }
        });

        //set method  of components
        cpNameText.setText(cpName.get(position));
        locationText.setText(cpAddress.get(position));

        //Checks if the car park is full and then writes the appropriate text to TextView
        if(Integer.parseInt( cpCondition.get(position) ) <= 0)
        {
            conditionText.setText("Park yeri dolu");
        }
        else {
            conditionText.setText(cpCondition.get(position) + "tane boş yer mevcut");
        }

        return carParkInfo;
    }


}