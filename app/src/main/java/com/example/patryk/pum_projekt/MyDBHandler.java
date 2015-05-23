package com.example.patryk.pum_projekt;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2; //tutaj trzeba zmienić przy wprowadzaniu zmian do struktury bazy
    private static final String DATABASE_NAME = "database.db";
    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECIPE = "recipe";
    public static final String COLUMN_RECIPEDESCRIPTION = "recipedescription";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) throws IOException {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i("TAG", "onCreate");

        String query = "CREATE TABLE " + TABLE_NAME + "( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE + " TEXT, " +
                COLUMN_RECIPEDESCRIPTION + " TEXT " +
                ")" +" ;";
        db.execSQL(query);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("TAG", "onUpgrade");

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }
    //dodawanie przepisu do bazy
    public void addRecipe(Recipe recipe)
    {

        ContentValues values = new ContentValues();

        values.put(COLUMN_RECIPE, recipe.getRecipename());
        values.put(COLUMN_RECIPEDESCRIPTION, recipe.getRecipredescription());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,values);

        //Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_NAME + " WHERE " + COLUMN_RECIPE + " = " + recipe.getRecipename(),null );
        //cursor.moveToFirst();

        //Log.i("TAG", "Id " + recipe.getRecipename() + Integer.toString(cursor.getInt(0)));

        db.close();
    }

    public ArrayList<Recipe> listOfRecipes()
    {

        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {

            Recipe recipe = new Recipe(cursor.getString(1),cursor.getString(2),null,null);
            recipe.set_id(cursor.getInt(0));
            recipes.add(recipe);
            cursor.moveToNext();
        }

        db.close();

        return recipes;
    }

    public Recipe getRecipe(String recipeName)
    {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_RECIPE + " = " + recipeName, null);
        cursor.moveToFirst();

        Recipe recipe = new Recipe(
                cursor.getString(1),
                cursor.getString(2),
                null,
                null
        );

        recipe.set_id(cursor.getInt(0));

        db.close();

        return recipe;

    }
}
