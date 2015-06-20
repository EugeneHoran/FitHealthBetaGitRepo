package com.eugene.fithealthmaingit.UI.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

public class RecipeAddSearchItem extends AppCompatActivity implements RecipeSearch.FragmentCallbacks, RecipeSaveSearchItemFragment.FragmentCallbacks {
    String mealType;
    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add_search_item);
        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getIntExtra(Globals.MEAL_ID, 0);
            mealType = intent.getStringExtra(Globals.MEAL_TYPE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new RecipeSearch()).commit();
    }

    @Override
    public void searchFragment(String id, String brand) {
        Fragment addItem = new RecipeSaveSearchItemFragment();
        Bundle b = new Bundle();
        b.putString("ID", id);
        b.putString(Globals.MEAL_BRAND, brand);
        b.putString(Globals.MEAL_TYPE, mealType);
        b.putString("Recipe", mId + "");
        addItem.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, addItem).addToBackStack(null).commit();
    }

    @Override
    public void fromFragment() {

    }
}
