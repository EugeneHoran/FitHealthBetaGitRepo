package com.eugene.fithealthmaingit.UI.Recipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

public class RecipeManualEntry extends AppCompatActivity {
    private Fragment fragment = null;
    String mealType, food_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_manual_entry);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
            food_id = extras.getString(Globals.MEAL_ID);
            fragment = new RecipeManualEntryFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.containerManual, fragment).commit();

        }
    }
}
