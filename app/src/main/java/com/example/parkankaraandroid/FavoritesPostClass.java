package com.example.parkankaraandroid;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkankaraandroid.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesPostClass extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> cpName;
    private final ArrayList<String> cpAddress;
    private final ArrayList<String> cpCondition;
    Set<String> favorites;
    Set<String> newFavorites;
    SharedPreferences sharedPreferences;

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
        newFavorites = new HashSet<>();
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
        Button unfav = favoriteCarParkInfo.findViewById(R.id.favoriteButton);


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

        //Adding click listener to the button
        unfav.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v view that is  being clicked
             */
            @Override
            public void onClick(View v) {

                for(String s : favorites) {
                    if (!cpName.get(position).equals(s)) {
                        newFavorites.add(s);
                    }
                }

                //Changing the shared preferences and updating the page
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("CarParkName", newFavorites).apply();
                editor.commit();
                //sharedPreferences.edit().putStringSet("CarParkName", newFavorites ).apply();
                Toast.makeText(context, cpName.get(position)  + " removed from favorites", Toast.LENGTH_LONG).show();

                //refresh the page
                Intent intent = new Intent(context, FavoriteCarParks.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return favoriteCarParkInfo;
    }

}