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

package com.eugene.fithealthmaingit.UI.NavFragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Custom.TextViewFont;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLog;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLogAdapter;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;

public class FragmentHealth extends Fragment {

    // Save and load user Info
    private SharedPreferences sharedPreferences;
    DecimalFormat dfW = new DecimalFormat("0");
    TextView mPersonBMR, mCaloriesToMaintain, mCaloriesToLose, mCalorieGoal, weightLossGoal, tvUnderWeight1, tvNormal1, tvOverWeight1, tvObese1, tvVeryObese1;
    TextView tvUnderWeight, tvNormal, tvOverWeight, tvObese, tvVeryObese;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_health, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        findViews();
        loadSavedPreferences();
        return v;
    }

    private void findViews() {
        Toolbar toolbar_journal_health = (Toolbar) v.findViewById(R.id.toolbar_journal_health);
        TextViewFont txtTitle = (TextViewFont) v.findViewById(R.id.txtTitle);
        txtTitle.setText("Health");
        toolbar_journal_health.setNavigationIcon(R.mipmap.ic_menu);
        toolbar_journal_health.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openNavigationDrawer();
            }
        });
        mCalorieGoal = (TextView) v.findViewById(R.id.CalorieGoal1);
        mPersonBMR = (TextView) v.findViewById(R.id.BMR);
        mCaloriesToMaintain = (TextView) v.findViewById(R.id.calorieMaintain);
        weightLossGoal = (TextView) v.findViewById(R.id.weightLossGoal);
        mCaloriesToLose = (TextView) v.findViewById(R.id.CalorieLoss);
        tvUnderWeight1 = (TextView) v.findViewById(R.id.tvUnderWeight1);
        tvNormal1 = (TextView) v.findViewById(R.id.tvNormal1);
        tvOverWeight1 = (TextView) v.findViewById(R.id.tvOverWeight1);
        tvObese1 = (TextView) v.findViewById(R.id.tvObese1);
        tvVeryObese1 = (TextView) v.findViewById(R.id.tvVeryObese1);
        tvUnderWeight = (TextView) v.findViewById(R.id.tvUnderWeight);
        tvNormal = (TextView) v.findViewById(R.id.tvNormal);
        tvOverWeight = (TextView) v.findViewById(R.id.tvOverWeight);
        tvObese = (TextView) v.findViewById(R.id.tvObese);
        tvVeryObese = (TextView) v.findViewById(R.id.tvVeryObese);
    }

    DecimalFormat df = new DecimalFormat("0");
    double weightPerWeek;
    double timeTillGoal;


    private void loadSavedPreferences() {
        // Goal Overview
        weightLossGoal.setText(sharedPreferences.getString(Globals.USER_GOAL, "0"));
        TextView goalWeight = (TextView) v.findViewById(R.id.weightGoal);
        goalWeight.setText(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, "") + " lbs");
        WeightLogAdapter weightLogAdapter = new WeightLogAdapter(getActivity(), 0, WeightLog.all());
        WeightLog weightLogCurrent = weightLogAdapter.getItem(weightLogAdapter.getCount() - 1);
        TextView currentWeight = (TextView) v.findViewById(R.id.weightCurrent);
        currentWeight.setText(df.format(weightLogCurrent.getCurrentWeight()) + " lbs");
        double weightRemaining = weightLogCurrent.getCurrentWeight() - Double.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, ""));
        int goalPosition = Integer.valueOf(sharedPreferences.getString(Globals.USER_WEIGHT_LOSS_GOAL, ""));
        if (goalPosition == 0 || goalPosition == 8) {
            weightPerWeek = 2;
        }
        if (goalPosition == 1 || goalPosition == 7) {
            weightPerWeek = 1.5;
        }
        if (goalPosition == 2 || goalPosition == 6) {
            weightPerWeek = 1;
        }
        if (goalPosition == 3 || goalPosition == 5) {
            weightPerWeek = .5;
        }
        if (goalPosition == 4) {
            weightPerWeek = 0;
        }
        timeTillGoal = weightRemaining / weightPerWeek;
        TextView timeRem = (TextView) v.findViewById(R.id.timeGoal);
        timeRem.setText(df.format(timeTillGoal) + " week(s)");

        // Calorie Goal
        mPersonBMR.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_BASAL_METABOLIC_RATE, "0"))) + " Cal");
        mCaloriesToMaintain.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_MAINTAIN_WEIGHT, "0"))) + " Cal");
        mCaloriesToLose.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_GIVE_UP, "0"))) + " Cal");
        mCalorieGoal.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, "0"))) + " Cal");

        // BMI
        double mBmi = Double.valueOf(sharedPreferences.getString(Globals.USER_BODY_MASS_INDEX, "0"));
        tvUnderWeight1.setText("19 <");
        if (mBmi <= 19) {
            tvUnderWeight1.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvUnderWeight1.setTypeface(null, Typeface.BOLD);
            tvUnderWeight.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvUnderWeight.setTypeface(null, Typeface.BOLD);
        }
        if (mBmi > 19 && mBmi <= 24) {
            tvNormal1.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvNormal1.setTypeface(null, Typeface.BOLD);
            tvNormal.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvNormal.setTypeface(null, Typeface.BOLD);
        }
        if (mBmi > 24 && mBmi <= 30) {
            tvOverWeight1.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvOverWeight1.setTypeface(null, Typeface.BOLD);
            tvOverWeight.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvOverWeight.setTypeface(null, Typeface.BOLD);
        }
        if (mBmi > 30 && mBmi <= 40) {
            tvObese1.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvObese1.setTypeface(null, Typeface.BOLD);
            tvObese.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvObese.setTypeface(null, Typeface.BOLD);
        }
        if (mBmi > 40) {
            tvVeryObese1.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvVeryObese1.setTypeface(null, Typeface.BOLD);
            tvVeryObese.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
            tvVeryObese.setTypeface(null, Typeface.BOLD);
        }
    }

    /**
     * Interface to communicate to the parent activity (MainActivity.java)
     */
    private FragmentCallbacks mCallbacks;


    public interface FragmentCallbacks {
        void openNavigationDrawer();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment One.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}

