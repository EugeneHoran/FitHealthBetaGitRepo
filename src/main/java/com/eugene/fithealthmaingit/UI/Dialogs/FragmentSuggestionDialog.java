package com.eugene.fithealthmaingit.UI.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.Date;

public class FragmentSuggestionDialog extends DialogFragment {
    public static final String TAG = "suggestion_dialog_fragment";
    private View v;
    private SharedPreferences sharedPreferences;
    private DecimalFormat dfW = new DecimalFormat("0");
    private String mMealType;
    private Date mDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMealType = bundle.getString(Globals.MEAL_TYPE);
            mDate = (Date) bundle.getSerializable(Globals.SUGGESTION_DATE);
        }
        v = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_suggestion, null);
        findViews();

        // alert Dialog builder implementation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setNegativeButton("Done", null);
        return builder.create();
    }

    private void findViews() {
        TextView mMeal = (TextView) v.findViewById(R.id.meal);
        mMeal.setText(mMealType + " Suggestion");
        double CalorieGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, ""));
        double mealCalGOal = CalorieGoal / 4;
        double FatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double mealFatGOal = FatGoal / 4;
        double CarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double mealCarbGOal = CarbGoal / 4;
        double ProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));
        double mealProGOal = ProteinGoal / 4;

        TextView mCalGoal = (TextView) v.findViewById(R.id.calGoal);
        mCalGoal.setText(dfW.format(mealCalGOal) + " cal");
        TextView mFatGoal = (TextView) v.findViewById(R.id.fatGoal);
        mFatGoal.setText(dfW.format(mealFatGOal) + " g");
        TextView mCarbGoal = (TextView) v.findViewById(R.id.carbGoal);
        mCarbGoal.setText(dfW.format(mealCarbGOal) + " g");
        TextView mProGoal = (TextView) v.findViewById(R.id.proGoal);
        mProGoal.setText(dfW.format(mealProGOal) + " g");

        LogAdapterAll mLogAdapterAll = new LogAdapterAll(getActivity(), 0, LogMeal.logSortByMealChoice(mMealType, mDate));
        double AllCaloriesConsumed = 0;
        double AllFatConsumed = 0;
        double AllCarbsConsumed = 0;
        double AllProteinConsumed = 0;
        for (LogMeal logMeal : mLogAdapterAll.getLogs()) {
            AllCaloriesConsumed += logMeal.getCalorieCount();
            AllFatConsumed += logMeal.getFatCount();
            AllCarbsConsumed += logMeal.getCarbCount();
            AllProteinConsumed += logMeal.getProteinCount();
        }
        double CaloriesRemain = mealCalGOal - AllCaloriesConsumed;
        double FatRemain = mealFatGOal - AllFatConsumed;
        double CarbsRemain = mealCarbGOal - AllCarbsConsumed;
        double ProRemain = mealProGOal - AllProteinConsumed;

        TextView mCalRem = (TextView) v.findViewById(R.id.calRem);
        mCalRem.setText(dfW.format(CaloriesRemain) + " cal");
        TextView mFatRem = (TextView) v.findViewById(R.id.fatRem);
        mFatRem.setText(dfW.format(FatRemain) + " g");
        TextView mCarbRem = (TextView) v.findViewById(R.id.carbRem);
        mCarbRem.setText(dfW.format(CarbsRemain) + " g");
        TextView mProRem = (TextView) v.findViewById(R.id.proRem);
        mProRem.setText(dfW.format(ProRemain) + " g");
    }
}
