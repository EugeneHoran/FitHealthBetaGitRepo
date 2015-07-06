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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eugene.fithealthmaingit.HomeScreenWidget.FitHealthWidget;
import com.eugene.fithealthmaingit.UI.ChooseAddMealActivity;
import com.eugene.fithealthmaingit.UI.Dialogs.FragmentSuggestionDialog;
import com.eugene.fithealthmaingit.UI.Dialogs.UpdateWeightDialogFragment;
import com.eugene.fithealthmaingit.UI.MealView.MealViewActivity;
import com.eugene.fithealthmaingit.UI.NavFragments.FragmentBlankLoading;
import com.eugene.fithealthmaingit.UI.NavFragments.FragmentFitbit;
import com.eugene.fithealthmaingit.UI.NavFragments.FragmentHealth;
import com.eugene.fithealthmaingit.UI.NavFragments.FragmentJournalMainHome;
import com.eugene.fithealthmaingit.UI.NavFragments.FragmentNutritionPager;
import com.eugene.fithealthmaingit.UI.NavFragments.FragmentSearch;
import com.eugene.fithealthmaingit.UI.NavFragments.FragmentWeight;
import com.eugene.fithealthmaingit.UI.QuickAddActivity;
import com.eugene.fithealthmaingit.UI.UserInformationActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements
    FragmentJournalMainHome.FragmentCallbacks,
    FragmentNutritionPager.FragmentCallbacks,
    FragmentWeight.FragmentCallbacks,
    FragmentHealth.FragmentCallbacks,
    UpdateWeightDialogFragment.FragmentCallbacks,
    FragmentFitbit.FragmentCallbacks {

    private DrawerLayout mNavigationDrawer;
    private Fragment fragment;

    public static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;

    private static final String FIRST_FRAGMENT_ADDED = "is_first_fragment_added";
    private boolean isFirstFragmentAdded = false;

    private static String FRAGMENT_TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
            isFirstFragmentAdded = savedInstanceState.getBoolean(FIRST_FRAGMENT_ADDED);
        } else {
            mNavItemId = R.id.nav_journal;
        }

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set menu header text to User Name
        TextView mHeaderText = (TextView) findViewById(R.id.txtName);
        mHeaderText.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(Globals.USER_NAME, ""));

        /**
         * Initiate NavigationView
         * Inflate Menu based on FitBit connection status
         */
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav);
        if (PreferenceManager.getDefaultSharedPreferences(this).getString("FITBIT_ACCESS_TOKEN", "").equals("")) {
            mNavigationView.inflateMenu(R.menu.drawer);
        } else {
            mNavigationView.inflateMenu(R.menu.drawer_fitbit);
        }
        mNavigationView.getMenu().findItem(mNavItemId).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() != R.id.nav_settings) {
                    mNavItemId = menuItem.getItemId();
                    switchFragment(menuItem.getItemId());
                    menuItem.setChecked(true);
                    Fragment loading = new FragmentBlankLoading();
                    Bundle b = new Bundle();
                    b.putInt(NAV_ITEM_ID, mNavItemId);
                    loading.setArguments(b);
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, loading).commit();
                } else {
                    startActivity(new Intent(MainActivity.this, UserInformationActivity.class));
                }
                handleNavigationDrawer();
                return false;
            }
        });
        switchFragment(mNavItemId);

        /**
         * Handles Home Screen Widget
         * Search and Add
         */
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

    /**
     * Handles Navigation Logic
     */
    private void switchFragment(int menuId) {
        switch (menuId) {
            case R.id.nav_journal:
                fragment = new FragmentJournalMainHome();
                FRAGMENT_TAG = "JOURNAL";
                break;
            case R.id.nav_nutrition:
                fragment = new FragmentNutritionPager();
                FRAGMENT_TAG = "NUTRITION";
                break;
            case R.id.nav_weight:
                fragment = new FragmentWeight();
                FRAGMENT_TAG = "WEIGHT";
                break;
            case R.id.nav_health:
                fragment = new FragmentHealth();
                FRAGMENT_TAG = "HEALTH";
                break;
            case R.id.nav_fitbit:
                fragment = new FragmentFitbit();
                FRAGMENT_TAG = "FITBIT";
                break;
            default:
                break;
        }
        if (fragment != null && !isFirstFragmentAdded) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, FRAGMENT_TAG).commit();
            isFirstFragmentAdded = true;
        }
        if (mNavigationDrawer != null)
            mNavigationDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    // Check to see if fragment is NUTRITION, has child fragments.
                    if (fragment != null && isFirstFragmentAdded && getSupportFragmentManager().findFragmentByTag("NUTRITION") == null) {
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
     * Fragment  Journal Main Home Listeners
     */


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


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
        outState.putBoolean(FIRST_FRAGMENT_ADDED, isFirstFragmentAdded);
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
     * Interface within UpdateWeightDialogFragment
     * Restarts FragmentWeight to refresh the new new weight saved
     * Easier to recall fragment, rather than
     */
    @Override
    public void updateWeight() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentWeight(), "WEIGHT").commit();
    }

    /**
     * Initiate FitBit
     *
     * @param s calories burned
     */
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

    private void widgetAdd() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this).setTitle("Choose Meal: ");
        final ArrayAdapter<String> mAdapterMoveMeal = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
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
                Intent i = new Intent(MainActivity.this, ChooseAddMealActivity.class);
                i.putExtra(Globals.MEAL_TYPE, strName);
                startActivity(i);
            }
        });
        builderSingle.show();
    }

}
