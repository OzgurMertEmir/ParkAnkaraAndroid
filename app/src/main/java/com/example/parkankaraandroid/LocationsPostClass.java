package com.example.parkankaraandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LocationsPostClass extends ArrayAdapter<String> {
    //

    private final ArrayList<String> cpName;
    private final ArrayList<String> cpAddress;
    private final ArrayList<String> cpCondition;
    private final Activity context;
    private final ArrayList<Boolean> favourited;
    static SharedPreferences sharedPreferences;
    Set<String> favorites;
    //ToggleButton toggleButton;

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
        favourited = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);
        favorites = new HashSet<>( sharedPreferences.getStringSet("CarParkName", new HashSet<String>()) );
        for(String name : cpName){
            if(favorites.contains(name)){
                favourited.add(true);
            }else{
                favourited.add(false);
            }
        }
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
        final ToggleButton toggleButton = carParkInfo.findViewById(R.id.favoriteButton);

        //set method  of components
        cpNameText.setText(cpName.get(position));
        locationText.setText(cpAddress.get(position));
        toggleButton.setChecked(favourited.get(position));


        toggleButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            @Override
            public void onClick(View v) {
                if(favourited.get(position)){
                    favorites.remove(cpName.get(position));
                    toggleButton.setChecked(false);
                    favourited.set(position, false);
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.commit();
                    Toast.makeText(context, cpName.get(position) + " Favori otoparklardan çıkarıldı!", Toast.LENGTH_LONG).show();
                }else{
                    favorites.add(cpName.get(position));
                    toggleButton.setChecked(true);
                    favourited.set(position, true);
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.commit();
                    Toast.makeText(context, cpName.get(position) + " Favori otoparklara eklendı!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Checks if the car park is full and then writes the appropriate text to TextView
        if(Integer.parseInt( cpCondition.get(position) ) <= 0)
        {
            conditionText.setText("Park yeri dolu");
        }
        else {
            conditionText.setText(cpCondition.get(position) + " tane boş yer mevcut");
        }
        return carParkInfo;
    }


}