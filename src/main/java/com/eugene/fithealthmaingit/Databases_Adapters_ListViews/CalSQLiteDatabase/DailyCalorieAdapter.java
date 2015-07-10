package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.CalSQLiteDatabase;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class DailyCalorieAdapter extends ArrayAdapter<DailyCalorieIntake> {
    // List context
    private final Context context;
    // List values
    private final List<DailyCalorieIntake> dailyCalorieIntake;

    public DailyCalorieAdapter(Context contextx, int textViewResourceId, List<DailyCalorieIntake> logs) {
        super(contextx, textViewResourceId);
        context = contextx;
        dailyCalorieIntake = logs;
    }

    // add item to adapter
    public void add(DailyCalorieIntake log) {
        dailyCalorieIntake.add(log);
    }

    // remove from adapter
    public void remove(DailyCalorieIntake log) {
        dailyCalorieIntake.remove(log);
    }

    // get adapter count
    public int getCount() {
        return dailyCalorieIntake.size();
    }

    // get dailyCalorieIntake by position
    public DailyCalorieIntake getItem(int position) {
        return dailyCalorieIntake.get(position);
    }

}