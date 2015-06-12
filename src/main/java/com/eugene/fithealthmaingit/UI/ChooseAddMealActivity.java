package com.eugene.fithealthmaingit.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;


public class ChooseAddMealActivity extends AppCompatActivity implements ChooseAddMealTabsFragment.FragmentCallbacks {
    private Fragment fragment = null;
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_item);
        container = (FrameLayout) findViewById(R.id.container);
        fragment = new ChooseAddMealTabsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(container.getWindowToken(), 0);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, Globals.RETAIN_FRAGMENT, fragment);
    }

    @Override
    public void recentSearchClicked(String s) {
        Bundle b = new Bundle();
        b.putString("MealName", s);
        Fragment searchItem = new ChooseAddMealSearchFragment();
        searchItem.setArguments(b);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, android.R.anim.fade_out).replace(R.id.container, searchItem).addToBackStack(null).commit();
    }

    @Override
    public void searchClicked() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, android.R.anim.fade_out).replace(R.id.container, new ChooseAddMealSearchFragment()).addToBackStack(null).commit();
    }

}
