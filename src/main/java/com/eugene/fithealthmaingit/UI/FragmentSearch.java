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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList.SearchAdapterItemResult;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList.SearchItemResult;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearchAdapter;
import com.eugene.fithealthmaingit.FatSecretSearchAndGet.FatSecretSearchMethod;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;
import com.eugene.fithealthmaingit.Utilities.InitiateSearch;
import com.eugene.fithealthmaingit.Utilities.NetworkConnectionStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Search Fragment
 * Searched Fat Secret Rest Api
 * Overlays FragmentJournalMain
 */
public class FragmentSearch extends Fragment {
    private View line_divider;
    private RelativeLayout view_search;
    private CardView card_search;
    private ImageView image_search_back, clearSearch;
    private EditText edit_text_search;
    private ListView listView, listContainer;
    LinearLayout searchBack;
    private LogQuickSearchAdapter logQuickSearchAdapter;
    private Set<String> set;
    private ArrayList<SearchItemResult> mItem;
    private FatSecretSearchMethod mFatSecretSearch;
    private SearchAdapterItemResult searchAdapter;
    private String brand;
    private AsyncTask<String, String, String> mAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        view_search = (RelativeLayout) v.findViewById(R.id.view_search);
        edit_text_search = (EditText) v.findViewById(R.id.edit_text_search);
        edit_text_search.requestFocus();
        card_search = (CardView) v.findViewById(R.id.card_search);
        image_search_back = (ImageView) v.findViewById(R.id.image_search_back);
        listView = (ListView) v.findViewById(R.id.listNaviationDrawer);
        listView.setVisibility(View.GONE);
        listContainer = (ListView) v.findViewById(R.id.listContainer);
        clearSearch = (ImageView) v.findViewById(R.id.clearSearch);
        line_divider = v.findViewById(R.id.line_divider);
        logQuickSearchAdapter = new LogQuickSearchAdapter(getActivity(), 0, LogQuickSearch.all());
        mItem = new ArrayList<>();
        searchAdapter = new SearchAdapterItemResult(getActivity(), mItem);
        listView.setAdapter(logQuickSearchAdapter);
        listContainer.setAdapter(searchAdapter);
        set = new HashSet<>();
        mFatSecretSearch = new FatSecretSearchMethod();
        searchBack = (LinearLayout) v.findViewById(R.id.searchBack);
        InitiateSearch();
        HandleSearch();
        view_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitiateSearch.handleToolBar(getActivity(), card_search, view_search, listView, edit_text_search, line_divider);
                endFragment();
            }
        });
        InitiateClick();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    String mealType = "";
    String mealId1 = "";
    String mealBrand = "";
    String mealFaborite = "";

    private void InitiateClick() {
        listContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mealId1 = mItem.get(position).getID();
                mealBrand = mItem.get(position).getBrand();
                mealFaborite = "false";
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity()).setTitle("Choose Meal: ");
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
                        Intent i = new Intent(getActivity(), SaveSearchAddItemActivityMain.class);
                        i.putExtra(Globals.MEAL_TYPE, strName);
                        i.putExtra(Globals.MEAL_FAVORITE, "false");
                        i.putExtra(Globals.MEAL_ID, mealId1);
                        i.putExtra(Globals.MEAL_BRAND, mealBrand);
                        startActivity(i);
                    }
                });
                builderSingle.show();
            }
        });
    }

    private void InitiateSearch() {
        card_search.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                InitiateSearch.handleToolBar(getActivity(), card_search, view_search, listView, edit_text_search, line_divider);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogQuickSearch logQuickSearch = logQuickSearchAdapter.getItem(position);
                edit_text_search.setText(logQuickSearch.getName());
                listView.setVisibility(View.GONE);
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                searchFood(logQuickSearch.getName(), 0);
            }
        });
        edit_text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edit_text_search.getText().toString().length() == 0) {
                    logQuickSearchAdapter = new LogQuickSearchAdapter(getActivity(), 0, LogQuickSearch.all());
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setImageResource(R.mipmap.ic_keyboard_voice);
                    IsAdapterEmpty();
                } else {
                    logQuickSearchAdapter = new LogQuickSearchAdapter(getActivity(), 0, LogQuickSearch.FilterByName(edit_text_search.getText().toString()));
                    listView.setAdapter(logQuickSearchAdapter);
                    clearSearch.setImageResource(R.mipmap.ic_close);
                    IsAdapterEmpty();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_text_search.getText().toString().length() != 0) {
                    edit_text_search.setText("");
                    searchBack.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    clearItems();
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    IsAdapterEmpty();
                } else {
                    promptSpeechInput(edit_text_search);
                }
            }
        });
    }

    private void HandleSearch() {
        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearItems();
                InitiateSearch.handleToolBar(getActivity(), card_search, view_search, listView, edit_text_search, line_divider);
                searchBack.setVisibility(View.GONE);
                endFragment();
            }
        });
        edit_text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edit_text_search.getText().toString().trim().length() > 0) {
                        clearItems();
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_text_search.getWindowToken(), 0);
                        UpdateQuickSearch(edit_text_search.getText().toString());
                        listView.setVisibility(View.GONE);
                        searchFood(edit_text_search.getText().toString(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void UpdateQuickSearch(String item) {
        for (int i = 0; i < logQuickSearchAdapter.getCount(); i++) {
            LogQuickSearch ls = logQuickSearchAdapter.getItem(i);
            String name = ls.getName();
            set.add(name.toUpperCase());
        }
        if (set.add(item.toUpperCase())) {
            LogQuickSearch recentLog = new LogQuickSearch();
            recentLog.setName(item);
            recentLog.setDate(new Date());
            recentLog.save();
            logQuickSearchAdapter.add(recentLog);
            logQuickSearchAdapter.notifyDataSetChanged();
        }
    }

    private void IsAdapterEmpty() {
        if (logQuickSearchAdapter.getCount() == 0) {
            line_divider.setVisibility(View.GONE);
        } else {
            line_divider.setVisibility(View.VISIBLE);
        }
    }

    private void clearItems() {
        listContainer.setVisibility(View.GONE);
        mItem.clear();
        searchAdapter.notifyDataSetChanged();
    }

    private void endFragment() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }, 300);
    }

    private void searchFood(final String item, final int page_num) {
        if (!NetworkConnectionStatus.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "Check your connection and try again", Toast.LENGTH_LONG).show();
        } else {
            mAsyncTask = new AsyncTask<String, String, String>() {
                @Override
                protected void onPreExecute() {
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
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    searchAdapter.notifyDataSetChanged();
                    if (listContainer.getCount() > 0) {
                        searchBack.setVisibility(View.VISIBLE);
                        TranslateAnimation slide = new TranslateAnimation(0, 0, listContainer.getHeight(), 0);
                        slide.setStartTime(1000);
                        listContainer.setVisibility(View.VISIBLE);
                        slide.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        slide.setDuration(400);
                        listContainer.startAnimation(slide);
                    } else {
                        searchBack.setVisibility(View.GONE);
                        listContainer.setVisibility(View.GONE);
                    }
                }

                @Override
                protected void onCancelled() {

                }
            };
            mAsyncTask.execute();
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
                edit_text_search.setText(result.get(0));
            }
        }
    }
}
