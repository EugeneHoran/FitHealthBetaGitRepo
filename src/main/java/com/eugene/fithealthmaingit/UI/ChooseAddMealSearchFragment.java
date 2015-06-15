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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList.SearchAdapterItemResult;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList.SearchItemResult;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearchAdapter;
import com.eugene.fithealthmaingit.FatSecretSearchAndGet.FatSecretSearchMethod;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Equations;
import com.eugene.fithealthmaingit.Utilities.Globals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Search FatSecret Rest Api
 * Add items to LogQuickSearch
 */
public class ChooseAddMealSearchFragment extends Fragment implements AbsListView.OnScrollListener {
    private String mealType;
    private ListView mListViewSearchResults;
    private Toolbar mToolbarSearch;
    private EditText mEtSearch;
    private SearchAdapterItemResult mEtSearchAdapter;
    private ArrayList<SearchItemResult> mItem;
    private Set<String> set;
    private FatSecretSearchMethod mFatSecretSearch;
    private LogQuickSearchAdapter mRecentLogAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int preLast;
    private String brand;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_add_item_fatsecret, container, false);
        // Change the notification bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.setStatusBarColor(getResources().getColor(R.color.light_grey));
        }
        // Get MealType (snack, breakfast, lunch, dinner)
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
        }
        // Initialize FatSecretSearchMethod
        mFatSecretSearch = new FatSecretSearchMethod();

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.red, R.color.primary_dark);
        mSwipeRefreshLayout.setProgressViewOffset(true, Equations.dpToPx(getActivity(), 0), Equations.dpToPx(getActivity(), 112));
        mSwipeRefreshLayout.setEnabled(false);

        mEtSearch = (EditText) v.findViewById(R.id.etSearch);
        // LogQuickSearch adapter to add items that have not been saved already
        mRecentLogAdapter = new LogQuickSearchAdapter(getActivity(), 0, LogQuickSearch.all());
        // List for adding items to the
        mItem = new ArrayList<>();
        // Set to prevent duplicates being saved in LogQuickSearch
        set = new HashSet<>();
        /**
         * set mListViewSearchResults adapter to SearchAdapterItemResult
         */
        mEtSearchAdapter = new SearchAdapterItemResult(getActivity(), mItem);
        mListViewSearchResults = (ListView) v.findViewById(R.id.listView);
        mListViewSearchResults.setAdapter(mEtSearchAdapter);
        mListViewSearchResults.setOnScrollListener(this);
        mListViewSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), SaveSearchAddItemActivityMain.class);
                i.putExtra(Globals.MEAL_TYPE, mealType);
                i.putExtra(Globals.MEAL_ID, mItem.get(position - 1).getID());
                i.putExtra(Globals.MEAL_BRAND, mItem.get(position - 1).getBrand());
                i.putExtra(Globals.MEAL_FAVORITE, "false");
                startActivity(i);
            }
        });
        // Toolbar
        mToolbarSearch = (Toolbar) v.findViewById(R.id.toolbar_search);
        mToolbarSearch.inflateMenu(R.menu.menu_search);
        mToolbarSearch.setNavigationIcon(R.mipmap.ic_arrow_back_grey);
        mToolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        mToolbarSearch.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_clear) {
                    mEtSearch.setText("");
                }
                if (menuItem.getItemId() == R.id.action_voice) {
                    promptSpeechInput();
                }
                return false;
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtSearch.getText().toString().length() >= 1) {
                    mToolbarSearch.getMenu().clear();
                    mToolbarSearch.inflateMenu(R.menu.menu_search_clear);
                } else {
                    mToolbarSearch.getMenu().clear();
                    mToolbarSearch.inflateMenu(R.menu.menu_search);
                }
                mItem.clear();
                updateListView();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Quick Search Clicked, start search based on list Item clicked from last fragment
        Bundle extrasFromRecentCliced = this.getArguments();
        if (extrasFromRecentCliced != null) {
            String mealName = extrasFromRecentCliced.getString("MealName");
            if (mealName != null) {
                mEtSearch.setText(mealName);
                mToolbarSearch.getMenu().clear();
                mToolbarSearch.inflateMenu(R.menu.menu_search_clear);
                searchFood(mealName, 0);
            }
        } else {
            mEtSearch.requestFocus();
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        /**
         * Handles softKeyboard search icon being clicked
         */
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
                    searchFood(mEtSearch.getText().toString(), 0);
                    mItem.clear();
                    mEtSearchAdapter.notifyDataSetChanged();
                    mEtSearch.clearFocus();
                    return true;
                }
                return false;
            }
        });
        updateListView();
        return v;
    }


    /**
     * Adds more items to the list if it is scrolled to the bottom
     */
    private void addMoreListItems() {
        if (mItem.size() == 20) // needs to be refactored
            searchFood(mEtSearch.getText().toString(), 1);
        else if (mItem.size() == 40)
            searchFood(mEtSearch.getText().toString(), 2);
        else if (mItem.size() == 60)
            searchFood(mEtSearch.getText().toString(), 3);
        else if (mItem.size() == 80)
            searchFood(mEtSearch.getText().toString(), 4);
        else if (mItem.size() == 100)
            searchFood(mEtSearch.getText().toString(), 5);
        else if (mItem.size() == 120)
            searchFood(mEtSearch.getText().toString(), 6);
        else if (mItem.size() == 140)
            searchFood(mEtSearch.getText().toString(), 7);
        else if (mItem.size() == 160)
            searchFood(mEtSearch.getText().toString(), 8);
        else if (mItem.size() == 180)
            searchFood(mEtSearch.getText().toString(), 9);
        else if (mItem.size() == 200)
            searchFood(mEtSearch.getText().toString(), 10);

    }

    /**
     * Update the ListView based on mItem size
     */
    private void updateListView() {
        if (mItem.size() == 0) {
            mListViewSearchResults.setVisibility(View.GONE);
        } else {
            mListViewSearchResults.setVisibility(View.VISIBLE);
        }
        mEtSearchAdapter.notifyDataSetChanged();
    }

    /**
     * FatSecret Search method.
     * Connect to Rest and return search results based on text and page number
     * Implements FatSecretSearchMethod.class
     *
     * @param item     string to search item
     * @param page_num currently showing 20 items per page. Adds more items if page num increases
     */
    private void searchFood(final String item, final int page_num) {
        /**
         * Add Items to the recent database if item is added to the hash set
         */
        for (int i = 0; i < mRecentLogAdapter.getCount(); i++) {
            LogQuickSearch ls = mRecentLogAdapter.getItem(i);
            String name = ls.getName();
            set.add(name.toUpperCase());
        }
        if (set.add(item.toUpperCase())) {
            LogQuickSearch recentLog = new LogQuickSearch();
            recentLog.setName(item);
            recentLog.setDate(new Date());
            recentLog.save();
            mRecentLogAdapter.add(recentLog);
            mRecentLogAdapter.notifyDataSetChanged();
        }

        AsyncTask<String, String, String> mAsyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                mSwipeRefreshLayout.setEnabled(true);
                mSwipeRefreshLayout.setRefreshing(true);

            }

            @Override
            protected String doInBackground(String... arg0) {
                JSONObject food = mFatSecretSearch.searchFood(item, page_num);
                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                String food_name = food_items.getString("food_name");
                                String food_description = food_items.getString("food_description");
                                String[] row = food_description.split("-");
                                String id = food_items.getString("food_type");
                                if (id.equals("Brand")) {
                                    brand = food_items.getString("brand_name");
                                }
                                if (id.equals("Generic")) {
                                    brand = "Generic";
                                }
                                String food_id = food_items.getString("food_id");
                                mItem.add(new SearchItemResult(food_name, row[1].substring(1),
                                    "" + brand, food_id));
                            }
                        }
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onCancelled() {
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Error"))
                    Toast.makeText(getActivity(), "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
                updateListView();
            }
        };
        mAsyncTask.execute();
    }


    /**
     * Speech Input
     * Voice search then implements search method based on result
     */
    public static int REQ_CODE_SPEECH_INPUT = 100;

    private void promptSpeechInput() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
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
                mEtSearch.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                mEtSearch.setText(result.get(0));
                searchFood(mEtSearch.getText().toString(), 0);
                mItem.clear();
                mEtSearchAdapter.notifyDataSetChanged();
                mEtSearch.clearFocus();
            }
        }
    }

    /**
     * Calls addMoreListItems() if the last list position is visible
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        switch (view.getId()) {
            case R.id.listView:
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) {
                        addMoreListItems();
                        preLast = lastItem;
                    }
                }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

}

