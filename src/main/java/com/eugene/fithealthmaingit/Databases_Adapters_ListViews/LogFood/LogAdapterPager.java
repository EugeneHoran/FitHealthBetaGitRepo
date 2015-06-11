package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class LogAdapterPager extends ArrayAdapter<LogMeal> {
    Context mContext;
    public static List<LogMeal> mLogs;

    public LogAdapterPager(Context context, int textViewResourceId, List<LogMeal> logs) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
    }

    public void setLogs(List<LogMeal> logs) {
        mLogs = logs;
    }

    public List<LogMeal> getLogs() {
        return mLogs;
    }

    public void add(LogMeal log) {
        mLogs.add(log);
    }

    public void remove(LogMeal log) {
        LogAdapterPager.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public LogMeal getItem(int position) {
        return mLogs.get(position);
    }
}
