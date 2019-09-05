package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.AppExecutors;
import com.example.bakingapp.BaseApp;
import com.example.bakingapp.R;
import com.example.bakingapp.data.FavoritesDatabase;
import com.example.bakingapp.data.RecipeRepository;
import com.example.bakingapp.model.Ingredients;
import com.example.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetItemFactory(this.getApplicationContext(), intent);
    }
    class IngredientsWidgetItemFactory implements RemoteViewsFactory{
        private Context mContext;
        private int mAppWidgetId;
        private List<Recipe> mRecipes;
        private ArrayList<Ingredients> mIngredients = new ArrayList<>();
        private RecipeRepository mRecipeRepository;

        IngredientsWidgetItemFactory(Context context, Intent intent){
            mContext = context;
            this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        @Override
        public void onCreate() {
            mRecipeRepository = ((BaseApp) getApplication()).getRepository();
        }

        @Override
        public void onDataSetChanged() {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mRecipes = mRecipeRepository.loadRecipesFromWidget();
                }
            });
        }

        @Override
        public void onDestroy() {
            mRecipeRepository = null;
            mRecipes.clear();
            mIngredients.clear();
        }

        @Override
        public int getCount() {
            return mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if(mRecipes == null || mRecipes.size() == 0) return null;

            mIngredients = mRecipes.get(i).getIngredients();
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredients_widget);
            String ingredientString = mIngredients.get(i).getIngredient();
            String quantityString = Float.toString(mIngredients.get(i).getQuantity()) + mIngredients.get(i).getMeasure();
            views.setTextViewText(R.id.tv_widget_ingredient, ingredientString);
            views.setTextViewText(R.id.tv_widget_quantity, quantityString);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
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
}
