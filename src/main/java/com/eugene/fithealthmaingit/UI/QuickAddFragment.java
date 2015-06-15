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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.MainActivityController;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.Date;

public class QuickAddFragment extends Fragment {
    private EditText mMealCalories;
    private EditText mMealName;
    private TextView mTxtCal;
    private EditText mMealFat;
    private TextView mTxtFat;
    private EditText mMealCarbs;
    private TextView mTxtCarb;
    private EditText mMealProtein;
    private TextView mTxtPro;
    private ImageButton mRemoveFive;
    private EditText mServingSize;
    private ImageButton mAddFive;
    private Spinner mServingType;
    private View v;
    DecimalFormat df = new DecimalFormat("0.0");
    String mMealType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_quick_add, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMealType = bundle.getString(Globals.MEAL_TYPE);
        }
        findViews();
        buttons();
        return v;
    }


    double calories = 0, fat = 0, carbohydrates = 0, protein = 0, servingSizeUpdate = 0;

    private void findViews() {
        Toolbar mToolbarQuickAdd = (Toolbar) v.findViewById(R.id.toolbarQuickAdd);
        mToolbarQuickAdd.setTitle("Quick Add");
        mToolbarQuickAdd.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbarQuickAdd.inflateMenu(R.menu.menu_user_info);
        mToolbarQuickAdd.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_save) {
                    if (mMealName.getText().toString().trim().length() > 0
                        && mMealCalories.getText().toString().trim().length() > 0
                        && mServingSize.getText().toString().trim().length() > 0) {
                        LogMeal logMeals = new LogMeal();
                        logMeals.setMealChoice(mMealType);
                        logMeals.setMealName(mMealName.getText().toString());
                        logMeals.setCalorieCount(Double.valueOf(mMealCalories.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                        if (mMealFat.getText().toString().trim().length() > 0)
                            logMeals.setFatCount(Double.valueOf(mMealFat.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                        else
                            logMeals.setFatCount(0);

                        if (mMealCarbs.getText().toString().trim().length() > 0)
                            logMeals.setCarbCount(Double.valueOf(mMealCarbs.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                        else {
                            logMeals.setCarbCount(0);
                        }

                        if (mMealProtein.getText().toString().trim().length() > 0)
                            logMeals.setProteinCount(Double.valueOf(mMealProtein.getText().toString()) * Double.valueOf(mServingSize.getText().toString()));
                        else
                            logMeals.setProteinCount(0);
                        logMeals.setDate(new Date());
                        logMeals.setServingSize(Double.parseDouble(mServingSize.getText().toString()));
                        logMeals.setMealServing(mServingType.getSelectedItem().toString());
                        logMeals.save();
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivityController.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().overridePendingTransition(0, 0);
                        mCallbacks.itemAdded("");
                    } else {
                        if (mMealName.getText().toString().trim().length() == 0)
                            mMealName.setError("Missing Field");
                        if (mMealCalories.getText().toString().trim().length() == 0)
                            mMealCalories.setError("Missing Field");
                    }
                }
                return false;
            }
        });
        mToolbarQuickAdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
            }
        });
        mMealName = (EditText) v.findViewById(R.id.mealName);
        mMealCalories = (EditText) v.findViewById(R.id.mealCalories);
        mTxtCal = (TextView) v.findViewById(R.id.txtCal);
        mMealFat = (EditText) v.findViewById(R.id.mealFat);
        mTxtFat = (TextView) v.findViewById(R.id.txtFat);
        mMealCarbs = (EditText) v.findViewById(R.id.mealCarbs);
        mTxtCarb = (TextView) v.findViewById(R.id.txtCarb);
        mMealProtein = (EditText) v.findViewById(R.id.mealProtein);
        mTxtPro = (TextView) v.findViewById(R.id.txtPro);
        mRemoveFive = (ImageButton) v.findViewById(R.id.removeFive);
        mServingSize = (EditText) v.findViewById(R.id.servingSize);
        mAddFive = (ImageButton) v.findViewById(R.id.addFive);
        mServingType = (Spinner) v.findViewById(R.id.servingType);
        mServingSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                servingSizeUpdate = Double.valueOf(mServingSize.getText().toString());
                if (mMealCalories.getText().toString().trim().length() > 0)
                    calories = Double.valueOf(mMealCalories.getText().toString());
                else
                    calories = 0;

                if (mMealFat.getText().toString().trim().length() > 0)
                    fat = Double.valueOf(mMealFat.getText().toString());
                else
                    fat = 0;

                if (mMealCarbs.getText().toString().trim().length() > 0)
                    carbohydrates = Double.valueOf(mMealCarbs.getText().toString());
                else
                    carbohydrates = 0;

                if (mMealProtein.getText().toString().trim().length() > 0)
                    protein = Double.valueOf(mMealProtein.getText().toString());
                else
                    protein = 0;
                double caloriesUpdated = servingSizeUpdate * calories;
                mTxtCal.setText(df.format(caloriesUpdated) + "");
                double fatUpdated = servingSizeUpdate * fat;
                mTxtFat.setText(df.format(fatUpdated) + "");
                double carbsUpdated = servingSizeUpdate * carbohydrates;
                mTxtCarb.setText(df.format(carbsUpdated) + "");
                double proteinUpdated = servingSizeUpdate * protein;
                mTxtPro.setText(df.format(proteinUpdated) + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void buttons() {
        mRemoveFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServingSize.setText(df.format(Double.parseDouble(mServingSize.getText().toString()) - .1) + "");
            }
        });
        mAddFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServingSize.setText(df.format(Double.parseDouble(mServingSize.getText().toString()) + .1) + "");
            }
        });
    }


    private QuickAddItemAdded mCallbacks;

    public interface QuickAddItemAdded {
        void itemAdded(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (QuickAddItemAdded) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

}
