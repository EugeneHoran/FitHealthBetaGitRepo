package com.eugene.fithealthmaingit.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.eugene.fithealthmaingit.MainActivityController;
import com.eugene.fithealthmaingit.Databases.LogWeight.WeightLog;
import com.eugene.fithealthmaingit.Databases.LogWeight.WeightLogAdapter;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Equations;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

public class UserInformationFragment extends Fragment {

    // Save and load user Info
    private SharedPreferences sharedPreferences;

    // Widgets
    private RadioButton vRbFemale;
    private RadioButton vRbMale;
    private EditText vName;
    private EditText vNpAge;
    private EditText vEtWeight;
    private EditText vEtGoal;
    private EditText vNpFeet;
    private EditText vNpInches;
    private Spinner vSpnActivity;
    private Spinner vWeightLoss;

    private String personSex;


    private AsyncTask<Void, Void, Void> mEquations;

    // Inflate view
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_information, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        findViews();
        loadSavedPreferences();
        saveUserInformation();
        equations();
        return v;
    }

    Toolbar vToolbar;

    private void findViews() {
        vToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        vToolbar.setTitle("User Information");
        vToolbar.inflateMenu(R.menu.menu_user_info);
        WeightLogAdapter weightLogAdapter = new WeightLogAdapter(getActivity(), 0, WeightLog.all());
        if (weightLogAdapter.getCount() == 0) {

        } else {
            if (Globals.USER_INFORMATION_SAVED.equals(sharedPreferences.getString(Globals.IS_USER_INFORMATION_SAVED, ""))) {
                vToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
            }
        }
        vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        vRbFemale = (RadioButton) v.findViewById(R.id.rbFemale);
        vRbMale = (RadioButton) v.findViewById(R.id.rbMale);
        vName = (EditText) v.findViewById(R.id.name);
        vNpAge = (EditText) v.findViewById(R.id.npAge);
        vEtWeight = (EditText) v.findViewById(R.id.etWeight);
        vEtGoal = (EditText) v.findViewById(R.id.etGoal);
        vNpFeet = (EditText) v.findViewById(R.id.npFeet);
        vNpInches = (EditText) v.findViewById(R.id.npInches);
        vSpnActivity = (Spinner) v.findViewById(R.id.spnActivity);
        vWeightLoss = (Spinner) v.findViewById(R.id.weightCurrent);

    }

    private void saveUserInformation() {
        vToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_save) {
                    if (vName.getText().toString().trim().length() > 0
                        && vNpAge.getText().toString().trim().length() > 0
                        && vEtWeight.getText().toString().trim().length() > 0
                        && vEtGoal.getText().toString().trim().length() > 0
                        && vNpFeet.getText().toString().trim().length() > 0
                        && vNpInches.getText().toString().trim().length() > 0) {
                        if (vRbMale.isChecked()) {
                            savePreferences(Globals.USER_SEX, "male");
                            personSex = "male";
                        }
                        if (vRbFemale.isChecked()) {
                            savePreferences(Globals.USER_SEX, "female");
                            personSex = "female";
                        }
                        savePreferences(Globals.USER_NAME, vName.getText().toString());
                        savePreferences(Globals.USER_AGE, vNpAge.getText().toString());
                        savePreferences(Globals.USER_WEIGHT, vEtWeight.getText().toString());
                        savePreferences(Globals.USER_WEIGHT_GOAL, vEtGoal.getText().toString());
                        savePreferences(Globals.USER_HEIGHT_FEET, vNpFeet.getText().toString());
                        savePreferences(Globals.USER_HEIGHT_INCHES, vNpInches.getText().toString());
                        savePreferences(Globals.USER_ACTIVITY_LEVEL, String.valueOf(vSpnActivity.getSelectedItemPosition()));
                        savePreferences(Globals.USER_WEIGHT_LOSS_GOAL, String.valueOf(vWeightLoss.getSelectedItemPosition()));
                        if (vWeightLoss.getSelectedItemPosition() == 0) {
                            savePreferences(Globals.USER_GOAL, "Gain 2 Pounds Per Week");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 1) {
                            savePreferences(Globals.USER_GOAL, "Gain 1.5 Pounds Per Week");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 2) {
                            savePreferences(Globals.USER_GOAL, "Gain 1 Pounds Per Week");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 3) {
                            savePreferences(Globals.USER_GOAL, "Gain .5 Pounds Per Week");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 4) {
                            savePreferences(Globals.USER_GOAL, "Maintain Weight");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 5) {
                            savePreferences(Globals.USER_GOAL, "Lose .5 Pounds Per Week");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 6) {
                            savePreferences(Globals.USER_GOAL, "Lose 1 Pounds Per Week");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 7) {
                            savePreferences(Globals.USER_GOAL, "Lose 1.5 Pounds Per Week");
                        }
                        if (vWeightLoss.getSelectedItemPosition() == 8) {
                            savePreferences(Globals.USER_GOAL, "Lose 2 Pounds Per Week");
                        }

                        mEquations.execute();
                    } else {
                        if (vName.getText().toString().trim().length() == 0)
                            vName.setError("Missing Field");
                        if (vNpAge.getText().toString().trim().length() == 0)
                            vNpAge.setError("Missing Field");
                        if (vEtWeight.getText().toString().trim().length() == 0)
                            vEtWeight.setError("Missing Field");
                        if (vEtGoal.getText().toString().trim().length() == 0)
                            vEtGoal.setError("Missing Field");
                        if (vNpFeet.getText().toString().trim().length() == 0)
                            vNpFeet.setError("Missing Field");
                        if (vNpInches.getText().toString().trim().length() == 0)
                            vNpInches.setError("Missing Field");
                    }
                }
                return false;
            }
        });
    }

    double mBodyMassIndex;
    double mBasalMetabolicRate;
    double mCaloriesToMaintainWeight;
    double mCaloriesToMeetGoal;
    double mCDailyCarbohydrates;
    double mDailyFat;
    double mDailyProtein;
    double mCaloriesGiveUp;

    private void equations() {
        mEquations = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mBodyMassIndex = Equations.personBMI(Double.valueOf(vEtWeight.getText().toString()), Double.valueOf(vNpFeet.getText().toString()), Double.valueOf(vNpInches.getText().toString()));

                mBasalMetabolicRate = Equations.personBMR(personSex, Double.valueOf(vEtWeight.getText().toString()), Double.valueOf(vNpFeet.getText().toString()), Double.valueOf(vNpInches.getText().toString()), Double.valueOf(vNpAge.getText().toString()));

                mCaloriesToMaintainWeight = Equations.personActivityLevel(vSpnActivity.getSelectedItemPosition()) *
                    Equations.personBMR(personSex, Double.valueOf(vEtWeight.getText().toString()), Double.valueOf(vNpFeet.getText().toString()), Double.valueOf(vNpInches.getText().toString()), Double.valueOf(vNpAge.getText().toString()));

                mCaloriesToMeetGoal = Equations.weightLossPerWeek(vWeightLoss.getSelectedItemPosition(), mCaloriesToMaintainWeight);
                mCaloriesGiveUp = Equations.caloriesToGiveUpk(vWeightLoss.getSelectedItemPosition());
                mDailyFat = (.275 * mCaloriesToMeetGoal) / 9;
                mCDailyCarbohydrates = (.50 * mCaloriesToMeetGoal) / 4;
                mDailyProtein = (.225 * mCaloriesToMeetGoal) / 4;
                return null;
            }

            protected void onPostExecute(Void result) {
                savePreferences(Globals.USER_BODY_MASS_INDEX, String.valueOf(mBodyMassIndex));
                savePreferences(Globals.USER_BASAL_METABOLIC_RATE, String.valueOf(mBasalMetabolicRate));
                savePreferences(Globals.USER_CALORIES_TO_MAINTAIN_WEIGHT, String.valueOf(mCaloriesToMaintainWeight));
                savePreferences(Globals.USER_CALORIES_TO_REACH_GOAL, String.valueOf(mCaloriesToMeetGoal));
                savePreferences(Globals.USER_DAILY_FAT, String.valueOf(mDailyFat));
                savePreferences(Globals.USER_DAILY_CARBOHYDRATES, String.valueOf(mCDailyCarbohydrates));
                savePreferences(Globals.USER_DAILY_PROTEIN, String.valueOf(mDailyProtein));
                savePreferences(Globals.IS_USER_INFORMATION_SAVED, Globals.USER_INFORMATION_SAVED);
                savePreferences(Globals.USER_CALORIES_GIVE_UP, String.valueOf(mCaloriesGiveUp));
                WeightLog weightLog = new WeightLog();
                weightLog.setDate(new Date());
                weightLog.setCurrentWeight(Double.valueOf(vEtWeight.getText().toString()));
                weightLog.save();
                Intent i = new Intent(getActivity(), MainActivityController.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
            }
        };
    }

    private void savePreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    private void loadSavedPreferences() {
        String stringSpnActivityString = sharedPreferences.getString(Globals.USER_ACTIVITY_LEVEL, "0");
        String stringSpinnerWeightLossGoal = sharedPreferences.getString(Globals.USER_WEIGHT_LOSS_GOAL, "4");
        String stringRbSex = sharedPreferences.getString(Globals.USER_SEX, "female");
        if (Globals.SEX_MALE.equals(stringRbSex)) {
            vRbMale.setChecked(true);
        } else {
            vRbFemale.setChecked(true);
        }
        vName.setText(sharedPreferences.getString(Globals.USER_NAME, ""));
        vNpAge.setText(sharedPreferences.getString(Globals.USER_AGE, ""));
        vEtWeight.setText(sharedPreferences.getString(Globals.USER_WEIGHT, ""));
        vEtGoal.setText(sharedPreferences.getString(Globals.USER_WEIGHT_GOAL, ""));
        vNpFeet.setText(sharedPreferences.getString(Globals.USER_HEIGHT_FEET, ""));
        vNpInches.setText(sharedPreferences.getString(Globals.USER_HEIGHT_INCHES, ""));
        vSpnActivity.setSelection(Integer.valueOf(stringSpnActivityString));
        vWeightLoss.setSelection(Integer.valueOf(stringSpinnerWeightLossGoal));
    }

    /**
     * SaveInstanceState on screen rotation
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // if you need information saved, do it here
        super.onSaveInstanceState(outState);
    }

}
