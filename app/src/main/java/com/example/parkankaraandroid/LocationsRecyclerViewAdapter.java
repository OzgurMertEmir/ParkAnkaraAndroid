package com.example.parkankaraandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class LocationsRecyclerViewAdapter extends RecyclerView.Adapter<LocationsRecyclerViewAdapter.ParkHolder> {

    private ControllerMaster controllerMaster;
    private final ArrayList<String> cpName;
    private final ArrayList<String> cpAddress;
    private final ArrayList<String> cpCondition;
    private final ArrayList<Boolean> favourited;
    private static SharedPreferences sharedPreferences;
    private final Activity context;
    private Set<String> favorites;

    LocationsRecyclerViewAdapter(ArrayList<String> cpName, ArrayList<String> cpAddress, ArrayList<String> cpCondition, Activity context ) {
        this.cpName = cpName;
        this.cpAddress = cpAddress;
        this.cpCondition = cpCondition;
        favourited = new ArrayList<>();
        this.context = context;
        controllerMaster = new ControllerMaster();
    }

    @NonNull
    @Override
    public ParkHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate( R.layout.locations_recycler_row, viewGroup, false);
        sharedPreferences = viewGroup.getContext().getSharedPreferences("com.example.parkankara", Context.MODE_PRIVATE);
        favorites = new HashSet<>( sharedPreferences.getStringSet("CarParkName", new HashSet<String>()) );
        for(String name : cpName){
            if(favorites.contains(name)){
                favourited.add(true);
            }else{
                favourited.add(false);
            }
        }

        return new ParkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ParkHolder parkHolder, int i) {
        parkHolder.address.setText(cpAddress.get(i));
        parkHolder.condition.setText(cpCondition.get(i));
        parkHolder.cpName.setText(cpName.get(i));
        parkHolder.favoriteButton.setChecked(favourited.get(i));

        parkHolder.favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    favorites.add(cpName.get(parkHolder.getAdapterPosition()));
                    parkHolder.favoriteButton.setChecked(true);
                    favourited.set(parkHolder.getAdapterPosition(), true);
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.apply();
                    Toast.makeText( context, cpName.get(parkHolder.getAdapterPosition()) + " Favori otoparklara eklendı!", Toast.LENGTH_LONG).show();
                } else{
                    favorites.remove(cpName.get(parkHolder.getAdapterPosition()));
                    parkHolder.favoriteButton.setChecked(false);
                    favourited.set(parkHolder.getAdapterPosition(), false);
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.apply();
                    Toast.makeText( context, cpName.get(parkHolder.getAdapterPosition()) + " Favori otoparklardan çıkarıldı!", Toast.LENGTH_LONG).show();
                }
            }
        });

        parkHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerMaster.getCarParkManager().chooseCarPark(cpName.get(parkHolder.getAdapterPosition()));
                Intent intent = new Intent(context, MapsActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cpName.size();
    }

    class ParkHolder extends RecyclerView.ViewHolder{

        TextView address;
        TextView condition;
        TextView cpName;
        ToggleButton favoriteButton;

        ParkHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.address);
            condition = itemView.findViewById(R.id.condition);
            cpName = itemView.findViewById(R.id.cpName);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
}
