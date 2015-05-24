package com.example.patryk.pum_projekt;

/**
 * Created by patryk on 17.05.15.
 */
public class Recipe {

    private int _id;
    private String recipename; //nazwa przepisu
    private String recipredescription; //sam przepis
    private String[] ingredients; //lista składników
    private String[] ingredientsAmount; //ilość składników - jest w stringu, bo równie dobrze może to być "kilogram" pomidorów albo "łyżeczka" pieprzu
    private String[] tasks; //zadania do przepisu
    private int[] tasksTime; //czas trwania zadań (w sekundach)

    Recipe(String recipename, String recipredescription, String[] ingredients,String[] ingredientsAmount,String[] tasks,int[] tasksTime)
    {
        this.recipename = recipename;
        this.recipredescription = recipredescription;
        this.ingredients = ingredients;
        this.ingredientsAmount = ingredientsAmount;
        this.tasks = tasks;
        this.tasksTime = tasksTime;
    }

    public String getRecipename()
    {
        return recipename;
    }

    public String getRecipredescription()
    {
        return recipredescription;
    }

    public void set_id(int id)
    {
        _id = id;
    }

    public int get_id()
    {
        return _id;
    }

    public String[] getIngredientsAmount()
    {
        return ingredientsAmount;
    }

    public String[] getTasks()
    {
        return tasks;
    }

    public int[] getTasksTime()
    {
        return tasksTime;
    }

    public String[] getIngredients()
    {
        return ingredients;
    }
}
