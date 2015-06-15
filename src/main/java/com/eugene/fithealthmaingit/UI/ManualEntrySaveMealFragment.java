/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eugene.fithealthmaingit.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual.LogAdapterManual;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual.LogManual;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterPrevention;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.FatSecretSearchAndGet.FatSecretGetMethod;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManualEntrySaveMealFragment extends Fragment {
    DecimalFormat df = new DecimalFormat("0.0");
    DecimalFormat dfW = new DecimalFormat("0");
    // Widgets
    private Toolbar mToolbar;
    private TextView mViewSevingSize;
    private TextView mServingg;
    private TextView mAmountPerServing;
    private TextView mServingSizeUpdated;
    private TextView mFoodCal;
    private TextView mCalUpdate;
    private TextView mViewFat1;
    private TextView mFattieUpdate;
    private TextView mViewSaturatedFat;
    private TextView mSaturatedFatUpdate;
    private TextView mViewCholesterol;
    private TextView mCholesterolUpdate;
    private TextView mViewSodium;
    private TextView mSodiumUpdate;
    private TextView mViewCarbs1;
    private TextView mCarbUpdate;
    private TextView mViewFiber;
    private TextView mFiberUpdate;
    private TextView mViewSugar;
    private TextView mSugarUpdate;
    private TextView mViewProtien1;
    private TextView mProUpdate;
    private TextView mViewVitA;
    private TextView mVitAUpdate;
    private TextView mViewVitC;
    private TextView mVitCUpdate;
    private TextView mViewCalcium;
    private TextView mCalciumUpdate;
    private TextView mViewIron;
    private TextView mIronUpdate;
    private ProgressBar mPbCal;
    private ProgressBar mPbFat;
    private ProgressBar mPbCarb;
    private ProgressBar mPbPro;

    private Spinner mSpnServings;
    private LinearLayout llVitA,
        llVitC,
        llCalcium,
        llIron,
        llCalories,
        llFat,
        llSat,
        llCholesterol,
        llSodium,
        llCarbs,
        llFiber,
        llSugars,
        llProtein;
    private View vVitA, vVitC, vCalcium, vIron, vFat,
        vSat,
        vCholesterol,
        vSodium,
        vCarbs,
        vFiber,
        vSugar,
        vPro;
    private LogAdapterPrevention mLogAdapterAll;
    // FatSecret method.get
    private FatSecretGetMethod mFatSecretGet;
    String arrayOrJson;
    // Inflate view
    private View v;
    String mealType;
    String food_id;
    private ArrayList<String> mItem;
    ArrayAdapter<String> servingAdapter;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_saved_manual_entry, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
            food_id = extras.getString(Globals.MEAL_ID);
        }
        findViewsById();
        getFood(food_id);
        return v;
    }
    //Declare a int member variable and initialize to 0 (at the top of your class)

    private int mLastSpinnerPosition = 0;

    private void findViewsById() {
        mLogAdapterAll = new LogAdapterPrevention(getActivity(), 0, LogMeal.logsByDate(new Date()));
        mFatSecretGet = new FatSecretGetMethod(); // method.get
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mViewSevingSize = (TextView) v.findViewById(R.id.viewSevingSize);

        mServingg = (TextView) v.findViewById(R.id.servingg);
        mServingg.setText("1");
        mAmountPerServing = (TextView) v.findViewById(R.id.amountPerServing);
        mServingSizeUpdated = (TextView) v.findViewById(R.id.servingSizeUpdated);
        mFoodCal = (TextView) v.findViewById(R.id.foodCal);
        mCalUpdate = (TextView) v.findViewById(R.id.calUpdate);
        mViewFat1 = (TextView) v.findViewById(R.id.viewFat1);
        mFattieUpdate = (TextView) v.findViewById(R.id.fattieUpdate);
        mViewSaturatedFat = (TextView) v.findViewById(R.id.viewSaturatedFat);
        mSaturatedFatUpdate = (TextView) v.findViewById(R.id.saturatedFatUpdate);
        mViewCholesterol = (TextView) v.findViewById(R.id.viewCholesterol);
        mCholesterolUpdate = (TextView) v.findViewById(R.id.cholesterolUpdate);
        mViewSodium = (TextView) v.findViewById(R.id.viewSodium);
        mSodiumUpdate = (TextView) v.findViewById(R.id.sodiumUpdate);
        mViewCarbs1 = (TextView) v.findViewById(R.id.viewCarbs1);
        mCarbUpdate = (TextView) v.findViewById(R.id.carbUpdate);
        mViewFiber = (TextView) v.findViewById(R.id.viewFiber);
        mFiberUpdate = (TextView) v.findViewById(R.id.fiberUpdate);
        mViewSugar = (TextView) v.findViewById(R.id.viewSugar);
        mSugarUpdate = (TextView) v.findViewById(R.id.sugarUpdate);
        mViewProtien1 = (TextView) v.findViewById(R.id.viewProtien1);
        mProUpdate = (TextView) v.findViewById(R.id.proUpdate);
        mViewVitA = (TextView) v.findViewById(R.id.viewVitA);
        mVitAUpdate = (TextView) v.findViewById(R.id.vitAUpdate);
        mViewVitC = (TextView) v.findViewById(R.id.viewVitC);
        mVitCUpdate = (TextView) v.findViewById(R.id.vitCUpdate);
        mViewCalcium = (TextView) v.findViewById(R.id.viewCalcium);
        mCalciumUpdate = (TextView) v.findViewById(R.id.calciumUpdate);
        mViewIron = (TextView) v.findViewById(R.id.viewIron);
        mIronUpdate = (TextView) v.findViewById(R.id.ironUpdate);
        mPbCal = (ProgressBar) v.findViewById(R.id.pbCal);
        mPbFat = (ProgressBar) v.findViewById(R.id.pbFat);
        mPbCarb = (ProgressBar) v.findViewById(R.id.pbCarb);
        mPbPro = (ProgressBar) v.findViewById(R.id.pbPro);
        mItem = new ArrayList<>();
        servingAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mItem);

        llVitA = (LinearLayout) v.findViewById(R.id.llVitA);
        llVitC = (LinearLayout) v.findViewById(R.id.llVitC);
        llCalcium = (LinearLayout) v.findViewById(R.id.llCalcium);
        llIron = (LinearLayout) v.findViewById(R.id.llIron);
        llCalories = (LinearLayout) v.findViewById(R.id.llCalories);
        llFat = (LinearLayout) v.findViewById(R.id.llFat);
        llSat = (LinearLayout) v.findViewById(R.id.llSat);
        llCholesterol = (LinearLayout) v.findViewById(R.id.llCholesterol);
        llSodium = (LinearLayout) v.findViewById(R.id.llSodium);
        llCarbs = (LinearLayout) v.findViewById(R.id.llCarbs);
        llFiber = (LinearLayout) v.findViewById(R.id.llFiber);
        llSugars = (LinearLayout) v.findViewById(R.id.llSugars);
        llProtein = (LinearLayout) v.findViewById(R.id.llProtein);
        vVitA = v.findViewById(R.id.vVitA);
        vVitC = v.findViewById(R.id.vVitC);
        vCalcium = v.findViewById(R.id.vCalcium);
        vIron = v.findViewById(R.id.vIron);
        vFat = v.findViewById(R.id.vFat);
        vSat = v.findViewById(R.id.vSat);
        vCholesterol = v.findViewById(R.id.vCholesterol);
        vSodium = v.findViewById(R.id.vSodium);
        vCarbs = v.findViewById(R.id.vCarbs);
        vFiber = v.findViewById(R.id.vFiber);
        vSugar = v.findViewById(R.id.vSugar);
        vPro = v.findViewById(R.id.vPro);
        updateItems();
    }

    double mCalorieProgress = 0;
    double mFatProgress = 0;
    double mCarbProgress = 0;
    double mProteinProgress = 0;

    private void progressBars() {
        // Nutrition Goals
        double mCalorieGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, ""));
        double mFatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double mCarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double mProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));

        double mAllCaloriesConsumed = 0;
        double mAllFatConsumed = 0;
        double mAllCarbsConsumed = 0;
        double mAllProteinConsumed = 0;
        for (LogMeal logMeal : mLogAdapterAll.getLogs()) {
            mAllCaloriesConsumed += logMeal.getCalorieCount();
            mAllFatConsumed += logMeal.getFatCount();
            mAllCarbsConsumed += logMeal.getCarbCount();
            mAllProteinConsumed += logMeal.getProteinCount();
        }
        // mPbCal, mPbFat, mPbCarb, mPbPro
        mPbCal.setMax(Integer.valueOf(dfW.format(mCalorieGoal)));
        mPbCal.setProgress(Integer.valueOf(dfW.format(mAllCaloriesConsumed)) + Integer.valueOf(dfW.format(mCalorieProgress)));
        mPbCal.setSecondaryProgress(Integer.valueOf(dfW.format(mAllCaloriesConsumed)));

        mPbFat.setMax(Integer.valueOf(dfW.format(mFatGoal)));
        mPbFat.setProgress(Integer.valueOf(dfW.format(mAllFatConsumed)) + Integer.valueOf(dfW.format(mFatProgress)));
        mPbFat.setSecondaryProgress(Integer.valueOf(dfW.format(mAllFatConsumed)));

        mPbCarb.setMax(Integer.valueOf(dfW.format(mCarbGoal)));
        mPbCarb.setProgress(Integer.valueOf(dfW.format(mAllCarbsConsumed)) + Integer.valueOf(dfW.format(mCarbProgress)));
        mPbCarb.setSecondaryProgress(Integer.valueOf(dfW.format(mAllCarbsConsumed)));

        mPbPro.setMax(Integer.valueOf(dfW.format(mProteinGoal)));
        mPbPro.setProgress(Integer.valueOf(dfW.format(mAllProteinConsumed)) + Integer.valueOf(dfW.format(mProteinProgress)));
        mPbPro.setSecondaryProgress(Integer.valueOf(dfW.format(mAllProteinConsumed)));
    }

    private void updateItems() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseAddMealActivity.class);
                intent.putExtra(Globals.MEAL_TYPE, mealType);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        mToolbar.inflateMenu(R.menu.menu_user_info);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_save)
                    saveMeal();
                return false;
            }
        });
        mServingSizeUpdated.setText("1");

        LinearLayout changeServing = (LinearLayout) v.findViewById(R.id.changeServing);
        changeServing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Update Serving Size: ");
                alert.setMessage("Servings Consumed");

                final EditText input = new EditText(getActivity());
                input.setText(mServingg.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.selectAll();
                input.setGravity(Gravity.CENTER_HORIZONTAL);
                alert.setView(input, 64, 0, 64, 0);
                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        double values = Double.valueOf(input.getText().toString());
                        mServingg.setText(df.format(values));
                        mServingSizeUpdated.setText(df.format(values));
                        mCalUpdate.setText(dfW.format(Double.valueOf(mCalories) * values));
                        mCalorieProgress = Double.valueOf(mCalories) * values;
                        mFattieUpdate.setText(df.format(Double.valueOf(mFat) * values));
                        mFatProgress = Double.valueOf(mFat) * values;
                        mSaturatedFatUpdate.setText(df.format(Double.valueOf(mSaturatedFat) * values));
                        mCholesterolUpdate.setText(df.format(Double.valueOf(mCholesterol) * values));
                        mSodiumUpdate.setText(df.format(Double.valueOf(mSodium) * values));
                        mCarbUpdate.setText(df.format(Double.valueOf(mCarbohydrates) * values));
                        mCarbProgress = Double.valueOf(mCarbohydrates) * values;
                        mFiberUpdate.setText(df.format(Double.valueOf(mFiber) * values));
                        mSugarUpdate.setText(df.format(Double.valueOf(mSugar) * values));
                        mProUpdate.setText(df.format(Double.valueOf(mProtein) * values));
                        mProteinProgress = Double.valueOf(mProtein) * values;
                        mVitAUpdate.setText(df.format(Double.valueOf(mVitA) * values));
                        mVitCUpdate.setText(df.format(Double.valueOf(mVitC) * values));
                        mCalciumUpdate.setText(df.format(Double.valueOf(mCalcium) * values));
                        mIronUpdate.setText(df.format(Double.valueOf(mIron) * values));
                        progressBars();
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(input.getWindowToken(), 0);
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
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }

    private String mBrand = null;
    private String mFoodName = null;
    private String mCalories = null;
    private String mCarbohydrates = null;
    private String mProtein = null;
    private String mFat = null;
    private String mSaturatedFat = null;
    private String mCholesterol = null;
    private String mSodium = null;
    private String mFiber = null;
    private String mSugar = null;
    private String mVitA = null;
    private String mVitC = null;
    private String mCalcium = null;
    private String mIron = null;
    private String mServingDescription = null;
    private String mNumberUnits = null;
    private String servingSize = null;

    private void getFood(final String id) {
        LogAdapterManual logAdapterManual = new LogAdapterManual(getActivity(), 0, LogManual.logsById(id), mealType);
        LogManual logManual = logAdapterManual.getItem(0);
        mBrand = logManual.getBrand();
        mFoodName = logManual.getMealName();
        mCalories = String.valueOf(logManual.getCalorieCount());
        mCarbohydrates = String.valueOf(logManual.getCarbCount());
        mProtein = String.valueOf(logManual.getProteinCount());
        mFat = String.valueOf(logManual.getFatCount());
        mSaturatedFat = String.valueOf(logManual.getSaturatedFat());
        mCholesterol = String.valueOf(logManual.getCholesterol());
        mSodium = String.valueOf(logManual.getSodium());
        mFiber = String.valueOf(logManual.getFiber());
        mSugar = String.valueOf(logManual.getSugars());
        mVitA = String.valueOf(logManual.getVitA());
        mVitC = String.valueOf(logManual.getVitC());
        mCalcium = String.valueOf(logManual.getCalcium());
        mIron = String.valueOf(logManual.getIron());
        mServingDescription = String.valueOf(logManual.getMealServing());
        mNumberUnits = String.valueOf(1);
        setItems();
    }

    private void setItems() {
        TextView foodTitle = (TextView) v.findViewById(R.id.foodTitle);
        foodTitle.setText(mFoodName);
        mAmountPerServing.setText(mNumberUnits);
        mFoodCal.setText(mCalories);
        mViewFat1.setText(mFat);
        mViewSaturatedFat.setText(mSaturatedFat);
        mViewCholesterol.setText(mCholesterol);
        mViewSodium.setText(mSodium);
        mViewCarbs1.setText(mCarbohydrates);
        mViewFiber.setText(mFiber);
        mViewSugar.setText(mSugar);
        mViewProtien1.setText(mProtein);
        mViewVitA.setText(mVitA);
        mViewVitC.setText(mVitC);
        mViewCalcium.setText(mCalcium);
        mViewIron.setText(mIron);
        // Updated Values
        mCalUpdate.setText(mCalories);
        mCalorieProgress = Double.valueOf(mCalories);
        mFattieUpdate.setText(mFat);
        mFatProgress = Double.valueOf(mFat);
        mSaturatedFatUpdate.setText(mSaturatedFat);
        mCholesterolUpdate.setText(mCholesterol);
        mSodiumUpdate.setText(mSodium);
        mCarbUpdate.setText(mCarbohydrates);
        mCarbProgress = Double.valueOf(mCarbohydrates);
        mFiberUpdate.setText(mFiber);
        mSugarUpdate.setText(mSugar);
        mProUpdate.setText(mProtein);
        mProteinProgress = Double.valueOf(mProtein);
        mVitAUpdate.setText(mVitA);
        mVitCUpdate.setText(mVitC);
        mCalciumUpdate.setText(mCalcium);
        mIronUpdate.setText(mIron);
        mViewSevingSize.setText("1 " + mServingDescription);
        progressBars();
        /**
         * Check to see if any variables are null
         * If so set their view gone, and set their value to zero
         */
        if (mCalories == null) {
            llCalories.setVisibility(View.GONE);
            mCalories = "0";
        }
        if (mFat == null) {
            llFat.setVisibility(View.GONE);
            vFat.setVisibility(View.GONE);
            mFat = "0";
        }
        if (mSaturatedFat == "0.0") {
            llSat.setVisibility(View.GONE);
            vSat.setVisibility(View.GONE);
            mSaturatedFat = "0";
        }
        if (mCholesterol == "0.0") {
            mCholesterol = "0";
            llCholesterol.setVisibility(View.GONE);
            vCholesterol.setVisibility(View.GONE);
        }
        if (mSodium == "0.0") {
            llSodium.setVisibility(View.GONE);
            vSodium.setVisibility(View.GONE);
            mSodium = "0";
        }
        if (mCarbohydrates == null) {
            llCarbs.setVisibility(View.GONE);
            vCarbs.setVisibility(View.GONE);
            mCarbohydrates = "0";
        }
        if (mFiber == "0.0") {
            mFiber = "0";
            llFiber.setVisibility(View.GONE);
            vFiber.setVisibility(View.GONE);
        }

        if (mSugar == "0.0") {
            llSugars.setVisibility(View.GONE);
            vSugar.setVisibility(View.GONE);
            mSugar = "0";
        }
        if (mProtein == null) {
            mProtein = "0";
            llProtein.setVisibility(View.GONE);
            vPro.setVisibility(View.GONE);
        }
        if (mVitA == "0.0") {
            llVitA.setVisibility(View.GONE);
            vVitA.setVisibility(View.GONE);
            mVitA = "0";
        }
        if (mVitC == "0.0") {
            llVitC.setVisibility(View.GONE);
            vVitC.setVisibility(View.GONE);
            mVitC = "0";
        }
        if (mCalcium == "0.0") {
            llCalcium.setVisibility(View.GONE);
            vCalcium.setVisibility(View.GONE);
            mCalcium = "0";
        }
        if (mIron == "0.0") {
            mIron = "0";
            llIron.setVisibility(View.GONE);
            vIron.setVisibility(View.GONE);
        }
    }

    private void saveMeal() {
        LogMeal logMeals = new LogMeal();
        logMeals.setMealId(food_id);
        logMeals.setMealChoice(mealType);
        logMeals.setMealName(mFoodName);
        if (llCalories.getVisibility() == View.VISIBLE)
            logMeals.setCalorieCount(Double.valueOf(mCalUpdate.getText().toString()));
        if (llFat.getVisibility() == View.VISIBLE)
            logMeals.setFatCount(Double.valueOf(mFattieUpdate.getText().toString()));
        if (llSat.getVisibility() == View.VISIBLE)
            logMeals.setSaturatedFat(Double.valueOf(mSaturatedFatUpdate.getText().toString()));
        if (llCholesterol.getVisibility() == View.VISIBLE)
            logMeals.setCholesterol(Double.valueOf(mCholesterolUpdate.getText().toString()));
        if (llSodium.getVisibility() == View.VISIBLE)
            logMeals.setSodium(Double.valueOf(mSodiumUpdate.getText().toString()));
        if (llCarbs.getVisibility() == View.VISIBLE)
            logMeals.setCarbCount(Double.valueOf(mCarbUpdate.getText().toString()));
        if (llFiber.getVisibility() == View.VISIBLE)
            logMeals.setFiber(Double.valueOf(mFiberUpdate.getText().toString()));
        if (llSugars.getVisibility() == View.VISIBLE)
            logMeals.setSugars(Double.valueOf(mSugarUpdate.getText().toString()));
        if (llProtein.getVisibility() == View.VISIBLE)
            logMeals.setProteinCount(Double.valueOf(mProUpdate.getText().toString()));
        if (llVitC.getVisibility() == View.VISIBLE)
            logMeals.setVitA(Double.valueOf(mVitAUpdate.getText().toString()));
        if (llVitC.getVisibility() == View.VISIBLE)
            logMeals.setVitC(Double.valueOf(mVitCUpdate.getText().toString()));
        if (llCalcium.getVisibility() == View.VISIBLE)
            logMeals.setCalcium(Double.valueOf(mCalciumUpdate.getText().toString()));
        if (llIron.getVisibility() == View.VISIBLE)
            logMeals.setIron(Double.valueOf(mIronUpdate.getText().toString()));

        logMeals.setManualEntry("false");
        logMeals.setBrand(mBrand);
        logMeals.setServingSize(Double.valueOf(mServingg.getText().toString()));
        logMeals.setMealServing("Serving");
        logMeals.setDate(new Date());
        logMeals.save();
        mCallbacks.fromFragment();
    }

    /**
     * Interface to transfer data to MainActivity
     */
    private FragmentCallbacks mCallbacks;

    public interface FragmentCallbacks {
        void fromFragment();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment Three.");
        }
    }
}