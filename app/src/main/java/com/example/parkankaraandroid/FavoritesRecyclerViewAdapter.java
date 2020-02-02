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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesRecyclerViewAdapter extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.FavoritesParkHolder> {
    private ControllerMaster controllerMaster = new ControllerMaster();
    private final ArrayList<String> cpName;
    private final ArrayList<String> cpAddress;
    private final ArrayList<String> cpCondition;
    private static SharedPreferences sharedPreferences;
    private final Activity context;
    private Set<String> favorites;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    FavoritesRecyclerViewAdapter( ArrayList<String> cpName, ArrayList<String> cpAddress, ArrayList<String> cpCondition, Activity context) {
        this.cpName = cpName;
        this.cpAddress = cpAddress;
        this.cpCondition = cpCondition;
        this.context = context;
    }


    @NonNull
    @Override
    public FavoritesParkHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from( viewGroup.getContext() );
        View view = layoutInflater.inflate( R.layout.favorites_recycler_row, viewGroup, false );
        sharedPreferences = viewGroup.getContext().getSharedPreferences( "com.example.parkankara", Context.MODE_PRIVATE );
        favorites = new HashSet<>( sharedPreferences.getStringSet( "CarParkName", new HashSet<String>() ) );
        return new FavoritesParkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoritesParkHolder favoritesParkHolder, int i) {
        favoritesParkHolder.address.setText( cpAddress.get(i) );
        favoritesParkHolder.condition.setText( cpCondition.get(i) );
        favoritesParkHolder.cpName.setText( cpName.get(i) );

        favoritesParkHolder.favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if( favorites.contains(cpName.get(favoritesParkHolder.getAdapterPosition())) ){
                    Set<String> oldFavorites = new HashSet<>();
                    oldFavorites.addAll(favorites);
                    favorites.remove(cpName.get(favoritesParkHolder.getAdapterPosition()));
                    editor.clear().apply();
                    editor.putStringSet("CarParkName", favorites).apply();
                    editor.apply();
                    Toast.makeText(context, cpName.get(favoritesParkHolder.getAdapterPosition()) + "Favori otoparklardan çıkarıldı!!!", Toast.LENGTH_LONG).show();
                    pcs.firePropertyChange("favorites", oldFavorites, favorites);
                }
            }
        });


        favoritesParkHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerMaster.getCarParkManager().chooseCarPark(cpName.get(favoritesParkHolder.getAdapterPosition()));
                Intent intent = new Intent(context, MapsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cpName.size();
    }

    class FavoritesParkHolder extends RecyclerView.ViewHolder{

        TextView address;
        TextView condition;
        TextView cpName;
        ToggleButton favoriteButton;

        FavoritesParkHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.favoritesAddress);
            condition = itemView.findViewById(R.id.favoritesCondition);
            cpName = itemView.findViewById(R.id.favoritesCpName);
            favoriteButton = itemView.findViewById(R.id.favoritesFavoriteButton);
        }
    }

    void addFavoritesPropertyChangeListener(PropertyChangeListener listener){
        this.pcs.addPropertyChangeListener("favorites", listener);
    }
}
