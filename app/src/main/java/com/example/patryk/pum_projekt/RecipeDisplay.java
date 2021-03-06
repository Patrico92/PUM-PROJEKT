package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;


public class RecipeDisplay extends Activity implements Button.OnClickListener {

    int recipeID;
    Timer timer;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle intent = getIntent().getExtras();
        recipeID = intent.getInt("ID");

        final MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 0);

        recipe = myDBHandler.getRecipe(recipeID);

        RelativeLayout myLayout = new RelativeLayout(this);
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Color.rgb(205,133,63));

        Resources r = getResources();

        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                300,
                r.getDisplayMetrics()
        );

        //tworzymy obrazek do naszego przepisu
        ImageView recipeImage = new ImageView(this);
        int id = 0;
        recipeImage.setId(++id);

        recipeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if(recipe.getRecipePath().equals("")) {
            recipeImage.setImageResource(R.drawable.logo);
        }
        else
        {
            recipeImage.setImageBitmap(decodeFile(recipe.getRecipePath()));
        }
        RelativeLayout.LayoutParams details = new RelativeLayout.LayoutParams(
                size,
                size
        );

        details.setMargins(10,10,10,10);

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
        detailsTextName.setMargins(5,5,5,5);
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
        detailsTextIngredients.setMargins(5,5,5,5);
        myLayout.addView(ingredientsText, detailsTextIngredients);

        String[] ingredients = recipe.getIngredients();
        String[] ingredientsAmount = recipe.getIngredientsAmount();

        for(int i = 0; i < ingredients.length; i++) // w tęj pętli dynamicznie tworzymy wyświetlanie składników
        {
            TextView ingredient = new TextView(this);
            if(ingredientsAmount[i].equals(("")))
            {
                ingredient.setText(ingredients[i]);
            } else {
                ingredient.setText(ingredients[i] + " - " + ingredientsAmount[i]);
            }

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
        detailsTextDescription.setMargins(5,10,5,5);
        recipeDescriptionText.setPadding(10,10,10,10);

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
            taskButton.setTag(R.id.taskName, tasks[i]); //button przechowuje inforację o zadaniu
            taskButton.setTag(R.id.taskTime, taskTime[i]); //przechowuje też informację o długości zadania
            taskButton.setOnClickListener(this);
            taskButton.setText(tasks[i] + " " + timeDisplay(taskTime[i]));

            RelativeLayout.LayoutParams detailsButtonTask = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            detailsButtonTask.addRule(RelativeLayout.CENTER_HORIZONTAL);
            detailsButtonTask.addRule(RelativeLayout.BELOW, id-1);

            myLayout.addView(taskButton, detailsButtonTask);

        }

        Button createShoppingList = new Button(this);
        createShoppingList.setId(++id);

        createShoppingList.setText("Dodaj składniki do listy zakupów");

        RelativeLayout.LayoutParams detailsButtonShoppingList = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        detailsButtonShoppingList.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsButtonShoppingList.addRule(RelativeLayout.BELOW, id-1);

        createShoppingList.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList<String> ingredientsToBuy = recipe.getAllIngredients();

                        for (String ingredient : ingredientsToBuy)
                        {
                            myDBHandler.addShoppingItem(ingredient);
                        }

                        Button thisButton = (Button) findViewById(v.getId());
                        thisButton.setText("Składniki dodane do listy zakupów!");

                    }
                }
        );

        myLayout.addView(createShoppingList, detailsButtonShoppingList);


        timer = new Timer();

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

    @Override
    public void onClick(View v) {

        final Button button = (Button) findViewById(v.getId());
        button.setText(button.getTag(R.id.taskName) + " zrobione!");

        startTimer((String) button.getTag(R.id.taskName), (Integer) button.getTag(R.id.taskTime));

    }

    public Bitmap decodeFile(String path) {
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

    public void startTimer(String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
