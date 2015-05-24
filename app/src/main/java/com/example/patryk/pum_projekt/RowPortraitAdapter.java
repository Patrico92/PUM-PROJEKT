package com.example.patryk.pum_projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patryk on 24.05.15.
 */
class RowPortraitAdapter extends ArrayAdapter<Recipe> {

    public RowPortraitAdapter(Context context, ArrayList<Recipe> objects)
    {
        super(context, R.layout.row_portrait, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View customView = layoutInflater.inflate(R.layout.row_portrait, parent, false);

        Recipe singleRecipe = getItem(position);
        TextView recipeName = (TextView) customView.findViewById(R.id.recipeName);
        ImageView recipePhoto = (ImageView) customView.findViewById(R.id.recipePhoto);

        recipeName.setText(singleRecipe.getRecipename());
        recipePhoto.setImageResource(R.drawable.recipe);

        return customView;
    }
}
