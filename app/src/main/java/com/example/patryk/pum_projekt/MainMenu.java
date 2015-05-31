package com.example.patryk.pum_projekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainMenu extends Activity implements View.OnClickListener {


    String TAG = "tag"; //to jest pomocnicze, służy do wyświetlania logów w konsoli
    Button buttonCreate;
    Button buttonBase;
    Button buttonNewRec;
    Recipe recipe;
    MyDBHandler myDBHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buttonBase = (Button) findViewById(R.id.buttonBase);
        buttonBase.setOnClickListener(this);

        buttonCreate = (Button) findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(this);

        buttonNewRec = (Button) findViewById(R.id.buttonNewRec);
        buttonNewRec.setOnClickListener(this);


        myDBHandler = new MyDBHandler(this,null,null,0); //tworzymy handlera do obsługi bazy

        Recipe recipe = new Recipe("Gotowany ryż", "Ugotuj ryż w wodzie przez 10 min", "" , new String[]{"ryż", "woda"},new String[]{"paczka","litr"},new String[]{"Gotuj ryż w wodzie"},new int[]{10*60});
        myDBHandler.addRecipe(recipe); //dodawanie przepisu do bazy

        recipe = new Recipe("Kromka z maslem", "Posmaruj chleb masłem", "", new String[]{"chleb", "masło"},new String[]{"kromka","troszku"},null,null);
        myDBHandler.addRecipe(recipe);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
    public void onClick(View v)
    {

        if(v.getId() == buttonBase.getId())
        {
            Intent i = new Intent(this, RecipesList.class);
            startActivity(i);
        }
        if(v.getId() == buttonNewRec.getId())
        {
            Intent createNewRecipe = new Intent(this, CreateRecipe.class);
            startActivity(createNewRecipe);
        }

    }


}
