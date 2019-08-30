package com.example.bakingapp.utils;

import com.example.bakingapp.model.RecipeResponse;
import com.example.bakingapp.retrofitInterfaces.RecipeListService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitUtils {

    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static Retrofit buildRecipeUrl(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Call<RecipeResponse> loadRecipies(){
        Retrofit retrofit = buildRecipeUrl();
        RecipeListService recipeListService = retrofit.create(RecipeListService.class);
        return recipeListService.getRecipeResponse();
    }
}