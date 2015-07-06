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
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.MainActivity;

/**
 * Controls ManualEntrySaveMealFragment
 */
public class ManualEntrySaveMealActivity extends AppCompatActivity implements ManualEntrySaveMealFragment.FragmentCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saved_manual_entry_activty);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerManual, new ManualEntrySaveMealFragment()).commit();
        }
    }

    @Override
    public void fromFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
