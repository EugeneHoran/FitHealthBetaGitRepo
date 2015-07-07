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

package com.eugene.fithealthmaingit.UI.NavFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Custom.TextViewFont;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterBreakfast;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterDinner;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterLunch;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterSnack;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.FitBit.FitBitCaloriesBurned;
import com.eugene.fithealthmaingit.HomeScreenWidget.FitHealthWidget;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.ChooseAddMealActivity;
import com.eugene.fithealthmaingit.Utilities.DateCompare;
import com.eugene.fithealthmaingit.Utilities.Equations;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.eugene.fithealthmaingit.Utilities.SetListHeight.SetBreakfastListHeight;
import com.eugene.fithealthmaingit.Utilities.SetListHeight.SetDinnerListHeight;
import com.eugene.fithealthmaingit.Utilities.SetListHeight.SetLunchListHeight;
import com.eugene.fithealthmaingit.Utilities.SetListHeight.SetSnackListHeight;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import github.chenupt.dragtoplayout.AttachUtil;
import github.chenupt.dragtoplayout.DragTopLayout;

/**
 * Main journal that displays:
 * list of item that are added
 * nutrition information
 */
public class FragmentJournalMainHome extends Fragment implements
    View.OnClickListener,
    SlidingUpPanelLayout.PanelSlideListener,
    DragTopLayout.PanelListener {

    private View v;

    private SharedPreferences sharedPreferences;

    private Date mDate = new Date();
    private DecimalFormat df = new DecimalFormat("0");

    private int slide_down_padding = 0;

    private LinearLayout mCaloriePullDownView, pullDownItems;
    private SlidingUpPanelLayout mSlidingLayout;
    private RelativeLayout mSliderLayoutHelper;
    private LogAdapterSnack mLogSnackAdapter;
    private LogAdapterBreakfast mLogBreakfastAdapter;
    private LogAdapterLunch mLogLunchAdapter;
    private LogAdapterDinner mLogDinnerAdapter;
    private LogAdapterAll mLogAdapterAll;
    private ListView mListSnack, mListBreakfast, mListLunch, mListDinner;
    private ProgressBar mPbCalories, mPbFat, mPbCarbs, mPbProtein;
    private TextView mCalories, mCaloriesRemainder;
    LinearLayout mNoSnacks, mNoBreakfast, mNoLunch, mNoDinner;
    private TextView mFatRemainder, mCarbRemainder, mProteinRemainder;
    private TextView mCalSnack, mCalBreakfast, mCalLunch, mCalDinner;
    private ImageView icSnack, icBreakfast, icLunch, icDinner;
    private int mYear, mMonth, mDay;
    private double mCalorieGoalMeal;
    LinearLayout llFitBit;
    TextView fbCaloriesNew;
    TextViewFont txtDate;
    LinearLayout changeDate;

    /**
     * Get the saved date before the views are created/updated
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDate = (Date) savedInstanceState.getSerializable(Globals.JOURNAL_DATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Globals.JOURNAL_DATE, mDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        savedState = savedInstanceState;
        v = inflater.inflate(R.layout.fragment_journal_main_home, container, false);
        // Initiate PreferenceManager to get user saved information
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        /**
         * Convert Date To Calendar
         */
        final Calendar c = DateCompare.DateToCalendar(mDate);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        /**
         * Originally tried to set two different fonts for toolbar title
         * Fit = Roboto-Light
         * Journal = Roboto-Bold
         * TODO: remove this statement and just update the Toolbar Title to custom font
         */
        txtDate = (TextViewFont) v.findViewById(R.id.txtDate);
        changeDate = (LinearLayout) v.findViewById(R.id.changeDate);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, year);
                            cal.set(Calendar.MONTH, monthOfYear);
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                            mDate = cal.getTime();
                            initializeAdapters(mDate);
                            handleDateChanges(mDate);
                            updateListViews();
                        }
                    }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        Toolbar mToolbar = (Toolbar) v.findViewById(R.id.toolbar_journal_main);
        mToolbar.inflateMenu(R.menu.menu_main_journal);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openNavigationDrawer();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                switch (menuItem) {
                    // Initiate Search Fragment
                    case R.id.action_search:
                        mCallbacks.searchFragment();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        // Bottom sheet that displays meals
        InitializeSlidingPanelLayout();
        // PullDown view that displays nutrition information
        InitializeDragTopLayout();
        // Set onClickLister
        v.findViewById(R.id.txtQuickAddSnack).setOnClickListener(this);
        v.findViewById(R.id.txtQuickAddBreakfast).setOnClickListener(this);
        v.findViewById(R.id.txtQuickAddLunch).setOnClickListener(this);
        v.findViewById(R.id.txtQuickAddDinner).setOnClickListener(this);
        v.findViewById(R.id.suggestion).setOnClickListener(this);
        v.findViewById(R.id.suggestion1).setOnClickListener(this);
        v.findViewById(R.id.suggestion2).setOnClickListener(this);
        v.findViewById(R.id.suggestion3).setOnClickListener(this);
        v.findViewById(R.id.snackInfo).setOnClickListener(this);
        v.findViewById(R.id.breakfastInfo).setOnClickListener(this);
        v.findViewById(R.id.lunchInfo).setOnClickListener(this);
        v.findViewById(R.id.dinnerInfo).setOnClickListener(this);

        // initialize lists for context menu and onItemClickListeners
        mListSnack = (ListView) v.findViewById(R.id.listSnack);
        mListBreakfast = (ListView) v.findViewById(R.id.listBreakfast);
        mListLunch = (ListView) v.findViewById(R.id.listLunch);
        mListDinner = (ListView) v.findViewById(R.id.listDinner);
        registerForContextMenu(mListSnack);
        registerForContextMenu(mListBreakfast);
        registerForContextMenu(mListLunch);
        registerForContextMenu(mListDinner);
        mListSnack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogMeal meals = mLogSnackAdapter.getItem(position);
                mCallbacks.viewMeal(meals.getId(), meals.getMealChoice(), position, mDate);
            }
        });
        mListBreakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogMeal meals = mLogBreakfastAdapter.getItem(position);
                mCallbacks.viewMeal(meals.getId(), meals.getMealChoice(), position, mDate);
            }
        });
        mListLunch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogMeal meals = mLogLunchAdapter.getItem(position);
                mCallbacks.viewMeal(meals.getId(), meals.getMealChoice(), position, mDate);
            }
        });
        mListDinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogMeal meals = mLogDinnerAdapter.getItem(position);
                mCallbacks.viewMeal(meals.getId(), meals.getMealChoice(), position, mDate);
            }
        });

        // Nutrition Widgets
        fbCaloriesNew = (TextView) v.findViewById(R.id.fbCaloriesNew);

        mCalSnack = (TextView) v.findViewById(R.id.calSnack);
        mCalBreakfast = (TextView) v.findViewById(R.id.calBreakfast);
        mCalLunch = (TextView) v.findViewById(R.id.calLunch);
        mCalDinner = (TextView) v.findViewById(R.id.calDinner);
        mNoSnacks = (LinearLayout) v.findViewById(R.id.txtItemSnack);
        mNoBreakfast = (LinearLayout) v.findViewById(R.id.txtItemBreakfast);
        mNoLunch = (LinearLayout) v.findViewById(R.id.txtItemLunch);
        mNoDinner = (LinearLayout) v.findViewById(R.id.txtItemDinner);
        mPbCalories = (ProgressBar) v.findViewById(R.id.pbCal);
        mPbFat = (ProgressBar) v.findViewById(R.id.pbFat);
        mPbCarbs = (ProgressBar) v.findViewById(R.id.pbCarbs);
        mPbProtein = (ProgressBar) v.findViewById(R.id.pbProtein);
        mFatRemainder = (TextView) v.findViewById(R.id.txtRemainderFat);
        mCarbRemainder = (TextView) v.findViewById(R.id.txtRemainderCarbs);
        mProteinRemainder = (TextView) v.findViewById(R.id.txtRemainderProtein);
        icSnack = (ImageView) v.findViewById(R.id.icSnack);
        icBreakfast = (ImageView) v.findViewById(R.id.icBreakfast);
        icLunch = (ImageView) v.findViewById(R.id.icLunch);
        icDinner = (ImageView) v.findViewById(R.id.icDinner);
        // Set Adapters based on Current date or updated date
        initializeAdapters(mDate);
        // Checks adapters if they are empty and notifies the user
        updateListViews();
        // Set toolbar subtitle to the current date or updated date
        handleDateChanges(mDate);
        /**
         * Design Support Library
         * Due to the error within the library: set margin manually till it is updated
         */
        FloatingActionButton mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) mFab.getLayoutParams();
            p.setMargins(0, 0, Equations.dpToPx(getActivity(), 8), 0); // get rid of margins since shadow area is now the margin
            mFab.setLayoutParams(p);
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        return v;
    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), ChooseAddMealActivity.class);
        switch (v.getId()) {
            //Sliding Panel
            case R.id.slide_helper:
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case R.id.btnSnack:
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                i.putExtra(Globals.MEAL_TYPE, Globals.MEAL_TYPE_SNACK);
                startActivity(i);
                break;
            case R.id.btnBreakfast:
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                i.putExtra(Globals.MEAL_TYPE, Globals.MEAL_TYPE_BREAKFAST);
                startActivity(i);
                break;
            case R.id.btnLunch:
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                i.putExtra(Globals.MEAL_TYPE, Globals.MEAL_TYPE_LUNCH);
                startActivity(i);
                break;
            case R.id.btnDinner:
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                i.putExtra(Globals.MEAL_TYPE, Globals.MEAL_TYPE_DINNER);
                startActivity(i);
                break;
            case R.id.txtQuickAddSnack:
                mCallbacks.openQuickAdd(Globals.MEAL_TYPE_SNACK);
                break;
            case R.id.txtQuickAddBreakfast:
                mCallbacks.openQuickAdd(Globals.MEAL_TYPE_BREAKFAST);
                break;
            case R.id.txtQuickAddLunch:
                mCallbacks.openQuickAdd(Globals.MEAL_TYPE_LUNCH);
                break;
            case R.id.txtQuickAddDinner:
                mCallbacks.openQuickAdd(Globals.MEAL_TYPE_DINNER);
                break;
            //Suggestion
            case R.id.suggestion:
                mCallbacks.viewSuggestion("Snack", mDate);
                break;
            case R.id.suggestion1:
                mCallbacks.viewSuggestion("Breakfast", mDate);
                break;
            case R.id.suggestion2:
                mCallbacks.viewSuggestion("Lunch", mDate);
                break;
            case R.id.suggestion3:
                mCallbacks.viewSuggestion("Dinner", mDate);
                break;

            // Calorie Goal Dialog
            case R.id.snackInfo:
                calorieInfoDialog(Globals.MEAL_TYPE_SNACK);
                break;
            case R.id.breakfastInfo:
                calorieInfoDialog(Globals.MEAL_TYPE_BREAKFAST);
                break;
            case R.id.lunchInfo:
                calorieInfoDialog(Globals.MEAL_TYPE_LUNCH);
                break;
            case R.id.dinnerInfo:
                calorieInfoDialog(Globals.MEAL_TYPE_DINNER);
                break;
            default:
                break;
        }
    }


    /**
     * Updated toobar subtitle to the date
     *
     * @param date current date or updated date
     */
    private void handleDateChanges(Date date) {
        if (DateCompare.areDatesEqual(new Date(), date)) { // Are Dates Equal Today
            txtDate.setText("Today");
        } else if (DateCompare.areDatesEqualYesterday(new Date(), date)) {  // Are Dates Equal Yesterday
            txtDate.setText("Yesterday");
        } else if (DateCompare.areDatesEqualTomorrow(new Date(), date)) {  // Are Dates Equal Yesterday
            txtDate.setText("Tomorrow");
        } else {
            txtDate.setText(DateFormat.format("MMM d, EE", date));
        }

    }

    /**
     * Set Adapters based on Current Date or Updated Date
     * Set List Adapters
     *
     * @param date current date or updated date
     */
    private void initializeAdapters(Date date) {
        mLogSnackAdapter = new LogAdapterSnack(getActivity(), 0, LogMeal.logSortByMealChoice("Snack", date));
        mListSnack.setAdapter(mLogSnackAdapter);
        SetSnackListHeight.setListViewHeight(mListSnack);

        mLogBreakfastAdapter = new LogAdapterBreakfast(getActivity(), 0, LogMeal.logSortByMealChoice("Breakfast", date));
        mListBreakfast.setAdapter(mLogBreakfastAdapter);
        SetBreakfastListHeight.setListViewHeight(mListBreakfast);

        mLogLunchAdapter = new LogAdapterLunch(getActivity(), 0, LogMeal.logSortByMealChoice("Lunch", date));
        mListLunch.setAdapter(mLogLunchAdapter);
        SetLunchListHeight.setListViewHeight(mListLunch);

        mLogDinnerAdapter = new LogAdapterDinner(getActivity(), 0, LogMeal.logSortByMealChoice("Dinner", date));
        mListDinner.setAdapter(mLogDinnerAdapter);
        SetDinnerListHeight.setListViewHeight(mListDinner);

        mLogAdapterAll = new LogAdapterAll(getActivity(), 0, LogMeal.logsByDate(date));
        InitiateFitBit(date);


    }

    /**
     * Checks adapters sizes and
     * notifies user no items have been saved/added
     */
    private void updateListViews() {
        if (mLogSnackAdapter.getCount() > 0)
            mNoSnacks.setVisibility(View.GONE);
        else
            mNoSnacks.setVisibility(View.VISIBLE);
        if (mLogBreakfastAdapter.getCount() > 0)
            mNoBreakfast.setVisibility(View.GONE);
        else
            mNoBreakfast.setVisibility(View.VISIBLE);
        if (mLogLunchAdapter.getCount() > 0)
            mNoLunch.setVisibility(View.GONE);
        else
            mNoLunch.setVisibility(View.VISIBLE);
        if (mLogDinnerAdapter.getCount() > 0)
            mNoDinner.setVisibility(View.GONE);
        else
            mNoDinner.setVisibility(View.VISIBLE);

        /**
         * Setting a post delay due to the progress bars not updating after an item is added through quick add.
         */
        Runnable mMyRunnable =
            new Runnable() {
                @Override
                public void run() {
                    equations();
                }
            };
        Handler myHandler = new Handler();
        myHandler.postDelayed(mMyRunnable, 10);
    }


    /**
     * Refreshed Adapters and ListVies after an Items has been deleted
     */
    private void refreshOnDelete() {
        mListSnack.setAdapter(mLogSnackAdapter);
        SetSnackListHeight.setListViewHeight(mListSnack);
        mListBreakfast.setAdapter(mLogBreakfastAdapter);
        SetBreakfastListHeight.setListViewHeight(mListBreakfast);
        mListLunch.setAdapter(mLogLunchAdapter);
        SetLunchListHeight.setListViewHeight(mListLunch);
        mListDinner.setAdapter(mLogDinnerAdapter);
        SetDinnerListHeight.setListViewHeight(mListDinner);
        updateListViews();
    }


    /**
     * Equation for all of the Nutrition and Meal information
     */
    private void equations() {
        double mCalorieGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, ""));
        double mFatGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_FAT, ""));
        double mCarbGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_CARBOHYDRATES, ""));
        double mProteinGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_DAILY_PROTEIN, ""));
        mCalorieGoalMeal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, "")) / 4;

        icSnack = (ImageView) v.findViewById(R.id.icSnack);
        icBreakfast = (ImageView) v.findViewById(R.id.icBreakfast);
        icLunch = (ImageView) v.findViewById(R.id.icLunch);
        icDinner = (ImageView) v.findViewById(R.id.icDinner);

