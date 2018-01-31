package com.tapura.kitchenization.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapura.kitchenization.R;
import com.tapura.kitchenization.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipeData;
    private Context mContext;
    private RecipeAdapterOnClickHandler mCallback;

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler callback) {
        mContext = context;
        mCallback = callback;
    }

    public void setRecipeList(List<Recipe> recipes) {
        mRecipeData = recipes;
        notifyDataSetChanged();
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recipe_adapter_view, parent, false);
        return new RecipeAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        holder.mTv.setText(mRecipeData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipeData != null) {
            return mRecipeData.size();
        }
        return 0;
    }

    public List<Recipe> getList() {
        return mRecipeData;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTv;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.text_view_recipe);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCallback.onClick(getAdapterPosition());
        }
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(int pos);
    }
}
