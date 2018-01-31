package com.tapura.kitchenization.ingredients;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapura.kitchenization.R;
import com.tapura.kitchenization.model.IngredientsItem;
import com.tapura.kitchenization.model.Recipe;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

public class IngredientsFragment extends Fragment {

    public static final String RECIPE_INGREDIENT = "recipe_ingredient";

    private Recipe mRecipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RECIPE_INGREDIENT)) {
            mRecipe = Parcels.unwrap(getArguments().getParcelable(RECIPE_INGREDIENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_detail, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.ingredients_list);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mRecipe.getIngredients()));

        return rootView;
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<IngredientsItem> mValues;

        SimpleItemRecyclerViewAdapter(List<IngredientsItem> items) {
            mValues = items;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_layout, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            IngredientsItem ingredient = mValues.get(position);
            holder.mQuantity.setText(Double.toString(ingredient.getQuantity()));
            holder.mMeasure.setText(ingredient.getMeasure());
            holder.mName.setText(ingredient.getIngredient());

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mQuantity;
            final TextView mMeasure;
            final TextView mName;

            ViewHolder(View view) {
                super(view);
                mQuantity = view.findViewById(R.id.text_view_quantity);
                mMeasure = view.findViewById(R.id.text_view_measure);
                mName = view.findViewById(R.id.text_view_ingredient_name);
            }
        }
    }
}