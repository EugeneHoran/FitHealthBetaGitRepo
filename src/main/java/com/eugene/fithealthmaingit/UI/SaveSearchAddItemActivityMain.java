package com.eugene.fithealthmaingit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eugene.fithealthmaingit.MainActivityController;
import com.eugene.fithealthmaingit.R;

public class SaveSearchAddItemActivityMain extends AppCompatActivity implements SaveSearchItemFragment.FragmentCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_search_item);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SaveSearchItemFragment()).commit();
    }

    @Override
    public void fromFragment() {
        Intent intent = new Intent(this, MainActivityController.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
