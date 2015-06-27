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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.eugene.fithealthmaingit.HomeScreenWidget.FitHealthWidget;
import com.eugene.fithealthmaingit.UI.ChooseAddMealActivity;
import com.eugene.fithealthmaingit.UI.Dialogs.FragmentSuggestionDialog;
import com.eugene.fithealthmaingit.UI.Dialogs.UpdateWeightDialogFragment;
import com.eugene.fithealthmaingit.UI.FragmentBlankLoading;
import com.eugene.fithealthmaingit.UI.FragmentFitbit;
import com.eugene.fithealthmaingit.UI.FragmentHealth;
import com.eugene.fithealthmaingit.UI.FragmentJournalMainHome;
import com.eugene.fithealthmaingit.UI.FragmentNavigationDrawer;
import com.eugene.fithealthmaingit.UI.FragmentNutrition;
import com.eugene.fithealthmaingit.UI.FragmentSearch;
import com.eugene.fithealthmaingit.UI.FragmentWeight;
import com.eugene.fithealthmaingit.UI.MealViewActivity;
import com.eugene.fithealthmaingit.UI.NutritionPagerTesting.FragmentNutritionPager;
import com.eugene.fithealthmaingit.UI.QuickAddActivity;
import com.eugene.fithealthmaingit.UI.UserInformationActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

/**
 * Activity that controls the fragments within the Navigation Drawer.
 */
