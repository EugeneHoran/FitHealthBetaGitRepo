package com.eugene.fithealthmaingit.UI.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeHolder;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeHolderAdapter;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeItems;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeItemsAdapter;
import com.eugene.fithealthmaingit.MainActivityController;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.ChooseAddMealActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

public class RecipeActivity extends AppCompatActivity {
    int mId;
    String mealType;
    private EditText recipeName;
    ListView listRecipes;
    private LogRecipeHolder recipe;
    LogRecipeItemsAdapter logRecipeItemsAdapter;
    LogRecipeHolderAdapter logRecipeHolderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Bundle b = new Bundle();
        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getIntExtra(Globals.MEAL_ID, 0);
            mealType = intent.getStringExtra(Globals.MEAL_TYPE);
        }
        recipeName = (EditText) findViewById(R.id.recipeName);
        toolbar();
        RecipeList();
        recipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeName.setCursorVisible(true);
            }
        });
        recipeName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    recipeName.setCursorVisible(false);
                }
                return false;
            }
        });
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeName.getText().toString().trim().length() != 0) {
                    recipe.setMealName(recipeName.getText().toString());
                }
                recipe.edit();
                Intent i = new Intent(RecipeActivity.this, RecipeManualEntry.class);
                i.putExtra(Globals.MEAL_ID, mId);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                startActivity(i);
            }
        });
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeName.getText().toString().trim().length() != 0) {
                    recipe.setMealName(recipeName.getText().toString());
                }
                recipe.edit();
                Intent i = new Intent(RecipeActivity.this, RecipeAddSearchItem.class);
                i.putExtra(Globals.MEAL_ID, mId);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                startActivity(i);
            }
        });
    }

    private void RecipeList() {
        // Recipe Holder Adapter filtered by Unique ID, can only have 1 item at position 0
        logRecipeHolderAdapter = new LogRecipeHolderAdapter(this, 0, LogRecipeHolder.logsById(mId));
        recipe = logRecipeHolderAdapter.getItem(0);

        if (!recipe.getMealName().equals("Recipe")) {
            recipeName.setText(recipe.getMealName());
        }

        listRecipes = (ListView) findViewById(R.id.listRecipes);
        logRecipeItemsAdapter = new LogRecipeItemsAdapter(this, 0, LogRecipeItems.logsById(mId));
        listRecipes.setAdapter(logRecipeItemsAdapter);

    }

    private void toolbar() {
        Toolbar toolbar_Recipe = (Toolbar) findViewById(R.id.toolbar_Recipe);
        toolbar_Recipe.setTitle("Create Recipe");
        toolbar_Recipe.inflateMenu(R.menu.menu_recipe);
        toolbar_Recipe.setNavigationIcon(R.mipmap.ic_close_white);
        toolbar_Recipe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logRecipeItemsAdapter.getCount() == 0) {
                    recipe.delete();
                } else {
                    if (recipeName.getText().toString().trim().length() != 0) {
                        recipe.setMealName(recipeName.getText().toString());
                        recipe.edit();
                    }
                }

                Intent i = new Intent(RecipeActivity.this, ChooseAddMealActivity.class);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                i.putExtra("PAGE", 2);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        toolbar_Recipe.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_save) {
                    setRecipe();
                    Intent i = new Intent(RecipeActivity.this, MainActivityController.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.action_delete) {
                    recipe.delete();
                    Intent i = new Intent(RecipeActivity.this, ChooseAddMealActivity.class);
                    i.putExtra(Globals.MEAL_TYPE, mealType);
                    i.putExtra("PAGE", 2);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                return false;
            }
        });
    }

    int mAllCaloriesConsumed = 0;
    int mAllFatConsumed = 0;
    int mAllCarbsConsumed = 0;
    int mAllSaturatedFatCount = 0;
    int mAllCholesterolCount = 0;
    int mAllSodiumCount = 0;
    int mAllFiberCount = 0;
    int mAllSugarCount = 0;
    int mAllProteinCount = 0;
    int mAllVitACount = 0;
    int mAllVitCCount = 0;
    int mAllCalciumCount = 0;
    int mAllIronCount = 0;

    private void setRecipe() {

        for (LogRecipeItems logRecipe : logRecipeItemsAdapter.getLogs()) {
            mAllCaloriesConsumed += logRecipe.getCalorieCount();
            mAllFatConsumed += logRecipe.getFatCount();
            mAllCarbsConsumed += logRecipe.getCarbCount();
            mAllProteinCount += logRecipe.getProteinCount();
            mAllSaturatedFatCount += logRecipe.getSaturatedFat();
            mAllCholesterolCount += logRecipe.getCholesterol();
            mAllSodiumCount += logRecipe.getSodium();
            mAllFiberCount += logRecipe.getFiber();
            mAllSugarCount += logRecipe.getSugars();
            mAllVitACount += logRecipe.getVitA();
            mAllVitCCount += logRecipe.getVitC();
            mAllCalciumCount += logRecipe.getCalcium();
            mAllIronCount += logRecipe.getIron();
        }

        LogMeal logMeal = new LogMeal();
        if (recipeName.getText().toString().trim().length() != 0) {
            recipe.setMealName(recipeName.getText().toString());
            logMeal.setMealName(recipeName.getText().toString());
        } else {
            logMeal.setMealName("Recipe");
        }

        recipe.setCalorieCount(Double.valueOf(mAllCaloriesConsumed));
        recipe.setFatCount(Double.valueOf(mAllFatConsumed));
        recipe.setCarbCount(Double.valueOf(mAllCarbsConsumed));
        recipe.setProteinCount(Double.valueOf(mAllProteinCount));
        recipe.setSaturatedFat(Double.valueOf(mAllSaturatedFatCount));
        recipe.setCholesterol(Double.valueOf(mAllCholesterolCount));
        recipe.setSodium(Double.valueOf(mAllSodiumCount));
        recipe.setFiber(Double.valueOf(mAllFiberCount));
        recipe.setSugars(Double.valueOf(mAllSugarCount));
        recipe.setVitA(Double.valueOf(mAllVitACount));
        recipe.setVitC(Double.valueOf(mAllVitCCount));
        recipe.setCalcium(Double.valueOf(mAllCalciumCount));
        recipe.setIron(Double.valueOf(mAllIronCount));
        recipe.edit();


        logMeal.setDate(new Date());
        logMeal.setReciipe("Recipe");
        logMeal.setServingSize(1);
        logMeal.setMealServing("Serving");
        logMeal.setCalorieCount(Double.valueOf(mAllCaloriesConsumed));
        logMeal.setFatCount(Double.valueOf(mAllFatConsumed));
        logMeal.setCarbCount(Double.valueOf(mAllCarbsConsumed));
        logMeal.setProteinCount(Double.valueOf(mAllProteinCount));
        logMeal.setSaturatedFat(Double.valueOf(mAllSaturatedFatCount));
        logMeal.setCholesterol(Double.valueOf(mAllCholesterolCount));
        logMeal.setSodium(Double.valueOf(mAllSodiumCount));
        logMeal.setFiber(Double.valueOf(mAllFiberCount));
        logMeal.setSugars(Double.valueOf(mAllSugarCount));
        logMeal.setVitA(Double.valueOf(mAllVitACount));
        logMeal.setVitC(Double.valueOf(mAllVitCCount));
        logMeal.setCalcium(Double.valueOf(mAllCalciumCount));
        logMeal.setIron(Double.valueOf(mAllIronCount));
        logMeal.setMealChoice(mealType);
        logMeal.setRecipeId(String.valueOf(mId));
        logMeal.save();
    }
}
