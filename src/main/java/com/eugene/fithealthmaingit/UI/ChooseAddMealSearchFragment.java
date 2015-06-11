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
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eugene.fithealthmaingit.FatSecretSearchAndGet.FatSecretSearchMethod;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogQuickSearchData.LogQuickSearchAdapter;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList.SearchItemResult;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FatSecretSearchList.SearchAdapterItemResult;
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

public class ChooseAddMealSearchFragment extends Fragment implements AbsListView.OnScrollListener {
    private View v;
    private String mealType;
    private String mealName;
    private ListView mListView;
    private RelativeLayout mRelativeLayout;
    private Toolbar mToolbarSearch;
    private EditText mEtSearch;
    private SearchAdapterItemResult mEtSearchAdapter;
    private ArrayList<SearchItemResult> mItem;
    private Set<String> set;
    private FatSecretSearchMethod mFatSecretSearch;
    private LogQuickSearchAdapter mRecentLogAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_add_item_fatsecret, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.setStatusBarColor(getResources().getColor(R.color.light_grey));
        }
        mFatSecretSearch = new FatSecretSearchMethod();
        Bundle extras = getActivity().getIntent().getExtras();
        Bundle e = this.getArguments();
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.red, R.color.primary_dark);
        mSwipeRefreshLayout.setProgressViewOffset(true, (int) Equations.dpToPx(getActivity(), 0), Equations.dpToPx(getActivity(), 112));
        mSwipeRefreshLayout.setEnabled(false);
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
        }
        mRecentLogAdapter = new LogQuickSearchAdapter(getActivity(), 0, LogQuickSearch.all());
        findViews();
        mItem = new ArrayList<>();
        toolbarSearch();
        updateRecentSearchList();
        listViewAndAdapterImplementation();
        if (e != null) {
            mealName = e.getString("MealName");
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
        searchImplementation();
        updateListView();
        return v;
    }

    /**
     * Handles the search through Soft Keyboard search button
     * Checks for network connection
     */
    private void searchImplementation() {
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
    }

    private void listViewAndAdapterImplementation() {
        mEtSearchAdapter = new SearchAdapterItemResult(getActivity(), mItem);
        mListView.setAdapter(mEtSearchAdapter);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }

    private void testing() {
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

    private void findViews() {
        mToolbarSearch = (Toolbar) v.findViewById(R.id.toolbar_search);
        mListView = (ListView) v.findViewById(R.id.listView);
        mRelativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
        mEtSearch = (EditText) v.findViewById(R.id.etSearch);
        set = new HashSet<>();
    }

    private void updateListView() {
        if (mItem.size() == 0) {
            mListView.setVisibility(View.GONE);
        } else
            mListView.setVisibility(View.VISIBLE);
        mEtSearchAdapter.notifyDataSetChanged();
    }

    private void updateRecentSearchList() {
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
    }

    private void toolbarSearch() {
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
    }

    private String brand;
    private AsyncTask<String, String, String> mAsyncTask;

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

        mAsyncTask = new AsyncTask<String, String, String>() {
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private int preLast;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        switch (view.getId()) {
            case R.id.listView:

                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) { //to avoid multiple calls for last item
                        Log.d("Last", "Last");
                        testing();
                        preLast = lastItem;
                    }
                }
        }
    }
}

