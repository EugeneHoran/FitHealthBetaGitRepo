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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual.LogAdapterManual;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual.LogManual;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearchSimpleAdapter;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeHolder;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogRecipes.LogRecipeHolderAdapter;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.Adapters.ChooseAddMealPagerAdapter.ChooseAddMealPagerAdapter;
import com.eugene.fithealthmaingit.UI.Recipe.RecipeActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.Date;

/**
 * Fragment containing Tabs with Manual Entry Items, Favorites, Recent Searches
 */
public class ChooseAddMealTabsFragment extends Fragment {
    private String mealType;
    private LogQuickSearchSimpleAdapter mRecentLogAdapter;
    private LogAdapterAll mLogAdapterFavorite;
    private LogAdapterManual mLogAdapterManual;
    private LinearLayout llNoRecentFav, llNoRecentManual, llNoRecentSearch;
    private EditText manualSearch, favSearch;
    private ImageView clearSearch, image_search_back, image_search_back_fav, clearSearchFav;
    private CardView card_search_manual, card_search_fav;
    private ListView mListFavorites;
    LogRecipeHolderAdapter logAdapterMealRecipe;
    private ListView listViewManual;
    View v;
    int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_add_item_pager, container, false);
        // Change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.setStatusBarColor(getResources().getColor(R.color.accent_dark));
        }

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
            page = extras.getInt("PAGE");
        }

        card_search_manual = (CardView) v.findViewById(R.id.card_search_manual);
        card_search_fav = (CardView) v.findViewById(R.id.card_search_fav);

        // Initiate Toolbar
        Toolbar mToolbar = (Toolbar) v.findViewById(R.id.toolbar_search_main);
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_search)
                    mCallbacks.searchClicked();
                if (menuItem.getItemId() == R.id.action_manual) {
                    Intent i = new Intent(getActivity(), ManualEntryActivity.class);
                    i.putExtra(Globals.MEAL_TYPE, mealType);
                    startActivity(i);
                }
                return false;
            }
        });
        mToolbar.setTitle("Add " + mealType);
        mToolbar.inflateMenu(R.menu.menu_search_add);

        // Hide keyboard
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);

        // Initiate Pager and Tabs
        ChooseAddMealPagerAdapter myPagerAdapterAdd = new ChooseAddMealPagerAdapter();
        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.pager);
        TabLayout tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setTabTextColors(Color.parseColor("#80ffffff"), Color.parseColor("#ffffff"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setAdapter(myPagerAdapterAdd);
        mViewPager.setOffscreenPageLimit(4);
        tabs.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(page);
        // Initiate ListView and their adapters
        ListView mListRecentSearches = (ListView) v.findViewById(R.id.listRecentSearches);
        mListFavorites = (ListView) v.findViewById(R.id.listFavorites);
        listViewManual = (ListView) v.findViewById(R.id.listViewManual);
        llNoRecentFav = (LinearLayout) v.findViewById(R.id.llNoRecentFav);
        llNoRecentManual = (LinearLayout) v.findViewById(R.id.llNoRecentManual);
        llNoRecentSearch = (LinearLayout) v.findViewById(R.id.llNoRecentSearch);
        mRecentLogAdapter = new LogQuickSearchSimpleAdapter(getActivity(), 0, LogQuickSearch.all());
        mListRecentSearches.setAdapter(mRecentLogAdapter);
        mListRecentSearches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogQuickSearch recentLog = mRecentLogAdapter.getItem(position);
                mCallbacks.recentSearchClicked(recentLog.getName());
            }
        });
        mLogAdapterFavorite = new LogAdapterAll(getActivity(), 0, LogMeal.logSortByFavorite("favorite"));
        mListFavorites.setAdapter(mLogAdapterFavorite);
        mListFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogMeal logMeal = mLogAdapterFavorite.getItem(position);
                Intent i = new Intent(getActivity(), SaveSearchAddItemActivityMain.class);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                i.putExtra(Globals.MEAL_ID, logMeal.getMealId());
                i.putExtra(Globals.MEAL_BRAND, logMeal.getBrand());
                i.putExtra(Globals.MEAL_FAVORITE, logMeal.getFavorite());
                startActivity(i);
            }
        });
        mLogAdapterManual = new LogAdapterManual(getActivity(), 0, LogManual.all(), mealType);
        listViewManual.setAdapter(mLogAdapterManual);
        listViewManual.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogManual logManual = mLogAdapterManual.getItem(position);
                Intent i = new Intent(getActivity(), ManualEntrySaveMealActivity.class);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                i.putExtra(Globals.MEAL_ID, logManual.getMealId());
                startActivity(i);
            }
        });

        searchManualEntry();
        searchFav();

        /**
         * TODO RECIPE
         */
        Button btnRecipe = (Button) v.findViewById(R.id.btnRecipe);
        btnRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogRecipeHolder newRecipe = new LogRecipeHolder();
                newRecipe.setDate(new Date());
                newRecipe.setMealChoice(mealType);
                newRecipe.setMealName("Recipe");
                newRecipe.save();
                Intent i = new Intent(getActivity(), RecipeActivity.class);
                i.putExtra(Globals.MEAL_ID, newRecipe.getId());
                i.putExtra(Globals.MEAL_TYPE, mealType);
                startActivity(i);
            }
        });
        logAdapterMealRecipe = new LogRecipeHolderAdapter(getActivity(), 0, LogRecipeHolder.all());
        listRecipes = (ListView) v.findViewById(R.id.listRecipes);
        listRecipes.setAdapter(logAdapterMealRecipe);
        listRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogRecipeHolder newRecipe = logAdapterMealRecipe.getItem(position);
                Intent i = new Intent(getActivity(), RecipeActivity.class);
                i.putExtra(Globals.MEAL_ID, newRecipe.getId());
                i.putExtra(Globals.MEAL_TYPE, mealType);
                startActivity(i);
            }
        });

        updateListView();

        return v;
    }

    ListView listRecipes;

    /**
     * Search Manual Entry Items
     * Updates widgets based on search results
     */
    private void searchManualEntry() {
        manualSearch = (EditText) v.findViewById(R.id.manualSearch);
        clearSearch = (ImageView) v.findViewById(R.id.clearSearch);
        image_search_back = (ImageView) v.findViewById(R.id.image_search_back);
        manualSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (manualSearch.getText().toString().trim().length() == 0) {
                    clearSearch.setImageResource(R.mipmap.ic_keyboard_voice);
                    mLogAdapterManual = new LogAdapterManual(getActivity(), 0, LogManual.all(), mealType);
                    listViewManual.setAdapter(mLogAdapterManual);
                    image_search_back.setImageResource(R.mipmap.ic_search);
                } else {
                    clearSearch.setImageResource(R.mipmap.ic_clear);
                    mLogAdapterManual = new LogAdapterManual(getActivity(), 0, LogManual.logsMealName(manualSearch.getText().toString()), mealType);
                    listViewManual.setAdapter(mLogAdapterManual);
                    image_search_back.setImageResource(R.mipmap.ic_arrow_back);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manualSearch.getText().toString().trim().length() != 0) {
                    manualSearch.setText("");
                }
            }
        });
        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manualSearch.getText().toString().length() != 0) {
                    manualSearch.setText("");
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(manualSearch.getWindowToken(), 0);
                }
            }
        });
    }

    /**
     * Search Favorite Entry Items
     * Updates widgets based on search results
     */
    private void searchFav() {
        favSearch = (EditText) v.findViewById(R.id.favSearch);
        image_search_back_fav = (ImageView) v.findViewById(R.id.image_search_back_fav);
        clearSearchFav = (ImageView) v.findViewById(R.id.clearSearchFav);
        favSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (favSearch.getText().toString().trim().length() == 0) {
                    clearSearchFav.setImageResource(R.mipmap.ic_keyboard_voice);
                    mLogAdapterFavorite = new LogAdapterAll(getActivity(), 0, LogMeal.logSortByFavorite("favorite"));
                    mListFavorites.setAdapter(mLogAdapterFavorite);
                    image_search_back_fav.setImageResource(R.mipmap.ic_search);
                } else {
                    clearSearchFav.setImageResource(R.mipmap.ic_clear);
                    mLogAdapterFavorite = new LogAdapterAll(getActivity(), 0, LogMeal.logSortByFavoriteMeal("favorite", favSearch.getText().toString()));
                    mListFavorites.setAdapter(mLogAdapterFavorite);
                    image_search_back_fav.setImageResource(R.mipmap.ic_arrow_back);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        clearSearchFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favSearch.getText().toString().trim().length() != 0) {
                    favSearch.setText("");
                }
            }
        });
        image_search_back_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favSearch.getText().toString().trim().length() != 0) {
                    favSearch.setText("");
                    favSearch.clearFocus();
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(favSearch.getWindowToken(), 0);
                }
            }
        });
    }

    /**
     * Update adapters, lists, and layouts based on adapter count
     * Notifies user no items have been saved
     */
    private void updateListView() {
        LinearLayout llNoRecipes = (LinearLayout) v.findViewById(R.id.llNoRecipes);
        if (mRecentLogAdapter.getCount() != 0) {
            llNoRecentSearch.setVisibility(View.GONE);
        } else {
            llNoRecentSearch.setVisibility(View.VISIBLE);
        }

        if (mLogAdapterFavorite.getCount() != 0) {
            llNoRecentFav.setVisibility(View.GONE);
            card_search_fav.setVisibility(View.VISIBLE);
        } else {
            llNoRecentFav.setVisibility(View.VISIBLE);
            card_search_fav.setVisibility(View.GONE);
        }

        if (mLogAdapterManual.getCount() == 0) {
            llNoRecentManual.setVisibility(View.VISIBLE);
            card_search_manual.setVisibility(View.GONE);
        } else {
            llNoRecentManual.setVisibility(View.GONE);
            card_search_manual.setVisibility(View.VISIBLE);
        }
        if (logAdapterMealRecipe.getCount() == 0) {
            llNoRecipes.setVisibility(View.VISIBLE);
            listRecipes.setVisibility(View.GONE);
        } else {
            llNoRecipes.setVisibility(View.GONE);
            listRecipes.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Interface
     */
    private FragmentCallbacks mCallbacks;

    public interface FragmentCallbacks {
        void recentSearchClicked(String mealName);

        void searchClicked();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment Three.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
