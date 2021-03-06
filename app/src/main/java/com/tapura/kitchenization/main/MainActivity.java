package com.tapura.kitchenization.main;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tapura.kitchenization.R;
import com.tapura.kitchenization.details.StepListActivity;
import com.tapura.kitchenization.model.Recipe;
import com.tapura.kitchenization.network.RESTService;
import com.tapura.kitchenization.network.RESTServiceBuilder;
import com.tapura.kitchenization.widget.IngredientWidget;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tapura.kitchenization.details.StepListActivity.RECIPE_KEY;

public class MainActivity extends AppCompatActivity implements Callback<List<Recipe>>, RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String RECIPE_LIST_KEY = "recipe_list_key";
    private RecipeAdapter mAdapter;
    private RESTService mService;
    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recipesGrid = findViewById(R.id.recycler_view_recipes);

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);

        mAdapter = new RecipeAdapter(this, this);

        recipesGrid.setAdapter(mAdapter);
        recipesGrid.setLayoutManager(layoutManager);
        recipesGrid.setHasFixedSize(true);

        mService = RESTServiceBuilder.build(this);

        if (savedInstanceState != null) {
            List<Recipe> savedList = Parcels.unwrap(savedInstanceState.getParcelable(RECIPE_LIST_KEY));
            if (savedList != null) {
                mAdapter.setRecipeList(savedList);
            } else {
                mService.getRecipes().enqueue(this);
            }
        } else {
            mService.getRecipes().enqueue(this);
        }

        setupWidget(getIntent());
    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
            mAdapter.setRecipeList(response.body());
            Log.d(TAG, "onResponse: SUCCESSFUL!");
        } else {
            Log.d(TAG, "onResponse: OH NO!");
        }
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        Log.d(TAG, "onFailure" + t.getMessage());
    }

    @Override
    public void onClick(int pos) {
        //Toast.makeText(this, mAdapter.getList().get(pos).getName(), Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, StepListActivity.class);
        //intent.putExtra(RECIPE_KEY, Parcels.wrap(mAdapter.getList().get(pos)));
        //startActivity(intent);
        if (mWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Intent intent = new Intent(this, StepListActivity.class);
            intent.putExtra(RECIPE_KEY, Parcels.wrap(mAdapter.getList().get(pos)));

            startActivity(intent);
        } else {

            IngredientWidget.updateAppWidget(getApplicationContext(),
                    AppWidgetManager.getInstance(getApplicationContext()), mWidgetId, mAdapter.getList().get(pos));

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_LIST_KEY, Parcels.wrap(mAdapter.getList()));
        super.onSaveInstanceState(outState);
    }

    public void setupWidget(Intent intent) {
        mWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        if (AppWidgetManager.ACTION_APPWIDGET_CONFIGURE.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            if (extras != null) {
                mWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            }
        }
    }
}
