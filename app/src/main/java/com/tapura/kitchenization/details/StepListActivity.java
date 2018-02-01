package com.tapura.kitchenization.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapura.kitchenization.R;
import com.tapura.kitchenization.ingredients.IngredientsActivity;
import com.tapura.kitchenization.ingredients.IngredientsFragment;
import com.tapura.kitchenization.model.Recipe;
import com.tapura.kitchenization.model.StepsItem;

import org.parceler.Parcels;

import java.util.List;

import static com.tapura.kitchenization.ingredients.IngredientsFragment.RECIPE_INGREDIENT;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe_key";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecipe = Parcels.unwrap(getIntent().getParcelableExtra(RECIPE_KEY));
        toolbar.setTitle(mRecipe.getName());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(mRecipe.getName());
        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.step_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View ingredientView = findViewById(R.id.card_view_ingredient);
        ingredientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    IngredientsFragment fragment = new IngredientsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RECIPE_INGREDIENT, Parcels.wrap(mRecipe));
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(StepListActivity.this, IngredientsActivity.class);
                    intent.putExtra(RECIPE_INGREDIENT, Parcels.wrap(mRecipe));
                    startActivity(intent);
                }
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<StepsItem> steps = mRecipe.getSteps();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(steps));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<StepsItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<StepsItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(Integer.toString(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(StepDetailFragment.ARG_ITEM, Parcels.wrap(holder.mItem));
                        StepDetailFragment fragment = new StepDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, StepDetailActivity.class);
                        intent.putExtra(StepDetailFragment.ARG_ITEM, Parcels.wrap(holder.mItem));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public final TextView mIdView;
            public StepsItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id);
                mContentView = view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
