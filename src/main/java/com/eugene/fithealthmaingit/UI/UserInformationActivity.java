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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.eugene.fithealthmaingit.R;


public class UserInformationActivity extends AppCompatActivity {
    private Fragment fragment = null;
    private static final String RETAIN_FRAGMENT = "retain_fragment_on_rotation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        fragment = new UserInformationFragment();
        if (savedInstanceState != null)
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, RETAIN_FRAGMENT);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, RETAIN_FRAGMENT, fragment);
    }

    static final int ACTIVITY_ONE_REQUEST = 1;  // The request code for ActivityOne
    public final static String ACTIVITY_ONE_RESULT = "activity_one"; // Data Argument For Activity One

    /**
     * Get the Results from the Other Activities
     *
     * @param requestCode In this case either 1 = ACTIVITY_ONE_REQUEST  or 2 = ACTIVITY_TWO_REQUEST
     * @param resultCode  Determines whether the request was successful.
     * @param data        The being sent from other activities via Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_ONE_REQUEST && resultCode == RESULT_OK) {
            if (data.hasExtra(ACTIVITY_ONE_RESULT)) {
                String result = data.getExtras().getString(ACTIVITY_ONE_RESULT);
                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
                if (result.equals("CONNECTED")) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            }
        }
    }
}
