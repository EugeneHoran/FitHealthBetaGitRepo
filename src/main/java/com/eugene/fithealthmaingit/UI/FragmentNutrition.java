package com.eugene.fithealthmaingit.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.DateCompare;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.Date;


public class FragmentNutrition extends Fragment {
    private FragmentCallbacks mCallbacks;
    private DecimalFormat df = new DecimalFormat("0");
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

    Toolbar mToolbarDaily;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_nutrition, container, false);
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