public class MainActivityController extends AppCompatActivity implements
    FragmentNavigationDrawer.NavigationDrawerCallbacks,
    FragmentJournalMainHome.FragmentCallbacks,
    FragmentNutrition.FragmentCallbacks,
    FragmentHealth.FragmentCallbacks,
    FragmentWeight.FragmentCallbacks,
    UpdateWeightDialogFragment.FragmentCallbacks,
    FragmentNutritionPager.FragmentCallbacks,
    FragmentFitbit.FragmentCallbacks {

    private DrawerLayout mNavigationDrawer;
    private Fragment fragment;

    // Determines whether or not to load fragment after nav drawer if closed.
    private static final String FIRST_FRAGMENT_ADDED = "is_first_fragment_added";
    private static String FRAGMENT_TAG = "";
    private boolean isFirstFragmentAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // From home screen widget
        Intent widgetIntent = this.getIntent();
        if (widgetIntent != null) {
            if (widgetIntent.getAction() != null && savedInstanceState == null) {
                if (widgetIntent.getAction().equals(FitHealthWidget.ACTION_SEARCH)) {
                    // Post delay to allow the app to open and not interfere with animation
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.containerSearch, new FragmentSearch()).addToBackStack(null).commit();
                        }
                    }, 100);
                }
                if (widgetIntent.getAction().equals(FitHealthWidget.ACTION_ADD)) {
                    widgetAdd();
                }
            }
        }

    }

    private void widgetAdd() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivityController.this).setTitle("Choose Meal: ");
        final ArrayAdapter<String> mAdapterMoveMeal = new ArrayAdapter<>(MainActivityController.this, android.R.layout.simple_list_item_1);
        mAdapterMoveMeal.add("Snack");
        mAdapterMoveMeal.add("Breakfast");
        mAdapterMoveMeal.add("Lunch");
        mAdapterMoveMeal.add("Dinner");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(mAdapterMoveMeal, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = mAdapterMoveMeal.getItem(which);
                Intent i = new Intent(MainActivityController.this, ChooseAddMealActivity.class);
                i.putExtra(Globals.MEAL_TYPE, strName);
                startActivity(i);
            }
        });
        builderSingle.show();
    }

    /**
     * Saves whether the first fragment was added
     * Helper for Navigation Drawer controls
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(FIRST_FRAGMENT_ADDED, isFirstFragmentAdded);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isFirstFragmentAdded = savedInstanceState.getBoolean(FIRST_FRAGMENT_ADDED);
    }

    /**
     * Interface From FragmentNavigationDrawer
     * Handles navigation drawer click events
     *
     * @param position position of the list item clicked
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        handleNavigationDrawer();
        switch (position) {
            case 0:
                fragment = new FragmentJournalMainHome();
                FRAGMENT_TAG = "JOURNAL";
                break;
            case 1:
                fragment = new FragmentNutritionPager();
                FRAGMENT_TAG = "NUTRITION";
                break;
            case 2:
                fragment = new FragmentWeight();
                FRAGMENT_TAG = "WEIGHT";
                break;
            case 3:
                fragment = new FragmentHealth();
                FRAGMENT_TAG = "HEALTH";
                break;
            case 4:
                fragment = new FragmentFitbit();
                FRAGMENT_TAG = "FITBIT";
                break;
            default:
                break;
        }
        /**
         * This might get confusing.
         * Goal: Replace the Fragment after the Navigation Drawer is closed to prevent common animation skips on earlier phones.
         * Load the first fragment called then set isFirstFragmentAdded == true.
         * If isFirstFragmentAdded = true, the fragment will be replaced after the drawer is closed.
         */
        if (fragment != null && !isFirstFragmentAdded) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG).commit();
            isFirstFragmentAdded = true;
        } else {
            Fragment loading = new FragmentBlankLoading();
            Bundle b = new Bundle();
            b.putInt("position", position);
            loading.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, loading).commit();
        }
        if (mNavigationDrawer != null)
            mNavigationDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    if (fragment != null && isFirstFragmentAdded) {
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment, FRAGMENT_TAG).commit();
                        isFirstFragmentAdded = true;
                    }
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                }
            });
    }

    public void FitBitCaloriesBurned(String s) {
        FragmentJournalMainHome fragmentMain = (FragmentJournalMainHome) getSupportFragmentManager().findFragmentByTag("JOURNAL");
        if (fragmentMain != null) {
            fragmentMain.FitBit(s);
        }
    }

    public void FitBitLoading() {
        FragmentJournalMainHome fragmentMain = (FragmentJournalMainHome) getSupportFragmentManager().findFragmentByTag("JOURNAL");
        if (fragmentMain != null) {
            fragmentMain.FitBitLoading();
        }
    }

    /**
     * Interface within all of the fragments controlled by MainActivityController
     * Menu icon click within the fragments toolbar
     * Opens navigation drawer
     */
    @Override
    public void openNavigationDrawer() {
        handleNavigationDrawer();
    }

    /**
     * Used to handle the closing and opening of the Navigation Drawer.
     * Prevent repetitive statements
     */
    private void handleNavigationDrawer() {
        if (mNavigationDrawer != null) {
            if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
            } else {
                mNavigationDrawer.openDrawer(GravityCompat.START);
            }
        }
    }

    /**
     * Interface within FragmentNavigationDrawer
     * Opens UserInformationActivity activityS
     */
    @Override
    public void openUserInformationActivity() {
        handleNavigationDrawer();
        startActivity(new Intent(this, UserInformationActivity.class));
    }

    /**
     * Interface within FragmentJournalMainHome
     * Opens Quick add activity
     *
     * @param mealType (snack, breakfast, lunch, dinner)
     */
    @Override
    public void openQuickAdd(String mealType) {
        Intent i = new Intent(this, QuickAddActivity.class);
        i.putExtra(Globals.MEAL_TYPE, mealType);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    /**
     * Interface within FragmentJournalMainHome
     * Opens MealViewActivity
     * List onItemClickListener
     *
     * @param mId      selected meal Id
     * @param MealType (snack, breakfast, lunch, dinner)
     * @param position current item selected position
     * @param d        date the item was added
     */
    @Override
    public void viewMeal(int mId, String MealType, int position, Date d) {
        Intent i = new Intent(this, MealViewActivity.class);
        i.putExtra(Globals.DATA_ID, mId);
        i.putExtra(Globals.MEAL_TYPE, MealType);
        i.putExtra(Globals.MEAL_POSITION, position);
        i.putExtra(Globals.MEAL_DATE, d);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    /**
     * Interface within FragmentJournalMainHome
     * Open suggestion FragmentSuggestionDialog
     *
     * @param mealType (snack, breakfast, lunch, dinner)
     * @param d        current date of the fragment
     */
    @Override
    public void viewSuggestion(String mealType, Date d) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentSuggestionDialog suggestionFragment = new FragmentSuggestionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Globals.MEAL_TYPE, mealType);
        bundle.putSerializable(Globals.SUGGESTION_DATE, d);
        suggestionFragment.setArguments(bundle);
        suggestionFragment.show(fm, FragmentSuggestionDialog.TAG);
    }

    /**
     * Interface within FragmentJournalMainHome
     * Opens FragmentSearch
     * Search icon menu Item click, quick search to speed up adding meal entries
     */
    @Override
    public void searchFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.containerSearch, new FragmentSearch()).addToBackStack(null).commit();
    }

    /**
     * Interface within UpdateWeightDialogFragment
     * Restarts FragmentWeight to refresh the new new weight saved
     */
    @Override
    public void updateWeight() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentWeight()).commit();
    }
}
