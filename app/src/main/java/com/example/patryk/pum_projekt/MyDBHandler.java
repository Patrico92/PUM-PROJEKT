package com.example.patryk.pum_projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 19; //tutaj trzeba zmienić przy wprowadzaniu zmian do struktury bazy

    private static final String DATABASE_NAME = "database.db";

    public static final String TABLE_NAME_RECIPES = "recipes";
    public static final String TABLE_NAME_INGREDIENTS = "ingredients";
    public static final String TABLE_NAME_TASKS = "tasks";
    public static final String TABLE_SHOPPINGLIST_NAME = "shoppinglist";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECIPE = "recipe";
    public static final String COLUMN_RECIPEDESCRIPTION = "recipedescription";
    private static final String COLUMN_RECIPEPATH = "recipepath";

    public static final String COLUMN_RECIPE_ID = "idrecipe";
    public static final String COLUMN_INGREDIENT = "ingredient";
    public static final String COLUMN_INGREDIENT_AMOUNT= "ingredientamount";

    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_TASK_TIME= "tasktime";

    public static final String COLUMN_SHOPPING_ITEM = "item";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //tworzenie tabeli przepisow
        String query = "CREATE TABLE " + TABLE_NAME_RECIPES + "( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE + " TEXT, " +
                COLUMN_RECIPEDESCRIPTION + " TEXT, " +
                COLUMN_RECIPEPATH + " TEXT " +
                ")" +" ;";
        db.execSQL(query);

        //tworzenie tabeli składników
        query = "CREATE TABLE " + TABLE_NAME_INGREDIENTS + "( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_ID + " INTEGER, " +
                COLUMN_INGREDIENT + " TEXT, " +
                COLUMN_INGREDIENT_AMOUNT + " TEXT " +
                ")" +" ;";
        db.execSQL(query);

        //tworzenie tabeli zadan do przepisów
        query = "CREATE TABLE " + TABLE_NAME_TASKS + "( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_ID + " INTEGER, " +
                COLUMN_TASK + " TEXT, " +
                COLUMN_TASK_TIME + " INTEGER " + //czas zadania zapisujemy w tabeli w sekundach
                ")" +" ;";
        db.execSQL(query);

        //tworzenie tabeli zadan do przepisów
        query = "CREATE TABLE " + TABLE_SHOPPINGLIST_NAME + "( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SHOPPING_ITEM + " TEXT " +
                ")" +" ;";
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_TASKS);
        onCreate(db);
    }

    //dodawanie przepisu do bazy, metoda zwróci "true" jeżeli zapis będzie udany
    public boolean addRecipe(Recipe recipe)
    {
        ContentValues values = new ContentValues();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_NAME_RECIPES + " WHERE " + COLUMN_RECIPE + " = '" + recipe.getRecipename() + "'", null);

        if (cursor.moveToFirst()) //sprawdzamy czy w bazie nie ma już przepisu o takiej nazwie
        {
            return false;
        }

        //tutaj trzeba sprawdzić, czy nie ma juz przepisu o takiej nazwie w bazie

        values.put(COLUMN_RECIPE, recipe.getRecipename());
        values.put(COLUMN_RECIPEDESCRIPTION, recipe.getRecipredescription());
        values.put(COLUMN_RECIPEPATH, recipe.getRecipePath());


        db.insert(TABLE_NAME_RECIPES,null,values); //dodajemy nazwę i opis

        cursor = db.rawQuery("SELECT _id FROM " + TABLE_NAME_RECIPES + " WHERE " + COLUMN_RECIPE + " = '" + recipe.getRecipename() + "'", null );
        cursor.moveToFirst();

        int recipeId = cursor.getInt(0);

        String[] ingredients = recipe.getIngredients();
        String[] ingredientsAmount = recipe.getIngredientsAmount();

        String[] tasks = recipe.getTasks();
        int[] taskstime = recipe.getTasksTime();

        if(ingredients!=null)
        {
            for (int i = 0; i < ingredients.length; i++) //dodajemy składniki i ich ilosci
            {
                values = new ContentValues();

                values.put(COLUMN_RECIPE_ID, recipeId);
                values.put(COLUMN_INGREDIENT, ingredients[i]);
                values.put(COLUMN_INGREDIENT_AMOUNT, ingredientsAmount[i]);

                db.insert(TABLE_NAME_INGREDIENTS, null, values);
            }
        }

        if(tasks != null)
        {
            for (int i = 0; i < tasks.length; i++) //dodajemy zadania i ich czasy trwania
            {
                values = new ContentValues();

                values.put(COLUMN_RECIPE_ID, recipeId);
                values.put(COLUMN_TASK, tasks[i]);
                values.put(COLUMN_TASK_TIME, taskstime[i]);

                db.insert(TABLE_NAME_TASKS, null, values);
            }
        }
        cursor.close();
        db.close();

        return true;
    }

    //pobieranie listy wszystkich przepisów będących w bazie
    public ArrayList<Recipe> listOfRecipes()
    {

        ArrayList<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_RECIPES, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {

            Recipe recipe = new Recipe(cursor.getString(1),cursor.getString(2),cursor.getString(3),null,null,null,null);
            recipe.set_id(cursor.getInt(0));
            recipes.add(recipe);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return recipes;
    }

    public void addShoppingItem(String item)
    {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put(COLUMN_SHOPPING_ITEM, item);
        db.insert(TABLE_SHOPPINGLIST_NAME, null,cv);

        db.close();

    }

    public void deleteShoppingItem(int id)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_SHOPPINGLIST_NAME, COLUMN_ID + " = " + id, null);

        db.close();

    }

    public ArrayList<String> getShoppingList()
    {
        ArrayList<String> items = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHOPPINGLIST_NAME, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            items.add(cursor.getString(1));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return items;
    }

    public void deleteShoppingList()
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_SHOPPINGLIST_NAME, null, null);

        db.close();

    }

    //pobieranie przepisu z bazy po nazwie
    public Recipe getRecipe(String recipeName)
    {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_RECIPES + " WHERE " + COLUMN_RECIPE + " = " + recipeName, null);
        cursor.moveToFirst();

        Recipe recipe = new Recipe(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                null,
                null,
                null,
                null
        );

        recipe.set_id(cursor.getInt(0));

        db.close();
        cursor.close();

        return recipe;

    }

    public Recipe getRecipe(int _id){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_RECIPES + " WHERE " + COLUMN_ID + " = " + _id, null);

        if(!cursor.moveToFirst()) return null;

        int recipeID = cursor.getInt(0);
        String name = cursor.getString(1);
        String description = cursor.getString(2);
        String path = cursor.getString(3);

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_INGREDIENTS + " WHERE " + COLUMN_RECIPE_ID + " = " + _id, null);

        cursor.moveToFirst();

        ArrayList<String> ingredients = new ArrayList<>();
        ArrayList<String> ingredientsAmount = new ArrayList<>();

        while(!cursor.isAfterLast())
        {
            ingredients.add(cursor.getString(2));
            ingredientsAmount.add(cursor.getString(3));
            cursor.moveToNext();
        }

        ArrayList<String> tasks = new ArrayList<>();
        ArrayList<Integer> tasksTime = new ArrayList<>();

        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_TASKS + " WHERE " + COLUMN_RECIPE_ID + " = " + _id, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            tasks.add(cursor.getString(2));
            tasksTime.add(cursor.getInt(3));
            cursor.moveToNext();
        }

        Recipe recipe = new Recipe(
                name,
                description,
                path,
                ingredients.toArray(new String[ingredients.size()]),
                ingredientsAmount.toArray(new String[ingredientsAmount.size()]),
                tasks.toArray(new String[tasks.size()]),
                listToArray(tasksTime)
        );

        recipe.set_id(recipeID);

        cursor.close();
        return recipe;
    }

    public boolean deleteRecipe(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_RECIPES, COLUMN_ID + " = " + id, null);
        db.delete(TABLE_NAME_TASKS, COLUMN_RECIPE_ID + " = " + id, null);
        db.delete(TABLE_NAME_INGREDIENTS, COLUMN_RECIPE_ID + " = " + id, null);
        db.close();

        return true;
    }

    public boolean editRecipe(int id, Recipe recipe) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put(COLUMN_RECIPE, recipe.getRecipename());
        cv.put(COLUMN_RECIPEDESCRIPTION, recipe.getRecipredescription());
        cv.put(COLUMN_RECIPEPATH, recipe.getRecipePath());


        db.update(TABLE_NAME_RECIPES, cv, "_id" + "=" + id, null);
        db.delete(TABLE_NAME_TASKS, COLUMN_RECIPE_ID + " = " + id, null);
        db.delete(TABLE_NAME_INGREDIENTS, COLUMN_RECIPE_ID + " = " + id, null);

        String[] ingredients = recipe.getIngredients();
        String[] ingredientsAmount = recipe.getIngredientsAmount();

        String[] tasks = recipe.getTasks();
        int[] taskstime = recipe.getTasksTime();

        if(ingredients!=null)
        {
            for (int i = 0; i < ingredients.length; i++) //dodajemy składniki i ich ilosci
            {
                ContentValues values = new ContentValues();

                values.put(COLUMN_RECIPE_ID, id);
                values.put(COLUMN_INGREDIENT, ingredients[i]);
                values.put(COLUMN_INGREDIENT_AMOUNT, ingredientsAmount[i]);

                db.insert(TABLE_NAME_INGREDIENTS, null, values);
            }
        }

        if(tasks != null)
        {
            for (int i = 0; i < tasks.length; i++) //dodajemy zadania i ich czasy trwania
            {
                ContentValues values = new ContentValues();

                values.put(COLUMN_RECIPE_ID, id);
                values.put(COLUMN_TASK, tasks[i]);
                values.put(COLUMN_TASK_TIME, taskstime[i]);

                db.insert(TABLE_NAME_TASKS, null, values);
            }
        }
        db.close();

        return true;

    }

    private int[] listToArray(ArrayList<Integer> list){
        int [] array = new int[list.size()];

        for(int i = 0; i < list.size(); i++)
        {
            array[i] = list.get(i);
        }

        return array;
    }

}