package com.eugene.fithealthmaingit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.MainActivityController;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.Utilities.Globals;

public class ManualEntryActivity extends AppCompatActivity {
    private Fragment fragment = null;
    String mealType, food_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mealType = extras.getString(Globals.MEAL_TYPE);
            food_id = extras.getString(Globals.MEAL_ID);
            if (food_id != null) {
                fragment = new ManualEntryFragmentUpdate();
                getSupportFragmentManager().beginTransaction().replace(R.id.containerManual, fragment).commit();
            } else {
                fragment = new ManualEntryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.containerManual, fragment).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivityController.class);
        intent.putExtra(Globals.MEAL_TYPE, mealType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
