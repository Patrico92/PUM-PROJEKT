package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class RecipesList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tutaj konfiguracja portrait
        setContentView(R.layout.recipes_list_portrait);

        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,0);

        ArrayList<Recipe> recipes = myDBHandler.listOfRecipes();
        ListAdapter listAdapter = new RowPortraitAdapter(this, recipes);

        ListView recipesListPortait = (ListView) findViewById(R.id.recipesListPortrait);
        recipesListPortait.setAdapter(listAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipes_list, menu);
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
}
