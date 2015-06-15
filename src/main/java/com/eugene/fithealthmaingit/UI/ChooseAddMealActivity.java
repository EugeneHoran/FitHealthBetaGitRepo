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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

/**
 * ChooseAddMealActivity controls ChooseAddMealTabsFragment and ChooseAddMealSearchFragment
 */

public class ChooseAddMealActivity extends AppCompatActivity implements ChooseAddMealTabsFragment.FragmentCallbacks {
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_item);
        fragment = new ChooseAddMealTabsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, Globals.RETAIN_FRAGMENT, fragment);
    }

    /**
     * Interface within ChooseAddMealTabsFragment
     * Start ChooseAddMealSearchFragment with a search result
     *
     * @param mealName Meal name of the recent search list item clicked
     */
    @Override
    public void recentSearchClicked(String mealName) {
        Bundle b = new Bundle();
        b.putString("MealName", mealName);
        Fragment searchItem = new ChooseAddMealSearchFragment();
        searchItem.setArguments(b);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, android.R.anim.fade_out).replace(R.id.container, searchItem).addToBackStack(null).commit();
    }

    /**
     * Interface within ChooseAddMealTabsFragment
     * Start ChooseAddMealSearchFragment with no results
     */
    @Override
    public void searchClicked() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, android.R.anim.fade_out).replace(R.id.container, new ChooseAddMealSearchFragment()).addToBackStack(null).commit();
    }

}
