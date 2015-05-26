package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class RecipeDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle intent = getIntent().getExtras();
        int recipeID = intent.getInt("ID");

        MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 0);

        Recipe recipe = myDBHandler.getRecipe(recipeID);

        RelativeLayout myLayout = new RelativeLayout(this);
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Color.rgb(139,69,19));

        Resources r = getResources();

        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                300,
                r.getDisplayMetrics()
        );



        //tworzymy obrazek do naszego przepisu
        ImageView recipeImage = new ImageView(this);
        recipeImage.setId(1);

        recipeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        recipeImage.setImageResource(R.drawable.recipe);

        RelativeLayout.LayoutParams details = new RelativeLayout.LayoutParams(
                size,
                size
        );

        details.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        details.addRule(RelativeLayout.CENTER_HORIZONTAL);
        myLayout.addView(recipeImage,details);

        //tworzymy miejsce na nazwę przepisy

        TextView name = new TextView(this);
        name.setText(recipe.getRecipename());
        name.setId(2);
        name.setTypeface(null, Typeface.BOLD_ITALIC);

        RelativeLayout.LayoutParams detailsTextName = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        detailsTextName.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsTextName.addRule(RelativeLayout.BELOW, 1);
        name.setTextAppearance(this, android.R.style.TextAppearance_Large);

        myLayout.addView(name, detailsTextName);

        TextView ingredientsText = new TextView(this);
        ingredientsText.setText("Składniki:");
        ingredientsText.setId(3);

        RelativeLayout.LayoutParams detailsTextIngredients = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        detailsTextIngredients.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsTextIngredients.addRule(RelativeLayout.BELOW, 2);
        ingredientsText.setTextAppearance(this, android.R.style.TextAppearance_Medium);

        myLayout.addView(ingredientsText, detailsTextIngredients);

        int id = 3;

        String[] ingredients = recipe.getIngredients();
        String[] ingredientsAmount = recipe.getIngredientsAmount();

        for(int i = 0; i < ingredients.length; i++)
        {
            TextView ingredient = new TextView(this);
            ingredient.setText(ingredients[i] + " - " + ingredientsAmount[i]);
            ingredient.setId(++id);

            RelativeLayout.LayoutParams detailsTextIngredient = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            detailsTextIngredient.addRule(RelativeLayout.CENTER_HORIZONTAL);
            detailsTextIngredient.addRule(RelativeLayout.BELOW, id-1);
            ingredient.setTextAppearance(this, android.R.style.TextAppearance_Medium);

            myLayout.addView(ingredient, detailsTextIngredient);
        }

        TextView recipeDescriptionText = new TextView(this);
        recipeDescriptionText.setText(recipe.getRecipredescription());
        recipeDescriptionText.setId(++id);

        RelativeLayout.LayoutParams detailsTextDescription = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        detailsTextDescription.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsTextDescription.addRule(RelativeLayout.BELOW, id-1);
        recipeDescriptionText.setTextAppearance(this, android.R.style.TextAppearance_Large);
        recipeDescriptionText.setTypeface(null, Typeface.ITALIC);

        myLayout.addView(recipeDescriptionText, detailsTextDescription);



        sv.addView(myLayout);
        setContentView(sv);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String timeDisplay(int time)
    {
        return null;
    }
}
