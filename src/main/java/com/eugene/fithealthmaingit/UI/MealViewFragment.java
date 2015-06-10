package com.eugene.fithealthmaingit.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases.LogFood.LogAdapterPager;
import com.eugene.fithealthmaingit.Databases.LogFood.LogAdapterPrevention;
import com.eugene.fithealthmaingit.Databases.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.Date;

public class MealViewFragment extends Fragment {
    DecimalFormat dfW = new DecimalFormat("0");

    public static MealViewFragment newInstance(int mId) {
        Bundle args = new Bundle();
        args.putSerializable("EXTRA_ID", mId);
        MealViewFragment fragment = new MealViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View v;
    private DecimalFormat df = new DecimalFormat("0");
    private LogMeal mLogMeal;

    /**
     * mLogMealAdapter : filtering the database based on mId.
     * Since id's are unique the adapter will only return on item at index 0
     * mLogMeal : is set to that index of zero returning the correct item.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_meal_view, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogAdapterPager mLogMealAdapter = new LogAdapterPager(getActivity(), 0, LogMeal.logsById(getArguments().getInt("EXTRA_ID")));
        mLogMeal = mLogMealAdapter.getItem(0);
        handleToolbar();
        handleTextViews();
        handleProgressBars();
    }

    private void handleToolbar() {
        TextView mealTime = (TextView) v.findViewById(R.id.mealTime);
        long lStartTime = new Date().getTime();
        long lEndTime = mLogMeal.getDate().getTime();
        long difference = lEndTime - lStartTime;
        int seconds = (int) (difference / 1000) * (-1);
        int minutes = (int) ((difference / (1000 * 60)));
        int min = minutes * -1;
        int hours = (int) ((difference / (1000 * 60 * 60)) % 24);
        int hour = hours * -1;
        if (seconds < 60 && minutes < 60) {
            mealTime.setText("Seconds ago");
        } else if (seconds >= 60 && min < 60 && hour < 1) {
            mealTime.setText(min + "  Minutes Ago");
        } else {
            mealTime.setText(DateFormat.format("MMM d, h:m a", mLogMeal.getDate()));
        }

    }

    private void handleTextViews() {
        // All TextViews.  Formatted like this because they are extremely local and are not changed after the OnCreate
        ((TextView) v.findViewById(R.id.mMealName)).setText(mLogMeal.getMealName());
        ((TextView) v.findViewById(R.id.servingCOnsumed)).setText(df.format(mLogMeal.getServingSize()));
        ((TextView) v.findViewById(R.id.caloriesNutrition)).setText(df.format(mLogMeal.getCalorieCount()));
        ((TextView) v.findViewById(R.id.fatNutrition)).setText(df.format(mLogMeal.getFatCount()) + " g");
        ((TextView) v.findViewById(R.id.saturatedFat)).setText(df.format(mLogMeal.getSaturatedFat()) + " g");
        ((TextView) v.findViewById(R.id.cholesterol)).setText(df.format(mLogMeal.getCholesterol()) + " mg");
        ((TextView) v.findViewById(R.id.sodium)).setText(df.format(mLogMeal.getSodium()) + " mg");
        ((TextView) v.findViewById(R.id.carbohydratesNutrition)).setText(df.format(mLogMeal.getCarbCount()) + " g");
        ((TextView) v.findViewById(R.id.fiber)).setText(df.format(mLogMeal.getFiber()) + " g");
        ((TextView) v.findViewById(R.id.sugars)).setText(df.format(mLogMeal.getSugars()) + " g");
        ((TextView) v.findViewById(R.id.proteinNutrition)).setText(df.format(mLogMeal.getProteinCount()) + " g");
        ((TextView) v.findViewById(R.id.vitiminA)).setText(df.format(mLogMeal.getVitA()) + "%");
        ((TextView) v.findViewById(R.id.vitiminC)).setText(df.format(mLogMeal.getVitC()) + "%");
        ((TextView) v.findViewById(R.id.calcium)).setText(df.format(mLogMeal.getCalcium()) + "%");
        ((TextView) v.findViewById(R.id.iron)).setText(df.format(mLogMeal.getIron()) + "%");
    }

    SharedPreferences sharedPreferences;
    double mCalorieProgress = 0;
    double mFatProgress = 0;
    double mCarbProgress = 0;
    double mProteinProgress = 0;

    private void handleProgressBars() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        LogAdapterPrevention mLogAdapterAll = new LogAdapterPrevention(getActivity(), 0, LogMeal.logsByDate(new Date()));
        ProgressBar mPbCal = (ProgressBar) v.findViewById(R.id.pbCal);
        ProgressBar mPbFat = (ProgressBar) v.findViewById(R.id.pbFat);
        ProgressBar mPbCarb = (ProgressBar) v.findViewById(R.id.pbCarb);
        ProgressBar mPbPro = (ProgressBar) v.findViewById(R.id.pbPro);

        double mCalorieGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, ""));
        double mFatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double mCarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double mProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));

        // mPbCal, mPbFat, mPbCarb, mPbPro
        mPbCal.setMax(Integer.valueOf(dfW.format(mCalorieGoal)));
        mPbCal.setProgress(Integer.valueOf(dfW.format(mLogMeal.getCalorieCount())) + Integer.valueOf(dfW.format(mCalorieProgress)));

        mPbFat.setMax(Integer.valueOf(dfW.format(mFatGoal)));
        mPbFat.setProgress(Integer.valueOf(dfW.format(mLogMeal.getFatCount())) + Integer.valueOf(dfW.format(mFatProgress)));

        mPbCarb.setMax(Integer.valueOf(dfW.format(mCarbGoal)));
        mPbCarb.setProgress(Integer.valueOf(dfW.format(mLogMeal.getCarbCount())) + Integer.valueOf(dfW.format(mCarbProgress)));

        mPbPro.setMax(Integer.valueOf(dfW.format(mProteinGoal)));
        mPbPro.setProgress(Integer.valueOf(dfW.format(mLogMeal.getProteinCount())) + Integer.valueOf(dfW.format(mProteinProgress)));

        TextView cRem = (TextView) v.findViewById(R.id.cRem);
        cRem.setText(dfW.format(mLogMeal.getCalorieCount()) + " / " + dfW.format(mCalorieGoal));

        TextView fRem = (TextView) v.findViewById(R.id.fRem);
        fRem.setText(dfW.format(mLogMeal.getFatCount()) + " / " + dfW.format(mFatGoal));

        TextView carRem = (TextView) v.findViewById(R.id.carRem);
        carRem.setText(dfW.format(mLogMeal.getCarbCount()) + " / " + dfW.format(mCarbGoal));

        TextView proRem = (TextView) v.findViewById(R.id.proRem);
        proRem.setText(dfW.format(mLogMeal.getProteinCount()) + " / " + dfW.format(mProteinGoal));
    }
}
