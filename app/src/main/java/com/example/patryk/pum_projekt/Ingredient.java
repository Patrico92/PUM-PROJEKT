package com.example.patryk.pum_projekt;


public class Ingredient {
    private String name;
    private String amount;
    public Ingredient(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {

        return name;
    }

    public String getAmount() {
        return amount;
    }
}
