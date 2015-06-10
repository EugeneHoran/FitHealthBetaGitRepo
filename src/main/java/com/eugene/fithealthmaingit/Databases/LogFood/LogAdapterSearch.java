package com.eugene.fithealthmaingit.Databases.LogFood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;

import java.util.List;

/**
 * Created by Eugene on 5/17/2015.
 */
public class LogAdapterSearch extends ArrayAdapter<LogMeal> {
    Context mContext;
    public static List<LogMeal> mLogs;

    public LogAdapterSearch(Context context, int textViewResourceId, List<LogMeal> logs) {
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
        LogAdapterAll.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public LogMeal getItem(int position) {
        return mLogs.get(position);
    }

    CheckBox cbFav;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LogMeal mLog = mLogs.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_log_favorites, parent, false);
        TextView mFoodName = (TextView) v.findViewById(R.id.food_name);
        TextView mBrand = (TextView) v.findViewById(R.id.food_brand);
        mFoodName.setText(mLog.getMealName());
        mBrand.setText(mLog.getBrand());

        cbFav = (CheckBox) v.findViewById(R.id.cbFav);
        if (v != null && cbFav != null) {
            if (mLog.getFavorite().equals("favorite")) {
                cbFav.setChecked(true);
            } else cbFav.setChecked(false);
            cbFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!cbFav.isChecked()) {
                        mLog.setFavorite("false");
                        mLog.edit();
                    } else {
                        mLog.setFavorite("favorite");
                        mLog.edit();
                    }
                }
            });
        }
        return v;
    }
}
