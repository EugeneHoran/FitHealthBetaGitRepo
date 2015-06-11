package com.eugene.fithealthmaingit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual.LogManual;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;
import java.util.Random;


public class ManualEntryFragment extends Fragment {


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_manual_entry, container, false);
        // Toolbar
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) // Get meal_type from activity to set meal.
            mealType = extras.getString(Globals.MEAL_TYPE);
        toolbar_manual = (Toolbar) v.findViewById(R.id.toolbar_manual);
        toolbar_manual.setTitle("Manual Entry");
        toolbar_manual.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar_manual.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        toolbar_manual.inflateMenu(R.menu.menu_user_info);
        toolbar_manual.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                if (menuItem == R.id.action_save) {
                    SaveMeal();
                    Intent intent = new Intent(getActivity(), ChooseAddMealActivity.class);
                    intent.putExtra(Globals.MEAL_TYPE, mealType);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
        return v;
    }

    private void SaveMeal() {
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
        LogManual logMeals = new LogManual();
        logMeals.setMealName(sMealName);
        Random r = new Random();
        int i1 = r.nextInt(10000 - 1) + 1;
        logMeals.setMealId(String.valueOf(i1));
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
        logMeals.setBrand(sMealBrand);
        logMeals.setServingSize(1);
        logMeals.setMealServing(sServingName);
        logMeals.setDate(new Date());
        logMeals.save();
    }

    private Double checkForNull(String s) {
        if (s.trim().length() == 0) {
            return (double) 0;
        } else {
            return Double.valueOf(s);
        }
    }
}