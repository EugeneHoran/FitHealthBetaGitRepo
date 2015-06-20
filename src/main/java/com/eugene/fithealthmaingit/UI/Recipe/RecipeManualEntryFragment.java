package com.eugene.fithealthmaingit.UI.Recipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeHolder;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeHolderAdapter;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeItems;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;
import java.util.Random;

public class RecipeManualEntryFragment extends Fragment {


    private View v;
    private Toolbar toolbar_manual;
    private EditText servingName;
    private EditText mealName;
    private EditText mealCalories;
    private EditText mealFat;
    private EditText mealCarbs;
    private EditText mealProtein;
    private EditText mealSatFat;
    private EditText mealCholesterol;
    private EditText mealSodium;
    private EditText mealFiber;
    private EditText mealSugar;
    private EditText mealA;
    private EditText mealC;
    private EditText mealCalcium;
    private EditText mealIron;
    private EditText mealBrand;


    private String sServingName;
    private String sMealName;
    private String sMealCalories;
    private String sMealFat;
    private String sMealCarbs;
    private String sMealProtein;
    private String sMealSatFat;
    private String sMealCholesterol;
    private String sMealSodium;
    private String sMealFiber;
    private String sMealSugar;
    private String sMealA;
    private String sMealC;
    private String sMealCalcium;
    private String sMealIron;
    private String sMealBrand;
    private String mealType;
    private int mId;
    LogRecipeHolderAdapter logRecipeHolderAdapter;
    LogRecipeHolder recipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_manual_entry, container, false);
        // Toolbar
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
            mId = extras.getInt(Globals.MEAL_ID, 0);
        }
        logRecipeHolderAdapter = new LogRecipeHolderAdapter(getActivity(), 0, LogRecipeHolder.logsById(mId));
        recipe = logRecipeHolderAdapter.getItem(0);
        toolbar_manual = (Toolbar) v.findViewById(R.id.toolbar_manual);
        toolbar_manual.setTitle("Manual Entry");
        toolbar_manual.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar_manual.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(toolbar_manual.getWindowToken(), 0);
                getActivity().finish();
            }
        });
        toolbar_manual.inflateMenu(R.menu.menu_recipe_manual_entry);
        toolbar_manual.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                if (menuItem == R.id.action_save) {
                    updateServing();

                }
                return false;
            }
        });
        // Edit Text
        servingName = (EditText) v.findViewById(R.id.servingName);
        mealName = (EditText) v.findViewById(R.id.mealName);
        mealCalories = (EditText) v.findViewById(R.id.mealCalories);
        mealFat = (EditText) v.findViewById(R.id.mealFat);
        mealCarbs = (EditText) v.findViewById(R.id.mealCarbs);
        mealProtein = (EditText) v.findViewById(R.id.mealProtein);
        mealSatFat = (EditText) v.findViewById(R.id.mealSatFat);
        mealCholesterol = (EditText) v.findViewById(R.id.mealCholesterol);
        mealSodium = (EditText) v.findViewById(R.id.mealSodium);
        mealFiber = (EditText) v.findViewById(R.id.mealFiber);
        mealSugar = (EditText) v.findViewById(R.id.mealSugar);
        mealA = (EditText) v.findViewById(R.id.mealA);
        mealC = (EditText) v.findViewById(R.id.mealC);
        mealCalcium = (EditText) v.findViewById(R.id.mealCalcium);
        mealIron = (EditText) v.findViewById(R.id.mealIron);
        mealBrand = (EditText) v.findViewById(R.id.mealBrand);
        mealBrand.setVisibility(View.GONE);
        return v;
    }

    int servingSize = 1;

    private void updateServing() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Update Serving Size: ");
        alert.setMessage("Servings Consumed");

        final EditText input = new EditText(getActivity());
        input.setText("1");
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.selectAll();
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        alert.setView(input, 64, 0, 64, 0);
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                servingSize = Integer.valueOf(input.getText().toString());
                SaveMeal();
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra(Globals.MEAL_TYPE, mealType);
                intent.putExtra(Globals.MEAL_ID, mId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        alert.setCancelable(false);
        alert.show();
    }


    /**
     * Save new meal to LogManualEntry
     */
    private void SaveMeal() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(toolbar_manual.getWindowToken(), 0);
        // Strings
        sServingName = servingName.getText().toString();
        if (sServingName.trim().equals("")) {
            sServingName = "Serving";
        }
        sMealName = mealName.getText().toString();
        if (sMealName.trim().equals("")) {
            sMealName = "No Name";
        }
        sMealCalories = mealCalories.getText().toString();
        sMealFat = mealFat.getText().toString();
        sMealCarbs = mealCarbs.getText().toString();
        sMealProtein = mealProtein.getText().toString();
        sMealSatFat = mealSatFat.getText().toString();
        sMealCholesterol = mealCholesterol.getText().toString();
        sMealSodium = mealSodium.getText().toString();
        sMealFiber = mealFiber.getText().toString();
        sMealSugar = mealSugar.getText().toString();
        sMealA = mealA.getText().toString();
        sMealC = mealC.getText().toString();
        sMealCalcium = mealCalcium.getText().toString();
        sMealIron = mealIron.getText().toString();
        sMealBrand = mealBrand.getText().toString();
        if (sMealBrand.trim().equals("")) {
            sMealBrand = "No Brand";
        }

        //Save
        LogRecipeItems logMeals = new LogRecipeItems();
        logMeals.setMealName(sMealName);
        Random r = new Random();
        logMeals.setMealId(String.valueOf(mId));
        logMeals.setCalorieCount(checkForNull(sMealCalories));
        logMeals.setFatCount(checkForNull(sMealFat));
        logMeals.setSaturatedFat(checkForNull(sMealSatFat));
        logMeals.setCholesterol(checkForNull(sMealCholesterol));
        logMeals.setSodium(checkForNull(sMealSodium));
        logMeals.setCarbCount(checkForNull(sMealCarbs));
        logMeals.setFiber(checkForNull(sMealFiber));
        logMeals.setSugars(checkForNull(sMealSugar));
        logMeals.setProteinCount(checkForNull(sMealProtein));
        logMeals.setVitA(checkForNull(sMealA));
        logMeals.setVitC(checkForNull(sMealC));
        logMeals.setCalcium(checkForNull(sMealCalcium));
        logMeals.setIron(checkForNull(sMealIron));
        logMeals.setManualEntry("true");
        logMeals.setForeignKey(recipe);
        logMeals.setBrand(sMealBrand);
        logMeals.setServingSize(servingSize);
        logMeals.setMealServing(sServingName);
        logMeals.setDate(new Date());
        logMeals.save();
    }

    private Double checkForNull(String s) {
        if (s.trim().length() == 0) {
            return (double) 0;
        } else {
            return Double.valueOf(s) * servingSize;
        }
    }
}
