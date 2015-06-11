package com.eugene.fithealthmaingit.Databases_Adapters_ListViews.FoodManual;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eugene.fithealthmaingit.Databases_Adapters_ListViews.LogFood.LogAdapterBreakfast;
import com.eugene.fithealthmaingit.R;
import com.eugene.fithealthmaingit.UI.ManualEntryActivity;
import com.eugene.fithealthmaingit.Utilities.Globals;

import java.text.DecimalFormat;
import java.util.List;

public class LogAdapterManual extends ArrayAdapter<LogManual> {
    Context mContext;
    public static List<LogManual> mLogs;
    String Meal_Type;

    public LogAdapterManual(Context context, int textViewResourceId, List<LogManual> logs, String s) {
        super(context, textViewResourceId);
        mContext = context;
        mLogs = logs;
        Meal_Type = s;
    }

    public void setLogs(List<LogManual> logs) {
        mLogs = logs;
    }

    public List<LogManual> getLogs() {
        return mLogs;
    }

    public void add(LogManual log) {
        mLogs.add(log);
    }

    public void remove(LogManual log) {
        LogAdapterBreakfast.mLogs.remove(log);
    }

    public int getCount() {
        return mLogs.size();
    }

    public LogManual getItem(int position) {
        return mLogs.get(position);
    }

    DecimalFormat df = new DecimalFormat("0");
    LogManual mLogManual;

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_manaual, null, true);
        mLogManual = getItem(position);
        TextView mealName = (TextView) view.findViewById(R.id.mealNameManual);
        mealName.setText(mLogs.get(position).getMealName());

        TextView mealBrand = (TextView) view.findViewById(R.id.mealBrandManual);
        mealBrand.setText(mLogs.get(position).getBrand());

        TextView mealNutrition = (TextView) view.findViewById(R.id.mealNutrition);
        mealNutrition.setText("Cal: " + df.format(mLogManual.getCalorieCount()) + " Fat: " + df.format(mLogManual.getFatCount()) + " Carb: " + df.format(mLogManual.getCarbCount()) + " Pro: " + df.format(mLogManual.getProteinCount()));
        View viewDivider = view.findViewById(R.id.viewDivider);
        if (getCount() - 1 == position) {
            viewDivider.setVisibility(View.GONE);
        } else {
            viewDivider.setVisibility(View.VISIBLE);
        }

        ImageView create = (ImageView) view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ManualEntryActivity.class);
                i.putExtra(Globals.MEAL_TYPE, Meal_Type);
                i.putExtra(Globals.MEAL_ID, mLogManual.getMealId());
                mContext.startActivity(i);
            }
        });
        return view;
    }
}
