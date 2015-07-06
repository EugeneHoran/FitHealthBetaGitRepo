package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.TestingActvity;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class LogAdapterTesting extends ArrayAdapter<LogMeal> {
    private static LogAdapterTesting instance;
    Context mContext;
    public static List<LogMeal> mLogs;

    public LogAdapterTesting(Context context, int textViewResourceId, List<LogMeal> logs) {
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
        LogAdapterTesting.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }


    DecimalFormat df = new DecimalFormat("0");

    public LogMeal getItem(int position) {
        return mLogs.get(position);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_testing, null, true);
        TextView section = (TextView) view.findViewById(R.id.section);
        TextView mealName = (TextView) view.findViewById(R.id.mealName);
        TextView calories = (TextView) view.findViewById(R.id.calories);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        LogAdapterSnack logAdapterSnack = new LogAdapterSnack(mContext, 0, LogMeal.logsByDateSAndMealType(new Date(), "Snack"));
        LogAdapterBreakfast logAdapterBreakfast = new LogAdapterBreakfast(mContext, 0, LogMeal.logsByDateSAndMealType(new Date(), "Breakfast"));
        LogAdapterLunch logAdapterLunch = new LogAdapterLunch(mContext, 0, LogMeal.logsByDateSAndMealType(new Date(), "Lunch"));
        LogAdapterDinner logAdapterDinner = new LogAdapterDinner(mContext, 0, LogMeal.logsByDateSAndMealType(new Date(), "Dinner"));

        if (logAdapterSnack.getCount() > 0) {
            if (position == 0) {
                section.setVisibility(View.VISIBLE);
                section.setText("Snack");
            }
        }
        if (logAdapterBreakfast.getCount() > 0) {
            int snack = logAdapterSnack.getCount();
            if (snack != 0) {
                if (position == snack) {
                    section.setVisibility(View.VISIBLE);
                    section.setText("Breakfast");
                }
            } else {
                if (position == 0) {
                    section.setVisibility(View.VISIBLE);
                    section.setText("Breakfast");
                }
            }
        }

        if (logAdapterLunch.getCount() > 0) {
            int lunch = logAdapterSnack.getCount() + logAdapterBreakfast.getCount();
            if (lunch != 0) {
                if (position == lunch) {
                    section.setVisibility(View.VISIBLE);
                    section.setText("Lunch");
                }
            } else {
                if (position == 0) {
                    section.setVisibility(View.VISIBLE);
                    section.setText("Lunch");
                }
            }
        }

        if (logAdapterDinner.getCount() > 0) {
            int dinner = logAdapterSnack.getCount() + logAdapterBreakfast.getCount() + logAdapterLunch.getCount();
            if (dinner != 0) {
                if (position == dinner) {
                    section.setVisibility(View.VISIBLE);
                    section.setText("Dinner");
                }
            } else {
                if (position == 0) {
                    section.setVisibility(View.VISIBLE);
                    section.setText("Dinner");
                }

            }
        }

        mealName.setText(mLogs.get(position).getMealName());
        calories.setText(df.format(mLogs.get(position).getCalorieCount()));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TestingActvity testingActvity = (TestingActvity) mContext;
                testingActvity.AddItem(position, isChecked);
            }
        });
        return view;
    }
}
