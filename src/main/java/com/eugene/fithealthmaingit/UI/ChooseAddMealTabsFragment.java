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

import android.animation.Animator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eugene.fithealthmaingit.Custom.TextViewFont;
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
import com.eugene.fithealthmaingit.Utilities.Equations;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        TextViewFont txtTitle = (TextViewFont) v.findViewById(R.id.txtTitle);
        if (txtTitle != null)
            txtTitle.setText(mealType);
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
        ViewGroup headerFav = (ViewGroup) inflater.inflate(R.layout.list_header_search_favorites, listViewManual, false);
        mListFavorites.addHeaderView(headerFav, null, false);
        mListFavorites.setAdapter(mLogAdapterFavorite);
        mListFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchManual.getWindowToken(), 0);
                LogMeal logMeal = mLogAdapterFavorite.getItem(position - 1);
                Intent i = new Intent(getActivity(), SaveSearchAddItemActivityMain.class);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                i.putExtra(Globals.MEAL_ID, logMeal.getMealId());
                i.putExtra(Globals.MEAL_BRAND, logMeal.getBrand());
                i.putExtra(Globals.MEAL_FAVORITE, logMeal.getFavorite());
                startActivity(i);
            }
        });

        mLogAdapterManual = new LogAdapterManual(getActivity(), 0, LogManual.all(), mealType);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_header_search, listViewManual, false);
        listViewManual.addHeaderView(header, null, false);
        listViewManual.setAdapter(mLogAdapterManual);
        listViewManual.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchManual.getWindowToken(), 0);
                LogManual logManual = mLogAdapterManual.getItem(position - 1);
                Intent i = new Intent(getActivity(), ManualEntrySaveMealActivity.class);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                i.putExtra(Globals.MEAL_ID, logManual.getMealId());
                startActivity(i);
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ((RelativeLayout.LayoutParams) card_search_manual.getLayoutParams()).setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
            ((RelativeLayout.LayoutParams) card_search_fav.getLayoutParams()).setMargins(0, 0, 0, 0); // get rid of margins since shadow area is now the margin
            headerFav.setPadding(0, 0, 0, Equations.dpToPx(getActivity(), 16));
            header.setPadding(0, 0, 0, Equations.dpToPx(getActivity(), 16));
        }

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

        searchManual = (Button) v.findViewById(R.id.searchManual);
        searchManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchManual();
            }
        });
        searchFavorite = (Button) v.findViewById(R.id.searchFav);
        searchFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchFavorite();
            }
        });

        updateListView();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchManual.getWindowToken(), 0);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return v;
    }

    Button searchManual;
    Button searchFavorite;

    private void handleSearchManual() {
        if (card_search_manual.getVisibility() == View.VISIBLE) {
            searchManual.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animatorHide = ViewAnimationUtils.createCircularReveal(card_search_manual,
                    card_search_manual.getWidth() - (int) convertDpToPixel(24, getActivity()),
                    (int) convertDpToPixel(23, getActivity()),
                    (float) Math.hypot(card_search_manual.getWidth(), card_search_manual.getHeight()),
                    0);
                animatorHide.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        card_search_manual.setVisibility(View.GONE);
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchManual.getWindowToken(), 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                animatorHide.setDuration(200);
                animatorHide.start();
            } else {
                card_search_manual.setVisibility(View.GONE);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchManual.getWindowToken(), 0);
            }
        } else {
            searchManual.setVisibility(View.INVISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animator = ViewAnimationUtils.createCircularReveal(card_search_manual,
                    card_search_manual.getWidth() - (int) convertDpToPixel(24, getActivity()),
                    (int) convertDpToPixel(23, getActivity()),
                    0,
                    (float) Math.hypot(card_search_manual.getWidth(), card_search_manual.getHeight()));
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        manualSearch.requestFocus();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                card_search_manual.setVisibility(View.VISIBLE);
                if (card_search_manual.getVisibility() == View.VISIBLE) {
                    animator.setDuration(300);
                    animator.start();
                    card_search_manual.setEnabled(true);
                }
            } else {
                manualSearch.requestFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                card_search_manual.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleSearchFavorite() {
        if (card_search_fav.getVisibility() == View.VISIBLE) {
            searchFavorite.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animatorHide = ViewAnimationUtils.createCircularReveal(card_search_fav,
                    card_search_fav.getWidth() - (int) convertDpToPixel(24, getActivity()),
                    (int) convertDpToPixel(23, getActivity()),
                    (float) Math.hypot(card_search_fav.getWidth(), card_search_fav.getHeight()),
                    0);
                animatorHide.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        card_search_fav.setVisibility(View.GONE);
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchFavorite.getWindowToken(), 0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                animatorHide.setDuration(200);
                animatorHide.start();
            } else {
                favSearch.requestFocus();
                card_search_fav.setVisibility(View.GONE);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchFavorite.getWindowToken(), 0);
            }
        } else {
            searchFavorite.setVisibility(View.INVISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Animator animator = ViewAnimationUtils.createCircularReveal(card_search_fav,
                    card_search_fav.getWidth() - (int) convertDpToPixel(24, getActivity()),
                    (int) convertDpToPixel(23, getActivity()),
                    0,
                    (float) Math.hypot(card_search_fav.getWidth(), card_search_fav.getHeight()));
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        favSearch.requestFocus();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        favSearch.requestFocus();
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                card_search_fav.setVisibility(View.VISIBLE);
                if (card_search_fav.getVisibility() == View.VISIBLE) {
                    animator.setDuration(300);
                    animator.start();
                }
            } else {
                favSearch.requestFocus();
                card_search_fav.setVisibility(View.VISIBLE);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
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
                } else {
                    clearSearch.setImageResource(R.mipmap.ic_clear);
                    mLogAdapterManual = new LogAdapterManual(getActivity(), 0, LogManual.logsMealName(manualSearch.getText().toString()), mealType);
                    listViewManual.setAdapter(mLogAdapterManual);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manualSearch.getText().toString().trim().length() == 0) {
                    SEARCH_VOICE = "MANUAL";
                    promptSpeechInput(manualSearch);
                }
                manualSearch.setText("");
            }
        });
        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manualSearch.getText().toString().length() != 0) {
                    manualSearch.setText("");
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(manualSearch.getWindowToken(), 0);
                }
                handleSearchManual();
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
                } else {
                    clearSearchFav.setImageResource(R.mipmap.ic_clear);
                    mLogAdapterFavorite = new LogAdapterAll(getActivity(), 0, LogMeal.logSortByFavoriteMeal("favorite", favSearch.getText().toString()));
                    mListFavorites.setAdapter(mLogAdapterFavorite);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        clearSearchFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favSearch.getText().toString().trim().length() == 0) {
                    SEARCH_VOICE = "FAVORITE";
                    promptSpeechInput(favSearch);
                }
                favSearch.setText("");
            }
        });
        image_search_back_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favSearch.getText().toString().trim().length() != 0) {
                    favSearch.setText("");
                    favSearch.clearFocus();
                }
                handleSearchFavorite();
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
            searchFavorite.setVisibility(View.VISIBLE);
        } else {
            llNoRecentFav.setVisibility(View.VISIBLE);
            searchFavorite.setVisibility(View.GONE);
        }

        if (mLogAdapterManual.getCount() == 0) {
            llNoRecentManual.setVisibility(View.VISIBLE);
            searchManual.setVisibility(View.GONE);
        } else {
            llNoRecentManual.setVisibility(View.GONE);
            searchManual.setVisibility(View.VISIBLE);
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
     * Speech Input
     * Voice search then implements search method based on result
     */
    public static int REQ_CODE_SPEECH_INPUT = 100;
    public static String SEARCH_VOICE = "";

    private void promptSpeechInput(EditText e) {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(e.getWindowToken(), 0);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set the text based on google voice then implement search
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (SEARCH_VOICE.equals("MANUAL")) {
                    manualSearch.setText(result.get(0));
                }
                if (SEARCH_VOICE.equals("FAVORITE")) {
                    favSearch.setText(result.get(0));
                }
            }
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
