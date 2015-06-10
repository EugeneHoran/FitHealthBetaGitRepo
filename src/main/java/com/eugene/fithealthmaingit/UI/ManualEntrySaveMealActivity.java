package com.eugene.fithealthmaingit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.MainActivityController;
import com.eugene.fithealthmaingit.R;

public class ManualEntrySaveMealActivity extends AppCompatActivity implements ManualEntrySaveMealFragment.FragmentCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saved_manual_entry_activty);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.containerManual, new ManualEntrySaveMealFragment()).commit();
        }
    }

    @Override
    public void fromFragment() {
        Intent intent = new Intent(this, MainActivityController.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