// _________________________Calories Snack_____________________________
        double mCalConsumedSnack = 0;
        for (LogMeal logMeal : mLogSnackAdapter.getLogs()) {
            mCalConsumedSnack += logMeal.getCalorieCount();
        }
        mCalSnack.setText(df.format(mCalConsumedSnack));

        // Set icon visible and color based on calories consumed for meal.
        if (mCalConsumedSnack >= mCalorieGoalMeal + 100) {
            icSnack.setImageResource(R.mipmap.ic_check_circle);
            icSnack.setColorFilter(Color.parseColor("#F44336"), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (mCalConsumedSnack > mCalorieGoalMeal - 100 && mCalConsumedSnack < mCalorieGoalMeal + 99) {
            icSnack.setImageResource(R.mipmap.ic_check_circle);
            icSnack.setColorFilter(Color.parseColor("#4CAF50"), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            icSnack.setImageResource(R.mipmap.ic_check);
            icSnack.setColorFilter(Color.parseColor("#6D6D6D"), android.graphics.PorterDuff.Mode.MULTIPLY);
        }


// _________________________Calories Breakfast_____________________________
        double mCalConsumedBreakfast = 0;
        for (LogMeal logMeal : mLogBreakfastAdapter.getLogs()) {
            mCalConsumedBreakfast += logMeal.getCalorieCount();
        }
        mCalBreakfast.setText(df.format(mCalConsumedBreakfast));

        // Set icon visible and color based on calories consumed for meal.
        if (mCalConsumedBreakfast >= mCalorieGoalMeal + 100) {
            icBreakfast.setColorFilter(Color.parseColor("#F44336"), android.graphics.PorterDuff.Mode.MULTIPLY);
            icBreakfast.setImageResource(R.mipmap.ic_check_circle);
        } else if (mCalConsumedBreakfast > mCalorieGoalMeal - 100 && mCalConsumedBreakfast < mCalorieGoalMeal + 99) {
            icBreakfast.setColorFilter(Color.parseColor("#4CAF50"), android.graphics.PorterDuff.Mode.MULTIPLY);
            icBreakfast.setImageResource(R.mipmap.ic_check_circle);
        } else {
            icBreakfast.setImageResource(R.mipmap.ic_check);
            icBreakfast.setColorFilter(Color.parseColor("#6D6D6D"), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

// _________________________Calories Lunch_____________________________
        double mCalConsumedLunch = 0;
        for (LogMeal logMeal : mLogLunchAdapter.getLogs()) {
            mCalConsumedLunch += logMeal.getCalorieCount();
        }
        mCalLunch.setText(df.format(mCalConsumedLunch));

        // Set icon visible and color based on calories consumed for meal.
        if (mCalConsumedLunch >= mCalorieGoalMeal + 100) {
            icLunch.setImageResource(R.mipmap.ic_check_circle);
            icLunch.setColorFilter(Color.parseColor("#F44336"), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (mCalConsumedLunch > mCalorieGoalMeal - 100 && mCalConsumedLunch < mCalorieGoalMeal + 99) {
            icLunch.setImageResource(R.mipmap.ic_check_circle);
            icLunch.setColorFilter(Color.parseColor("#4CAF50"), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            icLunch.setImageResource(R.mipmap.ic_check);
            icLunch.setColorFilter(Color.parseColor("#6D6D6D"), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

// _________________________Calories Lunch_____________________________
        double mCalConsumedDinner = 0;
        for (LogMeal logMeal : mLogDinnerAdapter.getLogs()) {
            mCalConsumedDinner += logMeal.getCalorieCount();
        }
        mCalDinner.setText(df.format(mCalConsumedDinner));

        // Set icon visible and color based on calories consumed for meal.
        if (mCalConsumedDinner >= mCalorieGoalMeal + 100) {
            icDinner.setImageResource(R.mipmap.ic_check_circle);
            icDinner.setColorFilter(Color.parseColor("#F44336"), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (mCalConsumedDinner > mCalorieGoalMeal - 100 && mCalConsumedDinner < mCalorieGoalMeal + 99) {
            icDinner.setImageResource(R.mipmap.ic_check_circle);
            icDinner.setColorFilter(Color.parseColor("#4CAF50"), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            icDinner.setImageResource(R.mipmap.ic_check);
            icDinner.setColorFilter(Color.parseColor("#6D6D6D"), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

// _________________________Calories, Fat, Carbs, Protein All_____________________________
        // Nutrition Consumed
        double mAllCaloriesConsumed = 0;
        double mAllFatConsumed = 0;
        double mAllCarbsConsumed = 0;
        double mAllProteinConsumed = 0;

        for (LogMeal logMeal : mLogAdapterAll.getLogs()) {
            mAllCaloriesConsumed += logMeal.getCalorieCount();
            mAllFatConsumed += logMeal.getFatCount();
            mAllCarbsConsumed += logMeal.getCarbCount();
            mAllProteinConsumed += logMeal.getProteinCount();
        }
        // Nutrition Goals
        // Remainder Nutrition
        mCaloriesRemainder.setText(df.format(mCalorieGoal - mAllCaloriesConsumed) + " Left");
        mFatRemainder.setText(df.format(mFatGoal - mAllFatConsumed) + " Left");
        mCarbRemainder.setText(df.format(mCarbGoal - mAllCarbsConsumed) + " Left");
        mProteinRemainder.setText(df.format(mProteinGoal - mAllProteinConsumed) + " Left");
        // Progress bars

        mPbCalories.setMax(Integer.valueOf(df.format(mCalorieGoal)));
        mPbCalories.setProgress(Integer.valueOf(df.format(mAllCaloriesConsumed)));
        mPbFat.setMax(Integer.valueOf(df.format(mFatGoal)));
        mPbFat.setProgress(Integer.valueOf(df.format(mAllFatConsumed)));
        mPbCarbs.setMax(Integer.valueOf(df.format(mCarbGoal)));
        mPbCarbs.setProgress(Integer.valueOf(df.format(mAllCarbsConsumed)));
        mPbProtein.setMax(Integer.valueOf(df.format(mProteinGoal)));
        mPbProtein.setProgress(Integer.valueOf(df.format(mAllProteinConsumed)));

        /**
         * Update Widget
         */
        Context context = getActivity();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        ComponentName thisWidget = new ComponentName(context, FitHealthWidget.class);

        views.setProgressBar(R.id.pbCal, Integer.valueOf(df.format(mCalorieGoal)), Integer.valueOf(df.format(mAllCaloriesConsumed)), false);
        views.setProgressBar(R.id.pbFat, Integer.valueOf(df.format(mFatGoal)), Integer.valueOf(df.format(mAllFatConsumed)), false);
        views.setProgressBar(R.id.pbCarb, Integer.valueOf(df.format(mCarbGoal)), Integer.valueOf(df.format(mAllCarbsConsumed)), false);
        views.setProgressBar(R.id.pbPro, Integer.valueOf(df.format(mProteinGoal)), Integer.valueOf(df.format(mAllProteinConsumed)), false);

        views.setTextViewText(R.id.txtRemainderCal, df.format(mCalorieGoal - mAllCaloriesConsumed));
        views.setTextViewText(R.id.txtRemainderFat, df.format(mFatGoal - mAllFatConsumed));
        views.setTextViewText(R.id.txtRemainderCarb, df.format(mCarbGoal - mAllCarbsConsumed));
        views.setTextViewText(R.id.txtRemainderPro, df.format(mProteinGoal - mAllProteinConsumed));

        appWidgetManager.updateAppWidget(thisWidget, views);


    }

    /**
     * Bottom Sheet Initiation
     * Sliding Panel Layout
     * After Fab clicked, displays which meal type you would like to add (snack, breakfast, lunch, dinner)
     */
    private void InitializeSlidingPanelLayout() {
        mSlidingLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);
        mSliderLayoutHelper = (RelativeLayout) v.findViewById(R.id.slide_helper);
        mSliderLayoutHelper.setOnClickListener(this);
        v.findViewById(R.id.btnSnack).setOnClickListener(this);
        v.findViewById(R.id.btnBreakfast).setOnClickListener(this);
        v.findViewById(R.id.btnLunch).setOnClickListener(this);
        v.findViewById(R.id.btnDinner).setOnClickListener(this);
    }

    // Helper to prevent views below sheet from being clicked
    @Override
    public void onPanelSlide(View view, float v) {
        mSliderLayoutHelper.setVisibility(View.VISIBLE);
        mSliderLayoutHelper.setAlpha(v);
    }

    @Override
    public void onPanelCollapsed(View view) {
        mSliderLayoutHelper.setVisibility(View.GONE);
    }

    @Override
    public void onPanelExpanded(View view) {
        mSliderLayoutHelper.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onPanelHidden(View view) {

    }

    /**
     * DragTopLayout displays the nutrition information
     * Attached to ScrollView
     * When the scrollView y coordinates equal 0, the pull down is enabled
     */

    private void InitializeDragTopLayout() {
        final DragTopLayout mDragLayout = (DragTopLayout) v.findViewById(R.id.drag_layout);
        mDragLayout.setOverDrag(false);
        mDragLayout.toggleTopView();
        mDragLayout.listener(this);
        final ScrollView mScrollView = (ScrollView) v.findViewById(R.id.scrollView);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDragLayout.setTouchMode(AttachUtil.isScrollViewAttach(mScrollView));
                return false;
            }
        });
        mCaloriePullDownView = (LinearLayout) v.findViewById(R.id.view4);
        Equations.dpToPx(getActivity(), 100);
        pullDownItems = (LinearLayout) v.findViewById(R.id.pullDownIems);
        mCalories = (TextView) v.findViewById(R.id.txtCalories);
        mCaloriesRemainder = (TextView) v.findViewById(R.id.txtRemainderCalories);
        mCalories.setTextSize(0);
        mCaloriesRemainder.setTextSize(0);
        View v1 = v.findViewById(R.id.view1);
        View v2 = v.findViewById(R.id.view2);
        View v3 = v.findViewById(R.id.view3);
        slide_down_padding = Math.round(Equations.dpToPx(getActivity(), 90));
        v1.setPadding(slide_down_padding, 0, slide_down_padding, 0);
        v2.setPadding(slide_down_padding, 0, slide_down_padding, 0);
        v3.setPadding(slide_down_padding, 0, slide_down_padding, 0);
    }

    // Animation of views
    @Override
    public void onSliding(float v) {
        int padding = Integer.valueOf(df.format(this.slide_down_padding * v));
        mCaloriePullDownView.setPadding(padding, 0, padding, 0);
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        int tes = Integer.valueOf(df.format((double) v * px));
        float max = 32 * v;
        if (max >= 14)
            max = 14;
        mCalories.setTextSize(max);
        mCalories.setAlpha(v);
        mCalories.setPadding(tes, tes - 2, tes, tes);
        mCaloriesRemainder.setTextSize(max);
        mCaloriesRemainder.setAlpha(v);
        mCaloriesRemainder.setPadding(tes, tes, tes, tes);
        pullDownItems.setAlpha(v);
    }

    @Override
    public void onPanelStateChanged(DragTopLayout.PanelState panelState) {
    }

    @Override
    public void onRefresh() {
    }

    /**
     * Context Menu Initiation
     * Move Meal or Delete Meal
     */
    private int contextListChoice;
    private int contextListPosition;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        String[] menuItems = getResources().getStringArray(R.array.context_menu);
        switch (v.getId()) {
            case R.id.listSnack:
                for (int i = 0; i < menuItems.length; i++) {
                    menu.add(Menu.NONE, i, i, menuItems[i]);
                }
                contextListChoice = 1;
                break;
            case R.id.listBreakfast:
                for (int i = 0; i < menuItems.length; i++) {
                    menu.add(Menu.NONE, i, i, menuItems[i]);
                }
                contextListChoice = 2;
                break;
            case R.id.listLunch:
                for (int i = 0; i < menuItems.length; i++) {
                    menu.add(Menu.NONE, i, i, menuItems[i]);
                }
                contextListChoice = 3;
                break;
            case R.id.listDinner:
                for (int i = 0; i < menuItems.length; i++) {
                    menu.add(Menu.NONE, i, i, menuItems[i]);
                }
                contextListChoice = 4;
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        contextListPosition = info.position;
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity()).setTitle("Move Meal To:");
        final ArrayAdapter<String> mAdapterMoveMeal = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
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
                if (contextListChoice == 1) {
                    LogMeal logMeals = mLogSnackAdapter.getItem(contextListPosition);
                    logMeals.setMealChoice(strName);
                    logMeals.edit();
                    initializeAdapters(mDate);
                }
                if (contextListChoice == 2) {
                    LogMeal logMeals = mLogBreakfastAdapter.getItem(contextListPosition);
                    logMeals.setMealChoice(strName);
                    logMeals.edit();
                    initializeAdapters(mDate);
                }
                if (contextListChoice == 3) {
                    LogMeal logMeals = mLogLunchAdapter.getItem(contextListPosition);
                    logMeals.setMealChoice(strName);
                    logMeals.edit();
                    initializeAdapters(mDate);
                }
                if (contextListChoice == 4) {
                    LogMeal logMeals = mLogDinnerAdapter.getItem(contextListPosition);
                    logMeals.setMealChoice(strName);
                    logMeals.edit();
                    initializeAdapters(mDate);
                }
                updateListViews();
            }

        });
        switch (contextListChoice) {
            case 1:
                switch (menuItemIndex) {
                    case 0:
                        LogMeal logMeals = mLogSnackAdapter.getItem(contextListPosition);
                        logMeals.delete();
                        mLogSnackAdapter.remove(logMeals);
                        mLogSnackAdapter.notifyDataSetChanged();
                        initializeAdapters(new Date());
                        refreshOnDelete();
                        updateListViews();
                        equations();
                        break;
                    case 1:
                        builderSingle.show();
                        break;
                }
                break;
            case 2:
                switch (menuItemIndex) {
                    case 0:
                        LogMeal logMeals = mLogBreakfastAdapter.getItem(contextListPosition);
                        logMeals.delete();
                        mLogBreakfastAdapter.remove(logMeals);
                        mLogBreakfastAdapter.notifyDataSetChanged();
                        initializeAdapters(new Date());
                        refreshOnDelete();
                        updateListViews();
                        equations();
                        break;
                    case 1:
                        builderSingle.show();
                        break;
                }
                break;
            case 3:
                switch (menuItemIndex) {
                    case 0:
                        LogMeal logMeals = mLogLunchAdapter.getItem(contextListPosition);
                        logMeals.delete();
                        mLogLunchAdapter.remove(logMeals);
                        mLogLunchAdapter.notifyDataSetChanged();
                        initializeAdapters(new Date());
                        refreshOnDelete();
                        updateListViews();
                        equations();
                        break;
                    case 1:
                        builderSingle.show();
                        break;
                }
                break;
            case 4:
                switch (menuItemIndex) {
                    case 0:
                        LogMeal logMeals = mLogDinnerAdapter.getItem(contextListPosition);
                        logMeals.delete();
                        mLogDinnerAdapter.remove(logMeals);
                        mLogDinnerAdapter.notifyDataSetChanged();
                        initializeAdapters(new Date());
                        refreshOnDelete();
                        updateListViews();
                        equations();
                        break;
                    case 1:
                        builderSingle.show();
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Display Calorie Goal Indicator Information
     */
    private void calorieInfoDialog(String s) {
        double calMin = mCalorieGoalMeal - 100;
        double calMax = mCalorieGoalMeal + 100;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(s + " Calorie Goal").setMessage("Goal:   " + df.format(calMin) + "  -  " + df.format(calMax) + " Calories")
            .setPositiveButton("Done", null);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        FrameLayout f1 = new FrameLayout(getActivity());
        f1.addView(inflater.inflate(R.layout.dialog_calorie_info, f1, false));
        builder.setView(f1);
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * FitBit Ini
     */
    Bundle savedState;
    TextView fbCaloriesBurned;
    ImageView fbRefresh;
    ProgressBar pbLoad;
    TextView fbCaloriesGoal, fbCaloriesConsumed;

    private void InitiateFitBit(final Date date) {
        llFitBit = (LinearLayout) v.findViewById(R.id.llFitBit);
        fbCaloriesBurned = (TextView) v.findViewById(R.id.fbCaloriesBurned);
        pbLoad = (ProgressBar) v.findViewById(R.id.pbLoad);

        if (sharedPreferences.getString("FITBIT_CONNECTION_STATUS", "").equals("CONNECTED")) {
            llFitBit.setVisibility(View.VISIBLE);
            fbRefresh = (ImageView) v.findViewById(R.id.fbRefresh);
            fbRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FitBitCaloriesBurned(getActivity(), date).execute();
                }
            });
            new FitBitCaloriesBurned(getActivity(), date).execute();
        } else {
            llFitBit.setVisibility(View.GONE);
        }
        //todo

    }

    public void FitBit(String s) {
        fbCaloriesBurned.setText(s);
        pbLoad.setVisibility(View.GONE);
        fbRefresh.setVisibility(View.VISIBLE);
        int caloriesBurned = Integer.valueOf(s);
        double mCalorieGoal = Double.valueOf(sharedPreferences.getString(Globals.USER_CALORIES_TO_REACH_GOAL, "")) + caloriesBurned;
        fbCaloriesNew.setText("" + df.format(mCalorieGoal));
        fbCaloriesGoal = (TextView) v.findViewById(R.id.fbCaloriesGoalNew);
        fbCaloriesGoal.setText("" + df.format(mCalorieGoal));
        fbCaloriesConsumed = (TextView) v.findViewById(R.id.fbCaloriesConsumed);
        double mAllCaloriesConsumed = 0;
        for (LogMeal logMeal : mLogAdapterAll.getLogs()) {
            mAllCaloriesConsumed += logMeal.getCalorieCount();
        }
        fbCaloriesConsumed.setText(df.format(mAllCaloriesConsumed));
        ProgressBar progressFitbit = (ProgressBar) v.findViewById(R.id.progressFitbit);
        progressFitbit.setMax(Integer.valueOf(df.format(mCalorieGoal)));
        progressFitbit.setProgress(Integer.valueOf(df.format(mAllCaloriesConsumed)));
    }

    public void FitBitLoading() {
        fbCaloriesBurned.setText("...");
        pbLoad.setVisibility(View.VISIBLE);
        fbRefresh.setVisibility(View.GONE);
    }

    /**
     * Interface to communicate to the parent activity (MainActivity.java)
     */
    private FragmentCallbacks mCallbacks;

    public interface FragmentCallbacks {
        void openNavigationDrawer();

        void openQuickAdd(String mealType);

        void viewMeal(int mId, String MealType, int position, Date d);

        void viewSuggestion(String s, Date d);

        void searchFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment One.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
