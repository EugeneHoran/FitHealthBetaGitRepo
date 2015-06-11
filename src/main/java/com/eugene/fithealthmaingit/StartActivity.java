package com.eugene.fithealthmaingit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLog;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogWeight.WeightLogAdapter;
import com.eugene.fithealthmaingit.UI.UserInformationActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent iNotSaved = new Intent(this, UserInformationActivity.class);
        Intent iSaved = new Intent(this, MainActivityController.class);
        WeightLogAdapter weightLogAdapter = new WeightLogAdapter(this, 0, WeightLog.all());
        if (weightLogAdapter.getCount() == 0) {
            startActivity(iNotSaved);
            finish();
        } else {
            if (sharedPreferences.getString(Globals.IS_USER_INFORMATION_SAVED, "").equals(Globals.USER_INFORMATION_SAVED)) {
                startActivity(iSaved);
                finish();
            } else {
                startActivity(iNotSaved);
                finish();
            }
        }
    }
}
