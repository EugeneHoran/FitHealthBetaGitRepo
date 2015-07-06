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

package com.eugene.fithealthmaingit.UI.MealView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.MainActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

/**
 * View Meals based on Date and Meal Type
 */
public class MealViewActivity extends AppCompatActivity {

    private String mealType;
    private int position;
    private Date mDate;
    ViewPager mViewPager;
    LogAdapterAll mLogMealAdapter;
    TabLayout tabs;
    Toolbar toolbar_meal_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE); // Meal Type (Snack, Breakfast, Lunch, Dinner)
            position = extras.getInt(Globals.MEAL_POSITION); // Meal Position
            mDate = (Date) extras.getSerializable(Globals.MEAL_DATE); // Meal Position
        }
        mLogMealAdapter = new LogAdapterAll(this, 0, LogMeal.logSortByMealChoice(mealType, mDate)); // Set adapter based on (Snack, Breakfast, Lunch, Dinner) and Date
        toolbar_meal_view = (Toolbar) findViewById(R.id.toolbar_meal_view);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        InitializeToolbar();
        InitializePagerTabs();

        tabs.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position);
    }

    private void InitializeToolbar() {
        toolbar_meal_view.setTitle("Your " + mealType);
        toolbar_meal_view.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar_meal_view.inflateMenu(R.menu.menu_meal_view);
        toolbar_meal_view.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        toolbar_meal_view.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete: // Delete the meal then restart the Activity
                        LogMeal logDelete = mLogMealAdapter.getItem(mViewPager.getCurrentItem());
                        logDelete.delete();
                        Intent intent = new Intent(MealViewActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }

    private void InitializePagerTabs() {
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                LogMeal log = mLogMealAdapter.getItem(position);
                return MealViewFragment.newInstance(log.getId());
            }

            @Override
            public CharSequence getPageTitle(int position) { // Tab text
                LogMeal logMeal1 = mLogMealAdapter.getItem(position);
                String s;
                if (logMeal1.getMealName().toString().trim().length() > 12) {
                    s = logMeal1.getMealName().substring(0, 12) + "..";
                } else {
                    s = logMeal1.getMealName();
                }
                return s;
            }

            @Override
            public int getCount() {
                return mLogMealAdapter.getCount();
            }
        });

        tabs.setTabTextColors(Color.parseColor("#80ffffff"), Color.parseColor("#ffffff"));
        if (mLogMealAdapter.getCount() == 1) {
            tabs.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}