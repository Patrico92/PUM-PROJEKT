package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class RecipesList extends Activity implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {


    MyDBHandler myDBHandler;
    ArrayList<Recipe> recipes;
    ListView recipesListPortait;
    RowPortraitAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //tutaj konfiguracja portrait
        setContentView(R.layout.recipes_list_portrait);

        myDBHandler = new MyDBHandler(this,null,null,0);

        listAdapter = new RowPortraitAdapter(this, myDBHandler.listOfRecipes());

        recipesListPortait = (ListView) findViewById(R.id.recipesListPortrait);
        recipesListPortait.setOnItemClickListener(this);
        recipesListPortait.setOnItemLongClickListener(this);
        recipesListPortait.setAdapter(listAdapter);
        recipesListPortait.requestFocus();

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

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

        final int pos = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Usuń lub edytuj przepis");

        builder.setPositiveButton("Usuń przepis", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Recipe recipeToDelete = (Recipe) parent.getItemAtPosition(position);
                myDBHandler.deleteRecipe(recipeToDelete.get_id());
                listAdapter.remove(recipeToDelete);
                listAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("Edytuj przepis", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //kod do edytowania przepisów
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent i = new Intent(this, RecipeDisplay.class);
            Recipe recipe =  (Recipe) parent.getItemAtPosition(position);
            i.putExtra("ID", recipe.get_id());
            startActivity(i);

    }


}