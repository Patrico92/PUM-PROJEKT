package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
        ImageView recipeImage = new ImageView(this); //TODO
        int id = 0;
        recipeImage.setId(++id);

        recipeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        recipeImage.setImageResource(R.drawable.recipe);

        RelativeLayout.LayoutParams details = new RelativeLayout.LayoutParams(
                size,
                size
        );

        details.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        details.addRule(RelativeLayout.CENTER_HORIZONTAL);
        myLayout.addView(recipeImage,details);

        TextView name = new TextView(this);  //tworzymy miejsce na nazwę przepisu
        name.setText(recipe.getRecipename());
        name.setId(++id);
        name.setTypeface(null, Typeface.BOLD);

        RelativeLayout.LayoutParams detailsTextName = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        detailsTextName.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsTextName.addRule(RelativeLayout.BELOW, id-1);
        name.setTextAppearance(this, android.R.style.TextAppearance_Large);

        myLayout.addView(name, detailsTextName);


        TextView ingredientsText = new TextView(this);  //TextView wyświetlający napis: "Składniki"
        ingredientsText.setText("Składniki:");
        ingredientsText.setId(++id);

        RelativeLayout.LayoutParams detailsTextIngredients = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        detailsTextIngredients.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsTextIngredients.addRule(RelativeLayout.BELOW, id-1);
        ingredientsText.setTextAppearance(this, android.R.style.TextAppearance_Medium);

        myLayout.addView(ingredientsText, detailsTextIngredients);

        String[] ingredients = recipe.getIngredients();
        String[] ingredientsAmount = recipe.getIngredientsAmount();

        for(int i = 0; i < ingredients.length; i++) // w tęj pętli dynamicznie tworzymy wyświetlanie składników
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

        TextView tasksText = new TextView(this);
        tasksText.setText("Zadania do wykonania:");
        tasksText.setId(++id);

        RelativeLayout.LayoutParams detailsTextTasks = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        detailsTextTasks.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsTextTasks.addRule(RelativeLayout.BELOW, id-1);
        tasksText.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        tasksText.setTypeface(null, Typeface.BOLD);

        myLayout.addView(tasksText, detailsTextTasks);

        String[] tasks = recipe.getTasks();
        int[] taskTime = recipe.getTasksTime();

        for(int i = 0; i< tasks.length; i++) //buttony służace do uruchamiania zadań
        {
            Button taskButton = new Button(this);
            taskButton.setId(++id);
            taskButton.setText(tasks[i] + " " + timeDisplay(taskTime[i]));

            RelativeLayout.LayoutParams detailsButtonTask = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            detailsButtonTask.addRule(RelativeLayout.CENTER_HORIZONTAL);
            detailsButtonTask.addRule(RelativeLayout.BELOW, id-1);

            myLayout.addView(taskButton, detailsButtonTask);

        }

        sv.addView(myLayout);
        setContentView(sv);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_recipe_display, menu);
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
        String timeString="";
        int hours = time/3600;
        time -= hours*3600;
        int minutes = time/60;
        time -= minutes*60;

        if (hours < 10 ) timeString+= "0" + Integer.toString(hours) + ":";
        else timeString += Integer.toString(hours)+":";

        if (minutes < 10 ) timeString+= "0" + Integer.toString(minutes) + ":";
        else timeString += Integer.toString(minutes)+":";

        if (time < 10 ) timeString+= "0" + Integer.toString(time);
        else timeString += Integer.toString(time)+":";

        return timeString;

    }
}
