package com.tapura.kitchenization.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.tapura.kitchenization.R;
import com.tapura.kitchenization.model.Recipe;

import org.parceler.Parcels;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    public static final String WIDGET_INGREDIENT = "widget_ingredient";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, Recipe recipe) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
        Intent intent = new Intent(context, WidgetService.class);

        String widgetText = "";
        if (recipe != null) {
            widgetText = recipe.getName();
            Bundle bundle = new Bundle();
            bundle.putParcelable(WIDGET_INGREDIENT, Parcels.wrap(recipe));
            intent.putExtra(WIDGET_INGREDIENT, bundle);
        } else {
            Recipe sharedRecipe = IngredientSharedPreferences.getRecipe(context);

            if (sharedRecipe != null) {
                widgetText = sharedRecipe.getName();
            }
        }

        views.setTextViewText(R.id.text_view_widget_recipe_name, widgetText);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.list_view_widget_ingredients, intent);
        views.setEmptyView(R.id.list_view_widget_ingredients, R.id.widget_empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        IngredientSharedPreferences.deletePref(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

