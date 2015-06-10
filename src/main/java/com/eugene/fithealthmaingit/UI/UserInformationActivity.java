package com.eugene.fithealthmaingit.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.eugene.fithealthmaingit.R;


public class UserInformationActivity extends ActionBarActivity {
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        fragment = new UserInformationFragment();
        if (savedInstanceState != null)
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "retain_fragment");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "retain_fragment", fragment);
    }

}
