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

    /**
     * Opens next activity based on user Saved Information
     * First It checks to see if any weight had been saved for the latest update,
     * helper for adding the weight to older versions of the app (prevent crash when opening
     * FragmentWeight
     * <p/>
     * If user information is saved, it opens MainActivityController
     * If user information is not saved, it opens UserInformationActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent iNotSaved = new Intent(this, UserInformationActivity.class);
        Intent iSaved = new Intent(this, MainActivity.class);
        WeightLogAdapter weightLogAdapter = new WeightLogAdapter(this, 0, WeightLog.all());
        if (weightLogAdapter.getCount() == 0) { // Helps get data without having to reinstall the app.
            startActivity(iNotSaved);
            finish();
        } else {
            if (Globals.USER_INFORMATION_SAVED.equals(sharedPreferences.getString(Globals.IS_USER_INFORMATION_SAVED, ""))) {
                startActivity(iSaved);
                finish();
            } else {
                startActivity(iNotSaved);
                finish();
            }
        }
    }
}
