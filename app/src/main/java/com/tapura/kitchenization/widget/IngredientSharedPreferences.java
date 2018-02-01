package com.tapura.kitchenization.widget;


import android.content.Context;
import android.content.SharedPreferences;

import com.tapura.kitchenization.model.IngredientsItem;
import com.tapura.kitchenization.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientSharedPreferences {

    private static final String WIDGET_INGREDIENTS_SHARED_PREFS = "widget_prefs";
    private static final String RECIPE_KEY = "recipe_key";

    public static void setIngredients(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(WIDGET_INGREDIENTS_SHARED_PREFS, 0).edit();

        String stringRecipe = Recipe.recipeToJson(recipe);
        prefs.putString(RECIPE_KEY, stringRecipe);

        prefs.apply();
    }

    public static List<IngredientsItem> getIngredients(Context context) {
        List<IngredientsItem> ingredients = new ArrayList<>();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WIDGET_INGREDIENTS_SHARED_PREFS, Context.MODE_PRIVATE);

        String savedRecipe = sharedPreferences.getString(RECIPE_KEY, null);

        if (savedRecipe != null) {
            Recipe r = Recipe.jsonToRecipe(savedRecipe);
            ingredients = r.getIngredients();
        }
        return ingredients;
    }

    public static Recipe getRecipe(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WIDGET_INGREDIENTS_SHARED_PREFS, Context.MODE_PRIVATE);

        String savedRecipe = sharedPreferences.getString(RECIPE_KEY, null);

        return Recipe.jsonToRecipe(savedRecipe);
    }

    public static void deletePref(Context context) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(WIDGET_INGREDIENTS_SHARED_PREFS, 0).edit();
        prefs.remove(RECIPE_KEY);
        prefs.apply();
    }
}
