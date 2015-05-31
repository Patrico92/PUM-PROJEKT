package com.example.patryk.pum_projekt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by patryk on 31.05.15.
 */
public class RowLandscapeAdapter extends ArrayAdapter<Recipe> {

    public RowLandscapeAdapter(Context context,  ArrayList<Recipe> objects)
    {
        super(context, R.layout.row_landscape, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View customView = layoutInflater.inflate(R.layout.row_landscape, parent, false);

        Recipe singleRecipe = getItem(position);

        TextView landscapeName = (TextView) customView.findViewById(R.id.landscapeName);
        TextView landscapeDesc = (TextView) customView.findViewById(R.id.landScapeDesc);
        ImageView landscapeImage = (ImageView) customView.findViewById(R.id.landscapeImage);

        landscapeName.setText(singleRecipe.getRecipename());

        String description = singleRecipe.getRecipredescription();

        if(description.length() > 50 )
        {
            description = description.substring(0,50) + "...";
        }

        landscapeDesc.setText(description);

        if(singleRecipe.getRecipePath().equals("")) {
            landscapeImage.setImageResource(R.drawable.logo);
        }
        else {
            landscapeImage.setImageBitmap(decodeFile(singleRecipe.getRecipePath()));
        }

        return customView;
    }


    private Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 300;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }
}
