package com.tapura.kitchenization.network;


import com.tapura.kitchenization.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RESTService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
