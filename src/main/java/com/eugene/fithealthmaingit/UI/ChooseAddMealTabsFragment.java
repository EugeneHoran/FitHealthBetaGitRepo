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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eugene.fithealthmaingit.Databases.FoodManual.LogAdapterManual;
import com.eugene.fithealthmaingit.Databases.FoodManual.LogManual;
import com.eugene.fithealthmaingit.Databases.LogFood.LogAdapterAll;
import com.eugene.fithealthmaingit.Databases.LogFood.LogMeal;
import com.eugene.fithealthmaingit.LogQuickSearchData.LogQuickSearch;
import com.eugene.fithealthmaingit.LogQuickSearchData.LogQuickSearchAdapter;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.Adapters.ChooseAddMealPagerAdapter.MyPagerAdapterAdd;
import com.eugene.fithealthmaingit.Utilities.Globals;


public class ChooseAddMealTabsFragment extends Fragment {

    private String mealType;
    private LogQuickSearchAdapter mRecentLogAdapter;
    private LogAdapterAll mLogAdapterFavorite;
    private LogAdapterManual mLogAdapterManual;
    private LinearLayout llNoRecentFav, llNoRecentManual, llNoRecentSearch;
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

        MyPagerAdapterAdd myPagerAdapterAdd = new MyPagerAdapterAdd();
        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.pager);
        TabLayout tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setTabTextColors(Color.parseColor("#80ffffff"), Color.parseColor("#ffffff"));
        mViewPager.setAdapter(myPagerAdapterAdd);
        mViewPager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(mViewPager);

        setListAdapters();
        updateListView();
        return v;
    }

    private void ToolbarSetup() {
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
    }


    private void setListAdapters() {
        ListView mListRecentSearches = (ListView) v.findViewById(R.id.listRecentSearches);
        ListView mListFavorites = (ListView) v.findViewById(R.id.listFavorites);
        ListView listViewManual = (ListView) v.findViewById(R.id.listViewManual);

        llNoRecentFav = (LinearLayout) v.findViewById(R.id.llNoRecentFav);
        llNoRecentManual = (LinearLayout) v.findViewById(R.id.llNoRecentManual);
        llNoRecentSearch = (LinearLayout) v.findViewById(R.id.llNoRecentSearch);

        mRecentLogAdapter = new LogQuickSearchAdapter(getActivity(), 0, LogQuickSearch.all());
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
        if (mRecentLogAdapter.getCount() != 0) // Check for any recent Logs
            llNoRecentSearch.setVisibility(View.GONE);
        else
            llNoRecentSearch.setVisibility(View.VISIBLE);

        if (mLogAdapterFavorite.getCount() != 0)// Check for any favorite Logs
            llNoRecentFav.setVisibility(View.GONE);
        else
            llNoRecentFav.setVisibility(View.VISIBLE);
        if (mLogAdapterManual.getCount() == 0) {
            llNoRecentManual.setVisibility(View.VISIBLE);
        } else {
            llNoRecentManual.setVisibility(View.GONE);
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
