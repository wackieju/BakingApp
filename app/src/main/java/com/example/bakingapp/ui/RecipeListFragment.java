package com.example.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.ui.adapterrs.RecipeListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment {

    private RecipeListAdapter mRecipeListAdapter;
    private TextView mErrorTextView;

    OnRecipeListClickListener mRecipeListCallback;

    public interface OnRecipeListClickListener {
        void onRecipeSelected(int position);
    }

    public RecipeListFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mRecipeListCallback = (OnRecipeListClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                     " Must implement OnRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView recipeListRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view_recipe_list);
        mErrorTextView = rootView.findViewById(R.id.tv_error);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeListRecycler.setLayoutManager(layoutManager);

        mRecipeListAdapter = new RecipeListAdapter();
        recipeListRecycler.setAdapter(mRecipeListAdapter);

        loadRecipesFromJSON();


        return rootView;
    }

    private void loadRecipesFromJSON() {
        RecipeRepository repository = RecipeRepository.getInstance();
        Call<List<Recipe>> responseCall = repository.getRecipiesFromJSON();

        responseCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(!response.isSuccessful()){
                    showError();
                    return;
                }
                ArrayList<Recipe> recipes;
                if(response.body() != null){
                    recipes = (ArrayList<Recipe>) response.body();
                    mRecipeListAdapter.setRecipes(recipes);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        mErrorTextView.setText(getString(R.string.error_message));
    }
}
