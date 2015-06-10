package com.eugene.fithealthmaingit.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

public class QuickAddActivity extends ActionBarActivity implements QuickAddFragment.QuickAddItemAdded {
    private String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_add);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
        }

        Bundle bundle = new Bundle();
        Fragment fragment = new QuickAddFragment();
        bundle.putString(Globals.MEAL_TYPE, mealType);
        fragment.setArguments(bundle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerQuickAdd, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void itemAdded(String s) {
    }
}
