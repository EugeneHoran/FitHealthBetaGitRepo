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
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.DateCompare;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Nutrition Fragment
 * Displays all of the nutrition values consumed based on Date
 */
public class FragmentNutrition extends Fragment {
    private FragmentCallbacks mCallbacks;
    private Toolbar mToolbarDaily;
    private DecimalFormat df = new DecimalFormat("0");
    private DecimalFormat dfT = new DecimalFormat("0.0");
    private Date mDate = new Date();
    private Date mDateDoesNotChange = new Date();
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDate = (Date) savedInstanceState.getSerializable(Globals.JOURNAL_DATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Globals.JOURNAL_DATE, mDate);
    }

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_nutrition, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mToolbarDaily = (Toolbar) v.findViewById(R.id.toolbar_daily);
        mToolbarDaily.setNavigationIcon(R.mipmap.ic_menu);
        mToolbarDaily.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openNavigationDrawer();
            }
        });
        mToolbarDaily.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.action_today) {
                    mDate = mDateDoesNotChange;
                    handleDateChanges(mDate);
                    handleTextViews(mDate);
                    DateCompare.lastX = 0;
                }

                return false;
            }
        });
        ImageView mDatePrev = (ImageView) v.findViewById(R.id.datePrevious);
        ImageView mDateNext = (ImageView) v.findViewById(R.id.dateNext);
        mDatePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = DateCompare.previousDate(mDate);
                handleDateChanges(mDate);
                handleTextViews(mDate);
            }
        });
        mDateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = DateCompare.nextDate(mDate);
                handleDateChanges(mDate);
                handleTextViews(mDate);
            }
        });
        handleTextViews(mDate);
        handleDateChanges(mDate);

        return v;
    }

    /**
     * Update text and menu based on date change
     *
     * @param date current Date or updated date
     */
    private void handleDateChanges(Date date) {
        TextView mDateText = (TextView) v.findViewById(R.id.tbDate);
        if (DateCompare.areDatesEqual(mDateDoesNotChange, date)) { // Are Dates Equal Today
            mToolbarDaily.getMenu().clear();
            mDateText.setText("Today");
        } else if (DateCompare.areDatesEqualYesterday(mDateDoesNotChange, date)) {  // Are Dates Equal Yesterday
            mToolbarDaily.getMenu().clear();
            mToolbarDaily.inflateMenu(R.menu.menu_today);
            mDateText.setText("Yesterday");
        } else if (DateCompare.areDatesEqualTomorrow(mDateDoesNotChange, date)) {  // Are Dates Equal Yesterday
            mToolbarDaily.getMenu().clear();
            mToolbarDaily.inflateMenu(R.menu.menu_today);
            mDateText.setText("Tomorrow");
        } else {
            mToolbarDaily.getMenu().clear();
            mToolbarDaily.inflateMenu(R.menu.menu_today);
            mDateText.setText(DateFormat.format("MMM d, EE", date));
        }
    }

    /**
     * Set nutrition information based on date
     *
     * @param d current Date or updated date
     */
    private void handleTextViews(Date d) {
        LogAdapterAll logMealArrayAdapter = new LogAdapterAll(getActivity(), 0, LogMeal.logsByDate(d));
        int breakfastCalorieCount = 0;
        int breakFatCount = 0;
        int breakfastSaturatedFatCount = 0;
        int breakCholesterolCount = 0;
        int breakSodiumCount = 0;
        int breakCarbsCount = 0;
        int breakFiberCount = 0;
        int breakSugarCount = 0;
        int breakProteinCount = 0;
        int breakVitACount = 0;
        int breakVitCCount = 0;
        int breakCalciumCount = 0;
        int breakIronCount = 0;
        for (LogMeal log : logMealArrayAdapter.getLogs()) {
            breakfastCalorieCount += log.getCalorieCount();
            breakFatCount += log.getFatCount();
            breakfastSaturatedFatCount += log.getSaturatedFat();
            breakCholesterolCount += log.getCholesterol();
            breakSodiumCount += log.getSodium();
            breakCarbsCount += log.getCarbCount();
            breakFiberCount += log.getFiber();
            breakSugarCount += log.getSugars();
            breakProteinCount += log.getProteinCount();
            breakVitACount += log.getVitA();
            breakVitCCount += log.getVitC();
            breakCalciumCount += log.getCalcium();
            breakIronCount += log.getIron();
        }
        // All TextViews.  Formatted like this because they are extremely local and are not changed after the OnCreate
        //   ((TextView) v.findViewById(R.id.servingCOnsumed)).setText(df.format(mLogMeal.getServingSize()));
        ((TextView) v.findViewById(R.id.caloriesNutrition)).setText(df.format(breakfastCalorieCount));
        ((TextView) v.findViewById(R.id.fatNutrition)).setText(df.format(breakFatCount) + " g");
        ((TextView) v.findViewById(R.id.saturatedFat)).setText(df.format(breakfastSaturatedFatCount) + " g");
        ((TextView) v.findViewById(R.id.cholesterol)).setText(df.format(breakCholesterolCount) + " mg");
        ((TextView) v.findViewById(R.id.sodium)).setText(df.format(breakSodiumCount) + " mg");
        ((TextView) v.findViewById(R.id.carbohydratesNutrition)).setText(df.format(breakCarbsCount) + " g");
        ((TextView) v.findViewById(R.id.fiber)).setText(df.format(breakFiberCount) + " g");
        ((TextView) v.findViewById(R.id.sugars)).setText(df.format(breakSugarCount) + " g");
        ((TextView) v.findViewById(R.id.proteinNutrition)).setText(df.format(breakProteinCount) + " g");
        ((TextView) v.findViewById(R.id.vitiminA)).setText(df.format(breakVitACount) + "%");
        ((TextView) v.findViewById(R.id.vitiminC)).setText(df.format(breakVitCCount) + "%");
        ((TextView) v.findViewById(R.id.calcium)).setText(df.format(breakCalciumCount) + "%");
        ((TextView) v.findViewById(R.id.iron)).setText(df.format(breakIronCount) + "%");

               /*
        Fat: 1 gram = 9 calories
        Protein: 1 gram = 4 calories
        Carbohydrates: 1 gram = 4 calories
        Alcohol: 1 gram = 7 calories
         */
        TextView fat = (TextView) v.findViewById(R.id.fat);
        TextView carb = (TextView) v.findViewById(R.id.carb);
        TextView pro = (TextView) v.findViewById(R.id.pro);

        PieGraph pg = (PieGraph) v.findViewById(R.id.graph);
        pg.removeSlices();
        // Fat
        int fatCount = breakFatCount * 9;
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        if (fatCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(fatCount);
        }
        pg.addSlice(slice);

        // Carbs
        int carbCount = breakCarbsCount * 4;
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        if (carbCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(carbCount);
        }
        pg.addSlice(slice);

        // Pro
        int proCount = breakProteinCount * 4;
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        if (proCount == 0) {
            slice.setValue(1);
        } else {
            slice.setValue(proCount);
        }
        pg.addSlice(slice);


        double percentage = fatCount + carbCount + proCount;

        double fatPer = (fatCount / percentage) * 100;
        double carbPer = (carbCount / percentage) * 100;
        double proPer = (proCount / percentage) * 100;

        boolean fatNaN = fatPer != fatPer;
        boolean carbNaN = carbPer != carbPer;
        boolean proNaN = proPer != proPer;
        if (fatNaN == true) {
            fatPer = 0;
        }
        if (carbNaN == true) {
            carbPer = 0;
        }
        if (proNaN == true) {
            proPer = 0;
        }
        fat.setText(dfT.format(fatPer) + "%");
        carb.setText(dfT.format(carbPer) + "%");
        pro.setText(dfT.format(proPer) + "%");


        // Bar Graph
        double FatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double CarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double ProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));

        ArrayList<Bar> points = new ArrayList<Bar>();

        // Fat
        Bar dd = new Bar();
        dd.setColor(Color.parseColor("#99CC00"));
        dd.setValue(breakFatCount);

        Bar d2 = new Bar();
        d2.setColor(Color.parseColor("#FFBB33"));
        d2.setValue(Integer.valueOf(df.format(FatGoal)));

        // Carbs
        Bar dd1 = new Bar();
        dd1.setColor(Color.parseColor("#99CC00"));
        dd1.setValue(breakCarbsCount / 2);


        Bar d21 = new Bar();
        d21.setColor(Color.parseColor("#FFBB33"));
        d21.setValue(Integer.valueOf(df.format(CarbGoal)) / 2);

        // Pro
        Bar dd11 = new Bar();
        dd11.setColor(Color.parseColor("#99CC00"));
        dd11.setValue(breakProteinCount);

        Bar d211 = new Bar();
        d211.setColor(Color.parseColor("#FFBB33"));
        d211.setValue(Integer.valueOf(df.format(ProteinGoal)));


        points.add(dd);
        points.add(d2);
        points.add(dd1);
        points.add(d21);
        points.add(dd11);
        points.add(d211);
        BarGraph g = (BarGraph) v.findViewById(R.id.graphBar);
        g.setShowPopup(false);
        g.setShowBarText(false);
        g.setShowAxis(false);
        g.setBars(points);

        TextView barFat = (TextView) v.findViewById(R.id.barFat);
        TextView barCarb = (TextView) v.findViewById(R.id.barCarb);
        TextView barPro = (TextView) v.findViewById(R.id.barPro);
        barFat.setText(df.format(breakFatCount) + "g : " + df.format(FatGoal) + "g");
        barCarb.setText(df.format(breakCarbsCount) + "g : " + df.format(CarbGoal) + "g");
        barPro.setText(df.format(breakProteinCount) + "g : " + df.format(ProteinGoal) + "g");

        RelativeLayout macrosInfo = (RelativeLayout) v.findViewById(R.id.macrosInfo);
        macrosInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Macros Info")
                    .setPositiveButton("Done", null);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                FrameLayout f1 = new FrameLayout(getActivity());
                f1.addView(inflater.inflate(R.layout.dialog_macros_info, f1, false));
                builder.setView(f1);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

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


