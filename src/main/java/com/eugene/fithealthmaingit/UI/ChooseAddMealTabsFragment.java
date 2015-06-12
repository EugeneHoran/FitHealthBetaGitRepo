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
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.Adapters.ChooseAddMealPagerAdapter.ChooseAddMealPagerAdapter;
import com.eugene.fithealthmaingit.Utilities.Globals;


public class ChooseAddMealTabsFragment extends Fragment {
    private Toolbar mToolbar;
    private String mealType;
    private LogQuickSearchSimpleAdapter mRecentLogAdapter;
    private LogAdapterAll mLogAdapterFavorite;
    private LogAdapterManual mLogAdapterManual;
    private LinearLayout llNoRecentFav, llNoRecentManual, llNoRecentSearch;
    private EditText manualSearch, favSearch;
    private ImageView clearSearch, image_search_back, image_search_back_fav, clearSearchFav;
    private CardView card_search_manual, card_search_fav;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_add_item_pager, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.setStatusBarColor(getResources().getColor(R.color.accent_dark));
        }
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
        }
        ToolbarSetup();
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mToolbar.getWindowToken(), 0);
        ChooseAddMealPagerAdapter myPagerAdapterAdd = new ChooseAddMealPagerAdapter();
        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.pager);
        TabLayout tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setTabTextColors(Color.parseColor("#80ffffff"), Color.parseColor("#ffffff"));
        mViewPager.setAdapter(myPagerAdapterAdd);
        mViewPager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(mViewPager);
        card_search_manual = (CardView) v.findViewById(R.id.card_search_manual);
        card_search_fav = (CardView) v.findViewById(R.id.card_search_fav);
        setListAdapters();
        updateListView();
        searchManualEntry();
        searchFav();
        return v;
    }

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

    private void ToolbarSetup() {
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar_search_main);
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
    }

    ListView mListRecentSearches;
    ListView mListFavorites;
    ListView listViewManual;

    private void setListAdapters() {
        mListRecentSearches = (ListView) v.findViewById(R.id.listRecentSearches);
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
    }

    private void updateListView() {
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
    }

    /**
     * Interface to transfer data to MainActivity
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
