package com.tapura.kitchenization.widget;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tapura.kitchenization.R;
import com.tapura.kitchenization.model.IngredientsItem;
import com.tapura.kitchenization.model.Recipe;

import org.parceler.Parcels;

import java.util.List;

import static com.tapura.kitchenization.widget.IngredientWidget.WIDGET_INGREDIENT;

public class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<IngredientsItem> mIngredients;

    public IngredientRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;

        if (intent.hasExtra(WIDGET_INGREDIENT)) {
            Bundle bundle = intent.getBundleExtra(WIDGET_INGREDIENT);
            Recipe recipe = Parcels.unwrap(bundle.getParcelable(WIDGET_INGREDIENT));

            mIngredients = recipe.getIngredients();

            IngredientSharedPreferences.setIngredients(context, recipe);
        } else {
            mIngredients = IngredientSharedPreferences.getIngredients(context);
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (getCount() == 0) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_widget_layout);

        views.setTextViewText(R.id.text_view_quantity, Double.toString(mIngredients.get(i).getQuantity()));
        views.setTextViewText(R.id.text_view_measure, mIngredients.get(i).getMeasure());
        views.setTextViewText(R.id.text_view_ingredient_name, mIngredients.get(i).getIngredient());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}