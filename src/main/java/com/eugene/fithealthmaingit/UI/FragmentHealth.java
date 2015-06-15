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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;

public class FragmentHealth extends Fragment {

    // Save and load user Info
    private SharedPreferences sharedPreferences;
    DecimalFormat dfW = new DecimalFormat("0");
    TextView mPersonBMR, mCaloriesToMaintain, mCaloriesToLose, mCalorieGoal, weightLossGoal, tvUnderWeight1, tvNormal1, tvOverWeight1, tvObese1, tvVeryObese1;
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
        toolbar_journal_health.setTitle("Your Health");
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
    }

    private void loadSavedPreferences() {
        weightLossGoal.setText(sharedPreferences.getString(Globals.USER_GOAL, "0"));
        mPersonBMR.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_BASAL_METABOLIC_RATE, "0"))) + " Cal");
        mCaloriesToMaintain.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_MAINTAIN_WEIGHT, "0"))) + " Cal");
        mCaloriesToLose.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_GIVE_UP, "0"))) + " Cal");
        mCalorieGoal.setText(dfW.format(Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, "0"))) + " Cal");
        double mBmi = Double.valueOf(sharedPreferences.getString(Globals.USER_BODY_MASS_INDEX, "0"));
        tvUnderWeight1.setText("19 <");
        if (mBmi <= 19) {
            tvUnderWeight1.setTextColor(getActivity().getResources().getColor(R.color.primary));
        }
        if (mBmi > 19 && mBmi <= 24) {
            tvNormal1.setTextColor(getActivity().getResources().getColor(R.color.primary));
        }
        if (mBmi > 24 && mBmi <= 30) {
            tvOverWeight1.setTextColor(getActivity().getResources().getColor(R.color.primary));
        }
        if (mBmi > 30 && mBmi <= 40) {
            tvObese1.setTextColor(getActivity().getResources().getColor(R.color.primary));
        }
        if (mBmi > 40) {
            tvVeryObese1.setTextColor(getActivity().getResources().getColor(R.color.primary));
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

