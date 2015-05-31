package com.example.patryk.pum_projekt;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class IngredientListAdapter extends ArrayAdapter<Ingredient> {
    private List<Ingredient> objects;
    private int resource;
    private Context context;


    public IngredientListAdapter(Context context, int resource, List<Ingredient> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    private class IngredientHolder {
        Ingredient ingredient;
        TextView name;
        TextView amount;
        Button deleteIngredientButton;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        IngredientHolder holder;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(resource, parent, false);

        holder = new IngredientHolder();
        holder.ingredient = objects.get(position);
        holder.deleteIngredientButton = (Button)row.findViewById(R.id.ButtonDelete);
        holder.deleteIngredientButton.setTag(holder.ingredient);

        holder.name = (TextView)row.findViewById(R.id.TextViewIngredient);

        holder.amount = (TextView)row.findViewById(R.id.TextViewAmount);


        row.setTag(holder);

        setupItem(holder);
        return row;
    }


    private void setupItem(IngredientHolder holder) {
        holder.name.setText(holder.ingredient.getName());
        holder.amount.setText(holder.ingredient.getAmount());
    }




}
