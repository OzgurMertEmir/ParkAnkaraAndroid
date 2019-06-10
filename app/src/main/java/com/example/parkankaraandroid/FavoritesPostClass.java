package com.example.parkankaraandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class FavoritesPostClass extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> cpName;
    private final ArrayList<String> cpAddress;
    private final ArrayList<String> cpCondition;
    Set<String> favorites;
    SharedPreferences sharedPreferences;
    ToggleButton toggleButton;

    /**
     *
     * @param cpName name of all the favorited  car parks
     * @param cpAddress address of all the favorited  car parks
     * @param cpCondition condition of all the favorited  car parks
     * @param context context of the parent activity
     */

    public FavoritesPostClass(ArrayList<String> cpName, ArrayList<String> cpAddress, ArrayList<String> cpCondition, Activity context) {
        super(context, R.layout.favorites_post_class, cpName);
        this.context = context;
        this.cpName = cpName;
        this.cpAddress = cpAddress;
        this.cpCondition = cpCondition;
        sharedPreferences = context.getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);

        //sharedPreferences = context.getPreferences(Context.MODE_PRIVATE);
        favorites = sharedPreferences.getStringSet("CarParkName", new HashSet<String>());
    }

    /**
     * @param position current index of  all arrays
     * @param convertView the old View to use if possible
     * @param parent The parent that this view will be  attached to
     * @return View corresponding to the data at the specified location
     */

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View favoriteCarParkInfo = layoutInflater.inflate(R.layout.favorites_post_class, null, true);

        //Find components
        TextView cpNameText = favoriteCarParkInfo.findViewById(R.id.cpName);
        TextView addressText = favoriteCarParkInfo.findViewById(R.id.address);
        TextView conditionText = favoriteCarParkInfo.findViewById(R.id.condition);
        toggleButton =  favoriteCarParkInfo.findViewById(R.id.favoriteButton);

         //Adding click listener to the button
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (!favorites.contains(cpName.get(position))) {
                    favorites.add(cpName.get(position));
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.commit();
                    Toast.makeText(context, cpName.get(position) + " added to favorites", Toast.LENGTH_LONG).show();
                }
                else {
                    favorites.remove(cpName.get(position));
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.commit();
                    Toast.makeText(context, cpName.get(position) + " removed from favorites", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(context, FavoriteCarParks.class);
                context.startActivity(intent);
            }
        });

        //set method  of components
        cpNameText.setText( cpName.get(position) );
        addressText.setText( cpAddress.get(position) );

        //Checks if the car park is full and then writes the appropriate text to TextView
        if(Integer.parseInt( cpCondition.get(position) ) <= 0)
        {
            conditionText.setText("Car Park Full!!");
        }
        else {
            conditionText.setText("Empty Space: " + cpCondition.get(position));
        }

        return favoriteCarParkInfo;
    }
}