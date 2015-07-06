package com.eugene.fithealthmaingit;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterTesting;
import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogMeal;

import java.util.Date;

public class TestingActvity extends AppCompatActivity {
    LogAdapterTesting logAdapterTesting;
    Boolean[] field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_actvity);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setDrawSelectorOnTop(true);
        logAdapterTesting = new LogAdapterTesting(this, 0, LogMeal.logSortByTesting(new Date()));
        listView.setAdapter(logAdapterTesting);
        field = new Boolean[logAdapterTesting.getCount()];

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_voice) {
                    for (int i = 0; i < logAdapterTesting.getCount(); i++) {

                        if (field[i] == null) {
                            field[i] = false;
                        }
                        if (field[i] == true) {
                            Log.e("POSITIONS", i + "");
                            final int pos = i;
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    LogMeal logMeal = logAdapterTesting.getItem(pos);
                                    logMeal.delete();
                                    logAdapterTesting.remove(logMeal);
                                    logAdapterTesting.notifyDataSetChanged();
                                }
                            }, 1000);
                        }
                    }
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void AddItem(int position, boolean checked) {
        field[position] = checked;
    }
}