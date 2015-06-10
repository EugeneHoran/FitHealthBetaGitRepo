package com.eugene.fithealthmaingit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eugene.fithealthmaingit.UI.Dialogs.FragmentSuggestionDialog;
import com.eugene.fithealthmaingit.UI.Dialogs.UpdateWeightDialogFragment;
import com.eugene.fithealthmaingit.UI.FragmentHealth;
import com.eugene.fithealthmaingit.UI.FragmentJournalMainHome;
import com.eugene.fithealthmaingit.UI.FragmentNavigationDrawer;
import com.eugene.fithealthmaingit.UI.FragmentNutrition;
import com.eugene.fithealthmaingit.UI.FragmentSearch;
import com.eugene.fithealthmaingit.UI.FragmentWeight;
import com.eugene.fithealthmaingit.UI.MealViewActivity;
import com.eugene.fithealthmaingit.UI.QuickAddActivity;
import com.eugene.fithealthmaingit.UI.UserInformationActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

public class MainActivityController extends AppCompatActivity implements
    FragmentNavigationDrawer.NavigationDrawerCallbacks,
    FragmentJournalMainHome.FragmentCallbacks,
    FragmentNutrition.FragmentCallbacks,
    FragmentHealth.FragmentCallbacks,
    FragmentWeight.FragmentCallbacks,
    UpdateWeightDialogFragment.FragmentCallbacks {
    private DrawerLayout mNavigationDrawer;
    private Fragment fragment;
    String s;
    private static final String FIRST_FRAGMENT_ADDED = "is_first_fragment_added";
    private boolean isFirstFragmentAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        HandleNavOpenClose();
        switch (position) {
            case 0:
                fragment = new FragmentJournalMainHome();
                break;
            case 1:
                fragment = new FragmentNutrition();
                break;
            case 2:
                fragment = new FragmentWeight();
                break;
            case 3:
                fragment = new FragmentHealth();
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
                .replace(R.id.container, fragment).commit();
            isFirstFragmentAdded = true;
        }
        if (mNavigationDrawer != null)
            mNavigationDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    if (fragment != null && isFirstFragmentAdded) {
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment).commit();
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

    @Override
    public void openUserInformationActivity() {
        HandleNavOpenClose();
        startActivity(new Intent(this, UserInformationActivity.class));
    }

    private void HandleNavOpenClose() {
        if (mNavigationDrawer != null) {
            if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
            } else {
                mNavigationDrawer.openDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void openNavigationDrawer() {
        HandleNavOpenClose();
    }

    @Override
    public void quickAdd(String mealType) {
        Intent i = new Intent(this, QuickAddActivity.class);
        i.putExtra(Globals.MEAL_TYPE, mealType);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

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

    @Override
    public void viewSuggestion(String s, Date d) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentSuggestionDialog suggestionFragment = new FragmentSuggestionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Globals.MEAL_TYPE, s);
        bundle.putSerializable(Globals.SUGGESTION_DATE, d);
        suggestionFragment.setArguments(bundle);
        suggestionFragment.show(fm, FragmentSuggestionDialog.TAG);
    }

    @Override
    public void searchFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.containerSearch, new FragmentSearch()).addToBackStack(null).commit();
    }

    @Override
    public void updateWeight() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentWeight()).commit();
    }
}
